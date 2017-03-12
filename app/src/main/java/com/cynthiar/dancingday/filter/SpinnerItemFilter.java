package com.cynthiar.dancingday.filter;

import com.cynthiar.dancingday.MultiDayFragment;
import com.cynthiar.dancingday.dummy.DummyItem;
import com.cynthiar.dancingday.dummy.extractor.Extractors;
import com.cynthiar.dancingday.dummy.propertySelector.DanceClassPropertySelector;

import java.util.List;

/**
 * Created by CynthiaR on 3/11/2017.
 */

public class SpinnerItemFilter implements IItemFilter {

    private List<SpinnerFilter> mSpinnerFilterList;

    public SpinnerItemFilter(List<SpinnerFilter> spinnerFilters) {
        mSpinnerFilterList = spinnerFilters;
    }

    @Override
    public boolean shouldFilter(DummyItem dummyItem) {
        // Check if any filter applies
        boolean shouldFilter = false;
        for (SpinnerFilter spinnerFilter: mSpinnerFilterList
                ) {
            if (null == spinnerFilter || null == spinnerFilter.groupToKeep || spinnerFilter.groupToKeep.isEmpty())
                continue;

            String groupToKeep = spinnerFilter.groupToKeep;

            // Check if the spinner asks for all results
            if (groupToKeep.startsWith(MultiDayFragment.ALL_KEY))
                continue;

            // Compare the property to the one specified in the filter
            DanceClassPropertySelector danceClassPropertySelector = Extractors.getInstance().getSelector(spinnerFilter.prefix);
            if (!danceClassPropertySelector.getProperty(dummyItem).equals(groupToKeep)) {
                shouldFilter = true;
                break;
            }
        }
        return shouldFilter;
    }
}
