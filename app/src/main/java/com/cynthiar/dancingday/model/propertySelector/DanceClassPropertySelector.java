package com.cynthiar.dancingday.model.propertySelector;

import com.cynthiar.dancingday.model.DummyItem;
import com.cynthiar.dancingday.model.DummyUtils;

/**
 * Created by Robert on 07/02/2017.
 */

public interface DanceClassPropertySelector {
    String getProperty(DummyItem dummyItem);
    DummyUtils.IComparer getComparer();
}

