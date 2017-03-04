package com.cynthiar.dancingday.dummy.extractor;

import android.content.Context;

import com.cynthiar.dancingday.dummy.DanceClassLevel;
import com.cynthiar.dancingday.dummy.DummyItem;
import com.cynthiar.dancingday.dummy.DummyUtils;
import com.cynthiar.dancingday.dummy.time.DanceClassTime;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Robert on 25/02/2017.
 */

public class WDCDanceClassExtractor extends DanceClassExtractor<Document> {
    private static final String mainSelector = ".aQJ , #WRchTxt3-gua span, #WRchTxt2-nsv span";

    public WDCDanceClassExtractor(Context context) { super(context); }

    @Override
    public String getKey() {
        return "WDC";
    }

    @Override
    public String getUrl() {
        return "https://www.westlakedance.com/schedule-";
    }

    @Override
    public List<String> getSchoolList() {
        List<String> schoolList = new ArrayList<>();
        schoolList.add(this.getKey());
        return schoolList;
    }

    @Override
    public Document processDownload(InputStream downloadStream, String baseUri) throws IOException {
        Document doc = Jsoup.parse(downloadStream, null, baseUri);
        return doc;
    }

    @Override
    public List<DummyItem> Extract(Document doc) throws IOException {
        Elements classes = doc.select(mainSelector);

        // Return empty list if nothing extracted
        if (null == classes || classes.size() == 0) {
            List<DummyItem> dummyItemList = new ArrayList<>();
            dummyItemList.add(new DummyItem("Monday", DanceClassTime.create("10:00-11:30am"), "WDC", "Heather Dawson", DanceClassLevel.BeginnerIntermediate));
            dummyItemList.add(new DummyItem("Monday", DanceClassTime.create("4:00-5:30 pm"), "WDC", "Erin Krall", DanceClassLevel.Intermediate));
            dummyItemList.add(new DummyItem("Tuesday", DanceClassTime.create("4:00-5:30pm"), "WDC", "Thomas Phelan", DanceClassLevel.Advanced));
            dummyItemList.add(new DummyItem("Tuesday", DanceClassTime.create("5:30-6:30pm"), "WDC", "Sandy Brown", DanceClassLevel.Beginner));
            dummyItemList.add(new DummyItem("Wednesday", DanceClassTime.create("5:30-6:30pm"), "WDC", "Kim Gockel", DanceClassLevel.Beginner));
            dummyItemList.add(new DummyItem("Thursday", DanceClassTime.create("4:00-5:30pm"), "WDC", "Thomas Phelan", DanceClassLevel.Advanced));
            dummyItemList.add(new DummyItem("Thursday", DanceClassTime.create("5:30-6:30pm"), "WDC", "Sandy Brown", DanceClassLevel.Beginner));
            dummyItemList.add(new DummyItem("Friday", DanceClassTime.create("6:30-8:00pm"), "WDC", "Kim Gockel", DanceClassLevel.Intermediate));
            dummyItemList.add(new DummyItem("Saturday", DanceClassTime.create("10:00-11:30am"), "WDC", "Kim Gockel", DanceClassLevel.Intermediate));
            dummyItemList.add(new DummyItem("Sunday", DanceClassTime.create("1:00-2:00 pm"), "WDC", "Melissa Brown", DanceClassLevel.Beginner));
            return dummyItemList;
        }
        else {
            DummyUtils.toast(mContext, "WDC found something!");
        }

        List<DummyItem> dummyItemList = new ArrayList<>();
        for (Element classRow:classes
                ) {

        }

        return dummyItemList;
    }
}
