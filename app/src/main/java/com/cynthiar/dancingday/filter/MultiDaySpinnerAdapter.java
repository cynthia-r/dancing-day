package com.cynthiar.dancingday.filter;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by CynthiaR on 2/25/2017.
 */

public class MultiDaySpinnerAdapter extends ArrayAdapter<String> {

    private Context context;
    private List<String> groupList;

    public MultiDaySpinnerAdapter(Context context, int textViewResourceId,
                                  List<String> values) {
        super(context, textViewResourceId, values);
        this.context = context;
        this.groupList = values;
    }

    public int getCount(){
        return groupList.size();
    }

    public String getItem(int position){
        return groupList.get(position);
    }

    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView view = new TextView(context);
        view.setTextColor(Color.BLACK);
        view.setGravity(Gravity.CENTER);
        view.setText(groupList.get(position));

        return view;
    }

    //View of Spinner on dropdown Popping
    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        TextView view = new TextView(context);
        view.setTextColor(Color.BLACK);
        view.setText(groupList.get(position));
        view.setHeight(60);

        return view;
    }
}
