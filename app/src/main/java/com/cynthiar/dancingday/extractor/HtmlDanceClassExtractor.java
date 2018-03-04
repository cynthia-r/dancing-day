package com.cynthiar.dancingday.extractor;

import android.content.Context;

import com.cynthiar.dancingday.model.DummyItem;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Robert on 14/03/2017.
 */

public abstract class HtmlDanceClassExtractor extends DanceClassExtractor<Document>{
    protected abstract String getSelector();

    protected abstract ExtractorResults parseBaseElement(int elementIndex, Element classElement);

    protected boolean validateElements(Elements elements) {
        return true;
    }

    protected ExtractorResults getAdditionalElements() {
        return new ExtractorResults();
    }

    public HtmlDanceClassExtractor(Context context) { super(context); }

    @Override
    public Document processDownload(InputStream inputStream, String baseUri) throws IOException {
        Document doc = Jsoup.parse(inputStream, null, baseUri);
        return doc;
    }

    @Override
    public ExtractorResults extract(Document doc) throws IOException {
        String mainSelector = this.getSelector();
        Elements baseElements = doc.select(mainSelector);

        // Return empty list if nothing extracted
        if (null == baseElements || baseElements.size() == 0)
            return new ExtractorResults(new ArrayList<DummyItem>(), new ArrayList<String>());

        // Validate the elements found
        if (!this.validateElements(baseElements)) {
            return new ExtractorResults(new ArrayList<DummyItem>(), "Invalid elements");
        }

        // Keep the ballet classes only
        ExtractorResults extractorResults = new ExtractorResults();
        for (int i=0; i < baseElements.size(); i++
                ) {
            Element classElement = baseElements.get(i);
            ExtractorResults currentExtractorResults = this.parseBaseElement(i, classElement);
            extractorResults.addExtractorResults(currentExtractorResults);
        }

        // Add the extra elements and return the results
        extractorResults.addExtractorResults(this.getAdditionalElements());
        return extractorResults;
    }
}
