package com.cynthiar.dancingday.dummy;

import android.support.v4.util.Pair;

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
    public final DummyItem[] ITEMS =
    {
        new DummyItem("Monday","7PM-8:30PM","KDC","Lindsay",DanceClassLevel.BeginnerIntermediate),
        new DummyItem("Tuesday", "6:30PM-8PM","PNB Bellevue","Landis",DanceClassLevel.BeginnerIntermediate),
        new DummyItem("Thursday","6PM-7PM","KDC","Jerri",DanceClassLevel.Beginner),
        new DummyItem("Thursday","6-30PM-8PM","ADI","Kara",DanceClassLevel.Intermediate),
        new DummyItem("Friday", "6:30PM-8PM","PNB Seattle","Landis",DanceClassLevel.BeginnerIntermediate),
    };

    /**
     * An array of sample (dummy) items.
     */
    public final DummyItem[] ITEMS2 =
    {
        new DummyItem("Monday","7PM-8:30PM","KDC","Jerri",DanceClassLevel.BeginnerIntermediate),
        new DummyItem("Wednesday", "6:30PM-8PM","PNB Bellevue","Landis",DanceClassLevel.BeginnerIntermediate),
        new DummyItem("Thursday","6:30PM-8PM","ADI","Kara",DanceClassLevel.Intermediate),
        new DummyItem("Saturday","10AM-11:30AM","PNB Seattle","Landis",DanceClassLevel.BeginnerIntermediate)
    };

    /**
     * An array of sample (dummy) items.
     */
    public final DummyItem[] ITEMS3 =
    {
            new DummyItem("Wednesday", "6:30PM-8PM","PNB Bellevue","Landis",DanceClassLevel.BeginnerIntermediate),
            new DummyItem("Wednesday", "8PM-9PM","KDC","Amy",DanceClassLevel.BeginnerIntermediate),
            new DummyItem("Thursday","6:30PM-8PM","ADI","Kara",DanceClassLevel.Intermediate),
            new DummyItem("Saturday","10AM-11:30AM","PNB Seattle","Landis",DanceClassLevel.BeginnerIntermediate),
            new DummyItem("Saturday","10AM-11:30AM","KDC","Jerri",DanceClassLevel.BeginnerIntermediate),
            new DummyItem("Saturday","11-20AM-12:50PM","ADI","Kara",DanceClassLevel.Intermediate)
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

}
