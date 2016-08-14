package com.gii.insreport;

import java.util.ArrayList;

/**
 * Created by Timur on 06-Jul-16.
 */
public class Frame {
    ArrayList<Stroke> strokes = new ArrayList<>();
    ArrayList<Icon> icons = new ArrayList<>();
    ArrayList<Operation> operations = new ArrayList<>();

    public ArrayList<Stroke> getStrokes() {
        return strokes;
    }

    public ArrayList<Icon> getIcons() {
        return icons;
    }

    public ArrayList<Operation> getOperations() {
        return operations;
    }

    public Frame() {

    }

    public Frame(Frame copyFromFrame) {
        strokes = new ArrayList<>(copyFromFrame.strokes);
        //icons = new ArrayList<>(copyFromFrame.icons);
        icons = new ArrayList<>();
        for (Icon icon : copyFromFrame.icons) {
            icons.add(new Icon(icon));
        }
        operations = new ArrayList<>(copyFromFrame.operations);
    }

    public void undo() {
        if (operations.size() == 0)
            return;
        Operation lastOperation = operations.get(operations.size() - 1);
        if (lastOperation.operationType.equals("new stroke")) {
            if (strokes.size() > 0) {
                strokes.remove(strokes.size() - 1);
            }
        }
        if (lastOperation.operationType.equals("new icon")) {
            if (icons.size() > 0) {
                icons.remove(icons.size() - 1);
            }
        }
        if (lastOperation.operationType.equals("move")) {
            icons.get(lastOperation.newIdInArray).left = lastOperation.oldIcon.left;
            icons.get(lastOperation.newIdInArray).right = lastOperation.oldIcon.right;
            icons.get(lastOperation.newIdInArray).top = lastOperation.oldIcon.top;
            icons.get(lastOperation.newIdInArray).bottom = lastOperation.oldIcon.bottom;
            icons.get(lastOperation.newIdInArray).center = lastOperation.oldIcon.center;
            icons.get(lastOperation.newIdInArray).rotation = lastOperation.oldIcon.rotation;
        }
        operations.remove(operations.size() - 1);
    }
}
