package com.cynthiar.dancingday.model.comparer;

import com.cynthiar.dancingday.model.DummyUtils;

/**
 * Created by Robert on 14/02/2017.
 */

public class StringComparer implements DummyUtils.IComparer<String> {
    @Override
    public int compare(String elem1, String elem2) {
        return elem1.compareTo(elem2);
    }
}
