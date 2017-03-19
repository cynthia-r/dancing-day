package com.cynthiar.dancingday.dummy;

import android.content.Context;
import android.widget.Toast;

import com.cynthiar.dancingday.dummy.comparer.SingleDayDummyItemComparer;
import com.cynthiar.dancingday.dummy.propertySelector.DanceClassPropertySelector;
import com.cynthiar.dancingday.dummy.propertySelector.DayPropertySelector;
import com.cynthiar.dancingday.dummy.time.DanceClassTime;
import com.cynthiar.dancingday.dummy.time.TimeHalf;
import com.cynthiar.dancingday.dummy.time.TimeParts;
import com.cynthiar.dancingday.filter.IItemFilter;

import org.joda.time.LocalTime;

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

    public static HashMap<String, List<DummyItem>> GroupBy(DanceClassPropertySelector danceClassPropertySelector, List<DummyItem> dummyItemList)
    {
        HashMap<String, List<DummyItem>> dummyItemDictionary = new HashMap<>();
        for (DummyItem dummyItem:dummyItemList
                ) {
            // Retrieve the property that we group by on
            String property = danceClassPropertySelector.getProperty(dummyItem);

            // Initialize the list of items for this entry if needed
            List<DummyItem> dummyItemListForGroup = dummyItemDictionary.get(property);
            if (null == dummyItemListForGroup)
                dummyItemListForGroup = new ArrayList<>();

            // Add the current item to this entry
            dummyItemListForGroup.add(dummyItem);
            dummyItemDictionary.put(property, dummyItemListForGroup);
        }
        return dummyItemDictionary;
    }

    public static HashMap<String, List<DummyItem>> GroupByWithFilter(DanceClassPropertySelector danceClassPropertySelector, List<DummyItem> dummyItemList, IItemFilter itemFilter)
    {
        HashMap<String, List<DummyItem>> dummyItemDictionary = new HashMap<>();
        for (DummyItem dummyItem:dummyItemList
                ) {
            // Check if the item should be filtered
            if (itemFilter.shouldFilter(dummyItem))
                continue;

            // Retrieve the property that we group by on
            String property = danceClassPropertySelector.getProperty(dummyItem);

            // Initialize the list of items for this entry if needed
            List<DummyItem> dummyItemListForGroup = dummyItemDictionary.get(property);
            if (null == dummyItemListForGroup)
                dummyItemListForGroup = new ArrayList<>();

            // Add the current item to this entry
            dummyItemListForGroup.add(dummyItem);
            dummyItemDictionary.put(property, dummyItemListForGroup);
        }
        return dummyItemDictionary;
    }

    public static List<String> sortAndRotateGroups(Context context, HashMap<String, List<DummyItem>> dummyItemMap, DanceClassPropertySelector propertySelector) {
        List<String> groupList = new ArrayList<>(dummyItemMap.keySet());

        // Sort the list
        String[] unsortedGroups = new String[groupList.size()];
        String[] sortedGroups = dummyItemMap.keySet().toArray(unsortedGroups);
        new DummyUtils<>(sortedGroups, propertySelector.getComparer()).quickSort();

        // Rotate days (tomorrow should be first)
        if (propertySelector instanceof DayPropertySelector) {
            String tomorrow = DummyUtils.getTomorrow();
            // Find the position of tomorrow
            int k=0;
            while (k < sortedGroups.length && !sortedGroups[k].equals(tomorrow)) {
                k++;
            }

            // Copy to the list back again
            groupList = new ArrayList<>();
            for (int j=k; j < sortedGroups.length; j++
                    ) {
                String group = sortedGroups[j];
                groupList.add(group);
            }
            for (int j=0; j < k; j++
                    ) {
                String group = sortedGroups[j];
                groupList.add(group);
            }
        }
        else {
            // Just copy the list back again
            groupList = new ArrayList<>();
            for (int j=0; j < sortedGroups.length; j++
                    ) {
                String group = sortedGroups[j];
                groupList.add(group);
            }
        }

        // Return the group list
        return groupList;
    }

    public static void sortItemMap(HashMap<String, List<DummyItem>> dummyItemMap) {
        for (String key:dummyItemMap.keySet()
                ) {
            List<DummyItem> sortedItemList = DummyUtils.sortItemList(dummyItemMap.get(key));
            dummyItemMap.put(key, sortedItemList);
        }
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

    public static String getTomorrow() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 1);
        int dayToFilter = calendar.get(Calendar.DAY_OF_WEEK);
        return getCurrentDay(dayToFilter);
    }

    private static String getCurrentDay(int calendarDayPosition) {
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

    public static void toast(Context context, String message) {
        CharSequence text = (null == message) ? "Toast" : message;
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    public static DanceClassTime parseTime(String timeString) {

        if (null == timeString || timeString.isEmpty())
            return null;

        String cleanTimeString = DummyUtils.clean(timeString); // todo remove line breaks if needed
        String[] timeParts = cleanTimeString.split("-");

        if (null == timeParts || 0 == timeParts.length)
            return null;

        LocalTime startTime;
        LocalTime endTime;

        String startTimePart = timeParts[0];
        TimeParts startTimeParts;
        TimeParts endTimeParts;
        startTimeParts = DummyUtils.parseTimeStringIntoParts(startTimePart);

        if (null == startTimeParts)
            return null;

        if (1 == timeParts.length) {
            startTime = new LocalTime(startTimeParts.hours, startTimeParts.minutes);
            endTime = startTime.plusHours(1).plusMinutes(30);
        }
        else {
            String endTimePart = timeParts[1];
            endTimeParts = DummyUtils.parseTimeStringIntoParts(endTimePart);

            if (null == endTimeParts)
                return null;

            endTime = new LocalTime(endTimeParts.hours, endTimeParts.minutes);

            if (TimeHalf.Undetermined == startTimeParts.timeHalf) {
                if (TimeHalf.Pm == endTimeParts.timeHalf
                        && 12 > startTimeParts.hours) {
                    startTimeParts.hours += 12;
                } else if (TimeHalf.Am == endTimeParts.timeHalf
                                && 12 == startTimeParts.hours) {
                            startTimeParts.hours = 0;
                        }
            }
            startTime = new LocalTime(startTimeParts.hours, startTimeParts.minutes);
        }

        DanceClassTime danceClassTime = new DanceClassTime(startTime, endTime);
        return danceClassTime;
    }

    private static TimeParts parseTimeStringIntoParts(String timeString) {
        TimeParts timeParts;

        int indexOfAmIndicator = timeString.toLowerCase().indexOf("am");
        if (0 <= indexOfAmIndicator) {
            String timePortion = timeString.substring(0, indexOfAmIndicator);
            timeParts = DummyUtils.parseTimePart(timePortion, TimeHalf.Am);
            return timeParts;
        }

        int indexOfPmIndicator = timeString.toLowerCase().indexOf("pm");
        if (0 <= indexOfPmIndicator) {
            String timePortion = timeString.substring(0, indexOfPmIndicator);
            timeParts = DummyUtils.parseTimePart(timePortion, TimeHalf.Pm);
            return timeParts;
        }

        timeParts = DummyUtils.parseTimePart(timeString, TimeHalf.Undetermined);
        return timeParts;
    }

    private static TimeParts parseTimePart(String timePortion, TimeHalf timeHalf) {
        if (null == timePortion || timePortion.isEmpty())
            return null;

        String[] timePortionParts = timePortion.split(":");
        String hoursPart = DummyUtils.clean(timePortionParts[0]);

        if (null == hoursPart || hoursPart.isEmpty())
            return null;

        int hours;
        try {
            hours = Integer.parseInt(hoursPart);
        }
        catch (Exception e) {
            return null;
        }
        if (TimeHalf.Pm == timeHalf && 12 > hours) // PM -> +12 except for 12PM = noon
            hours += 12;

        if (TimeHalf.Am == timeHalf && 12 == hours) // 12AM is midnight
            hours = 0;

        int minutes = 0;
        if (2 == timePortionParts.length) {
            String minutesPart = DummyUtils.clean(timePortionParts[1]);
            if (null != minutesPart && !minutesPart.isEmpty())
                try {
                    minutes = Integer.parseInt(minutesPart);
                } catch (NumberFormatException e) {
                    return null;
                }
        }
        return new TimeParts(hours, minutes, timeHalf);
    }

    public static List<DummyItem> sortItemList(List<DummyItem> dummyItemList) {
        DummyItem[] unsortedItems = new DummyItem[dummyItemList.size()];
        DummyItem[] sortedItems = dummyItemList.toArray(unsortedItems);
        new DummyUtils<>(sortedItems, new SingleDayDummyItemComparer()).quickSort();

        List<DummyItem> sortedItemList = new ArrayList<>();
        for (DummyItem dummyItem:sortedItems
                ) {
            sortedItemList.add(dummyItem);
        }
        return sortedItemList;
    }

    public static String clean(String inputString) {
        return inputString.trim().replaceAll("[^a-zA-Z0-9\\-:]", "");
    }

    public static String join(Character separator, String... stringParams) {
        if (null == stringParams || 0 == stringParams.length)
            return "";

        StringBuilder stringBuilder = new StringBuilder();
        for (String stringParam:stringParams
             ) {
            stringBuilder.append(stringParam);
            stringBuilder.append(separator);
        }
        return stringBuilder.toString();
    }

    public static DanceClassLevel tryParseLevel(String levelString) {
        for (DanceClassLevel danceClassLevel : DanceClassLevel.values()) {
            if (levelString.equals(danceClassLevel.toString()))
                return danceClassLevel;
        }
        return DanceClassLevel.Unknown;
    }
}
