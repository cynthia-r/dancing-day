package com.cynthiar.dancingday.recentactivity;

import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
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
        TextView paymentView = (TextView) findViewById(R.id.payment_type);
        timeView.setText(danceClass.danceClassTime.toString());
        schoolView.setText(danceClass.school.Key);
        teacherView.setText(danceClass.teacher);
        levelView.setText(danceClass.level.toString());
        String paymentText;
        if (PaymentType.PunchCard == mClassActivity.getPaymentType())
            paymentText = "Card";
        else
            paymentText = "Ticket";
        paymentView.setText("Payment: " + paymentText);

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
        // TODO - show dialog confirm
        mClassActivityDao.deleteActivity(mClassActivity);
        DummyUtils.toast(this, "Activity deleted");
        finish();
    }

    /**
     * Cancels the activity for this instance and credit the corresponding card
     * @param view: The "cancel debit" button
     */
    public void cancelDebit(View view) {
        // TODO - show dialog confirm
        mClassActivityDao.cancelActivity(mClassActivity);
        DummyUtils.toast(this, "Activity cancelled");


        // Dismiss the notification
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(Long.toString(mClassActivity.getId()), ClassActivityNotification.NOTIFICATION_ID);

        finish();
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
