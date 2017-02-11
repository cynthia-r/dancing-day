package com.cynthiar.dancingday;

import android.support.v4.util.Pair;

import com.cynthiar.dancingday.dummy.DummyContent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Robert on 09/02/2017.
 */

public class DanceClassDataProvider extends DataProvider<Pair<String, List<DummyContent.DummyItem>>>
    implements IConsumerCallback<String>{

    //private DownloadTask mDownloadTask;
    private TodayActivity mTodayActivity;

    /*public DanceClassDataProvider(DownloadTask downloadTask) {
        this.mDownloadTask = downloadTask;
    }*/
    public DanceClassDataProvider (/*IConsumerCallback<List<DummyContent.DummyItem>> mConsumerCallback,*/ TodayActivity todayActivity) {
        //super(mConsumerCallback);
        mTodayActivity = todayActivity;
    }


    @Override
    public void GiveMeTheData(String key) {
        String dataString = "";
        try{
            //dataString = mDownloadTask.downloadUrl();
            mTodayActivity.startDownload();
        } catch (Exception e) {

        }
        /*List<DummyContent.DummyItem> dummyItemList = new ArrayList<>();
        dummyItemList.add(new DummyContent.DummyItem("Monday","6PM",dataString.substring(0, 5),dataString.substring(0, 7),"Beginner"));
        mConsumerCallback.updateFromResult(new Pair<String, List<DummyContent.DummyItem>>(key, dummyItemList));*/
    }

    public void updateFromResult(String dataString) {
        List<DummyContent.DummyItem> dummyItemList = new ArrayList<>();
        dummyItemList.add(new DummyContent.DummyItem("Monday","6PM",dataString.substring(0, 5),dataString.substring(0, 7),"Beginner"));
        mConsumerCallback.updateFromResult(new Pair<String, List<DummyContent.DummyItem>>(TodayActivity.TODAY_KEY, dummyItemList));
        // TODO key
    }
}
