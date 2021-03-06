package com.gii.insreport;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
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
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.firebase.client.ServerValue;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class IncidentFormActivity extends AppCompatActivity {

    private static final String TAG = "IncidentFormActivity";

    private static final int SPEACH_INTENT = 12;
    private static final int DAMAGE_PLAN_INTENT = 15;
    private static final int REQUEST_IMAGE_CAPTURE = 17;
    private static final int FREE_DRAW_INTENT = 19;
    private static final int ANIMA_INTENT = 21;
    private static final int REQUEST_ELEMENT_PHOTO = 29;

    private static final int TIME_DELIMITER = 1000;


    String fireBaseCatalog = "";
    String id_no = "";
    Form currentForm = null;

    Calendar calendar = Calendar.getInstance();
    final Button[] datePicker = new Button[100];
    public int currentDatePicker = -1;
    public EditText currentEditText = null;
    public Button currentButton = null;
    public Element currentElement = null;
    public LinearLayout currentLinearLayout = null;

    public boolean readOnly = false;

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
                    currentDateElement.vText = String.valueOf(calendar.getTime().getTime());
                    new TimePickerDialog(thisActivity, android.R.style.Theme_Holo_Light_Dialog, timeTo, calendar
                            .get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
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

    public static String dateOnlyTextStrict(Date date) {
        if (date == null)
            return "";
        Calendar cal = Calendar.getInstance();
        Calendar today = Calendar.getInstance();
        cal.setTime(date);
        String year = "" + cal.get(Calendar.YEAR);
        int monthNo = cal.get(Calendar.MONTH);
        String day = "" + cal.get(Calendar.DAY_OF_MONTH);
        return (day + " " + monthName[monthNo] + " " + year + ", " + cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE));
    }

    public static String dateToYYMMDD(Date date) {
        if (date == null)
            return "";
        Calendar cal = Calendar.getInstance();
        Calendar today = Calendar.getInstance();
        cal.setTime(date);
        String year = "" + cal.get(Calendar.YEAR);
        int monthNo = cal.get(Calendar.MONTH);
        String day = "" + cal.get(Calendar.DAY_OF_MONTH);
        return (year + "_" + (monthNo + 1) + "/" + day);
    }


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
        today.add(Calendar.DAY_OF_MONTH, -1);
        if (today.compareTo(cal) == 0)
            return "Вчера";
        return (day + " " + monthName[monthNo] + " " + year);
    }

    static String[] monthName = {"января", "февраля", "марта", "апреля", "мая", "июня", "июля", "августа", "сентября", "октября", "ноября", "декабря"};


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

        if (InsReport.incidentFormActivity != null)
            InsReport.incidentFormActivity.finish();
        InsReport.incidentFormActivity = this;

        for (int i = 0; i < datePicker.length; i++)
            datePicker[i] = new Button(this);
        fireBaseCatalog = getIntent().getStringExtra(InsReport.EXTRA_FIREBASE_CATALOG);

        if (fireBaseCatalog.equalsIgnoreCase("incident"))
            setContentView(R.layout.activity_incident_form);
        if (fireBaseCatalog.equalsIgnoreCase("preinsurance"))
            setContentView(R.layout.activity_pre_insurance);

        //
//
        ArrayList<Integer> images = new ArrayList<>();


        ArrayList<String> items = new ArrayList<String>();
        ArrayList<Boolean> availability = new ArrayList<>();

//        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.card_recycler_view);
//        recyclerView.setHasFixedSize(true);
//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
//        recyclerView.setLayoutManager(layoutManager);

        if (fireBaseCatalog.equalsIgnoreCase("incident")) {
            images.add(R.drawable.ic_assignment_black_24dp);
            images.add(R.drawable.ic_perm_media_black_24dp);
            images.add(R.drawable.ic_directions_car_black_24dp);
            images.add(R.drawable.ic_people_black_24dp);
            images.add(R.drawable.ic_map_black_72dp);
            images.add(R.drawable.ic_border_color_black_24dp);
            images.add(R.drawable.ic_add_black_24dp);
            items.add(getString(R.string.general_info));
            availability.add(true);
            items.add(getString(R.string.photos_and_documents));
            availability.add(true);
            items.add(getString(R.string.objects_info));
            availability.add(true);
            items.add(getString(R.string.participants_info));
            availability.add(true);
            items.add(getString(R.string.event_description));
            availability.add(true);
            items.add(getString(R.string.signatures));
            availability.add(true);
            items.add(getString(R.string.additional_info));
            availability.add(true);
            ReportType adapter = new ReportType(getApplicationContext(), items, images, availability);
            ListView list = (ListView) findViewById(R.id.reports);
            list.setAdapter(adapter);
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    switch (i) {
                        case 0:
                            showTheFragment("general", "Общие сведения");
                            break;
                        case 1:
                            showManyPhotos();
                            break;
                        case 2:
                            showManyObjects();
                            break;
                        case 3:
                            showManyParticipants();
                            break;
                        case 4:
                            showTheFragment("description", "Описание событий");
                            break;
                        case 5:
                            showTheFragment("signature", getString(R.string.signatures));
                            break;
                        case 6:
                            showTheFragment("additionalInfo", "Дополнительная информация");
                            break;
                    }
//                Toast.makeText(getApplicationContext(), "Position " + i, Toast.LENGTH_SHORT).show();
                }
            });
        }
        if (fireBaseCatalog.equalsIgnoreCase("preinsurance")) {
            images.add(R.drawable.ic_assignment_black_24dp);
            images.add(R.drawable.ic_perm_media_black_24dp);
            images.add(R.drawable.ic_directions_car_black_24dp);
            images.add(R.drawable.ic_people_black_24dp);
            images.add(R.drawable.ic_camera_alt_black_24dp);
            images.add(R.drawable.ic_border_color_black_24dp);

            items.add(getString(R.string.general));
            availability.add(true);
            items.add(getString(R.string.documents));
            availability.add(true);
            items.add(getString(R.string.object_of_insurance));
            availability.add(true);
            items.add(getString(R.string.insured));
            availability.add(true);
            items.add(getString(R.string.photo));
            availability.add(true);
            items.add(getString(R.string.signature));
            availability.add(true);

            ReportType adapter = new ReportType(getApplicationContext(), items, images, availability);
            ListView list = (ListView) findViewById(R.id.reports);
            list.setAdapter(adapter);
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    switch (i) {
                        case 0:
                            showTheFragment("general", "Заявление");
                            break;
                        case 1:
                            showManyDocuments();
                            break;
                        case 2:
                            showManyObjects();
                            break;
                        case 3:
                            showManyInsured();
                            break;
                        case 4:
                            showManyPhotos();
                            break;
                        case 5:
                            showManySignatures();
                            break;
                    }
//                Toast.makeText(getApplicationContext(), "Position " + i, Toast.LENGTH_SHORT).show();
                }
            });

        }
