package com.cynthiar.dancingday.dummy;

public class SchoolPropertySelector implements DanceClassPropertySelector{
    public String GetProperty(DummyContent.DummyItem dummyItem){
        return dummyItem.school;
    }
}
