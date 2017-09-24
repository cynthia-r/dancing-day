package com.cynthiar.dancingday.model.comparer;

import com.cynthiar.dancingday.model.DummyUtils;

/**
 * Created by Robert on 14/02/2017.
 */

public class IntComparer implements DummyUtils.IComparer<Integer> {

    @Override
    public int compare(Integer elem1, Integer elem2) {
        if (elem1 == elem2)
            return 0;
        if (elem1 < elem2)
            return -1;
        return 1;
    }
}