//        recyclerView.setAdapter(adapter);
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
//                items);
//        ListView reportTypes = (ListView) findViewById(R.id.report_type);
//        reportTypes.setAdapter(adapter);
        //
        //

        id_no = getIntent().getStringExtra(InsReport.EXTRA_ID_NO);

        readOnly = getIntent().getBooleanExtra(InsReport.EXTRA_FORM_READY, false);

        currentForm = null;

        if (InsReport.currentForm != null && InsReport.currentForm.id.equals(id_no))
            currentForm = InsReport.currentForm;
        else {
            for (FormsCollection formsCollection : InsReport.mainMenuForms) {
                if (formsCollection.fireBaseCatalog.equals(fireBaseCatalog)) {
                    for (Form form : formsCollection.forms) {
                        if (form.id.equals(id_no)) {
                            currentForm = form;
                            InsReport.currentForm = form;
                            InsReport.savePref("lastFormId", id_no);
                            InsReport.savePref("lastFormCatalog", fireBaseCatalog);
                        }
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


        boolean portrait = (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT);
        if (fireBaseCatalog.equalsIgnoreCase("incident")) {

            ((Button) findViewById(R.id.AButtonGeneral)).
                    setCompoundDrawablesWithIntrinsicBounds(portrait ? 0 : R.drawable.ic_assignment_black_24dp,
                            portrait ? R.drawable.ic_assignment_black_24dp : 0, 0, 0);

            ((Button) findViewById(R.id.menuExtraInfo)).
                    setCompoundDrawablesWithIntrinsicBounds(portrait ? 0 : R.drawable.ic_add_black_24dp,
                            portrait ? R.drawable.ic_add_black_24dp : 0, 0, 0);

            ((Button) findViewById(R.id.menuDescription)).
                    setCompoundDrawablesWithIntrinsicBounds(portrait ? 0 : R.drawable.ic_map_black_72dp,
                            portrait ? R.drawable.ic_map_black_72dp : 0, 0, 0);

            ((Button) findViewById(R.id.menuParticipants)).
                    setCompoundDrawablesWithIntrinsicBounds(portrait ? 0 : R.drawable.ic_people_black_24dp,
                            portrait ? R.drawable.ic_people_black_24dp : 0, 0, 0);

            ((Button) findViewById(R.id.menuObjects)).
                    setCompoundDrawablesWithIntrinsicBounds(portrait ? 0 : R.drawable.ic_directions_car_black_24dp,
                            portrait ? R.drawable.ic_directions_car_black_24dp : 0, 0, 0);

            ((Button) findViewById(R.id.menuPhotos)).
                    setCompoundDrawablesWithIntrinsicBounds(portrait ? 0 : R.drawable.ic_perm_media_black_24dp,
                            portrait ? R.drawable.ic_perm_media_black_24dp : 0, 0, 0);

            ((Button) findViewById(R.id.AButtonGeneral)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showTheFragment("general", "Общие сведения");
                }
            });
            ((Button) findViewById(R.id.menuExtraInfo)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showTheFragment("additionalInfo", "Дополнительная информация");
                }
            });
            ((Button) findViewById(R.id.menuObjects)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //showTheFragment("object","Информация по объектам");
                    showManyObjects();
                }
            });
            ((Button) findViewById(R.id.menuParticipants)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showManyParticipants();
                }
            });

            ((Button) findViewById(R.id.menuPhotos)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showManyPhotos();
                }
            });

            ((Button) findViewById(R.id.menuDescription)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showTheFragment("description", "Описание событий");
                }
            });
        }

        if (fireBaseCatalog.equalsIgnoreCase("preinsurance")) {


            ((Button) findViewById(R.id.BButtonGeneral)).
                    setCompoundDrawablesWithIntrinsicBounds(portrait ? 0 : R.drawable.ic_assignment_black_24dp,
                            portrait ? R.drawable.ic_assignment_black_24dp : 0, 0, 0);

            ((Button) findViewById(R.id.BMenuObjects)).
                    setCompoundDrawablesWithIntrinsicBounds(portrait ? 0 : R.drawable.ic_directions_car_black_24dp,
                            portrait ? R.drawable.ic_directions_car_black_24dp : 0, 0, 0);

            ((Button) findViewById(R.id.BInsured)).
                    setCompoundDrawablesWithIntrinsicBounds(portrait ? 0 : R.drawable.ic_people_black_24dp,
                            portrait ? R.drawable.ic_people_black_24dp : 0, 0, 0);

            ((Button) findViewById(R.id.BMenuDocuments)).
                    setCompoundDrawablesWithIntrinsicBounds(portrait ? 0 : R.drawable.ic_description_black_24dp,
                            portrait ? R.drawable.ic_description_black_24dp : 0, 0, 0);

            ((Button) findViewById(R.id.BMenuSignature)).
                    setCompoundDrawablesWithIntrinsicBounds(portrait ? 0 : R.drawable.ic_border_color_black_24dp,
                            portrait ? R.drawable.ic_border_color_black_24dp : 0, 0, 0);

            ((Button) findViewById(R.id.BButtonPhoto)).
                    setCompoundDrawablesWithIntrinsicBounds(portrait ? 0 : R.drawable.ic_perm_media_black_24dp,
                            portrait ? R.drawable.ic_perm_media_black_24dp : 0, 0, 0);


            ((Button) findViewById(R.id.BButtonGeneral)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showTheFragment("general", getString(R.string.general));
                }
            });
            ((Button) findViewById(R.id.BMenuDocuments)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showTheFragment("documents", getString(R.string.documents));
                    //TODO: Documents!!!
                }
            });
            ((Button) findViewById(R.id.BMenuObjects)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //showTheFragment("object","Информация по объектам");
                    showManyObjects();
                }
            });
            ((Button) findViewById(R.id.BInsured)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showTheFragment("insured", getString(R.string.insured));
//                    showManyObjects();
                }
            });

            ((Button) findViewById(R.id.BButtonPhoto)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showManyPhotos();
                }
            });

            ((Button) findViewById(R.id.BMenuSignature)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showManySignatures();
                }
            });

        }

        if (findViewById(R.id.call_adik) == null) {
            finish();
            return;
        }

        ((ImageButton) findViewById(R.id.call_adik)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (currentForm.input.get("CLAIMANT_PHONE_NO") != null)
                    currentForm.phoneNo = currentForm.input.get("CLAIMANT_PHONE_NO");

                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + SMS.call(currentForm.phoneNo)));
                if (intent.resolveActivity(getPackageManager()) != null) {
                    InsReport.logFirebase("Make a call from inside the form: " + currentForm.fireBaseCatalog + " form no. " + currentForm.id + ", TEL: " + SMS.call(currentForm.phoneNo));
                    startActivity(intent);
                }

            }
        });

        ((ImageButton) findViewById(R.id.call_center_adik)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String phoneNo = InsReport.defaultCallCenter;
                if (currentForm.input.get("CALLCENTER") != null)
                    phoneNo = currentForm.input.get("CALLCENTER");

                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + SMS.call(phoneNo)));
                if (intent.resolveActivity(getPackageManager()) != null) {
                    InsReport.logFirebase("CALL CENTER from inside the form: " + currentForm.fireBaseCatalog + " form no. " + currentForm.id + ", TEL: " + SMS.call(phoneNo));
                    startActivity(intent);
                }

            }
        });


        final String address;
        String headerText = "";

        if (currentForm != null) {
            if (currentForm.input.get("CLIENT_NAME") != null)
                headerText = currentForm.input.get("CLIENT_NAME");
            if (currentForm.input.get("EVENT_PLACE") != null) {
                address = currentForm.input.get("EVENT_PLACE");
            } else
                address = "";
        } else
            address = "";


        if (findViewById(R.id.textHeader) != null)
            ((TextView) findViewById(R.id.textHeader)).setText(headerText);

        ((ImageButton) findViewById(R.id.map_adik)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_VIEW);

                String locationStr = address.trim();

                Uri locationUri = Uri.parse("geo:0,0?").buildUpon()
                        .appendQueryParameter("q", locationStr)
                        .build();
                intent.setData(locationUri);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    InsReport.logFirebase("Open a map from inside the form: " + currentForm.fireBaseCatalog + " form no. " + currentForm.id + ", Location: " + locationStr);
                    startActivity(intent);
