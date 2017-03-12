package com.cynthiar.dancingday.filter;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.cynthiar.dancingday.MultiDayListViewAdapter;

/**
 * Created by CynthiaR on 2/25/2017.
 */

public class SpinnerItemsSelectedListener implements AdapterView.OnItemSelectedListener {

    private MultiDaySpinner[] mSpinners;
    private MultiDayListViewAdapter mMultiDayListViewAdapter;

    public SpinnerItemsSelectedListener(MultiDaySpinner[] spinners, MultiDayListViewAdapter multiDayListViewAdapter) {
        mMultiDayListViewAdapter = multiDayListViewAdapter;
        mSpinners = spinners;
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String filterString = "";
        for (MultiDaySpinner multiDaySpinner:mSpinners
             ) {
            Spinner spinner = multiDaySpinner.getSpinner();
            int selectedPosition = spinner.getSelectedItemPosition();
            String groupSelected = (String)spinner.getAdapter().getItem(selectedPosition);
            String filterStringForSpinner = multiDaySpinner.getPrefix().concat("-").concat(groupSelected);
            filterString = filterString.concat(MultiDayFilter.FILTER_SEPARATOR).concat(filterStringForSpinner);
        }

        mMultiDayListViewAdapter.getFilter().filter(filterString);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
