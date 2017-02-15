package com.cynthiar.dancingday.dummy.propertySelector;

import com.cynthiar.dancingday.dummy.DummyContent;
import com.cynthiar.dancingday.dummy.DummyUtils;

/**
 * Created by Robert on 07/02/2017.
 */

public interface DanceClassPropertySelector {
    String getProperty(DummyContent.DummyItem dummyItem);
    DummyUtils.IComparer getComparer();
}

