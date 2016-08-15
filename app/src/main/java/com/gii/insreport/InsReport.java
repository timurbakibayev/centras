package com.gii.insreport;

import android.app.Application;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ListView;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Timur on 20-Jul-16.
 */
public class InsReport extends Application {

    private final static String TAG = "InsReport (Application)";

    public final static String EXTRA_FIREBASE_CATALOG = "Firebase.Catalog";
    public final static String EXTRA_ID_NO = "Firebase.Form.No";
    public final static String INCIDENT_TYPE = "Incident.Type";

    public static Firebase ref;
    public static FirebaseStorage storage;
    public static StorageReference storageRef;
    public static FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    public static String userID = "";
    public static SharedPreferences sharedPref;

    public static MainActivity mainActivity;


    public static DamagePlanData damagePlanData = new DamagePlanData();
    public static ArrayList<FormsCollection> mainMenuForms = new ArrayList<>();
    public static FormsCollection preInsuranceFormsCollection = new FormsCollection();
    public static FormsCollection incidentFormsCollection = new FormsCollection();
    public static int currentFormNo = -1;

    public static Element currentElement = null;

    public static FirebaseUser user;

    public static ListView currentListView = null;

    public static ArrayList<Bitmap> bitmapsNeedToBeRecycled = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        sharedPref = this.getSharedPreferences("default",0);
        Firebase.setAndroidContext(this);
        Firebase.getDefaultConfig().setPersistenceEnabled(true);

        if (sharedPref.getString("userID", "").equals("")) {
            userID = generateNewId();
            savePref("userID", userID);
        } else
            userID = sharedPref.getString("userID","");

        //TODO: REMOVE THIS WHEN RELEASED!!!!!
        userID = "testUserID";

        ref = new Firebase("https://insreport-f39a3.firebaseio.com/");

        mAuth = FirebaseAuth.getInstance();


        initForms();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                InsReport.user = user;
                if (user != null) {
                    // User is signed in
                    Log.e(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    for (FormsCollection mainMenuForm : mainMenuForms) {
                        mainMenuForm.retrieveDataFromFireBase();
                    }
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


        //TODO: REMOVE THIS WHEN RELEASED!!!!!
        //addDummyForms();
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
        //preInsuranceFormsCollection.retrieveDataFromFireBase();
        incidentFormsCollection.description = getString(R.string.Incident);
        incidentFormsCollection.fireBaseCatalog = "incident";
        //incidentFormsCollection.retrieveDataFromFireBase();
        //incidentFormsCollection.checkSMS(getApplicationContext());

        mainMenuForms.add(preInsuranceFormsCollection);
        mainMenuForms.add(incidentFormsCollection);
    }
}
