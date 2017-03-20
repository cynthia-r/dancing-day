package com.cynthiar.dancingday.download;

import com.cynthiar.dancingday.data.IProgress;

import java.io.InputStream;
import java.net.URL;

/**
 * Created by CynthiaR on 3/4/2017.
 * Represents a consumer of an HTTP call.
 */
public interface IHttpConsumer {
    /**
     * Performs action on progress of the HTTP request.
     * @param progresses: the progresses.
     */
    void onProgress(IProgress... progresses);

    /**
     * Process the HTTP response stream.
     * @param inputStream: the HTTP response stream.
     * @param url: the url.
     * @return: the resulting object.
     */
    Object processStream(InputStream inputStream, URL url);
}
