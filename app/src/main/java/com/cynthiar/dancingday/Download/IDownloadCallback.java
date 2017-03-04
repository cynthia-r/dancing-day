package com.cynthiar.dancingday.download;

import android.net.NetworkInfo;

/**
 * Created by Robert on 08/02/2017.
 */
public interface IDownloadCallback<T> {
    /**
     * Indicates that the callback handler needs to update its appearance or information based on
     * the result of the task. Expected to be called from the main thread.
     */
    void updateFromDownload(T result);

    /**
     * Get the device's active network status in the form of a NetworkInfo object.
     */
    NetworkInfo getActiveNetworkInfo();

    /**
     * Indicate to callback handler any progress update.
     * @param progressCode must be one of the constants defined in IDownloadCallback.Progress.
     * @param percentComplete must be 0-100.
     */
    void onProgressUpdate(DownloadTaskProgress progressCode, DownloadTaskProgress percentComplete);

    /**
     * Indicates that the download operation has finished. This method is called even if the
     * download hasn't completed successfully.
     */
    void finishDownloading();
}
