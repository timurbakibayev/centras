package com.gii.insreport;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.ServerValue;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private static String TAG = "MainActivity.java";

    boolean nowReleaseButtons = false;
    boolean triggerLastForm = false;

    Timer timer = new Timer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        InsReport.storage = FirebaseStorage.getInstance();
        //InsReport.storageRef = InsReport.storage.getReferenceFromUrl("gs://insreport-f39a3.appspot.com");



        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });


        checkDeviceName(this);

/*        ((Button)findViewById(R.id.button_add_plan)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mainActivity, VehicleDamageActivity.class);
                //EditText editText = (EditText) findViewById(R.id.edit_message);
                //String message = editText.getText().toString();
                //intent.putExtra(EXTRA_MESSAGE, message);
                startActivity(intent);
            }
        });
*/
        addForms();

        /*
        grantStoragePermission();
        grantCameraPermission();
        grantCallPermission();
        grantSMSPermission();
        */
        grantAllPermissions();

        refreshUser();
        InsReport.mainActivity = this;

        //Intent intent = new Intent(this, AnimaActivity.class);
        //startActivityForResult(intent, 10);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                boolean allLoaded = true;
                for (FormsCollection mainMenuForm : InsReport.mainMenuForms) {
                    if (!mainMenuForm.loadComplete) {
                        allLoaded = false;
                        Log.e(TAG, "run: something is not yet loaded: " + mainMenuForm.fireBaseCatalog);
                    }
                }
                if (!InsReport.directories.loaded) {
                    allLoaded = false;
                    Log.e(TAG, "run: directories are not yet loaded");
                }
                if (allLoaded || InsReport.mAuth.getCurrentUser() == null) {
                    timer.cancel();
                    final ProgressBar pb = (ProgressBar) findViewById(R.id.roundProgressbar);
                    if (pb != null && pb.getHandler() != null)
                    pb.getHandler().post(new Runnable() {
                        @Override
                        public void run() {
                            boolean a1 = checkIfNeededToFindByPhone();
                            boolean a2 = checkNewForms(thisActivity, true);
                            if (!(a1 || a2)) {
                                pb.setVisibility(View.GONE);
                                for (Button formButton : formButtons) {
                                    formButton.setEnabled(true);
                                }
                            }
                            nowReleaseButtons = true;
                        }
                    });
                    Log.e(TAG, "run: ALL LOADED");
                }
            }
        }, 1000, 1000);
        if (InsReport.sharedPref.getBoolean("open_last_doc",false))
            triggerLastForm = true;
    }

    static boolean dontCheckTwice = false;

    public static void checkDeviceName(Context context) {
        if (dontCheckTwice)
            return;
        Log.e(TAG, "checkDeviceName: entry");
        if (!InsReport.sharedPref.getString("devicename", "").equals(""))
            return;
        Log.e(TAG, "checkDeviceName: need a new name");
        showDeviceNameDialog(context);
    }

    public static void showDeviceNameDialog(Context context) {
        if (InsReport.user == null)
            return;
        dontCheckTwice = true;
        final Dialog deviceNameDialog = new Dialog(context);
        final EditText deviceNameET = new EditText(context);
        deviceNameET.setText(InsReport.sharedPref.getString("devicename", ""));
        TextView captionTV = new TextView(context);
        captionTV.setText("Введите название устройства, например 'Samsung s5' или 'Планшет Арман'. " +
                "Это необходимо для возможности " +
                "блокировки устройства при потере.");
        Button positiveButton = new Button(context);
        positiveButton.setText("OK");
        LinearLayout deviceNameLL = new LinearLayout(context);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(50, 50, 50, 50);
        deviceNameLL.setLayoutParams(lp);
        deviceNameLL.setOrientation(LinearLayout.VERTICAL);
        deviceNameLL.addView(captionTV);
        deviceNameLL.addView(deviceNameET);
        deviceNameLL.addView(positiveButton);
        //deviceNameDialog.setTitle("Введите название устройства, например 'Телефон Samsung' или 'Планшет Армана'");
        deviceNameDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        deviceNameDialog.setContentView(deviceNameLL);
        deviceNameDialog.show();
        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newName = deviceNameET.getText().toString();
                if (!newName.equals("")) {
                    InsReport.savePref("devicename", newName);
                    InsReport.ref.child("users/" + InsReport.user.getUid() + "/devices/" + FirebaseInstanceId.getInstance().getToken().replaceAll("[^0-9]+", "")).setValue(
                            InsReport.sharedPref.getString("devicename", "Неизвестное устройство"));
                    deviceNameDialog.dismiss();
                    InsReport.logFirebase("Device renamed to: " + newName);
                }
            }
        });
    }

    public void grantAllPermissions() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (this.checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                    this.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                    this.checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED ||
                    this.checkSelfPermission(Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED ||
                    this.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                    this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                //Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        android.Manifest.permission.CALL_PHONE,
                        Manifest.permission.SEND_SMS,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                }, 1);

            }
        }
    }

    public void grantStoragePermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (this.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
            } else {
                //Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }
    }

    public void grantCameraPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (this.checkSelfPermission(Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED) {
            } else {
                //Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, 1);

            }
        }
    }

    public void grantCallPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (this.checkSelfPermission(Manifest.permission.CALL_PHONE)
                    == PackageManager.PERMISSION_GRANTED) {
            } else {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CALL_PHONE}, 1);

            }
        }
    }

    public void grantSMSPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (this.checkSelfPermission(Manifest.permission.SEND_SMS)
                    == PackageManager.PERMISSION_GRANTED) {
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 1);

            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_login) {
            login();
        }

        if (id == R.id.action_settings) {
            settings();
        }

        return super.onOptionsItemSelected(item);
    }

    public void login() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivityForResult(intent, 1);
    }

    public void settings() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivityForResult(intent,17);
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.e(TAG, "onResume: YES");

        if (InsReport.directories.loaded) {
            checkIfNeededToArrivedByPhone();
            checkIfNeededToFindByPhone();
            checkNewForms(this,false);
            final ProgressBar pb = (ProgressBar) findViewById(R.id.roundProgressbar);
            if (pb != null && pb.getHandler() != null) {
                pb.getHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        pb.setVisibility(View.GONE);
                        for (Button formButton : formButtons) {
                            formButton.setEnabled(true);
                        }
                    }
                });
            };
        }
    }

    private boolean checkIfNeededToFindByPhone() {
        if (getIntent().hasExtra("findByPhone")) {
            String phoneNo = getIntent().getExtras().getString("findByPhone");
            if (phoneNo != null && !phoneNo.equals("")) {
                for (Form form : InsReport.incidentFormsCollection.forms) {
                    if (form.status.equals("accept") &&
                            form.phoneNo.equals(phoneNo) ||
                            (form.input.get("CLAIMANT_PHONE_NO") != null &&
                                    form.input.get("CLAIMANT_PHONE_NO").equals(phoneNo))
                            ) {
                        //here open the form
                        InsReport.savePref("lastFormId","");
                        openTheForm(form, this);
                        getIntent().removeExtra("findByPhone");
                        if (form.status.equals("accept"))
                            return true;
                        return false;
                    }
                }
            }
        }
        return false;
    }

    private boolean checkIfNeededToArrivedByPhone() {
        if (getIntent().hasExtra("arrivedByPhone")) {
            String phoneNo = getIntent().getExtras().getString("arrivedByPhone");
            if (phoneNo != null && !phoneNo.equals("")) {
                for (Form form : InsReport.incidentFormsCollection.forms) {
                    if (form.status.equals("accept") &&
                            form.phoneNo.equals(phoneNo) ||
                            (form.input.get("CLAIMANT_PHONE_NO") != null &&
                                    form.input.get("CLAIMANT_PHONE_NO").equals(phoneNo))
                            ) {
                        //set successfully arrived
                        InsReport.savePref("lastFormId","");
                        InsReport.ref.child("arrivals/"+form.fireBaseCatalog + "/" + form.id + "/created").setValue(form.dateCreated);
                        InsReport.ref.child("arrivals/"+form.fireBaseCatalog + "/" + form.id + "/arrived").setValue(ServerValue.TIMESTAMP);
                        openTheForm(form, this);
                        getIntent().removeExtra("arrivedByPhone");
                        return true;
                    }
                }
            }
        }
        return false;
    }


    ArrayList<Button> formButtons = new ArrayList<>();
    Button loginButton = null;
    MainActivity thisActivity = this;

    private void addForms() {
        final float scale = getResources().getDisplayMetrics().density;
        LinearLayout mainMenuLL = (LinearLayout) findViewById(R.id.mainMenuLL);
        mainMenuLL.removeAllViews();
        for (final FormsCollection formsCollection : InsReport.mainMenuForms) {
            Button newMenuButton = new Button(this);
            formButtons.add(newMenuButton);
            newMenuButton.setText(formsCollection.description);
            newMenuButton.setHeight((int) (96 * scale + 0.5f));
            newMenuButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(thisActivity, FormsList.class);
                    String message = formsCollection.fireBaseCatalog;
                    intent.putExtra(InsReport.EXTRA_FIREBASE_CATALOG, message);
                    InsReport.logFirebase("Opening " + formsCollection.fireBaseCatalog);
                    startActivity(intent);
                }
            });
            newMenuButton.setEnabled(false);
            mainMenuLL.addView(newMenuButton);
        }
        Button serverButton = new Button(this);
        serverButton.setText("Эмулятор сервера");
        serverButton.setHeight((int) (96 * scale + 0.5f));
        serverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent serverIntent = new Intent(thisActivity, ServerEmuActivity.class);
                startActivity(serverIntent);
            }
        });
        formButtons.add(serverButton);
        serverButton.setEnabled(false);
        mainMenuLL.addView(serverButton);

        Button newMenuButton = new Button(this);
        formButtons.add(newMenuButton);
        newMenuButton.setText("АВТОРИЗАЦИЯ");
        newMenuButton.setHeight((int) (96 * scale + 0.5f));
        newMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(thisActivity, LoginActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        loginButton = newMenuButton;
        mainMenuLL.addView(newMenuButton);
    }

    public boolean checkNewForms(Activity context, boolean formsAreJustLoaded) {
        if (InsReport.formToBeAccepted != null &&
                InsReport.formToBeAccepted.status.equals("")) {
            Log.e(TAG, "checkNewForms: acceptOrReject");
            acceptOrRejectDialogShow(InsReport.formToBeAccepted, context);
            return false;
        }
        if (formsAreJustLoaded && triggerLastForm) {
            String lastFormId = InsReport.sharedPref.getString("lastFormId", "");
            String fireBaseCatalog = InsReport.sharedPref.getString("lastFormCatalog","");
            if (!lastFormId.equals("")) {
                openTheForm(lastFormId, fireBaseCatalog, thisActivity);
                return true;
            }
        }
        return false;
    }

    public void openTheForm(Form form, Activity context) {
        if (form.status.equals("accept"))
            openTheForm(form.id,form.fireBaseCatalog,context);
        if (form.status.equals("")) {
            Log.e(TAG, "openTheForm: acceptOrRejectDialogShow");
            acceptOrRejectDialogShow(form, context);
        }
    }

    public void openTheForm(String id, String fireBaseCatalog, Activity context) {
        //TODO: Find the form, if the form is ready, don't open it!
        Intent intent = new Intent(thisActivity, IncidentFormActivity.class);
        intent.putExtra(InsReport.EXTRA_FIREBASE_CATALOG, fireBaseCatalog);
        intent.putExtra(InsReport.EXTRA_ID_NO, id);
        context.startActivity(intent);
    }

    public void acceptOrRejectDialogShow(final Form form, final Activity context) {
        Log.e(TAG, "acceptOrRejectDialogShow");
        InsReport.formToBeAccepted = null; //we don't have to accept or reject it second time. And it is now outdated anyways.
        final Dialog acceptOrRejectDialog = new Dialog(context);
        acceptOrRejectDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        acceptOrRejectDialog.setContentView(context.getLayoutInflater().inflate(R.layout.accept_reject
                , null));

        String headerText = "";
        final String phoneNo;
        final String address;
        final String personName;



        if (form.input.get("CLIENT_NAME") != null) {
            headerText += form.input.get("CLIENT_NAME") + "\n";
            personName = form.input.get("CLIENT_NAME");
        } else
            personName = "";

        if (form.input.get("CLAIMANT_PHONE_NO") != null) {
            headerText += "Телефон: " + form.input.get("CLAIMANT_PHONE_NO") + "\n";
            phoneNo = form.input.get("CLAIMANT_PHONE_NO");
        } else
            phoneNo = "";
        if (form.input.get("EVENT_PLACE") != null) {
            headerText += "Адрес: " + form.input.get("EVENT_PLACE") + "\n";
            address = form.input.get("EVENT_PLACE");
        } else
            address = "";

        ((TextView) acceptOrRejectDialog.findViewById(R.id.textHeader)).setText(headerText);

        ( acceptOrRejectDialog.findViewById(R.id.buttonAccept)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if this is first time, save the time of accept/reject
                if (form.status.equals(""))
                    InsReport.ref.child("forms/" + form.fireBaseCatalog + "/" + InsReport.forceUserID() + "/" + form.id + "/dateAccepted").
                            setValue(ServerValue.TIMESTAMP);
                form.status = "accept";
                form.statusNote = "В работе";
                scheduleNotification(personName,phoneNo,address,0);

                Log.e(TAG, "Sending SMS: " + SMS.send(phoneNo,
                        getString(R.string.accept_sms, "Нурбек"))); //phoneNo
                form.formReady = false;
                if (form.elements.size() == 0) {
                    FormTemplates.applyTemplate(form, form.fireBaseCatalog);
                    form.updateDescription();
                    form.validate();
                }
                form.saveToCloud();
                InsReport.notifyFormsList();
                InsReport.logFirebase("Accepted " + form.fireBaseCatalog + " form no. " + form.id);
                acceptOrRejectDialog.dismiss();
                openTheForm(form, context);
            }
        });

        ( acceptOrRejectDialog.findViewById(R.id.buttonReject)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if this is first time, save the time of accept/reject
                if (form.status.equals(""))
                    InsReport.ref.child("forms/" + form.fireBaseCatalog + "/" + InsReport.forceUserID() + "/" + form.id + "/dateAccepted").
                            setValue(ServerValue.TIMESTAMP);
                form.status = "reject";
                form.formReady = true;
                InsReport.logFirebase("Rejected " + form.fireBaseCatalog + " form no. " + form.id);
                form.saveToCloud();
                InsReport.notifyFormsList();
                // Clear all notification
                NotificationManager nMgr = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                nMgr.cancel(form.calculateId());
                acceptOrRejectDialog.dismiss();
                askWhyRejected(form, context);
            }
        });

        ( acceptOrRejectDialog.findViewById(R.id.buttonCall)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + phoneNo.trim()));
                if (intent.resolveActivity(getPackageManager()) != null) {
                    InsReport.logFirebase("Make a call from accept-reject dialog: " + form.fireBaseCatalog + " form no. " + form.id + ", TEL: " + phoneNo);
                    startActivity(intent);
                }
                acceptOrRejectDialog.dismiss();
            }
        });

        //Add sms not sure how to connect to firebase
        (acceptOrRejectDialog.findViewById(R.id.buttonDelay)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SMS.send(phoneNo,
                        getString(R.string.delay_sms, "Нурбек", 30));
                NotificationManager nMgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                nMgr.cancel(form.calculateId());
                //TODO: change this to 30!
                int postponeMinutes = 1;
                scheduleNotification(personName,phoneNo,address,postponeMinutes);
                Date postponingDate = new Date((new Date()).getTime() + postponeMinutes * 60000);
                form.status = "postpone";
                form.statusNote = "Форма отложена до " + IncidentFormActivity.timeText(postponingDate);
                form.saveToCloud();
                InsReport.notifyFormsList();
                acceptOrRejectDialog.dismiss();
            }
        });

        ( acceptOrRejectDialog.findViewById(R.id.buttonMap)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_VIEW);
                String regex = "((-|\\+)?[0-9]+(\\.[0-9]+)?)+";
                String[] location = address.split(" ");
                boolean coordinates = false;

                for (int i = 0; i < location.length; i++) {
                    if (location[i].matches(regex)) {
                        coordinates = true;
                    }
                }
                String locationStr;

                if (coordinates) {
                    locationStr = location[0]+","+location[1];
                    Uri locationUri = Uri.parse("geo:0,0?").buildUpon()
                            .appendQueryParameter("q", locationStr)
                            .build();
                    intent.setData(locationUri);
                } else {
                    locationStr = address.replaceAll(" ", "+");

                    Uri locationUri = Uri.parse("geo:0,0?").buildUpon()
                            .appendQueryParameter("q", locationStr)
                            .build();
                    intent.setData(locationUri);
                }

                if (intent.resolveActivity(getPackageManager()) != null) {
                    InsReport.logFirebase("Open a map from app: " + form.fireBaseCatalog + " form no. " + form.id + ", Location: " + locationStr);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this,
                            "Нет карты", Toast.LENGTH_SHORT).show();
                }

                acceptOrRejectDialog.dismiss();

            }
        });


        acceptOrRejectDialog.show();
    }

    private void scheduleNotification(String name, String phone, String address, int delay) {
        Intent notificationIntent = new Intent(this, NotificationPublisher.class);
        notificationIntent.putExtra("name", name);
        notificationIntent.putExtra("phone", phone);
        notificationIntent.putExtra("address", address);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        long futureInMillis = SystemClock.elapsedRealtime() + delay * 60000;
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
    }

    public void askWhyRejected(final Form form, Context context) {
        final Dialog askWhyRejectedDialog = new Dialog(context);
        final EditText reasonET = new EditText(context);
        //reasonET.setText(InsReport.sharedPref.getString("reasonToReject", ""));
        askWhyRejectedDialog.setCancelable(false);
        TextView captionTV = new TextView(context);
        captionTV.setText("Введите причину отклонения заявки");
        Button positiveButton = new Button(context);
        positiveButton.setText("OK");
        LinearLayout reasonToRejectLL = new LinearLayout(context);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(50, 50, 50, 50);
        reasonToRejectLL.setLayoutParams(lp);
        reasonToRejectLL.setOrientation(LinearLayout.VERTICAL);
        reasonToRejectLL.addView(captionTV);

        String reasons[] = new String[]{"Консультация","Отмена клиентом","Нехватка времени"};

        reasonToRejectLL.addView(reasonET);

        for (final String reason : reasons) {
            Button anotherReasonButton = new Button(context);
            anotherReasonButton.setText(reason);
            anotherReasonButton.setBackgroundColor(Color.TRANSPARENT);
            anotherReasonButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    reasonET.setText(reason);
                }
            });
            reasonToRejectLL.addView(anotherReasonButton);
        }

        reasonToRejectLL.addView(positiveButton);
        askWhyRejectedDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        askWhyRejectedDialog.setContentView(reasonToRejectLL);
        askWhyRejectedDialog.show();
        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newReason = reasonET.getText().toString();
                if (!newReason.equals("")) {
                    InsReport.savePref("reasonToReject", newReason);
                    form.statusNote = newReason;
                    form.saveToCloud();
                    askWhyRejectedDialog.dismiss();
                    InsReport.logFirebase("Reason to reject the form " + form.id + ": " + newReason);
                    InsReport.notifyFormsList();
                }
            }
        });

    }


    public void askReadyResult(final Form form, Context context, final boolean closeActivity, final Activity activity) {
        final Dialog askForResult = new Dialog(context);
        final EditText reasonET = new EditText(context);
        //reasonET.setText(InsReport.sharedPref.getString("reasonToReject", ""));
        askForResult.setCancelable(false);
        TextView captionTV = new TextView(context);
        captionTV.setText("Введите результат завершения работы");
        Button positiveButton = new Button(context);
        positiveButton.setText("OK");
        LinearLayout formResultLL = new LinearLayout(context);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(50, 50, 50, 50);
        formResultLL.setLayoutParams(lp);
        formResultLL.setOrientation(LinearLayout.VERTICAL);
        formResultLL.addView(captionTV);

        String reasons[] = new String[]{"Оформлен ДТП","Урегулирован на месте","Направлен страховщику виновной стороны"};

        formResultLL.addView(reasonET);

        for (final String reason : reasons) {
            Button anotherReasonButton = new Button(context);
            anotherReasonButton.setText(reason);
            anotherReasonButton.setBackgroundColor(Color.TRANSPARENT);
            anotherReasonButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    reasonET.setText(reason);
                }
            });
            formResultLL.addView(anotherReasonButton);
        }

        formResultLL.addView(positiveButton);
        askForResult.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        askForResult.setContentView(formResultLL);
        askForResult.show();
        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newResult = reasonET.getText().toString();
                if (!newResult.equals("")) {
                    form.statusNote = newResult;
                    form.saveToCloud();
                    InsReport.notifyFormsList();
                    askForResult.dismiss();
                    if (closeActivity)
                        activity.finish();
                    InsReport.logFirebase("Done with result " + form.id + ": " + newResult);
                }
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 17) {
            for (FormsCollection mainMenuForm : InsReport.mainMenuForms) {
                mainMenuForm.forms.clear();
                mainMenuForm.addDataChangeListener();
                InsReport.ref.child("users/"+InsReport.user.getUid()+"/name").setValue(
                        InsReport.sharedPref.getString("username","")
                );
            }
        }
        refreshUser();
    }

    public void refreshUser() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        FirebaseUser user = InsReport.mAuth.getCurrentUser();
        if (user != null) {
            toolbar.setSubtitle("Online: " + user.getEmail());
            InsReport.userID = user.getEmail();
            for (Button formButton : formButtons) {
                formButton.setVisibility(View.VISIBLE);
            }
            loginButton.setVisibility(View.GONE);
        } else {
            toolbar.setSubtitle("Offline");
            InsReport.userID = "Incognito";
            for (Button formButton : formButtons) {
                formButton.setVisibility(View.GONE);
            }
            loginButton.setVisibility(View.VISIBLE);
        }

    }
}
