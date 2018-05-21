package com.cynthiar.dancingday.dataprovider;

/**
 * Asynchronous data provider.
 * Created by Robert on 09/02/2017.
 */

public abstract class DataProvider<T> {
    /*
        Consumer to call when providing the data back.
     */
    protected IConsumerCallback<T> mConsumerCallback;

    public DataProvider() {
    }

    public void setConsumerCallback(IConsumerCallback<T> consumerCallback) {
        mConsumerCallback = consumerCallback;
    }

    public abstract void GiveMeTheData(String key);
}
