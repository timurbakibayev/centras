package com.gii.insreport;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class AnimaActivity extends AppCompatActivity {
    private static final String TAG = "AnimaActivity";
    public AnimaView animaView;

    public Bitmap backgroundbitmap;
    public IconsWindow iconsWindow;

    public ArrayList<Frame> frames = new ArrayList<>();

    public int frameNo = 0;
    public boolean play = false;
    public Timer tim = new Timer();

    private boolean readOnly = false;

    String currentFilename = "default";

    public FloatingActionButton defFab;

    @Override
    public void onBackPressed() {
        if (animaView.appState != AnimaView.AppState.idle) {
            animaView.appState = AnimaView.AppState.idle;
            refreshFab(this);
        } else
            super.onBackPressed();
        animaView.postInvalidate();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anima);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        readOnly = getIntent().getBooleanExtra("readOnly",false);

        iconsWindow = new IconsWindow();

        setSupportActionBar(toolbar);

        final AnimaActivity thisActivity = this;

        findViewById(R.id.penFab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (animaView.appState != animaView.appState.freeDraw)
                    animaView.appState = AnimaView.AppState.freeDraw;
                else
                    animaView.appState = AnimaView.AppState.idle;
                refreshFab(thisActivity);
            }
        });
        findViewById(R.id.penFab).setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(thisActivity,R.color.colorPrimary)));

        final FloatingActionButton photoFab = (FloatingActionButton) findViewById(R.id.photoFab);
        photoFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animaView.takeSnapshot();
            }
        });
        photoFab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(thisActivity,R.color.colorPrimary)));

        final FloatingActionButton undoFab = (FloatingActionButton) findViewById(R.id.undoFab);
        undoFab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(thisActivity,R.color.colorPrimary)));
        undoFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animaView.currentFrame.undo();
                animaView.appState = AnimaView.AppState.idle;
                refreshFab(thisActivity);
                animaView.postInvalidate();
            }
        });

        final FloatingActionButton iconFab = (FloatingActionButton) findViewById(R.id.iconFab);
        iconFab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(thisActivity,R.color.colorPrimary)));
        iconFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animaView.appState = AnimaView.AppState.chooseIcon;
                animaView.postInvalidate();
                refreshFab(thisActivity);
            }
        });

        final FloatingActionButton gmapsFab = (FloatingActionButton) findViewById(R.id.googleMaps);
        gmapsFab.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(thisActivity,R.color.colorPrimary)));
        gmapsFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gmaps = new Intent(thisActivity, MapsActivity.class);
                thisActivity.startActivityForResult(gmaps, 1);
            }
        });

        if (readOnly) {
            findViewById(R.id.penFab).setVisibility(View.GONE);
            gmapsFab.setVisibility(View.GONE);
            iconFab.setVisibility(View.GONE);
            undoFab.setVisibility(View.GONE);
            photoFab.setVisibility(View.GONE);
        }

        if (animaView == null) {
            animaView = new AnimaView(this);
            animaView.readOnly = readOnly;
            refreshFab(thisActivity);
        }

        animaView.bindActivity(this);
        animaView.loadResources(this);
        frames = InsReport.currentElement.frames;
        if (frames.size() == 0)
            frames.add(new Frame());
        animaView.frames = frames;
        if (animaView.currentFrame == null)
            animaView.currentFrame = frames.get(0);
        ViewGroup parent = (ViewGroup) animaView.getParent();
        if (parent != null) {
            parent.removeAllViews();
        }
        ((RelativeLayout) findViewById(R.id.canvas)).addView(animaView);

        tim.schedule(new TimerTask() {
            @Override
            public void run() {
                AnimaActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (play) {
                            if (frameNo < frames.size() - 1) {
                                //frameNo++;
                                animaView.playTo(frameNo + 1);
                                animaView.currentFrame = frames.get(frameNo);
                                animaView.postInvalidate();
                            } else
                                play = false;
                        }
                    }
                });
            }
        }, 0, 100);

        iconsWindow.init(animaView);

    }

    public void refreshFab(Context context) {
        View view = findViewById(R.id.penFab);

        animaView.postInvalidate();
        view.setBackgroundTintList(
                ColorStateList.valueOf(
                        animaView.appState != animaView.appState.freeDraw?
                                ContextCompat.getColor(context,R.color.colorPrimary):
                                ContextCompat.getColor(context,R.color.colorAccent))
        );
    }

    public void updateFrameNo() {
        frameNoMenu.setTitle("Кадр " + (frameNo + 1) + "/" + frames.size() + "\n Play");
    }

    MenuItem frameNoMenu;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_anima, menu);
        frameNoMenu = menu.findItem(R.id.action_frameClick);
        updateFrameNo();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_delete_frame) {
            deleteCurrentFrame();
            return true;
        }

        if (id == R.id.action_new) {
            newFile();
            refreshFab(this);
            return true;
        }

        if (id == R.id.action_prevFrame) {
            if (frameNo > 0) {
                frameNo--;
                animaView.currentFrame = frames.get(frameNo);
                animaView.appState = AnimaView.AppState.idle;
                updateFrameNo();
            }
            animaView.postInvalidate();
            refreshFab(this);
            return true;
        }

        if (id == R.id.action_nextFrame) {
            if (frameNo < frames.size() - 1) {
                frameNo++;
            } else {
                frames.add(new Frame(animaView.currentFrame));
                frameNo = frames.size() - 1;
            }
            animaView.currentFrame = frames.get(frameNo);
            animaView.appState = AnimaView.AppState.idle;
            updateFrameNo();
            animaView.postInvalidate();
            refreshFab(this);
            return true;
        }

        if (id == R.id.action_frameClick) {
            if (frameNo == frames.size() - 1) {
                frameNo = 0;
                animaView.currentFrame = frames.get(frameNo);
                animaView.appState = AnimaView.AppState.idle;
                animaView.postInvalidate();
            }
            play = true;
            animaView.playTo(frameNo);
        }
        refreshFab(this);
        return super.onOptionsItemSelected(item);
    }


    public void newFile() {
        frames.clear();
        frames.add(new Frame());
        frameNo = 0;
        updateFrameNo();
        currentFilename = "default";
        animaView.currentFrame = frames.get(0);
        animaView.postInvalidate();
    }

    public void deleteCurrentFrame() {
        frames.remove(frameNo);
        if (frameNo >= frames.size())
            frameNo--;
        if (frameNo < 0) {
            frames.add(new Frame());
            frameNo = 0;
        }
        updateFrameNo();
        animaView.currentFrame = frames.get(frameNo);
        animaView.postInvalidate();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Log.w(TAG, "onActivityResult: returned from google maps");
            backgroundbitmap = CameraAndPictures.decodeSampledBitmapFromFile(Environment.getExternalStorageDirectory() + File.separator + "image.jpg",
                    1000, 1000);
            Log.w(TAG, "onActivityResult: maps dimensions: " + backgroundbitmap.getWidth() + "," + backgroundbitmap.getHeight());
            animaView.postInvalidate();
        }
    }

}