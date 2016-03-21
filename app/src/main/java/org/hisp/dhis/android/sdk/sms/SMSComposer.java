package org.hisp.dhis.android.sdk.sms;

import android.util.Log;

import org.hisp.dhis.android.sdk.controllers.metadata.MetaDataController;
import org.hisp.dhis.android.sdk.persistence.models.OrganisationUnit;
import org.hisp.dhis.android.sdk.persistence.models.Program;
import org.hisp.dhis.android.sdk.persistence.models.TrackedEntityAttribute;
import org.hisp.dhis.android.sdk.persistence.models.TrackedEntityAttributeValue;
import org.hisp.dhis.android.sdk.utils.Utils;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by premer on 27.02.16.
 */
public class SMSComposer {
    private static final String CLASS_TAG = "SMSComposer";

    /**
     * Composes a message to send to health officers with data about a new enrollment
     * @param orgUnit Name of the orgunit the person was enrolled in
     * @param program Program of the enrollment
     * @param teiMap Tracked Entety Attribute value map to extract values from form
     * @param receiver The receiver of the message
     * @return Formatted message to ready to send
     */
    public static String composeSMS(String orgUnit,  Program program, Map<String, TrackedEntityAttributeValue> teiMap, String receiver, String incidentDate){
        Log.d(CLASS_TAG, "Composing message to notify superiors");

        String gender = "";
        String dob = "";

        for (String key: teiMap.keySet()) {
            TrackedEntityAttribute tei = MetaDataController.getTrackedEntityAttribute(key);
            if(tei.getName().equalsIgnoreCase("Sex")){
                gender = teiMap.get(key).getValue();
            }else if (tei.getName().equalsIgnoreCase("Date of Birth")){
                dob = teiMap.get(key).getValue();
            }
            Log.d(CLASS_TAG, "Key: " + key + "\tValue: " + teiMap.get(key).getValue() + "\tName: " + tei.getName());
        }

        return String.format("Hi %s, a %d year old %s enrolled in %s program at %s, incident date: %s",
                receiver, getAge(dob), gender, program.getName(), orgUnit, Utils.removeTimeFromDateString(incidentDate));
    }

    /**
     * Returns amount of years from date of birth untill todays date
     * @param dob String with date of birth in format yyyy-MM-dd
     * @return amount of years from dob until today
     */
    public static int getAge(String dob){
        DateTime dateOfBirth = DateTimeFormat.forPattern("yyyy-MM-dd").parseDateTime(dob);
        DateTime today = new DateTime();

        Period period = new Period(dateOfBirth,today);

        return period.getYears();
    }
}