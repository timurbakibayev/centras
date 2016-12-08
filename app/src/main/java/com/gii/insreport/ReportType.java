package com.gii.insreport;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by adik on 12/6/16.
 */

public class ReportType extends BaseAdapter {
    private static final String TAG = ReportType.class.getSimpleName();
    Context context;
    LayoutInflater lInflater;
    ArrayList<String> sections;
    ArrayList<Integer> icons;
    ArrayList<Boolean> availability;

    ReportType(Context context, ArrayList<String> sections,
               ArrayList<Integer> icons, ArrayList<Boolean> availability) {
        this.availability = availability;
        this.context = context;
        this.sections = sections;
        this.icons = icons;
        lInflater = (LayoutInflater) this.context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //mTouchLsitener = listener;
    }

    @Override
    public int getCount() {
        return sections.size();
    }

    @Override
    public Object getItem(int position) {
        return sections.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.listitem, parent, false);
        }
        if (availability.get(position))
            ((ImageView) view.findViewById(R.id.arrow)).setColorFilter(ContextCompat.getColor(context, R.color.colorPrimaryDark));
        ((TextView) view.findViewById(R.id.description)).setText(sections.get(position));
        ((ImageView) view.findViewById(R.id.image_section_icon)).setImageResource(icons.get(position));

        return view;
    }

}


