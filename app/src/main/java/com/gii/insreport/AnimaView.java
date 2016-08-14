package com.gii.insreport;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Timur on 06-Jul-16.
 */
public class AnimaView extends View {

    int playToPercent = 0;
    int playToFrame = -1;
    int intermediateFrames = 10;
    public void playTo(int frameTo) {
        if (playToFrame != frameTo) {
            playToPercent = 0;
            playToFrame = frameTo;
        }
        playToPercent++;
        if (playToPercent > intermediateFrames) {
            playToPercent = 0;
            animaActivity.frameNo++;
            if (animaActivity.frameNo >= animaActivity.frames.size()) {
                animaActivity.frameNo = animaActivity.frames.size() - 1;
                animaActivity.play = false;
            }
            animaActivity.updateFrameNo();
        }
        postInvalidate();
    }

    public enum AppState {
        idle, chooseIcon, positionIcon, moveIcon, rotateIcon
    }
    public AppState appState = AppState.idle;

    static public Drawable[] drawableIcon;
    static public Integer[] drawableIconCategory;

    public int canvasWidth = 1;
    public int canvasHeight = 1;

    private String TAG = "AnimaView";

    public Stroke currentStroke = new Stroke();
    public Frame currentFrame = null;
    public ArrayList<Frame> frames = new ArrayList<>();

    ScaleGestureDetector _scaleDetector;
    AnimaActivity animaActivity;
    Paint white = new Paint();
    Paint gray = new Paint();
    Paint green = new Paint();
    Path path = new Path();
    Rect drawRect = new Rect(0,0,1,1);
    Point center = new Point(0,0);
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvasWidth = canvas.getWidth();
        canvasHeight = canvas.getHeight();

        if (appState == AppState.idle || appState == AppState.positionIcon ||
                appState == AppState.moveIcon || appState == AppState.rotateIcon) {
            if (currentFrame.strokes.size() > 0) {
                for (Stroke stroke : currentFrame.strokes) {
                    if (stroke.points.size() > 0) {
                        path.reset();
                        path.moveTo(stroke.points.get(0).x, stroke.points.get(0).y);
                        for (Point point : stroke.points)
                            path.lineTo(point.x, point.y);
                        canvas.drawPath(path, gray);
                    }
                }
            }
            if (currentFrame.icons.size() > 0) {
                for (Icon icon : currentFrame.icons) {
                    int rotation = icon.rotation;
                    center.set(icon.center.x,icon.center.y);
                    drawRect.set(icon.left,icon.top,icon.right,icon.bottom);
                    if (animaActivity.play) {
                        for (Icon _icon : animaActivity.frames.get(playToFrame).icons) {
                            if (_icon.id.equals(icon.id)) {
                                //Log.e(TAG, "onDraw: Found same icon" );
                                rotation = icon.rotation + (_icon.rotation - icon.rotation) / intermediateFrames * playToPercent;
                                drawRect.set(
                                        icon.left + (_icon.left - icon.left) / intermediateFrames * playToPercent,
                                        icon.top + (_icon.top - icon.top) / intermediateFrames * playToPercent,
                                        icon.right + (_icon.right - icon.right) / intermediateFrames * playToPercent,
                                        icon.bottom + (_icon.bottom - icon.bottom) / intermediateFrames * playToPercent
                                );
                                center.set(icon.center.x + (_icon.center.x - icon.center.x) / intermediateFrames * playToPercent,
                                        icon.center.y + (_icon.center.y - icon.center.y) / intermediateFrames * playToPercent);
                            }
                        }
                    }

                    if (rotation != 0) {
                        canvas.save();
                        canvas.rotate(rotation,center.x,center.y);
                    }
                    drawableIcon[icon.picId].setBounds(drawRect);
                    drawableIcon[icon.picId].draw(canvas);
                    if (rotation != 0)
                        canvas.restore();
                }
            }
            if (appState == AppState.moveIcon || appState == AppState.rotateIcon) {
                canvas.drawRect(0,canvasHeight * 9 / 10, canvasWidth, canvasHeight, green);
            }
        }

