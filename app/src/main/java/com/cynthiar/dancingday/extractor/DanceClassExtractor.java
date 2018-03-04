package com.cynthiar.dancingday.extractor;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
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

    protected abstract ExtractorResults extract(T htmlContent) throws IOException;

    protected void initializeExtraction() {}

    public ExtractorResults extractItems(T htmlContent) throws IOException {
        initializeExtraction();
        return extract(htmlContent);
    }

    public Certificate getCertificate() throws IOException, CertificateException {
        return null;
    }
}
