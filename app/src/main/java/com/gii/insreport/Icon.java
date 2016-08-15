package com.gii.insreport;

import android.graphics.PointF;

/**
 * Created by Timur on 07-Jul-16.
 */
public class Icon {
    //@JsonIgnore
    //public Rect drawRect() = new Rect(0,0,1,1);

    public Integer picId = 0;
    public Integer rotation = 0;
    public PointF center = new PointF(0,0);

    public float left = 0;
    public float top = 0;
    public float right = 0;
    public float bottom = 0;

    public boolean aCopy = false;

    public String id = AnimaView.generateNewId();

    public boolean isaCopy() {
        return aCopy;
    }

    public float getLeft() {
        return left;
    }

    public float getTop() {
        return top;
    }

    public float getRight() {
        return right;
    }

    public float getBottom() {
        return bottom;
    }

    public Integer getPicId() {
        return picId;
    }

    public Integer getRotation() {
        return rotation;
    }

    public PointF getCenter() {
        return center;
    }

    public String getId() {
        return id;
    }

    public Icon(Icon copyIcon) {
        //this.drawRect = new Rect(copyIcon.drawRect.left,copyIcon.drawRect.top,copyIcon.drawRect.right,copyIcon.drawRect.bottom);
        this.left = copyIcon.left;
        this.top = copyIcon.top;
        this.right = copyIcon.right;
        this.bottom = copyIcon.bottom;
        this.picId = copyIcon.picId;
        this.rotation = copyIcon.rotation;
        this.center.set(copyIcon.center.x,copyIcon.center.y);
        this.id = copyIcon.id;
        this.aCopy = true;
    }
    public Icon() {

    }

}
