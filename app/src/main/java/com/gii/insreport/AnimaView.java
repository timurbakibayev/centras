package com.gii.insreport;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.media.MediaActionSound;
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

    public void takePicture() {
        final Bitmap screenShot = Bitmap.createBitmap(canvasWidth, canvasHeight, Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(screenShot);
        appState = AppState.idle;
        draw(canvas);

        String id = CameraAndPictures.generateNewId();
        InsReport.ref.child("images/" + id).setValue(CameraAndPictures.encodeToBase64(screenShot,Bitmap.CompressFormat.JPEG,70));
        InsReport.currentElement.vPhotos.add(id);
        //screenShot.recycle();
        InsReport.bitmapsNeedToBeRecycled.add(screenShot);
        InsReport.currentElement.bitmapsToBeAddedOnResult.add(screenShot);

        MediaActionSound sound = new MediaActionSound();
        sound.play(MediaActionSound.SHUTTER_CLICK);
    }

    public enum AppState {
        idle, chooseIcon, positionIcon, moveIcon, rotateIcon
    }

    public AppState appState = AppState.idle;

    static public Drawable[] drawableIcon;
    static public Integer[] drawableIconCategory;
    static public PointF[] drawableSize;
    static public Integer[] iconLayer;

    public int canvasWidth = 1;
    public int canvasHeight = 1;

    private String TAG = "AnimaView";

    public Stroke currentStroke = new Stroke();
    public Frame currentFrame = null;
    public ArrayList<Frame> frames = new ArrayList<>();

    ScaleGestureDetector _scaleDetector;
    AnimaActivity animaActivity;
    Paint road = new Paint();
    Paint roadMark = new Paint();
    Paint white = new Paint();
    Paint gray = new Paint();
    Paint backgroundPaint = new Paint();
    Paint green = new Paint();
    Path path = new Path();
    RectF drawRectF = new RectF(0,0,1,1);
    Rect drawRect = new Rect(0,0,1,1);
    PointF center = new PointF(0,0);

    Frame intermediateFrameProperties = new Frame();

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvasWidth = canvas.getWidth();
        canvasHeight = canvas.getHeight();


        canvas.drawRect(0,0,canvasWidth,canvasHeight,backgroundPaint);
        //canvas.drawLine(40,40,200,200,road);
        //canvas.drawLine(40,40,200,200,roadMark);

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
                for (int layer = 0; layer <= 10; layer ++)
                    for (Icon icon : currentFrame.icons) {
                        if (iconLayer[icon.picId] == layer){
                            intermediateFrameProperties.scale = currentFrame.scale;
                            intermediateFrameProperties.backgroundCenter.set(currentFrame.backgroundCenter.x,
                                    currentFrame.backgroundCenter.y);

                            int rotation = icon.rotation;
                            //center.set(icon.center.x,icon.center.y);
                            drawRectF.set(icon.left, icon.top, icon.right, icon.bottom);
                            if (animaActivity.play) {
                                for (Icon _icon : animaActivity.frames.get(playToFrame).icons) {
                                    if (_icon.id.equals(icon.id)) {
                                        //Log.e(TAG, "onDraw: Found same icon" );
                                        rotation = icon.rotation + (_icon.rotation - icon.rotation) / intermediateFrames * playToPercent;
                                        drawRectF.set(
                                                (icon.left + (_icon.left - icon.left) / intermediateFrames * playToPercent),
                                                icon.top + (_icon.top - icon.top) / intermediateFrames * playToPercent,
                                                icon.right + (_icon.right - icon.right) / intermediateFrames * playToPercent,
                                                icon.bottom + (_icon.bottom - icon.bottom) / intermediateFrames * playToPercent
                                        );
                                        //center.set(icon.center.x + (_icon.center.x - icon.center.x) / intermediateFrames * playToPercent,
                                        //        icon.center.y + (_icon.center.y - icon.center.y) / intermediateFrames * playToPercent);
                                    }
                                }

                                intermediateFrameProperties.scale = currentFrame.scale + (animaActivity.frames.get(playToFrame).scale - currentFrame.scale) / intermediateFrames * playToPercent;
                                intermediateFrameProperties.backgroundCenter.x = currentFrame.backgroundCenter.x + (animaActivity.frames.get(playToFrame).backgroundCenter.x - currentFrame.backgroundCenter.x) / intermediateFrames * playToPercent;
                                intermediateFrameProperties.backgroundCenter.y = currentFrame.backgroundCenter.y + (animaActivity.frames.get(playToFrame).backgroundCenter.y - currentFrame.backgroundCenter.y) / intermediateFrames * playToPercent;
                            }

                            position(drawRectF, drawRect, intermediateFrameProperties);
                            if (rotation != 0) {
                                canvas.save();
                                canvas.rotate(rotation, drawRect.centerX(), drawRect.centerY());
                            }
                            drawableIcon[icon.picId].setBounds(drawRect);
                            drawableIcon[icon.picId].draw(canvas);
                            if (rotation != 0)
                                canvas.restore();
                        }
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

    private void position(RectF drawRectF, Rect drawRect, Frame frame) {
        drawRect.set((int)(drawRectF.left * frame.scale + frame.backgroundCenter.x),
                (int)(drawRectF.top * frame.scale + frame.backgroundCenter.y),
                (int)(drawRectF.right * frame.scale + frame.backgroundCenter.x),
                (int)(drawRectF.bottom * frame.scale + frame.backgroundCenter.y));
    }

    public AnimaView(Context context) {
        super(context);
        _scaleDetector = new ScaleGestureDetector(this.getContext(), new ScaleListener());
    }

    public void loadResources(Context context) {
        //TODO: try to avoid this performance bottleneck
        setLayerType(View.LAYER_TYPE_HARDWARE, null);

        road = new Paint();
        road.setColor(Color.GRAY);
        road.setStrokeWidth(60);
        road.setStyle(Paint.Style.FILL);

        roadMark = new Paint();
        roadMark.setColor(Color.WHITE);
        roadMark.setStrokeWidth(4);
        roadMark.setStyle(Paint.Style.STROKE);
        roadMark.setPathEffect(new DashPathEffect(new float[] {5,10,15,20}, 0));

        white = new Paint();
        white.setColor(Color.WHITE);
        white.setStrokeWidth(2);
        white.setStyle(Paint.Style.STROKE);
        gray = new Paint();
        gray.setColor(Color.DKGRAY);
        gray.setStrokeWidth(10);
        gray.setStyle(Paint.Style.STROKE);
        backgroundPaint = new Paint();
        backgroundPaint.setColor(Color.LTGRAY);
        backgroundPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        green = new Paint();
        green.setColor(Color.GREEN);
        green.setAlpha(100);
        green.setStrokeWidth(2);
        green.setStyle(Paint.Style.FILL_AND_STROKE);
        drawableIcon = new Drawable[16];
        drawableIconCategory = new Integer[drawableIcon.length];
        drawableSize = new PointF[drawableIcon.length];
        iconLayer = new Integer[drawableIcon.length];
        int iconNo = 0;
        //drawableIconCategory[iconNo] = 0; drawableIcon[iconNo] = ContextCompat.getDrawable(context, R.drawable.__minivan); iconNo++;
        //drawableIconCategory[iconNo] = 0; drawableIcon[iconNo] = ContextCompat.getDrawable(context, R.drawable.__police); iconNo++;
        //drawableIconCategory[iconNo] = 0; drawableIcon[iconNo] = ContextCompat.getDrawable(context, R.drawable.__racecar); iconNo++;
        drawableSize[iconNo] = new PointF(1.7f,3);
        iconLayer[iconNo] = 1;
        drawableIconCategory[iconNo] = 1; drawableIcon[iconNo] = ContextCompat.getDrawable(context, R.drawable.__trafficlights); iconNo++;
        drawableSize[iconNo] = new PointF(1.7f,4);
        iconLayer[iconNo] = 10;
        drawableIconCategory[iconNo] = 0; drawableIcon[iconNo] = ContextCompat.getDrawable(context, R.drawable.__car_icon); iconNo++;
        drawableSize[iconNo] = new PointF(4,1.7f);
        iconLayer[iconNo] = 10;
        drawableIconCategory[iconNo] = 0; drawableIcon[iconNo] = ContextCompat.getDrawable(context, R.drawable.__top_car_figure_color); iconNo++;
        drawableSize[iconNo] = new PointF(4,1.7f);
        iconLayer[iconNo] = 10;
        drawableIconCategory[iconNo] = 0; drawableIcon[iconNo] = ContextCompat.getDrawable(context, R.drawable.limousine); iconNo++;
        drawableSize[iconNo] = new PointF(5,30);
        iconLayer[iconNo] = 0;
        drawableIconCategory[iconNo] = 2; drawableIcon[iconNo] = ContextCompat.getDrawable(context, R.drawable.qdoppellinie); iconNo++;
        drawableSize[iconNo] = new PointF(5,30);
        iconLayer[iconNo] = 0;
        drawableIconCategory[iconNo] = 2; drawableIcon[iconNo] = ContextCompat.getDrawable(context, R.drawable.qgeradeaus); iconNo++;
        drawableSize[iconNo] = new PointF(25,25);
        iconLayer[iconNo] = 0;
        drawableIconCategory[iconNo] = 2; drawableIcon[iconNo] = ContextCompat.getDrawable(context, R.drawable.qkreisverkehr); iconNo++;
        drawableSize[iconNo] = new PointF(25,25);
        iconLayer[iconNo] = 0;
        drawableIconCategory[iconNo] = 2; drawableIcon[iconNo] = ContextCompat.getDrawable(context, R.drawable.crossroad); iconNo++;
        drawableSize[iconNo] = new PointF(10,30);
        iconLayer[iconNo] = 0;
        drawableIconCategory[iconNo] = 2; drawableIcon[iconNo] = ContextCompat.getDrawable(context, R.drawable.qvierlinie); iconNo++;
        drawableSize[iconNo] = new PointF(2,2);
        iconLayer[iconNo] = 1;
        drawableIconCategory[iconNo] = 3; drawableIcon[iconNo] = ContextCompat.getDrawable(context, R.drawable.ahaltverbot); iconNo++;
        drawableSize[iconNo] = new PointF(2,2);
        iconLayer[iconNo] = 1;
        drawableIconCategory[iconNo] = 3; drawableIcon[iconNo] = ContextCompat.getDrawable(context, R.drawable.akreis_zeichen); iconNo++;
        drawableSize[iconNo] = new PointF(2,2);
        iconLayer[iconNo] = 1;
        drawableIconCategory[iconNo] = 3; drawableIcon[iconNo] = ContextCompat.getDrawable(context, R.drawable.astop); iconNo++;
        drawableSize[iconNo] = new PointF(2,2);
        iconLayer[iconNo] = 1;
        drawableIconCategory[iconNo] = 3; drawableIcon[iconNo] = ContextCompat.getDrawable(context, R.drawable.austupi); iconNo++;
        drawableSize[iconNo] = new PointF(1.7f,3);
        iconLayer[iconNo] = 1;
        drawableIconCategory[iconNo] = 1; drawableIcon[iconNo] = ContextCompat.getDrawable(context, R.drawable.sred); iconNo++;
        drawableSize[iconNo] = new PointF(1.7f,3);
        iconLayer[iconNo] = 1;
        drawableIconCategory[iconNo] = 1; drawableIcon[iconNo] = ContextCompat.getDrawable(context, R.drawable.sgreen); iconNo++;
        drawableSize[iconNo] = new PointF(1.7f,3);
        iconLayer[iconNo] = 1;
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
            /*
            if (appState == AppState.moveIcon || appState == AppState.rotateIcon) {
                float lastWidth = movingIcon.center.x - movingIcon.left;
                float lastHeight = movingIcon.center.y - movingIcon.top;
                movingIcon.left = movingIcon.center.x - Math.max((int)(lastWidth * detector.getScaleFactor()),1);
                movingIcon.right = movingIcon.center.x + Math.max((int)(lastWidth * detector.getScaleFactor()),1);
                movingIcon.top = movingIcon.center.y - Math.max((int)(lastHeight * detector.getScaleFactor()),1);
                movingIcon.bottom = movingIcon.center.y + Math.max((int)(lastHeight * detector.getScaleFactor()),1);
                copyAhead(movingIcon);
            }
            */
            currentFrame.scale *= detector.getScaleFactor();

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
    PointF relativePoint = new PointF(0,0);

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

                if (!(appState == AppState.rotateIcon)) {
                    int minLayer = InsReport.currentElement.vBoolean?2:0;
                    //if roads are locked, minLayer = 2;
                    for (int layer = 10; layer >= minLayer; layer--) {
                        i = 0;
                        for (Icon icon : currentFrame.icons) {
                            if (iconLayer[icon.picId] == layer) {
                                drawRectF.set(icon.left, icon.top, icon.right, icon.bottom);
                                position(drawRectF, drawRect, currentFrame);
                                if (drawRect.contains((int) event.getX(), (int) event.getY())) {
                                    movingIcon = icon;
                                    movingIconId = i;
                                    relativePoint.set((int) event.getX() - drawRect.centerX(),
                                            (int) event.getY() - drawRect.centerY());
                                    appState = AppState.moveIcon;
                                    Operation moveOperation;
                                    moveOperation = new Operation();
                                    moveOperation.operationType = "move";
                                    moveOperation.newIdInArray = movingIconId;
                                    moveOperation.oldIcon = new Icon(icon);
                                    currentFrame.operations.add(moveOperation);
                                    Log.e(TAG, "onTouchEventIdle: adding a new 'MOVE' operation!");
                                    layer = -1; //exiting the loop
                                }
                            }
                            i++;
                        }
                    }
                }
                if (appState == AppState.idle) {
                    relativePoint.set((int) event.getX(),
                            (int) event.getY());

                    Operation newOperation = new Operation();
                    newOperation.operationType = "background position";
                    newOperation.lastBackgroundCenter.set(currentFrame.backgroundCenter.x,currentFrame.backgroundCenter.y);
                    newOperation.lastScale = currentFrame.scale;
                    currentFrame.operations.add(newOperation);
                    Log.e(TAG, "onTouchEventIdle: adding a new 'BACKGROUND POSITION/SCALE' operation!" );

                    /*
                    currentStroke = new Stroke();
                    currentFrame.strokes.add(currentStroke);

                    currentStroke.points.add(new Point((int) event.getX(), (int) event.getY()));
                    currentStroke.intervals.add(0);
                    */
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (appState == AppState.idle) {
                    //currentStroke.points.add(new Point((int) event.getX(), (int) event.getY()));
                    //currentStroke.intervals.add((int) (time.getTimeInMillis() - lastTime.getTimeInMillis()));
                    currentFrame.backgroundCenter.set(
                            currentFrame.backgroundCenter.x + (int)(event.getX() - relativePoint.x),
                            currentFrame.backgroundCenter.y + (int)(event.getY() - relativePoint.y));
                    relativePoint.set((int)(event.getX()),(int)(event.getY()));
                };
                if (appState == AppState.moveIcon) {
                    float width = movingIcon.center.x - movingIcon.left;
                    float height = movingIcon.center.y - movingIcon.top;
                    PointF center = movingIcon.center;

                    //currentFrame.icons.get(movingIconId).drawRect.set(center.x - width, center.y - height, center.x + width, center.y + height);
                    drawRectF.set((int)event.getX() - width, (int)event.getY() - height,
                            (int)event.getX() + width, (int)event.getY() + height);
                    movingIcon.left = (event.getX() - relativePoint.x - currentFrame.backgroundCenter.x)/
                                        currentFrame.scale - width;
                    movingIcon.right = (event.getX() - relativePoint.x - currentFrame.backgroundCenter.x)/
                            currentFrame.scale + width;
                    movingIcon.top = (event.getY()  - relativePoint.y - currentFrame.backgroundCenter.y)/
                            currentFrame.scale - height;
                    movingIcon.bottom = (event.getY()  - relativePoint.y - currentFrame.backgroundCenter.y)/
                            currentFrame.scale + height;
                    movingIcon.center.set((movingIcon.left + movingIcon.right) / 2, (movingIcon.bottom + movingIcon.top) / 2);
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
                    /*
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
                    */
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

                currentFrame.icons.get(currentFrame.icons.size() - 1).center = new PointF((int)event.getX(),(int)event.getY());
                //copyAhead(currentFrame.icons.get(currentFrame.icons.size() - 1));
                break;
            case MotionEvent.ACTION_MOVE:
                PointF lastActionDownPoint = currentFrame.icons.get(currentFrame.icons.size() - 1).center;
                float width = Math.abs((int)event.getX() - lastActionDownPoint.x);
                float height = Math.abs((int)event.getY() - lastActionDownPoint.y);
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