/*
 *  Copyright (c) 2016, University of Oslo
 *  * All rights reserved.
 *  *
 *  * Redistribution and use in source and binary forms, with or without
 *  * modification, are permitted provided that the following conditions are met:
 *  * Redistributions of source code must retain the above copyright notice, this
 *  * list of conditions and the following disclaimer.
 *  *
 *  * Redistributions in binary form must reproduce the above copyright notice,
 *  * this list of conditions and the following disclaimer in the documentation
 *  * and/or other materials provided with the distribution.
 *  * Neither the name of the HISP project nor the names of its contributors may
 *  * be used to endorse or promote products derived from this software without
 *  * specific prior written permission.
 *  *
 *  * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 *  * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 *  * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 *  * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 *  * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 *  * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 *  * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *  * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 *  * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 */

package org.hisp.dhis.android.sdk.utils.services;

import org.hisp.dhis.android.sdk.controllers.metadata.MetaDataController;
import org.hisp.dhis.android.sdk.persistence.models.Constant;
import org.hisp.dhis.android.sdk.persistence.models.DataElement;
import org.hisp.dhis.android.sdk.persistence.models.DataValue;
import org.hisp.dhis.android.sdk.persistence.models.Enrollment;
import org.hisp.dhis.android.sdk.persistence.models.Event;
import org.hisp.dhis.android.sdk.persistence.models.ProgramRuleVariable;
import org.hisp.dhis.android.sdk.persistence.models.TrackedEntityAttribute;
import org.hisp.dhis.android.sdk.persistence.models.TrackedEntityAttributeValue;
import org.hisp.dhis.android.sdk.utils.api.ContextVariableType;
import org.hisp.dhis.android.sdk.utils.api.ProgramRuleVariableSourceType;
import org.hisp.dhis.android.sdk.utils.api.ValueType;
import org.hisp.dhis.android.sdk.utils.comparators.EventDateComparator;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.text.TextUtils.isEmpty;

/**
 * Class for handling {@link ProgramRuleVariable}s that can be used in expressions of {@link org.hisp.dhis.android.sdk.persistence.models.ProgramIndicator}
 * and {@link org.hisp.dhis.android.sdk.persistence.models.ProgramRule}s
 */
public class VariableService {

    private static VariableService variableService;

    /**
     * Map of all {@link ProgramRuleVariable}s generated in {@link #initialize(Enrollment, Event)}
     * mapped by key {@link ProgramRuleVariable#getName()}
     */
    private Map<String, ProgramRuleVariable> programRuleVariableMap;

    /**
     * List of all {@link Event}s linked to the {@link Enrollment} passed in {@link #initialize(Enrollment, Event)}
     */
    private List<Event> eventsForEnrollment;

    /**
     * The current {@link Enrollment} set in {@link #initialize(Enrollment, Event)}
     */
    private Enrollment currentEnrollment;

    /**
     * The current {@link Event} set in {@link #initialize(Enrollment, Event)}
     */
    private Event currentEvent;

    /**
     * Map of all {@link DataElement}s that are contained in either {@link org.hisp.dhis.android.sdk.persistence.models.ProgramStage}
     * for the {@link org.hisp.dhis.android.sdk.persistence.models.Program} of the {@link Enrollment}
     * passed in {@link #initialize(Enrollment, Event)}. The key is the Uid of the {@link DataElement}
     */
    private Map<String, DataElement> dataElementMap;

    /**
     * Map of all {@link TrackedEntityAttribute} for the {@link org.hisp.dhis.android.sdk.persistence.models.Program}
     * of the {@link Enrollment} passed in {@link #initialize(Enrollment, Event)}.
     * The key is the Uid of the {@link TrackedEntityAttribute}
     */
    private Map<String, TrackedEntityAttribute> trackedEntityAttributeMap;

    /**
     * Map of {@link List}s of {@link Event}s for each {@link org.hisp.dhis.android.sdk.persistence.models.ProgramStage}
     * of the {@link org.hisp.dhis.android.sdk.persistence.models.Program} of the {@link Enrollment}
     * passed in {@link #initialize(Enrollment, Event)}. The key is the Uid of the {@link org.hisp.dhis.android.sdk.persistence.models.ProgramStage}
     */
    private Map<String, List<Event>> eventsForProgramStages;

