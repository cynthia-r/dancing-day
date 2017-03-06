package com.cynthiar.dancingday.dummy.propertySelector;

import com.cynthiar.dancingday.dummy.DummyItem;
import com.cynthiar.dancingday.dummy.DummyUtils;
import com.cynthiar.dancingday.dummy.comparer.StringComparer;

public class SchoolPropertySelector implements DanceClassPropertySelector {
    public String getProperty(DummyItem dummyItem){ return dummyItem.school.Key; }

    public DummyUtils.IComparer getComparer() {
        return new StringComparer();
    }
}
