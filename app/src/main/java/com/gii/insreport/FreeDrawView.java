package com.gii.insreport;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by Timur_hnimdvi on 30-Jul-16.
 */
public class FreeDrawView extends View {

    FreeDrawActivity freeDrawActivity;
    Paint white = new Paint();
    Paint gray = new Paint();
    Paint green = new Paint();
    Paint blue = new Paint();
    Paint red = new Paint();

    Path currPath = new Path();

    Rect canvasRect = new Rect(0,0,1,1);

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.canvasRect.set(0,0,canvas.getWidth(),canvas.getHeight());
        for (ArrayList<PointF> point : InsReport.currentElement.vDraw) {
            if (point.size() > 0) {
                currPath.reset();
                currPath.moveTo((int)(canvas.getWidth() * point.get(0).x), (int)(canvas.getHeight() * point.get(0).y));
                for (PointF nextPoint : point)
                    currPath.lineTo((int)(canvas.getWidth() * nextPoint.x), (int)(canvas.getHeight() * nextPoint.y));
                canvas.drawPath(currPath,blue);
            }
        }
    }
    public FreeDrawView(Context context) {
        super(context);
        //_scaleDetector = new ScaleGestureDetector(this.getContext(), new ScaleListener());
    }

    public void bindActivity(FreeDrawActivity freeDrawActivity) {
        this.freeDrawActivity = freeDrawActivity;
    }

    public void loadResources(Context context) {
        //this.vehicleDamageActivity = vehicleDamageActivity;
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
        blue = new Paint();
        blue.setColor(Color.BLUE);
        blue.setAlpha(255);
        blue.setStrokeWidth(3);
        blue.setStyle(Paint.Style.STROKE);
        red = new Paint();
        red.setColor(Color.RED);
        red.setAlpha(100);
        red.setStrokeWidth(10);
        red.setStyle(Paint.Style.FILL_AND_STROKE);
        red.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
            return onTouchEventIdle(event);
    }

    ArrayList<PointF> newPath = new ArrayList<>();
    public boolean onTouchEventIdle(@NonNull MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                newPath = new ArrayList<>();
                InsReport.currentElement.vDraw.add(newPath);
                newPath.add(new PointF(event.getX() / canvasRect.right, event.getY() / canvasRect.bottom));
                break;
            case MotionEvent.ACTION_MOVE:
                newPath.add(new PointF(event.getX() / canvasRect.right, event.getY() / canvasRect.bottom));
                break;
            case MotionEvent.ACTION_UP:

                break;
        }
        postInvalidate();
        return true;
    }
}
