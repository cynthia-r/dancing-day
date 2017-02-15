package com.cynthiar.dancingday.dummy;

import android.support.v4.util.Pair;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DummyContent {

    public static final String[] DAYS_OF_THE_WEEK = {
            "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"
    };

    /**
     * An array of sample (dummy) items.
     */
    public static final DummyItem[] ITEMS =
    {
        new DummyItem("Monday","7PM-8:30PM","KDC","Lindsay","Beg-Int"),
        new DummyItem("Tuesday", "6:30PM-8PM","PNB Bellevue","Landis","Beg-Int"),
        new DummyItem("Thursday","6PM-7PM","KDC","Jerri","Beginner"),
        new DummyItem("Thursday","6-30PM-8PM","ADI","Kara","Intermediate"),
        new DummyItem("Friday", "6:30PM-8PM","PNB Seattle","Landis","Beg-Int"),
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

    /**
     * An array of sample (dummy) items.
     */
    public static final DummyItem[] ITEMS3 =
    {
            new DummyItem("Wednesday", "6:30PM-8PM","PNB Bellevue","Landis","Beg-Int"),
            new DummyItem("Wednesday", "8PM-9PM","KDC","Amy","Beg-Int"),
            new DummyItem("Thursday","6:30PM-8PM","ADI","Kara","Intermediate"),
            new DummyItem("Saturday","10AM-11:30AM","PNB Seattle","Landis","Beg-Int"),
            new DummyItem("Saturday","10AM-11:30AM","KDC","Jerri","Beg-Int"),
            new DummyItem("Saturday","11-20AM-12:50PM","ADI","Kara","Intermediate")
    };

    public static final Pair<String, String>[] CLASS_AND_SELECTORS = new Pair[]{
            new Pair<>("https://www.americandanceinstitute.com/ballet-classes/",
                    /*".tve_twc .thrv_content_container_shortcode , .tcb-flex-col:nth-child(1)",*/
                    ".tve_twc .tve_empty_dropzone , .tcb-flex-col.tve_clearfix:nth-child(1)")
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
