package org.hisp.dhis.android.sdk.sms;

import android.util.Log;
import android.util.Pair;

import org.hisp.dhis.android.sdk.controllers.metadata.MetaDataController;
import org.hisp.dhis.android.sdk.persistence.models.Enrollment;
import org.hisp.dhis.android.sdk.persistence.models.Event;
import org.hisp.dhis.android.sdk.persistence.models.OrganisationUnit;
import org.hisp.dhis.android.sdk.persistence.models.OrganisationUnitContactInfo;
import org.hisp.dhis.android.sdk.persistence.models.Program;
import org.hisp.dhis.android.sdk.persistence.models.TrackedEntity;
import org.hisp.dhis.android.sdk.persistence.models.TrackedEntityAttributeValue;
import org.hisp.dhis.android.sdk.persistence.models.TrackedEntityInstance;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Alexander Premer on 8/3/2015.
 */
public class SMSNotification {
    private static final String CLASS_TAG = "SMSNotification";

    public static void sendSMSNotification(OrganisationUnit orgUnit, Program program, Map<String, TrackedEntityAttributeValue> teiMap){
        OrganisationUnitContactInfo contactDetails = MetaDataController.getOrgUnitContactInfo(orgUnit.getId());
        if(contactDetails != null && contactDetails.getContactName() != null && contactDetails.getContactNo() != null) {
            SMSSender.sendSMS(SMSComposer.composeSMS(contactDetails.getShortName(), program, teiMap, contactDetails.getContactName()), contactDetails.getContactNo());
        }else{
            log("Could not find contactinfo");
        }
    }

    public static void log(String msg){
        Log.d(CLASS_TAG, msg);
    }

}
