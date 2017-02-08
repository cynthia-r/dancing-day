package com.cynthiar.dancingday.dummy;

public class DayPropertySelector implements DanceClassPropertySelector{
    public String GetProperty(DummyContent.DummyItem dummyItem){
        return dummyItem.day;
    }
}
