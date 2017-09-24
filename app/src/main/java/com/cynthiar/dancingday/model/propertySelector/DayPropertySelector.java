package com.cynthiar.dancingday.model.propertySelector;

import com.cynthiar.dancingday.model.DummyItem;
import com.cynthiar.dancingday.model.DummyUtils;
import com.cynthiar.dancingday.model.comparer.DayComparer;

public class DayPropertySelector implements DanceClassPropertySelector{
    public String getProperty(DummyItem dummyItem){
        return dummyItem.day;
    }

    public DummyUtils.IComparer getComparer() {
        return new DayComparer();
    }
}
