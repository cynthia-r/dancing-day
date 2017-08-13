package com.cynthiar.dancingday;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.cynthiar.dancingday.data.IConsumerCallback;
import com.cynthiar.dancingday.distance.matrix.DistanceQuery;
import com.cynthiar.dancingday.distance.matrix.DistanceResult;
import com.cynthiar.dancingday.distance.matrix.DistanceTask;
import com.cynthiar.dancingday.dummy.DummyItem;
import com.cynthiar.dancingday.dummy.DummyUtils;
import com.cynthiar.dancingday.dummy.Preferences;

import java.util.HashSet;
import java.util.Set;

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

    private static final String WAZE_SCHEME = "waze";

    private Toolbar myToolbar;
    private boolean mIsFavorite;

    private DistanceTask mDistanceTask;

    // Boolean telling us whether a distance estimate is in progress, so we don't trigger overlapping
    // estimations with consecutive button clicks.
    private boolean mEstimating = false;

    // The dance class this activity is foo
    private DummyItem mDanceClass;

    // Save the school coordinates
    private String mSchoolCoordinates;

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
        //ImageButton imageButton = (ImageButton) this.findViewById(R.id.favorite);
        //imageButton.setPressed(mIsFavorite);
        //imageButton.setOnTouchListener(new StarTouchListener(this));
        ToggleButton imageButton = (ToggleButton) this.findViewById(R.id.favorite);
        imageButton.setChecked(mIsFavorite);
        imageButton.setOnClickListener(new StarClickListener(this));
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
            String originAddress = "3933 Lake Washington Blvd NE #200, Kirkland, WA 98033";
            DistanceQuery distanceQuery = new DistanceQuery(originAddress, destinationAddress);
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
        }
        catch ( ActivityNotFoundException ex ) {
            DummyUtils.toast(this, "Failed to start navigation");
        }
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
