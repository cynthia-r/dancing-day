package com.cynthiar.dancingday.distance.matrix.model;

/**
 * Created by CynthiaR on 3/4/2017.
 */

public class DistanceMatrixValueText {
    private int mValue;
    private String mText;

    public DistanceMatrixValueText(){}

    public DistanceMatrixValueText(int value, String text){
        mValue = value;
        mText = text;
    }

    public String getText() {
        return mText;
    }

    public int getValue() {
        return mValue;
    }
}
