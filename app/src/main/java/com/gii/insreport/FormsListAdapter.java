package com.gii.insreport;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Timur_hnimdvi on 26-Jul-16.
 */
public class FormsListAdapter extends BaseAdapter {
    private static final String TAG = "FormListAdapter";
    Context ctx;
    LayoutInflater lInflater;
    ArrayList<Form> objects;

    FormsListAdapter(Context context, ArrayList<Form> forms) {
        ctx = context;
        objects = forms;
        lInflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.listitem, parent, false);
        }

        final Form p = getProduct(position);

        p.updateDescription();

        ((Switch) view.findViewById(R.id.complete_form)).setChecked(p.formReady);

        ((Switch) view.findViewById(R.id.complete_form)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((Switch) view).isChecked()) {
                    p.formReady = true;
                    p.saveToCloud();
                    Toast.makeText(view.getContext(), "Форма готова к отправке", Toast.LENGTH_SHORT).show();
                } else {
                    p.formReady = false;
                    p.saveToCloud();
                    Toast.makeText(view.getContext(), "Отмена готовности", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ((TextView) view.findViewById(R.id.textView1)).setText(p.description);
        String photoInfo = "";
        int photoCount = p.numberOfPhotos();
        if (photoCount > 0)
            photoInfo = "\n" + photoCount + " фото";

        ((TextView) view.findViewById(R.id.textView2)).setText(p.toString() + photoInfo);

        ((ProgressBar) view.findViewById(R.id.progressBar)).setProgress(p.filledPercent());

        if (!p.signed())
            ((ImageView) view.findViewById(R.id.imageInListView)).setVisibility(View.GONE);
        else
            ((ImageView) view.findViewById(R.id.imageInListView)).setVisibility(View.VISIBLE);

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
                            ActivityCompat.checkSelfPermission(FormsList.formList, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        Log.e(TAG, "onClick: call " + _phoneNo.trim());
                        String uri = "tel:" + _phoneNo.trim();
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