    /**
     * Map of all {@link DataValue}s for each {@link Event} for either the {@link Event} passed in
     * {@link #initialize(Enrollment, Event)}, or for the {@link Event}s of the {@link Enrollment}
     * passed in {@link #initialize(Enrollment, Event)}.
     */
    private Map<Event, Map<String, DataValue>> eventDataValueMaps;

    static {
        variableService = new VariableService();
    }

    public static VariableService getInstance() {
        return variableService;
    }

    public static void reset() {
        getInstance().programRuleVariableMap = null;
        getInstance().eventsForEnrollment = null;
        getInstance().currentEnrollment = null;
        getInstance().currentEvent = null;
        getInstance().dataElementMap = null;
        getInstance().trackedEntityAttributeMap = null;
        getInstance().eventsForEnrollment = null;
        getInstance().eventDataValueMaps = null;
    }

    /**
     * Initializes the VariableService so that it can be used for calculating Indicators and Program Rules
     * This method generates a set of ProgramRuleVariables, that are later used in expression calculations
     * from {@link org.hisp.dhis.android.sdk.utils.support.math.ExpressionFunctions},
     * {@link ProgramRuleService}, and {@link ProgramIndicatorService}
     *
     * Note that you may have to reset this singleton by calling {@link #reset()}
     * @param enrollment can be null
     * @param currentEvent can be null
     */
    public static void initialize(Enrollment enrollment, Event currentEvent) {
        //setting current enrollment and event
        getInstance().setCurrentEnrollment(enrollment);
        getInstance().setCurrentEvent(currentEvent);

        //setting list of events for enrollment
        List<Event> events;
        if(getInstance().getCurrentEnrollment() != null) {
            events = getInstance().getCurrentEnrollment().getEvents();
        } else {
            events = new ArrayList<>();
        }
        getInstance().setEventsForEnrollment(events);
        if(getInstance().getCurrentEvent() != null &&
                !getInstance().getEventsForEnrollment().contains(getInstance().getCurrentEvent())) {
            getInstance().getEventsForEnrollment().add(getInstance().getCurrentEvent());
        }
        Collections.sort(getInstance().getEventsForEnrollment(), new EventDateComparator());

        //setting data elements map
        List<DataElement> dataElements = MetaDataController.getDataElements();
        getInstance().setDataElementMap(new HashMap<String, DataElement>());
        for(DataElement dataElement : dataElements) {
            getInstance().getDataElementMap().put(dataElement.getUid(), dataElement);
        }

        //setting trackedEntityAttribute map
        List<TrackedEntityAttribute> trackedEntityAttributes = MetaDataController.getTrackedEntityAttributes();
        getInstance().setTrackedEntityAttributeMap(new HashMap<String, TrackedEntityAttribute>());
        for(TrackedEntityAttribute trackedEntityAttribute : trackedEntityAttributes) {
            getInstance().getTrackedEntityAttributeMap().put(trackedEntityAttribute.getUid(), trackedEntityAttribute);
        }

        //setting events in map for each program stage
        getInstance().setEventsForProgramStages(new HashMap<String, List<Event>>());
        for(Event event : getInstance().getEventsForEnrollment()) {
            List<Event> eventsForProgramStage = getInstance().getEventsForProgramStages().get(event.getProgramStageId());
            if(eventsForProgramStage == null) {
                eventsForProgramStage = new ArrayList<>();
                getInstance().getEventsForProgramStages().put(event.getProgramStageId(), eventsForProgramStage);
            }
            eventsForProgramStage.add(event);
        }

        //setting data values in map for each event
        getInstance().setEventDataValueMaps(new HashMap<Event, Map<String, DataValue>>());
        for(Event event : getInstance().getEventsForEnrollment()) {
            Map<String, DataValue> dataValueMap = new HashMap<>();
            for(DataValue dataValue : event.getDataValues()) {
                dataValueMap.put(dataValue.getDataElement(), dataValue);
            }
            getInstance().getEventDataValueMaps().put(event, dataValueMap);
        }

        //setting programRuleVariables
        List<ProgramRuleVariable> programRuleVariables = MetaDataController.getProgramRuleVariables();
        populateDataElementAndAttributeVariables(programRuleVariables);
        programRuleVariables.addAll(createContextVariables());
        programRuleVariables.addAll(createConstantVariables());
        Map<String, ProgramRuleVariable> programRuleVariableMap = new HashMap<>();
        for(ProgramRuleVariable programRuleVariable : programRuleVariables) {
            programRuleVariableMap.put(programRuleVariable.getName(), programRuleVariable);
        }
        getInstance().setProgramRuleVariableMap(programRuleVariableMap);
    }

