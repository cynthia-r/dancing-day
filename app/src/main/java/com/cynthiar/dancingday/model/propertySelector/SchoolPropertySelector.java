package com.cynthiar.dancingday.model.propertySelector;

import com.cynthiar.dancingday.model.DummyItem;
import com.cynthiar.dancingday.model.DummyUtils;
import com.cynthiar.dancingday.model.comparer.StringComparer;

public class SchoolPropertySelector implements DanceClassPropertySelector {
    public String getProperty(DummyItem dummyItem){ return dummyItem.school.Key; }

    public DummyUtils.IComparer getComparer() {
        return new StringComparer();
    }
}
