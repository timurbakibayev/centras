package com.gii.insreport;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.support.design.widget.TextInputLayout;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Calendar;

public class CameraAndPictures {
    public static String TAG = "CameraAndPictures";
    public static Bitmap bitmap;
    public static String generateNewId() {
        SecureRandom random = new SecureRandom();
        return  (new BigInteger(64, random).toString(32));
    }

    public static String savePictureToFirebase(String filename) {
        File file = new File(filename);
        //Uri fileUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory()+File.separator + "image.jpg"));
        bitmap = decodeSampledBitmapFromFile(file.getAbsolutePath(), 700, 700); //i guess, 640x480 would be enough 700 700
        String id = generateNewId();
        //uncomment this to upload the file to the firebase storage as well.
        //does not work offline :(
        //UploadTask uploadTask = InsReport.storageRef.child("images/" + id + ".jpg").putFile(fileUri);
        File fileTo = new File(Environment.getExternalStorageDirectory()+File.separator+"insurance");
        fileTo.mkdirs();
        Calendar calendar = Calendar.getInstance();
        fileTo = new File(Environment.getExternalStorageDirectory()+File.separator+"insurance"+File.separator+
                calendar.get(Calendar.YEAR)+"_"+(calendar.get(Calendar.MONTH)+1)+"_"+calendar.get(Calendar.DAY_OF_MONTH)+"_"
                +id+".jpg");
        try {
            FileOutputStream out = new FileOutputStream(fileTo);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        InsReport.ref.child("images/" + id).setValue(encodeToBase64(bitmap,Bitmap.CompressFormat.JPEG,70));
        InsReport.logFirebase("New picture saved: " + id);
        return id;
    }

    public void getPicFromFirebase(final Element element, final Form form, final LinearLayout linearLayout,
                                   final ArrayList<String> comboItems) {

        InsReport.ref.child("images/" + element.vText).
                addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        String gotData = snapshot.getValue(String.class);
                        if (gotData != null) {
                            final Bitmap thePicture = CameraAndPictures.decodeBase64(gotData);
                            InsReport.bitmapsNeedToBeRecycled.add(thePicture);
                            final ImageView theImageView = new ImageView(linearLayout.getContext());
                            theImageView.setImageBitmap(thePicture);
                            //final CameraAndPictures thisClass = this;
                            /*if (linearLayout.getOrientation() == LinearLayout.HORIZONTAL)
                                theImageView.setMinimumHeight(linearLayout.getHeight());
                            else
                                theImageView.setMinimumWidth(linearLayout.getWidth());*/
                            theImageView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    showZoomed(linearLayout, thePicture);
                                }
                            });
                            theImageView.setAdjustViewBounds(true);
                            TextView descriptionTV = descriptionTextView(element,form,theImageView,linearLayout.getContext(),comboItems);
                            if (!form.formReady)
                                linearLayout.addView(deleteTextView(element,form,theImageView,linearLayout.getContext(),descriptionTV));
                            linearLayout.addView(theImageView);
                            linearLayout.addView(descriptionTV);

                        }
                    }
                    @Override
                    public void onCancelled(FirebaseError firebaseError) {
                        Log.e("Firebase", "The read failed 4: " + firebaseError.getMessage());
                    }
                });
    }

    public TextView descriptionTextView(final Element element, final Form form, final ImageView imageView, final Context context,
                                        final ArrayList<String> comboItems) {
        final TextView descriptionAndDate = new TextView(context);
        descriptionAndDate.setTextColor(Color.BLACK);
        if (element.description.equals(""))
            element.description = "Без описания";
        Drawable d = imageView.getDrawable();
        int origW = d.getIntrinsicWidth();
        int origH = d.getIntrinsicHeight();
        String size = "Размер: " + origW + " x " + origH;
        descriptionAndDate.setText(element.description + "\n" + IncidentFormActivity.dateTimeText(element.vDate) + "\n" + size + "\n\n");
        if (!form.formReady)
            descriptionAndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (element.deleted) {
                    imageView.setVisibility(View.VISIBLE);
                    descriptionAndDate.setTextColor(Color.BLACK);
                    descriptionAndDate.setText(element.description + "\n" + IncidentFormActivity.dateTimeText(element.vDate) + "\n\n");
                    element.deleted = false;
                    form.saveToCloud();
                    return;
                }
                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                final EditText editText = new EditText(context);
                //ImageView speachButton = new ImageView(thisActivity);
                final String prevValue = element.description;
                editText.setText(element.description);
                TextInputLayout fieldHint = new TextInputLayout(context);
                fieldHint.setHint("Описание фотографии");
                fieldHint.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT, 1));
                fieldHint.addView(editText);
                final ScrollView photoDescriptionScrollView = new ScrollView(context);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                lp.setMargins(100,30,100,30);
                photoDescriptionScrollView.setLayoutParams(lp);
                photoDescriptionScrollView.addView(fieldHint);
                LinearLayout linearLayout = new LinearLayout(context);
                linearLayout.setOrientation(LinearLayout.VERTICAL);
                linearLayout.addView(photoDescriptionScrollView);
                for (final String comboItem : comboItems) {
                    if (!comboItem.equals("")) {
                        Button comboButton = new Button(context);
                        comboButton.setText(comboItem);
                        comboButton.setBackgroundColor(Color.TRANSPARENT);
                        comboButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                editText.setText(comboItem);
                            }
                        });
                        linearLayout.addView(comboButton);
                    }
                }
                builder
                        .setView(linearLayout)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                element.description = editText.getText().toString();
                                form.saveToCloud();
                                descriptionAndDate.setText(element.description + "\n" + IncidentFormActivity.dateTimeText(element.vDate) + "\n\n");
                            }
                        }).setNegativeButton("Удалить", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                            element.deleted = true;
                            descriptionAndDate.setText("Удалено. Нажмите для воостановления. \n" + element.description + "\n" + IncidentFormActivity.dateTimeText(element.vDate) + "\n\n");
                            descriptionAndDate.setTextColor(Color.RED);
                            imageView.setVisibility(View.GONE);
                            form.saveToCloud();
                    }
                })
                        .show();
            }
        });
        return descriptionAndDate;
    }

    public TextView deleteTextView(final Element element, final Form form, final ImageView imageView, final Context context, final TextView descriptionAndDate) {
        final TextView deleteTV = new TextView(context);
        deleteTV.setTextColor(Color.RED);
        deleteTV.setText("Удалить");
        deleteTV.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
        deleteTV.setVisibility(element.deleted?View.GONE:View.VISIBLE);
        deleteTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                element.deleted = true;
                descriptionAndDate.setText("Удалено. Нажмите для воостановления. \n" + element.description + "\n" + IncidentFormActivity.dateTimeText(element.vDate) + "\n\n");
                descriptionAndDate.setTextColor(Color.RED);
                imageView.setVisibility(View.GONE);
                form.saveToCloud();
            }
        });
        return deleteTV;
    }


    public void getPicFromFirebase(String photoID, final LinearLayout linearLayout) {

        InsReport.ref.child("images/" + photoID).
                addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        String gotData = snapshot.getValue(String.class);
                        if (gotData != null) {
                            final Bitmap thePicture = CameraAndPictures.decodeBase64(gotData);
                            InsReport.bitmapsNeedToBeRecycled.add(thePicture);
                            final ImageView theImageView = new ImageView(linearLayout.getContext());
                            theImageView.setImageBitmap(thePicture);
                            //final CameraAndPictures thisClass = this;
                            /*if (linearLayout.getOrientation() == LinearLayout.HORIZONTAL)
                                theImageView.setMinimumHeight(linearLayout.getHeight());
                            else
                                theImageView.setMinimumWidth(linearLayout.getWidth());*/
                            theImageView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    showZoomed(linearLayout, thePicture);
                                }
                            });
                            theImageView.setAdjustViewBounds(true);
                            linearLayout.addView(theImageView);
                        }
                    }
                    @Override
                    public void onCancelled(FirebaseError firebaseError) {
                        Log.e("Firebase", "The read failed 4: " + firebaseError.getMessage());
                    }
                });
    }

    public void showZoomed(LinearLayout linearLayout, Bitmap thePicture) {
        String file_path = Environment.getExternalStorageDirectory().getAbsolutePath() +
                "/Preview";
        File dir = new File(file_path);
        if(!dir.exists())
            dir.mkdirs();
        File file = new File(dir, "preview.jpeg");
        try {
            FileOutputStream fOut = new FileOutputStream(file);
            thePicture.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
            fOut.flush();
            fOut.close();
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.parse("file://" + file.getAbsolutePath()), "image/*");
            Log.e(TAG, "showZoomed: " + Uri.parse("file://" + file.getAbsolutePath().toString()));
            linearLayout.getContext().
                startActivity(intent);
        } catch (Exception e) {

        }

    }

    public static Bitmap decodeSampledBitmapFromFile(String path, int reqWidth, int reqHeight)
    {

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        final int height = options.outHeight;
        final int width = options.outWidth;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        int inSampleSize = 1;


        if (height > reqHeight)
        {
            inSampleSize = Math.round((float)height / (float)reqHeight);
        }
        int expectedWidth = width / inSampleSize;

        if (expectedWidth > reqWidth)
        {
            //if(Math.round((float)width / (float)reqWidth) > inSampleSize) // If bigger SampSize..
            inSampleSize = Math.round((float)width / (float)reqWidth);
        }


        options.inSampleSize = inSampleSize;

        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(path, options);
    }

    public static String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality)
    {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }

    public static Bitmap decodeBase64(String input)
    {
        byte[] decodedBytes = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

}
