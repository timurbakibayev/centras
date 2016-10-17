package com.gii.insreport;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Date;

/**
 * Created by Timur_hnimdvi on 18-Aug-16.
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e(TAG, "onMessageReceived: Push Notification Received!");

        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }


        Intent intent = new Intent(this, ContactFormWidgetProvider.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
// Use an array and EXTRA_APPWIDGET_IDS instead of AppWidgetManager.EXTRA_APPWIDGET_ID,
// since it seems the onUpdate() is only fired on that:
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance
                (this);
        ComponentName thisAppWidgetComponentName =
                new ComponentName(this.getPackageName(), getClass().getName()
                );
        int[] Ids = appWidgetManager.getAppWidgetIds(
                thisAppWidgetComponentName);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, Ids);
        intent.putExtra("name", remoteMessage.getData().get("name"));
        intent.putExtra("phone", remoteMessage.getData().get("phone"));
        intent.putExtra("address", remoteMessage.getData().get("address"));
        intent.setAction("Updated from notification!");
        sendBroadcast(intent);

        String personPhone = "";
        String address = "";
        String personName = "";

        personPhone = remoteMessage.getData().get("phone");
        address = remoteMessage.getData().get("address");
        personName = remoteMessage.getData().get("name");


        String t = "";
        if (remoteMessage.getData().get("name") != null)
            t = remoteMessage.getData().get("name");

        sendNotification(t,personName, personPhone, address, this, true, true);
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }
    // [END receive_message]

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    public static void sendNotification(String messageBody, String personName, String personPhone, String address, Context context, boolean beep, boolean setProximity) {
        //Intent intent = new Intent(this, MainActivity.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        //intent.setAction("foo");
        //PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
        //       PendingIntent.FLAG_UPDATE_CURRENT);

        int id = Form.hash(personPhone);

        Log.e(TAG, "sendNotification: id " + id);

        String soundUri = InsReport.sharedPref.getString("notifications_new_message_ringtone", RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION).toString());
        Uri defaultSoundUri = Uri.parse(soundUri);
        if (!beep)
            defaultSoundUri = null;

        long[] vibra = new long[0];
        if (InsReport.sharedPref.getBoolean("notifications_new_message_vibrate", false)) {
            vibra = new long[]{0, 100, 100, 100, 100, 400, 1000, 100, 100, 100, 100, 400};
        }

        Intent intentMA = new Intent(context, MainActivity.class);
        intentMA.setAction("foo");
        intentMA.putExtra("findByPhone", personPhone);
        intentMA.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent openAppPendingIntent = PendingIntent.getActivity(context, id, intentMA, PendingIntent.FLAG_CANCEL_CURRENT);

        Intent intentCall = new Intent(Intent.ACTION_CALL);
        intentCall.setData(Uri.parse("tel:" + personPhone.trim()));
        PendingIntent callPendingIntent = PendingIntent.getActivity(context, id, intentCall, PendingIntent.FLAG_UPDATE_CURRENT);

        String regex = "((-|\\+)?[0-9]+(\\.[0-9]+)?)+";
        String[] location = address.split(" ");
        boolean coordinates = false;

        for (int i = 0; i < location.length; i++) {
            if (location[i].matches(regex)) {
                coordinates = true;
            }
        }
        String locationStr;
        Intent mapIntent = new Intent(Intent.ACTION_VIEW);

        if (coordinates && location.length > 1
                && setProximity) {
            locationStr = location[0] + "," + location[1];
            Uri locationUri1 = Uri.parse("geo:0,0?").buildUpon()
                    .appendQueryParameter("q", locationStr)
                    .build();
            mapIntent.setData(locationUri1);
            try {
                double lat = Double.parseDouble(location[0]);
                double lon = Double.parseDouble(location[1]);

                if (lat > lon ) { //for Kazakhstan, lat is always less than lon! so, swap
                    double tmp = lat;
                    lat = lon;
                    lon = tmp;
                }


                Intent arriveIntent = new Intent(context, ProximityIntentReceiver.class);
                arriveIntent.putExtra("phoneNo", personPhone);
                arriveIntent.putExtra("personName", personName);
                //arriveIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                PendingIntent arrivePendingIntent = PendingIntent.getBroadcast(context, id, arriveIntent, 0);

                LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
                if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    locationManager.addProximityAlert(lat, lon, 200, -1, arrivePendingIntent);
                    InsReport.logGPSFirebase(personPhone,"PROXIMITY SET", "Lat:"+lat + ", Lon:"+lon);
                }
            } catch (Exception e) {

            }
        } else {
            locationStr = address.replaceAll(" ", "+");
            Uri locationUri2 = Uri.parse("geo:0,0?").buildUpon()
                    .appendQueryParameter("q", locationStr)
                    .build();
            mapIntent.setData(locationUri2);
        }
        PendingIntent mapPendingIntent = PendingIntent.getActivity(context, id, mapIntent, 0);
        NotificationCompat.Builder notificationBuilder;

        if (personPhone.equals("") || address.equals("")) {
            notificationBuilder = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.drawable.centraslogo)
                    .setContentTitle("Новый страховой случай")
                    .setContentText(messageBody)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setVibrate(vibra)
                    .setContentIntent(openAppPendingIntent);
        } else if (defaultSoundUri != null) {
            long when = (new Date()).getTime();
            notificationBuilder = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.drawable.centraslogo)
                    .setContentTitle(personName)
                    .setTicker(personName)
                    .setContentText(address + "\n" + personPhone)
                    .setSound(defaultSoundUri)
                    .setVibrate(vibra)
                    .setShowWhen(true)
                    .setWhen(when)
                    .addAction(R.drawable.ic_call_black_24dp,"Позвонить",callPendingIntent)
                    .addAction(R.drawable.ic_pin_drop_black_24dp,"Карта",mapPendingIntent)
                    .setOngoing(true)
                    .setContentIntent(openAppPendingIntent);
        } else {
            long when = (new Date()).getTime();
            notificationBuilder = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.drawable.centraslogo)
                    .setContentTitle(personName)
                    .setTicker(personName)
                    .setContentText(address + "\n" + personPhone)
                    .setVibrate(vibra)
                    .setShowWhen(true)
                    .setWhen(when)
                    .addAction(R.drawable.ic_call_black_24dp,"Позвонить",callPendingIntent)
                    .addAction(R.drawable.ic_pin_drop_black_24dp,"Карта",mapPendingIntent)
                    .setOngoing(true)
                    .setContentIntent(openAppPendingIntent);
        }

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (InsReport.sharedPref.getBoolean("notifications_new_message",true))
            notificationManager.notify(id, notificationBuilder.build());
        Log.e(TAG, "sendNotification: hm... done.");
    }
}