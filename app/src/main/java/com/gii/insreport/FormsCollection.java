package com.gii.insreport;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

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

    public String getFireBaseCatalog() {
        return fireBaseCatalog;
    }

    public String getDescription() {
        return description;
    }

    public ArrayList<Form> getForms() {
        return forms;
    }


    public void addDataChangeListener() {
        Query queryRef = InsReport.ref.child("forms/" + fireBaseCatalog + "/" + InsReport.user.getUid()).orderByChild("dateCreated").limitToLast(10); //how many forms do we see
        queryRef.
                addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        Log.e(TAG, "onDataChange: got new data, processing...");
                        //forms.clear();
                        for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                            try {
                                Form newForm = postSnapshot.getValue(Form.class);
                                newForm.id = postSnapshot.getKey();
                                Log.e(TAG, "onDataChange: processing another item..." + newForm.id);
                                //newForm.validate();
                                if (newForm.elements.size() == 0) {
                                    /*
                                    Log.e(TAG, "onDataChange: applying template for " + newForm.id);
                                    FormTemplates.applyTemplate(newForm, fireBaseCatalog);
                                    Log.e(TAG, "onDataChange: done applying. updating description...");
                                    newForm.updateDescription();
                                    Log.e(TAG, "onDataChange: done with description, validating...");
                                    newForm.validate();
                                    Log.e(TAG, "onDataChange: saving to cloud");
                                    newForm.saveToCloud();
                                    */
                                    InsReport.formToBeAccepted = newForm;
                                    newForm.fireBaseCatalog = fireBaseCatalog;
                                }
                                boolean exists = false;
                                for (int i = 0; i < forms.size(); i++) {
                                    if (forms.get(i).id.equals(newForm.id)) {
                                        forms.set(i,newForm);
                                        exists = true;
                                    }
                                    if (InsReport.formToBeAccepted != null &&
                                            InsReport.formToBeAccepted.id.equals(forms.get(i).id))
                                        InsReport.formToBeAccepted = newForm;
                                }
                                if (!exists)
                                    forms.add(newForm);
                            } catch (Exception e) {
                                Log.e(TAG, "onDataChange: PROBLEMS CASTING FROM DB!!! : " + postSnapshot.getKey() + ", " + e.getMessage());
                            }
                        }

                        Log.e(TAG, "onDataChange: Sorting...");
                        Collections.sort(forms, new Comparator<Form>() {
                            @Override
                            public int compare(Form lhs, Form rhs) {
                                return (!lhs.dateCreated.after(rhs.dateCreated)?1:-1);
                            }
                        });

                        Log.e(TAG, "onDataChange: Successful! Updating list view if needed...");
                        if (InsReport.currentListView != null &&
                                InsReport.currentListView.getAdapter() != null)
                            ((FormsListAdapter)(InsReport.currentListView.getAdapter())).notifyDataSetChanged();

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
