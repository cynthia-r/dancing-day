package com.cynthiar.dancingday.filter;

import android.content.Context;
import android.widget.ExpandableListView;
import android.widget.Filter;

import com.cynthiar.dancingday.MultiDayFragment;
import com.cynthiar.dancingday.MultiDayListViewAdapter;
import com.cynthiar.dancingday.R;
import com.cynthiar.dancingday.TodayActivity;
import com.cynthiar.dancingday.dummy.DummyItem;
import com.cynthiar.dancingday.dummy.DummyUtils;
import com.cynthiar.dancingday.dummy.extractor.Extractors;
import com.cynthiar.dancingday.dummy.propertySelector.DanceClassPropertySelector;
import com.cynthiar.dancingday.dummy.propertySelector.DayPropertySelector;
import com.cynthiar.dancingday.dummy.propertySelector.LevelPropertySelector;
import com.cynthiar.dancingday.dummy.propertySelector.SchoolPropertySelector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by CynthiaR on 2/25/2017.
 */

public class MultiDayFilter extends Filter {

    public final static String FILTER_SEPARATOR = "|";
    public final static String FILTER_SEPARATOR_REGEX = "\\|";

    protected MultiDayListViewAdapter mAdapter;
    protected HashMap<String, List<DummyItem>> mUnfilteredValues;
    private Context mContext;
    private List<String> mGroupList = new ArrayList<>();

    public MultiDayFilter(Context context, MultiDayListViewAdapter adapter, HashMap<String, List<DummyItem>> unfilteredValues) {
        mUnfilteredValues = unfilteredValues;
        mAdapter = adapter;
        mContext = context;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults filterResults = new FilterResults();

        String filterString = constraint.toString();
        if (null == filterString || filterString.isEmpty()) {
            filterResults.count = mUnfilteredValues.size();
            filterResults.values = mUnfilteredValues;
            return filterResults;
        }

        HashMap<String, List<DummyItem>> itemToKeepMap = new HashMap<>();
        String[] spinnerFilters = filterString.split(MultiDayFilter.FILTER_SEPARATOR_REGEX);

        // Retrieve the view by spinner
        String viewBySelected = null;
        List<SpinnerFilter> spinnerFilterList = new ArrayList<>();
        for (String spinnerFilter:spinnerFilters
             ) {
            if (null == spinnerFilter || spinnerFilter.isEmpty())
                continue;

            // Extract the spinner prefix and group
            String[] prefixAndGroup = spinnerFilter.split("-");
            String prefix = prefixAndGroup[0];
            String groupToKeep = prefixAndGroup[1];

            // Check if the spinner is the view by
            if (null == viewBySelected && prefix.equals(MultiDayFragment.VIEW_BY_SPINNER_PREFIX)) {
                viewBySelected = groupToKeep;
                continue;
            }
            else
                spinnerFilterList.add(new SpinnerFilter(prefix, groupToKeep));
        }

        // Flatten the list
        List<DummyItem> allItemList = new ArrayList<>();
        for (List<DummyItem> groupItemList:mUnfilteredValues.values()
             ) {
            allItemList.addAll(groupItemList);
        }

        // Group by the view by selected
        if (null != viewBySelected && !viewBySelected.isEmpty()) {
            // Get the property selector
            DanceClassPropertySelector viewBySelector = null;
            if (viewBySelected.equals("Day"))
                viewBySelector = new DayPropertySelector();
            else if (viewBySelected.equals("Level"))
                viewBySelector = new LevelPropertySelector();
            else if (viewBySelected.equals("School"))
                viewBySelector = new SchoolPropertySelector();

            // Group by, filter and sort
            if (null != viewBySelector) {
                SpinnerItemFilter spinnerItemFilter = new SpinnerItemFilter(spinnerFilterList);
                itemToKeepMap = DummyUtils.GroupByWithFilter(viewBySelector, allItemList, spinnerItemFilter);
                DummyUtils.sortItemMap(itemToKeepMap);
                mGroupList = DummyUtils.sortAndRotateGroups(mContext, itemToKeepMap, viewBySelector);

                // Update current property selector
                ((TodayActivity)mContext).setCurrentPropertySelector(viewBySelector);
            }
        }

        // Print if mGroupList has no elements // TODO check when valid to print
        /*if (0 == mGroupList.size())
            DummyUtils.toast(mContext, "No groups found");*/

        // Set the results and return
        filterResults.count = itemToKeepMap.size();
        filterResults.values = itemToKeepMap;
        return filterResults;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        // Set the groups and values
        mAdapter.setGroups(mGroupList);
        mAdapter.setValues((HashMap<String, List<DummyItem>>)results.values);
        mAdapter.notifyDataSetChanged();

        // Expands the groups
        ExpandableListView mListView = (ExpandableListView) ((TodayActivity)mContext).findViewById(R.id.multi_day_list_view);
        int groupCount = mGroupList.size();
        for (int i=0; i < groupCount; i++) {
            mListView.expandGroup(i);
        }
    }
}
