package com.cynthiar.dancingday;

import android.widget.Adapter;
import android.widget.Filter;

import com.cynthiar.dancingday.dummy.DummyContent;
import com.cynthiar.dancingday.dummy.propertySelector.DanceClassPropertySelector;
import com.cynthiar.dancingday.dummy.propertySelector.SchoolPropertySelector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by CynthiaR on 2/25/2017.
 */

public class MultiDayFilter extends Filter {

    protected MultiDayListViewAdapter mAdapter;
    protected DanceClassPropertySelector mDanceClassPropertySelector = new SchoolPropertySelector();
    protected HashMap<String, List<DummyContent.DummyItem>> mUnfilteredValues;

    public MultiDayFilter(MultiDayListViewAdapter adapter, HashMap<String, List<DummyContent.DummyItem>> unfilteredValues) {
        mUnfilteredValues = unfilteredValues;
        mAdapter = adapter;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults filterResults = new FilterResults();

        String groupToKeep = constraint.toString();
        HashMap<String, List<DummyContent.DummyItem>> itemToKeepList = new HashMap<>();

        if (groupToKeep.equals("All")) {// TODO const
            filterResults.count = mUnfilteredValues.size(); // keyset size?
            filterResults.values = mUnfilteredValues;
            return filterResults;
        }

        for (String groupKey: mUnfilteredValues.keySet()
             ) {
            List<DummyContent.DummyItem> itemInGroupList = mUnfilteredValues.get(groupKey);
            List<DummyContent.DummyItem> filteredListForGroup = new ArrayList<>();
            for (DummyContent.DummyItem dummyItem: itemInGroupList
                    ) {
                if (mDanceClassPropertySelector.getProperty(dummyItem).equals(groupToKeep))
                    filteredListForGroup.add(dummyItem);
            }
            itemToKeepList.put(groupKey, filteredListForGroup);
        }
        filterResults.count = itemToKeepList.size(); // keyset size?
        filterResults.values = itemToKeepList;
        return filterResults;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        mAdapter.setValues((HashMap<String, List<DummyContent.DummyItem>>)results.values);
        mAdapter.notifyDataSetChanged();
    }
}
