package com.cynthiar.dancingday.dummy.propertySelector;

import com.cynthiar.dancingday.dummy.DummyItem;
import com.cynthiar.dancingday.dummy.DummyUtils;
import com.cynthiar.dancingday.dummy.comparer.DayComparer;

public class DayPropertySelector implements DanceClassPropertySelector{
    public String getProperty(DummyItem dummyItem){
        return dummyItem.day;
    }

    public DummyUtils.IComparer getComparer() {
        return new DayComparer();
    }
}
