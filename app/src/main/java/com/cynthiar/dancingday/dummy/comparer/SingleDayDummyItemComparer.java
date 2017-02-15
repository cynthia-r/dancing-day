package com.cynthiar.dancingday.dummy.comparer;

import com.cynthiar.dancingday.dummy.DummyContent;
import com.cynthiar.dancingday.dummy.DummyUtils;

/**
 * Created by Robert on 14/02/2017.
 */

public class SingleDayDummyItemComparer implements DummyUtils.IComparer<DummyContent.DummyItem> {
    @Override
    public int compare(DummyContent.DummyItem elem1, DummyContent.DummyItem elem2) {
        return new DayComparer().compare(elem1.day, elem2.day);
    }
}
