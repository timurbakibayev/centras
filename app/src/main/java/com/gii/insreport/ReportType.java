package com.gii.insreport;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
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

    //View.OnTouchListener mTouchLsitener;

    ReportType(Context context, ArrayList<String> sections,
               ArrayList<Integer> icons/*,View.OnTouchListener listener*/) {

//        animation.setDuration(1000);
//        animation.setInterpolator(new LinearInterpolator());
//        animation.setRepeatCount(Animation.INFINITE);
//        animation.setRepeatMode(Animation.REVERSE);

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

    ArrayList<String> firstTimes = new ArrayList<>();

    public boolean firstTime(String id) {
        if (!firstTimes.contains(id)) {
            firstTimes.add(id);
            return true;
        }
        return false;
    }

    final Animation animation = new AlphaAnimation(1, 0);

    /*
@Override
public int getViewTypeCount() {

    return getCount();
}

@Override
public int getItemViewType(int position) {

    return position;
}

*/
    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.listitem, parent, false);
        }

//        final Form p = getProduct(position);

//        if (view != convertView) {
//            view.setOnTouchListener(mTouchLsitener);
//        }

//        if (p.description.equals("")) {
//            if (p.input.get("CLIENT_NAME") != null)
//                p.description += p.input.get("CLIENT_NAME") + "\n";
//            if (p.input.get("CLAIMANT_PHONE_NO") != null) {
//                p.phoneNo = p.input.get("CLAIMANT_PHONE_NO");
//            }
//        }


//        (view.findViewById(R.id.openAcceptOrReject)).setVisibility(p.status.equals("") ? View.VISIBLE : View.GONE);
//        (view.findViewById(R.id.postponedForm)).setVisibility(p.status.equals("postpone") ? View.VISIBLE : View.GONE);
//        (view.findViewById(R.id.acceptedForm)).setVisibility(p.status.equals("accept") ? View.VISIBLE : View.GONE);
//        (view.findViewById(R.id.rejectedForm)).setVisibility(p.status.equals("reject") ? View.VISIBLE : View.GONE);
        ((TextView) view.findViewById(R.id.description)).setText(sections.get(position));
        ((ImageView) view.findViewById(R.id.image_section_icon)).setImageResource(icons.get(position));
//        ((TextView) view.findViewById(R.id.statusNoteTV)).setText(p.statusNote);
//        ((TextView) view.findViewById(R.id.textView1)).setText(p.description);
//        ((TextView) view.findViewById(R.id.textViewID)).setText(p.id);
//        (view.findViewById(R.id.checkReady)).setVisibility(p.formReady ? View.VISIBLE : View.INVISIBLE);
//        String photoInfo = "";
//        int photoCount = p.numberOfPhotos();
//        if (photoCount > 0)
//            photoInfo = "\n" + photoCount + " фото";
//
//        ((TextView) view.findViewById(R.id.textView2)).setText(p.toString() + photoInfo);
//
//        switch (p.status.toLowerCase()) {
//            case "accept":
//                ((ImageView) view.findViewById(R.id.openAcceptOrReject)).setColorFilter(Color.GREEN);
//                (view.findViewById(R.id.openAcceptOrReject)).setAnimation(null);
//                ((TextView) view.findViewById(R.id.textView2)).setTextColor(context.getResources()
//                        .getColor(R.color.colorSecondaryText));
//                ((TextView) view.findViewById(R.id.textView1)).setTextColor(context.getResources().getColor(R.color.colorSecondaryText));
//                break;
//            case "reject":
//                ((ImageView) view.findViewById(R.id.openAcceptOrReject)).setColorFilter(Color.RED);
//                (view.findViewById(R.id.openAcceptOrReject)).setAnimation(null);
//                ((TextView) view.findViewById(R.id.textView2)).setTextColor(Color.rgb(70, 70, 70));
//                ((TextView) view.findViewById(R.id.textView1)).setTextColor(Color.rgb(70, 70, 70));
//                break;
//            case "postpone":
//                ((ImageView) view.findViewById(R.id.openAcceptOrReject)).setColorFilter(Color.RED);
//                (view.findViewById(R.id.openAcceptOrReject)).setAnimation(null);
//                ((TextView) view.findViewById(R.id.textView2)).setTextColor(Color.rgb(170, 170, 170));
//                ((TextView) view.findViewById(R.id.textView1)).setTextColor(Color.rgb(170, 170, 170));
//                break;
//            default:
//                ((ImageView) view.findViewById(R.id.openAcceptOrReject)).setColorFilter(Color.BLACK);
//                (view.findViewById(R.id.openAcceptOrReject)).setAnimation(animation);
//                ((TextView) view.findViewById(R.id.textView2)).setTextColor(context.getResources().getColor(R.color.colorSecondaryText));
//                ((TextView) view.findViewById(R.id.textView1)).setTextColor(context.getResources().getColor(R.color.colorSecondaryText));
//                break;
//        }



