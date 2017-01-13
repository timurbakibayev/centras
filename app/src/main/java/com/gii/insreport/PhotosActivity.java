package com.gii.insreport;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
 * Created by Acer on 10/10/2016.
 */

public class PhotosActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 17;
    private static final int REQUEST_IMAGE_GALLERY = 34;
    private static final int REQUEST_MULTIPLE_GALLERY = 39;
    BottomSheetBehavior behavior;

    Element element = InsReport.currentElement;
    Form currentForm = InsReport.currentForm;
    CameraAndPictures cameraAndPictures = new CameraAndPictures();
    LinearLayout picturesLL;
    Timer timer = new Timer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);

        //bottomsheets
        View bottomSheet = findViewById(R.id.design_bottom_sheet);
        behavior = BottomSheetBehavior.from(bottomSheet);
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_DRAGGING:
                        Log.i("BottomSheetCallback", "BottomSheetBehavior.STATE_DRAGGING");
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        Log.i("BottomSheetCallback", "BottomSheetBehavior.STATE_SETTLING");
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        Log.i("BottomSheetCallback", "BottomSheetBehavior.STATE_EXPANDED");
                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        Log.i("BottomSheetCallback", "BottomSheetBehavior.STATE_COLLAPSED");
                        break;
                    case BottomSheetBehavior.STATE_HIDDEN:
                        Log.i("BottomSheetCallback", "BottomSheetBehavior.STATE_HIDDEN");
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                Log.i("BottomSheetCallback", "slideOffset: " + slideOffset);
            }
        });
        //end of bottomsheets



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        //TODO: fails sometimes :(
        //Caused by: java.lang.NullPointerException: Attempt to read from field 'java.lang.String com.gii.insreport.Element.description' on a null object reference
        //setTitle(InsReport.currentElement.description);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabPhoto);

        Log.w("PhotosActivity", "onCreate: InsReport.currentForm" + (InsReport.currentForm != null));
        Log.w("PhotosActivity", "onCreate: InsReport.incidentFormsCollection" + (InsReport.incidentFormsCollection != null));
        if (InsReport.currentForm == null && InsReport.incidentFormsCollection != null) {
            for (Form form : InsReport.incidentFormsCollection.forms) {
                if (form.id == InsReport.sharedPref.getString("lastFormId",""))
                    InsReport.currentForm=form;
            }
        }
        if (InsReport.currentForm == null && InsReport.preInsuranceFormsCollection != null) {
            for (Form form : InsReport.preInsuranceFormsCollection.forms) {
                if (form.id == InsReport.sharedPref.getString("lastFormId",""))
                    InsReport.currentForm=form;
            }
        }
        if (InsReport.currentForm == null) {
            finish();
            return;
        }
        if (InsReport.currentForm.formReady) {
            fab.setVisibility(View.GONE);
            (findViewById(R.id.galleryFab)).setVisibility(View.GONE);
        }

        if (!InsReport.currentForm.formReady)
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    takeAPicture();
                }
            });

        if (!InsReport.currentForm.formReady)
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

        if (findViewById(R.id.titleTV) != null)
            ((TextView)findViewById(R.id.titleTV)).setText(element.description);

        setTitle(element.description);

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
                if (!InsReport.currentForm.formReady)
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



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_photo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_info) {
            showHint();
        }

        return super.onOptionsItemSelected(item);
    }

    private void showHint() {
        if (behavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        } else {
            behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }

}
