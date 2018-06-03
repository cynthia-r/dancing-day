package com.cynthiar.dancingday.recentactivity;

import android.content.Context;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.widget.DatePicker;

import com.cynthiar.dancingday.database.ClassActivityDao;

import org.joda.time.DateTime;

/**
 * Base class for recent activity fragments.
 */

public abstract class BaseRecentActivityFragment extends DialogFragment{
    protected ClassActivityDao classActivityDao;
    protected DateTime activityDate;
    protected RecentActivityDialogListener dialogListener;

    /*
        Listener interface for events coming from this dialog.
     */
    public interface RecentActivityDialogListener {
        void onDialogPositiveClick(DialogFragment dialog);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the target fragment implements the callback interface
        Fragment targetFragment = getTargetFragment();
        try {
            // Instantiate the RecentActivityDialogListener so we can send events to the host
            dialogListener = (RecentActivityDialogListener) targetFragment;
        } catch (ClassCastException e) {
            // The target fragment doesn't implement the interface, throw exception
            throw new ClassCastException(targetFragment.getTag()
                    + " must implement RecentActivityDialogListener");
        }
    }

    /*
        Listener for the activity's date picker.
     */
    protected class ActivityDatePickerListener implements DatePicker.OnDateChangedListener {
        @Override
        public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            activityDate = new DateTime(year, monthOfYear + 1, dayOfMonth, 00, 00);
            // The date picker's day of month is zero-based
        }
    }
}
