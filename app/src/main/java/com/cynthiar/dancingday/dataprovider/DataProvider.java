package com.cynthiar.dancingday.dataprovider;

/**
 * Created by Robert on 09/02/2017.
 */

public abstract class DataProvider<T> {
    public IConsumerCallback<T> mConsumerCallback;

    public DataProvider() {
    }

    public void setConsumerCallback(IConsumerCallback<T> consumerCallback) {
        mConsumerCallback = consumerCallback;
    }

    public abstract void GiveMeTheData(String key);
}
