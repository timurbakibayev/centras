package com.gii.insreport;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import java.util.Date;

/**
 * Created by Timur_hnimdvi on 10-Oct-16.
 */
public class ProximityIntentReceiver extends BroadcastReceiver{
    private static final String TAG = "ProximityIntentReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {

        String k = LocationManager.KEY_PROXIMITY_ENTERING;

        boolean state = intent.getBooleanExtra(k, false);
        if(state){
            Log.e(TAG, "onReceive: ENTERING region set by MyFirebaseMessagingService");
            Toast.makeText(context, "Вы прибыли на место аварии", Toast.LENGTH_LONG).show();
            String personName = intent.getExtras().getString("personName");
            String phoneNo = intent.getExtras().getString("phoneNo");
            InsReport.logErrorFirebase("ARRIVED!","Phone: " + phoneNo,
                    "Name: " + personName);

            String soundUri = InsReport.sharedPref.getString("notifications_new_message_ringtone", RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION).toString());
            Uri defaultSoundUri = Uri.parse(soundUri);

            long[] vibra = new long[0];
            if (InsReport.sharedPref.getBoolean("notifications_new_message_vibrate", false)) {
                vibra = new long[]{0, 100, 100, 100, 100, 400, 1000, 100, 100, 100, 100, 400};
            }

            NotificationCompat.Builder notificationBuilder;
            notificationBuilder = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.drawable.centraslogo)
                    .setContentTitle(personName)
                    .setTicker("Вы прибыли на место аварии!")
                    .setContentText("Вы прибыли на место аварии!")
                    .setSound(defaultSoundUri)
                    .setAutoCancel(true)
                    .setVibrate(vibra)
                    .setShowWhen(true)
                    .setWhen((new Date()).getTime())
                    .setOngoing(false)
                    .setContentIntent(null);

            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            if (InsReport.sharedPref.getBoolean("notifications_new_message",true))
                notificationManager.notify(777 /* ID of arrivals */, notificationBuilder.build());


        }else{
            Log.e(TAG, "onReceive: LEAVING region set by MyFirebaseMessagingService");
            Toast.makeText(context, "Вы покинули место аварии, спасибо за работу!",Toast.LENGTH_LONG).show();
        }
    }
}
