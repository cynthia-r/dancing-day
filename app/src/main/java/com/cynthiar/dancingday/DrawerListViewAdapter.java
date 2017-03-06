package com.cynthiar.dancingday;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cynthiar.dancingday.dummy.DummyItem;
import com.cynthiar.dancingday.dummy.time.DanceClassTime;

import java.util.List;

/**
 * Created by CynthiaR on 3/5/2017.
 */

public class DrawerListViewAdapter extends BaseAdapter {
    private String[] mValues;
    private Context mContext;
    private LayoutInflater mInflater;

    public DrawerListViewAdapter(String[] items, Context context) {
        mValues = items;
        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mValues.length;
    }

    //2
    @Override
    public Object getItem(int position) {
        return mValues[position];
    }

    //3
    @Override
    public long getItemId(int position) {
        return position;
    }

    //4
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get view for row item
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.drawer_list_item, parent, false);
        }

        TextView mMenuItemView = (TextView) convertView.findViewById(R.id.drawer_menu_item_text);
        ImageView mMenuImageView = (ImageView) convertView.findViewById(R.id.drawer_menu_item_image);

        String menuItemValue = mValues[position];
        mMenuItemView.setText(menuItemValue);
        mMenuImageView.setImageResource(R.mipmap.ic_launcher);

        /*convertView.setClickable(true);
        convertView.setFocusable(true);*/

        return convertView;
    }
}
