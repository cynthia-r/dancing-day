package com.cynthiar.dancingday;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.cynthiar.dancingday.model.DummyItem;
import com.cynthiar.dancingday.model.propertySelector.DayPropertySelector;
import com.cynthiar.dancingday.filter.MultiDayFilter;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * Created by CynthiaR on 2/5/2017.
 */

public class MultiDayListViewAdapter extends BaseExpandableListAdapter implements Filterable{
    private List<String> mGroups;
    private HashMap<String, List<DummyItem>> mValues;
    private HashMap<String, List<DummyItem>> mAllValues;
    private Context mContext;
    private LayoutInflater mInflater;

    public MultiDayListViewAdapter(List<String> groupList, HashMap<String, List<DummyItem>> itemMap, HashMap<String, List<DummyItem>> allItemMap, Context context) {
        mGroups = groupList;
        mValues = itemMap;
        mAllValues = allItemMap;
        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setValues(HashMap<String, List<DummyItem>> items) {
        mValues = items;
    }

    public void setGroups(List<String> groupList) {
        mGroups = groupList;
    }

    @Override
    public int getGroupCount() {
        return mGroups.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        String group = mGroups.get(groupPosition);
        return mValues.get(group).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mGroups.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        String group = mGroups.get(groupPosition);
        return mValues.get(group).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        // Get view for row item
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.multi_day_fragment_item_group, parent, false);
        }

        // Set text based on the property that was used for the group by
        TextView mGroupView = (TextView) convertView.findViewById(R.id.multi_day_group);
        String groupValue = mGroups.get(groupPosition);

        // Check if group is a day
        TodayActivity todayActivity = (TodayActivity)mContext;
        if (todayActivity.getCurrentPropertySelector() instanceof DayPropertySelector)
            groupValue = this.getDayGroupValue(groupPosition, groupValue);

        // Set text in the view
        mGroupView.setText(groupValue);

        // Return the row view
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        String group = mGroups.get(groupPosition);
        List<DummyItem> dummyItemList = mValues.get(group);
        SingleDayListViewAdapter singleDayListViewAdapter = new SingleDayListViewAdapter(dummyItemList, this.mContext);
        return singleDayListViewAdapter.getView(childPosition, convertView, parent);
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public Filter getFilter() {
        return new MultiDayFilter(mContext, this, mAllValues);
    }

    private String getDayGroupValue(int groupPosition, String initialValue) {
        // Move the calendar to tomorrow (first element)
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 1);

        // Move the calendar as many days forward
        // as the group position
        for (int i=0; i < groupPosition; i++) {
            calendar.add(Calendar.DATE, 1);
        }

        // Build group string: Day_Name Day_Of_Month
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        StringBuilder builder = new StringBuilder();
        builder.append(initialValue).append(" ").append(dayOfMonth);

        // Return group value
        return builder.toString();
    }
}
