package com.gii.insreport;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Timur_hnimdvi on 26-Jul-16.
 */
public class Element {
    private static final String TAG  = "Element.java";

    @JsonIgnore
    public ArrayList<Bitmap> bitmapsToBeAddedOnResult = new ArrayList<>();

    public Element() {

    }
    public Element(ElementType elementType, String fireBaseFieldName, String prompt) {
        this.type = elementType;
        this.fireBaseFieldName = fireBaseFieldName;
        this.description = prompt;
        this.content = "";
    }

    public Element(ElementType elementType, String fireBaseFieldName, String prompt, String[] comboItems) {
        this.type = elementType;
        this.fireBaseFieldName = fireBaseFieldName;
        this.description = prompt;
        this.content = "";
        this.comboItems.add("");
        for (int i = 0; i < comboItems.length; i++)
            this.comboItems.add(comboItems[i]);
        this.vInteger = -1;
        //if (comboItems.length > 0)
        this.vText = "";
    }

    public Element(ElementType elementType, String fireBaseFieldName, String prompt, String directory) {
        this.type = elementType;
        this.fireBaseFieldName = fireBaseFieldName;
        this.description = prompt;
        this.content = "";
        this.directory = directory;
        /*
        for (int i = 0; i < comboItems.length; i++)
            this.comboItems.add(comboItems[i]);
            */
        this.vInteger = -1;
        //if (comboItems.length > 0)
        this.vText = "";
    }

    public Element(ArrayList<Element> elements, String fireBaseFieldName, String prompt) {
        this.type = ElementType.eGroup;
        this.elements = elements;
        this.description = prompt;
        this.fireBaseFieldName = fireBaseFieldName;
        this.content = "";
    }

    String value = "";

    public String getValue() {
        return value;
    }

    public Element(Element element) {
        this.description = element.description;
        this.type = element.type;
        this.vDate = element.vDate;
        this.vBoolean = element.vBoolean;
        this.vDraw = element.vDraw;
        this.vInteger = element.vInteger;
        this.vPhotos = new ArrayList<>(element.vPhotos);
        this.vText = element.vText;
        this.fireBaseFieldName = element.fireBaseFieldName;
        this.content = element.toString();
    }

    public String collectData(ArrayList<String> descriptionFields) {
        if (type != ElementType.eGroup)
            return toString();
        String collectedData = "";
        for (int i = 0; i < elements.size(); i++) {
            String adding = elements.get(i).toString();
            if (descriptionFields.contains(elements.get(i).fireBaseFieldName))
                adding = "";
            collectedData = collectedData + adding;
            if (!adding.equals(""))
                if (i < elements.size() - 1)
                    collectedData = collectedData + ", ";
        }
        return collectedData;
    }

    @Override
    public String toString() {
        switch (type) {
            case eText:
                return vText;
            case eDate:
                return (FillFormActivity.dateOnlyText(vDate));
            case eDateTime:
                return (FillFormActivity.dateTimeText(vDate));
            case eInteger:
                return ("" + vInteger);
            case ePlan:
                return vPlan.damageDescription;
            case eDraw:
                return ((vDraw.size() == 0)?"":"Рисунок");
            case eSignature:
                return ((vDraw.size() == 0)?"":"Подписано");
            case eBoolean:
                return (vBoolean?description:"");
            case eRadio:
            case eCombo:
                return (vText);
        }
        return "";
    }

    public String toJson() {
        switch (type) {
            case eText:
                return vText;
            case eDate:
                return (FillFormActivity.dateJson(vDate));
            case eDateTime:
                return (FillFormActivity.dateTimeJson(vDate));
            case eInteger:
                return ("" + vInteger);
            case ePlan:
                return (vPlan.damageDescription);
            case eDraw:
                return ((vDraw.size() == 0)?"":"Рисунок");
            case eSignature:
                return ((vDraw.size() == 0)?"":"Подписано");
            case eBoolean:
                return (vBoolean?"true":"false");
            case ePhoto:
                return (vPhotos.size() + " фото");
            case eRadio:
            case eCombo:
                return (vText);
        }
        return "";
    }

    public int numberOfPhotos() {
        int n = 0;
        if (type == ElementType.ePhoto)
            n = vPhotos.size();
        if (type == ElementType.eGroup) {
            for (Element element : elements) {
                n += element.numberOfPhotos();
            }
        }
        return n;
    }

    public boolean signed() {
        if (type == ElementType.eSignature && vDraw.size() == 0) {
            //Log.e(TAG, "not signed: " + description );
            return false;
        }
        if (type == ElementType.eGroup) {
            for (Element element : elements)
                if (!element.signed())
                    return false;
        }
        return true;
    }

    public int filled() {
        int n = 0;
        if (type == ElementType.ePhoto) {
            n = vPhotos.size();
            return  (n>0?1:0);
        }
        if (type == ElementType.eGroup) {
            for (Element element : elements) {
                n += element.filled();
            }
        } else
            if (!toString().equals(""))
                n++;
        return (n);
    }

    public int outOf() {
        int n = 0;
        if (type == ElementType.eGroup) {
            for (Element element : elements) {
                n += element.outOf();
            }
        } else
            n++;
        return (n);
    }

