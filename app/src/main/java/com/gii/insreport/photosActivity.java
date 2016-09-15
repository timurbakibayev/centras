package com.gii.insreport;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.Date;

public class photosActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 17;
    private static final int REQUEST_IMAGE_GALLERY = 34;

    Element element = InsReport.currentElement;
    Form currentForm = InsReport.currentForm;
    CameraAndPictures cameraAndPictures = new CameraAndPictures();
    LinearLayout picturesLL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //toolbar.setTitle(InsReport.currentElement.description);
        //getWindow().setTitle(InsReport.currentElement.description);

        //TODO: fails sometimes :(
        //Caused by: java.lang.NullPointerException: Attempt to read from field 'java.lang.String com.gii.insreport.Element.description' on a null object reference
        //setTitle(InsReport.currentElement.description);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabPhoto);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takeAPicture();
            }
        });

        ((FloatingActionButton) findViewById(R.id.galleryFab)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takeFromGallery();
            }
        });

        picturesLL = (LinearLayout)findViewById(R.id.photoLLNew);

        for (Element el : element.elements)
            cameraAndPictures.getPicFromFirebase(el,picturesLL);

    }


    private void takeFromGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,
                "Select Picture"), REQUEST_IMAGE_GALLERY);
    }

    private void takeAPicture() {
        CameraAndPictures.bitmap = null;
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File file = new File(Environment.getExternalStorageDirectory()+File.separator + "image.jpg");
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            final String id = CameraAndPictures.savePictureToFirebase(Environment.getExternalStorageDirectory()+ File.separator + "image.jpg");
            if (CameraAndPictures.bitmap != null) {
                InsReport.bitmapsNeedToBeRecycled.add(CameraAndPictures.bitmap);
                ImageView newImage = new ImageView(this);
                newImage.setImageBitmap(CameraAndPictures.bitmap);
                newImage.setAdjustViewBounds(true);
                newImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cameraAndPictures.showZoomed(picturesLL, CameraAndPictures.bitmap);
                    }
                });
                picturesLL.addView(newImage);
                Element newPhotoElement = new Element();
                newPhotoElement.vText = id;
                newPhotoElement.vDate = new Date();
                element.elements.add(newPhotoElement);
                TextView descriptionAndDate = new TextView(this);
                descriptionAndDate.setText(newPhotoElement.description + "  " + FillFormActivity.dateTimeText(newPhotoElement.vDate));
                picturesLL.addView(descriptionAndDate);

            }
        }

        if (requestCode == REQUEST_IMAGE_GALLERY && resultCode == RESULT_OK) {
            if (data != null) {
                String selectedImagePath = "";
                Uri selectedImage = data.getData();
                String[] filePath = { MediaStore.Images.Media.DATA };
                Cursor c = getContentResolver().query(selectedImage, filePath,
                        null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                selectedImagePath = c.getString(columnIndex);
                c.close();

                if (selectedImagePath != null) {
                    final String id = CameraAndPictures.savePictureToFirebase(selectedImagePath);
                    if (CameraAndPictures.bitmap != null) {
                        InsReport.bitmapsNeedToBeRecycled.add(CameraAndPictures.bitmap);
                        ImageView newImage = new ImageView(this);
                        newImage.setImageBitmap(CameraAndPictures.bitmap);
                        newImage.setAdjustViewBounds(true);
                        newImage.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                cameraAndPictures.showZoomed(picturesLL, CameraAndPictures.bitmap);
                            }
                        });
                        picturesLL.addView(newImage);
                        Element newPhotoElement = new Element();
                        newPhotoElement.vText = id;
                        newPhotoElement.vDate = new Date();
                        element.elements.add(newPhotoElement);
                        TextView descriptionAndDate = new TextView(this);
                        descriptionAndDate.setText(newPhotoElement.description + "  " + FillFormActivity.dateTimeText(newPhotoElement.vDate));
                        picturesLL.addView(descriptionAndDate);

                    }
                }
            } else {
                Toast.makeText(getApplicationContext(), "Cancelled",
                        Toast.LENGTH_SHORT).show();
            }
        }

        currentForm.saveToCloud();
        super.onActivityResult(requestCode, resultCode, data);
    }

}
