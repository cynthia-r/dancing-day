package com.cynthiar.dancingday.filter;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.cynthiar.dancingday.R;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by CynthiaR on 2/25/2017.
 */

public class MultiDaySpinnerAdapter extends ArrayAdapter<String> {

    private Context context;
    private List<String> groupList;
    private LayoutInflater mInflater;

    public MultiDaySpinnerAdapter(Context context, int textViewResourceId,
                                  List<String> values) {
        super(context, textViewResourceId, values);
        this.context = context;
        this.groupList = values;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
        view.setTextColor(Color.WHITE);
        view.setGravity(Gravity.CENTER);
        view.setText(groupList.get(position));

        return view;
    }

    // View of Spinner on dropdown Popping
    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        /*TextView view = new TextView(context);
        view.setTextColor(Color.BLACK);
        view.setGravity(Gravity.CENTER);
        view.setText(groupList.get(position));
        view.setHeight(60);*/

        // Get view for row item
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.spinner_item, parent, false);
        }
        TextView textView = (TextView)convertView.findViewById(R.id.spinner_item_text);
        textView.setText(groupList.get(position));
        return textView;
    }
}
