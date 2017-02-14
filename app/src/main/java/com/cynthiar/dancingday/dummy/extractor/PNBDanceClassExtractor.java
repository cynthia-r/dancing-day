package com.cynthiar.dancingday.dummy.extractor;

import com.cynthiar.dancingday.dummy.DummyContent;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringEscapeUtils;

/**
 * Created by Robert on 13/02/2017.
 */

public class PNBDanceClassExtractor extends DanceClassExtractor {
    private static final String mainSelector = "table tr";
    private int mSchoolNumber = -1;

    @Override
    public String getKey() {
        return "PNB";
    }

    @Override
    public String getUrl() {
        return "https://www.pnb.org/pnb-school/classes/open-program/#fusion-tab-classschedule";
    }

    @Override
    public List<DummyContent.DummyItem> Extract(String htmlContent) {
        Document doc = Jsoup.parse(htmlContent);
        Elements classes = doc.select(mainSelector);

        // Return empty list if nothing extracted
        if (null == classes || classes.size() == 0)
            return new ArrayList<>();

        List<DummyContent.DummyItem> dummyItemList = new ArrayList<>();
        for (Element classRow:classes
             ) {
            List<DummyContent.DummyItem> classItemList = this.parseClassElement(classRow);
            if (null != classItemList && classItemList.size() > 0)
                dummyItemList.addAll(classItemList);
        }

        return dummyItemList;
    }

    private List<DummyContent.DummyItem> parseClassElement(Element classElement) {
        try {
            Element firstChild = (Element) classElement.childNode(0);
            if (null == firstChild)
                return null;

            String firstChildText = firstChild.text();
            if (firstChildText.equals("Class"))
                mSchoolNumber++;

            String level = this.parseLevel(firstChildText);
            if (level.equals(""))
                return null;

            List<DummyContent.DummyItem> classItemList = new ArrayList<>();
            for (int i=1; i <= 6; i++) {
                Element dayClassElement = (Element) classElement.childNode(i);
                if (null == dayClassElement)
                    continue; // no class on that day

                String classText = StringEscapeUtils.unescapeHtml4(dayClassElement.text());
                if (null == classText || classText.equals(""))
                    continue; // no class on that day

                int j=0;
                while (j < dayClassElement.childNodes().size()) { // multiple classes on that day
                    TextNode classInLevelElement = (TextNode)dayClassElement.childNode(j);
                    String classInLevelText = classInLevelElement.text();
                    if (null != classInLevelText && !classInLevelText.equals("")) {
                        DummyContent.DummyItem classInLevel = parseClassText(classInLevelText, i, level);
                        if (null != classInLevel)
                            classItemList.add(classInLevel);
                    }
                    j+=2;
                }
            }
            return classItemList;
        }
        catch (Exception e) {
            return null;
        }
    }

    private DummyContent.DummyItem parseClassText(String classText, int i, String level) {
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
        return new DummyContent.DummyItem(day, time, school, teacher, level);
    }

    private String parseLevel(String levelText) {
        if (levelText.contains("Int.") && levelText.contains("Adv."))
            return "Advanced";
        if (levelText.contains("Int.") && levelText.contains("Beg."))
            return "Beg/Int";
        if (levelText.contains("Beg."))
            return "Beginner";
        if (levelText.contains("Int."))
            return "Intermediate"; // TODO this should be an enum
        return "";
    }

    private String getSchool() {
        if (0 == mSchoolNumber)
            return "PNB Seattle";
        if (1 == mSchoolNumber)
            return "PNB Bellevue";
        return "";
    }
}