    public Map<String, ProgramRuleVariable> getProgramRuleVariableMap() {
        return programRuleVariableMap;
    }

    public static void setProgramRuleVariableMap(Map<String, ProgramRuleVariable> programRuleVariableMap) {
        getInstance().programRuleVariableMap = programRuleVariableMap;
    }

    public List<Event> getEventsForEnrollment() {
        return eventsForEnrollment;
    }

    public void setEventsForEnrollment(List<Event> eventsForEnrollment) {
        this.eventsForEnrollment = eventsForEnrollment;
    }

    public Enrollment getCurrentEnrollment() {
        return currentEnrollment;
    }

    public void setCurrentEnrollment(Enrollment currentEnrollment) {
        this.currentEnrollment = currentEnrollment;
    }

    public Event getCurrentEvent() {
        return currentEvent;
    }

    public void setCurrentEvent(Event currentEvent) {
        this.currentEvent = currentEvent;
    }

    public Map<String, DataElement> getDataElementMap() {
        return dataElementMap;
    }

    public void setDataElementMap(Map<String, DataElement> dataElementMap) {
        this.dataElementMap = dataElementMap;
    }

    public Map<String, TrackedEntityAttribute> getTrackedEntityAttributeMap() {
        return trackedEntityAttributeMap;
    }

    public void setTrackedEntityAttributeMap(Map<String, TrackedEntityAttribute> trackedEntityAttributeMap) {
        this.trackedEntityAttributeMap = trackedEntityAttributeMap;
    }

    public Map<String, List<Event>> getEventsForProgramStages() {
        return eventsForProgramStages;
    }

    public void setEventsForProgramStages(Map<String, List<Event>> eventsForProgramStages) {
        this.eventsForProgramStages = eventsForProgramStages;
    }

    public Map<Event, Map<String, DataValue>> getEventDataValueMaps() {
        return eventDataValueMaps;
    }

    public void setEventDataValueMaps(Map<Event, Map<String, DataValue>> eventDataValueMaps) {
        this.eventDataValueMaps = eventDataValueMaps;
    }

    /**
     * Processes a value to make sure the actually content corresponds to the {@link ValueType},
     * so that expression calculations can be done without errors.
     * @param processedValue
     * @param valueType
     * @return
     */
    public static String processSingleValue(String processedValue, ValueType valueType){
        switch (valueType) {
            case LONG_TEXT:
            case TEXT:
            case DATE:
                //Append single quotation marks in case the variable is of text or date type:
                if(processedValue != null) {
                    processedValue = "'" + processedValue + "'";
                } else {
                    processedValue = "''";
                }
                break;

            case BOOLEAN:
            case TRUE_ONLY:
                if(processedValue == null || processedValue.isEmpty() || !processedValue.equals("true")) {
                    processedValue = "false";
                }
                break;

            case INTEGER:
            case NUMBER:
            case INTEGER_POSITIVE:
            case INTEGER_NEGATIVE:
            case INTEGER_ZERO_OR_POSITIVE:
            case PERCENTAGE:
                if(processedValue != null) {
                    processedValue = String.valueOf(Double.valueOf(processedValue));
                } else {
                    processedValue = "0";
                }
                break;
        }

        return processedValue;
    }

    /**
     * Populates the passed {@link ProgramRuleVariable}s with data based on its {@link ProgramRuleVariableSourceType}
     * The values that are populated in the {@link ProgramRuleVariable}s are either taken from a {@link DataValue} or
     * from a {@link TrackedEntityAttributeValue}.
     * @param programRuleVariables
     */
    private static void populateDataElementAndAttributeVariables(List<ProgramRuleVariable> programRuleVariables) {
        for(ProgramRuleVariable programRuleVariable : programRuleVariables) {
            populateDataElementOrAttributeVariable(programRuleVariable);
        }
    }

