/*
 * Copyright (c) 2016, University of Oslo
 *
 * All rights reserved.
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * Neither the name of the HISP project nor the names of its contributors may
 * be used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.hisp.dhis.client.sdk.android.flow;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.ForeignKeyAction;
import com.raizlabs.android.dbflow.annotation.ForeignKeyReference;
import com.raizlabs.android.dbflow.annotation.Table;

import org.hisp.dhis.client.sdk.android.common.meta.DbDhis;
import org.hisp.dhis.client.sdk.models.program.ProgramType;

import java.util.List;

@Table(databaseName = DbDhis.NAME)
public final class Program$Flow extends BaseIdentifiableObject$Flow {

    static final String TRACKED_ENTITY_KEY = "trackedentity";

    @Column
    @ForeignKey(
            references = {
                    @ForeignKeyReference(columnName = TRACKED_ENTITY_KEY, columnType = String.class, foreignColumnName = "uId"),
            }, saveForeignKeyModel = true, onDelete = ForeignKeyAction.NO_ACTION
    )
    TrackedEntity$Flow trackedEntity;

    @Column
    ProgramType programType;

    @Column
    int version;

    @Column
    String enrollmentDateLabel;

    @Column
    String description;

    @Column
    boolean onlyEnrollOnce;

    @Column
    boolean extenalAccess;

    @Column
    boolean displayIncidentDate;

    @Column
    String incidentDateLabel;

    @Column
    boolean registration;

    @Column
    boolean selectEnrollmentDatesInFuture;

    @Column
    boolean dataEntryMethod;

    @Column
    boolean singleEvent;

    @Column
    boolean ignoreOverdueEvents;

    @Column
    boolean relationshipFromA;

    @Column
    boolean selectIncidentDatesInFuture;

    private List<ProgramStage$Flow> programStages;

    private List<ProgramTrackedEntityAttribute$Flow> programTrackedEntityAttributes;

    public TrackedEntity$Flow getTrackedEntity() {
        return trackedEntity;
    }

    public void setTrackedEntity(TrackedEntity$Flow trackedEntity) {
        this.trackedEntity = trackedEntity;
    }

    public ProgramType getProgramType() {
        return programType;
    }

    public void setProgramType(ProgramType programType) {
        this.programType = programType;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getEnrollmentDateLabel() {
        return enrollmentDateLabel;
    }

    public void setEnrollmentDateLabel(String enrollmentDateLabel) {
        this.enrollmentDateLabel = enrollmentDateLabel;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isOnlyEnrollOnce() {
        return onlyEnrollOnce;
    }

    public void setOnlyEnrollOnce(boolean onlyEnrollOnce) {
        this.onlyEnrollOnce = onlyEnrollOnce;
    }

    public boolean isExtenalAccess() {
        return extenalAccess;
    }

    public void setExtenalAccess(boolean extenalAccess) {
        this.extenalAccess = extenalAccess;
    }

    public boolean isDisplayIncidentDate() {
        return displayIncidentDate;
    }

    public void setDisplayIncidentDate(boolean displayIncidentDate) {
        this.displayIncidentDate = displayIncidentDate;
    }

    public String getIncidentDateLabel() {
        return incidentDateLabel;
    }

    public void setIncidentDateLabel(String incidentDateLabel) {
        this.incidentDateLabel = incidentDateLabel;
    }

    public boolean isRegistration() {
        return registration;
    }

    public void setRegistration(boolean registration) {
        this.registration = registration;
    }

    public boolean isSelectEnrollmentDatesInFuture() {
        return selectEnrollmentDatesInFuture;
    }

    public void setSelectEnrollmentDatesInFuture(boolean selectEnrollmentDatesInFuture) {
        this.selectEnrollmentDatesInFuture = selectEnrollmentDatesInFuture;
    }

    public boolean isDataEntryMethod() {
        return dataEntryMethod;
    }

    public void setDataEntryMethod(boolean dataEntryMethod) {
        this.dataEntryMethod = dataEntryMethod;
    }

    public boolean isSingleEvent() {
        return singleEvent;
    }

    public void setSingleEvent(boolean singleEvent) {
        this.singleEvent = singleEvent;
    }

    public boolean isIgnoreOverdueEvents() {
        return ignoreOverdueEvents;
    }

    public void setIgnoreOverdueEvents(boolean ignoreOverdueEvents) {
        this.ignoreOverdueEvents = ignoreOverdueEvents;
    }

    public boolean isRelationshipFromA() {
        return relationshipFromA;
    }

    public void setRelationshipFromA(boolean relationshipFromA) {
        this.relationshipFromA = relationshipFromA;
    }

    public boolean isSelectIncidentDatesInFuture() {
        return selectIncidentDatesInFuture;
    }

    public void setSelectIncidentDatesInFuture(boolean selectIncidentDatesInFuture) {
        this.selectIncidentDatesInFuture = selectIncidentDatesInFuture;
    }

    public List<ProgramStage$Flow> getProgramStages() {
        return programStages;
    }

    public void setProgramStages(List<ProgramStage$Flow> programStages) {
        this.programStages = programStages;
    }

    public List<ProgramTrackedEntityAttribute$Flow> getProgramTrackedEntityAttributes() {
        return programTrackedEntityAttributes;
    }

    public void setProgramTrackedEntityAttributes(List<ProgramTrackedEntityAttribute$Flow> programTrackedEntityAttributes) {
        this.programTrackedEntityAttributes = programTrackedEntityAttributes;
    }

    public Program$Flow() {
        // empty constructor
    }
}
