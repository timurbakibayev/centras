package com.gii.insreport;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ServerValue;
import com.firebase.client.ValueEventListener;
import com.firebase.ui.FirebaseListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ServerEmuActivity extends AppCompatActivity  {
    private static String TAG = "ServerEmuActivity.java";

    ArrayList <InputField> inputFields = new ArrayList<>();

    Spinner userSpinner;
    LinearLayout mainLL;
    ListView  fieldsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_emu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        userSpinner = (Spinner)findViewById(R.id.userSpinner);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String whois = userSpinner.getSelectedItem().toString();
                if (whois.equals(""))
                    Snackbar.make(view, "Выберите получателя", Snackbar.LENGTH_LONG).show();
                else {
                    sendTheForm(whois, view);
                }
            }
        });

        ArrayList<String> userEmails = new ArrayList<>();
        userEmails.add("");
        for (FirebaseUserEmail firebaseUserEmail : InsReport.firebaseUserEmails) {
            userEmails.add(firebaseUserEmail.email);
        }
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, userEmails);
        userSpinner.setAdapter(spinnerArrayAdapter);

        mainLL = (LinearLayout)findViewById(R.id.serverEmuLL);
        fieldsListView = (ListView)findViewById(R.id.serverListView);
        populateLinearLayout();

        ((Button)findViewById(R.id.serverButtonAddNewField)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewField();
            }
        });
        InsReport.ref.child("youfix/restTemplate").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                inputFields.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    InputField newInputField = snapshot.getValue(InputField.class);
                    newInputField.id = snapshot.getKey();
                    inputFields.add(newInputField);
                }
                Log.e("ServerEmuDataChange", "New items arrived!!!");
                for (InputField inputField : inputFields) {
                    Log.e("ServerEmuDataChange", "newItem " + inputField.id  + " : " + inputField.fieldName + ":" + inputField.fieldValue);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    FCMNotify fcmNotify = new FCMNotify();

    private void sendTheForm(String whois, View view) {
        for (FirebaseUserEmail firebaseUserEmail : InsReport.firebaseUserEmails) {
            if (firebaseUserEmail.email.equals(whois)) {
                Random rnd = new Random();
                String formId = "form"+rnd.nextInt(50000);
                Snackbar.make(view, "Sending " + formId + " to " + whois + "...", Snackbar.LENGTH_LONG)
                        .setAction("Show command", null).show();
                Firebase sendTo = InsReport.ref.child("forms/incident/"+firebaseUserEmail.id+"/"+formId);
                Map<String,String> input = new HashMap<>();
                Map<String,String> inputObjClient = new HashMap<>();
                Map<String,String> inputObj1 = new HashMap<>();
                inputObjClient.put("OBJECT_REGISRATION_NUMBER","A2-12-85-06");
                inputObjClient.put("PRODUCTION_YEAR","2015");
                inputObj1.put("OBJECT_REGISRATION_NUMBER","B2-12-85-06");
                String name = "";
                String phone = "";
                String address = "";
                for (InputField inputField : inputFields) {
                    input.put(inputField.fieldName, inputField.fieldValue);
                    if (inputField.fieldName.toUpperCase().contains("CLIENT_NAME"))
                        name = inputField.fieldValue;
                    if (inputField.fieldName.toUpperCase().contains("PHONE"))
                        phone = inputField.fieldValue;
                    if (inputField.fieldName.toUpperCase().contains("PLACE"))
                        address = inputField.fieldValue;
                }
                sendTo.child("input").setValue(input);
                sendTo.child("inputObjects/0").setValue(inputObjClient);
                sendTo.child("inputObjects/1").setValue(inputObj1);
                sendTo.child("dateCreated").setValue(ServerValue.TIMESTAMP);
                sendTo.child("id").setValue(formId);
                fcmNotify.trySendNotification(firebaseUserEmail.id,name,phone,address);
                //DONE! Now show the curl queries:
                LinearLayout linearLayout = new LinearLayout(this);
                linearLayout.setOrientation(LinearLayout.VERTICAL);
                TextView tvNotifyCaption = new TextView(this);
                tvNotifyCaption.setText("GCM Notification:");
                tvNotifyCaption.setTextColor(Color.RED);
                final TextView tvNotify = new TextView(this);
                tvNotify.setText(fcmNotify.tryGetNotificationString(firebaseUserEmail.id,name,phone,address));

                TextView tvDataCaption = new TextView(this);
                tvDataCaption.setText("Firebase new form:");
                tvDataCaption.setTextColor(Color.RED);
                final TextView tvData = new TextView(this);
                tvData.setText(
                        fcmNotify.tryGetFormDataString(firebaseUserEmail.id,formId,input)
                        );

                linearLayout.addView(tvNotifyCaption);
                linearLayout.addView(tvNotify);
                linearLayout.addView(tvDataCaption);
                linearLayout.addView(tvData);
                new AlertDialog.Builder(this).setTitle("Notification:").setView(linearLayout).
                        setPositiveButton("Копировать в Буфер", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                                ClipData clip = ClipData.newPlainText("Text Label", tvNotify.getText().toString() + "\n\n" + tvData.getText().toString());
                                clipboard.setPrimaryClip(clip);
                            }
                        }).
                        show();
            }
        }
    }

    private void addNewField() {
        InputField newInputField = new InputField();
        newInputField.fieldName = "FIELD_NAME";
        newInputField.fieldValue = "Значение";
        Firebase newPost = InsReport.ref.child("youfix/restTemplate").push();
        newPost.setValue(newInputField);
        newInputField.id = newPost.getKey();
        edit(newInputField);
        //adapter.notifyDataSetChanged();
    }


    FirebaseListAdapter adapter;
    private void populateLinearLayout() {
        adapter = new FirebaseListAdapter<InputField>(this, InputField.class,
                android.R.layout.two_line_list_item, InsReport.ref.child("youfix/restTemplate")) {
            @Override
            protected void populateView(View view, InputField inputField, int i) {
                ((TextView) view.findViewById(android.R.id.text1)).setText(inputField.fieldName);
                ((TextView) view.findViewById(android.R.id.text2)).setText(inputField.fieldValue);
            }
        };
        fieldsListView.setAdapter(adapter);
        fieldsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                InputField clicked = (InputField)adapter.getItem(i);
                Log.e(TAG, "onItemClick: " + clicked.fieldName);
                clicked.id = adapter.getRef(i).getKey();
                edit(clicked);
            }
        });
    }

    private void edit(final InputField clicked) {
        LinearLayout editLayout = new LinearLayout(this);
        final EditText fieldName = new EditText(this);
        final EditText fieldValue = new EditText(this);
        editLayout.addView(fieldName);
        editLayout.addView(fieldValue);
        fieldName.setText(clicked.fieldName);
        fieldValue.setText(clicked.fieldValue);
        new AlertDialog.Builder(this).setTitle("Редактировать").setView(editLayout).
                setPositiveButton("Сохранить", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        InsReport.ref.child("youfix/restTemplate/"+clicked.id+"/fieldName").setValue(fieldName.getText().toString());
                        InsReport.ref.child("youfix/restTemplate/"+clicked.id+"/fieldValue").setValue(fieldValue.getText().toString());
                    }
                }).setNegativeButton("Удалить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                InsReport.ref.child("youfix/restTemplate/"+clicked.id).setValue(null);
            }
        }).
                show();
    };
}
