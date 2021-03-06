package com.gii.insreport;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Timur_hnimdvi on 26-Jul-16.
 */
public class FormsCollection {
    public static String TAG = "FormsCollection.java";
    public String fireBaseCatalog = "";
    public String description = "";
    public boolean loadComplete = false;
    public boolean dataChangeListenerAdded = false;
    public ArrayList<Form> forms = new ArrayList<>();
    public static ArrayList<FormIndex> formIndices = new ArrayList<>();

    public String getFireBaseCatalog() {
        return fireBaseCatalog;
    }

    public String getDescription() {
        return description;
    }

    public ArrayList<Form> getForms() {
        return forms;
    }

    static ArrayList<String> indexedUserIds = new ArrayList<>();

    public static void addIndexListener(String userId) {
        if (indexedUserIds.contains(userId))
            return;
        indexedUserIds.add(userId);
        String fireBaseCatalog = "incident";
        Query queryRef = InsReport.ref.child("index/" + fireBaseCatalog + "/" + userId);
        queryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot postSnapshot, String s7) {
                try {
                    FormIndex newForm = postSnapshot.getValue(FormIndex.class);
                    //newForm.formId = postSnapshot.getKey();
                    //newForm.fireBaseCatalog = fireBaseCatalog;
                    Log.e(TAG, "onChildAdded: index " + newForm.formId);
                    //newForm.validate();
                    formIndices.add(newForm);
                    Log.e(TAG, "onDataChange: Sorting...");
                    Collections.sort(formIndices, new Comparator<FormIndex>() {
                        @Override
                        public int compare(FormIndex lhs, FormIndex rhs) {
                            return (!lhs.dateCreated.after(rhs.dateCreated) ? 1 : -1);
                        }
                    });
                    //InsReport.notifyFormsList();
                } catch (Exception e) {
                    //it's just a search, never mind
                }
            }

            @Override
            public void onChildChanged(DataSnapshot postSnapshot, String s7) {
                try {
                    FormIndex newForm = postSnapshot.getValue(FormIndex.class);

                    for (int i = 0; i < formIndices.size(); i++) {
                        if (formIndices.get(i).formId.equals(newForm.formId)) {
                            formIndices.set(i, newForm);
                        }
                    }
                    //InsReport.notifyFormsList();
                } catch (Exception e) {
                    //it's just a search...
                }

            }

