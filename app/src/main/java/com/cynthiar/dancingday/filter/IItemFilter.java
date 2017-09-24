package com.cynthiar.dancingday.filter;

import com.cynthiar.dancingday.model.DummyItem;

/**
 * Created by CynthiaR on 3/11/2017.
 */

public interface IItemFilter {
    boolean shouldFilter(DummyItem dummyItem);
}
