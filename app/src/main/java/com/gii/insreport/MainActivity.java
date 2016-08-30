package com.gii.insreport;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private static String TAG = "MainActivity.java";

    Timer timer = new Timer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        InsReport.storage = FirebaseStorage.getInstance();
        InsReport.storageRef = InsReport.storage.getReferenceFromUrl("gs://insreport-f39a3.appspot.com");

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
        refreshDescriptions();

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
                if (allLoaded) {
                    timer.cancel();
                    final ProgressBar pb = (ProgressBar)findViewById(R.id.roundProgressbar);
                    pb.getHandler().post(new Runnable() {
                        @Override
                        public void run() {
                            pb.setVisibility(View.GONE);
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
                return ;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            //Log.v(TAG,"Permission is granted");
            return ;
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
                return ;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            //Log.v(TAG,"Permission is granted");
            return ;
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
                return ;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            //Log.v(TAG,"Permission is granted");
            return ;
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

        refreshDescriptions();
    }


    ArrayList<Button> formButtons = new ArrayList<>();
    Button loginButton = null;
    private void addForms() {
        LinearLayout mainMenuLL = (LinearLayout)findViewById(R.id.mainMenuLL);
        mainMenuLL.removeAllViews();
        final MainActivity thisActivity = this;
        for (final FormsCollection formsCollection : InsReport.mainMenuForms) {
            Button newMenuButton = new Button(this);
            formButtons.add(newMenuButton);
            newMenuButton.setText(formsCollection.description);
            final float scale = getResources().getDisplayMetrics().density;
            newMenuButton.setHeight((int)(96 * scale + 0.5f));
            newMenuButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(thisActivity, FormsList.class);
                    String message = formsCollection.fireBaseCatalog;
                    intent.putExtra(InsReport.EXTRA_FIREBASE_CATALOG, message);
                    startActivity(intent);
                }
            });
            mainMenuLL.addView(newMenuButton);
        }

        Button newMenuButton = new Button(this);
        formButtons.add(newMenuButton);
        newMenuButton.setText("АВТОРИЗАЦИЯ");
        final float scale = getResources().getDisplayMetrics().density;
        newMenuButton.setHeight((int)(96 * scale + 0.5f));
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

    private void refreshDescriptions() {
        //((Button)findViewById(R.id.button_add_plan)).setText("План повреждений\n\n" + InsReport.damagePlanData.damageDescription);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        refreshUser();

    }

    public  void refreshUser() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        FirebaseUser user = InsReport.mAuth.getCurrentUser();
        if (user != null) {
            toolbar.setSubtitle("Online: " + user.getEmail());
            InsReport.userID = user.getEmail();
            for (Button formButton : formButtons) {
                formButton.setVisibility(View.VISIBLE);
            }
            loginButton.setVisibility(View.GONE);
        }
        else {
            toolbar.setSubtitle("Offline");
            InsReport.userID = "Incognito";
            for (Button formButton : formButtons) {
                formButton.setVisibility(View.GONE);
            }
            loginButton.setVisibility(View.VISIBLE);
        }

    }
}
