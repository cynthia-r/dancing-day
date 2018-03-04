package com.cynthiar.dancingday.distance.matrix;

import com.cynthiar.dancingday.dataprovider.IProgress;

/**
 * Created by CynthiaR on 3/4/2017.
 */

public class DistanceTaskProgress implements IProgress {
    private int mProgressCode;
    public DistanceTaskProgress(int progressCode) {
        mProgressCode = progressCode;
    }

    @Override
    public int getProgressCode() {
        return 0;
    }
}
