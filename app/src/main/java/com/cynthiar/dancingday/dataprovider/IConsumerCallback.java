package com.cynthiar.dancingday.dataprovider;

/**
 * Created by Robert on 11/02/2017.
 */

public interface IConsumerCallback<T> {
    void updateFromResult(T result);
}
