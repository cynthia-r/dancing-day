package com.cynthiar.dancingday;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;

import com.cynthiar.dancingday.dummy.DummyUtils;

/**
 * Created by CynthiaR on 2/25/2017.
 */

public class SpinnerItemsSelectedListener implements AdapterView.OnItemSelectedListener {

    private SchoolSpinnerAdapter mSpinnerAdapter;
    private Context mContext;
    private MultiDayListViewAdapter mMultiDayListViewAdapter;

    public SpinnerItemsSelectedListener(Context context, SchoolSpinnerAdapter spinnerAdapter, MultiDayListViewAdapter multiDayListViewAdapter) {
        mSpinnerAdapter = spinnerAdapter;
        mContext = context;
        mMultiDayListViewAdapter = multiDayListViewAdapter;
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String groupSelected = mSpinnerAdapter.getItem(position);
        //DummyUtils.toast(mContext, itemSelected);
        mMultiDayListViewAdapter.getFilter().filter(groupSelected);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
