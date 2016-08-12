package com.gii.insreport;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.math.BigInteger;
import java.security.SecureRandom;

public class CameraAndPictures {
    public static Bitmap bitmap;
    public static String generateNewId() {
        SecureRandom random = new SecureRandom();
        return  (new BigInteger(64, random).toString(32));
    }

    public static String savePictureToFirebase() {
        File file = new File(Environment.getExternalStorageDirectory()+File.separator + "image.jpg");
        //Uri fileUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory()+File.separator + "image.jpg"));
        bitmap = decodeSampledBitmapFromFile(file.getAbsolutePath(), 700, 700); //i guess, 640x480 would be enough
        String id = generateNewId();
        //uncomment this to upload the file to the firebase storage as well.
        //does not work offline :(
        //UploadTask uploadTask = InsReport.storageRef.child("images/" + id + ".jpg").putFile(fileUri);
        InsReport.ref.child("images/" + id).setValue(encodeToBase64(bitmap,Bitmap.CompressFormat.JPEG,70));
        return id;
    }

    public void getPicFromFirebase(String picId, final LinearLayout linearLayout) {

        InsReport.ref.child("images/" + picId).
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
        final AlertDialog.Builder builder = new AlertDialog.Builder(linearLayout.getContext());
        final AlertDialog dialog = builder.create();
        final ScrollView filterScrollView = new ScrollView(linearLayout.getContext());

        ImageView theImageViewBigger = new ImageView(linearLayout.getContext());
        theImageViewBigger.setImageBitmap(thePicture);
        //theImageViewBigger.setScaleType(ImageView.ScaleType.FIT_XY);
        theImageViewBigger.setAdjustViewBounds(true);

        filterScrollView.addView(theImageViewBigger);

        builder
                //.setMessage(getContext().getString(R.string.enter_amount))
                .setView(filterScrollView)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                }).show();
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
