package com.cynthiar.dancingday.dummy.extractor;

/**
 * Created by Robert on 13/02/2017.
 */

public class Extractors {

    public static final DanceClassExtractor[] EXTRACTORS = {
            new ADIDanceClassExtractor(),
            new PNBDanceClassExtractor(),
            new KDCDanceClassExtractor()
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
}
