package com.gii.insreport;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Acer on 9/7/2016.
 */
public class KolesaAdapterMulti extends BaseAdapter {
    private static final String TAG = "KolesaAdapter";
    Context ctx;
    LayoutInflater lInflater;
    String filter = "";
    ArrayList<DirectoryItem> objects;
    ArrayList<DirectoryItem> filteredObjects = new ArrayList<>();
    public ArrayList<String> selectedIds = new ArrayList<>();

    KolesaAdapterMulti(Context context, ArrayList<DirectoryItem> directoryItems, EditText filterEditText, String currentValue) {
        ctx = context;
        objects = directoryItems;
        applyFilter();
        lInflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        selectedIds = new ArrayList<>(Arrays.asList(currentValue.split(";")));
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
                boolean alreadySelected = false;
                for (String selectedId : selectedIds) {
                    if (selectedId.equals(object.id))
                        alreadySelected = true;
                }
                if (match || alreadySelected)
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
            view = lInflater.inflate(R.layout.kolesa_multi_list_item, parent, false);
        }

        final DirectoryItem p = getProduct(position);
        CheckBox workCheckBox = (CheckBox) view.findViewById(R.id.checkbox1);
        workCheckBox.setText(p.name.trim());
        workCheckBox.setChecked(selectedIds.contains(p.id));
        workCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean b = ((CheckBox)view).isChecked();
                if (b) {
                    selectedIds.add(p.id);
                } else {
                    selectedIds.remove(p.id);
                }
            }
        });

        return view;
    }

    DirectoryItem getProduct(int position) {
        return ((DirectoryItem) getItem(position));
    }
}
