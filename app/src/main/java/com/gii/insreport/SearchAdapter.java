package com.gii.insreport;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Acer on 9/7/2016.
 */
public class SearchAdapter extends BaseAdapter {
    private static final String TAG = "KolesaAdapter";
    Context ctx;
    LayoutInflater lInflater;
    String filter = "";
    ArrayList<FormIndex> objects;
    ArrayList<FormIndex> filteredObjects = new ArrayList<>();

    SearchAdapter(Context context, ArrayList<FormIndex> formIndices, EditText filterEditText) {
        ctx = context;
        objects = formIndices;
        applyFilter();
        lInflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        filterEditText.setHint("Поиск");
        filterEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                filter = charSequence.toString();
                applyFilter();
                notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void applyFilter() {
        filteredObjects.clear();
        String[] filterSubs = filter.split(" ");
        for (int i = 0; i < filterSubs.length; i++) {
            filterSubs[i] = filterSubs[i].toUpperCase();
        }
        for (FormIndex object : objects) {
            if (filter.equals(""))
                filteredObjects.add(object);
            else {
                boolean match = true;
                for (String filterSub : filterSubs) {
                    if ((!object.content.toUpperCase().contains(filterSub)) &&
                            (!object.userName.toUpperCase().contains(filterSub)) &&
                            (!object.userEmail.toUpperCase().contains(filterSub))
                            )
                        match = false;
                }
                if (match)
                    filteredObjects.add(object);
            }
        }
    }

    @Override
    public int getCount() {
        return filteredObjects.size();
    }

    @Override
    public Object getItem(int position) {
        return filteredObjects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    ArrayList<String> firstTimes = new ArrayList<>();

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.listitem_search, parent, false);
        }

        final FormIndex p = getProduct(position);
        ((TextView) view.findViewById(R.id.textView1)).setText(IncidentFormActivity.dateOnlyText(p.dateCreated));
        ((TextView) view.findViewById(R.id.textView2)).setText(p.content.trim());
        ((TextView) view.findViewById(R.id.statusNoteTV)).setText(p.userEmail.trim() + " " + p.userName.trim());
        return view;
    }

    FormIndex getProduct(int position) {
        return ((FormIndex) getItem(position));
    }
}
