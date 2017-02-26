package com.cynthiar.dancingday.dummy.extractor;

import android.content.Context;

import com.cynthiar.dancingday.dummy.DummyItem;

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

    protected DanceClassExtractor(Context context) {
        mContext = context;
    }

    public abstract String getKey();

    public abstract List<String> getSchoolList();

    public abstract String getUrl();

    public abstract T processDownload(InputStream downloadStream, String baseUri) throws IOException;

    public abstract List<DummyItem> Extract(T htmlContent) throws IOException;
}
