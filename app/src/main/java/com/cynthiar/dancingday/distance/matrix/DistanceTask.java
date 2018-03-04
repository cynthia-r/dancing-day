package com.cynthiar.dancingday.distance.matrix;

import android.os.AsyncTask;

import com.cynthiar.dancingday.dataprovider.IConsumerCallback;

/**
 * Created by CynthiaR on 3/4/2017.
 * Task that computes the estimated time in traffic between a source and a destination.
 * The computation is done asynchronously.
 */

public class DistanceTask extends AsyncTask<DistanceQuery, DistanceTaskProgress, DistanceResult> {

    private static final String G_DISTANCE_MATRIX_API_KEY = "AIzaSyAciWtCnB8EdadekShFPBzCirE065e2inQ";
    private IConsumerCallback<DistanceResult> mConsumerCallback;
    private DistanceMatrixClient mDistanceMatrixClient = new DistanceMatrixClient(DistanceTask.G_DISTANCE_MATRIX_API_KEY);

    public DistanceTask(IConsumerCallback<DistanceResult> consumerCallback) {
        mConsumerCallback = consumerCallback;
    }

    @Override
    protected DistanceResult doInBackground(DistanceQuery... params) {
        return mDistanceMatrixClient.getDistance(params[0]);
    }

    @Override
    protected void onPostExecute(DistanceResult result) {
        mConsumerCallback.updateFromResult(result);
    }
}
