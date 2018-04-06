package com.cynthiar.dancingday.recentactivity;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;

/**
 * Created by CynthiaR on 3/25/2018.
 */

public class GraphSpinnerSelectedListener implements AdapterView.OnItemSelectedListener{
    private Context context;
    private int[] numberOfDaysArray = {7, 30, 60, 90};

    public GraphSpinnerSelectedListener(Context context) {
        this.context = context;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int numberOfDays = this.numberOfDaysArray[position];
        GraphInformation graphInformation =  (7 != numberOfDays)
                ? ((30 == numberOfDays) ? new ThirtyDaysGraphInformation() : new SixtyDaysGraphInformation())
                : new SevenDaysGraphInformation();
        ((RecentActivityGraphActivity)this.context).resetGraph(graphInformation);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
