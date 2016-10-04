package com.gii.insreport;

import android.telephony.SmsManager;
import android.util.Log;

/**
 * Created by Timur_hnimdvi on 04-Oct-16.
 */
public class SMS {
    public static boolean send(String phoneNumber, String message) {
        if (!InsReport.sharedPref.getBoolean("sms",true))
            return false;
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null,message, null, null);
            return true;
        }
        catch (Exception e)
        {
            Log.e("SMS.java", "send: " + e.getMessage());
            return false;
        }
    }
}
