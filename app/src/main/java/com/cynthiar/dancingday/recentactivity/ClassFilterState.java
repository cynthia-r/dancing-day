package com.cynthiar.dancingday.recentactivity;

import android.widget.DatePicker;
import android.widget.Spinner;

import com.cynthiar.dancingday.model.classActivity.ClassActivity;

import org.joda.time.DateTime;

/**
 * Represents the current state of the class filter.
 */

public class ClassFilterState {
    private Spinner classSpinner;
    private DatePicker datePicker;

    public ClassFilterState(Spinner spinner, DatePicker datePicker) {
        this.classSpinner = spinner;
        this.datePicker = datePicker;
    }

    public String getFilterString() {
        // Initialize the filter string
        String filterString = "";

        // Add the state of the spinner
        int selectedPosition = this.classSpinner.getSelectedItemPosition();
        String classSelected = (String)this.classSpinner.getAdapter().getItem(selectedPosition);
        filterString = filterString.concat(classSelected);

        // Add the state of the date picker
        DateTime activityDate = new DateTime(datePicker.getYear(), datePicker.getMonth() + 1, datePicker.getDayOfMonth(), 00, 00);
        filterString = filterString.concat(ClassSpinnerFilter.FILTER_SEPARATOR).concat(activityDate.toString(ClassActivity.dateTimeFormatter));

        // Return the filter string
        return filterString;
    }
}
