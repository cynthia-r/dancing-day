package com.cynthiar.dancingday.model;

import com.cynthiar.dancingday.SettingsActivity;
import com.cynthiar.dancingday.model.classActivity.ClassActivity;
import com.cynthiar.dancingday.database.ClassActivityDao;
import com.cynthiar.dancingday.model.time.DanceClassTime;

import org.joda.time.LocalTime;

import java.util.Iterator;
import java.util.List;

/**
 * A dummy item representing a piece of content.
 */
public class DummyItem {
    private static final String KEY_SEPARATOR = "_";

    public final String day;
    public final String teacher;
    public final DanceClassLevel level;
    public final DanceClassTime danceClassTime;
    public Schools.DanceSchool school;
    private ClassActivityDao classActivityDao;

    public DummyItem(String day, DanceClassTime time, Schools.DanceSchool school, String teacher, DanceClassLevel level) {
        this.day = null == day ? "" : day;
        this.danceClassTime = time;
        this.school = school;
        this.teacher = null == teacher ? "" : teacher;
        this.level = level;
        this.classActivityDao = new ClassActivityDao();
    }

    /*
        Checks whether the class is about to start.
        The time range is 1h30 before to 10 minutes after.
     */
    public boolean isNow() {
        // Always return true in test mode
        if (SettingsActivity.testModeOn)
            return true;

        // Check the day and time
        LocalTime currentTime = LocalTime.now();
        return (this.day == DummyUtils.getCurrentDay()
            && this.danceClassTime.startTime.minusMinutes(90).isBefore(currentTime)
            && this.danceClassTime.startTime.plusMinutes(10).isAfter(currentTime));
    }

    public boolean activityExists() {
        // Retrieve the list of past activities matching this dance class
        List<ClassActivity> activityList = this.classActivityDao.getActivityListByDanceClass(this.toKey());
        if (null == activityList || activityList.isEmpty())
            return false;

        // Check if there is an existing activity for this class
        Iterator<ClassActivity> activityIterator = activityList.iterator();
        boolean activityExists = false;
        while (activityIterator.hasNext() && !activityExists) {
            ClassActivity activity = activityIterator.next();
            activityExists = activity.isCurrent();
        }

        // Return whether the activity exists
        return activityExists;
    }

    @Override
    public String toString() {
        return school.Name;
    }

    /*
        Returns a unique representation of this dance class.
     */
    public String toKey() {
        return DummyUtils.join(DummyItem.KEY_SEPARATOR, this.day, this.danceClassTime.toString(), this.school.toString(), this.teacher, this.level.toString());
    }

    public static DummyItem fromKey(String danceClassKey) {
        if (null == danceClassKey || danceClassKey.isEmpty())
            return null;

        String[] keyElements = danceClassKey.split(DummyItem.KEY_SEPARATOR);
        if (null == keyElements || 5 != keyElements.length)
            return null;

        return DummyItem.fromStrings(keyElements[0], keyElements[1], keyElements[2], keyElements[3], keyElements[4]);
    }

    public static DummyItem fromStrings(String day, String time, String school, String teacher, String level) {
        return new DummyItem(day, DanceClassTime.create(time), Schools.DanceSchool.fromString(school), teacher, DummyUtils.tryParseLevel(level));
    }
}
