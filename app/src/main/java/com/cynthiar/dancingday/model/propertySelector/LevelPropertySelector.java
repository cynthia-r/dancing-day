package com.cynthiar.dancingday.model.propertySelector;

import com.cynthiar.dancingday.model.DummyItem;
import com.cynthiar.dancingday.model.DummyUtils;
import com.cynthiar.dancingday.model.comparer.StringComparer;

public class LevelPropertySelector implements DanceClassPropertySelector {
    public String getProperty(DummyItem dummyItem){
        return dummyItem.level.toString();
    }

    public DummyUtils.IComparer getComparer() {
        return new StringComparer(); // TODO
    }
}
