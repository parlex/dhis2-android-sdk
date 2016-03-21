package org.hisp.dhis.android.sdk.sms;

import android.telephony.SmsManager;

/**
 * Created by premer on 27.02.16.
 */
public class SMSSender {
    private static final String CLASS_TAG = "SMSSender";

    public static SMSSender getInstance(){
        return smsSender;
    }

    private static final SMSSender smsSender;

    static {
        smsSender = new SMSSender();
    }

    /**
     * Sends an SMS with the given message to the phonenumber
     * @param message Message to send
     * @param phoneNumber Recipient of SMS
     */
    public static void sendSMS(String message, String phoneNumber){
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNumber, null, message, null, null);

    }

}
