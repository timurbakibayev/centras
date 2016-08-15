package com.gii.insreport;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

import java.util.Calendar;

/**
 * Created by Timur on 07-Jul-16.
 */
public class IconsWindow {

    private Canvas canvas;
    private float scaleFactor;

    private PointF backgroundPosition = new PointF(0, 0);
    private PointF lastBackgroundPosition = new PointF(0,0);
    private PointF canvasMovingStartingPoint = new PointF(0,0);
    private boolean moving;

    private Paint paintLevel1 = new Paint();

    private Paint paintLevel2 = new Paint();
    private Paint paintLevel3 = new Paint();
    private Paint paintGray = new Paint();

    public AnimaView animaView;

    public void init(AnimaView animaView) {
        this.animaView = animaView;

        paintGray.setColor(Color.GRAY);
        paintLevel1.setColor(Color.rgb(100,100,250));
        paintLevel2.setColor(Color.rgb(80,80,150));
        paintLevel3.setColor(Color.rgb(30,30,100));
        paintGray.setStrokeWidth(7);
        paintLevel1.setStrokeWidth(3);
        paintLevel2.setStrokeWidth(3);
        paintLevel3.setStrokeWidth(3);
        paintGray.setStyle(Paint.Style.FILL_AND_STROKE);
        paintLevel1.setStyle(Paint.Style.FILL_AND_STROKE);
        paintLevel2.setStyle(Paint.Style.FILL_AND_STROKE);
        paintLevel3.setStyle(Paint.Style.FILL_AND_STROKE);
        paintGray.setAlpha(90);
        color = -1;
        moving = false;
        backgroundPosition = new PointF(0, 0);
        pressedHere = false;
        ready = false;
        choosing = Choosing.icon;
        scaleFactor = 1;
        rectsNumber = animaView.drawableIcon.length;
        int n = Math.max(rectsNumber,colorsNumber);
        for (int i = 0; i < n; i++) {
            origRects[i] = new Rect(0,0,0,0);
            drawRects[i] = new Rect(0,0,0,0);
        }
    }

    boolean needToUpdate = false;

    float dy = 0; //vertical velocity
    float lastY0 = 0f;
    float lastY1 = 0f;
    Calendar lastY0Time = Calendar.getInstance();
    Calendar lastY1Time = Calendar.getInstance();
    public void update() {
        //return;
        if (needToUpdate) {
            backgroundPosition.set(backgroundPosition.x,backgroundPosition.y - dy);
            checkBackground();
            dy = (float)dy / 1.2f;
            if (Math.abs(dy) < 1) {
                needToUpdate = false;
            }
        }
    }

