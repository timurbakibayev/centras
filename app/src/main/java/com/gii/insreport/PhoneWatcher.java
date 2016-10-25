package com.gii.insreport;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * Created by Timur_hnimdvi on 25-Oct-16.
 */
public class PhoneWatcher implements TextWatcher{
    EditText editText;
    private boolean mWasEdited = false;

    public PhoneWatcher(EditText editText) {
        this.editText = editText;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (mWasEdited){

            mWasEdited = false;
            return;
        }
        String s = editable.toString();
        String s1 = "";
        s = s.replaceAll(" ","").replaceAll("_","");
        for (int i = 0; i < s.length(); i++) {
            if (i == 3 || i == 6 || i == 8)  s1 += " ";
            s1 += s.substring(i,i+1);
        }
        mWasEdited = true;
        editText.setText(s1);
        editText.setSelection(s1.length());
    }
}
