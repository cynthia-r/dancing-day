package com.cynthiar.dancingday.model.comparer;

import com.cynthiar.dancingday.model.time.DanceClassTime;
import com.cynthiar.dancingday.model.DummyItem;
import com.cynthiar.dancingday.model.DummyUtils;

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
        DanceClassTime startClassTime1 = elem1.danceClassTime;
        DanceClassTime startClassTime2 = elem2.danceClassTime;

        if (null == startClassTime1 && null == startClassTime2)
            return 0;

        if (null == startClassTime1)
            return -1;

        if (null == startClassTime2)
            return 1;

        return startClassTime1.startTime.compareTo(startClassTime2.startTime);
    }
}
