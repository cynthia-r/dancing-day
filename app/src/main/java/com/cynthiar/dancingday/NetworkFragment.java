package com.cynthiar.dancingday;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.cynthiar.dancingday.data.IConsumerCallback;
import com.cynthiar.dancingday.download.DownloadTask;
import com.cynthiar.dancingday.download.IDownloadCallback;
import com.cynthiar.dancingday.dummy.extractor.DanceClassExtractor;

/**
 * Created by Robert on 08/02/2017.
 */

public class NetworkFragment extends Fragment {
    public static final String TAG = "NetworkFragment";

    //private static final String URL_KEY = "UrlKey";

    private IDownloadCallback mDownloadCallback;
    private IConsumerCallback mConsumerCallback;
    private DownloadTask mDownloadTask;
    //private String mUrlString;
    private Context mContext;

    /**
     * Static initializer for NetworkFragment that sets the URL of the host it will be downloading
     * from.
     */
    public static NetworkFragment getInstance(FragmentManager fragmentManager/*, String url*/) {
        // Recover NetworkFragment in case we are re-creating the Activity due to a config change.
        // This is necessary because NetworkFragment might have a task that began running before
        // the config change occurred and has not finished yet.
        // The NetworkFragment is recoverable because it calls setRetainInstance(true).
        NetworkFragment networkFragment = (NetworkFragment) fragmentManager
                .findFragmentByTag(NetworkFragment.TAG);
        if (networkFragment == null) {
            networkFragment = new NetworkFragment();
            /*Bundle args = new Bundle();
            args.putString(URL_KEY, url);
            networkFragment.setArguments(args);*/
            fragmentManager.beginTransaction().add(networkFragment, TAG).commit();
        }
        return networkFragment;
    }

    public void setConsumerCallback(IConsumerCallback consumerCallback) {
        mConsumerCallback = consumerCallback;
    }

    public void setContext(Context context) {
        mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //mUrlString = getArguments().getString(URL_KEY);

        //...

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
     * Start non-blocking execution of DownloadTask.
     */
    public void startDownload(String key, DanceClassExtractor danceClassExtractor) {
        cancelDownload();
        String url = danceClassExtractor.getUrl();
        mDownloadTask = new DownloadTask(mDownloadCallback, mConsumerCallback, key, danceClassExtractor, mContext);
        mDownloadTask.execute(url);
    }

    /**
     * Cancel (and interrupt if necessary) any ongoing DownloadTask execution.
     */
    public void cancelDownload() {
        if (mDownloadTask != null) {
            mDownloadTask.cancel(true);
        }
    }

}
