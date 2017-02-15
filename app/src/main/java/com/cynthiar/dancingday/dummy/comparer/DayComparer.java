package com.cynthiar.dancingday.dummy.comparer;

import com.cynthiar.dancingday.dummy.DummyContent;
import com.cynthiar.dancingday.dummy.DummyUtils;

/**
 * Created by Robert on 14/02/2017.
 */

public class DayComparer implements DummyUtils.IComparer<String> {
    @Override
    public int compare(String elem1, String elem2) {
        // Find the position of both days in the array
        int elem1Position = findPosition(elem1);
        int elem2Position = findPosition(elem2);

        if (0 > elem1Position || 0 > elem2Position)
            return 0;

        // Compare the positions in the array
        return new IntComparer().compare(elem1Position, elem2Position);
    }

    private int findPosition(String day) {
        int i=0;
        while(!DummyContent.DAYS_OF_THE_WEEK[i].equals(day) && i < DummyContent.DAYS_OF_THE_WEEK.length) {
            i++;
        }
        if (i == DummyContent.DAYS_OF_THE_WEEK.length)
            return -1; // not found
        return i;
    }
}
