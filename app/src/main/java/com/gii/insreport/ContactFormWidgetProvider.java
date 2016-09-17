package com.gii.insreport;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

/**
 * Created by Timur_hnimdvi on 09-Sep-16.
 */
public class ContactFormWidgetProvider extends AppWidgetProvider {
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int count = appWidgetIds.length;

        String lastPressed = InsReport.sharedPref.getString("lastPressed", "");
        String personName = InsReport.sharedPref.getString("personName", "");
        String personPhone = InsReport.sharedPref.getString("personPhone", "");
        String personAddress = InsReport.sharedPref.getString("personAddress", "");

        for (int i = 0; i < count; i++) {
            int widgetId = appWidgetIds[i];

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                    R.layout.widget_layout);
            remoteViews.setTextViewText(R.id.widget_textview, personName + "\nТелефон: " + personPhone + "\nАдрес: " + personAddress);


            if (!personPhone.equals("")) {
                Intent intentMA = new Intent(context, MainActivity.class);
                intentMA.putExtra("findByPhone", personPhone);
                PendingIntent configPendingIntent = PendingIntent.getActivity(context, 0, intentMA, 0);
                remoteViews.setOnClickPendingIntent(R.id.widget_textview, configPendingIntent);
            }


            Intent intentCall = new Intent(Intent.ACTION_CALL);
            intentCall.setData(Uri.parse("tel:" + personPhone.trim()));
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intentCall, 0);
            if (!personPhone.trim().equals(""))
                remoteViews.setOnClickPendingIntent(R.id.actionButtonCall, pendingIntent);

            //Google Maps

            Intent intentMaps = new Intent(Intent.ACTION_VIEW);
            String location = personAddress.replaceAll(" ", "+");
            Uri locationUri = Uri.parse("geo:0,0?").buildUpon()
                    .appendQueryParameter("q", location)
                    .build();

            intentMaps.setData(locationUri);
            //intentMaps.setPackage("com.google.android.apps.maps");
            PendingIntent pendingIntent1 = PendingIntent.getActivity(context, 0, intentMaps, 0);
            if (!personAddress.trim().equals(""))
                remoteViews.setOnClickPendingIntent(R.id.actionButtonAddress, pendingIntent1);

            appWidgetManager.updateAppWidget(widgetId, remoteViews);
        }
    }

    private void onUpdate(Context context) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance
                (context);

        ComponentName thisAppWidgetComponentName =
                new ComponentName(context.getPackageName(), getClass().getName()
                );
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(
                thisAppWidgetComponentName);
        onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        if (intent.getStringExtra("name") != null)
            InsReport.savePref("personName", intent.getStringExtra("name"));
        if (intent.getStringExtra("phone") != null)
            InsReport.savePref("personPhone", intent.getStringExtra("phone"));
        if (intent.getStringExtra("address") != null)
            InsReport.savePref("personAddress", intent.getStringExtra("address"));

        //if ("address".equals(intent.getAction())) {
        onUpdate(context);
        //}
    }
}