    public enum ElementType {
        eInteger, eBoolean, ePhoto, eText, eCombo, eRadio, eDate, eDateTime, eGroup, eDraw, ePlan, eSignature, eAnima, eLookUp;
    }

    @JsonIgnore
    public View container = null;

    public String content = "";
    public String directory = "";
    public ArrayList<String> comboItems = new ArrayList<>();
    public ArrayList<Frame> frames = new ArrayList<>();
    public ElementType type = ElementType.eText;
    public String fireBaseFieldName = "";
    public String description = "";
    public int vInteger = -1;
    public boolean vBoolean = false;
    public String vText = "";
    public DamagePlanData vPlan = new DamagePlanData();
    public ArrayList<String> vPhotos = new ArrayList<>();
    public Date vDate;
    public ArrayList<Element> elements = new ArrayList<>();
    public ArrayList<ArrayList<PointF>> vDraw = new ArrayList<>();
    @JsonIgnore
    public Bitmap freeDrawBitmap = null;
    @JsonIgnore
    BitmapDrawable myIcon = null;

    public BitmapDrawable getBitmapDrawable(Context context) {
        if (freeDrawBitmap == null || freeDrawBitmap.isRecycled())
            myIcon = new BitmapDrawable(context.getResources(), getFreeDrawBitmap());
        return myIcon;
    }

    public Bitmap getFreeDrawBitmap() {
        if (freeDrawBitmap == null || freeDrawBitmap.isRecycled()) {
            freeDrawBitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
            if (vDraw.size() > 0) {
                Paint blue = new Paint();
                blue.setColor(Color.BLUE);
                blue.setAlpha(255);
                blue.setStrokeWidth(1);
                blue.setStyle(Paint.Style.STROKE);
                Path currPath = new Path();
                PointF minPoint = new PointF(-1,-1);
                PointF maxPoint = new PointF(-1,-1);
                for (ArrayList<PointF> point : vDraw) {
                    if (point.size() > 0) {
                        for (PointF nextPoint : point) {
                            if (minPoint.x == -1 || minPoint.x > nextPoint.x)
                                minPoint.set(nextPoint.x, minPoint.y);
                            if (minPoint.y == -1 || minPoint.y > nextPoint.y)
                                minPoint.set(minPoint.x, nextPoint.y);
                            if (maxPoint.x == -1 || maxPoint.x < nextPoint.x)
                                maxPoint.set(nextPoint.x, maxPoint.y);
                            if (maxPoint.y == -1 || maxPoint.y < nextPoint.y)
                                maxPoint.set(maxPoint.x, nextPoint.y);
                        }
                    }
                }
                if (maxPoint.x == -1 || maxPoint.x - minPoint.x == 0 || maxPoint.y - minPoint.y == 0 ||
                        (maxPoint.x - minPoint.x) * (maxPoint.y - minPoint.y) == 0) //something's wrong
                    freeDrawBitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
                else {
                    freeDrawBitmap = Bitmap.createBitmap(150, (int) (150 * ((maxPoint.y - minPoint.y) / (maxPoint.x - minPoint.x))), Bitmap.Config.ARGB_8888);
                    Canvas canvas = new Canvas(freeDrawBitmap);
                    for (ArrayList<PointF> point : vDraw) {
                        if (point.size() > 0) {
                            currPath.reset();
                            int i = 0;
                            for (PointF nextPoint : point) {
                                PointF nextPointAdjustedToBorders = new PointF((nextPoint.x - minPoint.x) * (1 / (maxPoint.x - minPoint.x)),
                                        (nextPoint.y - minPoint.y) * (1 / (maxPoint.y - minPoint.y)));
                                if (i == 0)
                                    currPath.moveTo((int) (canvas.getWidth() * nextPointAdjustedToBorders.x), (int) (canvas.getHeight() * nextPointAdjustedToBorders.y));
                                else
                                    currPath.lineTo((int) (canvas.getWidth() * nextPointAdjustedToBorders.x), (int) (canvas.getHeight() * nextPointAdjustedToBorders.y));
                                i++;
                            }
                            canvas.drawPath(currPath, blue);
                        }
                    }
                }
            }
        }
        return freeDrawBitmap;
    }

    //all these getters we need to satisfy FireBase requirements

    public DamagePlanData getvPlan() {
        return vPlan;
    }

    public ArrayList<ArrayList<PointF>> getvDraw() {
        return vDraw;
    }

    public ElementType getType() {
        return type;
    }

    public String getFireBaseFieldName() {
        return fireBaseFieldName;
    }

    public String getDescription() {
        return description;
    }

    public int getvInteger() {
        return vInteger;
    }

    public boolean isvBoolean() {
        return vBoolean;
    }

    public ArrayList<String> getvPhotos() {
        return vPhotos;
    }

    public Date getvDate() {
        return vDate;
    }

    public ArrayList<Element> getElements() {
        return elements;
    }

    public String getvText() {
        return vText;
    }

    public String getContent() {
        return content;
    }

    public ArrayList<String> getComboItems() {
        return comboItems;
    }

    public String getDirectory() {
        return directory;
    }

    public ArrayList<Frame> getFrames() {
        return frames;
    }
}