//                    Toast.makeText(thisActivity, locationStr, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(thisActivity,
                            "Нет карты", Toast.LENGTH_SHORT).show();
                }

            }
        });

        /*
        ((ImageButton)findViewById(R.id.arrive_button)).setColorFilter(currentForm.atTheAddress? Color.GREEN: Color.WHITE);
        findViewById(R.id.arrive_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentForm.atTheAddress = !currentForm.atTheAddress;
                Log.w(TAG, "Location: " + InsReport.locationTracker.hasPossiblyStaleLocation());
                if (InsReport.locationTracker.hasPossiblyStaleLocation()) {
                    Location loc = InsReport.locationTracker.getPossiblyStaleLocation();
                    String location = loc.getLatitude() + "," + loc.getLongitude();
                    if (currentForm.atTheAddress)
                        currentForm.coordinatesDateArrived = location;
                    else
                        currentForm.coordinatesDateLeft = location;
                    InsReport.logGPSFirebase(currentForm.phoneNo, location, currentForm.atTheAddress ? "Manual Arrived" : "Manual Left");
                    InsReport.logFirebase(currentForm.atTheAddress ? "Manual Arrived " + location : "Manual Left " + location);
                } else {
                    InsReport.logFirebase(currentForm.atTheAddress? "Manual Arrived (no location)":"Manual Left (no location)");
                }

                ((ImageButton)view).setColorFilter(currentForm.atTheAddress? Color.GREEN: Color.WHITE);
                currentForm.saveToCloud();
                if (currentForm.atTheAddress) {
                    InsReport.ref.child("forms/" + fireBaseCatalog + "/" + InsReport.forceUserID() + "/" + currentForm.id + "/dateArrived").
                            setValue(ServerValue.TIMESTAMP);
                    Toast.makeText(IncidentFormActivity.this, "Зафиксировано прибытие на место ДТП", Toast.LENGTH_LONG).show();
                } else {
                    InsReport.ref.child("forms/" + fireBaseCatalog + "/" + InsReport.forceUserID() + "/" + currentForm.id + "/dateLeft").
                            setValue(ServerValue.TIMESTAMP);
                    Toast.makeText(IncidentFormActivity.this, "Зафиксирован отъезд с места ДТП", Toast.LENGTH_LONG).show();
                }

            }
        });
        */
        findViewById(R.id.ready_adik).setVisibility(readOnly ? View.GONE : View.VISIBLE);
        ((ImageButton) findViewById(R.id.ready_adik)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LinearLayout workProgressStatus = (LinearLayout) findViewById(R.id.work_progress_parent);
                View workView = View.inflate(thisActivity, R.layout.work_progress_dialog, null);
                final Dialog deviceNameDialog = new Dialog(thisActivity);
                deviceNameDialog.setContentView(workView);
                Button buttonArrive = (Button) deviceNameDialog.findViewById(R.id.work_stars);
                Button buttonComplete = (Button) deviceNameDialog.findViewById(R.id.work_finish);
                Button buttonLeave = (Button) deviceNameDialog.findViewById(R.id.work_end);
                buttonArrive.setEnabled(!currentForm.atTheAddress && currentForm.coordinatesDateArrived.equals(""));
                buttonLeave.setEnabled(currentForm.atTheAddress);
                buttonComplete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        currentForm.switchDone(thisActivity, true, thisActivity);
                        deviceNameDialog.dismiss();
                    }
                });
                buttonArrive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        switchArrival();
                        deviceNameDialog.dismiss();
                    }
                });
                buttonLeave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        switchArrival();
                        deviceNameDialog.dismiss();
                    }
                });
                deviceNameDialog.findViewById(R.id.pdf_button).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Thread thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                PDFReport pdfReport = new PDFReport(thisActivity, currentForm);
                                pdfReport.generatePDF();
                            }
                        });
                        thread.start();
                        Toast.makeText(IncidentFormActivity.this, "Пожалуйста, подождите... ", Toast.LENGTH_SHORT).show();
                        deviceNameDialog.dismiss();
                    }
                });

                deviceNameDialog.setTitle("Статус осмотра");
                deviceNameDialog.show();

            }
        });
    }

    private void switchArrival() {
        currentForm.atTheAddress = !currentForm.atTheAddress;
        Log.w(TAG, "Location: " + InsReport.locationTracker.hasPossiblyStaleLocation());
        if (InsReport.locationTracker.hasPossiblyStaleLocation()) {
            Location loc = InsReport.locationTracker.getPossiblyStaleLocation();
            String location = loc.getLatitude() + "," + loc.getLongitude();
            if (currentForm.atTheAddress)
                currentForm.coordinatesDateArrived = location;
            else
                currentForm.coordinatesDateLeft = location;
            InsReport.logGPSFirebase(currentForm.phoneNo, location, currentForm.atTheAddress ? "Manual Arrived" : "Manual Left");
            InsReport.logFirebase(currentForm.atTheAddress ? "Manual Arrived " + location : "Manual Left " + location);
        } else {
            InsReport.logFirebase(currentForm.atTheAddress ? "Manual Arrived (no location)" : "Manual Left (no location)");
        }

        currentForm.saveToCloud();
        if (currentForm.atTheAddress) {
            InsReport.ref.child("forms/" + fireBaseCatalog + "/" + InsReport.forceUserID() + "/" + currentForm.id + "/dateArrived").
                    setValue(ServerValue.TIMESTAMP);
            Toast.makeText(IncidentFormActivity.this, "Начало осмотра", Toast.LENGTH_LONG).show();
        } else {
            InsReport.ref.child("forms/" + fireBaseCatalog + "/" + InsReport.forceUserID() + "/" + currentForm.id + "/dateLeft").
                    setValue(ServerValue.TIMESTAMP);
            Toast.makeText(IncidentFormActivity.this, "Конец осмотра", Toast.LENGTH_LONG).show();
        }
    }

    private void makeFormReady() {
        new AlertDialog.Builder(thisActivity).setTitle("Форма полностью готова?")
                .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        currentForm.switchDone(thisActivity, true, thisActivity);
                        //finish();
                    }
                }).
                setNeutralButton("Нет", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        currentForm.saveToCloud();
                    }
                }).
                show();
    }

    private void showManyInsured() {
        if (currentForm.insureds.elements.size() == 0 && !readOnly) {
            //Необходимо добавить как минимум клиента. Дальше объекты будут создаваться вручную.
            Element newObject = new Element("object", Element.ElementType.eInsured,
                    "insureds", "Застрахованные");
            FormTemplates.applyTemplateForInsureds(newObject);
            currentForm.insureds.elements.add(newObject);
            currentForm.saveToCloud();
        }

        final String[] insureds = new String[currentForm.insureds.elements.size() + 1];
        for (int i = 0; i < currentForm.insureds.elements.size(); i++) {
            Element element = currentForm.insureds.elements.get(i);
            int count = 0;
            for (Element element1 : element.elements) {
                if (element1.category.equals("insureds")) {
                    for (Element element2 : element1.elements) {
                        if (!element2.deleted)
                            count++;
                    }
                }
            }
            insureds[i] = element.constructInduredsInfo() + " (" + count + " застрахованные)";
        }
        insureds[insureds.length - 1] = "Добавить +";
        final IncidentFormActivity incidentFormActivity = this;
        new android.app.AlertDialog.Builder(this)
                .setTitle("Выберите застрахованного")
                .setSingleChoiceItems(insureds, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case Dialog.BUTTON_NEGATIVE: // Cancel button selected, do nothing
                                dialog.cancel();
                                break;

                            case Dialog.BUTTON_POSITIVE: // OK button selected, send the data back
                                dialog.dismiss();
                                break;

                            default:
                                dialog.dismiss();
                                if (which == insureds.length - 1) {
                                    if (readOnly)
                                        return;
                                    Element newInsureds = new Element("object", Element.ElementType.eParticipant,
                                            "object" + (which + 1), "Застрахованные " + (which + 1));
                                    FormTemplates.applyTemplateForInsureds(newInsureds);
                                    currentForm.insureds.elements.add(newInsureds);
                                }
                                InsReport.currentElement = currentForm.insureds.elements.get(which);
                                currentElement = currentForm.insureds.elements.get(which);
                                LinearLayout newLL = new LinearLayout(incidentFormActivity);
                                newLL.setOrientation(LinearLayout.VERTICAL);
                                addElementsToLL(newLL, InsReport.currentElement.elements);
                                linearLayoutForFragment.put("participant", newLL);
                                showTheFragment("participant", InsReport.currentElement.description);
                                break;
                        }
                    }
                })
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        //return;
                    }
                }).
                show();
    }

    private void showManyDocuments() {
        if (currentForm.documents.elements.size() == 0 && !readOnly) {
            //Необходимо добавить как минимум клиента. Дальше объекты будут создаваться вручную.
            Element newDocument = new Element("document", Element.ElementType.eParticipant,
                    "client", "Клиент");
            FormTemplates.applyTemplateForDocuments(newDocument);
            currentForm.documents.elements.add(newDocument);
            currentForm.saveToCloud();
        }

        final String[] documents = new String[currentForm.documents.elements.size() + 1];
        for (int i = 0; i < currentForm.documents.elements.size(); i++) {
            Element element = currentForm.documents.elements.get(i);
            int count = 0;
            for (Element element1 : element.elements) {
                if (element1.category.equals("documents")) {
                    for (Element element2 : element1.elements) {
                        if (!element2.deleted)
                            count++;
                    }
                }
            }
            documents[i] = element.constructDocumentInfo() + " (" + count + " документы)";
        }
        documents[documents.length - 1] = "Добавить +";
        final IncidentFormActivity incidentFormActivity = this;
        new android.app.AlertDialog.Builder(this)
                .setTitle("Выберите документ")
                .setSingleChoiceItems(documents, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case Dialog.BUTTON_NEGATIVE: // Cancel button selected, do nothing
                                dialog.cancel();
                                break;

                            case Dialog.BUTTON_POSITIVE: // OK button selected, send the data back
                                dialog.dismiss();
                                break;

                            default:
                                dialog.dismiss();
                                if (which == documents.length - 1) {
                                    if (readOnly)
                                        return;
                                    Element newDocument = new Element("documents", Element.ElementType.eParticipant,
                                            "documents" + (which + 1), "Документы " + (which + 1));
                                    FormTemplates.applyTemplateForDocuments(newDocument);
                                    currentForm.documents.elements.add(newDocument);
                                }
                                InsReport.currentElement = currentForm.documents.elements.get(which);
                                currentElement = currentForm.documents.elements.get(which);
                                LinearLayout newLL = new LinearLayout(incidentFormActivity);
                                newLL.setOrientation(LinearLayout.VERTICAL);
                                addElementsToLL(newLL, InsReport.currentElement.elements);
                                linearLayoutForFragment.put("participant", newLL);
                                showTheFragment("participant", InsReport.currentElement.description);
                                break;
                        }
                    }
                })
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        //return;
                    }
                }).
                show();
    }

    private void showManyPhotos() {
        final ArrayList<Element> photoElements = new ArrayList<>();
        final ArrayList<String> photoDescription = new ArrayList<>();
        Log.w(TAG, "showManyPhotos: step1");
        for (Element element : currentForm.elements) {
            if (element.category.equals("photo")) {
                photoElements.add(element);
                int k = 0;
                for (Element element1 : element.elements) {
                    if (!element1.deleted)
                        k++;
                }
                photoDescription.add(element.description + " (" + k + " фото)");
            }
        }
        Log.w(TAG, "showManyPhotos: step2");

        //final String[] objects = new String[currentForm.objects.elements.size()];
        for (int i = 0; i < currentForm.objects.elements.size(); i++) {
            Element element = currentForm.objects.elements.get(i);
            for (Element element1 : element.elements) {
                if (element1.category.equals("photo")) {
                    photoElements.add(element1);
                    int k = 0;
                    for (Element element2 : element1.elements) {
                        if (!element2.deleted)
                            k++;
                    }
                    photoDescription.add(element.description + " (" + k + " фото)" + (element1.allPhotosTaken() ? " ✔" : ""));
                }
            }
        }

        if (currentForm.objects.elements.size() == 0 && !readOnly) {
            //Необходимо добавить как минимум клиента. Дальше объекты будут создаваться вручную.
            Element newObject = new Element("object", Element.ElementType.eParticipant,
                    "client", "Клиент");
            FormTemplates.applyTemplateForObjects(newObject);
            for (Element element1 : newObject.elements) {
                if (element1.category.equals("photo")) {
                    Log.w(TAG, "adding a client");
                    photoElements.add(element1);
                    photoDescription.add(newObject.description);
                }
            }
            currentForm.objects.elements.add(newObject);
            currentForm.saveToCloud();
        }

        Log.w(TAG, "showManyPhotos: step3");

        final String[] photos = new String[photoElements.size() + 1];
        for (int i = 0; i < photoElements.size(); i++) {
            photos[i] = photoDescription.get(i);
        }

        Log.w(TAG, "showManyPhotos: step4");

        photos[photos.length - 1] = "Добавить участника +";

        Log.w(TAG, "showManyPhotos: step5, now going to show the dialog. photos: " + photos.length);
        //final IncidentFormActivity incidentFormActivity = this;
        new android.app.AlertDialog.Builder(this)
                .setTitle("Выберите тип фото")
                .setSingleChoiceItems(photos, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case Dialog.BUTTON_NEGATIVE: // Cancel button selected, do nothing
                                dialog.cancel();
                                break;

                            case Dialog.BUTTON_POSITIVE: // OK button selected, send the data back
                                dialog.dismiss();
                                break;

                            default:
                                dialog.dismiss();
                                if (which == photos.length - 1) {
                                    if (readOnly)
                                        return;
                                    else {
                                        int objectNumber = currentForm.numberOfObjects();
                                        Element newObject = new Element("object", Element.ElementType.eParticipant,
                                                "object" + (objectNumber), "Участник №" + (objectNumber));
                                        FormTemplates.applyTemplateForObjects(newObject);
                                        for (Element element1 : newObject.elements) {
                                            if (element1.category.equals("photo")) {
                                                Log.w(TAG, "showManyPhotos: added a new element of type 'photo'");
                                                photoElements.add(element1);
                                                photoDescription.add(newObject.description);
                                            }
                                        }
                                        currentForm.objects.elements.add(newObject);
                                        currentForm.saveToCloud();
                                    }
                                }

                                InsReport.currentElement = photoElements.get(which);
                                InsReport.currentForm = currentForm;
                                Intent intent = new Intent(thisActivity, PhotosActivity.class);
                                startActivity(intent);
                                break;
                        }
                    }
                })
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        //return;
                    }
                }).
                show();
    }

    private void showManyObjects() {

        if (currentForm.objects.elements.size() == 0 && !readOnly) {
            //Необходимо добавить как минимум клиента. Дальше объекты будут создаваться вручную.
            Element newObject = new Element("object", Element.ElementType.eParticipant,
                    "client", "Клиент");
            FormTemplates.applyTemplateForObjects(newObject);
            currentForm.objects.elements.add(newObject);
            currentForm.saveToCloud();
        }

        final String[] objects = new String[currentForm.objects.elements.size() + 1];
        for (int i = 0; i < currentForm.objects.elements.size(); i++) {
            Element element = currentForm.objects.elements.get(i);
            int count = 0;
            for (Element element1 : element.elements) {
                if (element1.category.equals("object")) {
                    for (Element element2 : element1.elements) {
                        if (!element2.deleted)
                            count++;
                    }
                }
            }
            objects[i] = element.constructObjectInfo() + " (" + count + " фото)";
        }
        objects[objects.length - 1] = "Добавить +";
        final IncidentFormActivity incidentFormActivity = this;
        new android.app.AlertDialog.Builder(this)
                .setTitle("Выберите участника")
                .setSingleChoiceItems(objects, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case Dialog.BUTTON_NEGATIVE: // Cancel button selected, do nothing
                                dialog.cancel();
                                break;

                            case Dialog.BUTTON_POSITIVE: // OK button selected, send the data back
                                dialog.dismiss();
                                break;

                            default:
                                dialog.dismiss();
                                if (which == objects.length - 1) {
                                    if (readOnly)
                                        return;
                                    Element newObject = new Element("object", Element.ElementType.eParticipant,
                                            "object" + (which + 1), "Участник " + (which + 1));
                                    FormTemplates.applyTemplateForObjects(newObject);
                                    currentForm.objects.elements.add(newObject);
                                }
                                InsReport.currentElement = currentForm.objects.elements.get(which);
                                currentElement = currentForm.objects.elements.get(which);
                                LinearLayout newLL = new LinearLayout(incidentFormActivity);
                                newLL.setOrientation(LinearLayout.VERTICAL);
                                addElementsToLL(newLL, InsReport.currentElement.elements);
                                linearLayoutForFragment.put("participant", newLL);
                                showTheFragment("participant", InsReport.currentElement.description);
                                break;
                        }
                    }
                })
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        //return;
                    }
                }).
                show();
    }

    private void showManySignatures() {
        final ArrayList<Element> signatureElements = new ArrayList<>();
        for (Element element : currentForm.elements) {
            if (element.category.equals("signature"))
                signatureElements.add(element);
        }
        final String[] signatures = new String[signatureElements.size()];
        for (int i = 0; i < signatureElements.size(); i++) {
            Element element = signatureElements.get(i);
            signatures[i] = element.description + "  " + element.toString();
        }
        final IncidentFormActivity incidentFormActivity = this;
        new android.app.AlertDialog.Builder(this)
                .setTitle("Выберите подпись")
                .setSingleChoiceItems(signatures, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case Dialog.BUTTON_NEGATIVE: // Cancel button selected, do nothing
                                dialog.cancel();
                                break;

                            case Dialog.BUTTON_POSITIVE: // OK button selected, send the data back
                                dialog.dismiss();
                                break;

                            default:
                                dialog.dismiss();
                                InsReport.currentElement = signatureElements.get(which);
                                InsReport.currentForm = currentForm;
                                Intent intent = new Intent(thisActivity, FreeDrawActivity.class);
                                startActivityForResult(intent, FREE_DRAW_INTENT);
                                break;
                        }
                    }
                })
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        //return;
                    }
                }).
                show();
    }

    private void showManyParticipants() {
        if (currentForm.participants.elements.size() == 0 && !readOnly) {
            //Необходимо добавить как минимум клиента. Дальше объекты будут создаваться вручную.
            Element newObject = new Element("participant", Element.ElementType.eParticipant,
                    "client", "Клиент");
            FormTemplates.applyTemplateForParticipants(newObject);
            currentForm.participants.elements.add(newObject);
            currentForm.saveToCloud();
        }

        final String[] participants = new String[currentForm.participants.elements.size() + 1];
        for (int i = 0; i < currentForm.participants.elements.size(); i++) {
            Element element = currentForm.participants.elements.get(i);
            participants[i] = element.constructParticipantInfo();
        }
        participants[participants.length - 1] = "Добавить +";
        final IncidentFormActivity incidentFormActivity = this;
        new android.app.AlertDialog.Builder(this)
                .setTitle("Выберите участника")
                .setSingleChoiceItems(participants, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case Dialog.BUTTON_NEGATIVE: // Cancel button selected, do nothing
                                dialog.cancel();
                                break;

                            case Dialog.BUTTON_POSITIVE: // OK button selected, send the data back
                                dialog.dismiss();
                                break;

                            default:
                                dialog.dismiss();
                                if (which == participants.length - 1) {
                                    if (readOnly)
                                        return;
                                    Element newParticipant = new Element("participant", Element.ElementType.eParticipant,
                                            "participant" + (which + 1), "Участник " + (which + 1));
                                    FormTemplates.applyTemplateForParticipants(newParticipant);
                                    currentForm.participants.elements.add(newParticipant);
                                }
                                InsReport.currentElement = currentForm.participants.elements.get(which);
                                currentElement = currentForm.participants.elements.get(which);
                                LinearLayout newLL = new LinearLayout(incidentFormActivity);
                                newLL.setOrientation(LinearLayout.VERTICAL);
                                addElementsToLL(newLL, InsReport.currentElement.elements);
                                linearLayoutForFragment.put("participant", newLL);
                                showTheFragment("participant", InsReport.currentElement.description);
                                break;
                        }
                    }
                })
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        //return;
                    }
                }).
                show();
    }


    Map<String, LinearLayout> linearLayoutForFragment = new HashMap<>();

    private void showTheFragment(String menuName, String title) {
        //if (linearLayoutForFragment.get(menuName) == null) {
        //    FormTemplates.applyTemplate(currentForm, currentForm.fireBaseCatalog);
        //    buildTheForm(currentForm);
        //}
        Log.e(TAG, "showTheFragment: " + menuName);
        if (linearLayoutForFragment.get(menuName) == null) { //if template did not help, exit
            Toast.makeText(this, "Неподходящая форма", Toast.LENGTH_LONG);
            Log.e(TAG, "showTheFragment: " + menuName + " is null :( Form is not ready? Quitting");
            return;
        }
        ViewGroup parent = (ViewGroup) linearLayoutForFragment.get(menuName).getParent();

        if (parent != null)
            parent.removeAllViews();

        ScrollView scrollView = new ScrollView(this);
        int padding = (int) getResources().getDimension(R.dimen.activity_vertical_margin);
        scrollView.setPadding(padding, 0, padding, 0);
        scrollView.addView(linearLayoutForFragment.get(menuName));

        Log.e(TAG, "showTheFragment: about to show the form...");

        if (!readOnly && !menuName.equals("participant"))
            new AlertDialog.Builder(this).setTitle(title).setView(scrollView)
                    .setPositiveButton("Сохранить", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            currentForm.saveToCloud();
                        }
                    }).setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    currentForm.saveToCloud();
                }
            }).
                    show();

        if (!readOnly && menuName.equals("participant"))
            new AlertDialog.Builder(this).setTitle(title).setView(scrollView)
                    .setNegativeButton("Удалить", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (currentElement.fireBaseFieldName.equals("client")) {
                                new AlertDialog.Builder(thisActivity).setTitle("Это наш клиент, его нельзя удалять!")
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                currentForm.saveToCloud();
                                            }
                                        })
                                        .setOnDismissListener(new DialogInterface.OnDismissListener() {
                                            @Override
                                            public void onDismiss(DialogInterface dialogInterface) {
                                                currentForm.saveToCloud();
                                            }
                                        }).
                                        show();

                                return;
                            }
                            for (final Element element : currentForm.participants.elements) {
                                if (element.fireBaseFieldName.equals(currentElement.fireBaseFieldName)) {
                                    new AlertDialog.Builder(thisActivity).setTitle("Удалить участника?")
                                            .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    currentForm.participants.elements.remove(element);
                                                    currentForm.saveToCloud();
                                                }
                                            }).
                                            setNeutralButton("Нет", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    currentForm.saveToCloud();
                                                }
                                            })
                                            .setOnDismissListener(new DialogInterface.OnDismissListener() {
                                                @Override
                                                public void onDismiss(DialogInterface dialogInterface) {
                                                    currentForm.saveToCloud();
                                                }
                                            }).
                                            show();
                                }
                            }
                            for (final Element element : currentForm.objects.elements) {
                                if (element.fireBaseFieldName.equals(currentElement.fireBaseFieldName)) {
                                    new AlertDialog.Builder(thisActivity).setTitle("Удалить участника?")
                                            .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    currentForm.objects.elements.remove(element);
                                                    currentForm.saveToCloud();
                                                }
                                            }).
                                            setNeutralButton("Нет", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    currentForm.saveToCloud();
                                                }
                                            })
                                            .setOnDismissListener(new DialogInterface.OnDismissListener() {
                                                @Override
                                                public void onDismiss(DialogInterface dialogInterface) {
                                                    currentForm.saveToCloud();
                                                }
                                            }).
                                            show();
                                }
                            }
                        }
                    })
                    .setPositiveButton("Сохранить", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            currentForm.saveToCloud();
                        }
                    }).setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    currentForm.saveToCloud();
                }
            }).
                    show();

        if (readOnly)
            new AlertDialog.Builder(this).setTitle(title).setView(scrollView)
                    .setPositiveButton("Выйти", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    }).
                    show();
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
            addElementsToLL(linearLayoutEntry.getValue(), elements);
        }
    }

    final IncidentFormActivity thisActivity = this;
    int datePickerIndex = -1;
    Element currentDateElement = null;

    CameraAndPictures cameraAndPictures = new CameraAndPictures();

    public void addElementsToLL(LinearLayout LL, ArrayList<Element> elements) {
        boolean firstGroup = true;
        for (final Element element : elements) {
            if (element.serverStatic || (readOnly && (
                    element.type == Element.ElementType.eBoolean ||
                            element.type == Element.ElementType.eCombo ||
                            element.type == Element.ElementType.eComboMulti ||
                            element.type == Element.ElementType.eComboFromFile ||
                            element.type == Element.ElementType.eInteger ||
                            element.type == Element.ElementType.eLookUp ||
                            element.type == Element.ElementType.eRadio ||
                            element.type == Element.ElementType.eText ||
                            element.type == Element.ElementType.eTextNum
            )
            )) {
                LinearLayout horizontalLLStatic = new LinearLayout(this);
                horizontalLLStatic.setOrientation(LinearLayout.HORIZONTAL);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                lp.setMargins(0, 0, 0, 20);
                horizontalLLStatic.setLayoutParams(lp);
                TextView fieldName = new TextView(this);
                fieldName.setMaxLines(5);
                fieldName.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT, 0.5f));
                fieldName.setText(element.description);
                horizontalLLStatic.addView(fieldName);

                TextView fieldValue = new TextView(this);
                fieldValue.setMaxLines(5);
                fieldValue.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT, 0.5f));
                fieldValue.setText(element.toStringDirectory());
                horizontalLLStatic.addView(fieldValue);
                LL.addView(horizontalLLStatic);
            }
        }

        for (final Element element : elements) {
            if (!element.serverStatic && !(readOnly && (
                    element.type == Element.ElementType.eBoolean ||
                            element.type == Element.ElementType.eCombo ||
                            element.type == Element.ElementType.eComboMulti ||
                            element.type == Element.ElementType.eComboFromFile ||
                            element.type == Element.ElementType.eInteger ||
                            element.type == Element.ElementType.eLookUp ||
                            element.type == Element.ElementType.eRadio ||
                            element.type == Element.ElementType.eText ||
                            element.type == Element.ElementType.eTextNum
            )
            ))
                switch (element.type) {
                    case eGroup:
                        LinearLayout outerLL = new LinearLayout(this);
                        element.container = outerLL;
                        final LinearLayout innerLL = new LinearLayout(this);
                        final ProgressBar progressBar = new ProgressBar(this, null,
                                android.R.attr.progressBarStyleHorizontal);
                        progressBar.setProgress(30);
                        innerLL.setOrientation(LinearLayout.VERTICAL);
                        LinearLayout.LayoutParams innerLL_lp = (new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                        innerLL_lp.setMargins(10, 10, 10, 10);
                        innerLL.setLayoutParams(innerLL_lp);
                        outerLL.setOrientation(LinearLayout.VERTICAL);
                        final float scale = getResources().getDisplayMetrics().density;
                        LinearLayout.LayoutParams lp = null;
                        if (LL.getOrientation() == LinearLayout.HORIZONTAL)
                            lp = new LinearLayout.LayoutParams((int) (500 * scale), LinearLayout.LayoutParams.WRAP_CONTENT);
                        else
                            lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        lp.rightMargin = 20;
                        lp.leftMargin = 20;
                        //lp.setMargins(50,50,50,50);

                        outerLL.setLayoutParams(lp);
                        addElementsToLL(innerLL, element.elements);
                        outerLL.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.boxy, null));
                        Button expandingButton = new Button(this);

                        expandingButton.setText(element.description);
                        expandingButton.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.boxy_button, null));
                        //expandingButton.setCompoundDrawablesWithIntrinsicBounds(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_expand_more_black_24dp, null),null,null,null);
                        expandingButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Button thisButton = (Button) v;
                                if (innerLL.getVisibility() == View.GONE) {
                                    innerLL.setVisibility(View.VISIBLE);
                                    thisButton.setCompoundDrawablesWithIntrinsicBounds(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_expand_less_black_24dp, null), null, null, null);
                                    thisButton.setText(element.description);
                                    thisButton.requestFocus();
                                    progressBar.setVisibility(View.GONE);
                                } else {
                                    innerLL.setVisibility(View.GONE);
                                    thisButton.setCompoundDrawablesWithIntrinsicBounds(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_expand_more_black_24dp, null), null, null, null);
                                    thisButton.setText(element.description + "\n" + element.collectData(new ArrayList<String>()));
                                    progressBar.setVisibility(View.VISIBLE);
                                    progressBar.setProgress((int) ((float) element.filled() / element.outOf() * 100));
                                }
                            }
                        });
                        if (firstGroup || true) //true is for the tablets
                            innerLL.setVisibility(View.VISIBLE);
                        else
                            innerLL.setVisibility(View.GONE);
                        firstGroup = false;
                        if (innerLL.getVisibility() == View.VISIBLE) {
                            expandingButton.setCompoundDrawablesWithIntrinsicBounds(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_expand_less_black_24dp, null), null, null, null);
                            expandingButton.setText(element.description);
                            expandingButton.requestFocus();
                            progressBar.setVisibility(View.GONE);
                        } else {
                            expandingButton.setCompoundDrawablesWithIntrinsicBounds(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_expand_more_black_24dp, null), null, null, null);
                            expandingButton.setText(element.description + "\n" + element.collectData(new ArrayList<String>()));
                            progressBar.setVisibility(View.VISIBLE);
                            progressBar.setProgress((int) ((float) element.filled() / element.outOf() * 100));
                        }
                        ;
                        progressBar.setProgress((int) ((float) element.filled() / element.outOf() * 100));
                        outerLL.addView(expandingButton);
                        outerLL.addView(progressBar);
                        outerLL.addView(innerLL);
                        outerLL.setFocusableInTouchMode(true);
                        LL.addView(outerLL);
                        break;
                    case eDate:
                        LinearLayout horizontalLLdp1 = new LinearLayout(thisActivity);
                        horizontalLLdp1.setOrientation(LinearLayout.VERTICAL);
                        horizontalLLdp1.setLayoutParams(new LinearLayoutCompat.LayoutParams(LinearLayoutCompat.LayoutParams.MATCH_PARENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT, 1));
                        TextView captionTVdp1 = new TextView(thisActivity);
                        captionTVdp1.setText(element.description);
                        captionTVdp1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));

                        datePickerIndex++;
                        final int dpI = datePickerIndex;
                        final Element dpElement = element;
                        element.container = datePicker[datePickerIndex];
                        datePicker[datePickerIndex].setText(dateOnlyText(element.vDate));
                        datePicker[datePickerIndex].setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.boxy_button_spinner, null));
                        datePicker[datePickerIndex].setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_expand_more_black_24dp, 0);

                        datePicker[datePickerIndex].setInputType(InputType.TYPE_NULL);
                        datePicker[datePickerIndex].setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
                        datePicker[datePickerIndex].setOnFocusChangeListener(new View.OnFocusChangeListener() {
                            @Override
                            public void onFocusChange(View v, boolean hasFocus) {
                                if (hasFocus) {
                                    if (element.vDate == null)
                                        element.vDate = new Date();
                                    calendar.setTime(element.vDate);
                                    currentDatePicker = dpI;
                                    currentDateElement = dpElement;
                                    if (!readOnly)
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
                                if (!readOnly)
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
                        horizontalLLdp.setOrientation(LinearLayout.VERTICAL);
                        horizontalLLdp.setLayoutParams(new LinearLayoutCompat.LayoutParams(LinearLayoutCompat.LayoutParams.MATCH_PARENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT, 1));
                        TextView captionTVdp = new TextView(thisActivity);
                        captionTVdp.setText(element.description);
                        captionTVdp.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));

                        datePickerIndex++;
                        final int dpI1 = datePickerIndex;
                        final Element dpElement1 = element;
                        element.container = datePicker[datePickerIndex];
                        datePicker[datePickerIndex].setText(dateTimeText(element.vDate));
                        datePicker[datePickerIndex].setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.boxy_button_spinner, null));
                        datePicker[datePickerIndex].setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_expand_more_black_24dp, 0);

                        datePicker[datePickerIndex].setInputType(InputType.TYPE_NULL);
                        datePicker[datePickerIndex].setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
                        datePicker[datePickerIndex].setOnFocusChangeListener(new View.OnFocusChangeListener() {
                            @Override
                            public void onFocusChange(View v, boolean hasFocus) {
                                if (hasFocus) {
                                    if (element.vDate == null)
                                        element.vDate = new Date();
                                    calendar.setTime(element.vDate);
                                    currentDatePicker = dpI1;
                                    currentDateElement = dpElement1;
                                    if (!readOnly)
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
                                if (!readOnly)
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
                        //ImageView speachButton = new ImageView(thisActivity);
                        currentEditText = editText;
                        element.container = editText;
                        editText.setText(element.vText);
                        //change the element, but do not save:
                        editText.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                            }

                            @Override
                            public void afterTextChanged(Editable s) {
                                element.vText = s.toString();
                            }
                        });
                        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                            @Override
                            public void onFocusChange(View v, boolean hasFocus) {
                                if (!hasFocus) {
                                    element.vText = ((EditText) v).getText().toString();
                                    //saveToCloud();
                                }
                            }
                        });
                        LinearLayout horizontalLL = new LinearLayout(thisActivity);
                        horizontalLL.setOrientation(LinearLayout.HORIZONTAL);
                        horizontalLL.setLayoutParams(new LinearLayoutCompat.LayoutParams(LinearLayoutCompat.LayoutParams.MATCH_PARENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT, 1));

                        //ADIK: 06 August 2016:
                        TextInputLayout fieldHint = new TextInputLayout(thisActivity);
                        fieldHint.setHint(element.description);
                        fieldHint.setLayoutParams(new LinearLayout.LayoutParams(0,
                                LinearLayout.LayoutParams.WRAP_CONTENT, 1));
                        fieldHint.addView(editText);
                        horizontalLL.addView(fieldHint);
                        LinearLayout.LayoutParams loParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        loParams.gravity = Gravity.CENTER_VERTICAL;
                        /*
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
                        */

                        LL.addView(horizontalLL);
                        break;
                    case eTextNum:
                        final EditText editTextNum = new EditText(thisActivity);
                        currentEditText = editTextNum;
                        element.container = editTextNum;
                        editTextNum.setText(element.vText);
                        //change the element, but do not save:
                        editTextNum.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                            }

                            @Override
                            public void afterTextChanged(Editable s) {
                                element.vText = s.toString();
                            }
                        });
                        editTextNum.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                            @Override
                            public void onFocusChange(View v, boolean hasFocus) {
                                if (!hasFocus) {
                                    element.vText = ((EditText) v).getText().toString();
                                    //saveToCloud();
                                }
                            }
                        });
                        LinearLayout horizontalLLNum = new LinearLayout(thisActivity);
                        horizontalLLNum.setOrientation(LinearLayout.HORIZONTAL);
                        horizontalLLNum.setLayoutParams(new LinearLayoutCompat.LayoutParams(LinearLayoutCompat.LayoutParams.MATCH_PARENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT, 1));

                        //ADIK: 06 August 2016:
                        TextInputLayout fieldHintNum = new TextInputLayout(thisActivity);
                        fieldHintNum.setHint(element.description);
                        editTextNum.setInputType(InputType.TYPE_CLASS_NUMBER);
                        if (element.fireBaseFieldName.contains("SUM")) {
                            //TODO: editText.setMask
                            editTextNum.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
                            editTextNum.setRawInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                            editTextNum.addTextChangedListener(new AmountWatcher(editTextNum));
                        }
                        if (element.fireBaseFieldName.contains("PHONE")) {
                            //TODO: editText.setMask
                            editTextNum.addTextChangedListener(new PhoneWatcher(editTextNum));
                        }
                        fieldHintNum.setLayoutParams(new LinearLayout.LayoutParams(0,
                                LinearLayout.LayoutParams.WRAP_CONTENT, 1));
                        fieldHintNum.addView(editTextNum);
                        horizontalLLNum.addView(fieldHintNum);
                        LinearLayout.LayoutParams loParamsNum = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        loParamsNum.gravity = Gravity.CENTER_VERTICAL;
                        //hide button if there is no speech recognition on the device:
                        LL.addView(horizontalLLNum);
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
                        planButton.setText(element.vPlan.damageDescription);
                        planButton.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.boxy_button_spinner, null));
                        planButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_expand_more_black_24dp, 0);
                        if (!readOnly)
                            planButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    currentElement = element;
                                    currentButton = planButton;
                                    Intent intent = new Intent(thisActivity, VehicleDamageActivity.class);
                                    InsReport.damagePlanData = element.vPlan;
                                    InsReport.currentElement = element;
                                    InsReport.logFirebase("Open Damage Plan: " + currentForm.id);
                                    startActivityForResult(intent, DAMAGE_PLAN_INTENT);
                                }
                            });
                        if (readOnly)
                            planButton.setVisibility(View.GONE);
                        TextView damageTV = new TextView(this);
                        damageTV.setText(element.description);
                        LL.addView(damageTV);
                        LL.addView(planButton);
                        break;
                    case ePhoto:
                        final Button photoButton = new Button(this);
                        element.container = photoButton;
                        int count = 0;
                        for (Element element1 : element.elements) {
                            if (!element1.deleted)
                                count++;
                        }
                        photoButton.setText(count + " фото");
                        photoButton.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.boxy_button_spinner, null));
                        photoButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_expand_more_black_24dp, 0);

                        photoButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                InsReport.currentElement = element;
                                InsReport.currentForm = currentForm;
                                Intent intentPhoto = new Intent(thisActivity, PhotosActivity.class);
                                startActivityForResult(intentPhoto, REQUEST_ELEMENT_PHOTO);
                            }
                        });
                        TextView photoTV = new TextView(this);
                        photoTV.setText(element.description);
                        LL.addView(photoTV);
                        LL.addView(photoButton);
                        break;
                    case eDraw:
                        final Button drawButton = new Button(this);
                        element.container = drawButton;
                        drawButton.setText(span2Strings(element.description, element.toString()), Button.BufferType.SPANNABLE);
                        drawButton.setCompoundDrawablesWithIntrinsicBounds(element.getBitmapDrawable(this), null, null, null);
                        if (!readOnly)
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
                            cameraAndPictures.getPicFromFirebase(photoID, linearLayoutPhoto1);

                        final Button animaButton = new Button(this);
                        //element.container = animaButton;
                        animaButton.setText(span2Strings(element.description, element.toString()), Button.BufferType.SPANNABLE);
                        animaButton.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT));
