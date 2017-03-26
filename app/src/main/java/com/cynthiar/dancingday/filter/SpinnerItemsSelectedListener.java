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

    private MultiDayFilterState mFilterState;
    private MultiDayListViewAdapter mMultiDayListViewAdapter;

    public SpinnerItemsSelectedListener(MultiDayFilterState filterState, MultiDayListViewAdapter multiDayListViewAdapter) {
        mMultiDayListViewAdapter = multiDayListViewAdapter;
        mFilterState = filterState;
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String filterString = mFilterState.getFilterString();
        mMultiDayListViewAdapter.getFilter().filter(filterString);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
