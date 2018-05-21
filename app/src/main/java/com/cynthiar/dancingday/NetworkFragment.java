package com.cynthiar.dancingday;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.cynthiar.dancingday.dataprovider.IConsumerCallback;
import com.cynthiar.dancingday.dataprovider.IProgress;
import com.cynthiar.dancingday.download.DownloadTask;
import com.cynthiar.dancingday.download.DownloadTaskProgress;
import com.cynthiar.dancingday.download.IDownloadCallback;
import com.cynthiar.dancingday.extractor.DanceClassExtractor;
import com.cynthiar.dancingday.model.DummyItem;
import com.cynthiar.dancingday.model.DummyUtils;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Robert on 08/02/2017.
 */

/**
 * Network fragment used for downloading.
 */
public class NetworkFragment extends Fragment
        implements IDownloadCallback<String, List<DummyItem>> {

    public static final String TAG = "NetworkFragment";

    private IConsumerCallback mConsumerCallback;
    private Context mContext;

    // Boolean telling us whether a download is in progress, so we don't trigger overlapping
    // downloads with consecutive button clicks.
    //private boolean mDownloading = false;
    private HashMap<String, Boolean> mDownloadingMap = new HashMap<>();

    private HashMap<String, DownloadTask> mDownloadTaskMap = new HashMap<>();

    /**
     * Static initializer for NetworkFragment that sets the URL of the host it will be downloading
     * from.
     */
    public static NetworkFragment getInstance(FragmentManager fragmentManager, Context context, IConsumerCallback consumerCallback) {
        // Recover NetworkFragment in case we are re-creating the Activity due to a config change.
        // This is necessary because NetworkFragment might have a task that began running before
        // the config change occurred and has not finished yet.
        // The NetworkFragment is recoverable because it calls setRetainInstance(true).
        NetworkFragment networkFragment = (NetworkFragment) fragmentManager
                .findFragmentByTag(NetworkFragment.TAG);
        if (networkFragment == null) {
            networkFragment = new NetworkFragment();
            networkFragment.setMembers(context, consumerCallback);
            fragmentManager.beginTransaction().add(networkFragment, TAG).commit();
        }
        return networkFragment;
    }

    private void setMembers(Context context, IConsumerCallback consumerCallback) {
        mContext = context;
        mConsumerCallback = consumerCallback;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retain this Fragment across configuration changes in the host Activity.
        setRetainInstance(true);
    }

    @Override
    public void onDestroy() {
        // Cancel task when Fragment is destroyed.
        for (String downloadTaskKey : mDownloadingMap.keySet()) {
            cancelDownload(downloadTaskKey);
        }

        super.onDestroy();
    }

    /**
     * Starts non-blocking execution of DownloadTask.
     */
    public void startDownload(String key, DanceClassExtractor danceClassExtractor) {
        if (mDownloadingMap.containsKey(key) && mDownloadingMap.get(key)) {
            return;
        }
        cancelDownload(key);
        String url = danceClassExtractor.getUrl();
        DownloadTask downloadTask = new DownloadTask(this, mConsumerCallback, key, danceClassExtractor);
        mDownloadTaskMap.put(key, downloadTask);
        downloadTask.execute(url);
        mDownloadingMap.put(key, true);
    }

    /**
     * Cancels (and interrupts if necessary) any ongoing DownloadTask execution.
     */
    public void cancelDownload(String key) {
        if (mDownloadTaskMap.containsKey(key)) {
            mDownloadTaskMap.get(key).cancel(true);
        }
    }

    public void finishDownloading(String key) {
        mDownloadingMap.put(key, false);
        cancelDownload(key);
    }

    public void updateFromDownload(List<DummyItem> result) {
        // Do nothing
    }

    public NetworkInfo getActiveNetworkInfo() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo;
    }

    public void onProgressUpdate(DownloadTaskProgress taskProgress, DownloadTaskProgress percentComplete) {
        switch(taskProgress.getProgressCode()) {
            // You can add UI behavior for progress updates here.
            case IProgress.ERROR:
                DummyUtils.toast(mContext, "Error happened during downloading");
                break;
            case IProgress.CONNECT_SUCCESS:

                break;
            case IProgress.GET_INPUT_STREAM_SUCCESS:

                break;
            case IProgress.PROCESS_INPUT_STREAM_IN_PROGRESS:

                break;
            case IProgress.PROCESS_INPUT_STREAM_SUCCESS:

                break;
            case IProgress.NO_NETWORK_CONNECTION:
                DummyUtils.toast(mContext, "No network connection");
                break;
        }
    }

    public void reportError(Exception exception) {
        DummyUtils.toast(mContext, exception.getMessage());
    }
}
