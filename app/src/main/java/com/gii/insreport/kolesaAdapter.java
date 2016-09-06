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
 * Created by Timur_hnimdvi on 26-Jul-16.
 */
public class KolesaAdapter extends BaseAdapter {
    private static final String TAG = "KolesaAdapter";
    Context ctx;
    LayoutInflater lInflater;
    String filter = "";
    ArrayList<DirectoryItem> objects;
    ArrayList<DirectoryItem> filteredObjects = new ArrayList<>();

    KolesaAdapter(Context context, ArrayList<DirectoryItem> directoryItems, EditText filterEditText) {
        ctx = context;
        objects = directoryItems;
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
        for (DirectoryItem object : objects) {
            if (filter.equals(""))
                filteredObjects.add(object);
            else {
                boolean match = true;
                for (String filterSub : filterSubs) {
                    if (!object.name.toUpperCase().contains(filterSub))
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
            view = lInflater.inflate(R.layout.kolesa_list_item, parent, false);
        }

        final DirectoryItem p = getProduct(position);
        ((TextView) view.findViewById(R.id.textView1)).setText(p.name.trim());
        return view;
    }

    DirectoryItem getProduct(int position) {
        return ((DirectoryItem) getItem(position));
    }
}