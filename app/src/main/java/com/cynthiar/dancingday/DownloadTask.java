package com.cynthiar.dancingday;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import com.cynthiar.dancingday.dummy.DummyContent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Robert on 08/02/2017.
 * Implementation of AsyncTask designed to fetch data from the network.
 */
public class DownloadTask extends AsyncTask<String, DownloadTaskProgress, DownloadTask.Result> {
    private IDownloadCallback<List<DummyContent.DummyItem>> mCallback;
    private IConsumerCallback<String> mConsumerCallback;
    private String mUrl;

   // private DataCache<List<DummyContent.DummyItem>> mDanceClassCache;

    DownloadTask(IDownloadCallback<List<DummyContent.DummyItem>> callback,
                 IConsumerCallback<String> consumerCallback,
                 String url) {
        setCallback(callback);
        mUrl = url;
        mConsumerCallback = consumerCallback;

        // Setup cache
        /*DataProvider<List<DummyContent.DummyItem>> danceClassDataProvider = new DanceClassDataProvider(this);
        mDanceClassCache = new DataCache<List<DummyContent.DummyItem>>(danceClassDataProvider);*/
    }

    void setCallback(IDownloadCallback<List<DummyContent.DummyItem>> callback) {
        mCallback = callback;
    }

    /**
     * Wrapper class that serves as a union of a result value and an exception. When the download
     * task has completed, either the result value or exception can be a non-null value.
     * This allows you to pass exceptions to the UI thread that were thrown during doInBackground().
     */
    static class Result {
        public String mResultValue;
        public List<DummyContent.DummyItem> mResultList;
        public Exception mException;
        public Result(String resultValue) { mResultValue = resultValue; }
        public Result(List<DummyContent.DummyItem> resultList) { mResultList = resultList; }
        public Result(Exception exception) {
            mException = exception;
        }
    }

    /**
     * Cancel background network operation if we do not have network connectivity.
     */
    @Override
    protected void onPreExecute() {
        if (mCallback != null) {
            NetworkInfo networkInfo = mCallback.getActiveNetworkInfo();
            if (networkInfo == null || !networkInfo.isConnected() ||
                    (networkInfo.getType() != ConnectivityManager.TYPE_WIFI
                            && networkInfo.getType() != ConnectivityManager.TYPE_MOBILE)) {
                // If no connectivity, cancel task and update Callback with null data.
                mCallback.updateFromDownload(null); // todo set exception message for no connectivity?
                cancel(true);
            }
        }
    }

    /**
     * Defines work to perform on the background thread.
     */
    @Override
    protected DownloadTask.Result doInBackground(String... urls) {
        Result result = null;
        if (!isCancelled() && urls != null && urls.length > 0) {
        //if (!isCancelled()) {
            String urlString = urls[0];
            try {
                URL url = new URL(urlString);
                String resultString = downloadUrl();
                if (resultString != null) {
                    result = new Result(resultString);
                } else {
                    throw new IOException("No response received.");
                }
            } catch(Exception e) {
                result = new Result(e);
            }
        }
        return result;
    }

    /**
     * Updates the IDownloadCallback with the result.
     */
    @Override
    protected void onPostExecute(Result result) {
        if (result != null && mCallback != null) {
            if (result.mException != null) {
                //mCallback.updateFromDownload(result.mException.getMessage());
                mCallback.updateFromDownload(null);
            } else if (result.mResultValue != null) {
                mCallback.updateFromDownload(result.mResultList);
                mConsumerCallback.updateFromResult(result.mResultValue);
            }else if (result.mResultList != null) {
                mCallback.updateFromDownload(result.mResultList);
                //mConsumerCallback.updateFromResult(result.mResultList);
            }
            mCallback.finishDownloading();
        }
    }

    /**
     * Override to add special behavior for cancelled AsyncTask.
     */
    @Override
    protected void onCancelled(Result result) {
    }

    /**
     * Given a URL, sets up a connection and gets the HTTP response body from the server.
     * If the network request is successful, it returns the response body in String form. Otherwise,
     * it will throw an IOException.
     */
    //public String downloadUrl(URL url) throws IOException {
    public String downloadUrl() throws IOException {
        InputStream stream = null;
        HttpsURLConnection connection = null;
        String result = null;
        try {
            URL url = new URL(mUrl);
            connection = (HttpsURLConnection) url.openConnection();
            // Timeout for reading InputStream arbitrarily set to 3000ms.
            connection.setReadTimeout(3000);
            // Timeout for connection.connect() arbitrarily set to 3000ms.
            connection.setConnectTimeout(3000);
            // For this use case, set HTTP method to GET.
            connection.setRequestMethod("GET");
            // Already true by default but setting just in case; needs to be true since this request
            // is carrying an input (response) body.
            connection.setDoInput(true);
            // Open communications link (network traffic occurs here).
            connection.connect();
            this.publishProgress(new DownloadTaskProgress(IDownloadCallback.Progress.CONNECT_SUCCESS));
            int responseCode = connection.getResponseCode();
            if (responseCode != HttpsURLConnection.HTTP_OK) {
                throw new IOException("HTTP error code: " + responseCode);
            }
            // Retrieve the response body as an InputStream.
            stream = connection.getInputStream();
            publishProgress(new DownloadTaskProgress(IDownloadCallback.Progress.GET_INPUT_STREAM_SUCCESS), new DownloadTaskProgress(0));
            if (stream != null) {
                // Converts Stream to String
                result = readAllStream(stream);
            }
        } finally {
            // Close Stream and disconnect HTTPS connection.
            if (stream != null) {
                stream.close();
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
        return result;
    }

    /**
     * Converts the contents of an InputStream to a String.
     */
    private String readStream(InputStream stream, int maxLength) throws IOException {
        String result = null;
        // Read InputStream using the UTF-8 charset.
        InputStreamReader reader = new InputStreamReader(stream, "UTF-8");
        // Create temporary buffer to hold Stream data with specified max length.
        char[] buffer = new char[maxLength];
        // Populate temporary buffer with Stream data.
        int numChars = 0;
        int readSize = 0;
        while (numChars < maxLength && readSize != -1) {
            numChars += readSize;
            int pct = (100 * numChars) / maxLength;
            publishProgress(new DownloadTaskProgress(IDownloadCallback.Progress.PROCESS_INPUT_STREAM_IN_PROGRESS), new DownloadTaskProgress(pct));
            readSize = reader.read(buffer, numChars, buffer.length - numChars);
        }
        if (numChars != -1) {
            // The stream was not empty.
            // Create String that is actual length of response body if actual length was less than
            // max length.
            numChars = Math.min(numChars, maxLength);
            result = new String(buffer, 0, numChars);
        }
        return result;
    }

    private String readAllStream(InputStream inputStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder out = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            out.append(line);
        }
        return out.toString();
    }
}
