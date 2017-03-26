package com.cynthiar.dancingday.dummy;

import android.content.Context;
import android.content.SharedPreferences;

import com.cynthiar.dancingday.R;
import com.cynthiar.dancingday.dummy.time.DanceClassTime;

import java.util.HashSet;
import java.util.Set;

/**
 * A dummy item representing a piece of content.
 */
public class DummyItem {
    private static final String KEY_SEPARATOR = "-";

    public final String day;
    public final String teacher;
    public final DanceClassLevel level;
    public final DanceClassTime danceClassTime;
    public Schools.DanceSchool school;
    private boolean mIsMarkedAsFavorite;

    public DummyItem(String day, DanceClassTime time, Schools.DanceSchool school, String teacher, DanceClassLevel level) {
        this.day = day;
        this.danceClassTime = time;
        this.school = school;
        this.teacher = teacher;
        this.level = level;
        this.mIsMarkedAsFavorite = false;
    }

    @Override
    public String toString() {
        return school.Name;
    }

    /*
        Returns a unique representation of this dance class.
     */
    public String toKey() {
        return DummyUtils.join('-', this.day, this.danceClassTime.toString(), this.school.toString(), this.teacher, this.level.toString());
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
