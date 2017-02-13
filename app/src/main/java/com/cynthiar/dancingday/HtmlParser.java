package com.cynthiar.dancingday;

import android.support.v4.util.Pair;

import com.cynthiar.dancingday.dummy.DummyContent;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Created by Robert on 09/02/2017.
 */

public class HtmlParser {
    private String mUrl;
    private String mCssSelector;
    private String mHtmlContent;

    public HtmlParser(String htmlContent) {
        mHtmlContent = htmlContent;
    }

    public String ExtractContent() throws IOException {

        return "";
    }
}
