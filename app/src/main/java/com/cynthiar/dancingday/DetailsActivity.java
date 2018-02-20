package com.cynthiar.dancingday;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.cynthiar.dancingday.data.IConsumerCallback;
import com.cynthiar.dancingday.distance.matrix.DistanceQuery;
import com.cynthiar.dancingday.distance.matrix.DistanceResult;
import com.cynthiar.dancingday.distance.matrix.DistanceTask;
import com.cynthiar.dancingday.model.DummyItem;
import com.cynthiar.dancingday.model.DummyUtils;
import com.cynthiar.dancingday.model.Preferences;
import com.cynthiar.dancingday.model.classActivity.ClassActivity;
import com.cynthiar.dancingday.model.database.ClassActivityDao;

/**
 * Activity for the detailed view of a given dance class.
 */
public class DetailsActivity extends AppCompatActivity implements IConsumerCallback<DistanceResult> {
    public static final String DANCE_CLASS_KEY = "Dance_Class";
    public static final String SCHOOL_ADDRESS_KEY = "School_Address";
    public static final String SCHOOL_COORDINATES_KEY = "School_Coordinates";
    public static final String SCHOOL_KEY = "School";
    public static final String LEVEL_KEY = "Level";
    public static final String TEACHER_KEY = "Teacher";
    public static final String TIME_KEY = "Time";
    public static final String DAY_KEY = "Day";

    private static final String ORIGIN_ADDRESS = "1120 112th Ave NE Bellevue WA 98004";
    private static final String WAZE_SCHEME = "waze";

    private Toolbar myToolbar;
    private boolean mIsFavorite;

    private DistanceTask mDistanceTask;

    // Boolean telling us whether a distance estimate is in progress, so we don't trigger overlapping
    // estimations with consecutive button clicks.
    private boolean mEstimating = false;

    // The dance class this activity is for
    private DummyItem mDanceClass;

    // Save the company coordinates
    private String mSchoolCoordinates;

    private ClassActivityDao mClassActivityDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        // Get the Intent that started this activity and extract the bundle
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra(DetailsActivity.DANCE_CLASS_KEY);

        // Setup toolbar
        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitle(getResources().getString(R.string.title_activity_details));
        setSupportActionBar(myToolbar);

        // Setup action bar buttons
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        // Calculate ETA asynchronously
        mDistanceTask = new DistanceTask(this);
        String destinationAddress = bundle.getString(DetailsActivity.SCHOOL_ADDRESS_KEY);
        startEstimate(destinationAddress);

        // Save the dance class information
        String day = bundle.getString(DetailsActivity.DAY_KEY);
        String time = bundle.getString(DetailsActivity.TIME_KEY);
        String school = bundle.getString(DetailsActivity.SCHOOL_KEY);
        String teacher = bundle.getString(DetailsActivity.TEACHER_KEY);
        String level = bundle.getString(DetailsActivity.LEVEL_KEY);
        mDanceClass = DummyItem.fromStrings(day, time, school, teacher, level);

        // Save coordinates
        mSchoolCoordinates = bundle.getString(DetailsActivity.SCHOOL_COORDINATES_KEY);

        // Set text in views
        TextView schoolView = (TextView) findViewById(R.id.school);
        TextView teacherView = (TextView) findViewById(R.id.teacher);
        TextView levelView = (TextView) findViewById(R.id.level);
        TextView timeView = (TextView) findViewById(R.id.time);
        schoolView.setText(school);
        teacherView.setText(teacher);
        levelView.setText(level);
        timeView.setText(time);

        // Set ETA to empty initially
        TextView etaView = (TextView) findViewById(R.id.eta);
        etaView.setText("");

        // Retrieve if dummy item is a favorite
        String danceClassKey = mDanceClass.toKey();
        mIsFavorite = Preferences.getInstance(this).isFavorite(danceClassKey);

        // Set star button accordingly
        ToggleButton imageButton = (ToggleButton) this.findViewById(R.id.favorite);
        imageButton.setChecked(mIsFavorite);
        imageButton.setOnClickListener(new StarClickListener(this));

