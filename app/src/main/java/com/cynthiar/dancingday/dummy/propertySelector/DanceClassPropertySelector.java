package com.cynthiar.dancingday.dummy.propertySelector;

import com.cynthiar.dancingday.dummy.DummyItem;
import com.cynthiar.dancingday.dummy.DummyUtils;

/**
 * Created by Robert on 07/02/2017.
 */

public interface DanceClassPropertySelector {
    String getProperty(DummyItem dummyItem);
    DummyUtils.IComparer getComparer();
}

