package com.gii.insreport;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.multidex.MultiDex;
import android.util.Log;
import android.widget.ListView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ServerValue;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by Timur on 20-Jul-16.
 */
public class InsReport extends Application {

    private final static String TAG = "InsReport (Application)";

    public final static String fireBaseGeneralDirectory = "calimregistration";
    public final static String EXTRA_FIREBASE_CATALOG = "Firebase.Catalog";
    public final static String EXTRA_ID_NO = "Firebase.Form.No";
    public final static String EXTRA_FORM_READY = "Firebase.Form.formReady";
    public final static String INCIDENT_TYPE = "Incident.Type";

    //public final static String forceUserID = "5K0dJbLCxzPrz9ijuXuJPqiu18u1";

    public static FallbackLocationTracker locationTracker;

    public static Firebase ref;
    public static FirebaseAuth mAuth;
    public static FirebaseStorage storage;
    public static StorageReference storageRef;
    public static Directories directories;
    public static ArrayList<FirebaseUserEmail> firebaseUserEmails = new ArrayList<>();

    public static ArrayList<String> multipleImages = new ArrayList<>();
    public static ArrayList<Long> multipleImagesDate = new ArrayList<>();

    //TODO: EDIT THIS PHONE FOR EACH COMPANY!
    public static String defaultCallCenter = "7072";

    private FirebaseAuth.AuthStateListener mAuthListener;

    public static String userID = "";

    public static String forceUserID() {
        if (sharedPref.getString("force_user_id","").equals("")) {
            if (user != null)
                return user.getUid();
            return "error_user_id";
        }
        return sharedPref.getString("force_user_id","").trim();
    }

    public static SharedPreferences sharedPref;

    public static MainActivity mainActivity;

    public static DamagePlanData damagePlanData = new DamagePlanData();
    public static ArrayList<FormsCollection> mainMenuForms = new ArrayList<>();
    public static FormsCollection preInsuranceFormsCollection = new FormsCollection();
    public static FormsCollection incidentFormsCollection = new FormsCollection();
    public static IncidentFormActivity incidentFormActivity;
    public static Form formToBeAccepted = null;

    public static Element currentElement = null;
    public static Form currentForm= null;

    public static FirebaseUser user;

    public static ListView currentListView = null;
    public static Bitmap tempBitmap;

    public static ArrayList<Bitmap> bitmapsNeedToBeRecycled = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        locationTracker = new FallbackLocationTracker(this);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        prepareBeep();

        Firebase.setAndroidContext(this);
        Firebase.getDefaultConfig().setPersistenceEnabled(true);

        if (sharedPref.getString("userID", "").equals("")) {
            userID = generateNewId();
            savePref("userID", userID);
        } else
            userID = sharedPref.getString("userID","");

        //TODO: REMOVE THIS WHEN RELEASED!!!!!
        userID = "testUserID";

        ref = new Firebase("https://" + fireBaseGeneralDirectory + ".firebaseio.com/");

        mAuth = FirebaseAuth.getInstance();

