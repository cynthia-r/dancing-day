package com.cynthiar.dancingday.dummy.propertySelector;

import com.cynthiar.dancingday.dummy.DummyItem;
import com.cynthiar.dancingday.dummy.DummyUtils;
import com.cynthiar.dancingday.dummy.comparer.StringComparer;

public class LevelPropertySelector implements DanceClassPropertySelector {
    public String getProperty(DummyItem dummyItem){
        return dummyItem.level.toString();
    }

    @Override
    public DummyUtils.IComparer getComparer() {
        return new StringComparer(); // TODO
    }
}
