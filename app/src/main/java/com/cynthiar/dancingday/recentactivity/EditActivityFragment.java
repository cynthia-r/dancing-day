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
import android.widget.DatePicker;

import com.cynthiar.dancingday.R;
import com.cynthiar.dancingday.TodayActivity;
import com.cynthiar.dancingday.database.ClassActivityDao;
import com.cynthiar.dancingday.model.DummyUtils;
import com.cynthiar.dancingday.model.classActivity.ClassActivity;

import org.joda.time.DateTime;

/**
 * Created by CynthiaR on 3/31/2018.
 */
public class EditActivityFragment extends BaseRecentActivityFragment{
    public static final String TAG = "EditActivityFragment";
    public static final int REQUEST_CODE = 1;

    private ClassActivity classActivity;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public EditActivityFragment() {
    }

    public static com.cynthiar.dancingday.recentactivity.EditActivityFragment newInstance(ClassActivity classActivity) {
        com.cynthiar.dancingday.recentactivity.EditActivityFragment editActivityFragment = new com.cynthiar.dancingday.recentactivity.EditActivityFragment();
        editActivityFragment.setClassActivity(classActivity);
        return editActivityFragment;
    }

    private void setClassActivity(ClassActivity classActivity) {
        this.classActivity = classActivity;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Get the parent activity
        final TodayActivity parentActivity = (TodayActivity) getActivity();

        // Initialize the data
        classActivityDao = new ClassActivityDao();

        // Get the fragment view
        LayoutInflater inflater = parentActivity.getLayoutInflater();
        final View editActivityDateView = inflater.inflate(R.layout.edit_activity_date_fragment, null);

        // Setup the activity date picker
        DatePicker datePicker = (DatePicker) editActivityDateView.findViewById(R.id.activity_date_picker);
        activityDate = classActivity.getDate();
        datePicker.init(activityDate.getYear(), activityDate.getMonthOfYear() - 1, activityDate.getDayOfMonth(), new ActivityDatePickerListener());
        // The date picker's day of month is zero-based

        // Build the dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.title_edit_activity_dialog)
                // Inflate and set the layout for the dialog
                // Pass null as the parent view because its going in the dialog layout
                .setView(editActivityDateView)
                .setPositiveButton(R.string.confirm_edit_activity, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Update the activity
                        classActivityDao.updateActivityDate(classActivity.getId(), activityDate);
                        dialogListener.onDialogPositiveClick(EditActivityFragment.this);
                        DummyUtils.toast(getActivity(), "Activity updated");
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
}
