package com.cynthiar.dancingday.recentactivity;

import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.cynthiar.dancingday.ClassActivityNotification;
import com.cynthiar.dancingday.R;
import com.cynthiar.dancingday.card.CardListViewAdapter;
import com.cynthiar.dancingday.model.DummyItem;
import com.cynthiar.dancingday.model.DummyUtils;
import com.cynthiar.dancingday.model.classActivity.ClassActivity;
import com.cynthiar.dancingday.model.classActivity.PaymentType;
import com.cynthiar.dancingday.model.database.ClassActivityDao;

public class RecentActivityDetailsActivity extends AppCompatActivity {
    private Toolbar myToolbar;
    private ClassActivity mClassActivity;
    private ClassActivityDao mClassActivityDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_activity_details);

        // Get the Intent that started this activity and extract the bundle
        Intent intent = getIntent();

        // Set the DAO
        mClassActivityDao = new ClassActivityDao();

        // Retrieve the class activity information
        long classActivityId = intent.getLongExtra(ClassActivityNotification.CLASS_ACTIVITY_ID_KEY, -1);
        mClassActivity = mClassActivityDao.getActivityById(classActivityId);
        DummyItem danceClass = mClassActivity.getDanceClass();

        // Set text in views
        TextView timeView = (TextView) findViewById(R.id.activity_time);
        TextView schoolView = (TextView) findViewById(R.id.school);
        TextView teacherView = (TextView) findViewById(R.id.teacher);
        TextView levelView = (TextView) findViewById(R.id.level);
        timeView.setText(danceClass.danceClassTime.toString());
        schoolView.setText(danceClass.school.Key);
        teacherView.setText(danceClass.teacher);
        levelView.setText(danceClass.level.toString());

        // Set payment type view
        this.setPaymentTypeViews(mClassActivity.getPaymentType());

        // Setup toolbar
        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);

        // Set title in toolbar
        String activityDateString = "Activity on " + mClassActivity.getDate().toString(CardListViewAdapter.ExpirationDateFormatter);
        myToolbar.setTitle(activityDateString);
        setSupportActionBar(myToolbar);

        // Setup action bar buttons
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        // Check if the activity was opened from a notification action
        boolean notification = intent.getBooleanExtra(ClassActivityNotification.NOTIFICATION_ACTION_KEY, false);
        if (notification) {
            this.handleNotificationConfirmAction(intent);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    /**
     * Deletes the activity from the history
     * @param view: The "Delete activity" button.
     */
    public void deleteActivity(View view) {
        // Show confirmation dialog
        new AlertDialog.Builder(this)
            .setTitle("Delete activity")
            .setMessage("Are you sure you want to delete this activity?")
            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Delete the class activity
                    mClassActivityDao.deleteActivity(mClassActivity);
                    DummyUtils.toast(getApplicationContext(), "Activity deleted");

                    // Close the activity
                    finish();
                }
            })
            .setNegativeButton(android.R.string.no, null) // No action
            .show();
    }

    /**
     * Edits the payment type.
     * @param view: The "Edit payment type" button.
     */
    public void editPaymentType(View view) {
        // Retrieve the current payment type
        PaymentType currentPaymentType = mClassActivity.getPaymentType();

        // Determine the change of payment type
        boolean ticketToCard = PaymentType.SingleTicket == currentPaymentType;
        PaymentType newPaymentType = PaymentType.PunchCard;
        String currentPaymentTypeString = "ticket";
        String newPaymentTypeString = "punch card";

        // Check if we're changing from a card to a ticket
        if (!ticketToCard) {
            newPaymentType = PaymentType.SingleTicket;
            currentPaymentTypeString = "punch card";
            newPaymentTypeString = "ticket";
        }
        final PaymentType paymentTypeToSet = newPaymentType;

        // Show confirmation dialog
        new AlertDialog.Builder(this)
            .setTitle("Edit payment type")
            .setMessage("A " + currentPaymentTypeString + " was used for this class activity." +
                    " Do you want to change it to a " + newPaymentTypeString + "?")
            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Edit payment type
                    try {
                        mClassActivityDao.editPaymentType(mClassActivity, paymentTypeToSet);
                    }
                    catch (Exception e) {
                        DummyUtils.toast(getApplicationContext(), "Failed to edit payment type: " + e.getMessage());
                        return;
                    }

                    // Set the payment type view
                    setPaymentTypeViews(paymentTypeToSet);
                }
            })
            .setNegativeButton(android.R.string.no, null) // No action
            .show();
    }

    /**
     * Cancels the activity for this instance and credit the corresponding card
     * @param view: The "cancel debit" button
     */
    public void cancelDebit(View view) {
        // Show confirmation dialog
        new AlertDialog.Builder(this)
            .setTitle("Cancel debit")
            .setMessage("Are you sure you want to cancel this activity?")
            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Cancel the class activity
                    mClassActivityDao.cancelActivity(mClassActivity);
                    DummyUtils.toast(getApplicationContext(), "Activity cancelled");

                    // Dismiss the notification
                    NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    notificationManager.cancel(Long.toString(mClassActivity.getId()), ClassActivityNotification.NOTIFICATION_ID);

                    // Close the activity
                    finish();
                }
            })
            .setNegativeButton(android.R.string.no, null) // No action
            .show();
    }

    private void setPaymentTypeViews(PaymentType paymentTypeToSet) {
        // Retrieve the payment type views
        ImageButton cardView = (ImageButton) findViewById(R.id.payment_card);
        ImageButton ticketView = (ImageButton) findViewById(R.id.payment_ticket);
        Button cancelDebitButton = (Button) findViewById(R.id.cancelDebit);

        // Set payment view and display the "Cancel Debit" button for card payments
        String paymentText;
        if (PaymentType.PunchCard == paymentTypeToSet) {
            cardView.setVisibility(View.VISIBLE);
            ticketView.setVisibility(View.GONE);
            cancelDebitButton.setVisibility(View.VISIBLE);
        }
        else {
            ticketView.setVisibility(View.VISIBLE);
            cardView.setVisibility(View.GONE);
            cancelDebitButton.setVisibility(View.GONE);
        }
    }

    private void handleNotificationConfirmAction(Intent notificationIntent) {
        // Check if the activity is confirmed
        boolean confirmed = notificationIntent.getBooleanExtra(ClassActivityNotification.CLASS_ACTIVITY_CONFIRMED_KEY, false);
        if (confirmed) {
            DummyUtils.toast(this, "Confirmed");
            mClassActivityDao.confirmActivity(mClassActivity);
        }

        // Dismiss the notification
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(Long.toString(mClassActivity.getId()), ClassActivityNotification.NOTIFICATION_ID);
    }
}
