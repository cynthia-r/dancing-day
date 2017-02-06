package com.cynthiar.dancingday.dummy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DummyContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final DummyItem[] ITEMS =
    {
        new DummyItem("Monday","7PM-8:30PM","KDC","Jerri","Beg-Int"),
        new DummyItem("Wednesday", "6:30PM-8PM","PNB Bellevue","Landis","Beg-Int")
    };

    /**
     * An array of sample (dummy) items.
     */
    public static final DummyItem[] ITEMS2 =
    {
            new DummyItem("Monday","7PM-8:30PM","KDC","Jerri","Beg-Int"),
            new DummyItem("Wednesday", "6:30PM-8PM","PNB Bellevue","Landis","Beg-Int"),
            new DummyItem("Thursday","6:30PM-8PM","ADI","Kara","Intermediate"),
            new DummyItem("Saturday","10AM-11:30AM","PNB Seattle","Landis","Beg-Int")
    };

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class DummyItem {
        public final String day;
        public final String time;
        public final String school;
        public final String teacher;
        public final String level;

        public DummyItem(String day, String time, String school, String teacher, String level) {
            this.day = day;
            this.time = time;
            this.school = school;
            this.teacher = teacher;
            this.level = level;
        }

        @Override
        public String toString() {
            return school;
        }
    }
}
