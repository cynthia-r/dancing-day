package com.cynthiar.dancingday.dummy.propertySelector;

import com.cynthiar.dancingday.dummy.DummyContent;
import com.cynthiar.dancingday.dummy.DummyUtils;
import com.cynthiar.dancingday.dummy.comparer.DayComparer;

public class DayPropertySelector implements DanceClassPropertySelector{
    public String getProperty(DummyContent.DummyItem dummyItem){
        return dummyItem.day;
    }

    @Override
    public DummyUtils.IComparer getComparer() {
        return new DayComparer();
    }
}
