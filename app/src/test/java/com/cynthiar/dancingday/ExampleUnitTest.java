package com.cynthiar.dancingday;

import com.cynthiar.dancingday.dummy.DanceClassTime;
import com.cynthiar.dancingday.dummy.DummyUtils;
import com.cynthiar.dancingday.dummy.comparer.IntComparer;

import org.joda.time.LocalTime;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    private class CustomStringComparer implements DummyUtils.IComparer<String> {

        @Override
        public int compare(String elem1, String elem2) {
            return new IntComparer().compare(elem1.length(), elem2.length());
        }
    }

    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void quickSortIntegers() {
        Integer[] inputArray = { 2, 3, 5, 1, 4};
        Integer[] outputArray = { 1, 2, 3, 4, 5};
        new DummyUtils<Integer>(inputArray, new IntComparer()).quickSort();
        assertArrayEquals(outputArray, inputArray);
    }

    @Test
    public void quickSortStrings() {
        String[] inputArray = { "abc", "ab", "abcd", "a", "ab"};
        String[] outputArray = { "a", "ab", "ab", "abc", "abcd"};
        new DummyUtils<String>(inputArray, new CustomStringComparer()).quickSort();
        assertArrayEquals(outputArray, inputArray);
    }

    @Test
    public void testParseTime() {
        String[] inputTimes = {
                "7:45 - 9:15PM\n",
                "10 am\n",
                "6-7 pm\n",
                "10:00-11:30am\n",
                "4:00-5:30 pm\n",
                "8:00 - 9:15PM\n",
                "11:30 am-12:30 pm",
                "7:30-9AM",
                "12-1:30 am"
        };
        DanceClassTime[] expectedDanceClassTimes = {
                new DanceClassTime(19, 45, 21, 15),
                new DanceClassTime(10, 00, 11, 30),
                new DanceClassTime(18, 00, 19, 00),
                new DanceClassTime(10, 00, 11, 30),
                new DanceClassTime(16, 00, 17, 30),
                new DanceClassTime(20, 00, 21, 15),
                new DanceClassTime(11, 30, 12, 30),
                new DanceClassTime(7, 30, 9, 00),
                new DanceClassTime(0, 0, 1, 30),
        };

        for (int i=0;i<inputTimes.length;i++
             ) {
            String inputTime = inputTimes[i];
            DanceClassTime expectedDanceClassTime = expectedDanceClassTimes[i];
            DanceClassTime parsedDanceClassTime = DummyUtils.parseTime(inputTime);
            assertEquals(expectedDanceClassTime.startTime, parsedDanceClassTime.startTime);
            assertEquals(expectedDanceClassTime.endTime, parsedDanceClassTime.endTime);
        }
    }
}