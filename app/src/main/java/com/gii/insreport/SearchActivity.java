package com.gii.insreport;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        EditText filterEditText = (EditText)findViewById(R.id.filterEditText);
        final BaseAdapter baseAdapter = new SearchAdapter(this,FormsCollection.formIndices,filterEditText);
        ((ListView)findViewById(R.id.ListViewSearchByIndex)).setAdapter(baseAdapter);
        ((ListView)findViewById(R.id.ListViewSearchByIndex)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        loadAndRun(((FormIndex)baseAdapter.getItem(i)));
                    }
                });
                thread.start();
                Toast.makeText(SearchActivity.this, "Пожалуйста, подождите... Загрузка формы...", Toast.LENGTH_LONG).show();
            }
        });
    }
    public void loadAndRun(final FormIndex formIndex) {
        final String fireBaseCatalog = "incident";
        Query queryRef = InsReport.ref.child("forms/" + fireBaseCatalog + "/" + formIndex.userId + "/" + formIndex.formId);
        queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Toast.makeText(SearchActivity.this, "Форма загружена", Toast.LENGTH_SHORT).show();
                try {
                    Form form = dataSnapshot.getValue(Form.class);
                    InsReport.currentForm = form;
                    Intent intent = new Intent(SearchActivity.this, IncidentFormActivity.class);
                    intent.putExtra(InsReport.EXTRA_FIREBASE_CATALOG, fireBaseCatalog);
                    intent.putExtra(InsReport.EXTRA_ID_NO, formIndex.formId);
                    SearchActivity.this.startActivity(intent);

                } catch (Exception e) {
                    Toast.makeText(SearchActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Toast.makeText(SearchActivity.this, "Загрузка не удалась", Toast.LENGTH_LONG).show();
            }
        });
    }
}
