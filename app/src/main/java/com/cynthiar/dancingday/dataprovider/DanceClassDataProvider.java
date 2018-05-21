package com.cynthiar.dancingday.dataprovider;

import android.support.v4.util.Pair;

import com.cynthiar.dancingday.TodayActivity;
import com.cynthiar.dancingday.model.DummyItem;

import java.util.List;

/**
 * Created by Robert on 09/02/2017.
 */

public class DanceClassDataProvider extends DataProvider<Pair<String, List<DummyItem>>>
    implements IConsumerCallback<Pair<String, List<DummyItem>>> {

    private TodayActivity mTodayActivity;

    public DanceClassDataProvider (TodayActivity todayActivity) {
        mTodayActivity = todayActivity;
    }

    @Override
    public void GiveMeTheData(String key) {
        try{
            mTodayActivity.startDownload(key);
        } catch (Exception e) {

        }
    }

    public void updateFromResult(Pair<String, List<DummyItem>> keyAndDataString) {
        String key = keyAndDataString.first;
        List<DummyItem> dummyItemList = keyAndDataString.second;

        mConsumerCallback.updateFromResult(new Pair<>(key, dummyItemList));
    }
}
