package com.cynthiar.dancingday.filter;

import android.widget.Spinner;

/**
 * Created by Robert on 25/02/2017.
 */

public class MultiDaySpinner {

    protected Spinner mSpinner;
    protected String mPrefix;
    public MultiDaySpinner(Spinner spinner, String prefix) {
        mSpinner = spinner;
        mPrefix = prefix;
    }

    public Spinner getSpinner() {
        return mSpinner;
    }

    public String getPrefix() { return mPrefix; }
}