    /**
     * Populates the passed {@link ProgramRuleVariable} with data based on its {@link ProgramRuleVariableSourceType}
     * The value that is populated in the {@link ProgramRuleVariable} is either taken from a {@link DataValue} or
     * from a {@link TrackedEntityAttributeValue}.
     * @param programRuleVariable
     */
    private static void populateDataElementOrAttributeVariable(ProgramRuleVariable programRuleVariable) {
        String value = null;
        String defaultValue = "";
        List<String> allValues = new ArrayList<>();
        if(programRuleVariable.getSourceType() == null) {
            return;
        }

        switch (programRuleVariable.getSourceType()) {
            case CONSTANT:
            case CALCULATED_VALUE:
                break;
            case DATAELEMENT_NEWEST_EVENT_PROGRAM: {
                DataValue dataValue;
                for (Event event : getInstance().getEventsForEnrollment()) {
                    dataValue = getInstance().getEventDataValueMaps().get(event).get(programRuleVariable.getDataElement());
                    if (dataValue != null) {
                        if(value == null) {
                            value = dataValue.getValue();
                        }
                        allValues.add(dataValue.getValue());
                    }
                }
                break;
            }
            case DATAELEMENT_NEWEST_EVENT_PROGRAM_STAGE: {
                DataValue dataValue = null;
                String programStage = programRuleVariable.getProgramStage();
                if(programStage != null) {
                    List<Event> eventsForProgramStage = getInstance().getEventsForProgramStages().get(programStage);
                    //in some cases the program stage may not have been loaded onto the device, although all variables have
                    //in this case we the current variable doesn't matter as it is from a different program,
                    //so we can ignore it.
                    if(eventsForProgramStage != null) {
                        for (Event event : eventsForProgramStage) {
                            dataValue = getInstance().getEventDataValueMaps().get(event).get(programRuleVariable.getDataElement());
                            if (dataValue != null) {
                                if(value == null) {
                                    value = dataValue.getValue();
                                }
                                allValues.add(dataValue.getValue());
                            }
                        }
                        if (dataValue != null) {
                            value = dataValue.getValue();
                        } else {
                            DataElement dataElement = getInstance().getDataElementMap().get(programRuleVariable.getDataElement());
                            defaultValue = getDefaultValue(dataElement.getValueType());
                        }
                    }
                }
                break;
            }
            case DATAELEMENT_PREVIOUS_EVENT: {
                if(getInstance().getCurrentEvent() != null) {
                    DataValue dataValue;
                    Comparator<Event> comparator = new EventDateComparator();
                    //select a value from an event that precedes 'currentEvent'
                    for (Event event : getInstance().getEventsForEnrollment()) {
                        if(event.getUid().equals(getInstance().getCurrentEvent().getUid())) {
                            continue;
                        }
                        //if currentEvent is later than 'event'
                        if (comparator.compare(getInstance().getCurrentEvent(), event) > 0) {
                            dataValue = getInstance().getEventDataValueMaps().get(event).get(programRuleVariable.getDataElement());
                            if (dataValue != null) {
                                if (value == null) {
                                    value = dataValue.getValue();
                                }
                                allValues.add(dataValue.getValue());
                            }
                        }
                    }
                }
                break;
            }
            case TEI_ATTRIBUTE: {
                if(getInstance().getCurrentEnrollment() != null) {
                    TrackedEntityAttributeValue trackedEntityAttributeValue = null;
                    for (TrackedEntityAttributeValue storedValue : getInstance().getCurrentEnrollment().getAttributes()) {
                        if (storedValue.getTrackedEntityAttributeId().equals(programRuleVariable.getTrackedEntityAttribute())) {
                            trackedEntityAttributeValue = storedValue;
                            break;
                        }
                    }
                    if (trackedEntityAttributeValue != null) {
                        value = trackedEntityAttributeValue.getValue();
                        allValues.add(value);
                    }
                }
                break;
            }
            default:
            case DATAELEMENT_CURRENT_EVENT: {
                if(getInstance().getCurrentEvent() != null) {
                    DataValue dataValue = getInstance().getEventDataValueMaps().get(getInstance().getCurrentEvent()).get(programRuleVariable.getDataElement());
                    if (dataValue != null) {
                        value = dataValue.getValue();
                        allValues.add(value);
                    }
                }
                break;
            }
        }
        if(isEmpty(value)) {
            programRuleVariable.setHasValue(false);
            value = defaultValue;
        } else {
            programRuleVariable.setHasValue(true);
        }
        programRuleVariable.setVariableValue(value);
        programRuleVariable.setAllValues(allValues);
    }

