package com.cynthiar.dancingday.distance.matrix;

/**
 * Created by CynthiaR on 3/4/2017.
 */

public class DistanceResult {
    private int mEstimatedTime;

    public DistanceResult(int estimatedTime) {
        mEstimatedTime = estimatedTime;
    }

    public int getEstimatedTime(){
        return mEstimatedTime;
    }
}
