package com.cynthiar.dancingday.recentactivity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
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
import com.cynthiar.dancingday.model.DanceClassLevel;
import com.cynthiar.dancingday.model.DummyItem;
import com.cynthiar.dancingday.model.DummyUtils;
import com.cynthiar.dancingday.model.Schools;
import com.cynthiar.dancingday.model.classActivity.ClassActivity;
import com.cynthiar.dancingday.model.time.DanceClassTime;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CynthiaR on 6/3/2018.
 */

public class RecordActivityFragment extends BaseRecentActivityFragment {
    public static final String TAG = "RecordActivityFragment";
    public static final int REQUEST_CODE = 1;

    private Schools.DanceCompany danceCompany;

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
        danceCompany = Schools.DanceCompany.fromString("ADI");

        // Get the fragment view
        LayoutInflater inflater = parentActivity.getLayoutInflater();
        final View recordActivityView = inflater.inflate(R.layout.record_activity_fragment, null);

        // Setup the school spinner
        final Spinner schoolSpinner = (Spinner) recordActivityView.findViewById(R.id.schoolSpinner);
        final List<String> schoolSpinnerItemList = new ArrayList<>();
        for (Schools.DanceCompany company:Schools.COMPANIES) {
            schoolSpinnerItemList.add(company.toString());
        }
        SpinnerAdapter spinnerAdapter = new SpinnerAdapter(
                parentActivity, R.layout.school_spinner_item, R.layout.spinner_dropdown_item, schoolSpinnerItemList);
        schoolSpinner.setAdapter(spinnerAdapter);
        schoolSpinner.setOnItemSelectedListener(new SchoolSpinnerListener(schoolSpinnerItemList));
        schoolSpinner.setSelection(schoolSpinnerItemList.indexOf(danceCompany.Key));

        // Setup the activity date picker
        DatePicker datePicker = (DatePicker) recordActivityView.findViewById(R.id.activity_date_picker);
        activityDate = DateTime.now();
        datePicker.init(activityDate.getYear(), activityDate.getMonthOfYear() - 1, activityDate.getDayOfMonth(), new ActivityDatePickerListener());
        // The date picker's day of month is zero-based

        // Build the dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(parentActivity);
        builder.setTitle(R.string.title_record_activity_dialog)
                // Inflate and set the layout for the dialog
                // Pass null as the parent view because its going in the dialog layout
                .setView(recordActivityView)
                .setPositiveButton(R.string.confirm_record_activity, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Create the activity - TODO
                        DummyItem danceClass = new DummyItem("Monday", new DanceClassTime(7, 0, 8, 30), Schools.KDC_SCHOOL, "Jerry", DanceClassLevel.Advanced);
                        try {
                            classActivityDao.registerActivity(ClassActivity.buildActivity(parentActivity, danceClass, activityDate));
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
        Listener for the card's company spinner.
     */
    private class SchoolSpinnerListener implements AdapterView.OnItemSelectedListener {
        private List<String> mSchoolList;
        public SchoolSpinnerListener(List<String> schoolList) {
            mSchoolList = schoolList;
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            danceCompany = Schools.DanceCompany.fromString(mSchoolList.get(position));
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            // Do nothing
        }
    }
}
