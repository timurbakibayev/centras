package com.gii.insreport;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Timur on 20-Jul-16.
 */
public class VehicleDamageView extends View {
    public static Firebase ref = InsReport.ref;
    private String TAG = "VehicleDamageView.java";
    //
    public static String carType = "";

    VehicleDamageActivity vehicleDamageActivity;
    Spinner spinner;
    Bitmap[] vehicleImage;
    Rect[] vehicleImageSourceRect;
    Rect[] vehicleImageDestRect;


    public enum DamagePlanState {
        idle, choosePlan
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }


    public DamagePlanState appState = DamagePlanState.idle;

    public int currentDamageNo = -1;

    public int vehicleNo = 0;

    Paint white = new Paint();
    Paint gray = new Paint();
    Paint green = new Paint();
    Paint red = new Paint();

    Point centerPoint = new Point(0, 0);

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawLine(0, 0, 100, 100, green);
        int step = canvas.getWidth() / 30;

        if (vehicleImageDestRect[vehicleNo].width() != canvas.getWidth()) {
            vehicleImageDestRect[vehicleNo].set(0, 0, canvas.getWidth(),
                    (int) ((float) canvas.getWidth() / vehicleImage[vehicleNo].getWidth() * vehicleImage[vehicleNo].getHeight()));
        }
        canvas.drawBitmap(vehicleImage[vehicleNo], vehicleImageSourceRect[vehicleNo], vehicleImageDestRect[vehicleNo], null);
        for (DamageMark damageMark : InsReport.damagePlanData.damageMarks) {
            centerPoint.set((int) (damageMark.relativeCoordinates.x * vehicleImageDestRect[vehicleNo].width() + vehicleImageDestRect[vehicleNo].left),
                    (int) (damageMark.relativeCoordinates.y * vehicleImageDestRect[vehicleNo].height() + vehicleImageDestRect[vehicleNo].top));
            damageMark.toDraw.set(centerPoint.x - step, centerPoint.y - step, centerPoint.x + step, centerPoint.y + step);
            canvas.drawLine(damageMark.toDraw.left, damageMark.toDraw.top, damageMark.toDraw.right, damageMark.toDraw.bottom, red);
            canvas.drawLine(damageMark.toDraw.right, damageMark.toDraw.top, damageMark.toDraw.left, damageMark.toDraw.bottom, red);
        }
    }

    public VehicleDamageView(Context context) {
        super(context);
        this.spinner = spinner;
        //_scaleDetector = new ScaleGestureDetector(this.getContext(), new ScaleListener());
    }


    public void bindActivity(VehicleDamageActivity vehicleDamageActivity) {
        this.vehicleDamageActivity = vehicleDamageActivity;


    }

    public void loadResources(Context context) {
        //this.vehicleDamageActivity = vehicleDamageActivity;
//        final LinearLayout layout = new LinearLayout(vehicleDamageActivity);
//        layout.setOrientation(LinearLayout.VERTICAL);

//        Spinner spinner = new Spinner(context);
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,
//                R.array.cars_array, android.R.layout.simple_spinner_item);
//
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinner.setAdapter(adapter);



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
        red = new Paint();
        red.setColor(Color.RED);
        red.setAlpha(100);
        red.setStrokeWidth(10);
        red.setStyle(Paint.Style.FILL_AND_STROKE);
        red.setStrokeCap(Paint.Cap.ROUND);
        vehicleImage = new Bitmap[3];
//
        vehicleImage[2] = BitmapFactory.decodeResource(vehicleDamageActivity.getResources(), R.drawable.limousine_plan_open);
        vehicleImage[1] = BitmapFactory.decodeResource(vehicleDamageActivity.getResources(), R.drawable.jeep_plan);
        switch (carType) {
            case "Bike":
                vehicleImage[0] = BitmapFactory.decodeResource(vehicleDamageActivity.getResources(), R.drawable.bike);
                break;
            case "Bus":
                vehicleImage[0] = BitmapFactory.decodeResource(vehicleDamageActivity.getResources(), R.drawable.bus);
                break;
            case "Truck":
                vehicleImage[0] = BitmapFactory.decodeResource(vehicleDamageActivity.getResources(), R.drawable.truck);
                break;
            default:
                vehicleImage[0] = BitmapFactory.decodeResource(vehicleDamageActivity.getResources(), R.drawable.limousine_plan_open);
        }
//        vehicleImage[0] = BitmapFactory.decodeResource(vehicleDamageActivity.getResources(),R.drawable.bike);

        vehicleImageDestRect = new Rect[vehicleImage.length];
        vehicleImageSourceRect = new Rect[vehicleImage.length];
        for (int i = 0; i < vehicleImage.length; i++) {
            vehicleImageSourceRect[i] = new Rect(0, 0, vehicleImage[i].getWidth(), vehicleImage[i].getHeight());
            vehicleImageDestRect[i] = new Rect(0, 0, vehicleImage[i].getWidth(), vehicleImage[i].getHeight());
        }
        loadCommonDamages();
    }


    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        Log.e(TAG, "onTouchEvent: ");
        if (appState == DamagePlanState.idle)
            return onTouchEventIdle(event);

        /*
        if (appState == AppState.positionIcon)
            return onTouchEventPositionIcon(event);

        if (appState == AppState.chooseIcon) {
            return mainActivity.iconsWindow.onTouchEvent(event);
        }*/

        return false;
    }

    public boolean onTouchEventIdle(@NonNull MotionEvent event) {
        Log.e(TAG, "onTouchEventIdle: ");
        /*
        _scaleDetector.onTouchEvent(event);
        if (_scaleDetector.isInProgress()) {
            scaling = true;
            postInvalidate();
            return true;
        }*/

        Calendar time = Calendar.getInstance();
        //lastTime = Calendar.getInstance();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                currentDamageNo = -1;
                int i = 0;
                for (DamageMark damageMark : InsReport.damagePlanData.damageMarks) {
                    if (damageMark.toDraw.contains((int) event.getX(), (int) event.getY())) {
                        currentDamageNo = i;
                    }
                    i++;
                }

                if (currentDamageNo == -1) {
                    DamageMark newDamageMark = new DamageMark();
                    newDamageMark.relativeCoordinates.set((event.getX() - vehicleImageDestRect[vehicleNo].left) / vehicleImageDestRect[vehicleNo].width(),
                            (event.getY() - vehicleImageDestRect[vehicleNo].top) / vehicleImageDestRect[vehicleNo].height());
                    InsReport.damagePlanData.damageMarks.add(newDamageMark);
                    Log.e(TAG, "onTouchEventIdle: new Mark: " + newDamageMark.relativeCoordinates.x + "," + newDamageMark.relativeCoordinates.y);
                    currentDamageNo = InsReport.damagePlanData.damageMarks.size() - 1;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (currentDamageNo != -1) {
                    InsReport.damagePlanData.damageMarks.get(currentDamageNo).relativeCoordinates.set((event.getX() - vehicleImageDestRect[vehicleNo].left) / vehicleImageDestRect[vehicleNo].width(),
                            (event.getY() - vehicleImageDestRect[vehicleNo].top) / vehicleImageDestRect[vehicleNo].height());
                }
                break;
            case MotionEvent.ACTION_UP:
                if (InsReport.damagePlanData.damageMarks.get(currentDamageNo).id.equals(""))
                    InsReport.damagePlanData.damageMarks.get(currentDamageNo).generateNewId();
                edit(currentDamageNo, InsReport.damagePlanData.damageMarks.get(currentDamageNo).relativeCoordinates);
                currentDamageNo = -1;
                break;
        }

        postInvalidate();
        return true;

    }

    ArrayList<DamageMark> commonDamageMarks = new ArrayList<>();

    private void loadCommonDamages() {
        ref.child("youfix/commonDamages/" + vehicleNo).
                addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        commonDamageMarks.clear();
                        for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                            DamageMark newDM = postSnapshot.getValue(DamageMark.class);
                            boolean exists = false;
                            for (DamageMark damageMark : commonDamageMarks) {
                                if (damageMark.id.equals(newDM.id))
                                    exists = true;
                            }
                            if (!exists)
                                commonDamageMarks.add(newDM);
                        }
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {
                        Log.e("Firebase", "The read (commonDamageMarks) failed open: " + firebaseError.getMessage());
                    }
                });
    }

    private void edit(final int currentDamageNo, PointF currentRelativePosition) {
        final int finalDamageNo = currentDamageNo;

        if (!InsReport.damagePlanData.damageMarks.get(finalDamageNo).description.equals("")) {
            askForDescription(currentDamageNo);
            return;
        }


        int n = commonDamageMarks.size() + 1;
        String[] commonArray = new String[n];
        PointF[] commonArrayPoints = new PointF[n];
        for (int i = 0; i < n - 1; i++) {
            commonArray[i] = commonDamageMarks.get(i).description;
            commonArrayPoints[i] = new PointF(commonDamageMarks.get(i).relativeCoordinates.x, commonDamageMarks.get(i).relativeCoordinates.y);
        }

        commonArray[n - 1] = "Другое";

        final String[] toShowArray = new String[Math.min(n, 5)];

        for (int i = 0; i < n - 2; i++)
            for (int j = i + 1; j < n - 1; j++) {
                double d1 = Math.hypot(currentRelativePosition.x - commonArrayPoints[i].x,
                        currentRelativePosition.y - commonArrayPoints[i].y);
                double d2 = Math.hypot(currentRelativePosition.x - commonArrayPoints[j].x,
                        currentRelativePosition.y - commonArrayPoints[j].y);
                if (d1 > d2) {
                    //swapping:
                    PointF tempPoint = commonArrayPoints[i];
                    String tempDesc = commonArray[i];
                    commonArrayPoints[i] = commonArrayPoints[j];
                    commonArray[i] = commonArray[j];
                    commonArrayPoints[j] = tempPoint;
                    commonArray[j] = tempDesc;
                }
            }

        for (int i = 0; i < n; i++) {
            if (i < 4)
                toShowArray[i] = commonArray[i];
        }
        if (n >= 5)
            toShowArray[4] = "Другое";

        new AlertDialog.Builder(vehicleDamageActivity)
                //.setTitle(GIIApplication.gii.activity.getString(R.string.hint_description))
                //.setView(pin)
                .setSingleChoiceItems(toShowArray, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case Dialog.BUTTON_NEGATIVE: // Cancel button selected, do nothing
                                //damageMarks.remove(finalDamageNo);
                                dialog.cancel();
                                break;

                            case Dialog.BUTTON_POSITIVE: // OK button selected, send the data back
                                //damageMarks.remove(finalDamageNo);
                                dialog.dismiss();
                                // message selected value to registered callbacks with the
                                // selected value.
                                //calcCurrency = GIIApplication.gii.properties.currency.replace(" ",",").split(",")[mSelectedIndex];
                                //mDialogSelectorCallback.onSelectedOption(mSelectedIndex);
                                break;

                            default: // choice item selected
                                // store the new selected value in the static variable
                                InsReport.damagePlanData.damageMarks.get(finalDamageNo).description = toShowArray[which];
                                dialog.dismiss();
                                //mSelectedIndex = which;
                                if (which == toShowArray.length - 1) {
                                    askForDescription(finalDamageNo);
                                }
                                break;
                        }
                    }
                })
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        //return;
                        if (InsReport.damagePlanData.damageMarks.get(finalDamageNo).description.equals(""))
                            InsReport.damagePlanData.damageMarks.remove(finalDamageNo);
                        postInvalidate();
                    }
                }).
                show();

    }

    private void askForDescription(final int finalDamageNo) {
        final EditText newDescription = new EditText(vehicleDamageActivity);

        newDescription.setHint("Какая часть повреждена?");
        newDescription.setSingleLine(true);
        if (!InsReport.damagePlanData.damageMarks.get(finalDamageNo).description.equals("Другое"))
            newDescription.setText(InsReport.damagePlanData.damageMarks.get(finalDamageNo).description);

        final LinearLayout layout = new LinearLayout(vehicleDamageActivity);
        layout.setOrientation(LinearLayout.VERTICAL);

        layout.addView(newDescription);

        new android.app.AlertDialog.Builder(vehicleDamageActivity)
                //.setTitle("Сохранить как...")
                //.setMessage("Введите имя файла для сохранения")
                .setView(layout)
                .setPositiveButton("Сохранить", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String url = newDescription.getText().toString();
                        if (!url.equals("")) {
                            InsReport.damagePlanData.damageMarks.get(finalDamageNo).description = url;
                            boolean exists = false;
                            for (DamageMark damageMark : commonDamageMarks)
                                if (damageMark.description.equals(url))
                                    exists = true;
                            //ref.child("youfix/anima/" + prefs.getString("AndroidID", "") + "/" + url).
                            if (!exists)
                                ref.child("youfix/commonDamages/" + vehicleNo + "/" + InsReport.damagePlanData.damageMarks.get(finalDamageNo).id).
                                        setValue(InsReport.damagePlanData.damageMarks.get(finalDamageNo));
                        }
                    }
                }).setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        }).show();

    }

    //TODO: bad view in landscape orientation :(

}
