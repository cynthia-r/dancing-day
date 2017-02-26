package com.cynthiar.dancingday.dummy.propertySelector;

import com.cynthiar.dancingday.dummy.DummyContent;
import com.cynthiar.dancingday.dummy.DummyUtils;
import com.cynthiar.dancingday.dummy.comparer.StringComparer;
import com.cynthiar.dancingday.dummy.propertySelector.DanceClassPropertySelector;

public class LevelPropertySelector implements DanceClassPropertySelector {
    public String getProperty(DummyContent.DummyItem dummyItem){
        return dummyItem.level.toString();
    }

    @Override
    public DummyUtils.IComparer getComparer() {
        return new StringComparer(); // TODO
    }
}
