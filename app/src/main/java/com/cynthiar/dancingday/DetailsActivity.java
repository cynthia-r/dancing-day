package com.cynthiar.dancingday;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.cynthiar.dancingday.data.IConsumerCallback;
import com.cynthiar.dancingday.distance.matrix.DistanceQuery;
import com.cynthiar.dancingday.distance.matrix.DistanceResult;
import com.cynthiar.dancingday.distance.matrix.DistanceTask;
import com.cynthiar.dancingday.dummy.DummyUtils;

public class DetailsActivity extends AppCompatActivity implements IConsumerCallback<DistanceResult> {

    public static final String DANCE_CLASS_KEY = "Dance_Class";
    public static final String SCHOOL_ADDRESS_KEY = "School_Address";
    public static final String SCHOOL_COORDINATES_KEY = "School_Coordinates";
    public static final String SCHOOL_KEY = "School";
    public static final String LEVEL_KEY = "Level";
    public static final String TEACHER_KEY = "Teacher";
    public static final String TIME_KEY = "Time";

    private static final String WAZE_SCHEME = "waze";

    private Toolbar myToolbar;
    private boolean mIsFavorite;

    private Context mContext;
    private DistanceTask mDistanceTask;

    // Boolean telling us whether a distance estimate is in progress, so we don't trigger overlapping
    // estimations with consecutive button clicks.
    private boolean mEstimating = false;

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
        myToolbar.setTitle("Class");
        setSupportActionBar(myToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        // Calculate ETA
        mDistanceTask = new DistanceTask(this);
        String destinationAddress = bundle.getString(DetailsActivity.SCHOOL_ADDRESS_KEY);
        startEstimate(destinationAddress);

        // Save coordinates
        mSchoolCoordinates = bundle.getString(DetailsActivity.SCHOOL_COORDINATES_KEY);

        // Set text in views
        TextView schoolView = (TextView) findViewById(R.id.school);
        TextView teacherView = (TextView) findViewById(R.id.teacher);
        TextView levelView = (TextView) findViewById(R.id.level);
        TextView timeView = (TextView) findViewById(R.id.time);
        schoolView.setText(bundle.getString(DetailsActivity.SCHOOL_KEY));
        teacherView.setText(bundle.getString(DetailsActivity.TEACHER_KEY));
        levelView.setText(bundle.getString(DetailsActivity.LEVEL_KEY));
        timeView.setText(bundle.getString(DetailsActivity.TIME_KEY));

        // Set ETA to empty initially
        TextView etaView = (TextView) findViewById(R.id.eta);
        etaView.setText("");

        // TODO retrieve if dummy item is a favorite
        mIsFavorite = (bundle.getString(DetailsActivity.SCHOOL_KEY).contains("PNB"));
        final ImageButton imageButton = (ImageButton) this.findViewById(R.id.favorite);
        imageButton.setPressed(mIsFavorite);
       imageButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO see if can remove this
                // show interest in events resulting from ACTION_DOWN
                if(event.getAction()==MotionEvent.ACTION_DOWN) return true;

                // don't handle event unless its ACTION_UP so "doSomething()" only runs once.
                if(event.getAction()!=MotionEvent.ACTION_UP) return false;
                // doSomething();
                //v.setActivated(mIsFavorite);
                mIsFavorite = !mIsFavorite;
                v.setPressed(mIsFavorite);
                return true;
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    /**
     * Start non-blocking execution of DistanceTask.
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
     * Cancel (and interrupt if necessary) any ongoing DistanceTask execution.
     */
    public void cancelEstimate() {
        if (mDistanceTask != null) {
            mDistanceTask.cancel(true);
        }
    }

    public void updateFromResult(DistanceResult distanceResult) {
        TextView etaView = (TextView) findViewById(R.id.eta);
        etaView.setText(distanceResult.getEstimatedTime());
        mEstimating = false;
    }

    public void goNow(View view) {
        try
        {
            // Build url
            Uri.Builder builder = new Uri.Builder();
            builder.scheme(DetailsActivity.WAZE_SCHEME);
            builder.authority("");
            builder.appendQueryParameter("ll", mSchoolCoordinates);
            builder.appendQueryParameter("z", "10");
            builder.appendQueryParameter("navigate", "yes");
            String url = builder.build().toString();
            Intent intent = new Intent( Intent.ACTION_VIEW, Uri.parse( url ) );
            startActivity( intent );
        }
        catch ( ActivityNotFoundException ex  )
        {
            DummyUtils.toast(this, "Failed to start navigation");
        }
    }

    public void onToggleStar(View view) {
        DummyUtils.toast(this, "Star");
        ImageButton imageButton = (ImageButton)view;
        //imageButton.setSelected(true);
        //imageButton.setPressed(!mIsFavorite); // todo set button on
        //imageButton.setPressed(true);
//        mIsFavorite = !mIsFavorite;
    }
}
