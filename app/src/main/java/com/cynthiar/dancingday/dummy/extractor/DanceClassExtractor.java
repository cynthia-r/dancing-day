package com.cynthiar.dancingday.dummy.extractor;

import android.support.v4.util.Pair;

import com.cynthiar.dancingday.dummy.DummyContent;

import java.util.List;

/**
 * Created by Robert on 12/02/2017.
 */

public abstract class DanceClassExtractor {

    protected DanceClassExtractor() {
    }

    public abstract String getKey();

    public abstract String getUrl();

    public abstract List<DummyContent.DummyItem> Extract(String htmlContent);
}
