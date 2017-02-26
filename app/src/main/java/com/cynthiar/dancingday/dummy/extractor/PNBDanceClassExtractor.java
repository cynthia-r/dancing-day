package com.cynthiar.dancingday.dummy.extractor;

import android.content.Context;

import com.cynthiar.dancingday.dummy.DanceClassLevel;
import com.cynthiar.dancingday.dummy.DummyContent;
import com.cynthiar.dancingday.dummy.DummyItem;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringEscapeUtils;

/**
 * Created by Robert on 13/02/2017.
 */

public class PNBDanceClassExtractor extends DanceClassExtractor<Document> {
    private static final String mainSelector = "table tr";
    private int mSchoolNumber = -1;

    public PNBDanceClassExtractor(Context context) { super(context); }

    @Override
    public String getKey() {
        return "PNB";
    }

    @Override
    public String getUrl() {
        return "https://www.pnb.org/pnb-school/classes/open-program/#fusion-tab-classschedule";
    }

    @Override
    public List<String> getSchoolList() {
        List<String> schoolList = new ArrayList<>();
        schoolList.add("PNB Seattle"); // TODO consts
        schoolList.add("PNB Bellevue");
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
        if (null == classes || classes.size() == 0)
            return new ArrayList<>();

        List<DummyItem> dummyItemList = new ArrayList<>();
        for (Element classRow:classes
             ) {
            List<DummyItem> classItemList = this.parseClassElement(classRow);
            if (null != classItemList && classItemList.size() > 0)
                dummyItemList.addAll(classItemList);
        }

        return dummyItemList;
    }

    private List<DummyItem> parseClassElement(Element classElement) {
        try {

            //Element firstChild = (Element) classElement.childNode(0);
            NextElement nextElement = findNextChildElement(classElement, 0);
            Element firstChild = nextElement.getElement();
            if (null == firstChild)
                return null;

            String firstChildText = firstChild.text();
            if (firstChildText.equals("Class"))
                mSchoolNumber++;

            DanceClassLevel level = this.parseLevel(firstChildText);
            if (level.equals(""))
                return null;

            List<DummyItem> classItemList = new ArrayList<>();
            int i=1;   // 6 days (no classes on Sunday)
            while (i <= 6) {
                nextElement = findNextChildElement(classElement, nextElement.getNextN());
                Element dayClassElement = nextElement.getElement();
                if (null == dayClassElement) {
                    i++;
                    continue; // no class on that day
                }


                String classText = StringEscapeUtils.unescapeHtml4(dayClassElement.text());
                if (null == classText || classText.equals(""))
                {
                    i++;
                    continue; // no class on that day
                }

                int j=0;
                while (j < dayClassElement.childNodes().size()) { // multiple classes on that day
                    TextNode classInLevelElement = (TextNode)dayClassElement.childNode(j);
                    String classInLevelText = classInLevelElement.text();
                    if (null != classInLevelText && !classInLevelText.equals("")) {
                        DummyItem classInLevel = parseClassText(classInLevelText, i, level);
                        if (null != classInLevel)
                            classItemList.add(classInLevel);
                    }
                    j+=2;
                }
                i++;
            }
            return classItemList;
        }
        catch (Exception e) {
            return null;
        }
    }

    private DummyItem parseClassText(String classText, int i, DanceClassLevel level) {
        int indexOfOpeningParenthesis = classText.indexOf('(');
        int indexOfClosingParenthesis = classText.indexOf(')');
        if (indexOfOpeningParenthesis < 0 || indexOfClosingParenthesis <= 0)
            return null; // cannot parse class on that day

        // Extract the time
        String time = classText.substring(0, indexOfOpeningParenthesis).trim();

        // Extract the teacher
        String teacher = classText.substring(indexOfOpeningParenthesis + 1, indexOfClosingParenthesis);

        // Get the school
        String school = getSchool();

        // Get the day
        String day = DummyContent.DAYS_OF_THE_WEEK[i-1];

        // Build and return the dummy item
        return new DummyItem(day, time, school, teacher, level);
    }

    private DanceClassLevel parseLevel(String levelText) {
        if (levelText.contains("Int.") && levelText.contains("Adv."))
            return DanceClassLevel.Advanced;
        if (levelText.contains("Int.") && levelText.contains("Beg."))
            return DanceClassLevel.BeginnerIntermediate;
        if (levelText.contains("Beg."))
            return DanceClassLevel.Beginner;
        if (levelText.contains("Int."))
            return DanceClassLevel.Intermediate;
        return DanceClassLevel.Unknown;
    }

    private String getSchool() {
        if (0 == mSchoolNumber)
            return "PNB Seattle";
        if (1 == mSchoolNumber)
            return "PNB Bellevue";
        return "";
    }

    private NextElement findNextChildElement(Element parentElement, int n) {
        // Find the next element child
        Element nextElement = null;
        while (n < parentElement.childNodeSize()) {
            try {
                nextElement = (Element) parentElement.childNode(n);
                n++;
                break;
            }
            catch (Exception e) {
                n++;
            }
        }
        return new NextElement(nextElement, n);
    }

    private class NextElement {
        Element mElement;
        int mNextN;
        public NextElement(Element element, int n) {
            mElement = element;
            mNextN = n;
        }

        public Element getElement() {
            return mElement;
        }

        public int getNextN() {
            return mNextN;
        }
    }
}
