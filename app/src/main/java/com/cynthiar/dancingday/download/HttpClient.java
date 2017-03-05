package com.cynthiar.dancingday.download;

import com.cynthiar.dancingday.data.IProgress;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by CynthiaR on 3/4/2017.
 */

public class HttpClient {

    private IHttpUser mHttpUser;

    public HttpClient(IHttpUser httpUser){
        mHttpUser = httpUser;
    }

    /**
     * Given a URL, sets up a connection and gets the HTTP response body from the server.
     * If the network request is successful, it returns the response body in processed Object form. Otherwise,
     * it will throw an IOException.
     */
    public Object getResponse(URL url) throws IOException {
        InputStream stream = null;
        HttpURLConnection connection = null;
        Object processedInput = null;
        try {
            if (url.getProtocol().equals("https"))
                connection = (HttpsURLConnection) url.openConnection();
            else
                connection = (HttpURLConnection) url.openConnection();
            // Timeout for reading InputStream arbitrarily set to 3000ms.
            connection.setReadTimeout(600000);
            // Timeout for connection.connect() arbitrarily set to 3000ms.
            connection.setConnectTimeout(600000);
            // For this use case, set HTTP method to GET.
            connection.setRequestMethod("GET");
            // Already true by default but setting just in case; needs to be true since this request
            // is carrying an input (response) body.
            connection.setDoInput(true);
            // Open communications link (network traffic occurs here).
            connection.connect();
            mHttpUser.onProgress(IProgress.CONNECT_SUCCESS);
            int responseCode = connection.getResponseCode();
            if (responseCode != HttpsURLConnection.HTTP_OK) {
                throw new IOException("HTTP error code: " + responseCode);
            }
            // Retrieve the response body as an InputStream.
            stream = connection.getInputStream();
            mHttpUser.onProgress(IProgress.GET_INPUT_STREAM_SUCCESS, 0);
            if (stream != null) {
                // Process the stream
                processedInput = mHttpUser.processStream(stream, url);
            }
        } finally {
            // Close the stream
            if (stream != null)
                stream.close();

            // Disconnect HTTPS connection.
            if (connection != null) {
                connection.disconnect();
            }
        }
        return processedInput;
    }
}
