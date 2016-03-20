package org.hisp.dhis.android.sdk.sms;

import android.util.Log;

import org.hisp.dhis.android.sdk.persistence.models.OrganisationUnit;
import org.hisp.dhis.android.sdk.persistence.models.Program;
import org.hisp.dhis.android.sdk.persistence.models.TrackedEntityAttributeValue;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by premer on 27.02.16.
 */
public class SMSComposer {
    private static final String CLASS_TAG = "SMSComposer";

    public static String composeSMS(OrganisationUnit orgUnit,  Program program, Map<String, TrackedEntityAttributeValue> teiMap, String receiver){
        Log.d(CLASS_TAG, "Composing message to notify superiors");
        String message = String.format("Hi %s, new case registered under the %s program at %s",
                receiver, program.getName(), orgUnit.getLabel());
        SMSNotification.log("SMSComposer : msg = " + message);

        return message;
    }
}
