package com.gii.insreport;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Acer on 8/30/2016.
 */
public class AlertDialogFragment extends DialogFragment {

    EditText[] editText;
    AutoCompleteTextView[] autoComplete;
    int editTextSize = 0;

    public AlertDialogFragment() {
        // Empty constructor required for DialogFragment
    }


    private ArrayList<String> list;

    public static AlertDialogFragment newInstance(String title, Bundle bundle) {
        AlertDialogFragment frag = new AlertDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);

        if (bundle.containsKey("TextValue")) {
            args.putStringArray("TextValue", bundle.getStringArray("TextValue"));
        }
        if (bundle.containsKey("EditValue")) {
            args.putStringArray("EditValue", bundle.getStringArray("EditValue"));
        }
        if (bundle.containsKey("autoHint")) {
            args.putStringArray("autoHint", bundle.getStringArray("autoHint"));
        }
        if (bundle.containsKey("autoName")) {
            args.putStringArray("autoName", bundle.getStringArray("autoName"));
        }

        if (bundle.containsKey("autoDictionary")) {
            args.putSerializable("autoDictionary", bundle.getSerializable("autoDictionary"));
        }
        if (bundle.containsKey("savedData")) {
            args.putSerializable("savedData", bundle.getStringArrayList("savedData"));
        }

        frag.setArguments(args);
        return frag;
    }

    @Override
    public android.app.Dialog onCreateDialog(Bundle savedInstanceState) {
        final String title = getArguments().getString("title");

        String[] givenTextList = getArguments().getStringArray("TextValue");
        String[] givenEditList = getArguments().getStringArray("EditValue");
        String[] autoHint = getArguments().getStringArray("autoHint");
        String[] autoName = getArguments().getStringArray("autoName");

        ArrayList<String[]> autoDictionary = (ArrayList<String[]>) getArguments().getSerializable("autoDictionary");
        ArrayList<String> savedData = getArguments().getStringArrayList("savedData");


        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle(title);
        ScrollView scroll = new ScrollView(getContext());

        list = new ArrayList<>();

        int padding = getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin);

        LinearLayout ll = new LinearLayout(getContext());
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.setFocusable(true);
        ll.setFocusableInTouchMode(true);
        LayoutParams innerLL_lp = (new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT));

        ll.setLayoutParams(innerLL_lp);

        TextView text, text1;
        LinearLayout[] innerLL = new LinearLayout[givenTextList.length / 2];
        int k = 0;


        for (int i = 0; i < givenTextList.length / 2; i++) {
            innerLL[i] = new LinearLayout(getContext());
            innerLL[i].setOrientation(LinearLayout.HORIZONTAL);
            innerLL[i].setLayoutParams(innerLL_lp);
            text = new TextView(getContext());
            text.setText(givenTextList[k++]);
            text.setPadding(2 * padding, 0, 0, 0);
            text.setLayoutParams(new LayoutParams(0,
                    ViewGroup.LayoutParams.WRAP_CONTENT, 1));

            text1 = new TextView(getContext());
            text1.setText(givenTextList[k++]);
            text1.setPadding(2 * padding, 0, 0, 0);
            text1.setLayoutParams(new LayoutParams(0,
                    ViewGroup.LayoutParams.WRAP_CONTENT, 1));
            innerLL[i].addView(text);
            innerLL[i].addView(text1);

        }
        for (int i = 0; i < givenTextList.length / 2; i++) {
            ll.addView(innerLL[i]);
        }


        TextInputLayout fieldHint;

        editTextSize = givenEditList.length;


        editText = new EditText[editTextSize];


        for (int i = 0; i < editTextSize; i++) {
            editText[i] = new EditText(getContext());
            if(savedData != null) {
                editText[i].setText(savedData.get(i + 1));
            }
            fieldHint = new TextInputLayout(getContext());
            fieldHint.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT));
            fieldHint.setHint(givenEditList[i]);
            fieldHint.setPadding(2 * padding, 0, padding, 0);
            fieldHint.addView(editText[i]);
            ll.addView(fieldHint);
        }


        final int autoLength = autoHint.length;

        autoComplete = new AutoCompleteTextView[autoLength];


        for (int i = 0; i < autoLength; i++) {
            autoComplete[i] = new AutoCompleteTextView(getContext());
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                    getContext(), android.R.layout.simple_list_item_1, autoDictionary.get(i)
            );
            autoComplete[i].setAdapter(adapter);
            if(savedData != null){
                autoComplete[i].setText(savedData.get(editTextSize+i+1));
            }
            fieldHint = new TextInputLayout(getContext());
            fieldHint.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT));
            fieldHint.setHint(autoHint[i]);
            fieldHint.setPadding(2 * padding, 0, padding, 0);
            fieldHint.addView(autoComplete[i]);
            ll.addView(fieldHint);

        }

        scroll.addView(ll);

        alertDialogBuilder.setView(scroll);


        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // on success
                //MyDialogFragmentListener activity = (MyDialogFragmentListener) getActivity();
                list.add(title);
                for (int i = 0; i < editTextSize; i++) {
                    list.add(editText[i].getText().toString());
                }
                for (int i = 0; i < autoLength; i++) {
                    if(autoComplete[i].getText().toString().equalsIgnoreCase(""))
                    {
                        list.add("");
                    } else {
                        list.add(autoComplete[i].getText().toString());
                    }
                }

                //activity.onReturnValue(list);
            }
        });
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        return alertDialogBuilder.create();
    }
}

