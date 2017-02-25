package com.cynthiar.dancingday;

import android.support.v4.util.Pair;

import com.cynthiar.dancingday.dummy.DummyContent;
import com.cynthiar.dancingday.dummy.extractor.DanceClassExtractor;
import com.cynthiar.dancingday.dummy.extractor.Extractors;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Robert on 09/02/2017.
 */

public class DanceClassDataProvider extends DataProvider<Pair<String, List<DummyContent.DummyItem>>>
    implements IConsumerCallback<Pair<String, List<DummyContent.DummyItem>>>{

    //private DownloadTask mDownloadTask;
    private TodayActivity mTodayActivity;
    private String mCurrentKey; // TODO better design

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
            mTodayActivity.startDownload(key);
        } catch (Exception e) {

        }
        /*List<DummyContent.DummyItem> dummyItemList = new ArrayList<>();
        dummyItemList.add(new DummyContent.DummyItem("Monday","6PM",dataString.substring(0, 5),dataString.substring(0, 7),"Beginner"));
        mConsumerCallback.updateFromResult(new Pair<String, List<DummyContent.DummyItem>>(key, dummyItemList));*/
    }

    public void updateFromResult(Pair<String, List<DummyContent.DummyItem>> keyAndDataString) {
        String key = keyAndDataString.first;
        List<DummyContent.DummyItem> dummyItemList = keyAndDataString.second;

        // Parse the result
        /*DanceClassExtractor danceClassExtractor = Extractors.getExtractor(key);
        List<DummyContent.DummyItem> dummyItemList = new ArrayList<>();
        try {

            dummyItemList = danceClassExtractor.Extract(dataString);
        }
        catch(Exception e) {
            String l = e.getMessage();
        }*/

        // See if can callback the main activity directly
        mConsumerCallback.updateFromResult(new Pair<>(key, dummyItemList));
    }
}
