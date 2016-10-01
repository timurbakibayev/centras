package com.gii.insreport;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Timur_hnimdvi on 26-Jul-16.
 */
public class FormsListAdapter extends BaseAdapter {
    private static final String TAG = "FormListAdapter";
    Context ctx;
    LayoutInflater lInflater;
    ArrayList<Form> objects;

    View.OnTouchListener mTouchLsitener;
    FormsListAdapter(Context context, ArrayList<Form> forms, View.OnTouchListener listener) {

        animation.setDuration(1000);
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatCount(Animation.INFINITE);
        animation.setRepeatMode(Animation.REVERSE);

        ctx = context;
        objects = forms;
        lInflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mTouchLsitener = listener;
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Object getItem(int position) {
        return objects.get(position);
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
            view = lInflater.inflate(R.layout.listitem2, parent, false);
        }

        if(view != convertView){
            view.setOnTouchListener(mTouchLsitener);
        }

        final Form p = getProduct(position);

        //p.updateDescription();

//        ((Switch) view.findViewById(R.id.complete_form)).setChecked(p.formReady);
//
//        ((Switch) view.findViewById(R.id.complete_form)).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (((Switch) view).isChecked()) {
//                    p.formReady = true;
//                    p.saveToCloud();
//                    Toast.makeText(view.getContext(), "Форма готова к отправке", Toast.LENGTH_SHORT).show();
//                } else {
//                    p.formReady = false;
//                    p.saveToCloud();
//                    Toast.makeText(view.getContext(), "Отмена готовности", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });

        if (p.description.equals("")) {
            if (p.input.get("CLIENT_NAME") != null)
                p.description += p.input.get("CLIENT_NAME") + "\n";
            if (p.input.get("CLAIMANT_PHONE_NO") != null) {
                p.phoneNo = p.input.get("CLAIMANT_PHONE_NO");
            }
        }

        ((TextView) view.findViewById(R.id.textView1)).setText(p.description);
        ((TextView) view.findViewById(R.id.textViewID)).setText(p.id);
        (view.findViewById(R.id.checkReady)).setVisibility(p.formReady?View.VISIBLE:View.INVISIBLE);
        String photoInfo = "";
        int photoCount = p.numberOfPhotos();
        if (photoCount > 0)
            photoInfo = "\n" + photoCount + " фото";

        ((TextView) view.findViewById(R.id.textView2)).setText(p.toString() + photoInfo);

        ((ProgressBar) view.findViewById(R.id.progressBar)).setProgress(p.filledPercent());

        if (!p.signed())
            (view.findViewById(R.id.imageInListView)).setVisibility(View.GONE);
        else
            (view.findViewById(R.id.imageInListView)).setVisibility(View.VISIBLE);

        int color = Color.BLACK;


        switch (p.status.toLowerCase()) {
            case "accept":
                ((ImageView)view.findViewById(R.id.openAcceptOrReject)).setColorFilter(Color.GREEN);
                (view.findViewById(R.id.openAcceptOrReject)).setAnimation(null);
                ((TextView) view.findViewById(R.id.textView2)).setTextColor(Color.BLACK);
                ((TextView) view.findViewById(R.id.textView1)).setTextColor(Color.BLACK);
                break;
            case "reject":
                ((ImageView)view.findViewById(R.id.openAcceptOrReject)).setColorFilter(Color.RED);
                (view.findViewById(R.id.openAcceptOrReject)).setAnimation(null);
                ((TextView) view.findViewById(R.id.textView2)).setTextColor(Color.rgb(70, 70, 70));
                ((TextView) view.findViewById(R.id.textView1)).setTextColor(Color.rgb(70, 70, 70));
                break;
            default:
                ((ImageView)view.findViewById(R.id.openAcceptOrReject)).setColorFilter(Color.BLACK);
                (view.findViewById(R.id.openAcceptOrReject)).setAnimation(animation);
                ((TextView) view.findViewById(R.id.textView2)).setTextColor(Color.BLACK);
                ((TextView) view.findViewById(R.id.textView1)).setTextColor(Color.BLACK);
                break;
        }

        view.findViewById(R.id.openAcceptOrReject).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!p.formReady)
                    InsReport.mainActivity.acceptOrRejectDialogShow(p,FormsList.formList);
            }
        });

        if (p.phoneNo.equals(""))
            ((ImageView) view.findViewById(R.id.imageCallInListView)).setVisibility(View.GONE);
        else {
            final String _phoneNo = p.phoneNo;
            ((ImageView) view.findViewById(R.id.imageCallInListView)).setVisibility(View.VISIBLE);
            ((ImageView) view.findViewById(R.id.imageCallInListView)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //call _phoneNo
                    if (Build.VERSION.SDK_INT < 23 ||
                            ActivityCompat.checkSelfPermission(FormsList.formList, android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                        Log.e(TAG, "onClick: call " + _phoneNo.trim());
                        String uri = "tel:" + _phoneNo.trim();
                        InsReport.logFirebase("Make a call from list: " + p.fireBaseCatalog + " form no. " + p.id + ", TEL: " + _phoneNo);
                        Intent intent = new Intent(Intent.ACTION_CALL);
                        intent.setData(Uri.parse(uri));
                        FormsList.formList.startActivity(intent);
                        return;
                    }
                }
            });
        }



        //We fail to allocate enough memory for this :(
        //We should substitute it with some drawing or ticks/x/minus/etc...
        /*
        final ImageView imageView = (ImageView)view.findViewById(R.id.imageInListView);
        for (Element element : p.elements) {
            if (element.type == Element.ElementType.ePhoto &&
                    element.vPhotos.size() > 0
                    ) {
                if (firstTime(p.id))
                InsReport.ref.child("images/" + element.vPhotos.get(0)).
                        addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot snapshot) {
                                String gotData = snapshot.getValue(String.class);
                                if (gotData != null) {
                                    final Bitmap thePicture = CameraAndPictures.decodeBase64(gotData);
                                    imageView.setImageBitmap(thePicture);
                                    imageView.setMaxWidth(150);
                                    imageView.setAdjustViewBounds(true);
                                }
                            }
                            @Override
                            public void onCancelled(FirebaseError firebaseError) {
                                Log.e("Firebase", "The read failed 5: " + firebaseError.getMessage());
                            }
                        });
                Log.e(TAG, "getView: signed up for " + "images/" + element.vPhotos.get(0) );
                break;
            }
        }
        */

        return view;
    }

    Form getProduct(int position) {
        return ((Form) getItem(position));
    }
}