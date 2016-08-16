package com.gii.insreport;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.text.Editable;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class FillFormActivity extends AppCompatActivity {

    private static final String TAG = "FillFormActivity.java";

    private static final int SPEACH_INTENT = 12;
    private static final int DAMAGE_PLAN_INTENT = 15;
    private static final int REQUEST_IMAGE_CAPTURE = 17;
    private static final int FREE_DRAW_INTENT = 19;
    private static final int ANIMA_INTENT = 21;

    String fireBaseCatalog = "";
    String id_no = "";
    Form currentForm = null;

    Calendar calendar = Calendar.getInstance();
    final EditText[] datePicker = new EditText[100];
    public int currentDatePicker = -1;
    public EditText currentEditText = null;
    public Button currentButton = null;
    public Element currentElement = null;
    public LinearLayout currentLinearLayout = null;

    DatePickerDialog.OnDateSetListener dateThenTime = new
            DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear,
                                      int dayOfMonth) {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, monthOfYear);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    datePicker[currentDatePicker].setText(dateTimeText(calendar.getTime()));
                    currentDateElement.vDate.setTime(calendar.getTime().getTime());
                    new TimePickerDialog(thisActivity, timeTo, calendar
                                .get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),true).show();
                }

            };

    DatePickerDialog.OnDateSetListener dateOnly = new
            DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear,
                                      int dayOfMonth) {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, monthOfYear);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    datePicker[currentDatePicker].setText(dateOnlyText(calendar.getTime()));
                    currentDateElement.vDate.setTime(calendar.getTime().getTime());
                    saveToCloud();
                }

            };

    TimePickerDialog.OnTimeSetListener timeTo = new
            TimePickerDialog.OnTimeSetListener() {

                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    calendar.set(Calendar.MINUTE, minute);
                    currentDateElement.vDate.setTime(calendar.getTime().getTime());
                    saveToCloud();
                    datePicker[currentDatePicker].setText(dateTimeText(calendar.getTime()));
                }

            };

    public static String dateTimeText(Date date) {
        if (date == null)
            return "";
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        String year = "" + cal.get(Calendar.YEAR);
        int monthNo = cal.get(Calendar.MONTH);
        String day = "" + cal.get(Calendar.DAY_OF_MONTH);
        return (day + " " + monthName[monthNo] + " " + year + " " + timeText(date));
    }

    public static String dateTimeJson(Date date) {
        if (date == null)
            return "";
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        String year = "" + cal.get(Calendar.YEAR);
        int monthNo = cal.get(Calendar.MONTH);
        String day = "" + cal.get(Calendar.DAY_OF_MONTH);
        return (day + "." + (monthNo + 1) + "." + year + " " + timeText(date));
    }

    public static String dateJson(Date date) {
        if (date == null)
            return "";
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        String year = "" + cal.get(Calendar.YEAR);
        int monthNo = cal.get(Calendar.MONTH);
        String day = "" + cal.get(Calendar.DAY_OF_MONTH);
        return (day + "." + (monthNo + 1) + "." + year);
    }

    public static String dateOnlyText(Date date) {
        if (date == null)
            return "";
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        String year = "" + cal.get(Calendar.YEAR);
        int monthNo = cal.get(Calendar.MONTH);
        String day = "" + cal.get(Calendar.DAY_OF_MONTH);
        return (day + " " + monthName[monthNo] + " " + year);
    }

    static String[] monthName = {"января","февраля","марта","апреля","мая","июня","июля","августа","сентября","октября","ноября","декабря"};


    public static String timeText(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        String hours = "" + cal.get(Calendar.HOUR_OF_DAY);
        String minutes = "" + cal.get(Calendar.MINUTE);
        if (minutes.length() < 2)
            minutes = "0" + minutes;
        return (hours.concat(":").concat(minutes));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        for (int i = 0; i < datePicker.length; i++)
            datePicker[i] = new EditText(this);
        setContentView(R.layout.activity_fill_form);
        fireBaseCatalog = getIntent().getStringExtra(InsReport.EXTRA_FIREBASE_CATALOG);
        id_no = getIntent().getStringExtra(InsReport.EXTRA_ID_NO);
        currentForm = null;
        for (FormsCollection formsCollection : InsReport.mainMenuForms) {
            if (formsCollection.fireBaseCatalog.equals(fireBaseCatalog)) {
                for (Form form : formsCollection.forms) {
                    if (form.id.equals(id_no)) {
                        currentForm = form;
                    }
                }
            }
        }
        if (currentForm == null)
            finish();
        currentForm.updateDescription();
        setTitle(currentForm.description);
        buildTheForm(currentForm);
    }

    private void buildTheForm(Form currentForm) {
        LinearLayout fillFormLL = (LinearLayout)findViewById(R.id.fill_form_ll);
        fillFormLL.setOrientation(LinearLayout.VERTICAL);
        if (fillFormLL.getOrientation() == LinearLayout.VERTICAL) {
            //fillFormLL.setLayoutParams(new LinearLayoutCompat.LayoutParams(LinearLayoutCompat.LayoutParams.MATCH_PARENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT));
            //TODO: handle different fillFormLL orientation.
        }
        datePickerIndex = -1;
        addElementsToLL(fillFormLL,currentForm.elements);
    }

    final FillFormActivity thisActivity = this;
    int datePickerIndex = -1;
    Element currentDateElement = null;

    CameraAndPictures cameraAndPictures = new CameraAndPictures();

    public void addElementsToLL(LinearLayout LL, ArrayList<Element> elements) {
        boolean firstGroup = true;
        for (final Element element : elements) {
            switch (element.type) {
                case eGroup:
                    LinearLayout outerLL = new LinearLayout(this);
                    element.container = outerLL;
                    final LinearLayout innerLL = new LinearLayout(this);
                    final ProgressBar progressBar = new ProgressBar(this,null,
                            android.R.attr.progressBarStyleHorizontal);
                    progressBar.setProgress(30);
                    innerLL.setOrientation(LinearLayout.VERTICAL);
                    innerLL.setLayoutParams(new LinearLayoutCompat.LayoutParams(LinearLayoutCompat.LayoutParams.MATCH_PARENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT));
                    outerLL.setOrientation(LinearLayout.VERTICAL);
                    final float scale = getResources().getDisplayMetrics().density;
                    if (LL.getOrientation() == LinearLayout.HORIZONTAL)
                        outerLL.setLayoutParams(new LinearLayoutCompat.LayoutParams((int)(500 * scale), LinearLayoutCompat.LayoutParams.WRAP_CONTENT));
                    else
                        outerLL.setLayoutParams(new LinearLayoutCompat.LayoutParams(LinearLayoutCompat.LayoutParams.MATCH_PARENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT));
                    addElementsToLL(innerLL,element.elements);
                    GradientDrawable gd = new GradientDrawable();
                    gd.setColor(0xFFEEEEFF);
                    gd.setCornerRadius(15);
                    gd.setStroke(1, 0xFF000000);
                    outerLL.setBackground(gd);
                    Button expandingButton = new Button(this);
                    expandingButton.setText(element.description);
                    //expandingButton.setCompoundDrawablesWithIntrinsicBounds(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_expand_more_black_24dp, null),null,null,null);
                    expandingButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Button thisButton = (Button)v;
                            if (innerLL.getVisibility() == View.GONE) {
                                innerLL.setVisibility(View.VISIBLE);
                                thisButton.setCompoundDrawablesWithIntrinsicBounds(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_expand_less_black_24dp, null),null,null,null);
                                thisButton.setText(element.description);
                                thisButton.requestFocus();
                                progressBar.setVisibility(View.GONE);
                            } else {
                                innerLL.setVisibility(View.GONE);
                                thisButton.setCompoundDrawablesWithIntrinsicBounds(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_expand_more_black_24dp, null),null,null,null);
                                thisButton.setText(element.description + "\n" + element.collectData(new ArrayList<String>()));
                                progressBar.setVisibility(View.VISIBLE);
                                progressBar.setProgress((int)((float)element.filled() / element.outOf() * 100));
                            }
                        }
                    });
                    if (firstGroup)
                        innerLL.setVisibility(View.VISIBLE);
                    else
                        innerLL.setVisibility(View.GONE);
                    firstGroup = false;
                    if (innerLL.getVisibility() == View.VISIBLE) {
                        expandingButton.setCompoundDrawablesWithIntrinsicBounds(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_expand_less_black_24dp, null),null,null,null);
                        expandingButton.setText(element.description);
                        expandingButton.requestFocus();
                        progressBar.setVisibility(View.GONE);
                    } else {
                        expandingButton.setCompoundDrawablesWithIntrinsicBounds(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_expand_more_black_24dp, null),null,null,null);
                        expandingButton.setText(element.description + "\n" + element.collectData(new ArrayList<String>()));
                        progressBar.setVisibility(View.VISIBLE);
                        progressBar.setProgress((int)((float)element.filled() / element.outOf() * 100));
                    };
                    progressBar.setProgress((int)((float)element.filled() / element.outOf() * 100));
                    outerLL.addView(expandingButton);
                    outerLL.addView(progressBar);
                    outerLL.addView(innerLL);
                    outerLL.setFocusableInTouchMode(true);
                    LL.addView(outerLL);
                    break;
                case eDate:
                    LinearLayout horizontalLLdp1 = new LinearLayout(thisActivity);
                    horizontalLLdp1.setOrientation(LinearLayout.HORIZONTAL);
                    horizontalLLdp1.setLayoutParams(new LinearLayoutCompat.LayoutParams(LinearLayoutCompat.LayoutParams.MATCH_PARENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT,1));
                    TextView captionTVdp1 = new TextView(thisActivity);
                    captionTVdp1.setText(element.description);
                    captionTVdp1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));

                    datePickerIndex++;
                    final int dpI = datePickerIndex;
                    final Element dpElement = element;
                    element.container = datePicker[datePickerIndex];
                    datePicker[datePickerIndex].setText(dateOnlyText(element.vDate));
                    datePicker[datePickerIndex].setInputType(InputType.TYPE_NULL);
                    datePicker[datePickerIndex].setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
                    datePicker[datePickerIndex].setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            if (hasFocus) {
                                if (element.vDate == null)
                                    element.vDate = new Date();
                                calendar.setTime(element.vDate);
                                currentDatePicker = dpI;
                                currentDateElement = dpElement;
                                new DatePickerDialog(thisActivity, dateOnly, calendar
                                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                                        calendar.get(Calendar.DAY_OF_MONTH)).show();
                            }
                        }
                    });
                    datePicker[datePickerIndex].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (element.vDate == null)
                                element.vDate = new Date();
                            calendar.setTime(element.vDate);
                            currentDatePicker = dpI;
                            currentDateElement = dpElement;
                            new DatePickerDialog(thisActivity, dateOnly, calendar
                                    .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                                    calendar.get(Calendar.DAY_OF_MONTH)).show();
                        }
                    });
                    ViewGroup parent = (ViewGroup) datePicker[datePickerIndex].getParent();

                    if (parent != null)
                        parent.removeAllViews();

                    horizontalLLdp1.addView(captionTVdp1);
                    horizontalLLdp1.addView(datePicker[datePickerIndex]);
                    LL.addView(horizontalLLdp1);
                    break;
                case eDateTime:
                    LinearLayout horizontalLLdp = new LinearLayout(thisActivity);
                    horizontalLLdp.setOrientation(LinearLayout.HORIZONTAL);
                    horizontalLLdp.setLayoutParams(new LinearLayoutCompat.LayoutParams(LinearLayoutCompat.LayoutParams.MATCH_PARENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT,1));
                    TextView captionTVdp = new TextView(thisActivity);
                    captionTVdp.setText(element.description);
                    captionTVdp.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));

                    datePickerIndex++;
                    final int dpI1 = datePickerIndex;
                    final Element dpElement1 = element;
                    element.container = datePicker[datePickerIndex];
                    datePicker[datePickerIndex].setText(dateTimeText(element.vDate));
                    datePicker[datePickerIndex].setInputType(InputType.TYPE_NULL);
                    datePicker[datePickerIndex].setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
                    datePicker[datePickerIndex].setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            if (hasFocus) {
                                if (element.vDate == null)
                                    element.vDate = new Date();
                                calendar.setTime(element.vDate);
                                currentDatePicker = dpI1;
                                currentDateElement = dpElement1;
                                new DatePickerDialog(thisActivity, dateThenTime, calendar
                                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                                        calendar.get(Calendar.DAY_OF_MONTH)).show();
                            }
                        }
                    });
                    datePicker[datePickerIndex].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (element.vDate == null)
                                element.vDate = new Date();
                            calendar.setTime(element.vDate);
                            currentDatePicker = dpI1;
                            currentDateElement = dpElement1;
                            new DatePickerDialog(thisActivity, dateThenTime, calendar
                                    .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                                    calendar.get(Calendar.DAY_OF_MONTH)).show();
                        }
                    });
                    ViewGroup parent1 = (ViewGroup) datePicker[datePickerIndex].getParent();

                    if (parent1 != null)
                        parent1.removeAllViews();

                    horizontalLLdp.addView(captionTVdp);
                    horizontalLLdp.addView(datePicker[datePickerIndex]);
                    LL.addView(horizontalLLdp);
                    break;
                case eText:
                    final EditText editText = new EditText(thisActivity);
                    ImageView speachButton = new ImageView(thisActivity);
                    currentEditText = editText;
                    element.container = editText;
                    editText.setText(element.vText);
                    //change the element, but do not save:
                    editText.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) { }

                        @Override
                        public void afterTextChanged(Editable s) {
                            element.vText = s.toString();
                        }
                    });
                    editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            if (!hasFocus) {
                                element.vText = ((EditText)v).getText().toString();
                                saveToCloud();
                            }
                        }
                    });
                    LinearLayout horizontalLL = new LinearLayout(thisActivity);
                    horizontalLL.setOrientation(LinearLayout.HORIZONTAL);
                    horizontalLL.setLayoutParams(new LinearLayoutCompat.LayoutParams(LinearLayoutCompat.LayoutParams.MATCH_PARENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT,1));

                    //ADIK: 06 August 2016:
                    TextInputLayout fieldHint = new TextInputLayout(thisActivity);
                    fieldHint.setHint(element.description);
                    fieldHint.setLayoutParams(new LinearLayout.LayoutParams(0,
                        LinearLayout.LayoutParams.WRAP_CONTENT, 1));
                    fieldHint.addView(editText);
                    horizontalLL.addView(fieldHint);
                    LinearLayout.LayoutParams loParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    loParams.gravity = Gravity.CENTER_VERTICAL;
                    speachButton.setLayoutParams(loParams);
                    speachButton.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_mic_black_24dp, null));
                    speachButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            currentEditText = editText;
                            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "ru-RU");
                            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Пожалуйста, говорите...");
                            intent.putExtra("android.speech.extra.EXTRA_ADDITIONAL_LANGUAGES", new String[]{});
                            startActivityForResult(intent, SPEACH_INTENT);
                        }
                    });
                    //hide button if there is no speech recognition on the device:
                    Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                    if (intent.resolveActivity(getPackageManager()) == null)
                        speachButton.setVisibility(View.GONE);

                    horizontalLL.addView(speachButton);

                    LL.addView(horizontalLL);
                    break;
                case eBoolean:
                    final Switch booleanSwitch = new Switch(this);
                    element.container = booleanSwitch;
                    booleanSwitch.setText(element.description);
                    booleanSwitch.setChecked(element.vBoolean);
                    booleanSwitch.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            element.vBoolean = booleanSwitch.isChecked();
                            saveToCloud();
                        }
                    });
                    LL.addView(booleanSwitch);
                    break;
                case ePlan:
                    final Button planButton = new Button(this);
                    element.container = planButton;
                    planButton.setText(span2Strings(element.description,element.vPlan.damageDescription), Button.BufferType.SPANNABLE);
                    planButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            currentElement = element;
                            currentButton = planButton;
                            Intent intent = new Intent(thisActivity, VehicleDamageActivity.class);
                            InsReport.damagePlanData = element.vPlan;
                            startActivityForResult(intent, DAMAGE_PLAN_INTENT);
                        }
                    });
                    LL.addView(planButton);
                    break;
                case ePhoto:
                    final HorizontalScrollView scrollViewPhoto = new HorizontalScrollView(this);
                    element.container = scrollViewPhoto;
                    scrollViewPhoto.setHorizontalScrollBarEnabled(true);
                    scrollViewPhoto.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 200));
                    final LinearLayout linearLayoutPhoto = new LinearLayout(this);
                    linearLayoutPhoto.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT));
                    linearLayoutPhoto.setOrientation(LinearLayout.HORIZONTAL);
                    //here request the pictures from the cloud into linearLayoutPhoto:
                    for (String photoID : element.vPhotos)
                        cameraAndPictures.getPicFromFirebase(photoID,linearLayoutPhoto);
                    final Button takePhotoButton = new Button(this);
                    takePhotoButton.setText("Камера+");
                    takePhotoButton.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT));
                    takePhotoButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            currentElement = element;
                            currentLinearLayout = linearLayoutPhoto;
                            takePhoto();
                        }
                    });
                    linearLayoutPhoto.addView(takePhotoButton);
                    scrollViewPhoto.addView(linearLayoutPhoto);
                    LL.addView(scrollViewPhoto);
                    break;
                case eDraw:
                    final Button drawButton = new Button(this);
                    element.container = drawButton;
                    drawButton.setText(span2Strings(element.description,element.toString()), Button.BufferType.SPANNABLE);
                    drawButton.setCompoundDrawablesWithIntrinsicBounds(element.getBitmapDrawable(this),null,null,null);
                    drawButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            InsReport.currentElement = element;
                            currentElement = element;
                            currentButton = drawButton;
                            Intent intent = new Intent(thisActivity, FreeDrawActivity.class);
                            InsReport.damagePlanData = element.vPlan;
                            startActivityForResult(intent, FREE_DRAW_INTENT);
                        }
                    });
                    LL.addView(drawButton);
                    break;
                case eAnima:
                    final Button animaButton = new Button(this);
                    element.container = animaButton;
                    animaButton.setText(span2Strings(element.description,element.toString()), Button.BufferType.SPANNABLE);
                    //animaButton.setCompoundDrawablesWithIntrinsicBounds(element.getBitmapDrawable(this),null,null,null);
                    animaButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            InsReport.currentElement = element;
                            currentElement = element;
                            currentButton = animaButton;
                            Intent intent = new Intent(thisActivity, AnimaActivity.class);
                            //InsReport.damagePlanData = element.vPlan;
                            //TODO: bind the frames...
                            startActivityForResult(intent, ANIMA_INTENT);
                        }
                    });
                    LL.addView(animaButton);
                    break;
                case eSignature:
                    final Button drawButton1 = new Button(this);
                    element.container = drawButton1;
                    drawButton1.setText(span2Strings(element.description,element.toString()), Button.BufferType.SPANNABLE);
                    drawButton1.setCompoundDrawablesWithIntrinsicBounds(element.getBitmapDrawable(this),null,null,null);
                    drawButton1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            InsReport.currentElement = element;
                            currentElement = element;
                            currentButton = drawButton1;
                            Intent intent = new Intent(thisActivity, FreeDrawActivity.class);
                            InsReport.damagePlanData = element.vPlan;
                            startActivityForResult(intent, FREE_DRAW_INTENT);
                        }
                    });
                    LL.addView(drawButton1);
                    break;
                case eCombo:
                    LinearLayout horizontalLLCombo = new LinearLayout(thisActivity);
                    horizontalLLCombo.setOrientation(LinearLayout.HORIZONTAL);
                    horizontalLLCombo.setLayoutParams(new LinearLayoutCompat.LayoutParams(LinearLayoutCompat.LayoutParams.MATCH_PARENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT,1));

                    TextView captionTVCombo = new TextView(this);
                    captionTVCombo.setText(element.description);

                    captionTVCombo.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));

                    final Spinner comboSpinner = new Spinner(this);
                    element.container = comboSpinner;
                    comboSpinner.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));

                    if (!element.directory.equals("")) {
                        Query queryRef = InsReport.ref.child("dirs/" + element.directory);
                        queryRef.
                                addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot snapshot) {
                                        Log.e(TAG, "onDataChange: got new directory, processing...");
                                        int i  =0;
                                        final ArrayList<DirectoryItem> directoryItems = new ArrayList<DirectoryItem>();
                                        for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                                            try {
                                                final DirectoryItem newDirectoryItem = postSnapshot.getValue(DirectoryItem.class);
                                                newDirectoryItem.id = postSnapshot.getKey();
                                                if (newDirectoryItem.status)
                                                    directoryItems.add(newDirectoryItem);
                                            } catch (Exception e) {
                                                Log.e(TAG, "onDataChange: PROBLEMS CASTING DIRECTORY FROM DB!!! : " + element.directory + "/" + postSnapshot.getKey());
                                            }
                                        }
                                        if (directoryItems.size() > 0) {
                                            element.comboItems = new ArrayList<String>();
                                            for (DirectoryItem directoryItem : directoryItems) {
                                                element.comboItems.add(directoryItem.name);
                                            }
                                            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(thisActivity, android.R.layout.simple_spinner_dropdown_item, element.comboItems);
                                            comboSpinner.setAdapter(spinnerArrayAdapter);
                                            for (int j = 0; j < directoryItems.size(); j++)
                                                if (directoryItems.get(j).id.equals(element.vText))
                                                    comboSpinner.setSelection(j);
                                            comboSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                @Override
                                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                                    element.vText = directoryItems.get(comboSpinner.getSelectedItemPosition()).id;
                                                    element.vInteger = comboSpinner.getSelectedItemPosition();
                                                    saveToCloud();
                                                }
                                                @Override
                                                public void onNothingSelected(AdapterView<?> parent) {

                                                }
                                            });
                                        }
                                    }
                                    @Override
                                    public void onCancelled(FirebaseError firebaseError) {
                                        Log.e("Firebase", "The read failed 9: " + firebaseError.getMessage());
                                    }
                                });
                    } else {
                        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, element.comboItems);
                        comboSpinner.setAdapter(spinnerArrayAdapter);
                        comboSpinner.setSelection(element.vInteger);
                        comboSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                element.vText = comboSpinner.getSelectedItem().toString();
                                element.vInteger = comboSpinner.getSelectedItemPosition();
                                saveToCloud();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                    }
                    horizontalLLCombo.addView(captionTVCombo);
                    horizontalLLCombo.addView(comboSpinner);
                    LL.addView(horizontalLLCombo);
                    break;
                case eRadio:
                    LinearLayout horizontalLLRadio = new LinearLayout(thisActivity);
                    horizontalLLRadio.setOrientation(LinearLayout.HORIZONTAL);
                    horizontalLLRadio.setLayoutParams(new LinearLayoutCompat.LayoutParams(LinearLayoutCompat.LayoutParams.MATCH_PARENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT,1));
                    TextView captionTVRadio = new TextView(this);
                    captionTVRadio.setText(element.description + ":");

                    captionTVRadio.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));

                    final RadioGroup radioGroup = new RadioGroup(this);
                    element.container = radioGroup;
                    radioGroup.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
                    //ArrayAdapter<String> radioArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, element.comboItems);
                    radioGroup.setOrientation(RadioGroup.HORIZONTAL);
                    final RadioButton[] radioButtons = new RadioButton[element.comboItems.size()];

                    if (!element.directory.equals("")) {
                        Query queryRef = InsReport.ref.child("dirs/" + element.directory);
                        queryRef.
                                addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot snapshot) {
                                        Log.e(TAG, "onDataChange: got new directory, processing...");
                                        int i  =0;
                                        for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                                            try {
                                                final DirectoryItem newDirectoryItem = postSnapshot.getValue(DirectoryItem.class);
                                                newDirectoryItem.id = postSnapshot.getKey();
                                                if (newDirectoryItem.status) {
                                                    RadioButton radioButton = new RadioButton(thisActivity);
                                                    radioButton.setText(newDirectoryItem.name);
                                                    i++;
                                                    radioButton.setId(i);
                                                    radioButton.setChecked(element.vText.equals(newDirectoryItem.id));
                                                    radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                                        @Override
                                                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                            if (isChecked) {
                                                                element.vText = newDirectoryItem.id;
                                                                saveToCloud();
                                                            }
                                                        }
                                                    });
                                                    if (radioGroup != null)
                                                        radioGroup.addView(radioButton);
                                                }
                                            } catch (Exception e) {
                                                Log.e(TAG, "onDataChange: PROBLEMS CASTING DIRECTORY FROM DB!!! : " + element.directory + "/" + postSnapshot.getKey());
                                            }
                                        }
                                    }
                                    @Override
                                    public void onCancelled(FirebaseError firebaseError) {
                                        Log.e("Firebase", "The read failed 9: " + firebaseError.getMessage());
                                    }
                                });
                    } else {
                        for (int i = 0; i < element.comboItems.size(); i++) {
                            if (!element.comboItems.get(i).equals("")) {
                                radioButtons[i] = new RadioButton(this);
                                radioButtons[i].setText(element.comboItems.get(i));
                                radioButtons[i].setChecked(i == element.vInteger);
                                final int _i = i;
                                radioButtons[i].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                    @Override
                                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                        if (isChecked) {
                                            element.vInteger = _i;
                                            element.vText = element.comboItems.get(_i);
                                            saveToCloud();
                                        }
                                    }
                                });
                                radioGroup.addView(radioButtons[i]);
                            }
                        }
                    }
                    LL.addView(captionTVRadio);
                    horizontalLLRadio.addView(radioGroup);
                    LL.addView(horizontalLLRadio);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == SPEACH_INTENT && resultCode == RESULT_OK)
        {
            ArrayList<String> matches = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            if (matches.size() > 0)
                currentEditText.setText(matches.get(0));
        }
        if (requestCode == DAMAGE_PLAN_INTENT)
        {
            //TODO: Warning: if you start the plan, rotate device and exit activity, currentButton does not exist any more, so, the app crashes!
            currentButton.setText(span2Strings(currentElement.description,currentElement.vPlan.damageDescription), Button.BufferType.SPANNABLE);
        }
        if (requestCode == FREE_DRAW_INTENT)
        {
            currentElement.freeDrawBitmap = null;
            currentButton.setText(span2Strings(currentElement.description,currentElement.toString()), Button.BufferType.SPANNABLE);
            currentButton.setCompoundDrawablesWithIntrinsicBounds(currentElement.getBitmapDrawable(this),null,null,null);
        }
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            final String id = CameraAndPictures.savePictureToFirebase();
            if (CameraAndPictures.bitmap != null) {
                InsReport.bitmapsNeedToBeRecycled.add(CameraAndPictures.bitmap);
                ImageView newImage = new ImageView(this);
                newImage.setImageBitmap(CameraAndPictures.bitmap);
                //newImage.setLayoutParams(new LinearLayout.LayoutParams(150, LinearLayout.LayoutParams.MATCH_PARENT));
                newImage.setAdjustViewBounds(true);
                newImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cameraAndPictures.showZoomed(currentLinearLayout, CameraAndPictures.bitmap);
                    }
                });
                currentLinearLayout.addView(newImage);
                currentElement.vPhotos.add(id);
            }
        }
        saveToCloud();
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void saveToCloud() {
        currentForm.updateDescription();
        currentForm.validate();
        currentForm.saveToCloud();
        setTitle(currentForm.description);
    }

    private void takePhoto() {
        CameraAndPictures.bitmap = null;
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File file = new File(Environment.getExternalStorageDirectory()+File.separator + "image.jpg");
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    public SpannableStringBuilder span2Strings(String string1, String string2) {
        SpannableStringBuilder spanSin = new SpannableStringBuilder();
        SpannableString itemasin = new SpannableString(string1 + "\n");
        itemasin.setSpan(new AbsoluteSizeSpan(9, true), 0,itemasin.length(),0);
        itemasin.setSpan(new ForegroundColorSpan(Color.parseColor("#EA7C07")), 0, itemasin.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spanSin.append(itemasin);

        SpannableString itemsin = new SpannableString(string2);
        itemsin.setSpan(new AbsoluteSizeSpan(12, true), 0,itemsin.length(),0);
        spanSin.append(itemsin);
        return spanSin;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        currentForm.validate();
        currentForm.saveToCloud();
        for (Bitmap bitmap : InsReport.bitmapsNeedToBeRecycled)
            bitmap.recycle();
        InsReport.bitmapsNeedToBeRecycled.clear();
        cleanBitmaps(currentForm.elements);
        Log.e(TAG, "onDestroy: recycling bitmaps...");
    }

    public void cleanBitmaps(ArrayList<Element> elements) {
        for (Element element : elements) {
            if (element.type == Element.ElementType.eGroup)
                cleanBitmaps(element.elements);
            else {
                if (element.freeDrawBitmap != null &&
                        !element.freeDrawBitmap.isRecycled())
                    element.freeDrawBitmap.recycle();
                if (element.myIcon != null &&
                        element.myIcon.getBitmap() != null &&
                        !element.myIcon.getBitmap().isRecycled())
                    element.myIcon.getBitmap().recycle();
            }
        }
    }

}
