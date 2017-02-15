package com.cynthiar.dancingday;

import com.cynthiar.dancingday.dummy.DummyUtils;
import com.cynthiar.dancingday.dummy.comparer.IntComparer;

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
}