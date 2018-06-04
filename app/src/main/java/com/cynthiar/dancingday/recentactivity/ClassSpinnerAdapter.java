package com.cynthiar.dancingday.recentactivity;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.cynthiar.dancingday.SpinnerAdapter;
import com.cynthiar.dancingday.model.DummyItem;
import com.cynthiar.dancingday.model.DummyUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter for the class spinner.
 */

public class ClassSpinnerAdapter extends SpinnerAdapter implements Filterable {

    private List<DummyItem> danceClassList;
    private List<DummyItem> allDanceClassList;

    public ClassSpinnerAdapter(Context context, int layoutResourceId, int dropDownViewResourceId, List<DummyItem> danceClassList, List<DummyItem> allDanceClassList) {
        super(context, layoutResourceId, dropDownViewResourceId, new ArrayList<String>());
        setValues(danceClassList);
        this.allDanceClassList = allDanceClassList;
    }

    private String toClassSpinnerItem(String danceClassKey) {
        DummyItem dummyItem = DummyItem.fromKey(danceClassKey);
        return DummyUtils.join("_", dummyItem.day, dummyItem.teacher, dummyItem.danceClassTime.toString());
    }

    public void setValues(List<DummyItem> danceClassList) {
        this.danceClassList = danceClassList;

        List<String> values = new ArrayList<>();
        for (DummyItem danceClass:danceClassList
                ) {
            values.add(toClassSpinnerItem(danceClass.toKey()));
        }
        this.clear();
        this.addAll(values);
    }

    public DummyItem getSelectedDanceClass(int position) {
        return this.danceClassList.get(position);
    }

    @Override
    public Filter getFilter() {
        return new ClassSpinnerFilter(this, allDanceClassList);
    }
}
