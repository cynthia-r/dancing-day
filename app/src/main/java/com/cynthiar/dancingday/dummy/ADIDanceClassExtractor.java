package com.cynthiar.dancingday.dummy;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by Robert on 12/02/2017.
 */

public class ADIDanceClassExtractor extends DanceClassExtractor {
    //private static final String mainSelector = ".tve_twc .tve_empty_dropzone , .tcb-flex-col.tve_clearfix:nth-child(1)";
    private static final String mainSelector = ".tve_tfo p";
    private static Pattern classTextPattern = Pattern.compile("Pre-Ballet/Tap Combo II (ages 6-7): Monday, 3:40 - 4:40PM (Maia) Studio #2");

    public String getUrl() {
        return "https://www.americandanceinstitute.com/winter-spring-2017-greenwood-dance-class-schedule-greenwood/#tab-con-7";
    }

    @Override
    public List<DummyContent.DummyItem> Extract(String htmlContent) {
        Document doc = Jsoup.parse(htmlContent);
        Elements classes = doc.select(mainSelector);

        // Return empty list if nothing extracted
        if (null == classes || classes.size() == 0)
            return new ArrayList<>();

        // Keep the ballet classes only
        List<DummyContent.DummyItem> dummyItemList = new ArrayList<>();
        for (Element classElement:classes
             ) {
            DummyContent.DummyItem classItem = this.parseClassElement(classElement);
            if (null != classItem)
                dummyItemList.add(classItem);

        }
        return dummyItemList;
    }

    private DummyContent.DummyItem parseClassElement(Element classElement) {
        String classType = "";
        String classText = "";
        String levelText = "";
        String classDescription = "";
        String classElementText = classElement.text();
        try {
            /*Element classTypeElement = (Element) classElement.childNode(0);
            TextNode classTypeTextNode = (TextNode) classTypeElement.childNode(0);
            classType = classTypeTextNode.text();


            TextNode classTextNode = (TextNode) classElement.childNode(1);
            classText = classTextNode.text();*/

            // Find the colon delimiter
            int indexOfColon = classElementText.indexOf(':');
            if (0 > indexOfColon)
            {
                System.out.print("Excluded:" + classElementText + " because doesn't contain ':'");
                return null;
            }

            // Get the class type
            classType = classElementText.substring(0, indexOfColon);
            if (!classType.contains("Ballet"))
            {
                System.out.print("Excluded:" + classType + " because doesn't contain 'Ballet'");
                return null;
            }

            // Get the class text
            classText = classElementText.substring(indexOfColon + 1);

            // Get the day
            int indexOfDay = -1;
            String day = "";
            for (String dayOfTheWeek:DummyContent.DAYS_OF_THE_WEEK
                 ) {
                int index = classText.indexOf(dayOfTheWeek);
                if (0 <= index) {
                    indexOfDay = index;
                    day = dayOfTheWeek;
                    break;
                }
            }

            if (0 > indexOfDay)
                return null;

            // Get the level and class description
            levelText = classText.substring(0, indexOfDay);
            String classTimeText = classText.substring(indexOfDay + day.length() + 1); // go after the day and the comma

            // Parse the level
            String level = this.parseLevel(levelText);
            if (level.equals("Children"))
            {
                System.out.print("Excluded:" + levelText + " because level is Children");
                return null;
            }

            /*int indexOfComma = classDescription.indexOf(',');
            if (0 > indexOfComma)
            {
                System.out.print("Excluded:" + classDescription + " because doesn't contain ','");
                return null;
            }*/

            //String day = classDescription.substring(0, indexOfComma); // TODO this should be an enum
            //String classTimeText = classDescription.substring(indexOfComma + 1);

            int indexOfOpeningParenthesis = classTimeText.indexOf('(');
            int indexOfClosingParenthesis = classTimeText.indexOf(')');

            // Extract the time
            String time = classTimeText.substring(0, indexOfOpeningParenthesis).trim();

            // Extract the teacher
            String teacher = classTimeText.substring(indexOfOpeningParenthesis + 1, indexOfClosingParenthesis);

            // Build and return the class object
            return new DummyContent.DummyItem(day, time, "ADI", teacher, level);
        }
        catch (Exception e) {
            String a = classElement.text();
            System.out.print("Excluded: elementText:"+ classElement.text()
                    + " classType: " + classType
                    + " classText: " + classText
                    + " levelText: " + levelText
                    + " classDescription: " + classDescription
                    + "because of an exception: " + e.getMessage());
            return null;
        }

    }

    private String parseLevel(String levelText) {
        if (levelText.contains("Pointe"))
            return "Pointe";
        if (!levelText.contains("Adult"))
            return "Children";
        if (levelText.contains("Intermediate"))
            return "Intermediate";
        if (levelText.contains("Beginning"))
            return "Beginner"; // TODO this should be an enum
        return "";
    }
}