        if (appState == AppState.chooseIcon) {
            animaActivity.iconsWindow.onDraw(canvas);
        }
    }

    public AnimaView(Context context) {
        super(context);
        _scaleDetector = new ScaleGestureDetector(this.getContext(), new ScaleListener());
    }

    public void loadResources(Context context) {
        white = new Paint();
        white.setColor(Color.WHITE);
        white.setStrokeWidth(2);
        white.setStyle(Paint.Style.STROKE);
        gray = new Paint();
        gray.setColor(Color.DKGRAY);
        gray.setStrokeWidth(10);
        gray.setStyle(Paint.Style.STROKE);
        green = new Paint();
        green.setColor(Color.GREEN);
        green.setAlpha(100);
        green.setStrokeWidth(2);
        green.setStyle(Paint.Style.FILL_AND_STROKE);
        drawableIcon = new Drawable[19];
        drawableIconCategory = new Integer[drawableIcon.length];
        int iconNo = 0;
        drawableIconCategory[iconNo] = 0; drawableIcon[iconNo] = ContextCompat.getDrawable(context, R.drawable.__minivan); iconNo++;
        drawableIconCategory[iconNo] = 0; drawableIcon[iconNo] = ContextCompat.getDrawable(context, R.drawable.__police); iconNo++;
        drawableIconCategory[iconNo] = 0; drawableIcon[iconNo] = ContextCompat.getDrawable(context, R.drawable.__racecar); iconNo++;
        drawableIconCategory[iconNo] = 1; drawableIcon[iconNo] = ContextCompat.getDrawable(context, R.drawable.__trafficlights); iconNo++;
        drawableIconCategory[iconNo] = 0; drawableIcon[iconNo] = ContextCompat.getDrawable(context, R.drawable.__car_icon); iconNo++;
        drawableIconCategory[iconNo] = 0; drawableIcon[iconNo] = ContextCompat.getDrawable(context, R.drawable.__top_car_figure_color); iconNo++;
        drawableIconCategory[iconNo] = 0; drawableIcon[iconNo] = ContextCompat.getDrawable(context, R.drawable.limousine); iconNo++;
        drawableIconCategory[iconNo] = 2; drawableIcon[iconNo] = ContextCompat.getDrawable(context, R.drawable.qdoppellinie); iconNo++;
        drawableIconCategory[iconNo] = 2; drawableIcon[iconNo] = ContextCompat.getDrawable(context, R.drawable.qgeradeaus); iconNo++;
        drawableIconCategory[iconNo] = 2; drawableIcon[iconNo] = ContextCompat.getDrawable(context, R.drawable.qkreisverkehr); iconNo++;
        drawableIconCategory[iconNo] = 2; drawableIcon[iconNo] = ContextCompat.getDrawable(context, R.drawable.crossroad); iconNo++;
        drawableIconCategory[iconNo] = 2; drawableIcon[iconNo] = ContextCompat.getDrawable(context, R.drawable.qvierlinie); iconNo++;
        drawableIconCategory[iconNo] = 3; drawableIcon[iconNo] = ContextCompat.getDrawable(context, R.drawable.ahaltverbot); iconNo++;
        drawableIconCategory[iconNo] = 3; drawableIcon[iconNo] = ContextCompat.getDrawable(context, R.drawable.akreis_zeichen); iconNo++;
        drawableIconCategory[iconNo] = 3; drawableIcon[iconNo] = ContextCompat.getDrawable(context, R.drawable.astop); iconNo++;
        drawableIconCategory[iconNo] = 3; drawableIcon[iconNo] = ContextCompat.getDrawable(context, R.drawable.austupi); iconNo++;
        drawableIconCategory[iconNo] = 1; drawableIcon[iconNo] = ContextCompat.getDrawable(context, R.drawable.sred); iconNo++;
        drawableIconCategory[iconNo] = 1; drawableIcon[iconNo] = ContextCompat.getDrawable(context, R.drawable.sgreen); iconNo++;
        drawableIconCategory[iconNo] = 1; drawableIcon[iconNo] = ContextCompat.getDrawable(context, R.drawable.syellow); iconNo++;
    }

    public void bindActivity(AnimaActivity mainActivity) {
        this.animaActivity = mainActivity;
        //if (mainActivity.prefs.getString("AndroidID", "").equals("")) {
        //    SharedPreferences.Editor edit = mainActivity.prefs.edit();
        //    edit.putString("AndroidID", generateNewId());
        //    edit.commit();
       // }
    }


    public static String generateNewId() {
        SecureRandom random = new SecureRandom();
        return (new BigInteger(64, random).toString(32));
    }


    public AnimaView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public AnimaView(Context context, AttributeSet attributeSet, int defStyle) {
        super(context, attributeSet, defStyle);
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            //properties.scaleFactor *= detector.getScaleFactor();
            if (appState == AppState.moveIcon || appState == AppState.rotateIcon) {
                int lastWidth = movingIcon.center.x - movingIcon.left;
                int lastHeight = movingIcon.center.y - movingIcon.top;
                movingIcon.left = movingIcon.center.x - Math.max((int)(lastWidth * detector.getScaleFactor()),1);
                movingIcon.right = movingIcon.center.x + Math.max((int)(lastWidth * detector.getScaleFactor()),1);
                movingIcon.top = movingIcon.center.y - Math.max((int)(lastHeight * detector.getScaleFactor()),1);
                movingIcon.bottom = movingIcon.center.y + Math.max((int)(lastHeight * detector.getScaleFactor()),1);
                copyAhead(movingIcon);
            }
            return true;
        }
    }

    public boolean scaling = false;

    Calendar lastTime = null;

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        if (appState == AppState.idle || appState == AppState.moveIcon || appState == AppState.rotateIcon)
            return onTouchEventIdle(event);

        if (appState == AppState.positionIcon)
            return onTouchEventPositionIcon(event);

        if (appState == AppState.chooseIcon) {
            return animaActivity.iconsWindow.onTouchEvent(event);
        }

        return false;
    }

    Icon movingIcon = null;
    int movingIconId = 0;
    int startedRotationAt = 0;
    Point relativePoint = new Point(0,0);

    public boolean onTouchEventIdle(@NonNull MotionEvent event) {
        _scaleDetector.onTouchEvent(event);
        if (_scaleDetector.isInProgress()) {
            scaling = true;
            postInvalidate();
            return true;
        }

        Calendar time = Calendar.getInstance();
        //lastTime = Calendar.getInstance();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                int i = 0;
                if (appState == AppState.moveIcon && event.getY() > canvasHeight * 9 / 10) {
                    //rotation zone
                    startedRotationAt = (int)event.getX();
                    appState = AppState.rotateIcon;
                } else
                    appState = AppState.idle;

                if (!(appState == AppState.rotateIcon))
                for (Icon icon : currentFrame.icons) {
                    drawRect.set(icon.left,icon.top,icon.right,icon.bottom);
                    if (drawRect.contains((int) event.getX(), (int) event.getY())) {
                        Operation moveOperation;
                        movingIcon = icon;
                        movingIconId = i;
                        relativePoint.set((int) event.getX() - movingIcon.center.x,
                                (int) event.getY() - movingIcon.center.y);
                        appState = AppState.moveIcon;
                        moveOperation = new Operation();
                        moveOperation.operationType = "move";
                        moveOperation.newIdInArray = i;
                        moveOperation.oldIcon = new Icon(icon);
                        currentFrame.operations.add(moveOperation);
                    }
                    i++;
                }

                if (appState == AppState.idle) {
                    currentStroke = new Stroke();
                    currentFrame.strokes.add(currentStroke);
                    Operation newOperation = new Operation();
                    newOperation.operationType = "new stroke";
                    currentFrame.operations.add(newOperation);

                    currentStroke.points.add(new Point((int) event.getX(), (int) event.getY()));
                    currentStroke.intervals.add(0);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (appState == AppState.idle) {
                    currentStroke.points.add(new Point((int) event.getX(), (int) event.getY()));

                    currentStroke.intervals.add((int) (time.getTimeInMillis() - lastTime.getTimeInMillis()));
                };
                if (appState == AppState.moveIcon) {
                    int width = movingIcon.center.x - movingIcon.left;
                    int height = movingIcon.center.y - movingIcon.top;
                    Point center = movingIcon.center;

                    //currentFrame.icons.get(movingIconId).drawRect.set(center.x - width, center.y - height, center.x + width, center.y + height);
                    drawRect.set((int)event.getX() - width, (int)event.getY() - height,
                            (int)event.getX() + width, (int)event.getY() + height);
                    movingIcon.left = (int)event.getX() - width - relativePoint.x;
                    movingIcon.right = (int)event.getX() + width - relativePoint.x;
                    movingIcon.top = (int)event.getY() - height - relativePoint.y;
                    movingIcon.bottom = (int)event.getY() + height - relativePoint.y;
                    movingIcon.center.set((int)event.getX() - relativePoint.x, (int)event.getY() - relativePoint.y);
                    //copyAhead(movingIcon);
                }
                if (appState == AppState.rotateIcon) {
                    movingIcon.rotation += ((int)event.getX() - startedRotationAt)/2;
                    startedRotationAt = (int)event.getX();
                    //copyAhead(movingIcon);
                }
                break;
            case MotionEvent.ACTION_UP:
                scaling = false;
                if (appState == AppState.idle) {
                    if (currentStroke.intervals.size() > 2) {
                        ArrayList<Point> newSetOfPoints = new ArrayList<>();
                        for (int j = 0; j < currentStroke.intervals.size(); j++) {
                            int interval = currentStroke.intervals.get(j);
                            Point point = currentStroke.points.get(j);
                            if (j == 0 || j == currentStroke.intervals.size() - 1 || interval > 30) {
                                newSetOfPoints.add(point);
                            }
                            //Log.e(TAG, "onTouchEvent: " + interval.toString());
                        }
                        currentStroke.points = newSetOfPoints;
                        copyAhead(currentStroke);
                    }
                }
                if (appState == AppState.rotateIcon) {
                    appState = AppState.moveIcon;
                }
                if (appState == AppState.moveIcon) {
                    copyAhead(movingIcon);
                }

                break;
        }
        lastTime = time;
        postInvalidate();
        return true;
    }

    private void copyAhead(Stroke currentStroke) {
        for (int i = animaActivity.frameNo + 1; i < animaActivity.frames.size(); i++) {
            animaActivity.frames.get(i).strokes.add(currentStroke);
        }
    }

    private void copyAhead(Icon movingIcon) {
        movingIcon.aCopy = false;
        for (int i = animaActivity.frameNo + 1; i < animaActivity.frames.size(); i++) {
            boolean applied = false;
            Frame nextFrame = animaActivity.frames.get(i);
            //for (Icon icon : nextFrame.icons)
            for (int j = 0; j < nextFrame.icons.size(); j++) {
                Icon icon = nextFrame.icons.get(j);
                if (icon.id.equals(movingIcon.id)) {
                    Log.e(TAG, "copyAhead: found at " + i );
                    if (icon.aCopy) {
                        nextFrame.icons.set(j,new Icon(movingIcon));
                        Log.e(TAG, "copyAhead: changed the copy" );
                    }
                    applied = true;
                }
            }
            if (!applied) {
                Log.e(TAG, "copyAhead: could not found, creating at " + i );
                Icon newIcon = new Icon(movingIcon);
                nextFrame.icons.add(newIcon);
            }
        }

    }

    public boolean onTouchEventPositionIcon(@NonNull MotionEvent event) {
        _scaleDetector.onTouchEvent(event);
        if (_scaleDetector.isInProgress()) {
            scaling = true;
            postInvalidate();
            return true;
        }

        Calendar time = Calendar.getInstance();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //currentFrame.icons.get(currentFrame.icons.size() - 1).drawRect.set((int)event.getX() - 30, (int)event.getY() - 30,
                //        (int)event.getX() + 30, (int)event.getY() + 30);
                currentFrame.icons.get(currentFrame.icons.size() - 1).left = (int)event.getX() - 30;
                currentFrame.icons.get(currentFrame.icons.size() - 1).right = (int)event.getX() - 30;
                currentFrame.icons.get(currentFrame.icons.size() - 1).top = (int)event.getY() - 30;
                currentFrame.icons.get(currentFrame.icons.size() - 1).bottom = (int)event.getY() - 30;
                //currentFrame.icons.get(currentFrame.icons.size() - 1).center = new Point((int)event.getX(),(int)event.getY());

                currentFrame.icons.get(currentFrame.icons.size() - 1).center = new Point((int)event.getX(),(int)event.getY());
                //copyAhead(currentFrame.icons.get(currentFrame.icons.size() - 1));
                break;
            case MotionEvent.ACTION_MOVE:
                Point lastActionDownPoint = currentFrame.icons.get(currentFrame.icons.size() - 1).center;
                int width = Math.abs((int)event.getX() - lastActionDownPoint.x);
                int height = Math.abs((int)event.getY() - lastActionDownPoint.y);
                //currentFrame.icons.get(currentFrame.icons.size() - 1).drawRect.set(lastActionDownPoint.x - width, lastActionDownPoint.y - height,
                 //       lastActionDownPoint.x + width, lastActionDownPoint.y + height);
                currentFrame.icons.get(currentFrame.icons.size() - 1).left = lastActionDownPoint.x - width;
                currentFrame.icons.get(currentFrame.icons.size() - 1).right = lastActionDownPoint.x + width;
                currentFrame.icons.get(currentFrame.icons.size() - 1).top = lastActionDownPoint.y - height;
                currentFrame.icons.get(currentFrame.icons.size() - 1).bottom = lastActionDownPoint.y + height;
                //copyAhead(currentFrame.icons.get(currentFrame.icons.size() - 1));
                break;
            case MotionEvent.ACTION_UP:
                copyAhead(currentFrame.icons.get(currentFrame.icons.size() - 1));
                scaling = false;
                appState = AppState.idle;
                break;
        }
        lastTime = time;
        postInvalidate();
        return true;
    }


}