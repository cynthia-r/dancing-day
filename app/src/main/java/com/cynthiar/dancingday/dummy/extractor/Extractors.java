package com.cynthiar.dancingday.dummy.extractor;

import com.cynthiar.dancingday.MultiDayFragment;
import com.cynthiar.dancingday.dummy.DanceClassLevel;
import com.cynthiar.dancingday.dummy.propertySelector.DanceClassPropertySelector;
import com.cynthiar.dancingday.dummy.propertySelector.LevelPropertySelector;
import com.cynthiar.dancingday.dummy.propertySelector.SchoolPropertySelector;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Robert on 13/02/2017.
 */

public class Extractors {

    public static final DanceClassExtractor[] EXTRACTORS = {
            new ADIDanceClassExtractor(),
            new PNBDanceClassExtractor(),
            new KDCDanceClassExtractor(),
            new WDCDanceClassExtractor()
    };

    public static DanceClassExtractor getExtractor(String key) {
        for (int i=0; i < EXTRACTORS.length; i++
             ) {
            DanceClassExtractor extractor = EXTRACTORS[i];
            if (extractor.getKey().equals(key))
                return extractor;
        }
        return null;
    }

    public static DanceClassPropertySelector getSelector(String key) {
        switch (key) {
            case MultiDayFragment.SCHOOL_SPINNER_PREFIX:
                return new SchoolPropertySelector();
            case MultiDayFragment.LEVEL_SPINNER_PREFIX:
                return new LevelPropertySelector();
            default:
                return null;
        }
    }

    public static List<String> getSchoolList() {
        List<String> propertyList = new ArrayList<>();
        propertyList.add(MultiDayFragment.ALL_KEY.concat(" schools"));
        for (DanceClassExtractor danceClassExtractor : EXTRACTORS
             ) {
            propertyList.addAll(danceClassExtractor.getSchoolList());
        }
        return propertyList;
    }

    public static List<String> getLevelList() {
        List<String> propertyList = new ArrayList<>();
        propertyList.add(MultiDayFragment.ALL_KEY.concat(" levels"));
        for (DanceClassLevel danceClassLevel : DanceClassLevel.values()
                ) {
            propertyList.add(danceClassLevel.toString());
        }
        return propertyList;
    }
}
