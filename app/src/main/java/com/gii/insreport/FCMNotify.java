package com.gii.insreport;

import android.util.Log;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Timur_hnimdvi on 10-Sep-16.
 */
public class FCMNotify {
    private static String TAG = "FCMNotify.java";
    public void trySendNotification(String userID, String name, String phone, String address) {
        JSONObject body = new JSONObject();
        try {
            body.put("to", "/topics/" + userID);
            body.put("priority", "high");

            JSONObject data = new JSONObject();
            data.put("name", name);
            data.put("phone", phone);
            data.put("address", address);

            body.put("data", data);
        } catch (Exception e) {
            Log.e(TAG, "trySendNotification: " + e.getMessage() );
        } finally {
            post("https://fcm.googleapis.com/fcm/send", body.toString(), new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    // Something went wrong
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String responseStr = response.body().string();
                        Log.e(TAG, "Response:" + responseStr);
                    } ;
                }
            });
        }

    }

    public String tryGetNotificationString(String userID, String name, String phone, String address) {
        JSONObject body = new JSONObject();
        String s = "null";
        try {
            body.put("to", "/topics/" + userID);
            body.put("priority", "high");

            JSONObject data = new JSONObject();
            data.put("name", name);
            data.put("phone", phone);
            data.put("address", address);

            body.put("data", data);
            s = body.toString();
        } catch (Exception e) {
            Log.e(TAG, "trySendNotification: " + e.getMessage() );
        } finally {

            //return postString("https://fcm.googleapis.com/fcm/send", body.toString());
        }

        s = "curl --insecure --header 'Authorization: key=AIzaSyCCpeRFK0PoHkuo08VqZiVkoVqB0USoZNQ' --header 'Content-Type:application/json' -d '" + s;
        s = s + "' \"https://fcm.googleapis.com/fcm/send\"";
        return s;
    }

    public String tryGetFormDataString(String userID, String formId, Map<String,String> input) {
        JSONObject body = new JSONObject();
        String s = "null";
        try {
            JSONObject data = new JSONObject();
            for (Map.Entry<String, String> entry : input.entrySet()) {
                body.put(entry.getKey(),entry.getValue());
            }
            s = body.toString();
        } catch (Exception e) {
            Log.e(TAG, "trySendNotification: " + e.getMessage() );
        } finally {

            //return postString("https://fcm.googleapis.com/fcm/send", body.toString());
        }

        s = "curl -X PUT -d '{\"description\" : \"fromServer\", \"id\" : \"" + formId + "\"," +
                "\"dateCreated\": {\".sv\" : \"timestamp\"}, \"input\" : "  + s;
        s = s + "}' " + "\"" + "https://insreport-f39a3.firebaseio.com/forms/incident/"+userID+"/"+formId + "/.json\"";
        return s;
    }


    OkHttpClient client = new OkHttpClient();
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    Call post(String url, String json, Callback callback) {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "key=AIzaSyCCpeRFK0PoHkuo08VqZiVkoVqB0USoZNQ")
                .url(url)
                .post(body)
                .build();
        Call call = client.newCall(request);
        call.enqueue(callback);
        return call;
    }


}
