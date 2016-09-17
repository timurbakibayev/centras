package com.gii.insreport;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.ServerValue;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private static String TAG = "MainActivity.java";

    Timer timer = new Timer();

    int imgMainClicked = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        InsReport.storage = FirebaseStorage.getInstance();
        InsReport.storageRef = InsReport.storage.getReferenceFromUrl("gs://insreport-f39a3.appspot.com");

        ImageView imgMain = (ImageView) findViewById(R.id.main_img);
        /*
        imgMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgMainClicked++;
                if (imgMainClicked >= 3 && InsReport.mAuth.getCurrentUser() != null) {
                    Intent serverIntent = new Intent(thisActivity, ServerEmuActivity.class);
                    startActivity(serverIntent);
                }
            }
        });
        */

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });


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

        grantStoragePermission();
        grantCameraPermission();
        grantCallPermission();

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
                    pb.getHandler().post(new Runnable() {
                        @Override
                        public void run() {
                            pb.setVisibility(View.GONE);
                            for (Button formButton : formButtons) {
                                formButton.setEnabled(true);
                            }
                            checkNewForms(thisActivity);
                            checkIfNeededToFindByPhone();
                        }
                    });
                    Log.e(TAG, "run: ALL LOADED");
                }
            }
        }, 1000, 1000);

    }


    public void grantStoragePermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (this.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                //Log.v(TAG,"Permission is granted");
                return;
            } else {
                //Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            //Log.v(TAG,"Permission is granted");
            return;
        }
    }

    public void grantCameraPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (this.checkSelfPermission(Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED) {
                //Log.v(TAG,"Permission is granted");
                return;
            } else {
                //Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, 1);
                return;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            //Log.v(TAG,"Permission is granted");
            return;
        }
    }

    public void grantCallPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (this.checkSelfPermission(Manifest.permission.CALL_PHONE)
                    == PackageManager.PERMISSION_GRANTED) {
                //Log.v(TAG,"Permission is granted");
                return;
            } else {
                //Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CALL_PHONE}, 1);
                return;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            //Log.v(TAG,"Permission is granted");
            return;
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

        return super.onOptionsItemSelected(item);
    }

    public void login() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivityForResult(intent, 1);
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.e(TAG, "onResume: YES");

        if (InsReport.directories.loaded) {
            checkNewForms(this);
            checkIfNeededToFindByPhone();
        }
    }

    private void checkIfNeededToFindByPhone() {
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
                        openTheForm(form,this);
                        getIntent().removeExtra("findByPhone");
                        break;
                    }
                }
            }
        }
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

    public void checkNewForms(Activity context) {
        if (InsReport.formToBeAccepted != null &&
                InsReport.formToBeAccepted.status.equals("")) {
            acceptOrRejectDialogShow(InsReport.formToBeAccepted, context);
        }
    }

    public void openTheForm(Form form, Activity context) {
        Intent intent = new Intent(thisActivity, AdikStyleActivity.class);
        intent.putExtra(InsReport.EXTRA_FIREBASE_CATALOG, form.fireBaseCatalog);
        intent.putExtra(InsReport.EXTRA_ID_NO, form.id);
        startActivity(intent);
    }

    public void acceptOrRejectDialogShow(final Form form, final Activity context) {
        final Dialog acceptOrRejectDialog = new Dialog(context);
        acceptOrRejectDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        acceptOrRejectDialog.setContentView(context.getLayoutInflater().inflate(R.layout.accept_reject
                , null));

        String headerText = "";
        final String phoneNo;
        final String address;

        //TODO: scan the elements
        //Внимание! Если создать форму с нуля, а не с сервера, то данные не подтягиваются!
        //Надо: просканировать elements формы, найти эти данные и записать их в input!
        //Примерно так:
        //for (Element element : form.elements) {
        //    if (element.fireBaseFieldName.equals("dddd"))
        //        form.input.put("dddd",element.toString());
        //}

        if (form.input.get("CLIENT_NAME") != null)
            headerText += form.input.get("CLIENT_NAME") + "\n";
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

        ((Button) acceptOrRejectDialog.findViewById(R.id.buttonAccept)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if this is first time, save the time of accept/reject
                if (form.status.equals(""))
                    InsReport.ref.child("forms/" + form.fireBaseCatalog + "/" + InsReport.user.getUid() + "/" + form.id + "/dateAccepted").
                            setValue(ServerValue.TIMESTAMP);
                form.status = "accept";
                if (form.elements.size() == 0) {
                    FormTemplates.applyTemplate(form, form.fireBaseCatalog);
                    form.updateDescription();
                    form.validate();
                }
                form.saveToCloud();
                acceptOrRejectDialog.dismiss();
                openTheForm(form, context);
            }
        });

        ((Button) acceptOrRejectDialog.findViewById(R.id.buttonReject)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if this is first time, save the time of accept/reject
                if (form.status.equals(""))
                    InsReport.ref.child("forms/" + form.fireBaseCatalog + "/" + InsReport.user.getUid() + "/" + form.id + "/dateAccepted").
                            setValue(ServerValue.TIMESTAMP);
                form.status = "reject";
                form.saveToCloud();
                acceptOrRejectDialog.dismiss();
            }
        });

        ((Button) acceptOrRejectDialog.findViewById(R.id.buttonCall)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: make a call to phoneNo
                //здесь переменная phoneNo уже содержит телефон
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + phoneNo.trim()));
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
                acceptOrRejectDialog.dismiss();
            }
        });

        ((Button) acceptOrRejectDialog.findViewById(R.id.buttonMap)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: open map intent to address
                //здесь переменная address уже содержит адрес

                Intent intent = new Intent(Intent.ACTION_VIEW);

                String locationStr = address.replaceAll(" ", "+");

                Uri locationUri = Uri.parse("geo:0,0?").buildUpon()
                        .appendQueryParameter("q", locationStr)
                        .build();
                intent.setData(locationUri);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                    Toast.makeText(MainActivity.this, locationStr, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this,
                            "Нет карты", Toast.LENGTH_SHORT).show();
                }
                acceptOrRejectDialog.dismiss();

            }
        });


        acceptOrRejectDialog.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

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
        //TODO: Consider adding '".indexOn": "dateCreated"' at /forms/incident/5ARsewRkMeTL9kRLHVIkwnKbq812 to your security and Firebase rules for better performance

    }
}
