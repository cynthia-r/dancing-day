package com.cynthiar.dancingday;

import android.util.Pair;

import java.util.Date;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Robert on 09/02/2017.
 */

public class DataCache<T> {
    HashMap<String, Pair<T, Date>> mCache;
    IDataProvider<T> mDataProvider;
    public static final int CACHE_EXPIRY_IN_MINUTES = 15;

    public DataCache(IDataProvider<T> dataProvider) {
        mCache = new HashMap<String, Pair<T, Date>>();
        mDataProvider = dataProvider;
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
        T freshData = this.mDataProvider.GiveMeTheData();

        // Save data for next time
        this.Save(key, freshData);
        return freshData;
    }
}
