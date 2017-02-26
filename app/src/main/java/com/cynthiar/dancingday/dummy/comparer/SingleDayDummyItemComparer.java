package com.cynthiar.dancingday.dummy.comparer;

import com.cynthiar.dancingday.dummy.DanceClassTime;
import com.cynthiar.dancingday.dummy.DummyItem;
import com.cynthiar.dancingday.dummy.DummyUtils;

/**
 * Created by Robert on 14/02/2017.
 */

public class SingleDayDummyItemComparer implements DummyUtils.IComparer<DummyItem> {
    @Override
    public int compare(DummyItem elem1, DummyItem elem2) {
        int dayComparisonResult = new DayComparer().compare(elem1.day, elem2.day);

        // Items are on different days
        if (0 != dayComparisonResult)
            return dayComparisonResult;

        // Items are on the same day - compare the start times
        DanceClassTime startClassTime1 = elem1.getClassTime();
        DanceClassTime startClassTime2 = elem2.getClassTime();

        if (null == startClassTime1 && null == startClassTime2)
            return 0;

        if (null == startClassTime1)
            return -1;

        if (null == startClassTime2)
            return 1;

        return startClassTime1.startTime.compareTo(startClassTime2.startTime);
    }
}
