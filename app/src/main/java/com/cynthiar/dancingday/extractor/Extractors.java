package com.cynthiar.dancingday.extractor;

import android.content.Context;

import com.cynthiar.dancingday.MultiDayFragment;
import com.cynthiar.dancingday.model.DanceClassLevel;
import com.cynthiar.dancingday.model.propertySelector.DanceClassPropertySelector;
import com.cynthiar.dancingday.model.propertySelector.LevelPropertySelector;
import com.cynthiar.dancingday.model.propertySelector.SchoolPropertySelector;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Robert on 13/02/2017.
 */

public class Extractors {

    private static Extractors extractorsInstance;

    private static volatile Object syncObject = new Object();

    private Extractors(DanceClassExtractor[] danceClassExtractors) {
        this.extractors = danceClassExtractors;
    }

    private DanceClassExtractor[] extractors;

    public static Extractors getInstance(Context context) {
        if (null != extractorsInstance)
            return extractorsInstance;

        synchronized (syncObject) {
            if (null == extractorsInstance) {
                DanceClassExtractor[] danceClassExtractors = loadExtractors(context);
                extractorsInstance = new Extractors(danceClassExtractors);
            }
        }
        return extractorsInstance;
    }

    public static Extractors getInstance() { return extractorsInstance; }

    private static DanceClassExtractor[] loadExtractors(Context context) {
        DanceClassExtractor[] danceClassExtractors = new DanceClassExtractor[] {
                new ADIDanceClassExtractor(context),
                //new PNBDanceClassExtractor(context),
                new PNBBellevueDanceClassExtractor(context),
                new PNBSeattleDanceClassExtractor(context),
                new KDCDanceClassExtractor(context),
                new WDCDanceClassExtractor(context),
                new ExitSpaceDanceClassExtractor(context)
        };
        return danceClassExtractors;
    }

    public DanceClassExtractor[] getExtractors() {
        return this.extractors;
    }

    public DanceClassExtractor getExtractor(String key) {
        for (int i = 0; i < this.extractors.length; i++
             ) {
            DanceClassExtractor extractor = this.extractors[i];
            if (extractor.getKey().equals(key))
                return extractor;
        }
        return null;
    }

    public DanceClassPropertySelector getSelector(String key) {
        switch (key) {
            case MultiDayFragment.SCHOOL_SPINNER_PREFIX:
                return new SchoolPropertySelector();
            case MultiDayFragment.LEVEL_SPINNER_PREFIX:
                return new LevelPropertySelector();
            default:
                return null;
        }
    }

    public List<String> getSchoolList(boolean includeAll) {
        List<String> propertyList = new ArrayList<>();
        if (includeAll)
            propertyList.add(MultiDayFragment.ALL_KEY.concat(" schools"));
        for (DanceClassExtractor danceClassExtractor : this.extractors
             ) {
            propertyList.addAll(danceClassExtractor.getSchoolList());
        }
        return propertyList;
    }

    public List<String> getLevelList() {
        List<String> propertyList = new ArrayList<>();
        propertyList.add(MultiDayFragment.ALL_KEY.concat(" levels"));
        for (DanceClassLevel danceClassLevel : DanceClassLevel.values()
                ) {
            if ((DanceClassLevel.Unknown != danceClassLevel)
                && (DanceClassLevel.Children != danceClassLevel))
                propertyList.add(danceClassLevel.toString());
        }
        return propertyList;
    }
}
