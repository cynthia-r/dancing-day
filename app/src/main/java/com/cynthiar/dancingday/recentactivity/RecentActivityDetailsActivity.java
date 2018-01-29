package com.cynthiar.dancingday.recentactivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.cynthiar.dancingday.R;
import com.cynthiar.dancingday.card.CardListViewAdapter;
import com.cynthiar.dancingday.model.DummyItem;
import com.cynthiar.dancingday.model.classActivity.ClassActivity;
import com.cynthiar.dancingday.model.classActivity.PaymentType;
import com.cynthiar.dancingday.model.database.ClassActivityDao;

public class RecentActivityDetailsActivity extends AppCompatActivity {
    public static final String CLASS_ACTIVITY_KEY = "Class_Activity";
    public static final String CLASS_ACTIVITY_ID_KEY = "Class_Activity_Id";

    private Toolbar myToolbar;
    private ClassActivity mClassActivity;
    private ClassActivityDao mClassActivityDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_activity_details);

        // Get the Intent that started this activity and extract the bundle
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra(RecentActivityDetailsActivity.CLASS_ACTIVITY_KEY);

        // Set the DAO
        mClassActivityDao = new ClassActivityDao();

        // Retrieve the class activity information
        long classActivityId = bundle.getLong(RecentActivityDetailsActivity.CLASS_ACTIVITY_ID_KEY);
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
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    /**
     * Cancels the activity for this instance and credit the corresponding card
     * @param view: The "cancel debit" button
     */
    public void cancelDebit(View view) {
        // TODO - show dialog confirm + dismiss activity if cancelled
        // mClassActivityDao.cancelActivity(mClassActivity);
    }
}