    /**
     * Returns a list of populated {@link ProgramRuleVariable}s based on all {@link Constant}
     * currently stored in the database.
     * @return
     */
    private static List<ProgramRuleVariable> createConstantVariables() {
        List<Constant> constants = MetaDataController.getConstants();
        List<ProgramRuleVariable> programRuleVariables = new ArrayList<>();
        for(Constant constant : constants) {
            programRuleVariables.add(createConstantVariable(constant));
        }
        return programRuleVariables;
    }

    /**
     * Returns a populated {@link ProgramRuleVariable} based on a {@link Constant}.
     * @param constant
     * @return
     */
    private static ProgramRuleVariable createConstantVariable(Constant constant) {
        if(constant != null) {
            ProgramRuleVariable programRuleVariable = new ProgramRuleVariable();
            programRuleVariable.setVariableValue(Double.toString(constant.getValue()));
            programRuleVariable.setHasValue(true);
            programRuleVariable.setName(constant.getName());
            programRuleVariable.setSourceType(ProgramRuleVariableSourceType.CONSTANT);
            return programRuleVariable;
        } else {
            return null;
        }
    }

    /**
     * Generates and returns a {@link List} of populated {@link ProgramRuleVariable} for the context set in {@link #initialize(Enrollment, Event)}.
     * Context variables are a set of pre-defined {@link ProgramRuleVariable}s that the user can use when writing expressions.
     * @return
     */
    private static List<ProgramRuleVariable> createContextVariables() {
        List<ProgramRuleVariable> programRuleVariables = new ArrayList<>();
        for(ContextVariableType type : ContextVariableType.values()) {
            programRuleVariables.add(createContextVariable(type.toString(),
                    getInstance().getCurrentEvent(), getInstance().getCurrentEnrollment(),
                    getInstance().getEventsForEnrollment()));
        }
        return programRuleVariables;
    }

