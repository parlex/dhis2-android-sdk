//package org.hisp.dhis.android.sdk.sms;
//
//import android.provider.ContactsContract;
//import android.util.Log;
//
//import org.hisp.dhis.android.sdk.controllers.datavalues.DataValueController;
//import org.hisp.dhis.android.sdk.controllers.metadata.MetaDataController;
//import org.hisp.dhis.android.sdk.network.http.ApiRequestCallback;
//import org.hisp.dhis.android.sdk.persistence.models.DataElement;
//import org.hisp.dhis.android.sdk.persistence.models.DataValue;
//import org.hisp.dhis.android.sdk.persistence.models.Enrollment;
//import org.hisp.dhis.android.sdk.persistence.models.Event;
//import org.hisp.dhis.android.sdk.persistence.models.Option;
//import org.hisp.dhis.android.sdk.persistence.models.Program;
//import org.hisp.dhis.android.sdk.persistence.models.TrackedEntity;
//import org.hisp.dhis.android.sdk.persistence.models.TrackedEntityAttribute;
//import org.hisp.dhis.android.sdk.persistence.models.TrackedEntityAttributeValue;
//import org.hisp.dhis.android.sdk.persistence.models.TrackedEntityInstance;
//
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.List;
//
///**
// * Created by Alexander on 7/28/2015.
// */
//public class SMSParse {
//    private static final String APPIDENTIFIER = "trackercapture";
//    private static final String CLASS_TAG = "SMSParse";
//
//    public static String onSendEvent(Event event){
//        Log.d(CLASS_TAG, "Composing message from event");
//        String sep = ":";
//        String message = String.format("p=%s%so=%s%sps=%s%sdD=%s%seD=%s%s", event.getProgramId(),
//                sep, event.getOrganisationUnitId(), sep, event.getProgramStageId(), sep,
//                event.getDueDate(), sep, event.getEventDate(), sep);
//
//        if(event.getLatitude() != null && event.getLongitude() != null) {
//            message += "l=" + Math.round(event.getLatitude()) + ":";
//            message += "lo=" + Math.round(event.getLongitude()) + ":d=";
//        }
//
//        message += addDataValuesToSMS(event.getDataValues());
//
//        return message.substring(0, message.length() - 1);
//    }
//
//    public static String addDataValuesToSMS(List<DataValue> dataValues){
//        String message = "";
//        for(DataValue dataValue : dataValues){
//            //Gets the dataelement to extract the options for the element
//            //in order to create smaller messages.
//            DataElement dataElm = MetaDataController.getDataElement(dataValue.getDataElement());
//            List<Option> dataValueOptions = MetaDataController.getOptions(dataElm.getOptionSet());
//
//            if(dataValueOptions != null) {
//                message += addDataValuesToSMS(dataValueOptions, dataValue);
//            }else{
//                Log.d(CLASS_TAG, "No optionset for dataelement");
//                //message += dataValueCnt + "!" + d.getValue() + ",";
//            }
//        }
//
//        return message;
//    }
//
//    public static String addDataValuesToSMS(List<Option> dataValueOptions, DataValue dataValue){
//        String message = "";
//        int cnt = 0;
//        // If dataelement doesn't have a optionset
//        if(dataValueOptions.size() < 1){
//            message += dataValue.getDataElement() + "!" + dataValue.getValue() + ",";
//        }
//
//        for (Option o : dataValueOptions) {
//            Log.d(CLASS_TAG, "Option: " + o.getName() + " has number = " + cnt);
//            if(o.getName().equalsIgnoreCase(dataValue.getValue())){
//                message += dataValue.getDataElement() + "!" + cnt + ",";
//            }
//            cnt++;
//        }
//        return message;
//    }
//
//    /* The format the Json on the  server should end up looking like
//
//            {    "trackedEntityInstance": "ZRyCnJ1qUXS",
//                    "program": "S8uo8AlvYMz",
//                    "dateOfEnrollment": "2013-09-17",
//                    "dateOfIncident": "2013-09-17"
//            }
//    */
//    public static String onSendEnrollment(ApiRequestCallback callback, Enrollment enrollment){
//        String tei = enrollment.getTrackedEntityInstance();
//        String sep = ":";
//
//        if(tei != null){
//            // e.g 2013-09-17:2013-09-17:IpHINAT79UW:cyl5vuJ5ETQ:1
//            callback.onSuccess(null);
//            return enrollment.getDateOfIncident() + sep +
//                    enrollment.getDateOfEnrollment() + sep +
//                    enrollment.getProgram().getId() + sep + tei + sep + enrollment.getLocalId();
//        }else{
//            callback.onFailure(null);
//            Log.d(CLASS_TAG, "Tei is not registered yet, postponing enrollment");
//        }
//        return null;
//    }
//
//    public static String onSendTEI(TrackedEntityInstance trackedEntityInstance){
//        String message = "mpe ";
//        message += new SimpleDateFormat("MM/dd").format(new Date()) + " ";
//        //TODO When these values can be fetched from web api this needs to be done, rather than hardcoding the values
//        for(TrackedEntityAttributeValue teiav : trackedEntityInstance.getAttributes()){
//            switch (teiav.getTrackedEntityAttributeId()){
//                case "Al9Qz1TYrAU":
//                    message += "fn." + teiav.getValue() + ".";
//                    break;
//                case "W2OSxivVRTZ":
//                    message += "ln." + teiav.getValue() + ".";
//                    break;
//                case "QEqEOJ34neT":
//                    SimpleDateFormat fromSys = new SimpleDateFormat("yyyy-MM-dd");
//                    SimpleDateFormat myformat = new SimpleDateFormat("yyyyMMdd");
//                    try{
//                        message += "dob." + myformat.format(fromSys.parse(teiav.getValue())) + ".";
//                    }catch (ParseException e){
//                        e.printStackTrace();
//                    }
//                    break;
//                case "P4MG4QOyNeK":
//                    message += "sex." + teiav.getValue() + ".";
//                    break;
//                case "jTB52d16MLj":
//                    message += "nr." + teiav.getValue() + ".";
//                    break;
//                case "hrKJXrHrBy8":
//                    message += "tadr." + teiav.getValue() + ".";
//                    break;
//                case "JOCLGJiKEc9":
//                    message += "ctv." + teiav.getValue() + ".";
//                    break;
//                case "ycAOULCq7iR":
//                    message += "adr." + teiav.getValue() + ".";
//                    break;
//                case "TOe7i8vkN4i":
//                    message += "hn." + teiav.getValue() + ".";
//                    break;
//                case "sfDpeZppyhE":
//                    message += "w." + teiav.getValue() + ".";
//                    break;
//                case "FHWw3DJ5FS8":
//                    message += "b."+ teiav.getValue() + ".";
//                    break;
//                default:
//                    Log.d(CLASS_TAG, "Didn't find Tracked entity Attr, Something went wrong..");
//                    break;
//            }
//        }
//        // To get rid of last stop char.
//        return message.substring(0,message.length()-1) + " id." + trackedEntityInstance.getLocalId();
//    }
//
//    // TODO make these methods generic, doesn't seem like this is very nice looking...
//    public static void onReceiveEventStatus(int localID, String serverRef){
//        Event event = DataValueController.getEvent(localID);
//
//        if(event != null){
//            Log.d(CLASS_TAG, "Updating tei with localID: " + localID);
//            event.setEvent(serverRef);
//            event.setFromServer(true);
//            event.save();
//        }else{
//            Log.d(CLASS_TAG, "Didn't find tei with localID: " + localID);
//        }
//    }
//
//    public static void onReceiveEnrollmentStatus(int localID, String serverRef){
//        Enrollment enrollment = DataValueController.getEnrollment(localID);
//
//        if(enrollment != null){
//            Log.d(CLASS_TAG, "Updating tei with localID: " + localID);
//            enrollment.setEnrollment(serverRef);
//            enrollment.setFromServer(true);
//            enrollment.save();
//        }else{
//            Log.d(CLASS_TAG, "Didn't find tei with localID: " + localID);
//        }
//    }
//
//    public static void onReceiveTEIStatus(int localID, String serverRef){
//        TrackedEntityInstance tei = DataValueController.getTrackedEntityInstance(localID);
//
//        if(tei != null){
//            Log.d(CLASS_TAG, "Updating tei with localID: " + localID);
//            tei.setTrackedEntityInstance(serverRef);
//            tei.setFromServer(true);
//            tei.save();
//        }else{
//            Log.d(CLASS_TAG, "Didn't find tei with localID: " + localID);
//        }
//    }
//
//    public static boolean validSMSUpdate(String message){
//
//        String[] identifier = message.split(" ");
//        if(!(identifier.length > 0)){
//            Log.d(CLASS_TAG, "No identifier found, I am not parsing this sms");
//            return false;
//
//        }else if (!(identifier[0].equals(APPIDENTIFIER))){
//            Log.d(CLASS_TAG, "Identifier like app identifer, therefore disregarding sms");
//            return false;
//
//        }else if(identifier[1].split(":") == null){
//            Log.d(CLASS_TAG, "SMS not on correct format");
//            return false;
//
//        }else if(identifier[1].split(":").length < 3){
//            Log.d(CLASS_TAG, "SMS not on correct format");
//            return false;
//        }
//
//        return true;
//    }
//
//    public static String onNotify(Event event){
//        /**
//         *  What values do we want to send
//         *
//         *  The addressfields and if the case is imported or not
//         */
//
//        String message = "";
//        String orgId = event.getOrganisationUnitId();
//        String enrollment = event.getEnrollment();
//        String programStageID = event.getProgramStageId();
//        long enrollmentID = event.getLocalEnrollmentId();
//        List<DataValue> dataValues = event.getDataValues();
//        String tei = event.getTrackedEntityInstance();
//        String program = event.getProgramId();
//
//        message += String.format("orgUnit: %s\nProgramStage: %s\nProgram: %s\nEnrollment: %s\nTei: %s\n",
//                orgId, programStageID, program, enrollment, tei);
//
//        for(DataValue dataValue: dataValues){
//            message += "Dataelement : " + dataValue.getDataElement();
//            message += "\tvalue : " + dataValue.getValue() + "\n";
//        }
//
//        return message;
//    }
//
//    public static String onNotify(TrackedEntityInstance trackedEntityInstance){
//        List<TrackedEntityAttributeValue> teiAttrs = trackedEntityInstance.getAttributes();
//        String message = "";
//
//        for(TrackedEntityAttributeValue teiAttr : teiAttrs){
//            message += teiAttr.getValue();
//        }
//
//        return message;
//    }
//
//    public static String onNotify(Enrollment enrollment){
//        String message = "";
//        List<TrackedEntityAttributeValue> teiAttrValues = enrollment.getAttributes();
//        String orgUnit = enrollment.getOrgUnit();
//        Program program = enrollment.getProgram();
//        List<Event> events = enrollment.getEvents();
//        String tei = enrollment.getTrackedEntityInstance();
//        long teiLocalID = enrollment.getLocalTrackedEntityInstanceId();
//
//        message += String.format("org: %s\nprogram: %s\ntei: %s", orgUnit, program.getDisplayName(), tei);
//
//        for(TrackedEntityAttributeValue teiAttrVal : teiAttrValues){
//            message += "\nTeiAttrVal: " + teiAttrVal.getValue();
//        }
//
//        return message;
//    }
//}

