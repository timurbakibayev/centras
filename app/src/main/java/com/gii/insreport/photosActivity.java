package com.gii.insreport;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Acer on 9/27/2016.
 */

public class PhotosActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 17;
    private static final int REQUEST_IMAGE_GALLERY = 34;
    private static final int REQUEST_MULTIPLE_GALLERY = 39;

    Element element = InsReport.currentElement;
    Form currentForm = InsReport.currentForm;
    CameraAndPictures cameraAndPictures = new CameraAndPictures();
    LinearLayout picturesLL;
    Timer timer = new Timer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

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

        //TODO: for some reason element.elements was null once :(
        for (Element el : element.elements)
            if (!el.deleted)
                cameraAndPictures.getPicFromFirebase(el,currentForm,picturesLL, element.comboItems);

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                TextView tv = (TextView)findViewById(R.id.requirementsTV);
                if (tv != null &&
                        tv.getHandler() != null) {
                    tv.getHandler().post(new Runnable() {
                        @Override
                        public void run() {
                            refreshRequirements();
                        }
                    });
                }
            }
        }, 1000,1000);
    }

    void refreshRequirements() {
        if (findViewById(R.id.requirementsTV) == null)
            return;
        String requirements = "";
        for (String comboItem : element.comboItems) {
            if (!comboItem.equals("")) {
                boolean exitsts = false;
                for (Element element1 : element.elements) {
                    if (element1.description.equals(comboItem) && !element1.deleted)
                        exitsts = true;
                }
                if (!exitsts) {
                    if (requirements.equals(""))
                        requirements = "Требуется:";
                    requirements += "\n" + comboItem;
                }
            }
        }
        ((TextView)findViewById(R.id.requirementsTV)).setText(requirements);
        if (findViewById(R.id.titleTV) == null)
            return;
        ((TextView)findViewById(R.id.titleTV)).setText(element.description);
    }


    private void takeFromGallery() {
        Intent intent = new Intent(this, Gallery.class);
        startActivityForResult(intent, REQUEST_MULTIPLE_GALLERY);
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
                final Element newPhotoElement = new Element();
                newPhotoElement.vText = id;
                newPhotoElement.vDate = new Date();
                final PhotosActivity thisActivity = this;
                TextView descriptionTV = cameraAndPictures.descriptionTextView(newPhotoElement,currentForm,newImage,thisActivity,element.comboItems);
                element.elements.add(newPhotoElement);
                picturesLL.addView(cameraAndPictures.deleteTextView(newPhotoElement,currentForm,newImage,thisActivity,descriptionTV));
                picturesLL.addView(newImage);
                picturesLL.addView(descriptionTV);
            }
        }

        if (requestCode == REQUEST_IMAGE_GALLERY && resultCode == RESULT_OK) {
            if (data != null) {
                String selectedImagePath = "";
                Uri selectedImage = data.getData();
                String[] filePath = { MediaStore.Images.Media.DATA };
                Cursor c = getContentResolver().query(selectedImage, filePath,
                        null, null, null);
                if (c == null)
                    return;

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
                        final Element newPhotoElement = new Element();
                        newPhotoElement.vText = id;
                        newPhotoElement.vDate = new Date();
                        final PhotosActivity thisActivity = this;
                        TextView descriptionTV = cameraAndPictures.descriptionTextView(newPhotoElement,currentForm,newImage,thisActivity,element.comboItems);
                        element.elements.add(newPhotoElement);
                        picturesLL.addView(cameraAndPictures.deleteTextView(newPhotoElement,currentForm,newImage,thisActivity,descriptionTV));
                        picturesLL.addView(newImage);
                        picturesLL.addView(descriptionTV);
                    }
                }
            } else {
                Toast.makeText(getApplicationContext(), "Cancelled",
                        Toast.LENGTH_SHORT).show();
            }
        }

        if (requestCode == REQUEST_MULTIPLE_GALLERY) {
            for (int i = 0; i < InsReport.multipleImages.size(); i++) {
                String nextImage = InsReport.multipleImages.get(i);
                Long nextDate = InsReport.multipleImagesDate.get(i);
                final String id = CameraAndPictures.savePictureToFirebase(nextImage);
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
                    final Element newPhotoElement = new Element();
                    newPhotoElement.vText = id;
                    newPhotoElement.vDate = new Date(nextDate);
                    final PhotosActivity thisActivity = this;
                    element.elements.add(newPhotoElement);
                    TextView descriptionTV = cameraAndPictures.descriptionTextView(newPhotoElement,currentForm,newImage,thisActivity,element.comboItems);
                    picturesLL.addView(cameraAndPictures.deleteTextView(newPhotoElement,currentForm,newImage,thisActivity,descriptionTV));
                    picturesLL.addView(newImage);
                    picturesLL.addView(descriptionTV);
                }
            }
        }

        currentForm.saveToCloud();
        super.onActivityResult(requestCode, resultCode, data);
    }

}

