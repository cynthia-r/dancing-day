package com.cynthiar.dancingday.dummy;

/**
 * A dummy item representing a piece of content.
 */
public class DummyItem {
    public final String day;
    public final String time;
    public final String school;
    public final String teacher;
    public final DanceClassLevel level;
    private DanceClassTime danceClassTime; // TODO set at constructor time

    public DummyItem(String day, String time, String school, String teacher, DanceClassLevel level) {
        this.day = day;
        this.time = time;
        this.school = school;
        this.teacher = teacher;
        this.level = level;
    }

    public DanceClassTime getClassTime() {
        if (null == danceClassTime)
            danceClassTime = DummyUtils.parseTime(this.time);
        return danceClassTime;
    }

    @Override
    public String toString() {
        return school;
    }
}
