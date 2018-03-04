package com.cynthiar.dancingday;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.cynthiar.dancingday.dataprovider.IConsumerCallback;
import com.cynthiar.dancingday.download.DownloadTask;
import com.cynthiar.dancingday.download.IDownloadCallback;
import com.cynthiar.dancingday.extractor.DanceClassExtractor;

/**
 * Created by Robert on 08/02/2017.
 */

/**
 * Network fragment used for downloading.
 */
public class NetworkFragment extends Fragment {
    public static final String TAG = "NetworkFragment";

    private IDownloadCallback mDownloadCallback;
    private IConsumerCallback mConsumerCallback;
    private DownloadTask mDownloadTask;

    /**
     * Static initializer for NetworkFragment that sets the URL of the host it will be downloading
     * from.
     */
    public static NetworkFragment getInstance(FragmentManager fragmentManager, IConsumerCallback consumerCallback) {
        // Recover NetworkFragment in case we are re-creating the Activity due to a config change.
        // This is necessary because NetworkFragment might have a task that began running before
        // the config change occurred and has not finished yet.
        // The NetworkFragment is recoverable because it calls setRetainInstance(true).
        NetworkFragment networkFragment = (NetworkFragment) fragmentManager
                .findFragmentByTag(NetworkFragment.TAG);
        if (networkFragment == null) {
            networkFragment = new NetworkFragment();
            networkFragment.setConsumerCallback(consumerCallback);
            fragmentManager.beginTransaction().add(networkFragment, TAG).commit();
        }
        return networkFragment;
    }

    private void setConsumerCallback(IConsumerCallback consumerCallback) {
        mConsumerCallback = consumerCallback;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retain this Fragment across configuration changes in the host Activity.
        setRetainInstance(true);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Host Activity will handle callbacks from task.
        mDownloadCallback = (IDownloadCallback) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        // Clear reference to host Activity to avoid memory leak.
        mDownloadCallback = null;
    }

    @Override
    public void onDestroy() {
        // Cancel task when Fragment is destroyed.
        cancelDownload();
        super.onDestroy();
    }

    /**
     * Starts non-blocking execution of DownloadTask.
     */
    public void startDownload(String key, DanceClassExtractor danceClassExtractor) {
        cancelDownload();
        String url = danceClassExtractor.getUrl();
        mDownloadTask = new DownloadTask(mDownloadCallback, mConsumerCallback, key, danceClassExtractor);
        mDownloadTask.execute(url);
    }

    /**
     * Cancels (and interrupts if necessary) any ongoing DownloadTask execution.
     */
    public void cancelDownload() {
        if (mDownloadTask != null) {
            mDownloadTask.cancel(true);
        }
    }
}
