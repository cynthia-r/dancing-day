package com.cynthiar.dancingday.dummy.extractor;

import android.content.Context;
import android.support.v4.util.Pair;

import com.cynthiar.dancingday.dummy.DummyContent;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by Robert on 12/02/2017.
 */

public abstract class DanceClassExtractor<T> {

    protected Context mContext;
    protected DanceClassExtractor() {
    }

    public abstract String getKey();

    public abstract String getUrl();

    public abstract T processDownload(InputStream downloadStream, String baseUri) throws IOException;

    public abstract List<DummyContent.DummyItem> Extract(T htmlContent) throws IOException;

    public void setContext(Context context) {
        mContext = context;
    }
}
