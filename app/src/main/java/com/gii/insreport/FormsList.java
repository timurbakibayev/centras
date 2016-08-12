package com.gii.insreport;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

public class FormsList extends AppCompatActivity {

    String fireBaseCatalog = "";
    FormsCollection currentFormsCollection;
    public static FormsList formList;

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
                addnewForm();
            }
        });

        ((Button)findViewById(R.id.hiButton)).setText(fireBaseCatalog);

        for (FormsCollection formCollections : InsReport.mainMenuForms) {
            if (fireBaseCatalog.equals(formCollections.fireBaseCatalog)) {
                FormsListAdapter formsListAdapter = new FormsListAdapter(this, formCollections.forms);
                currentFormsCollection = formCollections;
                InsReport.currentListView = ((ListView)findViewById(R.id.listView));
                InsReport.currentListView.setAdapter(formsListAdapter);
            }
        }

        final FormsList thisActivity = this;
        ((ListView)findViewById(R.id.listView)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
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

    private void addnewForm() {
        //fireBaseCatalog is accessible. Now we need to prepare the corresponding template
        Form newForm = new Form();
        newForm.generateNewId();
        FormTemplates.applyTemplate(newForm, fireBaseCatalog);
        currentFormsCollection.forms.add(newForm);
        ((FormsListAdapter)((ListView)findViewById(R.id.listView)).getAdapter()).notifyDataSetChanged();
        InsReport.currentFormNo = currentFormsCollection.forms.size() - 1;
        Intent intent = new Intent(this, FillFormActivity.class);
        intent.putExtra(InsReport.EXTRA_FIREBASE_CATALOG, fireBaseCatalog);
        intent.putExtra(InsReport.EXTRA_ID_NO, newForm.id);
        startActivity(intent);
    }

    public void snackBar(String message) {
        Snackbar.make(((FloatingActionButton)findViewById(R.id.fab)), message, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((FormsListAdapter)((ListView)findViewById(R.id.listView)).getAdapter()).notifyDataSetChanged();
        //clear memory
    }
}
