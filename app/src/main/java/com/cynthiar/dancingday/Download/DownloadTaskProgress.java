package com.cynthiar.dancingday.download;

import com.cynthiar.dancingday.data.IProgress;

/**
 * Created by Robert on 08/02/2017.
 */

public class DownloadTaskProgress implements IProgress {
    private int mProgressCode;
    public DownloadTaskProgress(int progressCode) {
        mProgressCode = progressCode;
    }

    public int GetProgressCode() {
        return mProgressCode;
    }
}
