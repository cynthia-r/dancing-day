package com.cynthiar.dancingday;

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

public class SpinnerAdapter extends ArrayAdapter<String> {

    private List<String> groupList;

    public SpinnerAdapter(Context context, int layoutResourceId,
                          int dropDownViewResourceId, List<String> values) {
        super(context, layoutResourceId, values);
        this.groupList = values;
        this.setDropDownViewResource(dropDownViewResourceId);
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

}
