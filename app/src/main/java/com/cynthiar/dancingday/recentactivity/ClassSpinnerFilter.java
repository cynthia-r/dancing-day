package com.cynthiar.dancingday.recentactivity;

import android.widget.Filter;

import com.cynthiar.dancingday.model.DummyItem;
import com.cynthiar.dancingday.model.DummyUtils;
import com.cynthiar.dancingday.model.classActivity.ClassActivity;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

/**
 * Filter for the class spinner.
 */

public class ClassSpinnerFilter extends Filter {
    public final static String FILTER_SEPARATOR = "|";
    public final static String FILTER_SEPARATOR_REGEX = "\\|";

    private ClassSpinnerAdapter spinnerAdapter;
    private List<DummyItem> unfilteredValues;

    public ClassSpinnerFilter(ClassSpinnerAdapter spinnerAdapter, List<DummyItem> unfilteredValues) {
        this.spinnerAdapter = spinnerAdapter;
        this.unfilteredValues = unfilteredValues;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults filterResults = new FilterResults();

        // Return early if no constraint
        String filterString = constraint.toString();
        if (null == filterString || filterString.isEmpty()) {
            filterResults.count = unfilteredValues.size();
            filterResults.values = unfilteredValues;
            return filterResults;
        }

        // Initialize the list of items to keep
        List<DummyItem> itemToKeepList = new ArrayList<>();

        // Retrieve the filters
        String[] viewFilters = filterString.split(ClassSpinnerFilter.FILTER_SEPARATOR_REGEX);
        String schoolFilter = viewFilters[0];
        DateTime dateFilter = DateTime.parse(viewFilters[1], ClassActivity.dateTimeFormatter);

        // Filter by school and day
        for (DummyItem danceClass:unfilteredValues
             ) {
            if (danceClass.school.Key.equals(schoolFilter)
                && danceClass.day.equals(DummyUtils.getDayOfTheWeekName(dateFilter.getDayOfWeek()))) {
                itemToKeepList.add(danceClass);
            }
        }

        // Set and return the filtered results
        filterResults.count = itemToKeepList.size();
        filterResults.values = itemToKeepList;
        return filterResults;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        // Set the values
        spinnerAdapter.setValues((List<DummyItem>)results.values);
        spinnerAdapter.notifyDataSetChanged();
    }
}
