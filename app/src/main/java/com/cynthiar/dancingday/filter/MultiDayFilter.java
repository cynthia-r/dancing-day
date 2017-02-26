package com.cynthiar.dancingday.filter;

import android.widget.Filter;

import com.cynthiar.dancingday.MultiDayFragment;
import com.cynthiar.dancingday.MultiDayListViewAdapter;
import com.cynthiar.dancingday.dummy.DummyContent;
import com.cynthiar.dancingday.dummy.extractor.Extractors;
import com.cynthiar.dancingday.dummy.propertySelector.DanceClassPropertySelector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by CynthiaR on 2/25/2017.
 */

public class MultiDayFilter extends Filter {

    protected MultiDayListViewAdapter mAdapter;
    protected HashMap<String, List<DummyContent.DummyItem>> mUnfilteredValues;

    public MultiDayFilter(MultiDayListViewAdapter adapter, HashMap<String, List<DummyContent.DummyItem>> unfilteredValues) {
        mUnfilteredValues = unfilteredValues;
        mAdapter = adapter;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults filterResults = new FilterResults();

        String filterString = constraint.toString();
        if (null == filterString || filterString.isEmpty()) {
            filterResults.count = mUnfilteredValues.size(); // keyset size?
            filterResults.values = mUnfilteredValues;
            return filterResults;
        }

        HashMap<String, List<DummyContent.DummyItem>> itemToKeepList = new HashMap<>();
        String[] spinnerFilters = filterString.split("\\|"); // TODO separators should be const

        // Loop through each item to see if it should be filtered
        for (String groupKey: mUnfilteredValues.keySet()
                ) {
            List<DummyContent.DummyItem> itemInGroupList = mUnfilteredValues.get(groupKey);
            List<DummyContent.DummyItem> filteredListForGroup = new ArrayList<>();
            for (DummyContent.DummyItem dummyItem: itemInGroupList
                    ) {
                // Check if any filter applies
                boolean shouldFilter = false;
                for (String spinnerFilter:spinnerFilters
                        ) {
                    if (null == spinnerFilter || spinnerFilter.isEmpty())
                        continue;

                    String[] prefixAndGroup = spinnerFilter.split("-");
                    String prefix = prefixAndGroup[0];
                    DanceClassPropertySelector danceClassPropertySelector = Extractors.getInstance().getSelector(prefix);

                    String groupToKeep = prefixAndGroup[1];
                    if (groupToKeep.startsWith(MultiDayFragment.ALL_KEY))
                        continue;

                    if (!danceClassPropertySelector.getProperty(dummyItem).equals(groupToKeep)) {
                        shouldFilter = true;
                        break;
                    }
                }

                // Add the item to the list to keep if no filter applies
                if (!shouldFilter)
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
