package com.gii.insreport;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class FormsList extends AppCompatActivity {

    String fireBaseCatalog = "";
    FormsCollection currentFormsCollection;
    public static FormsList formList;
    String selectionType = "";
    Dialog settingsDialog;

    FormsListAdapter formsListAdapter;

    //my changes
    BackgroundContainer mBackgroundContainer;
    final ArrayList<View> mCheckedViews = new ArrayList<View>();
    boolean mSwiping = false;
    boolean mItemPressed = false;
    HashMap<Long, Integer> mItemIdTopMap = new HashMap<Long, Integer>();
    boolean mAnimating = false;
    float mCurrentX = 0;
    float mCurrentAlpha = 1;

    View mListIDView;

    private static final int SWIPE_DURATION = 250;
    private static final int MOVE_DURATION = 150;

    //end of changes
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forms_list);
        formList = this;

        //my
        mBackgroundContainer = (BackgroundContainer) findViewById(R.id.listViewBackground);

        //end
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fireBaseCatalog = getIntent().getStringExtra(InsReport.EXTRA_FIREBASE_CATALOG);

        FormsCollection formsCollection = null;
        for (FormsCollection formsCollection1 : InsReport.mainMenuForms)
            if (formsCollection1.fireBaseCatalog.equals(fireBaseCatalog))
                formsCollection = formsCollection1;

        ((TextView)findViewById(R.id.text_header)).setText(formsCollection.description);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fireBaseCatalog.equalsIgnoreCase("preInsurance")) {
                    showSelectionDialog();
                } else {
                    addnewForm();
                }
            }
        });

