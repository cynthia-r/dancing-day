package com.cynthiar.dancingday;

import com.cynthiar.dancingday.dummy.DummyContent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Robert on 09/02/2017.
 */

public class DanceClassDataProvider implements IDataProvider<List<DummyContent.DummyItem>> {

    private DownloadTask mDownloadTask;

    public DanceClassDataProvider(DownloadTask downloadTask) {
        this.mDownloadTask = downloadTask;
    }

    @Override
    public List<DummyContent.DummyItem> GiveMeTheData() {
        String dataString = "";
        try{
            dataString = mDownloadTask.downloadUrl();
        } catch (Exception e) {

        }
        List<DummyContent.DummyItem> dummyItemList = new ArrayList<>();
        dummyItemList.add(new DummyContent.DummyItem("Monday","6PM",dataString.substring(0, 5),dataString.substring(0, 7),"Beginner"));
        return dummyItemList;
    }
}
