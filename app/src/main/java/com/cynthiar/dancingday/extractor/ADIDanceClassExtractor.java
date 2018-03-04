package com.cynthiar.dancingday.extractor;

import android.content.Context;

import com.cynthiar.dancingday.model.DanceClassLevel;
import com.cynthiar.dancingday.model.Schools;
import com.cynthiar.dancingday.model.time.DanceClassTime;
import com.cynthiar.dancingday.model.DummyContent;
import com.cynthiar.dancingday.model.DummyItem;

import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Robert on 12/02/2017.
 */

public class ADIDanceClassExtractor extends HtmlDanceClassExtractor {
    private static final String mainSelector = ".tve_empty_dropzone:nth-child(1) p:nth-child(1)";

    public ADIDanceClassExtractor(Context context) { super(context); }

    @Override
    public String getKey() {
        return "ADI";
    }

    @Override
    public String getUrl() {
        return "https://www.americandanceinstitute.com/2018-winter-spring-class-schedule-%e2%8b%86-greenwood/";
    }

    @Override
    public List<String> getSchoolList() {
        List<String> schoolList = new ArrayList<>();
        schoolList.add(this.getKey());
        return schoolList;
    }

    @Override
    protected String getSelector() {
        return ADIDanceClassExtractor.mainSelector;
    }

    @Override
    protected ExtractorResults parseBaseElement(int elementIndex, Element classElement) {
        String classType = "";
        String classText = "";
        String levelText = "";
        String classDescription = "";
        String classElementText = classElement.text();
        try {

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

            int indexOfOpeningParenthesis = classTimeText.indexOf('(');
            int indexOfClosingParenthesis = classTimeText.indexOf(')');

            // Extract the time
            String time = classTimeText.substring(0, indexOfOpeningParenthesis).trim();

            // Extract the teacher
            String teacher = classTimeText.substring(indexOfOpeningParenthesis + 1, indexOfClosingParenthesis);

            // Build and return the class object
            List<DummyItem> dummyItemList = new ArrayList<>();
            dummyItemList.add(new DummyItem(day, DanceClassTime.create(time), Schools.ADI_SCHOOL, teacher, level));
            return new ExtractorResults(dummyItemList);
        }
        catch (Exception e) {
            String errorMessage = "Excluded: elementText:"+ classElement.text()
                    + " classType: " + classType
                    + " classText: " + classText
                    + " levelText: " + levelText
                    + " classDescription: " + classDescription
                    + "because of an exception: " + e.getMessage();
            return new ExtractorResults(errorMessage);
        }
    }

    @Override
    protected ExtractorResults getAdditionalElements() {
        List<DummyItem> dummyItemList = new ArrayList<>();
        dummyItemList.add(new DummyItem("Thursday", DanceClassTime.create("6:30pm-8pm"), Schools.ADI_SCHOOL, "Kara", DanceClassLevel.Intermediate));
        return new ExtractorResults(dummyItemList);
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