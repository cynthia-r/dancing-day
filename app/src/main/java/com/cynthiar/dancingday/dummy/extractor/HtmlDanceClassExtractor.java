package com.cynthiar.dancingday.dummy.extractor;

import android.content.Context;

import com.cynthiar.dancingday.dummy.DummyItem;
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
 * Created by Robert on 14/03/2017.
 */

public abstract class HtmlDanceClassExtractor extends DanceClassExtractor<Document>{
    protected abstract String getSelector();
    protected abstract List<DummyItem> parseBaseElement(int elementIndex, Element classElement);

    protected boolean validateElements(Elements elements) {
        return true;
    }

    public HtmlDanceClassExtractor(Context context) { super(context); }

    @Override
    public Document processDownload(InputStream inputStream, String baseUri) throws IOException {
        Document doc = Jsoup.parse(inputStream, null, baseUri);
        return doc;
    }

    @Override
    public List<DummyItem> extract(Document doc) throws IOException {
        String mainSelector = this.getSelector();
        Elements baseElements = doc.select(mainSelector);

        // Return empty list if nothing extracted
        if (null == baseElements || baseElements.size() == 0)
            return new ArrayList<>();

        // Validate the elements found
        if (!this.validateElements(baseElements)) {
            //DummyUtils.toast(mContext, "Invalid elements"); // Runtime exception
            return new ArrayList<>();
        }

        // Keep the ballet classes only
        List<DummyItem> dummyItemList = new ArrayList<>();
        for (int i=0; i < baseElements.size(); i++
                ) {
            Element classElement = baseElements.get(i);
            List<DummyItem> classItemList = this.parseBaseElement(i, classElement);
            if (null != classItemList && !classItemList.isEmpty())
                dummyItemList.addAll(classItemList);
        }
        return dummyItemList;
    }
}
