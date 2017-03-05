package com.cynthiar.dancingday.distance.matrix.model;

/**
 * Created by CynthiaR on 3/4/2017.
 */

public class DistanceMatrixRow {
    private DistanceMatrixElement[] mElements;

    public DistanceMatrixRow() {}

    public DistanceMatrixRow(DistanceMatrixElement[] elements) {
        mElements = elements;
    }

    public DistanceMatrixElement[] getElements() {
        return mElements;
    }
}
