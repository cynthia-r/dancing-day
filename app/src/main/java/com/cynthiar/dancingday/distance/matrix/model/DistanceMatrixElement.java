package com.cynthiar.dancingday.distance.matrix.model;

/**
 * Created by CynthiaR on 3/4/2017.
 */

public class DistanceMatrixElement {
    private DistanceMatrixStatusCode mStatus;
    private DistanceMatrixValueText mDuration;
    private DistanceMatrixValueText mDurationInTraffic;
    private DistanceMatrixValueText mDistance;

    public DistanceMatrixElement() {}

    public DistanceMatrixElement(DistanceMatrixStatusCode statusCode, DistanceMatrixValueText duration,
                                 DistanceMatrixValueText durationInTraffic, DistanceMatrixValueText distance) {
        mStatus = statusCode;
        mDuration = duration;
        mDurationInTraffic = durationInTraffic;
        mDistance = distance;
    }
    public DistanceMatrixStatusCode getStatus() {
        return mStatus;
    }

    public DistanceMatrixValueText getDuration() {
        return mDuration;
    }

    public DistanceMatrixValueText getDurationInTraffic() {
        return mDurationInTraffic;
    }

    public DistanceMatrixValueText getDistance() {
        return mDistance;
    }

}
