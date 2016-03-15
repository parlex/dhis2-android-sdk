package org.hisp.dhis.android.sdk.sms;

import android.util.Log;

import org.hisp.dhis.android.sdk.controllers.metadata.MetaDataController;
import org.hisp.dhis.android.sdk.persistence.models.Enrollment;
import org.hisp.dhis.android.sdk.persistence.models.Event;
import org.hisp.dhis.android.sdk.persistence.models.OrganisationUnit;
import org.hisp.dhis.android.sdk.persistence.models.OrganisationUnitContactInfo;
import org.hisp.dhis.android.sdk.persistence.models.Program;
import org.hisp.dhis.android.sdk.persistence.models.TrackedEntityInstance;

import java.util.Iterator;
import java.util.List;

/**
 * Created by Alexander Premer on 8/3/2015.
 */
public class SMSNotification {
    private static final String CLASS_TAG = "SMSNotification";

    public static void sendSMSNotification(Enrollment enrollment) {
        //SMSSender.sendSms(SMSParse.onNotify(enrollment));
    }

    public static void sendSMSNotification(Event event) {
        //SMSSender.sendSms(SMSParse.onNotify(event));
    }

    public static void sendSMSNotification(TrackedEntityInstance trackedEntityInstance) {
        //SMSSender.sendSms(SMSParse.onNotify(trackedEntityInstance));
    }

    public static void sensSMSNotification(OrganisationUnit orgUnit, Program program){
        SMSComposer.composeSMS(orgUnit, program);
        //SMSSender.sendSMS(SMSComposer.composeSMS(orgUnit, program), orgUnit.getContactPerson(), orgUnit.getPhoneNumber());
        List<OrganisationUnitContactInfo> orgInfo = MetaDataController.getOrgUnitContactInfo("pIUx4ola8QM");
        Iterator<OrganisationUnitContactInfo> it = orgInfo.iterator();
        while(it.hasNext()){
            OrganisationUnitContactInfo o = it.next();
            SMSNotification.log("id fra telefonkatalogen: " + o.getId());
        }

    }

    public static void log(String msg){
        Log.d(CLASS_TAG, msg);
    }

}
