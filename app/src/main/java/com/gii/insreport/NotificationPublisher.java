package com.gii.insreport;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NotificationPublisher extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        String name = intent.getStringExtra("name");
        String phone = intent.getStringExtra("phone");
        String address = intent.getStringExtra("address");
        MyFirebaseMessagingService.sendNotification(name,name,phone,address,context, true, false);
    }
}