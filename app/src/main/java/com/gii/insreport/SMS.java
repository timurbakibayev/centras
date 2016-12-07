package com.gii.insreport;

import android.os.AsyncTask;
import android.telephony.SmsManager;
import android.util.Log;

import org.ksoap2.HeaderProperty;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Timur_hnimdvi on 04-Oct-16.
 */
public class SMS {
//    public static String TAG = "SMS.java";
//    public static String SOAP_ACTION = "http://insisdb/SmsServ.wsdl/sendsms";
//    public static String METHOD_NAME = "sendsms";
//    public static String NAMESPACE = "http://insisdb/SmsServ.wsdl";
//    public static String URL = "http://88.204.255.194:8001/SmsAppl-sms-context-root/smsServPort?wsdl";
//    public static boolean send(String phoneNumber, String message) {
//        if (!InsReport.sharedPref.getBoolean("sms", true))
//            return false;
//        try {
//            SmsManager smsManager = SmsManager.getDefault();
//            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
//            return true;
//        } catch (Exception e) {
//            Log.e("SMS.java", "send: " + e.getMessage());
//            return false;
//        }
//    }
//
//    public static void wsdlQuery(String phoneNo, String message) {
//        Log.e(TAG, "wsdlQuery: start");
//        final SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER10);
//        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
//        request.addProperty("piPhone", phoneNo);
//        request.addProperty("piTheme", "theme");
//        request.addProperty("piMessage", message);
//        envelope.setOutputSoapObject(request);
//        final HttpTransportSE httpTransport = new HttpTransportSE(URL);
//        try {
//            AsyncTask.execute(new Runnable() {
//                @Override
//                public void run() {
//                    //TODO your background code
//                    try {
//                        List<HeaderProperty> headerList = new ArrayList<HeaderProperty>();
//                        headerList.add(new HeaderProperty("Authorization", "Basic " + org.kobjects.base64.Base64.encode("SAGRAD:Z@%xA5soR".getBytes())));
//                        httpTransport.call(SOAP_ACTION, envelope, headerList);
//                        Log.e(TAG, "wsdlQuery: called");
//                        SoapPrimitive resultString = (SoapPrimitive) envelope.getResponse();
//                        Log.e(TAG, "wsdlQuery: " + resultString);
//                    } catch (Exception e) {
//                        Log.e(TAG, "sms inner wsdlQuery run: " + e.getMessage());
//                    }
//                }
//            });
//        } catch (Exception e) {
//            Log.e(TAG, "wsdlQuery: error: " + e.getMessage());
//            e.printStackTrace();
//        }
//    }

    public static String call(String phoneNumder) {
        phoneNumder = phoneNumder.trim();
        if(phoneNumder.length() > 7 && phoneNumder.charAt(0) != '8'
                && phoneNumder.charAt(0) != '+'){
            phoneNumder = "+7" +phoneNumder;
        }
        return phoneNumder;
    }
}
