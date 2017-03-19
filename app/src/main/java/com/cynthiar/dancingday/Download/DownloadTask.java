package com.cynthiar.dancingday.download;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v4.util.Pair;

import com.cynthiar.dancingday.data.IConsumerCallback;
import com.cynthiar.dancingday.data.IProgress;
import com.cynthiar.dancingday.dummy.DummyItem;
import com.cynthiar.dancingday.dummy.extractor.DanceClassExtractor;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Robert on 08/02/2017.
 * Implementation of AsyncTask designed to fetch data from the network.
 */
public class DownloadTask extends AsyncTask<String, DownloadTaskProgress, DownloadTask.Result>
    implements IHttpUser {
    private IDownloadCallback<List<DummyItem>> mCallback;
    private IConsumerCallback<Pair<String, List<DummyItem>>> mConsumerCallback;
    private String mKey;
    private DanceClassExtractor mExtractor;

    public DownloadTask(IDownloadCallback<List<DummyItem>> callback,
                 IConsumerCallback<Pair<String, List<DummyItem>>> consumerCallback,
                 String key, DanceClassExtractor danceClassExtractor) {
        setCallback(callback);
        mKey = key;
        mConsumerCallback = consumerCallback;
        mExtractor = danceClassExtractor;
    }

    void setCallback(IDownloadCallback<List<DummyItem>> callback) {
        mCallback = callback;
    }

    public void onProgress(int... progressCodes) {
        DownloadTaskProgress[] downloadTaskProgresses = new DownloadTaskProgress[progressCodes.length];
        for (int i=0; i < progressCodes.length; i++)
            downloadTaskProgresses[i] = new DownloadTaskProgress(progressCodes[i]);
        this.publishProgress(downloadTaskProgresses);
    }

    public Object processStream(InputStream inputStream, URL url){
        try {
            return mExtractor.processDownload(inputStream, url.toString());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Wrapper class that serves as a union of a result value and an exception. When the download
     * task has completed, either the result value or exception can be a non-null value.
     * This allows you to pass exceptions to the UI thread that were thrown during doInBackground().
     */
    public class Result {
        public Pair<String, List<DummyItem>> mResultValue;
        public List<DummyItem> mResultList;
        public Exception mException;
        public Result(Pair<String, List<DummyItem>> resultValue) { mResultValue = resultValue; }
        public Result(List<DummyItem> resultList) { mResultList = resultList; }
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
                mCallback.onProgressUpdate(new DownloadTaskProgress(IProgress.NO_NETWORK_CONNECTION), new DownloadTaskProgress(IProgress.ERROR)); // todo set exception message for no connectivity?
                Result result = new Result(new Pair<String, List<DummyItem>>(mKey, new ArrayList<DummyItem>()));
                mConsumerCallback.updateFromResult(result.mResultValue);
                mCallback.finishDownloading();
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
            String urlString = urls[0];
            try {
                URL url = new URL(urlString);
                HttpClient httpClient = new HttpClient(this);
                Object processedResult = httpClient.getResponse(url);
                /*if (resultString != null) {
                    result = new Result(new Pair<>(mKey, resultString));*/
                if (processedResult != null) {
                    List<DummyItem> dummyItemList = mExtractor.extractItems(processedResult);
                    result = new Result(new Pair<>(mKey, dummyItemList));
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
                mCallback.updateFromDownload(null);
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
            publishProgress(new DownloadTaskProgress(IProgress.PROCESS_INPUT_STREAM_IN_PROGRESS), new DownloadTaskProgress(pct));
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
}
