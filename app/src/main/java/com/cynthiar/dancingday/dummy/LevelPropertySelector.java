package com.cynthiar.dancingday.dummy;

public class LevelPropertySelector implements DanceClassPropertySelector{
    public String GetProperty(DummyContent.DummyItem dummyItem){
        return dummyItem.level;
    }
}
