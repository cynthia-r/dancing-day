package com.cynthiar.dancingday.data;

/**
 * Created by Robert on 11/02/2017.
 */

public interface IConsumerCallback<T> {
    void updateFromResult(T result);
}
