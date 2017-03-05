package com.cynthiar.dancingday.download;

import com.cynthiar.dancingday.data.IProgress;

import java.io.InputStream;
import java.net.URL;

/**
 * Created by CynthiaR on 3/4/2017.
 */

public interface IHttpUser {
    void onProgress(int... progressCode);

    Object processStream(InputStream inputStream, URL url);
}
