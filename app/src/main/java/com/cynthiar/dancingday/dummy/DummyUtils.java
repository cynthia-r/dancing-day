package com.cynthiar.dancingday.dummy;

import android.content.Context;
import android.widget.Toast;

import com.cynthiar.dancingday.dummy.propertySelector.DanceClassPropertySelector;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Robert on 14/02/2017.
 */

public class DummyUtils<T> {

    public interface IComparer<T> {
        public int compare(T elem1, T elem2);
    }

    private IComparer<T> mComparer;
    private T[] mArray;

    public DummyUtils(T[] array, IComparer comparer) {
        mComparer = comparer;
        mArray = array;
    }

    public static HashMap<String, List<DummyContent.DummyItem>> GroupBy(DanceClassPropertySelector danceClassPropertySelector, List<DummyContent.DummyItem> dummyItemList)
    {
        HashMap<String, List<DummyContent.DummyItem>> dummyItemDictionary = new HashMap<String, List<DummyContent.DummyItem>>();
        for (DummyContent.DummyItem dummyItem:dummyItemList
                ) {
            // Retrieve the property that we group by on
            String property = danceClassPropertySelector.getProperty(dummyItem);

            // Initialize the list of items for this entry if needed
            List<DummyContent.DummyItem> dummyItemListForGroup = dummyItemDictionary.get(property);
            if (null == dummyItemListForGroup)
                dummyItemListForGroup = new ArrayList<DummyContent.DummyItem>();

            // Add the current item to this entry
            dummyItemListForGroup.add(dummyItem);
            dummyItemDictionary.put(property, dummyItemListForGroup);
        }
        return dummyItemDictionary;
    }

    public void quickSort() {
        quickSort(0, mArray.length - 1);
    }

    private void quickSort(int lo, int hi) {
        if (lo < hi) {
            int p = partition(mArray, mComparer, lo, hi);
            quickSort(lo, p);
            quickSort(p+1, hi);
        }
    }

    private int partition(T[] collection, IComparer<T> comparer, int lo, int hi) {
        T pivot = collection[lo];
        int i = lo - 1;
        int j = hi + 1;
        while (true) {
            do {
                i = i+1;
            } while (comparer.compare(collection[i], pivot) < 0);
            do {
                j = j-1;
            } while (comparer.compare(collection[j], pivot) > 0);
            if (i >= j)
                return j;
            T temp = collection[i];
            collection[i] = collection[j];
            collection[j] = temp;
        }
    }

    public static String getCurrentDay() {
        Calendar calendar = Calendar.getInstance();
        int dayToFilter = calendar.get(Calendar.DAY_OF_WEEK);
        return getCurrentDay(dayToFilter);
    }

    public static String getCurrentDay(int calendarDayPosition) {
        String dayString = "";
        switch (calendarDayPosition) {
            case Calendar.MONDAY:
                dayString = DummyContent.DAYS_OF_THE_WEEK[0];
                break;
            case Calendar.TUESDAY:
                dayString = DummyContent.DAYS_OF_THE_WEEK[1];
                break;
            case Calendar.WEDNESDAY:
                dayString = DummyContent.DAYS_OF_THE_WEEK[2];
                break;
            case Calendar.THURSDAY:
                dayString = DummyContent.DAYS_OF_THE_WEEK[3];
                break;
            case Calendar.FRIDAY:
                dayString = DummyContent.DAYS_OF_THE_WEEK[4];
                break;
            case Calendar.SATURDAY:
                dayString = DummyContent.DAYS_OF_THE_WEEK[5];
                break;
            case Calendar.SUNDAY:
                dayString = DummyContent.DAYS_OF_THE_WEEK[6];
                break;
            default:
                break;
        }
        return dayString;
    }

    public static String readAllStream(InputStream inputStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder out = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            out.append(line);
        }
        return out.toString();
    }

    public File writeToStorage(InputStream inputStream, String filePath) throws IOException {
        InputStreamReader input = new InputStreamReader(inputStream, "UTF-8");
        File file = null;
        try {
            file = new File(filePath);
            OutputStream output = new FileOutputStream(file);
            try {
                try {
                    char[] buffer = new char[4 * 1024]; // or other buffer size
                    int read;

                    while ((read = input.read(buffer)) != -1) {
                        byte[] bytes = new byte[buffer.length];
                        for (int i=0; i < bytes.length; i++)
                            bytes[i] = (byte)buffer[i];
                        output.write(bytes, 0, read);
                    }
                    output.flush();
                } finally {
                    output.close();
                }
            } catch (Exception e) {
                e.printStackTrace(); // handle exception, define IOException and others
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            input.close();
        }
        return file;
    }

    public static void toast(Context context) {
        CharSequence text = "Toast";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }
}
