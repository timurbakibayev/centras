package com.gii.insreport;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class FormsList extends AppCompatActivity {

    String fireBaseCatalog = "";
    FormsCollection currentFormsCollection;
    public static FormsList formList;
    String selectionType = "";
    Dialog settingsDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forms_list);
        formList = this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fireBaseCatalog = getIntent().getStringExtra(InsReport.EXTRA_FIREBASE_CATALOG);

        FormsCollection formsCollection = null;
        for (FormsCollection formsCollection1 : InsReport.mainMenuForms)
            if (formsCollection1.fireBaseCatalog.equals(fireBaseCatalog))
                formsCollection = formsCollection1;

        setTitle(formsCollection.description);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fireBaseCatalog.equalsIgnoreCase("preInsurance")) {
                    showSelectionDialog();
                } else {
                    addnewForm();
                }
            }
        });

        ((Button) findViewById(R.id.hiButton)).setText(fireBaseCatalog);

        for (FormsCollection formCollections : InsReport.mainMenuForms) {
            if (fireBaseCatalog.equals(formCollections.fireBaseCatalog)) {
                FormsListAdapter formsListAdapter = new FormsListAdapter(this, formCollections.forms);
                currentFormsCollection = formCollections;
                InsReport.currentListView = ((ListView) findViewById(R.id.listView));
                InsReport.currentListView.setAdapter(formsListAdapter);
            }
        }

        final FormsList thisActivity = this;
        ((ListView) findViewById(R.id.listView)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                InsReport.currentFormNo = position;
                Intent intent = new Intent(thisActivity, FillFormActivity.class);
                intent.putExtra(InsReport.EXTRA_FIREBASE_CATALOG, fireBaseCatalog);
                intent.putExtra(InsReport.EXTRA_ID_NO, currentFormsCollection.forms.get(position).id);
                startActivity(intent);
            }
        });
    }

    public void dismissListener(View view) {
        switch (view.getId()) {
            case R.id.button1:
                selectionType = "Bike";
                settingsDialog.dismiss();
                addnewForm();
                break;
            case R.id.button2:
                selectionType = "Bus";
                settingsDialog.dismiss();
                addnewForm();
                break;
            case R.id.button3:
                selectionType = "Truck";
                settingsDialog.dismiss();
                addnewForm();
                break;
            case R.id.button4:
                selectionType = "Car";
                settingsDialog.dismiss();
                addnewForm();
                break;
        }
        Toast.makeText(FormsList.this, String.valueOf(view.getId()), Toast.LENGTH_SHORT).show();
    }

    private void addnewForm() {
        //fireBaseCatalog is accessible. Now we need to prepare the corresponding template
        Form newForm = new Form();
        newForm.generateNewId();
        FormTemplates.selectionTypes = selectionType;
        FormTemplates.applyTemplate(newForm, fireBaseCatalog);

        currentFormsCollection.forms.add(newForm);
        ((FormsListAdapter) ((ListView) findViewById(R.id.listView)).getAdapter()).notifyDataSetChanged();
        InsReport.currentFormNo = currentFormsCollection.forms.size() - 1;
        Intent intent = new Intent(this, FillFormActivity.class);
        intent.putExtra(InsReport.EXTRA_FIREBASE_CATALOG, fireBaseCatalog);
        intent.putExtra(InsReport.EXTRA_ID_NO, newForm.id);
        intent.putExtra(InsReport.INCIDENT_TYPE, selectionType);
        startActivity(intent);
    }

    public void snackBar(String message) {
        Snackbar.make(((FloatingActionButton) findViewById(R.id.fab)), message, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((FormsListAdapter) ((ListView) findViewById(R.id.listView)).getAdapter()).notifyDataSetChanged();
        //clear memory
    }

    public void showSelectionDialog() {
        settingsDialog = new Dialog(FormsList.this);
        settingsDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        settingsDialog.setContentView(getLayoutInflater().inflate(R.layout.report_selection
                , null));
        settingsDialog.show();
    }

}
