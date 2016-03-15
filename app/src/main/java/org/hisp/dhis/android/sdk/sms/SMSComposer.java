package org.hisp.dhis.android.sdk.sms;

import android.util.Log;

import org.hisp.dhis.android.sdk.persistence.models.OrganisationUnit;
import org.hisp.dhis.android.sdk.persistence.models.Program;

/**
 * Created by premer on 27.02.16.
 */
public class SMSComposer {
    private static final String CLASS_TAG = "SMSComposer";

    public static String composeSMS(OrganisationUnit orgUnit,  Program program){
        Log.d(CLASS_TAG, "Composing message to notify superiors");
        String message = String.format("New case registered under the %s program at %s",
                program.getName(), orgUnit.getLabel());
        SMSNotification.log("SMSComposer : msg = " + message);

        return message;
    }
}
