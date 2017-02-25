package com.cynthiar.dancingday.download;

/**
 * Created by Robert on 08/02/2017.
 */

public class DownloadTaskProgress implements IDownloadCallback.Progress {
    private int mProgressCode;
    public DownloadTaskProgress(int progressCode) {
        mProgressCode = progressCode;
    }

    public int GetProgressCode() {
        return mProgressCode;
    }
}
