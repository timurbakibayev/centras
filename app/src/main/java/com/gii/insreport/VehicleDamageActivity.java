package com.gii.insreport;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

public class VehicleDamageActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    public static VehicleDamageView vehicleDamageView;
    Spinner spinner;



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

        spinner = new Spinner(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.cars_array, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(this);
//
        if (vehicleDamageView == null) {
            vehicleDamageView = new VehicleDamageView(this);
        }

        vehicleDamageView.bindActivity(this);
        vehicleDamageView.loadResources(this);

        ViewGroup parent = (ViewGroup)vehicleDamageView.getParent();
        if (parent != null) {
            parent.removeAllViews();
        }

        ((RelativeLayout)findViewById(R.id.vehicleViewSubstitute)).addView(vehicleDamageView);
        ((RelativeLayout)findViewById(R.id.vehicleViewSubstitute)).addView(spinner);


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
                        finish();
                    }
                }).setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        }).show();

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Toast.makeText(VehicleDamageActivity.this, adapterView.getItemAtPosition(i).toString(),
                Toast.LENGTH_SHORT).show();
        String type = adapterView.getItemAtPosition(i).toString();
        if(type.equalsIgnoreCase("Автобус")){
            VehicleDamageView.carType = "Bus";
            vehicleDamageView.loadResources(this);
            ViewGroup parent = (ViewGroup)vehicleDamageView.getParent();
            if (parent != null) {
                parent.removeAllViews();
            }
            ((RelativeLayout)findViewById(R.id.vehicleViewSubstitute)).addView(vehicleDamageView);
            ((RelativeLayout)findViewById(R.id.vehicleViewSubstitute)).addView(spinner);
        }else if(type.equalsIgnoreCase("Грузовик")){
            VehicleDamageView.carType = "Truck";
            vehicleDamageView.loadResources(this);
            ViewGroup parent = (ViewGroup)vehicleDamageView.getParent();
            if (parent != null) {
                parent.removeAllViews();
            }
            ((RelativeLayout)findViewById(R.id.vehicleViewSubstitute)).addView(vehicleDamageView);
            ((RelativeLayout)findViewById(R.id.vehicleViewSubstitute)).addView(spinner);
        }else if(type.equalsIgnoreCase("Мотоцикл")){
            VehicleDamageView.carType = "Bike";
            vehicleDamageView.loadResources(this);
            ViewGroup parent = (ViewGroup)vehicleDamageView.getParent();
            if (parent != null) {
                parent.removeAllViews();
            }
            ((RelativeLayout)findViewById(R.id.vehicleViewSubstitute)).addView(vehicleDamageView);
            ((RelativeLayout)findViewById(R.id.vehicleViewSubstitute)).addView(spinner);
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        Toast.makeText(this, "Does it work ?", Toast.LENGTH_SHORT).show();

    }

    //TODO: add "clear" and "undo" buttons
}