//        view.findViewById(R.id.openAcceptOrReject).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                InsReport.mainActivity.acceptOrRejectDialogShow(p, FormsList.formList);
//                Log.w(TAG, "onClick: openAcceptOrReject");
//            }
//        });
//
//        view.findViewById(R.id.postponedForm).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                InsReport.mainActivity.acceptOrRejectDialogShow(p, FormsList.formList);
//                Log.w(TAG, "onClick: postponedForm");
//            }
//        });
//
//        view.findViewById(R.id.acceptedForm).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (!p.formReady)
//                    InsReport.mainActivity.acceptOrRejectDialogShow(p, FormsList.formList);
//                Log.w(TAG, "onClick: postponedForm");
//            }
//        });
//
//        view.findViewById(R.id.rejectedForm).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                InsReport.mainActivity.acceptOrRejectDialogShow(p, FormsList.formList);
//                Log.w(TAG, "onClick: postponedForm");
//            }
//        });
//
//        if (p.phoneNo.equals(""))
//            ((ImageView) view.findViewById(R.id.imageCallInListView)).setVisibility(View.GONE);
//        else {
//            final String _phoneNo = p.phoneNo;
//            ((ImageView) view.findViewById(R.id.imageCallInListView)).setVisibility(View.VISIBLE);
//            ((ImageView) view.findViewById(R.id.imageCallInListView)).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    //call _phoneNo
//                    if (Build.VERSION.SDK_INT < 23 ||
//                            ActivityCompat.checkSelfPermission(FormsList.formList, android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
//                        Log.e(TAG, "onClick: call " + _phoneNo.trim());
//                        String uri = "tel:" + _phoneNo.trim();
//                        InsReport.logFirebase("Make a call from list: " + p.fireBaseCatalog + " form no. " + p.id + ", TEL: " + _phoneNo);
//                        Intent intent = new Intent(Intent.ACTION_CALL);
//                        intent.setData(Uri.parse(uri));
//                        FormsList.formList.startActivity(intent);
//                        return;
//                    }
//                }
//            });
//        }


        return view;
    }

    Form getProduct(int position) {
        return ((Form) getItem(position));
    }
}




//        extends RecyclerView.Adapter<ReportType.ViewHolder> {
//    private Context context;
//    private ArrayList<String> reportType;
//    private ArrayList<Integer> images;
//    private Integer test;
//
//    public ReportType(Context context, ArrayList<String> reportType, ArrayList<Integer> images) {
//        this.context = context;
//        this.reportType = reportType;
//        this.images = images;
//    }
//
//
//    @Override
//    public ReportType.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.report_type,
//                parent, false);
//        return new ViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(ViewHolder holder, int position) {
//        holder.report_name.setText(reportType.get(position));
//        holder.report_icon.setImageResource(images.get(position));
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return reportType.size();
//    }
//
//    public class ViewHolder extends RecyclerView.ViewHolder {
//        TextView report_name;
//        ImageView report_icon;
//
//        public ViewHolder(View view) {
//            super(view);
//            report_name = (TextView) view.findViewById(R.id.report_name);
//            report_icon = (ImageView) view.findViewById(R.id.report_icon);
//        }
//    }
//}