        initForms();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                InsReport.user = user;
                if (FirebaseInstanceId.getInstance().getToken() != null)
                    checkBlock(FirebaseInstanceId.getInstance().getToken().replaceAll("[^0-9]+", ""));
                if (user != null) {
                    // User is signed in
                    Log.e(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    FirebaseMessaging.getInstance().subscribeToTopic(user.getUid());
                    Log.e(TAG, "onAuthStateChanged: Instance_Id_Scope:" + FirebaseInstanceId.getInstance().getToken());
                    Log.e(TAG, "onAuthStateChanged: subscribed to " + user.getUid());
                    for (FormsCollection mainMenuForm : mainMenuForms) {
                        if (!mainMenuForm.dataChangeListenerAdded)
                            mainMenuForm.addDataChangeListener();
                    }
                    ref.child("users/"+user.getUid()+"/email").setValue(user.getEmail().toString());
                    ref.child("users/"+user.getUid()+"/token").setValue(FirebaseInstanceId.getInstance().getToken());
                    ref.child("users/"+user.getUid()+"/id").setValue(user.getUid());
                    ref.child("users/"+user.getUid()+"/lastLogin").setValue(ServerValue.TIMESTAMP);
                    Date d = new Date();
                    ref.child("users/"+user.getUid()+"/lastLoginAndroid").setValue(IncidentFormActivity.dateOnlyTextStrict(d));
                    ref.child("users/"+user.getUid()+"/devices/" + FirebaseInstanceId.getInstance().getToken().replaceAll("[^0-9]+", "")).setValue(
                            sharedPref.getString("devicename","Неизвестное устройство"));
                    MainActivity.checkDeviceName(mainActivity);
                    logFirebase("login");
                } else {
                    // User is signed out
                    Log.e(TAG, "onAuthStateChanged:signed_out");
                }
                if (mainActivity != null)
                    mainActivity.refreshUser();
                // ...
            }
        };

        mAuth.addAuthStateListener(mAuthListener);

        directories = new Directories();

        loadUserBase();

    }

    private void loadUserBase() {
        ref.child("users").addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                firebaseUserEmails.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    FirebaseUserEmail newUser = snapshot.getValue(FirebaseUserEmail.class);
                    firebaseUserEmails.add(newUser);
                    //Log.e(TAG, "onDataChange: found user " + newUser.email);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public static void notifyFormsList() {
        if (currentListView != null &&
                currentListView.getAdapter() != null) {
            ((FormsListAdapter) (currentListView.getAdapter())).notifyDataSetChanged();
            currentListView.invalidateViews();
            Log.e(TAG, "notifyFormsList: Done");
        }
    }

    public static String generateNewId() {
        SecureRandom random = new SecureRandom();
        return(new BigInteger(64, random).toString(32));
    }

    public static void savePref(String key, String value) {
        SharedPreferences.Editor prefsEditor = sharedPref.edit();
        prefsEditor.putString(key, value);
        prefsEditor.commit();
    }

    String[] dummyPhotos = {"3nk7q4jh3it09", "2q0k5vbcib7d5", "5a0utipgtap7f", "8e020q1vbnt5c", "b9pg7fj5mtreo", "aclnh37f9bhed"};
    private void addDummyForms() {
        Random rnd = new Random();
        for (int i = 0; i < 25; i++) {
            Form newForm = new Form();
            newForm.generateNewId();
            FormTemplates.applyTemplate(newForm, preInsuranceFormsCollection.fireBaseCatalog);
            for (Element element : newForm.elements) {
                if (element.type == Element.ElementType.ePhoto) {
                    int r = rnd.nextInt(5);
                    for (int j = 0; j < r; j++)
                        element.vPhotos.add(dummyPhotos[rnd.nextInt(dummyPhotos.length)]);
                }
                if (element.type == Element.ElementType.eGroup)
                    for (Element element1 : element.elements) {
                        if (element1.type == Element.ElementType.ePhoto) {
                            int r = rnd.nextInt(5);
                            for (int j = 0; j < r; j++)
                                element1.vPhotos.add(dummyPhotos[rnd.nextInt(dummyPhotos.length)]);
                        }
                }
            }
            newForm.description = "PreInsurance form no. " + i;
            preInsuranceFormsCollection.forms.add(newForm);
        }

        for (int i = 0; i < 25; i++) {
            Form newForm = new Form();
            newForm.generateNewId();
            FormTemplates.applyTemplate(newForm, incidentFormsCollection.fireBaseCatalog);
            for (Element element : newForm.elements) {
                if (element.type == Element.ElementType.ePhoto) {
                    int r = rnd.nextInt(5);
                    for (int j = 0; j < r; j++)
                        element.vPhotos.add(dummyPhotos[rnd.nextInt(dummyPhotos.length)]);
                }
                if (element.type == Element.ElementType.eGroup)
                    for (Element element1 : element.elements) {
                        if (element1.type == Element.ElementType.ePhoto) {
                            int r = rnd.nextInt(5);
                            for (int j = 0; j < r; j++)
                                element1.vPhotos.add(dummyPhotos[rnd.nextInt(dummyPhotos.length)]);
                        }
                    }
            }
            newForm.description = "Убыток номер " + i;
            incidentFormsCollection.forms.add(newForm);
        }
    }

    private void initForms() {
        mainMenuForms = new ArrayList<>();

        preInsuranceFormsCollection.description = getString(R.string.preInsurance);
        preInsuranceFormsCollection.fireBaseCatalog = "preInsurance";
        //preInsuranceFormsCollection.addDataChangeListener();
        incidentFormsCollection.description = getString(R.string.Incident);
        incidentFormsCollection.fireBaseCatalog = "incident";
        //incidentFormsCollection.addDataChangeListener();
        //incidentFormsCollection.checkSMS(getApplicationContext());

        mainMenuForms.add(preInsuranceFormsCollection);
        mainMenuForms.add(incidentFormsCollection);
    }

    public static void logFirebase(String logText) {
        Map<String,Object> log = new HashMap<>();
        if (user != null) {
            log.put("User",user.getEmail().toString());
            log.put("ServerTime",ServerValue.TIMESTAMP);
            Date d = new Date();
            log.put("TextTime", IncidentFormActivity.dateOnlyTextStrict(d));
            log.put("Text",logText);
            log.put("Device ID",FirebaseInstanceId.getInstance().getToken());

            ref.child("log/"+ IncidentFormActivity.dateToYYMMDD(d) + "/" + InsReport.forceUserID()).
                    push().setValue(log);
        }
    }

    public static void logErrorFirebase(String logText, String logText2, String logText3) {
        Map<String,Object> log = new HashMap<>();
        if (user != null) {
            log.put("User",user.getEmail().toString());
            log.put("ServerTime",ServerValue.TIMESTAMP);
            Date d = new Date();
            log.put("TextTime", IncidentFormActivity.dateOnlyTextStrict(d));
            log.put("Text",logText);
            log.put("Text2",logText2);
            log.put("Text3",logText3);
            log.put("Device ID",FirebaseInstanceId.getInstance().getToken());

            ref.child("error/"+ IncidentFormActivity.dateToYYMMDD(d) + "/" + InsReport.forceUserID()).
                    push().setValue(log);
        }
    }

    public static void logGPSFirebase(String phoneNo, String logText2, String logText3) {
        Map<String,Object> log = new HashMap<>();
        if (user != null) {
            log.put("User",user.getEmail().toString());
            log.put("ServerTime",ServerValue.TIMESTAMP);
            Date d = new Date();
            log.put("TextTime", IncidentFormActivity.dateOnlyTextStrict(d));
            log.put("Phone",phoneNo);
            log.put("Text2",logText2);
            log.put("Text3",logText3);
            log.put("Device ID",FirebaseInstanceId.getInstance().getToken());

            ref.child("gps/"+ IncidentFormActivity.dateToYYMMDD(d) + "/" + InsReport.forceUserID()).
                    push().setValue(log);
        }
    }

    public void checkBlock(String token) {
        ref.child("block/"+token).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists())
                    Log.e(TAG, "checkBlock: device is not blocked");
                else {
                    Log.e(TAG, "checkBlock: DEVICE IS BLOCKED! QUIT!");
                    System.exit(0);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    static Ringtone r;
    public void prepareBeep() {
        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            String soundUri = sharedPref.getString("notifications_new_message_ringtone",RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION).toString());
            r = RingtoneManager.getRingtone(getApplicationContext(), Uri.parse(soundUri));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void beep() {
        try {
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

}