        // Initialize DAO
        mClassActivityDao = new ClassActivityDao();
    }

    @Override
    public void onPause() {
        super.onPause();  // Always call the superclass method first
        Preferences.getInstance(this).save(this);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    /**
     * Starts non-blocking execution of DistanceTask.
     */
    private void startEstimate(String destinationAddress) {
        if (!mEstimating && mDistanceTask != null) {
            // Execute the async estimate
            DistanceQuery distanceQuery = new DistanceQuery(ORIGIN_ADDRESS, destinationAddress);
            mDistanceTask.execute(distanceQuery);
            mEstimating = true;
        }
    }

    /**
     * Cancels (and interrupts if necessary) any ongoing DistanceTask execution.
     */
    public void cancelEstimate() {
        if (mDistanceTask != null) {
            mDistanceTask.cancel(true);
        }
    }

    /**
     * Updates the activity from the result of the distance estimate.
     */
    public void updateFromResult(DistanceResult distanceResult) {
        TextView etaView = (TextView) findViewById(R.id.eta);
        etaView.setText(distanceResult.getEstimatedTime());
        mEstimating = false;
    }

    /**
     * Starts the Waze navigation.
     * @param view: The "Go now" button.
     */
    public void goNow(View view) {
        try {
            // Build url
            Uri.Builder builder = new Uri.Builder();
            builder.scheme(DetailsActivity.WAZE_SCHEME);
            builder.authority("");
            builder.appendQueryParameter("ll", mSchoolCoordinates);
            builder.appendQueryParameter("z", "10");
            builder.appendQueryParameter("navigate", "yes");
            String url = builder.build().toString();

            // Start the Waze navigation
            Intent intent = new Intent( Intent.ACTION_VIEW, Uri.parse( url ) );
            startActivity( intent );

            // Register a new activity if the class is about to start
            // and there is no current activity for this class
            if (mDanceClass.isNow() && !mDanceClass.activityExists()) {
                ClassActivity classActivity = ClassActivity.buildActivity(this, mDanceClass);
                try {
                    // Build a notification
                    long classActivityId = mClassActivityDao.registerActivity(classActivity);
                    Notification notification = new ClassActivityNotification(this).buildNotification(classActivityId, mDanceClass);

                    // Issue the notification
                    NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    notificationManager.notify(Long.toString(classActivityId), ClassActivityNotification.NOTIFICATION_ID, notification);
                }
                catch (Exception e) {
                    DummyUtils.toast(this, "Failed to register activity:" + e.getLocalizedMessage());
                }
            }
        }
        catch ( ActivityNotFoundException ex ) {
            DummyUtils.toast(this, "Failed to start navigation");
        }
    }

    /**
     * Converts a dance class to a bundle.
     * @param dummyItem: The dance class information.
     * @return: The bundle that can be used between activities.
     */
    public static Bundle toBundle(DummyItem dummyItem) {
        Bundle bundle = new Bundle();
        bundle.putString(DetailsActivity.DAY_KEY, dummyItem.day);
        bundle.putString(DetailsActivity.LEVEL_KEY, dummyItem.level.toString());
        bundle.putString(DetailsActivity.SCHOOL_KEY, dummyItem.school.Key);
        bundle.putString(DetailsActivity.SCHOOL_ADDRESS_KEY, dummyItem.school.Address);
        bundle.putString(DetailsActivity.SCHOOL_COORDINATES_KEY, dummyItem.school.Coordinates);
        bundle.putString(DetailsActivity.TEACHER_KEY, dummyItem.teacher.toString());
        bundle.putString(DetailsActivity.TIME_KEY, dummyItem.danceClassTime.toString());
        return bundle;
    }

    /**
     * Click listener for the star button.
     */
    private class StarClickListener implements View.OnClickListener {
        private Context mContext;
        public StarClickListener(Context context) {mContext = context;}
        @Override
        public void onClick(View v) {
            // Mark the dance class as favorite
            String danceClassKey = mDanceClass.toKey();
            Preferences.getInstance(mContext).changeFavoriteStatus(danceClassKey, mIsFavorite);

            // Keep track of the new state
            mIsFavorite = !mIsFavorite;
        }
    }
}
