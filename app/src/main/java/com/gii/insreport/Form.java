package com.gii.insreport;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.firebase.client.Firebase;

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

    @JsonIgnore
    public ArrayList<String> descriptionFields = new ArrayList<>();

    public boolean formReady = false;

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

    public int filledPercent() {
        int n = 0;
        int outOf = 0;
        for (Element element : elements) {
            n += element.filled();
            outOf += element.outOf();
        }
        //Log.e(TAG, "filledPercent: " + n + "/" + outOf );
        //Log.e(TAG, "filledPercent result: " + (int)((float)n/outOf*100) );
        return (int)((float)n/outOf*100);
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
        ref.child("forms/" + fireBaseCatalog + "/" + InsReport.user.getUid() + "/" + id).
                setValue(this);
        //now save raw data (ungroup and save):

    }

    public void updateDescription() {
        description = id + " ";
        for (Element element : elements) {
            if (element.type == Element.ElementType.eGroup) {
                for (Element _element : element.elements) {
                    //...
                    if (descriptionFields.contains(_element.fireBaseFieldName))
                        description = description + " " + _element.toString();
                    if (_element.fireBaseFieldName.equals("phone"))
                        phoneNo = _element.toString();
                }
            }
            //...
            if (descriptionFields.contains(element.fireBaseFieldName))
                description = description + " " + element.toString();
            if (element.fireBaseFieldName.equals("phone"))
                phoneNo = element.toString();
        }
        if (description.trim().equals(""))
            description = "Новая форма";
    }

    @JsonIgnore
    Element rawData = new Element(Element.ElementType.eGroup,"rawData","");


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
                    String value = input.get(element.fireBaseFieldName);
                    if (element.type == Element.ElementType.eText)
                        element.vText = value;
                    if (element.type == Element.ElementType.eDate || element.type == Element.ElementType.eDateTime) {
                        try {
                            Long t = Long.parseLong(value);
                            Date inputDate = new Date(t);
                            element.vDate = inputDate;
                        } catch (Exception e) {

                        } ;
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
}
