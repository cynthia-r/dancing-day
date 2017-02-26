package com.cynthiar.dancingday.dummy.extractor;

import com.cynthiar.dancingday.dummy.DummyContent;

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

    @Override
    public String getKey() {
        return "WDC";
    }

    @Override
    public String getUrl() {
        return "https://www.westlakedance.com/schedule-";
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
        if (null == classes || classes.size() == 0)
            return new ArrayList<>();

        List<DummyContent.DummyItem> dummyItemList = new ArrayList<>();
        for (Element classRow:classes
                ) {

        }

        return dummyItemList;
    }
}
