package com.gii.insreport;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class AnimaActivity extends AppCompatActivity {
    public AnimaView animaView;

    public IconsWindow iconsWindow;

    public ArrayList<Frame> frames = new ArrayList<>();

    public int frameNo = 0;
    public boolean play = false;
    public Timer tim = new Timer();

    String currentFilename = "default";

    public FloatingActionButton defFab;

    @Override
    public void onBackPressed() {
        if (animaView.appState != AnimaView.AppState.idle)
            animaView.appState = AnimaView.AppState.idle;
        else
            super.onBackPressed();
        animaView.postInvalidate();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anima);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        iconsWindow = new IconsWindow();

        setSupportActionBar(toolbar);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.lock_roads);
        defFab = (FloatingActionButton) findViewById(R.id.lock_roads);
        final AnimaActivity thisActivity = this;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //fab.setBackgroundDrawable();
                animaView.roads_are_locked = !animaView.roads_are_locked;
                if (animaView.roads_are_locked) {
                    fab.setImageResource(R.drawable.ic_lock_outline_black_24dp);
                    Toast.makeText(thisActivity, "Дороги зафиксированы", Toast.LENGTH_SHORT).show();
                }
                else {
                    fab.setImageResource(R.drawable.ic_lock_open_black_24dp);
                    Toast.makeText(thisActivity, "Дороги разблокированы", Toast.LENGTH_SHORT).show();
                }
            }
        });



        final FloatingActionButton deleteAllFab = (FloatingActionButton) findViewById(R.id.deleteFab);
        deleteAllFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animaView.currentFrame.strokes.clear();
                animaView.currentFrame.icons.clear();
                animaView.postInvalidate();
            }
        });

        final FloatingActionButton undoFab = (FloatingActionButton) findViewById(R.id.undoFab);
        undoFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animaView.currentFrame.undo();
                animaView.appState = AnimaView.AppState.idle;
                animaView.postInvalidate();
            }
        });

        final FloatingActionButton iconFab = (FloatingActionButton) findViewById(R.id.iconFab);
        iconFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animaView.appState = AnimaView.AppState.chooseIcon;
                animaView.postInvalidate();
            }
        });

        if (animaView == null) {
            animaView = new AnimaView(this);

        }

        animaView.bindActivity(this);
        animaView.loadResources(this);
        if (frames.size() == 0)
            frames.add(new Frame());
        animaView.frames = frames;
        if (animaView.currentFrame == null)
            animaView.currentFrame = frames.get(0);
        ViewGroup parent = (ViewGroup)animaView.getParent();
        if (parent != null) {
            parent.removeAllViews();
        }
        ((RelativeLayout)findViewById(R.id.canvas)).addView(animaView);

        tim.schedule(new TimerTask() {
            @Override
            public void run() {
                AnimaActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (play) {
                            if (frameNo < frames.size()-1) {
                                //frameNo++;
                                animaView.playTo(frameNo + 1);
                                animaView.currentFrame = frames.get(frameNo);
                                animaView.postInvalidate();
                                //TODO: updateFrameNo();
                            }
                            else
                                play = false;
                        }
                    }
                });
            }
        }, 0, 100);

        iconsWindow.init(animaView);

        if (animaView.roads_are_locked)
            fab.setImageResource(R.drawable.ic_lock_outline_black_24dp);
        else
            fab.setImageResource(R.drawable.ic_lock_open_black_24dp);
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_save_as) {
            saveAs();
            return true;
        }

        if (id == R.id.action_open) {
            load();
            return true;
        }

        if (id == R.id.action_new) {
            newFile();
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
            return true;
        }
        if (id == R.id.action_nextFrame) {
            if (frameNo < frames.size() - 1) {
                frameNo++;
            } else {
                frames.add(new Frame(animaView.currentFrame));
                frameNo = frames.size()-1;
            }
            animaView.currentFrame = frames.get(frameNo);
            animaView.appState = AnimaView.AppState.idle;
            updateFrameNo();
            animaView.postInvalidate();
            return true;
        }
        if (id == R.id.action_frameClick) {
            if (frameNo == frames.size()-1) {
                frameNo = 0;
                animaView.currentFrame = frames.get(frameNo);
                animaView.appState = AnimaView.AppState.idle;
                animaView.postInvalidate();
            }
            play = true;
            animaView.playTo(frameNo);
        }

        return super.onOptionsItemSelected(item);
    }


    public void saveAs() {

    }

    public void load() {

    }

    public void newFile() {
        //currentFilename = "";
        frames = new ArrayList<>();
        frames.add(new Frame());
        frameNo = 0;
        updateFrameNo();
        currentFilename = "default";
        animaView.currentFrame = frames.get(0);
        animaView.postInvalidate();
    }
}
