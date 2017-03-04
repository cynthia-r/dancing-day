package com.cynthiar.dancingday.distance.matrix;

import android.os.AsyncTask;

import com.cynthiar.dancingday.data.IConsumerCallback;

/**
 * Created by CynthiaR on 3/4/2017.
 * Task that computes the estimated time in traffic between a source and a destination.
 * The computation is done asynchronously.
 */

public class DistanceTask extends AsyncTask<DistanceQuery, DistanceTaskProgress, DistanceResult> {

    private IConsumerCallback<DistanceResult> mConsumerCallback;
    /*private String mOrigin;
    private String mDestination;*/

    public DistanceTask(IConsumerCallback<DistanceResult> consumerCallback) {
        mConsumerCallback = consumerCallback;
    }

    @Override
    protected DistanceResult doInBackground(DistanceQuery... params) {
        return new DistanceResult(1);
    }

    @Override
    protected void onPostExecute(DistanceResult result) {
        mConsumerCallback.updateFromResult(result);
    }
}
