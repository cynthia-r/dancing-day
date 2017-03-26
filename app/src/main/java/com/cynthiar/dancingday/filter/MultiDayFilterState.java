package com.cynthiar.dancingday.filter;

import android.widget.ImageButton;
import android.widget.Spinner;

import com.cynthiar.dancingday.MultiDayFragment;

/**
 * Created by CynthiaR on 3/25/2017.
 */

public class MultiDayFilterState {
    private MultiDaySpinner[] mSpinners;
    private ImageButton mStarFilter;

    public MultiDayFilterState(MultiDaySpinner[] multiDaySpinners, ImageButton starFilter) {
        mSpinners = multiDaySpinners;
        mStarFilter = starFilter;
    }

    public String getFilterString() {
        // Initialize the filter string
        String filterString = "";

        // Add the state of each spinner
        for (MultiDaySpinner multiDaySpinner:mSpinners
                ) {
            Spinner spinner = multiDaySpinner.getSpinner();
            int selectedPosition = spinner.getSelectedItemPosition();
            String groupSelected = (String)spinner.getAdapter().getItem(selectedPosition);
            String filterStringForSpinner = multiDaySpinner.getPrefix().concat("-").concat(groupSelected);
            filterString = filterString.concat(MultiDayFilter.FILTER_SEPARATOR).concat(filterStringForSpinner);
        }

        // Add the state of the star button
        String filterStringForFavorites = MultiDayFragment.FAVORITE_BUTTON_PREFIX.concat("-")
                .concat(mStarFilter.isPressed() ? "1" : "0");
        filterString = filterString.concat(MultiDayFilter.FILTER_SEPARATOR).concat(filterStringForFavorites);

        // Return the filter string
        return filterString;
    }
}
