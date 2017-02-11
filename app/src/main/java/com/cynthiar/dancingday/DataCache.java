package com.cynthiar.dancingday;

import android.support.v4.util.Pair;

import java.util.Date;
import java.util.HashMap;

/**
 * Created by Robert on 09/02/2017.
 */

public class DataCache<T> implements IConsumerCallback<Pair<String, T>> {
    HashMap<String, Pair<T, Date>> mCache;
    DataProvider<Pair<String, T>> mDataProvider;
    public IConsumerCallback<T> mConsumerCallback;
    public static final int CACHE_EXPIRY_IN_MINUTES = 15;

    public DataCache(DataProvider<Pair<String, T>> dataProvider, IConsumerCallback<T> consumerCallback) {
        mCache = new HashMap<String, Pair<T, Date>>();
        mDataProvider = dataProvider;
        mDataProvider.setConsumerCallback(this);
        mConsumerCallback = consumerCallback;
    }

    /*
        Saves data into the cache and records the current time.
     */
    public void Save(String key, T data){
        mCache.put(key, new Pair<T, Date>(data, new Date()));
    }

    /*
        Loads data from the cache and reloads it if it's too old.
     */
    public T Load(String key) {
        if (mCache.containsKey(key)) {
            Pair<T,Date> dataPair = mCache.get(key);

            // Return data if fresh
            Date currentDate = new Date();
            long elapsedMilliseconds = currentDate.getTime() - dataPair.second.getTime();
            if (elapsedMilliseconds / (60 * 1000) < DataCache.CACHE_EXPIRY_IN_MINUTES)
                return dataPair.first;
        }

        // Load data if not there
        // Or reload data if not fresh
        this.mDataProvider.GiveMeTheData(key);
        return null;
    }

    public void updateFromResult(Pair<String, T> result) {
        String key = result.first;
        T freshData = result.second;

        // Save data for next time
        this.Save(key, freshData);

        // Update consumer with the result
        mConsumerCallback.updateFromResult(freshData);
    }
}
