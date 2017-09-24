package com.cynthiar.dancingday.model.extractor;

import android.content.Context;

import com.cynthiar.dancingday.model.DanceClassLevel;
import com.cynthiar.dancingday.model.DummyContent;
import com.cynthiar.dancingday.model.DummyItem;
import com.cynthiar.dancingday.model.Schools;
import com.cynthiar.dancingday.model.time.DanceClassTime;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Robert on 14/03/2017.
 */

public class ExitSpaceDanceClassExtractor extends HtmlDanceClassExtractor {
    private static final String mainSelector = "#ballet-content .studio-onetwo,.studio-three";

    public ExitSpaceDanceClassExtractor(Context context) {
        super(context);
    }

    @Override
    public String getKey() {
        return "ExitSpace";
    }

    @Override
    public List<String> getSchoolList() {
        List<String> schoolList = new ArrayList<>();
        schoolList.add("ExitSpace"); // TODO consts
        return schoolList;
    }

    @Override
    public String getUrl() {
        return "http://www.exitspacedance.com/exitSpaceAdult/classes/adultschedule.php";
    }

    @Override
    protected String getSelector() {
        return ExitSpaceDanceClassExtractor.mainSelector;
    }

    /*@Override
    protected boolean validateElements(Elements elements) {
        // There should be 5 studios
        return 5 == elements.size();
    }*/ // TODO we are getting 9 of them

    @Override
    protected List<DummyItem> parseBaseElement(int elementIndex, Element studioElement) {
        // Base element is a studio
        List<DummyItem> dummyItemList = new ArrayList<>();

        // Extract the company: company may be in studio A or B at the Nest
        Schools.DanceSchool school = (elementIndex <= 2) ? Schools.EXIT_SPACE_SCHOOL : Schools.THE_NEST_SCHOOL;

        // Get the days
        Elements dayElements = studioElement.getElementsByClass("day");
        if (null == dayElements || 0 == dayElements.size())
            return new ArrayList<>();

        // Loop through each day
        for (int i=0; i < dayElements.size(); i++) {
            // Extract day
            String day = DummyContent.DAYS_OF_THE_WEEK[i];

            // Get the day element
            Element dayElement = dayElements.get(i);

            // Extract classes
            Elements classElements = dayElement.getElementsByClass("inner-container");
            if (null == classElements || 0 == classElements.size())
                continue;

            // Loop through each class
            for (int j=0; j < classElements.size(); j++) {
                // Get the class element
                Element classElement = classElements.get(j);

                // Get the class info
                Elements classInfoElements = classElement.getElementsByTag("span");
                if (null == classInfoElements || 0 == classInfoElements.size())
                    continue;

                // Extract the level
                Element levelElement = classInfoElements.get(1);
                String levelString = levelElement.text();

                // Check that it's a ballet class
                if (!levelString.contains("Ballet") && !levelString.contains("Pointe"))
                    continue;

                // Parse the level
                DanceClassLevel danceClassLevel = this.parseLevel(levelString);

                // Extract the time
                Element timeElement = classInfoElements.get(0);
                String timeString = timeElement.text();
                DanceClassTime danceClassTime = DanceClassTime.create(timeString);

                // Extract the teacher
                Element teacherElement = classInfoElements.get(2);
                String teacher = teacherElement.text();

                // Initialize the class item
                DummyItem classItem = new DummyItem(day, danceClassTime, school, teacher, danceClassLevel);
                dummyItemList.add(classItem);
            }
        }
        return dummyItemList;
    }

    private DanceClassLevel parseLevel(String levelText) {
        if (levelText.contains("Pointe"))
            return DanceClassLevel.Pointe;
        if (levelText.contains("Open"))
            return DanceClassLevel.OpenLevel;
        if (!levelText.contains("Beg/Int"))
            return DanceClassLevel.BeginnerIntermediate;
        if (levelText.contains("Int"))
            return DanceClassLevel.Intermediate;
        if (levelText.contains("Beg") || levelText.contains("Basic"))
            return DanceClassLevel.Beginner;
        return DanceClassLevel.Unknown;
    }
}
