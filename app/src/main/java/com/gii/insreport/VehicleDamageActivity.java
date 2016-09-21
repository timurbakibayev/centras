package com.gii.insreport;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;

public class VehicleDamageActivity extends AppCompatActivity {

    public static VehicleDamageView vehicleDamageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_damage_plan);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
                confirmDamageTextAndFinish();

            }
        });

//
        if (vehicleDamageView == null) {
            vehicleDamageView = new VehicleDamageView(this);
        }

        vehicleDamageView.bindActivity(this);

        if (toolbar.findViewById(R.id.damageToolbarSpinner) != null) {
            Log.e("VehicleDamageActivity", "onCreate: GOT YA!");
            vehicleDamageView.spinner = (Spinner)toolbar.findViewById(R.id.damageToolbarSpinner);
        }
        vehicleDamageView.loadResources(this);

        ViewGroup parent = (ViewGroup)vehicleDamageView.getParent();
        if (parent != null) {
            parent.removeAllViews();
        }

        ((RelativeLayout)findViewById(R.id.vehicleViewSubstitute)).addView(vehicleDamageView);
    }

    private void confirmDamageTextAndFinish() {
        final EditText newDescription = new EditText(this);

        //newDescription.setHint("");
        newDescription.setSingleLine(true);
        String offerTheTextFromDamages = "";

        int i  = 0;
        for (DamageMark damageMark : InsReport.damagePlanData.damageMarks) {
            String textToAdd = damageMark.description;
            if (i == 0)
                textToAdd = textToAdd.substring(0,1).toUpperCase() + textToAdd.substring(1).toLowerCase();
            else
                textToAdd = textToAdd.toLowerCase();
            offerTheTextFromDamages += textToAdd;
            if (i != InsReport.damagePlanData.damageMarks.size() - 1)
                offerTheTextFromDamages += ", ";
            i++;
        }

        newDescription.setText(offerTheTextFromDamages);

        final LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        layout.addView(newDescription);

        new android.app.AlertDialog.Builder(this)
                .setTitle("Описание всех повреждений")
                //.setMessage("Введите имя файла для сохранения")
                .setView(layout)
                .setPositiveButton("Сохранить", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        InsReport.damagePlanData.damageDescription = newDescription.getText().toString();
                        vehicleDamageView.takeSnapshot();
                        finish();
                    }
                }).setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        }).show();
    }

    //TODO: add "clear" and "undo" buttons
}
