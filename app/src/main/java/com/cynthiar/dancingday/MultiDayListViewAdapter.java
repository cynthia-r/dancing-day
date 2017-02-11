package com.cynthiar.dancingday;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;

import com.cynthiar.dancingday.dummy.DummyContent;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import java.util.Map;

/**
 * Created by CynthiaR on 2/5/2017.
 */

public class MultiDayListViewAdapter extends BaseExpandableListAdapter {
    private List<String> mGroups;
    private HashMap<String, List<DummyContent.DummyItem>> mValues;
    private Context mContext;
    private LayoutInflater mInflater;

    public MultiDayListViewAdapter(List<String> groupList, HashMap<String, List<DummyContent.DummyItem>> itemMap, Context context) {
        mGroups = groupList;
        mValues = itemMap;
        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
        if (convertView == null)
        {
            convertView = mInflater.inflate(R.layout.multi_day_fragment_item_group, parent, false);
        }

        // Set text based on the property that was used for the group by
        TextView mDayView = (TextView) convertView.findViewById(R.id.multi_day_group);
        String groupValue = mGroups.get(groupPosition);
        mDayView.setText(groupValue);

        // Return the row view
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        String group = mGroups.get(groupPosition);
        List<DummyContent.DummyItem> dummyItemList = mValues.get(group);
        SingleDayListViewAdapter singleDayListViewAdapter = new SingleDayListViewAdapter(dummyItemList, this.mContext);
        return singleDayListViewAdapter.getView(childPosition, convertView, parent);
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}