            @Override
            public void onChildRemoved(DataSnapshot postSnapshot) {
                try {
                    FormIndex newForm = postSnapshot.getValue(FormIndex.class);
                    for (int i = 0; i < formIndices.size(); i++) {
                        if (formIndices.get(i).formId.equals(newForm.formId)) {
                            formIndices.remove(i);
                            break;
                        }
                    }
                    //InsReport.notifyFormsList();
                } catch (Exception e) {

                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }

    public void addDataChangeListener() {
        InsReport.logFirebase("Retrieving data from: " + "forms/" + fireBaseCatalog + "/" + InsReport.user.getUid());
        Log.e(TAG, "addDataChangeListener: Retrieving data from: " + "forms/" + fireBaseCatalog + "/" + InsReport.user.getUid());
        int numOfForms = Integer.parseInt(InsReport.sharedPref.getString("number_of_forms_to_load","10"));
        Query queryRef = InsReport.ref.child("forms/" + fireBaseCatalog + "/" + InsReport.forceUserID()).orderByChild("dateCreated").limitToLast(
                numOfForms
        ); //how many forms do we see

        queryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot postSnapshot, String s7) {
                try {
                    Form newForm = postSnapshot.getValue(Form.class);
                    newForm.id = postSnapshot.getKey();
                    newForm.fireBaseCatalog = fireBaseCatalog;
                    Log.e(TAG, "onChildAdded: processing new item..." + newForm.id);
                    //newForm.validate();
                    boolean exists = false;
                    for (int i = 0; i < forms.size(); i++) {
                        if (forms.get(i).id.equals(newForm.id)) {
                            exists = true;
                            Log.e(TAG, "onChildAdded: found the corresponding form id");
                        }
                    }

                    if (!exists) {
                        if (newForm.elements.size() == 0) {
                            InsReport.formToBeAccepted = newForm;
                            newForm.fireBaseCatalog = fireBaseCatalog;
                        }
                        forms.add(newForm);
                    }
                    Log.e(TAG, "onDataChange: Sorting...");
                    Collections.sort(forms, new Comparator<Form>() {
                        @Override
                        public int compare(Form lhs, Form rhs) {
                            return (!lhs.dateCreated.after(rhs.dateCreated) ? 1 : -1);
                        }
                    });
                    InsReport.notifyFormsList();
                } catch (Exception e) {
                    Log.e(TAG, "onDataChange: PROBLEMS CASTING FROM DB!!! : " + postSnapshot.getKey() + ", " + e.getMessage());
                    e.printStackTrace();
                    InsReport.logFirebase("ERROR FORM: " + "forms/" + fireBaseCatalog + "/" + InsReport.user.getUid() + "/" + postSnapshot.getKey());
                    Writer writer = new StringWriter();
                    PrintWriter printWriter = new PrintWriter(writer);
                    e.printStackTrace(printWriter);
                    String s = writer.toString();
                    String s1 = "";
                    if (s.indexOf("InvalidFormatException") > 0) {
                        int a = s.indexOf("InvalidFormatException");
                        s = s.substring(a,a+500);
                        if (s.indexOf("chain:") > 0)
                            s1 = s.substring(s.indexOf("chain:"),s.indexOf("chain:") + 100);
                        InsReport.logErrorFirebase("FORM NO." + postSnapshot.getKey(), s, s1);
                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot postSnapshot, String s7) {
                try {
                    Form newForm = postSnapshot.getValue(Form.class);
                    newForm.id = postSnapshot.getKey();
                    newForm.fireBaseCatalog = fireBaseCatalog;
                    Log.e(TAG, "onChildChanged: processing existing item..." + newForm.id + ", status " + newForm.status);
                    //newForm.validate();
                    /*
                    if (newForm.elements.size() == 0) {
                        InsReport.formToBeAccepted = newForm;
                        newForm.fireBaseCatalog = fireBaseCatalog;
                    }*/

                    for (int i = 0; i < forms.size(); i++) {
                        if (forms.get(i).id.equals(newForm.id)) {
                            if (InsReport.currentForm == null || !InsReport.currentForm.id.equals(newForm.id)) {
                                forms.set(i, newForm);
                            } else {
                                Log.w(TAG, "onChildChanged: ignoring form, because it's open");
                                InsReport.currentForm.atTheAddress = newForm.atTheAddress;
                                InsReport.currentForm.dateArrived = newForm.dateArrived;
                                InsReport.currentForm.dateLeft = newForm.dateLeft;
                                InsReport.currentForm.dateModified = newForm.dateModified;
                                InsReport.currentForm.coordinatesDateArrived = newForm.coordinatesDateArrived;
                                InsReport.currentForm.coordinatesDateLeft = newForm.coordinatesDateLeft;
                            }
                        }
                    }
                    InsReport.notifyFormsList();
                } catch (Exception e) {
                    Log.e(TAG, "onDataChange: PROBLEMS CASTING FROM DB!!! : " + postSnapshot.getKey() + ", " + e.getMessage());
                    e.printStackTrace();
                    InsReport.logFirebase("ERROR FORM: " + "forms/" + fireBaseCatalog + "/" + InsReport.user.getUid() + "/" + postSnapshot.getKey());
                    Writer writer = new StringWriter();
                    PrintWriter printWriter = new PrintWriter(writer);
                    e.printStackTrace(printWriter);
                    String s = writer.toString();
                    String s1 = "";
                    if (s.indexOf("InvalidFormatException") > 0) {
                        int a = s.indexOf("InvalidFormatException");
                        s = s.substring(a,a+500);
                        if (s.indexOf("chain:") > 0)
                            s1 = s.substring(s.indexOf("chain:"),s.indexOf("chain:") + 100);
                        InsReport.logErrorFirebase("FORM NO." + postSnapshot.getKey(), s, s1);
                    }
                }

            }

            @Override
            public void onChildRemoved(DataSnapshot postSnapshot) {
                try {
                    Form newForm = postSnapshot.getValue(Form.class);
                    newForm.id = postSnapshot.getKey();
                    newForm.fireBaseCatalog = fireBaseCatalog;
                    Log.e(TAG, "onChildRemoved: removing existing item..." + newForm.id + ", status " + newForm.status);
                    //newForm.validate();
                    /*
                    if (newForm.elements.size() == 0) {
                        InsReport.formToBeAccepted = newForm;
                        newForm.fireBaseCatalog = fireBaseCatalog;
                    }*/
                    for (int i = 0; i < forms.size(); i++) {
                        if (forms.get(i).id.equals(newForm.id)) {
                            Log.w(TAG, "onChildRemoved: removing form, because it's open");
                            forms.get(i).removed = true;
                            forms.remove(i);
                            break;
                        }
                    }
                    InsReport.notifyFormsList();
                } catch (Exception e) {
                    Log.e(TAG, "onDataChange: PROBLEMS CASTING FROM DB!!! : " + postSnapshot.getKey() + ", " + e.getMessage());
                    e.printStackTrace();
                    InsReport.logFirebase("ERROR FORM: " + "forms/" + fireBaseCatalog + "/" + InsReport.user.getUid() + "/" + postSnapshot.getKey());
                    Writer writer = new StringWriter();
                    PrintWriter printWriter = new PrintWriter(writer);
                    e.printStackTrace(printWriter);
                    String s = writer.toString();
                    String s1 = "";
                    if (s.indexOf("InvalidFormatException") > 0) {
                        int a = s.indexOf("InvalidFormatException");
                        s = s.substring(a,a+500);
                        if (s.indexOf("chain:") > 0)
                            s1 = s.substring(s.indexOf("chain:"),s.indexOf("chain:") + 100);
                        InsReport.logErrorFirebase("FORM NO." + postSnapshot.getKey(), s, s1);
                    }
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        queryRef.
                addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        Log.e(TAG, "onDataChange: got new form data");
                        loadComplete = true;
                    }
                    @Override
                    public void onCancelled(FirebaseError firebaseError) {
                        Log.e("Firebase", "The read failed 7: " + firebaseError.getMessage());
                    }
                });
        dataChangeListenerAdded = true;
    }

    public void checkSMS(Context context) {
        Cursor cursor =  context.getContentResolver().query(Uri.parse("content://sms/inbox"), null, null, null, null);

        if (cursor.moveToFirst()) { // must check the result to prevent exception
            do {
                String msgData = "";
                boolean relevantData = false;
                for(int idx=0;idx<cursor.getColumnCount();idx++)
                {
                    msgData += " " + cursor.getColumnName(idx) + ":" + cursor.getString(idx);
                    if (cursor.getColumnName(idx).equals("body") &&
                            cursor.getString(idx).contains("ОСГПО"))
                        relevantData = true;
                }
                // use msgData
                if (relevantData)
                    Log.e(TAG, "checkSMS: " + msgData);
            } while (cursor.moveToNext());
        } else {
            // empty box, no SMS
        }
    }
}
