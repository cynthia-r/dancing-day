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
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Robert on 08/02/2017.
 * Implementation of AsyncTask designed to fetch data from the network.
 */
public class DownloadTask extends AsyncTask<String, DownloadTaskProgress, DownloadTask.Result>
    implements IHttpConsumer {
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

    private void setCallback(IDownloadCallback<List<DummyItem>> callback) {
        mCallback = callback;
    }

    public void onProgress(IProgress... progresses) {
        DownloadTaskProgress[] downloadTaskProgresses = new DownloadTaskProgress[progresses.length];
        for (int i=0; i < progresses.length; i++)
            downloadTaskProgresses[i] = (DownloadTaskProgress)progresses[i];
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
        public Exception mException;
        public Result(Pair<String, List<DummyItem>> resultValue) { mResultValue = resultValue; }
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
     * Performs the download in background.
     */
    @Override
    protected DownloadTask.Result doInBackground(String... urls) {
        // Initialize the result
        Result result = null;

        // Start execution
        if (!isCancelled() && urls != null && urls.length > 0) {
            String urlString = urls[0];
            try {
                // Use the HTTP client to perform the request
                URL url = new URL(urlString);
                HttpClient httpClient = new HttpClient(this);
                Object processedResult = httpClient.getResponse(url);

                // Extract the items from the response
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

        // Return the result
        return result;
    }

    /**
     * Updates the IDownloadCallback with the result.
     */
    @Override
    protected void onPostExecute(Result result) {
        if (result != null && mCallback != null) {
            if (result.mException != null)
                mCallback.reportError(result.mException);
            if (result.mResultValue != null) {
                mConsumerCallback.updateFromResult(result.mResultValue);
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
}
