package com.gii.insreport;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by Timur_hnimdvi on 25-Oct-16.
 */
public class AmountWatcher implements TextWatcher{
    EditText editText;
    boolean changedHere = false;
    public DecimalFormat df = (java.text.DecimalFormat) NumberFormat.getInstance(Locale.US);
    public AmountWatcher(EditText editText) {
        this.editText = editText;
        DecimalFormatSymbols symbols = df.getDecimalFormatSymbols();
        symbols.setGroupingSeparator(' ');
        df.setDecimalFormatSymbols(symbols);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (changedHere) {
            changedHere = false;
            return;
        }
        int deci = -1;
        String in = editable.toString().replaceAll(" ","").replaceAll(",",".");
        if (in.length()>1 &&
                in.substring(in.length()-1,in.length()).equals("."))
            deci = 0;
        if (in.length()>2 &&
                in.substring(in.length()-2,in.length()).equals(".0"))
            deci = 1;
        if (in.length()>3 &&
                in.substring(in.length()-3,in.length()).equals(".00"))
            deci = 2;
        try {
            Double d = Double.parseDouble(in);
            String s = df.format(d);
            if (deci >= 0) {
                s += ".";
                for (int i = 0; i < deci; i++)
                    s += "0";
            }
            changedHere = true;
            editText.setText(s);
            editText.setSelection(s.length());
        } catch (Exception e) {
            Log.e("TextWatch", "afterTextChanged: " + e.getMessage());
        }
    }
}
