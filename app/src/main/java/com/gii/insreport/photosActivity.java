package com.gii.insreport;

import android.content.Intent;
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

import java.io.File;
import java.util.Date;

public class photosActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 17;

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

        picturesLL = (LinearLayout)findViewById(R.id.photoLLNew);

        for (Element el : element.elements)
            cameraAndPictures.getPicFromFirebase(el,picturesLL);

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
            final String id = CameraAndPictures.savePictureToFirebase();
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

        currentForm.saveToCloud();
        super.onActivityResult(requestCode, resultCode, data);
    }

}