    /**
     * Generates and returns a populated {@link ProgramRuleVariable} for the context of the passed parameters.
     * Context variables are a set of pre-defined {@link ProgramRuleVariable}s that the user can use when writing expressions.
     * @param variableName
     * @param executingEvent
     * @param executingEnrollment
     * @param events
     * @return
     */
    private static ProgramRuleVariable createContextVariable(String variableName, Event executingEvent, Enrollment executingEnrollment, List<Event> events) {
        ContextVariableType contextVariableType = ContextVariableType.fromValue(variableName);
        ProgramRuleVariable programRuleVariable = new ProgramRuleVariable();
        programRuleVariable.setName(variableName);
        programRuleVariable.setSourceType(ProgramRuleVariableSourceType.CALCULATED_VALUE);
        switch (contextVariableType) {
            case CURRENT_DATE:
                programRuleVariable.setVariableValue(DateTime.now().toString());
                programRuleVariable.setHasValue(true);
                programRuleVariable.setVariableType(ValueType.DATE);
                break;
            case EVENT_DATE:
                if(executingEvent != null) {
                    programRuleVariable.setVariableValue(executingEvent.getEventDate());
                    programRuleVariable.setHasValue(true);
                    programRuleVariable.setVariableType(ValueType.DATE);
                } else {
                    programRuleVariable.setHasValue(false);
                    programRuleVariable.setVariableValue("");
                    programRuleVariable.setVariableType(ValueType.DATE);
                }
                break;
            case DUE_DATE:
                if(executingEvent != null) {
                    programRuleVariable.setVariableValue(executingEvent.getDueDate());
                    programRuleVariable.setHasValue(true);
                    programRuleVariable.setVariableType(ValueType.DATE);
                } else {
                    programRuleVariable.setHasValue(false);
                    programRuleVariable.setVariableValue("");
                    programRuleVariable.setVariableType(ValueType.DATE);
                }
                break;
            case EVENT_COUNT:
                programRuleVariable.setVariableValue(Integer.toString(events.size()));
                programRuleVariable.setHasValue(true);
                programRuleVariable.setVariableType(ValueType.INTEGER);
                break;
            case ENROLLMENT_DATE:
                if(executingEnrollment != null) {
                    programRuleVariable.setVariableValue(executingEnrollment.getEnrollmentDate());
                    programRuleVariable.setHasValue(true);
                    programRuleVariable.setVariableType(ValueType.DATE);
                } else {
                    programRuleVariable.setHasValue(false);
                    programRuleVariable.setVariableValue("");
                    programRuleVariable.setVariableType(ValueType.DATE);
                }
                break;
            case ENROLLMENT_ID:
                if(executingEnrollment != null) {
                    programRuleVariable.setVariableValue(executingEnrollment.getEnrollment());
                    programRuleVariable.setHasValue(true);
                    programRuleVariable.setVariableType(ValueType.TEXT);
                } else {
                    programRuleVariable.setHasValue(false);
                    programRuleVariable.setVariableValue("");
                    programRuleVariable.setVariableType(ValueType.TEXT);
                }
                break;
            case EVENT_ID:
                if(executingEvent != null) {
                    programRuleVariable.setVariableValue(executingEvent.getEvent());
                    programRuleVariable.setHasValue(true);
                    programRuleVariable.setVariableType(ValueType.TEXT);
                } else {
                    programRuleVariable.setHasValue(false);
                    programRuleVariable.setVariableValue("");
                    programRuleVariable.setVariableType(ValueType.TEXT);
                }
                break;
            case INCIDENT_DATE:
                if(executingEnrollment != null) {
                    programRuleVariable.setVariableValue(executingEnrollment.getIncidentDate());
                    programRuleVariable.setHasValue(true);
                    programRuleVariable.setVariableType(ValueType.DATE);
                } else {
                    programRuleVariable.setHasValue(false);
                    programRuleVariable.setVariableValue("");
                    programRuleVariable.setVariableType(ValueType.DATE);
                }
                break;
            case ENROLLMENT_COUNT: {
                if(executingEnrollment != null) {
                    programRuleVariable.setVariableValue("1");
                    programRuleVariable.setHasValue(true);
                    programRuleVariable.setVariableType(ValueType.INTEGER);
                } else {
                    programRuleVariable.setVariableValue("0");
                    programRuleVariable.setHasValue(false);
                    programRuleVariable.setVariableType(ValueType.INTEGER);
                }
                break;
            }
            case TEI_COUNT: {
                if(executingEnrollment != null) {
                    programRuleVariable.setVariableValue("1");
                    programRuleVariable.setHasValue(true);
                    programRuleVariable.setVariableType(ValueType.INTEGER);
                } else {
                    programRuleVariable.setVariableValue("0");
                    programRuleVariable.setHasValue(false);
                    programRuleVariable.setVariableType(ValueType.INTEGER);
                }
                break;
            }
        }
        return programRuleVariable;
    }

    /**
     * Returns the value of a {@link ProgramRuleVariable}
     * @param variableName
     * @return
     */
    public static String getReplacementForProgramRuleVariable(String variableName) {
        String value;
        ProgramRuleVariable programRuleVariable = VariableService.getInstance().getProgramRuleVariableMap().get(variableName);
        value = retrieveVariableValue(programRuleVariable);
        return value;
    }

    /**
     * Returns the value of a {@link ProgramRuleVariable}
     * @param programRuleVariable
     * @return
     */
    public static String retrieveVariableValue(ProgramRuleVariable programRuleVariable) {
        String defaultValue = "";
        if (programRuleVariable == null) {
            return defaultValue;
        }
        String value = programRuleVariable.getVariableValue();
        if(isEmpty(value)) {
            value = defaultValue;
        }
        return value;
    }

    /**
     * Returns a valid default value for a given {@link ValueType}
     * @param valueType
     * @return
     */
    public static String getDefaultValue(ValueType valueType) {
        String value;
        switch (valueType) {
            case BOOLEAN:
            case TRUE_ONLY:
                value = "false";
                break;
            case INTEGER_POSITIVE:
            case INTEGER:
            case INTEGER_ZERO_OR_POSITIVE:
            case INTEGER_NEGATIVE:
            case NUMBER:
            case PERCENTAGE:
            case UNIT_INTERVAL:
                value = "0";
                break;
            case DATE:
            case DATETIME:
            case LONG_TEXT:
            case TEXT:
                value = "";
                break;
            default: value = "";
        }
        return value;
    }
}
