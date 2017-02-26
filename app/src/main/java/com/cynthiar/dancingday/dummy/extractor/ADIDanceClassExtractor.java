package com.cynthiar.dancingday.dummy.extractor;

import android.content.Context;

import com.cynthiar.dancingday.dummy.DanceClassLevel;
import com.cynthiar.dancingday.dummy.DummyContent;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by Robert on 12/02/2017.
 */

public class ADIDanceClassExtractor extends DanceClassExtractor<Document> {
    //private static final String mainSelector = ".tve_twc .tve_empty_dropzone , .tcb-flex-col.tve_clearfix:nth-child(1)";
    private static final String mainSelector = ".tve_tfo p";

    public ADIDanceClassExtractor(Context context) { super(context); }

    @Override
    public String getKey() {
        return "ADI";
    }

    @Override
    public String getUrl() {
        return "https://www.americandanceinstitute.com/winter-spring-2017-greenwood-dance-class-schedule-greenwood/#tab-con-7";
    }

    @Override
    public List<String> getSchoolList() {
        List<String> schoolList = new ArrayList<>();
        schoolList.add(this.getKey());
        return schoolList;
    }

    @Override
    public Document processDownload(InputStream inputStream, String baseUri) throws IOException {
        Document doc = Jsoup.parse(inputStream, null, baseUri);
        return doc;
    }

    @Override
    public List<DummyContent.DummyItem> Extract(Document doc) throws IOException {

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
            DanceClassLevel level = this.parseLevel(levelText);
            if (DanceClassLevel.Children == level)
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

    private DanceClassLevel parseLevel(String levelText) {
        if (levelText.contains("Pointe"))
            return DanceClassLevel.Pointe;
        if (!levelText.contains("Adult"))
            return DanceClassLevel.Children;
        if (levelText.contains("Intermediate"))
            return DanceClassLevel.Intermediate;
        if (levelText.contains("Beginning"))
            return DanceClassLevel.Beginner;
        return DanceClassLevel.Unknown;
    }
}
