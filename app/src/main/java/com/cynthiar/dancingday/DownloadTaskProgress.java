package com.cynthiar.dancingday;

/**
 * Created by Robert on 08/02/2017.
 */

public class DownloadTaskProgress implements DownloadCallback.Progress {
    private int mProgressCode;
    public DownloadTaskProgress(int progressCode) {
        mProgressCode = progressCode;
    }

    public int GetProgressCode() {
        return mProgressCode;
    }
}
