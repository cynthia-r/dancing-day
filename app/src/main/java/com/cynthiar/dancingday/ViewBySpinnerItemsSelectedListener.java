package com.cynthiar.dancingday;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.cynthiar.dancingday.dummy.DummyItem;
import com.cynthiar.dancingday.dummy.DummyUtils;
import com.cynthiar.dancingday.dummy.propertySelector.DanceClassPropertySelector;
import com.cynthiar.dancingday.dummy.propertySelector.DayPropertySelector;
import com.cynthiar.dancingday.dummy.propertySelector.LevelPropertySelector;
import com.cynthiar.dancingday.dummy.propertySelector.SchoolPropertySelector;

import java.util.HashMap;
import java.util.List;

/**
 * Created by CynthiaR on 3/5/2017.
 */

public class ViewBySpinnerItemsSelectedListener implements AdapterView.OnItemSelectedListener {
    private Spinner mSpinner;
    private Context mContext;
    private List<DummyItem> mAllItemList;
    private MultiDayListViewAdapter mAdapter;

    public ViewBySpinnerItemsSelectedListener(Context context, Spinner viewBySpinner, List<DummyItem> allItemMap, MultiDayListViewAdapter adapter) {
        mSpinner = viewBySpinner;
        mContext = context;
        mAllItemList = allItemMap;
        mAdapter = adapter;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int selectedPosition = mSpinner.getSelectedItemPosition();

        String viewBySelected = (String)mSpinner.getAdapter().getItem(selectedPosition);
        DanceClassPropertySelector danceClassPropertySelector;
        if (viewBySelected.equals("Day"))
            danceClassPropertySelector = new DayPropertySelector();
        else if (viewBySelected.equals("Level"))
            danceClassPropertySelector = new LevelPropertySelector();
        else if (viewBySelected.equals("School"))
            danceClassPropertySelector = new SchoolPropertySelector();
        else
            return;

        // Group by and sort
        HashMap<String, List<DummyItem>> dummyItemMap = DummyUtils.GroupBy(danceClassPropertySelector, mAllItemList);
        DummyUtils.sortItemMap(dummyItemMap);
        List<String> groupList = DummyUtils.sortAndRotateGroups(mContext, dummyItemMap, danceClassPropertySelector);

        // Update current property selector
        ((TodayActivity)mContext).setCurrentPropertySelector(danceClassPropertySelector);

        // Update values and notify
        mAdapter.setValues(dummyItemMap);
        mAdapter.setGroups(groupList);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
