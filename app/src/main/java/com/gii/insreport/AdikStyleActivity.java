package com.gii.insreport;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
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
import android.view.Window;
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

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AdikStyleActivity extends AppCompatActivity {

    private static final String TAG = "AdikStyleActivity.java";

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

    //LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);


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
                    //saveToCloud();
                }

            };

    TimePickerDialog.OnTimeSetListener timeTo = new
            TimePickerDialog.OnTimeSetListener() {

                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    calendar.set(Calendar.MINUTE, minute);
                    currentDateElement.vDate.setTime(calendar.getTime().getTime());
                    //saveToCloud();
                    datePicker[currentDatePicker].setText(dateTimeText(calendar.getTime()));
                }

            };

    public static String dateTimeText(Date date) {
        if (date == null)
            return "";
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return (dateOnlyText(date) + " " + timeText(date));
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
        Calendar today = Calendar.getInstance();
        cal.setTime(date);
        String year = "" + cal.get(Calendar.YEAR);
        int monthNo = cal.get(Calendar.MONTH);
        String day = "" + cal.get(Calendar.DAY_OF_MONTH);
        //TODO: test
        if (today.compareTo(cal) == 0)
            return "Сегодня";
        today.add(Calendar.DAY_OF_MONTH,-1);
        if (today.compareTo(cal) == 0)
            return "Вчера";
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
        setContentView(R.layout.activity_adik_style);

        for (int i = 0; i < datePicker.length; i++)
            datePicker[i] = new EditText(this);
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
        else {
            currentForm.updateDescription();
            setTitle(currentForm.description);
            buildTheForm(currentForm);
        }

        ((Button)findViewById(R.id.AButtonGeneral)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTheFragment("general","Общие сведения");
            }
        });
        ((Button)findViewById(R.id.menu2)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTheFragment("participant","Дополнительная информация");
            }
        });
    }


    Map<String,LinearLayout> linearLayoutForFragment = new HashMap<>();
    private void showTheFragment(String menuName, String title) {
        ViewGroup parent = (ViewGroup)linearLayoutForFragment.get(menuName).getParent();

        if (parent != null)
            parent.removeAllViews();

        /*Dialog buttonDialog = new Dialog(this);
        buttonDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        buttonDialog.setContentView(linearLayoutForFragment[menuNo]);
        buttonDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        dd
        buttonDialog.show();
        */

        new AlertDialog.Builder(this).setTitle(title).setView(linearLayoutForFragment.get(menuName))
                .setPositiveButton("Сохранить", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //currentForm.saveToCloud();
                    }
                }).show();
    }


    private void buildTheForm(Form currentForm) {
        int n = 0;
        datePickerIndex = -1;
        //linearLayoutForFragment = new LinearLayout[n];
        linearLayoutForFragment.clear();
        for (Element element : currentForm.elements) {
            if (linearLayoutForFragment.get(element.category) == null) {
                LinearLayout newLL = new LinearLayout(this);
                newLL.setOrientation(LinearLayout.VERTICAL);
                linearLayoutForFragment.put(element.category, newLL);
            }
        }

        for (Map.Entry<String, LinearLayout> linearLayoutEntry : linearLayoutForFragment.entrySet()) {
            ArrayList<Element> elements = new ArrayList<>();
            for (Element element : currentForm.elements) {
                if (element.category.equals(linearLayoutEntry.getKey())) {
                    elements.add(element);
                }
            }
            addElementsToLL(linearLayoutEntry.getValue(),elements);
        }
    }

    final AdikStyleActivity thisActivity = this;
    int datePickerIndex = -1;
    Element currentDateElement = null;

    CameraAndPictures cameraAndPictures = new CameraAndPictures();

    public void addElementsToLL(LinearLayout LL, ArrayList<Element> elements) {
        boolean firstGroup = true;
        for (final Element element : elements) {
            if (element.serverStatic) {
                LinearLayout horizontalLLStatic = new LinearLayout(this);
                horizontalLLStatic.setOrientation(LinearLayout.HORIZONTAL);
                horizontalLLStatic.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                TextView fieldName = new TextView(this);
                fieldName.setMaxLines(5);
                fieldName.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,0.5f));
                fieldName.setText(element.description);
                horizontalLLStatic.addView(fieldName);

                TextView fieldValue = new TextView(this);
                fieldValue.setMaxLines(5);
                fieldValue.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,0.5f));
                fieldValue.setText(element.toString());
                horizontalLLStatic.addView(fieldValue);
                LL.addView(horizontalLLStatic);
            }
        }

        for (final Element element : elements) {
            if (!element.serverStatic)
                switch (element.type) {
                    case eGroup:
                        LinearLayout outerLL = new LinearLayout(this);
                        element.container = outerLL;
                        final LinearLayout innerLL = new LinearLayout(this);
                        final ProgressBar progressBar = new ProgressBar(this,null,
                                android.R.attr.progressBarStyleHorizontal);
                        progressBar.setProgress(30);
                        innerLL.setOrientation(LinearLayout.VERTICAL);
                        LinearLayout.LayoutParams innerLL_lp = (new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                        innerLL_lp.setMargins(10,10,10,10);
                        innerLL.setLayoutParams(innerLL_lp);
                        outerLL.setOrientation(LinearLayout.VERTICAL);
                        final float scale = getResources().getDisplayMetrics().density;
                        LinearLayout.LayoutParams lp = null;
                        if (LL.getOrientation() == LinearLayout.HORIZONTAL)
                            lp = new LinearLayout.LayoutParams((int)(500 * scale), LinearLayout.LayoutParams.WRAP_CONTENT);
                        else
                            lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        lp.rightMargin = 20;
                        lp.leftMargin = 20;
                        //lp.setMargins(50,50,50,50);

                        outerLL.setLayoutParams(lp);
                        addElementsToLL(innerLL,element.elements);
                        outerLL.setBackground(ResourcesCompat.getDrawable(getResources(),R.drawable.boxy, null));
                        Button expandingButton = new Button(this);
                        expandingButton.setText(element.description);
                        expandingButton.setBackground(ResourcesCompat.getDrawable(getResources(),R.drawable.boxy_button, null));
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
                        if (firstGroup || true) //true is for the tablets
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
                                    //saveToCloud();
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
                                //saveToCloud();
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
                        final HorizontalScrollView scrollViewPhoto1 = new HorizontalScrollView(this);
                        scrollViewPhoto1.setHorizontalScrollBarEnabled(true);
                        scrollViewPhoto1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 200));
                        final LinearLayout linearLayoutPhoto1 = new LinearLayout(this);
                        element.container = linearLayoutPhoto1;
                        linearLayoutPhoto1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT));
                        linearLayoutPhoto1.setOrientation(LinearLayout.HORIZONTAL);
                        //here request the pictures from the cloud into linearLayoutPhoto:
                        for (String photoID : element.vPhotos)
                            cameraAndPictures.getPicFromFirebase(photoID,linearLayoutPhoto1);

                        final Button animaButton = new Button(this);
                        //element.container = animaButton;
                        animaButton.setText(span2Strings(element.description,element.toString()), Button.BufferType.SPANNABLE);
                        animaButton.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT));
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
                        linearLayoutPhoto1.addView(animaButton);
                        scrollViewPhoto1.addView(linearLayoutPhoto1);
                        LL.addView(scrollViewPhoto1);
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

                        if (!element.directory.equals("") &&
                                InsReport.directories.map.get(element.directory) != null) {
                            final ArrayList<DirectoryItem> directoryItems = InsReport.directories.map.get(element.directory).items;
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
                                        //saveToCloud();
                                    }
                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            }
                        } else {
                            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, element.comboItems);
                            comboSpinner.setAdapter(spinnerArrayAdapter);
                            comboSpinner.setSelection(element.vInteger);
                            comboSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    element.vText = comboSpinner.getSelectedItem().toString();
                                    element.vInteger = comboSpinner.getSelectedItemPosition();
                                    //saveToCloud();
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
                            int i = 0;
                            for (final DirectoryItem item : InsReport.directories.map.get(element.directory).items) {
                                RadioButton radioButton = new RadioButton(thisActivity);
                                radioButton.setText(item.name);
                                i++;
                                radioButton.setId(i);
                                radioButton.setChecked(element.vText.equals(item.id));
                                radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                    @Override
                                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                        if (isChecked) {
                                            element.vText = item.id;
                                        }
                                    }
                                });
                                if (radioGroup != null)
                                    radioGroup.addView(radioButton);
                            }
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
                                                //saveToCloud();
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
                    case eLookUp:
                        LinearLayout horizontalLLCombo1 = new LinearLayout(thisActivity);
                        horizontalLLCombo1.setOrientation(LinearLayout.HORIZONTAL);
                        horizontalLLCombo1.setLayoutParams(new LinearLayoutCompat.LayoutParams(LinearLayoutCompat.LayoutParams.MATCH_PARENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT,1));

                        TextView captionTVCombo1 = new TextView(this);
                        captionTVCombo1.setText(element.description);

                        captionTVCombo1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));

                        final EditText lookupEditText = new EditText(this);
                        element.container = lookupEditText;
                        lookupEditText.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
                        if (!element.directory.equals("")) {
                            lookupEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                                @Override
                                public void onFocusChange(View view, boolean b) {
                                    if (b) {
                                        openLookUpDialog(lookupEditText,InsReport.directories.map.get(element.directory).items);
                                    }
                                }
                            });
                            lookupEditText.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    openLookUpDialog(lookupEditText,InsReport.directories.map.get(element.directory).items);
                                }
                            });
                        };

                        horizontalLLCombo1.addView(captionTVCombo1);
                        horizontalLLCombo1.addView(lookupEditText);
                        LL.addView(horizontalLLCombo1);
                        break;
                    default:
                        break;
                }
        }
    }

    private void openLookUpDialog(EditText lookupEditText, ArrayList<DirectoryItem> directoryItems) {
        Dialog lookUpDialog = new Dialog(this);
        lookUpDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        lookUpDialog.setContentView(getLayoutInflater().inflate(R.layout.lookup
                , null));
        lookUpDialog.show();
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
            if (currentButton != null)
                currentButton.setText(span2Strings(currentElement.description,currentElement.vPlan.damageDescription), Button.BufferType.SPANNABLE);
        }
        if (requestCode == FREE_DRAW_INTENT)
        {
            currentElement.freeDrawBitmap = null;
            //TODO: get the current element from the currentElement.container!

            if (currentButton != null) {
                currentButton.setText(span2Strings(currentElement.description, currentElement.toString()), Button.BufferType.SPANNABLE);
                currentButton.setCompoundDrawablesWithIntrinsicBounds(currentElement.getBitmapDrawable(this), null, null, null);
            }
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
        //TODO: if animaView, add the pictures (need to remove the recycle in the anima view)
        //TODO: remove the green line in the bottom of the screenshots!!!
        if (requestCode == ANIMA_INTENT) {
            for (final Bitmap bitmap : currentElement.bitmapsToBeAddedOnResult) {
                final ImageView theImageView = new ImageView(InsReport.currentElement.container.getContext());
                final LinearLayout linearLayout = (LinearLayout)InsReport.currentElement.container;
                theImageView.setImageBitmap(bitmap);
                theImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cameraAndPictures.showZoomed(linearLayout, bitmap);
                    }
                });
                theImageView.setAdjustViewBounds(true);
                linearLayout.addView(theImageView);
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
    public void onBackPressed() {
        saveToCloud();
        finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (currentForm != null) {
            currentForm.validate();
            currentForm.saveToCloud();
        }
        for (Bitmap bitmap : InsReport.bitmapsNeedToBeRecycled)
            bitmap.recycle();
        InsReport.bitmapsNeedToBeRecycled.clear();
        if (currentForm != null)
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