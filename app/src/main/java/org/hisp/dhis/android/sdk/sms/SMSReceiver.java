package org.hisp.dhis.android.sdk.sms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Alexander Premer on 22.07.15.
 */
public class SMSReceiver extends BroadcastReceiver{
    private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
    private static final String CLASS_TAG = "SMSReceiver";
    private static final String APPIDENTIFIER = "trackercapture";

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(SMS_RECEIVED)){
            Bundle bundle = intent.getExtras();
            if(bundle != null){
                // get sms objects and parse incoming sms
                Pair<ArrayList<SmsMessage>, StringBuilder> pair= getSmsObjects((Object[]) bundle.get("pdus"));
                if(pair != null) {
                    parseIncomingSMS(pair.first, pair.second, context);
                }
            }
        }
    }

    /**
     *
     * @param pdus
     * @return a pair of Arraylist with SmsMessage objects and a StringBuilder object
     *          return is null if pdus length is 0
     *          TODO: here you don't have to send both the arraylist and the stringbuilder
     *          the stringbuilder is sufficient
     */
    private Pair<ArrayList<SmsMessage>, StringBuilder> getSmsObjects(Object[] pdus){
        if(pdus.length > 0) {
            // Large message might be broken into many
            ArrayList<SmsMessage> messages = new ArrayList<>(pdus.length);
            StringBuilder stringBuilder = new StringBuilder();
            for(Object message : pdus ){
                messages.add(SmsMessage.createFromPdu((byte[]) message));
                stringBuilder.append(messages.get(messages.size()-1).getMessageBody());
            }

            return new Pair<>(messages, stringBuilder);
        }
        return null;
    }

    /**
     * Parses the incoming message and either saves the element it receives or not depending on the message
     *
     * @param smsMessages arraylist of SmsMessage
     * @param stringMessage StringBuilder object of the messages
     * @param context current context
     *
     * TODO: currently it does nothing other than displaying the sms in a toast, need to implement real parsing
     * Message format as far:
     *                yes/no         edalh321
     *               success/type/localid/serverID
     *                       e.g. 2
     *
     *                Example of sms: IDENTIFIER S:evt:1:W0DNaZmDBQk
     *                succesfull update on server of localID Tei 1 with serverRef W0DNaZmDBQk
     *                TODO remove uneccesary parameters and things you do in methods...
     */
    private void parseIncomingSMS(ArrayList<SmsMessage> smsMessages, StringBuilder stringMessage, Context context){
        /*
            how to get the sender of the SMS
            String sender = smsMessages.get(0).getOriginatingAddress();
         */
        String message = stringMessage.toString();

//        if(!SMSParse.validSMSUpdate(message)){
//            return;
//        }

        // Displaying the sms on screen
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

        String[] fields = message.split(":");

        // Means that the sms is successfully uploaded to the server
        if (fields[0].equalsIgnoreCase(APPIDENTIFIER + " S")) {
            //Extract necessary datafields
            try {
                String type = fields[1];
                int localID = Integer.parseInt(fields[2]);
                String serverReference = fields[3];
//                updateLocal(type, localID, serverReference);
            } catch (NumberFormatException nfe) {
                nfe.printStackTrace();
            }
        } else {
            // The message needs to be sent again.
            Log.d(CLASS_TAG, "SMS could not be parsed");
        }
    }

//    public void updateLocal(String type, int localID, String serverRef){
//        switch (type){
//            case "evt":
//                SMSParse.onReceiveEventStatus(localID, serverRef);
//                break;
//            case "enr":
//                SMSParse.onReceiveEnrollmentStatus(localID, serverRef);
//                break;
//            case "tei":
//                SMSParse.onReceiveTEIStatus(localID, serverRef);
//                break;
//            default:
//                Log.d(CLASS_TAG, "Couldn't find the correct type");
//        }
//    }
}
