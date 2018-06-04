package com.cynthiar.dancingday.recentactivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.Spinner;

import com.cynthiar.dancingday.R;
import com.cynthiar.dancingday.SpinnerAdapter;
import com.cynthiar.dancingday.TodayActivity;
import com.cynthiar.dancingday.database.ClassActivityDao;
import com.cynthiar.dancingday.model.DummyItem;
import com.cynthiar.dancingday.model.DummyUtils;
import com.cynthiar.dancingday.model.Schools;
import com.cynthiar.dancingday.model.classActivity.ClassActivity;
import com.cynthiar.dancingday.model.propertySelector.DanceClassPropertySelector;
import com.cynthiar.dancingday.model.propertySelector.SchoolPropertySelector;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Fragment to record a recent activity.
 */

public class RecordActivityFragment extends BaseRecentActivityFragment {
    public static final String TAG = "RecordActivityFragment";
    public static final int REQUEST_CODE = 1;

    private Schools.DanceSchool danceSchool;
    private ClassFilterState classFilterState;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RecordActivityFragment() {
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Get the parent activity
        final TodayActivity parentActivity = (TodayActivity) getActivity();

        // Initialize the data
        classActivityDao = new ClassActivityDao();
        danceSchool = Schools.ADI_SCHOOL;

        // Get the fragment view
        LayoutInflater inflater = parentActivity.getLayoutInflater();
        final View recordActivityView = inflater.inflate(R.layout.record_activity_fragment, null);

        // Get the current list of classes, grouped by school
        List<DummyItem> danceClassList = parentActivity.getCurrentList(false);
        DanceClassPropertySelector danceClassPropertySelector = new SchoolPropertySelector();
        HashMap<String, List<DummyItem>> dummyItemMap = DummyUtils.GroupBy(danceClassPropertySelector, danceClassList);
        DummyUtils.sortItemMap(dummyItemMap);

        // Get the current school list
        List<DummyItem> currentDanceClassList = dummyItemMap.get(danceSchool.Key);

        // Setup the class selection
        final Spinner classSpinner = (Spinner) recordActivityView.findViewById(R.id.classSpinner);
        final ClassSpinnerAdapter classSpinnerAdapter = new ClassSpinnerAdapter(
                parentActivity, R.layout.school_spinner_item, R.layout.spinner_dropdown_item, currentDanceClassList, danceClassList);
        classSpinner.setAdapter(classSpinnerAdapter);
        classSpinner.setSelection(0);

        // Setup the school spinner
        final Spinner schoolSpinner = (Spinner) recordActivityView.findViewById(R.id.schoolSpinner);
        final List<String> schoolSpinnerItemList = new ArrayList<>();
        for (Schools.DanceSchool danceSchool:Schools.SCHOOLS) {
            schoolSpinnerItemList.add(danceSchool.toString());
        }
        SpinnerAdapter spinnerAdapter = new SpinnerAdapter(
                parentActivity, R.layout.school_spinner_item, R.layout.spinner_dropdown_item, schoolSpinnerItemList);
        schoolSpinner.setAdapter(spinnerAdapter);
        schoolSpinner.setOnItemSelectedListener(new SchoolSpinnerListener(schoolSpinnerItemList, classSpinnerAdapter));
        schoolSpinner.setSelection(schoolSpinnerItemList.indexOf(danceSchool.Key));

        // Setup the activity date picker
        DatePicker datePicker = (DatePicker) recordActivityView.findViewById(R.id.activity_date_picker);
        activityDate = DateTime.now();
        datePicker.init(activityDate.getYear(), activityDate.getMonthOfYear() - 1, activityDate.getDayOfMonth(), new RecordActivityDatePickerListener(classSpinnerAdapter));
        // The date picker's day of month is zero-based

        // Setup class filter state
        classFilterState = new ClassFilterState(schoolSpinner, datePicker);

        // Build the dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(parentActivity);
        builder.setTitle(R.string.title_record_activity_dialog)
                // Inflate and set the layout for the dialog
                // Pass null as the parent view because its going in the dialog layout
                .setView(recordActivityView)
                .setPositiveButton(R.string.confirm_record_activity, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            // Retrieve the selected class
                            int selectedClassPosition = classSpinner.getSelectedItemPosition();
                            DummyItem danceClass = classSpinnerAdapter.getSelectedDanceClass(selectedClassPosition);

                            // Create and register the activity
                            ClassActivity classActivity = ClassActivity.buildActivity(parentActivity, danceClass, activityDate);
                            classActivity.confirm();
                            classActivityDao.registerActivity(classActivity);
                        } catch (Exception e) {
                            DummyUtils.toast(parentActivity, "Failed to record activity: " + e.getMessage());
                        }
                        dialogListener.onDialogPositiveClick(RecordActivityFragment.this);
                        DummyUtils.toast(parentActivity, "Activity recorded");
                    }
                })
                .setNegativeButton(R.string.cancel_activity_action, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

    /*
        Listener for the activity's company spinner.
     */
    private class SchoolSpinnerListener implements AdapterView.OnItemSelectedListener {
        private List<String> schoolList;
        private ClassSpinnerAdapter classSpinnerAdapter;

        public SchoolSpinnerListener(List<String> schoolList, ClassSpinnerAdapter classSpinnerAdapter) {
            this.schoolList = schoolList;
            this.classSpinnerAdapter = classSpinnerAdapter;
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            // Update school
            danceSchool = Schools.DanceSchool.fromString(schoolList.get(position));

            // Perform filtering
            String filterString = classFilterState.getFilterString();
            classSpinnerAdapter.getFilter().filter(filterString);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            // Do nothing
        }
    }

    /*
        Listener for the activity's date picker.
     */
    private class RecordActivityDatePickerListener implements DatePicker.OnDateChangedListener {
        private ClassSpinnerAdapter classSpinnerAdapter;

        public RecordActivityDatePickerListener(ClassSpinnerAdapter classSpinnerAdapter) {
            this.classSpinnerAdapter = classSpinnerAdapter;
        }

        @Override
        public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            // Update activity date
            activityDate = new DateTime(year, monthOfYear + 1, dayOfMonth, 00, 00);
            // The date picker's day of month is zero-based

            // Perform filtering
            String filterString = classFilterState.getFilterString();
            classSpinnerAdapter.getFilter().filter(filterString);
        }
    }
}