    public boolean onTouchEvent(@NonNull MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                pressedHere = true;
                canvasMovingStartingPoint.set((int) event.getX(), (int) event.getY());
                lastBackgroundPosition = backgroundPosition;
                dy = 0;
                lastY0 = event.getY();
                lastY1 = event.getY();
                lastY0Time = Calendar.getInstance();
                lastY1Time = Calendar.getInstance();
                needToUpdate = false;
                break;
            case MotionEvent.ACTION_MOVE:
                if (pressedHere &&
                        Math.sqrt((canvasMovingStartingPoint.x - event.getX()) * (canvasMovingStartingPoint.x - event.getX()) +
                                (canvasMovingStartingPoint.y - event.getY()) * (canvasMovingStartingPoint.y - event.getY())) > 10)
                    moving = true;
                if (pressedHere && moving) {
                    backgroundPosition.set(lastBackgroundPosition.x + (canvasMovingStartingPoint.x - event.getX()), lastBackgroundPosition.y + (canvasMovingStartingPoint.y - event.getY()));
                    backgroundPosition = new PointF(lastBackgroundPosition.x + (canvasMovingStartingPoint.x - event.getX()), lastBackgroundPosition.y + (canvasMovingStartingPoint.y - event.getY()));
                    checkBackground();
                }
                dy = 0;
                lastY0 = lastY1;
                lastY0Time.setTime(lastY1Time.getTime());
                lastY1 = event.getY();
                lastY1Time = Calendar.getInstance();
                needToUpdate = false;
                break;
            case MotionEvent.ACTION_UP:
                if (pressedHere && !moving) {
                    click(event.getX(), event.getY());
                }
                if (pressedHere && moving) {
                    dy = (lastY1-lastY0) / (lastY1Time.getTimeInMillis() - lastY0Time.getTimeInMillis()) * 100;
                    needToUpdate = true;
                }
                moving = false;
                break;
            default:
                return false;
        }
        return true;
    }

    private void checkBackground() {
        int n = 0;
        if (choosing == Choosing.icon)
            n = rectsNumber;
        if (choosing == Choosing.color)
            n = colorsNumber;
        Rect border = new Rect(origRects[0]);
        for (int i = 0; i < n; i++) {
            if (origRects[i].right > border.right)
                border.set(border.left,border.top, origRects[i].right,border.bottom);
            if (origRects[i].bottom > border.bottom)
                border.set(border.left,border.top,border.right, origRects[i].bottom);
        }
        if (border.bottom - backgroundPosition.y < animaView.canvasHeight)
            backgroundPosition.y = border.bottom - animaView.canvasHeight;
        if (backgroundPosition.y < 0)
            backgroundPosition.y = 0;
    }

    private void click(float x, float y) {
        for (int i = 0; i < rectsNumber; i++) {
            if (drawRects[i].contains((int) x, (int) y)) {
                pictureNo = i;
                choosing = Choosing.color;
                backgroundPosition = new PointF(0, 0);
                ready = true;
                pressedHere = false;
                Icon newIcon = new Icon();
                newIcon.picId = i;
                //newIcon.drawRect = new Rect(30,30,70,70);
                newIcon.center = new PointF(0,0);
                newIcon.left = - AnimaView.drawableSize[i].x / 2;
                newIcon.top = - AnimaView.drawableSize[i].y / 2;
                newIcon.right = + AnimaView.drawableSize[i].x / 2;
                newIcon.bottom = + AnimaView.drawableSize[i].y / 2;
                animaView.currentFrame.icons.add(newIcon);
                Operation newOperation = new Operation();
                newOperation.operationType = "new icon";
                animaView.currentFrame.operations.add(newOperation);
                animaView.appState = AnimaView.AppState.idle;
                animaView.postInvalidate();
                return;
            }
        }
    }

    public boolean onScale(ScaleGestureDetector detector) {
        scaleFactor *= detector.getScaleFactor();
        scaleFactor = Math.max(0.1f, Math.min(scaleFactor, 4));
        if (scaleFactor <= 0.1f)
            animaView.appState = AnimaView.AppState.idle;
        return true;
    }

    int[] catOrder = {0,1,2,3};

    protected void onDraw(Canvas canvas) {
        int diameter = Math.min((int)canvas.getWidth(),(int)canvas.getHeight())/5;
        int x = 10;
        int y = 10;
        int offset = (int)(diameter * 0.15);
            for (int cat = 0; cat < 4; cat++) {
                for (int i = 0; i < rectsNumber; i++) {
                    if (Math.abs(animaView.drawableIconCategory[i]) == catOrder[cat]) {
                        if (x + diameter > animaView.canvasWidth) {
                            x = 10;
                            y += diameter;
                        }
                        origRects[i].set(x, y, x + diameter, y + diameter);
                        drawRects[i].set(x + offset, y + offset - (int) backgroundPosition.y, x + diameter - offset, y + diameter - offset - (int) backgroundPosition.y);
                        //graphics.drawIcon(null, i, (i * 3) % graphics.circleColor.length, (i * 3) % graphics.circleColor.length, drawRects[i], 0, false, canvas);
                        animaView.drawableIcon[i].setBounds(drawRects[i]);
                        animaView.drawableIcon[i].draw(canvas);
                        x += diameter;
                    }
                }
                x = 10;
                canvas.drawLine(0, y + diameter - (int) backgroundPosition.y, animaView.canvasWidth, y + diameter - (int) backgroundPosition.y,
                        paintLevel1);
                canvas.drawLine(0, y - 3 + diameter - (int) backgroundPosition.y, animaView.canvasWidth, y + diameter - (int) backgroundPosition.y - 3,
                        paintLevel2);
                canvas.drawLine(0, y + 3 + diameter - (int) backgroundPosition.y, animaView.canvasWidth, y + diameter - (int) backgroundPosition.y + 3,
                        paintLevel3);
                y += diameter;
                //y += diameter;
            }
    }

    ScaleGestureDetector _scaleDetector;

    public Rect[] origRects = new Rect[500];
    public Rect[] drawRects = new Rect[500];
    public int rectsNumber = 0;
    public int colorsNumber = 0;
    public boolean ready = false;
    public int pictureNo = 0;
    public int color = -1;

    public boolean pressedHere = false;

    public enum Choosing {
        icon, color
    }

    public Choosing choosing = Choosing.icon;

}
