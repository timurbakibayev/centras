package com.gii.insreport;

/**
 * Created by Timur on 07-Jul-16.
 */
public class Operation {
    public String operationType = "";
    public Stroke newStroke = new Stroke();
    public Icon newIcon = new Icon();
    public Icon oldIcon = new Icon();
    public int newIdInArray = 0;

    public String getOperationType() {
        return operationType;
    }

    public Stroke getNewStroke() {
        return newStroke;
    }

    public Icon getNewIcon() {
        return newIcon;
    }

    public Icon getOldIcon() {
        return oldIcon;
    }

    public int getNewIdInArray() {
        return newIdInArray;
    }
}