//                        if (!readOnly)
                        animaButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                InsReport.currentElement = element;
                                InsReport.currentForm = currentForm;
                                currentElement = element;
                                currentButton = animaButton;
                                Intent intent = new Intent(thisActivity, AnimaActivity.class);
                                intent.putExtra("readOnly", readOnly);
                                //InsReport.damagePlanData = element.vPlan;
                                startActivityForResult(intent, ANIMA_INTENT);
                            }
                        });
//                        if (readOnly)
//                            animaButton.setVisibility(View.GONE);
                        linearLayoutPhoto1.addView(animaButton);
                        scrollViewPhoto1.addView(linearLayoutPhoto1);
                        LL.addView(scrollViewPhoto1);
                        break;
                    case eSignature:
                        final Button drawButton1 = new Button(this);
                        element.container = drawButton1;
                        drawButton1.setText(span2Strings(element.description, element.toString()), Button.BufferType.SPANNABLE);
                        drawButton1.setCompoundDrawablesWithIntrinsicBounds(element.getBitmapDrawable(this), null, null, null);
                        if (!readOnly)
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
                        horizontalLLCombo.setOrientation(LinearLayout.VERTICAL);
                        horizontalLLCombo.setLayoutParams(new LinearLayoutCompat.LayoutParams(LinearLayoutCompat.LayoutParams.MATCH_PARENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT, 1));

                        TextView captionTVCombo = new TextView(this);
                        captionTVCombo.setText(element.description);

                        captionTVCombo.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

                        final Button comboButton = new Button(this);
                        element.container = comboButton;
                        comboButton.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                        if (!element.directory.equals("") &&
                                InsReport.directories.map.get(element.directory) != null &&
                                !element.vText.equals("")) {
                            ArrayList<DirectoryItem> directoryItems1 = InsReport.directories.map.get(element.directory).items;
                            for (DirectoryItem directoryItem : directoryItems1) {
                                if (directoryItem.id.equals(element.vText))
                                    comboButton.setText(directoryItem.name);
                            }
                        }
                        comboButton.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.boxy_button_spinner, null));
                        comboButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_expand_more_black_24dp, 0);
                        if (!element.directory.equals("") &&
                                InsReport.directories.map.get(element.directory) != null) {
                            final ArrayList<DirectoryItem> directoryItems = InsReport.directories.map.get(element.directory).items;
                            if (directoryItems.size() > 0) {
                                comboButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        openLookUpDialog(comboButton, directoryItems, element);
                                    }
                                });
                            }
                        }
                        horizontalLLCombo.addView(captionTVCombo);
                        horizontalLLCombo.addView(comboButton);
                        LL.addView(horizontalLLCombo);
                        break;
                    case eComboFromFile:
                        LinearLayout horizontalLLComboFromFile = new LinearLayout(thisActivity);
                        horizontalLLComboFromFile.setOrientation(LinearLayout.VERTICAL);
                        horizontalLLComboFromFile.setLayoutParams(new LinearLayoutCompat.LayoutParams(LinearLayoutCompat.LayoutParams.MATCH_PARENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT, 1));

                        TextView captionTVComboFromFile = new TextView(this);
                        captionTVComboFromFile.setText(element.description);

                        captionTVComboFromFile.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

                        final Button comboFFButton = new Button(this);
                        element.container = comboFFButton;
                        comboFFButton.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                        comboFFButton.setText(element.vText);
                        comboFFButton.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.boxy_button_spinner, null));
                        comboFFButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_expand_more_black_24dp, 0);

                        comboFFButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                openLookUpFromFile(comboFFButton, element);
                            }
                        });

                        horizontalLLComboFromFile.addView(captionTVComboFromFile);
                        horizontalLLComboFromFile.addView(comboFFButton);
                        LL.addView(horizontalLLComboFromFile);
                        break;
                    case eComboMulti:
                        TextView captionTVComboMulti = new TextView(this);
                        captionTVComboMulti.setText(element.description);

                        captionTVComboMulti.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

                        final Button comboButtonMulti = new Button(this);
                        element.container = comboButtonMulti;
                        comboButtonMulti.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                        if (!element.directory.equals("") &&
                                InsReport.directories.map.get(element.directory) != null &&
                                !element.vText.equals("")) {
                            ArrayList<DirectoryItem> directoryItems1 = InsReport.directories.map.get(element.directory).items;
                            String[] codes = element.vText.split(";");
                            String compoundText = "";
                            for (String code : codes) {
                                for (DirectoryItem directoryItem : directoryItems1) {
                                    if (directoryItem.id.equals(code)) {
                                        if (!compoundText.equals(""))
                                            compoundText += ", ";
                                        compoundText += directoryItem.name;
                                    }
                                }
                            }
                            comboButtonMulti.setText(compoundText);
                        }
                        comboButtonMulti.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.boxy_button_spinner, null));
                        comboButtonMulti.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_expand_more_black_24dp, 0);
                        if (!element.directory.equals("") &&
                                InsReport.directories.map.get(element.directory) != null) {
                            final ArrayList<DirectoryItem> directoryItems = InsReport.directories.map.get(element.directory).items;
                            if (directoryItems.size() > 0) {
                                comboButtonMulti.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        openLookUpDialogMulti(comboButtonMulti, directoryItems, element);
                                    }
                                });
                            }
                        }
                        LL.addView(captionTVComboMulti);
                        LL.addView(comboButtonMulti);
                        break;
                    case eRadio:
                        LinearLayout horizontalLLRadio = new LinearLayout(thisActivity);
                        horizontalLLRadio.setOrientation(LinearLayout.HORIZONTAL);
                        horizontalLLRadio.setLayoutParams(new LinearLayoutCompat.LayoutParams(LinearLayoutCompat.LayoutParams.MATCH_PARENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT, 1));
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
                        final AutoCompleteTextView lookupEditText = new AutoCompleteTextView(this);
                        element.container = lookupEditText;
                        //lookupEditText.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
                        /*
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
                        };*/
                        lookupEditText.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                            }

                            @Override
                            public void afterTextChanged(Editable s) {
                                element.vText = s.toString();
                            }
                        });
                        lookupEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                            @Override
                            public void onFocusChange(View v, boolean hasFocus) {
                                if (!hasFocus) {
                                    element.vText = ((EditText) v).getText().toString();
                                    //saveToCloud();
                                }
                            }
                        });

                        String[] autoComplete = new String[InsReport.directories.map.get(element.directory).items.size()];
                        int i = 0;
                        for (DirectoryItem item : InsReport.directories.map.get(element.directory).items) {
                            autoComplete[i] = item.name;
                            i++;
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                                this, android.R.layout.simple_list_item_1,
                                autoComplete
                        );

                        lookupEditText.setAdapter(adapter);

                        TextInputLayout fieldHintAuto = new TextInputLayout(thisActivity);
                        fieldHintAuto.setHint(element.description);
                        fieldHintAuto.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT, 1));
                        fieldHintAuto.addView(lookupEditText);

                        LL.addView(fieldHintAuto);
                        break;
                    default:
                        break;
                }
        }
    }

    private void openLookUpDialog(final Button lookupEditText, final ArrayList<DirectoryItem> directoryItems, final Element element) {
        final Dialog lookUpDialog = new Dialog(this);
        lookUpDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        lookUpDialog.setContentView(getLayoutInflater().inflate(R.layout.lookup
                , null));

        ListView listView = (ListView) lookUpDialog.findViewById(R.id.ListView007);
        final EditText filterEditText = (EditText) lookUpDialog.findViewById(R.id.filterEditText);
        final KolesaAdapter kolesaAdapter = new KolesaAdapter(this, directoryItems, filterEditText);
        listView.setAdapter(kolesaAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedString = kolesaAdapter.filteredObjects.get(i).name;
                lookupEditText.setText(selectedString);
                element.vText = kolesaAdapter.filteredObjects.get(i).id;
                lookUpDialog.dismiss();
            }
        });
        lookUpDialog.findViewById(R.id.addToDirectory).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewDirectoryItem(filterEditText.getText().toString(), lookupEditText, element);
                lookUpDialog.dismiss();
            }
        });

        lookUpDialog.show();
    }

    private void openLookUpFromFile(final Button lookupEditText, final Element element) {
        final Dialog lookUpDialog = new Dialog(this);
        lookUpDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        lookUpDialog.setContentView(getLayoutInflater().inflate(R.layout.lookup
                , null));

        ArrayList<DirectoryItem> directoryItems = new ArrayList<>();
        if (element.fireBaseFieldName.equals("OBJECT_PRODUCTION")) {
            for (String carProducer : InsReport.carProducers) {
                directoryItems.add(new DirectoryItem(carProducer, carProducer));
            }
        }
        if (element.fireBaseFieldName.equals("OBJECT_MODEL")) {
            String s = "not found";
            for (Element elementa : currentForm.objects.elements) {
                Log.w(TAG, "car: looking into some object: " + elementa.description);
                String s1 = "";
                boolean thisObject = false;
                for (Element element1 : elementa.elements) {

                    if (element1 == element)
                        thisObject = true;

                    if (element1.fireBaseFieldName.equals("OBJECT_PRODUCTION")) {
                        Log.w(TAG, "element: found in some object: " + element1.vText);
                        s1 = element1.vText;
                    }
                }
                if (!s1.equals("") && thisObject) {
                    s = s1;
                    Log.w(TAG, "openLookUpFromFile: found corect object: " + s);
                }
            }

            Log.w(TAG, "openLookUpFromFile: setting filter: " + s);
            for (String carModel : InsReport.carModels) {
                String a = carModel.split(",")[0];
                String b = carModel.split(",")[1];
                if (a.trim().contains(s.trim())) {
                    directoryItems.add(new DirectoryItem(b, b));
                }
            }
        }

        ListView listView = (ListView) lookUpDialog.findViewById(R.id.ListView007);
        final EditText filterEditText = (EditText) lookUpDialog.findViewById(R.id.filterEditText);
        final KolesaAdapter kolesaAdapter = new KolesaAdapter(this, directoryItems, filterEditText);
        listView.setAdapter(kolesaAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedString = kolesaAdapter.filteredObjects.get(i).name;
                lookupEditText.setText(selectedString);
                element.vText = kolesaAdapter.filteredObjects.get(i).id;
                lookUpDialog.dismiss();
            }
        });
        lookUpDialog.findViewById(R.id.addToDirectory).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lookupEditText.setText(filterEditText.getText().toString());
                element.vText = filterEditText.getText().toString();
                lookUpDialog.dismiss();
            }
        });

        lookUpDialog.show();
    }


    private void addNewDirectoryItem(String s, final Button lookupEditText, final Element element) {
        final Dialog newDirectoryItemDialog = new Dialog(this);
        newDirectoryItemDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        newDirectoryItemDialog.setContentView(getLayoutInflater().inflate(R.layout.add_new_directory_item
                , null));
        final EditText dirNameEditText = (EditText) newDirectoryItemDialog.findViewById(R.id.filterEditText);
        dirNameEditText.setText(s);
        final EditText codeEditText = (EditText) newDirectoryItemDialog.findViewById(R.id.directoryCode);
        newDirectoryItemDialog.findViewById(R.id.addToDirectory).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (codeEditText.getText().toString().equals("")) {
                    Toast.makeText(IncidentFormActivity.this, "Не введён код", Toast.LENGTH_LONG).show();
                    return;
                }
                ArrayList<DirectoryItem> directoryItems1 = InsReport.directories.map.get(element.directory).items;
                for (DirectoryItem directoryItem : directoryItems1)
                    if (directoryItem.id.equals(codeEditText.getText().toString())) {
                        Toast.makeText(IncidentFormActivity.this, "Код уже существует!", Toast.LENGTH_LONG).show();
                        return;
                    }
                addNewDirectoryItem(dirNameEditText.getText().toString(), codeEditText.getText().toString(), element.directory);
                lookupEditText.setText(dirNameEditText.getText().toString());
                element.vText = codeEditText.getText().toString();
                newDirectoryItemDialog.dismiss();
            }
        });
        newDirectoryItemDialog.show();
    }

    private void addNewDirectoryItem(String s, String code, String directory) {
        InsReport.ref.child("dirs").child(directory).child(code).child("name").setValue(s);
        InsReport.ref.child("dirs").child(directory).child(code).child("status").setValue("Y");
    }

    private void openLookUpDialogMulti(final Button lookupEditText, final ArrayList<DirectoryItem> directoryItems, final Element element) {
        final Dialog lookUpDialogMulti = new Dialog(this);
        lookUpDialogMulti.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        lookUpDialogMulti.setContentView(getLayoutInflater().inflate(R.layout.lookup_multi
                , null));

        ListView listView = (ListView) lookUpDialogMulti.findViewById(R.id.ListView007);
        EditText filterEditText = (EditText) lookUpDialogMulti.findViewById(R.id.filterEditText);
        final KolesaAdapterMulti kolesaAdapterMulti = new KolesaAdapterMulti(this, directoryItems, filterEditText, element.vText);
        listView.setAdapter(kolesaAdapterMulti);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //TODO: .code
            }
        });

        ((Button) lookUpDialogMulti.findViewById(R.id.buttonOKMulti)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String selectedString = "";
                String selectedText = "";
                for (String selectedId : kolesaAdapterMulti.selectedIds) {
                    if (!selectedString.equals("")) {
                        selectedString += ";";
                        selectedText += ", ";
                    }
                    selectedString += selectedId;
                    for (DirectoryItem directoryItem : directoryItems) {
                        if (directoryItem.id.equals(selectedId))
                            selectedText += directoryItem.name;
                    }
                }
                element.vText = selectedString;
                lookupEditText.setText(selectedText);
                lookUpDialogMulti.dismiss();
            }
        });
        lookUpDialogMulti.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SPEACH_INTENT && resultCode == RESULT_OK) {
            ArrayList<String> matches = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            if (matches.size() > 0)
                currentEditText.setText(matches.get(0));
        }
        if (requestCode == DAMAGE_PLAN_INTENT) {
            //TODO: Warning: if you start the plan, rotate device and exit activity, currentButton does not exist any more, so, the app crashes!
            if (currentButton != null)
                currentButton.setText(currentElement.vPlan.damageDescription);
        }
        if (requestCode == FREE_DRAW_INTENT) {
            //currentElement.freeDrawBitmap = null;
            //TODO: get the current element from the currentElement.container!

            //if (currentButton != null) {
            //    currentButton.setText(span2Strings(currentElement.description, currentElement.toString()), Button.BufferType.SPANNABLE);
            //    currentButton.setCompoundDrawablesWithIntrinsicBounds(currentElement.getBitmapDrawable(this), null, null, null);
            //}
        }
        if (requestCode == REQUEST_ELEMENT_PHOTO) {
            int count = 0;
            for (Element element : InsReport.currentElement.elements) {
                if (!element.deleted)
                    count++;
            }
            ((Button) InsReport.currentElement.container).setText(count + " фото");
        }

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            final String id = CameraAndPictures.savePictureToFirebase(Environment.getExternalStorageDirectory() + File.separator + "image.jpg");
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
                final LinearLayout linearLayout = (LinearLayout) InsReport.currentElement.container;
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

        if (!readOnly)
            saveToCloud();
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void saveToCloud() {
        currentForm.updateDescription();
        currentForm.validate();
        currentForm.saveToCloud();
        InsReport.logFirebase("Saving: " + currentForm.id + ", " + currentForm.description);
        setTitle(currentForm.description);
    }

    private void takePhoto() {
        CameraAndPictures.bitmap = null;
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File file = new File(Environment.getExternalStorageDirectory() + File.separator + "image.jpg");
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
            if (!readOnly)
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    public SpannableStringBuilder span2Strings(String string1, String string2) {
        SpannableStringBuilder spanSin = new SpannableStringBuilder();
        SpannableString itemasin = new SpannableString(string1 + "\n");
        itemasin.setSpan(new AbsoluteSizeSpan(9, true), 0, itemasin.length(), 0);
        itemasin.setSpan(new ForegroundColorSpan(Color.parseColor("#EA7C07")), 0, itemasin.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spanSin.append(itemasin);

        SpannableString itemsin = new SpannableString(string2);
        itemsin.setSpan(new AbsoluteSizeSpan(12, true), 0, itemsin.length(), 0);
        spanSin.append(itemsin);
        return spanSin;
    }

    @Override
    public void onBackPressed() {
        InsReport.currentForm = null;
        if (!readOnly)
            saveToCloud();
        finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        InsReport.currentForm = null;
        if (currentForm != null && !readOnly) {
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
