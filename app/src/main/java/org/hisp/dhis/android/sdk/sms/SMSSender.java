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

    public static void sendSMS(String message, String contactPerson, String phoneNumber){
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNumber, null, message, null, null);

    }

}
