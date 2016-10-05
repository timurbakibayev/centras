package com.gii.insreport;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.util.Log;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.firebase.client.Firebase;
import com.firebase.client.ServerValue;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Timur_hnimdvi on 26-Jul-16.
 */
public class Form {
    private static final String TAG = "Form.java";
    public ArrayList<Element> elements = new ArrayList<>();

    public String id = "";

    public String fireBaseCatalog = "";
    public Date dateCreated = new Date();
    public Date dateModified = new Date();
    public Date dateAccepted = null;

    @JsonIgnore
    public ArrayList<String> descriptionFields = new ArrayList<>();

    public boolean formReady = false;

    public String status = "";
    public String statusNote = "";

    public String phoneNo = "";

    public String description = "";

    public String getPhoneNo() {
        return phoneNo;
    }

    public ArrayList<Element> getElements() {
        return elements;
    }

    public String getFireBaseCatalog() {
        return fireBaseCatalog;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public Date getDateModified() {
        return dateModified;
    }

    public String getDescription() {
        return description;
    }

    public String getId() {
        return id;
    }

    public boolean isFormReady() {
        return formReady;
    }

    public void generateNewId() {
        SecureRandom random = new SecureRandom();
        id = (new BigInteger(64, random).toString(32));
    }


    @Override
    public String toString() {
        String collectedData = "";
        for (int i = 0; i < elements.size(); i++) {
            String adding = elements.get(i).collectData(descriptionFields);
            collectedData = collectedData + adding;
            if (!adding.equals(""))
                if (i < elements.size() - 1)
                    collectedData = collectedData + "\n";
        }
        return collectedData;
    }

    public int numberOfPhotos() {
        int n = 0;
        for (Element element : elements) {
            n += element.numberOfPhotos();
        }
        return n;
    }

    public boolean signed() {
        for (Element element : elements)
            if (!element.signed())
                return false;
        return true;
    }

    @JsonIgnore
    public static Firebase ref = InsReport.ref;

    public void saveToCloud() {
        updateDescription();
        if (!fireBaseCatalog.equals("")) {
            ref.child("forms/" + fireBaseCatalog + "/" + InsReport.forceUserID() + "/" + id).
                    setValue(this);
            //TODO: ONLY IF IT REALLY WAS MODIFIED!
            ref.child("forms/" + fireBaseCatalog + "/" + InsReport.forceUserID() + "/" + id + "/dateModified").
                    setValue(ServerValue.TIMESTAMP);
            Log.e(TAG, "saveToCloud: " + "forms/" + fireBaseCatalog + "/" + InsReport.forceUserID() + "/" + id);
        } else
            Log.e(TAG, "NOT saveToCloud: " + id);
        //now save raw data (ungroup and save):

    }

    public void updateDescription() {
        description = id + " ";
        String firebaseFieldClientName = "CLIENT_NAME";
        for (Element element : elements) {
            if (element.type == Element.ElementType.eGroup) {
                for (Element _element : element.elements) {
                    //...
                    if (descriptionFields.contains(_element.fireBaseFieldName))
                        description = description + " " + _element.toString();
                    if (_element.fireBaseFieldName.equals("CLAIMANT_PHONE_NO"))
                        phoneNo = _element.toString();
                    if (element.fireBaseFieldName.equals(firebaseFieldClientName))
                        description = element.toString();
                }
            }
            //...
            if (descriptionFields.contains(element.fireBaseFieldName))
                description = description + " " + element.toString();
            if (element.fireBaseFieldName.equals("CLAIMANT_PHONE_NO"))
                phoneNo = element.toString();
            if (element.fireBaseFieldName.equals(firebaseFieldClientName))
                description = element.toString();
        }
        if (input.get(firebaseFieldClientName) != null &&
                !input.get(firebaseFieldClientName).equals(""))
            description = input.get(firebaseFieldClientName);
        if (description.trim().equals(""))
            description = "Новая форма";
    }

    @JsonIgnore
    Element rawData = new Element("raw",Element.ElementType.eGroup,"rawData","");


    Map<String, String> rawMap = new HashMap<String, String>();

    Map<String, String> input = new HashMap<String, String>();

    public Map<String, String> getInput() {
        return input;
    }

    public Map<String, String> getRawMap() {
        return rawMap;
    }

    public void validate() {
        copyElementsRaw(rawMap, elements);
        //if (signed())
        //    formReady = true;
    }

    private void copyElementsRaw(Map<String,String> destinationMapping, ArrayList<Element> elements) {
        for (Element element : elements) {
            if (element.type == Element.ElementType.eGroup)
                copyElementsRaw(destinationMapping,element.elements);
            else
                destinationMapping.put(element.fireBaseFieldName,element.toJson());
        }
    }

    public void applyInput(ArrayList<Element> elements) {
        for (Element element : elements) {
            if (element.type == Element.ElementType.eGroup)
                applyInput(element.elements);
            else
                if (input.get(element.fireBaseFieldName) != null) {
                    element.serverStatic = true;
                    String value = input.get(element.fireBaseFieldName);
                    element.vText = value;
                    if (element.type == Element.ElementType.eText)
                        element.vText = value;
                    if (element.type == Element.ElementType.eDate || element.type == Element.ElementType.eDateTime) {
                        try {
                            Long t = Long.parseLong(value);
                            Date inputDate = new Date(t);
                            element.vDate = inputDate;
                        } catch (Exception e) {

                        }
                    }
                    //TODO: not tested!
                    if (element.type == Element.ElementType.eInteger)
                        try {
                            element.vInteger = Integer.parseInt(value);
                        } catch (Exception e) {};

                    //TODO: not tested!
                    if (element.type == Element.ElementType.eRadio || element.type == Element.ElementType.eCombo) {
                        for (int i = 0; i < element.comboItems.size(); i++) {
                            if (element.comboItems.get(i).equals(value)) {
                                element.vText = value;
                                element.vInteger = i;
                            }
                        }
                    }
                }
        }
    }

    public int numberOfObjects() {
        return objects.elements.size();
    }

    public int numberOfParticipants() {
        return participants.elements.size();
    }

    public Element objects = new Element();
    public Element participants = new Element();

    public Element getParticipants() {
        return participants;
    }

    public Element getObjects() {
        return objects;
    }

    public String getStatus() {
        return status;
    }

    public String getStatusNote() {
        return statusNote;
    }

    public void switchDone(Context context, boolean closeActivity, Activity activity) {
        InsReport.savePref("lastFormId","");
        if (status.equals("accept")) {
            formReady = !formReady;
            saveToCloud();
            if (formReady) {
                InsReport.mainActivity.askReadyResult(this, context, closeActivity, activity);
                NotificationManager nMgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                nMgr.cancel(calculateId());
                Log.e(TAG, "switchDone: " + calculateId());
            }
            else {
                statusNote = "В работе";
                saveToCloud();
            }
            InsReport.notifyFormsList();
        }
    }

    public int calculateId() {
        if (input.get("CLAIMANT_PHONE_NO") != null)
            phoneNo = input.get("CLAIMANT_PHONE_NO");
        return hash(phoneNo);

    }

    public static int hash (String phoneNo) {
        int idk = 0;
        for (int i = 0; i < phoneNo.length(); i++) {
            int k = phoneNo.charAt(i);
            k = k * (i *100);
            idk += k;
        }
        return idk;
    }
}
