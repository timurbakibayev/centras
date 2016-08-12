package com.gii.insreport;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

public class FreeDrawActivity extends AppCompatActivity {

    public static FreeDrawView freeDrawView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_free_draw);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveAndReturn();
            }
        });

        ((FloatingActionButton)findViewById(R.id.undoFab)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                undo();
            }
        });

        ((FloatingActionButton)findViewById(R.id.deleteFab)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                killAll();
            }
        });

        if (freeDrawView == null) {
            freeDrawView = new FreeDrawView(this);
        }

        freeDrawView.bindActivity(this);
        freeDrawView.loadResources(this);

        ViewGroup parent = (ViewGroup)freeDrawView.getParent();
        if (parent != null) {
            parent.removeAllViews();
        }

        ((RelativeLayout)findViewById(R.id.freeDrawViewSubstitute)).addView(freeDrawView);
    }

    private void killAll() {
        InsReport.currentElement.vDraw.clear();
        freeDrawView.postInvalidate();
    }

    private void undo() {
        if (InsReport.currentElement.vDraw.size() > 0) {
            InsReport.currentElement.vDraw.remove(InsReport.currentElement.vDraw.size() - 1);
            freeDrawView.postInvalidate();
        }
    }

    @Override
    public void onBackPressed() {
        saveAndReturn();
    }

    private void saveAndReturn() {
        //TODO: generate bitmap
        finish();
    }
}
