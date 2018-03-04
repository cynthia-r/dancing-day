package com.cynthiar.dancingday.extractor;

import android.content.Context;

import com.cynthiar.dancingday.model.DanceClassLevel;
import com.cynthiar.dancingday.model.DummyItem;
import com.cynthiar.dancingday.model.Schools;
import com.cynthiar.dancingday.model.time.DanceClassTime;

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
    private static final String mainSelector = ".wcs-class--visible";

    public WDCDanceClassExtractor(Context context) { super(context); }

    @Override
    public String getKey() {
        return "WDC";
    }

    @Override
    public String getUrl() {
        return "http://westlakedancecenter.com/class-schedule/";
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
    public ExtractorResults extract(Document doc) throws IOException {
        Elements classes = doc.select(mainSelector);
        String unexpectedMessage;

        // Return empty list if nothing extracted
        if (null == classes || classes.size() == 0) {
            List<DummyItem> dummyItemList = new ArrayList<>();
            dummyItemList.add(new DummyItem("Monday", DanceClassTime.create("10:00-11:30am"), Schools.WDC_SCHOOL, "Heather Dawson", DanceClassLevel.BeginnerIntermediate));
            dummyItemList.add(new DummyItem("Monday", DanceClassTime.create("4:00-5:30 pm"), Schools.WDC_SCHOOL, "Erin Krall", DanceClassLevel.Intermediate));
            dummyItemList.add(new DummyItem("Tuesday", DanceClassTime.create("4:00-5:30pm"), Schools.WDC_SCHOOL, "Thomas Phelan", DanceClassLevel.Advanced));
            dummyItemList.add(new DummyItem("Tuesday", DanceClassTime.create("5:30-6:30pm"), Schools.WDC_SCHOOL, "Sandy Brown", DanceClassLevel.Beginner));
            dummyItemList.add(new DummyItem("Wednesday", DanceClassTime.create("5:30-6:30pm"), Schools.WDC_SCHOOL, "Kim Gockel", DanceClassLevel.Beginner));
            dummyItemList.add(new DummyItem("Thursday", DanceClassTime.create("4:00-5:30pm"), Schools.WDC_SCHOOL, "Thomas Phelan", DanceClassLevel.Advanced));
            dummyItemList.add(new DummyItem("Thursday", DanceClassTime.create("5:30-6:30pm"), Schools.WDC_SCHOOL, "Sandy Brown", DanceClassLevel.Beginner));
            dummyItemList.add(new DummyItem("Friday", DanceClassTime.create("6:30-8:00pm"), Schools.WDC_SCHOOL, "Kim Gockel", DanceClassLevel.Intermediate));
            dummyItemList.add(new DummyItem("Saturday", DanceClassTime.create("10:00-11:30am"), Schools.WDC_SCHOOL, "Kim Gockel", DanceClassLevel.Intermediate));
            dummyItemList.add(new DummyItem("Sunday", DanceClassTime.create("1:00-2:00 pm"), Schools.WDC_SCHOOL, "Melissa Brown", DanceClassLevel.Beginner));
            return new ExtractorResults(dummyItemList);
        }
        else {
            unexpectedMessage = "WDC found something!";
        }

        List<DummyItem> dummyItemList = new ArrayList<>();
        for (Element classRow:classes
                ) {

        }

        return new ExtractorResults(dummyItemList, unexpectedMessage);
    }
}
