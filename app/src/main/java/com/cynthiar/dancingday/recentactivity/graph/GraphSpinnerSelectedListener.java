package com.cynthiar.dancingday.recentactivity.graph;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;

/**
 * Listener for the graph selection dropdown.
 */
public class GraphSpinnerSelectedListener implements AdapterView.OnItemSelectedListener{
    private Context context;
    private int[] numberOfDaysArray = {7, 30, 60};

    public GraphSpinnerSelectedListener(Context context) {
        this.context = context;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int numberOfDays = this.numberOfDaysArray[position];
        GraphInformation graphInformation;
        switch (numberOfDays) {
            case 7:
                graphInformation = new SevenDaysGraphInformation();
                break;
            case 30:
                graphInformation = new ThirtyDaysGraphInformation();
                break;
            case 60:
                graphInformation = new SixtyDaysGraphInformation();
                break;
            default:
                graphInformation = new SevenDaysGraphInformation();
                break;
        }
        ((RecentActivityGraphActivity)this.context).resetGraph(graphInformation);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
