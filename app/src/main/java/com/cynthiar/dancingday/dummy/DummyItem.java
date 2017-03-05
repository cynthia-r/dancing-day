package com.cynthiar.dancingday.dummy;

import com.cynthiar.dancingday.dummy.time.DanceClassTime;

/**
 * A dummy item representing a piece of content.
 */
public class DummyItem {
    public final String day;
    //public final String school;
    public final String teacher;
    public final DanceClassLevel level;
    public final DanceClassTime danceClassTime;
    public Schools.DanceSchool school;

    public DummyItem(String day, DanceClassTime time, Schools.DanceSchool school, String teacher, DanceClassLevel level) {
        this.day = day;
        this.danceClassTime = time;
        this.school = school;
        this.teacher = teacher;
        this.level = level;
    }

    @Override
    public String toString() {
        return school.Name;
    }
}
