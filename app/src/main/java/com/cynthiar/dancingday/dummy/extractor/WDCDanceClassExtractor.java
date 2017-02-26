package com.cynthiar.dancingday.dummy.extractor;

import android.content.Context;

import com.cynthiar.dancingday.dummy.DummyContent;
import com.cynthiar.dancingday.dummy.DummyUtils;

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
    public List<DummyContent.DummyItem> Extract(Document doc) throws IOException {
        Elements classes = doc.select(mainSelector);

        // Return empty list if nothing extracted
        if (null == classes || classes.size() == 0) {
            List<DummyContent.DummyItem> dummyItemList = new ArrayList<>();
            dummyItemList.add(new DummyContent.DummyItem("Monday", "6-7 pm", "WDC", "Jerri", "Beg/Int"));
            dummyItemList.add(new DummyContent.DummyItem("Monday", "6-7 pm", "WDC", "Beinna", "Beg/Int"));
            dummyItemList.add(new DummyContent.DummyItem("Monday", "7-8 pm", "WDC", "Elbert/Cody", "Open Level"));
            dummyItemList.add(new DummyContent.DummyItem("Tuesday", "6-7 pm", "WDC", "Lindsay", "Beginner"));
            dummyItemList.add(new DummyContent.DummyItem("Tuesday", "7-8 pm", "WDC", "Mustafa", "Open Level"));
            dummyItemList.add(new DummyContent.DummyItem("Wednesday", "7-8 pm", "WDC", "Mari", "Beg/Int"));
            dummyItemList.add(new DummyContent.DummyItem("Thursday", "6-7 pm", "WDC", "Jerri", "Beginner"));
            dummyItemList.add(new DummyContent.DummyItem("Saturday", "10-11:30 am", "WDC", "Jerri", "Beg/Int"));
            dummyItemList.add(new DummyContent.DummyItem("Saturday", "11:30 am-12:30 pm", "WDC", "Jerri", "Pointe"));
            return dummyItemList;
        }
        else {
            DummyUtils.toast(mContext, "WDC found something!");
        }



        List<DummyContent.DummyItem> dummyItemList = new ArrayList<>();
        for (Element classRow:classes
                ) {

        }

        return dummyItemList;
    }
}