//        ((Button) findViewById(R.id.hiButton)).setText(fireBaseCatalog);

        for (FormsCollection formCollections : InsReport.mainMenuForms) {
            if (fireBaseCatalog.equals(formCollections.fireBaseCatalog)) {
                formsListAdapter = new FormsListAdapter(this, formCollections.forms, mTouchListener);
                currentFormsCollection = formCollections;
                InsReport.currentListView = ((ListView) findViewById(R.id.listView));
                InsReport.currentListView.setAdapter(formsListAdapter);
            }
        }

        final FormsList thisActivity = this;
        mListIDView = findViewById(R.id.listView);
        ((ListView) findViewById(R.id.listView)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if(view.getId() == )

                openTheForm(currentFormsCollection.forms.get(position).id);
            }
        });

        final FormsList context = this;
        findViewById(R.id.search_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent searchIntent = new Intent(context,SearchActivity.class);
                startActivity(searchIntent);
            }
        });

    }

    private boolean isRuntimePostGingerbread() {
        return android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB;
    }


    final FormsList thisActivity = this;
    LinearLayout underCover;
    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {

        float mDownX;
        private int mSwipeSlop = -1;

        @Override
        public boolean onTouch(View view, MotionEvent event) {
            if (mSwipeSlop < 0) {
                mSwipeSlop = ViewConfiguration.get(FormsList.this).getScaledTouchSlop();
            }
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (mAnimating) {
                        return true;
                    }
                    mItemPressed = true;
                    mDownX = event.getX();
                    //underCover = (LinearLayout)view;
                    //underCover.setBackgroundColor(Color.GREEN);
                    break;
                case MotionEvent.ACTION_CANCEL:
                    setSwipePosition(view, 0);
                    mItemPressed = false;
                    break;
                case MotionEvent.ACTION_MOVE: {
                    if (mAnimating) {
                        return true;
                    }

                    float x = event.getX();
                    if (isRuntimePostGingerbread()) {
                        x += view.getTranslationX();
                    }
                    float deltaX = x - mDownX;
                    float deltaXAbs = Math.abs(deltaX);
                    if (!mSwiping) {
                        if (deltaXAbs > mSwipeSlop) {
                            mSwiping = true;
                            InsReport.currentListView.requestDisallowInterceptTouchEvent(true);
                            mBackgroundContainer.showBackground(view.getTop(), view.getHeight());

                        }
                    }
                    if (mSwiping) {
                        setSwipePosition(view, deltaX);
                    }
                }
                break;
                case MotionEvent.ACTION_UP: {
                    if (mAnimating) {
                        return true;
                    }
                    if (mSwiping) {
                        float x = event.getX();
                        if (isRuntimePostGingerbread()) {
                            x += view.getTranslationX();
                        }
                        float deltaX = x - mDownX;
                        float deltaXAbs = Math.abs(deltaX);
                        float fractionCovered;
                        float endX;
                        final boolean remove;
                        if (deltaXAbs > view.getWidth() / 4) {
                            fractionCovered = deltaXAbs / view.getWidth();
                            endX = deltaX < 0 ? -view.getWidth() : view.getWidth();
                            remove = true;
                        } else {
                            fractionCovered = 1 - (deltaXAbs / view.getWidth());
                            endX = 0;
                            remove = false;
                        }
                        long duration = (int) ((1 - fractionCovered) * SWIPE_DURATION);
                        animateSwipe(view, endX, duration, remove);
                        mSwiping = false;
                    } else {
                        mItemPressed = false;
                        String s = ((TextView) view.findViewById(R.id.textViewID)).getText().toString();
                        openTheForm(s);
                    }
                }
                break;
                default:
                    return false;
            }
            return true;
        }
    };

    private void animateSwipe(final View view, float endX, long duration, final boolean remove) {
        mAnimating = true;
        InsReport.currentListView.setEnabled(false);
        if (isRuntimePostGingerbread()) {
            view.animate().setDuration(duration).
                    alpha(remove ? 0 : 1).translationX(endX).
                    setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            // Restore animated values
                            view.setAlpha(1);
                            view.setTranslationX(0);
                            if (remove) {
                                animateOtherViews(InsReport.currentListView, view);
                            } else {
                                mBackgroundContainer.hideBackground();
                                mSwiping = false;
                                mAnimating = false;
                                InsReport.currentListView.setEnabled(true);
                            }
                            mItemPressed = false;
                        }
                    });
        } else {
            TranslateAnimation swipeAnim = new TranslateAnimation(mCurrentX, endX, 0, 0);
            AlphaAnimation alphaAnim = new AlphaAnimation(mCurrentAlpha, remove ? 0 : 1);
            AnimationSet set = new AnimationSet(true);
            set.addAnimation(swipeAnim);
            //set.addAnimation(alphaAnim);
            set.setDuration(duration);
            view.startAnimation(set);
            setAnimationEndAction(set, new Runnable() {
                @Override
                public void run() {
                    if (remove) {
                        animateOtherViews(InsReport.currentListView, view);
                    } else {
                        mBackgroundContainer.hideBackground();
                        mSwiping = false;
                        mAnimating = false;
                        InsReport.currentListView.setEnabled(true);
                    }
                    mItemPressed = false;
                }
            });
        }
    }

    private void setSwipePosition(View view, float deltaX) {
        float fraction = Math.abs(deltaX) / view.getWidth();
        if (isRuntimePostGingerbread()) {
            view.setTranslationX(deltaX);
            //view.setAlpha(1 - fraction);
            //mBackgroundContainer.setAlpha(fraction);
        } else {
            // Hello, Gingerbread!
            TranslateAnimation swipeAnim = new TranslateAnimation(deltaX, deltaX, 0, 0);
            mCurrentX = deltaX;
            mCurrentAlpha = (1 - fraction);
            AlphaAnimation alphaAnim = new AlphaAnimation(mCurrentAlpha, mCurrentAlpha);
            AnimationSet set = new AnimationSet(true);
            set.addAnimation(swipeAnim);
            //set.addAnimation(alphaAnim);
            set.setFillAfter(true);
            set.setFillEnabled(true);
            view.startAnimation(set);
        }
    }

    private void animateOtherViews(final ListView listView, View viewToRemove) {
        int firstVisiblePosition = listView.getFirstVisiblePosition();
        for (int i = 0; i < listView.getChildCount(); ++i) {
            View child = listView.getChildAt(i);
            int position = firstVisiblePosition + i;
            long itemId = formsListAdapter.getItemId(position);
            if (child != viewToRemove) {
                mItemIdTopMap.put(itemId, child.getTop());
            }
        }
        // Delete the item from the adapter
        //int position = InsReport.currentListView.getPositionForView(viewToRemove);
        //currentFormsCollection.forms.remove(position);
        String id = ((TextView) viewToRemove.findViewById(R.id.textViewID)).getText().toString();
        for (Form form : currentFormsCollection.forms) {
            if (form.id.equals(id)) {
                form.switchDone(viewToRemove.getContext(), false, null);
            }
        }

        final ViewTreeObserver observer = listView.getViewTreeObserver();
        observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                observer.removeOnPreDrawListener(this);
                boolean firstAnimation = true;
                int firstVisiblePosition = listView.getFirstVisiblePosition();
                for (int i = 0; i < listView.getChildCount(); ++i) {
                    final View child = listView.getChildAt(i);
                    int position = firstVisiblePosition + i;
                    long itemId = formsListAdapter.getItemId(position);
                    Integer startTop = mItemIdTopMap.get(itemId);
                    int top = child.getTop();
                    if (startTop == null) {
                        // Animate new views along with the others. The catch is that they did not
                        // exist in the start state, so we must calculate their starting position
                        // based on whether they're coming in from the bottom (i > 0) or top.
                        int childHeight = child.getHeight() + listView.getDividerHeight();
                        startTop = top;// + (i > 0 ? childHeight : -childHeight);
                    }
                    int delta = startTop - top;
                    if (delta != 0) {
                        Runnable endAction = firstAnimation ?
                                new Runnable() {
                                    public void run() {
                                        mBackgroundContainer.hideBackground();
                                        mSwiping = false;
                                        mAnimating = false;
                                        InsReport.currentListView.setEnabled(true);
                                    }
                                } :
                                null;
                        firstAnimation = false;
                        moveView(child, 0, 0, delta, 0, endAction);
                    } else {
                        mBackgroundContainer.hideBackground();
                        mSwiping = false;
                        mAnimating = false;
                        InsReport.currentListView.setEnabled(true);
                    }
                }
                mItemIdTopMap.clear();
                return true;
            }
        });
    }

    private void moveView(View view, float startX, float endX, float startY, float endY,
                          Runnable endAction) {
        final Runnable finalEndAction = endAction;
        if (isRuntimePostGingerbread()) {
            view.animate().setDuration(MOVE_DURATION);
            if (startX != endX) {
                ObjectAnimator anim = ObjectAnimator.ofFloat(view, View.TRANSLATION_X, startX, endX);
                anim.setDuration(MOVE_DURATION);
                anim.start();
                setAnimatorEndAction(anim, endAction);
                endAction = null;
            }
            if (startY != endY) {
                ObjectAnimator anim = ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, startY, endY);
                anim.setDuration(MOVE_DURATION);
                anim.start();
                setAnimatorEndAction(anim, endAction);
            }
        } else {
            TranslateAnimation translator = new TranslateAnimation(startX, endX, startY, endY);
            translator.setDuration(MOVE_DURATION);
            view.startAnimation(translator);
            if (endAction != null) {
                view.getAnimation().setAnimationListener(new AnimationListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        finalEndAction.run();
                    }
                });
            }
        }
    }

    private void setAnimatorEndAction(Animator animator, final Runnable endAction) {
        if (endAction != null) {
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    endAction.run();
                }
            });
        }
    }

    private void setAnimationEndAction(Animation animation, final Runnable endAction) {
        if (endAction != null) {
            animation.setAnimationListener(new AnimationListenerAdapter() {
                @Override
                public void onAnimationEnd(Animation animation) {
                    endAction.run();
                }
            });
        }
    }

    static class AnimationListenerAdapter implements Animation.AnimationListener {

        @Override
        public void onAnimationEnd(Animation animation) {
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
        }

        @Override
        public void onAnimationStart(Animation animation) {
        }
    }


    public void dismissListener(View view) {
        switch (view.getId()) {
            case R.id.button1:
                selectionType = "Bike";
                settingsDialog.dismiss();
                addnewForm();
                break;
            case R.id.button2:
                selectionType = "Bus";
                settingsDialog.dismiss();
                addnewForm();
                break;
            case R.id.button3:
                selectionType = "Truck";
                settingsDialog.dismiss();
                addnewForm();
                break;
            case R.id.button4:
                selectionType = "Car";
                settingsDialog.dismiss();
                addnewForm();
                break;
        }
        Toast.makeText(FormsList.this, String.valueOf(view.getId()), Toast.LENGTH_SHORT).show();
    }

    private void addnewForm() {
        //fireBaseCatalog is accessible. Now we need to prepare the corresponding template
        Form newForm = new Form();
        newForm.generateNewId();
        InsReport.logFirebase("Create new " + fireBaseCatalog + " form no. " + newForm.id);
        FormTemplates.selectionTypes = selectionType;
        FormTemplates.applyTemplate(newForm, fireBaseCatalog);
        newForm.dateAccepted = new Date();
        newForm.dateCreated = new Date();
        newForm.status = "accept";
        currentFormsCollection.forms.add(newForm);
        ((FormsListAdapter) ((ListView) findViewById(R.id.listView)).getAdapter()).notifyDataSetChanged();
        openTheForm(newForm.id);
    }

    public void openTheForm(String id) {
        Form currentForm = null;
        for (FormsCollection formsCollection : InsReport.mainMenuForms) {
            if (formsCollection.fireBaseCatalog.equals(fireBaseCatalog)) {
                for (Form form : formsCollection.forms) {
                    if (form.id.equals(id)) {
                        currentForm = form;
                    }
                }
            }
        }

        if (currentForm != null) {
            if (currentForm.status.equals("")) {
                if (!currentForm.formReady)
                    InsReport.mainActivity.acceptOrRejectDialogShow(currentForm,this);
            }
            if (currentForm.status.equals("accept")) {
                Intent intent = new Intent(thisActivity, IncidentFormActivity.class);
                intent.putExtra(InsReport.EXTRA_FIREBASE_CATALOG, fireBaseCatalog);
                intent.putExtra(InsReport.EXTRA_ID_NO, id);
                intent.putExtra(InsReport.EXTRA_FORM_READY, currentForm.formReady);
                InsReport.logFirebase("Open " + fireBaseCatalog + " form no. " + id);
                startActivity(intent);
            } else {
                snackBar("Форма не была принята!");
            }
        }
    }

    public void snackBar(String message) {
        Snackbar.make(((FloatingActionButton) findViewById(R.id.fab)), message, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((FormsListAdapter) ((ListView) findViewById(R.id.listView)).getAdapter()).notifyDataSetChanged();
        //clear memory
    }

    public void showSelectionDialog() {
        settingsDialog = new Dialog(FormsList.this);
        settingsDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        settingsDialog.setContentView(getLayoutInflater().inflate(R.layout.report_selection
                , null));
        settingsDialog.show();
    }

}
