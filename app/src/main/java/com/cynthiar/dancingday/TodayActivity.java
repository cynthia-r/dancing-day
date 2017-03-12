package com.cynthiar.dancingday;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.cynthiar.dancingday.data.DanceClassDataProvider;
import com.cynthiar.dancingday.data.DataCache;
import com.cynthiar.dancingday.data.IConsumerCallback;
import com.cynthiar.dancingday.data.IProgress;
import com.cynthiar.dancingday.distance.matrix.DistanceQuery;
import com.cynthiar.dancingday.distance.matrix.DistanceResult;
import com.cynthiar.dancingday.download.DownloadTaskProgress;
import com.cynthiar.dancingday.download.IDownloadCallback;
import com.cynthiar.dancingday.dummy.DummyItem;
import com.cynthiar.dancingday.dummy.DummyUtils;
import com.cynthiar.dancingday.dummy.extractor.DanceClassExtractor;
import com.cynthiar.dancingday.dummy.extractor.Extractors;
import com.cynthiar.dancingday.dummy.propertySelector.DanceClassPropertySelector;

import net.danlew.android.joda.JodaTimeAndroid;

import java.util.ArrayList;
import java.util.List;

public class TodayActivity extends AppCompatActivity
        implements IDownloadCallback<List<DummyItem>>,
        IConsumerCallback<List<DummyItem>> {

    public static final String TODAY_KEY = "Today";
    public static final String TOMORROW_KEY = "Tomorrow";
    public static final String NEXT_SEVEN_DAYS_KEY = "NextSevenDays";

    private String[] timeFrames;

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private LinearLayout mLeftDrawerLayout;

    private Toolbar myToolbar;
    private ActionBarDrawerToggle mDrawerToggle;
    private String mTitle;
    private String mDrawerTitle = "Timeframes";

    // Keep a reference to the NetworkFragment, which owns the AsyncTask object
    // that is used to execute network ops.
    private NetworkFragment mNetworkFragment;

    // Boolean telling us whether a download is in progress, so we don't trigger overlapping
    // downloads with consecutive button clicks.
    private boolean mDownloading = false;

    private DataCache<List<DummyItem>> mDanceClassCache;
    private List<DummyItem> mDummyItemList;
    private boolean mAllListsLoaded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today);

        // Setup toolbar
        mTitle = "Today";
        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitle(mTitle);
        setSupportActionBar(myToolbar);

        // Setup navigation drawer
        timeFrames = getResources().getStringArray(R.array.timeframes_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mLeftDrawerLayout = (LinearLayout) findViewById(R.id.left_drawer);
        mDrawerList = (ListView) findViewById(R.id.left_drawer_list);

        // Set the adapter for the drawer list view
        mDrawerList.setAdapter(new DrawerListViewAdapter(timeFrames, this));
        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        // Setup drawer toggle
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.string.drawer_open,  /* "open drawer" description */
                R.string.drawer_close  /* "close drawer" description */
        ) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mTitle);
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                //getSupportActionBar().setTitle(mDrawerTitle);
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        // Check first item in drawer
        mDrawerList.setItemChecked(0, true);

        // Setup action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        // Setup joda time library
        JodaTimeAndroid.init(this);

        // Setup cache
        DanceClassDataProvider danceClassDataProvider = new DanceClassDataProvider(this);
        mDanceClassCache = new DataCache<List<DummyItem>>(danceClassDataProvider, this);

        // Add the fragment to the 'fragment_container' FrameLayout
        mDummyItemList = new ArrayList<>();
        SingleDayFragment firstFragment = SingleDayFragment.newInstance(0);
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_frame, firstFragment, SingleDayFragment.TAG).commit();

        // Setup the network fragment
        mNetworkFragment = NetworkFragment.getInstance(getSupportFragmentManager());
        mNetworkFragment.setConsumerCallback(danceClassDataProvider);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    /** Swaps fragments in the main school view */
    private void selectItem(int position) {

        // Create a new fragment and specify the title to show based on position
        Fragment fragment;
        String fragmentTag;
        if (position < 2)
        {
            fragment = SingleDayFragment.newInstance(position);
            fragmentTag = SingleDayFragment.TAG;
        }
        else
        {
            fragment = new MultiDayFragment();
            fragmentTag = MultiDayFragment.TAG;
        }

        // Switch fragment
        this.switchToFragment(fragment, fragmentTag);

        // Highlight the selected item, update the title, and close the drawer
        mDrawerList.setItemChecked(position, true);
        mDrawerLayout.closeDrawer(mLeftDrawerLayout);
    }

    private void switchToFragment(Fragment fragment, String fragmentTag) {
        // Start fragment transaction
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.fragment_frame, fragment, fragmentTag);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title.toString();
        myToolbar.setTitle(title);
    }

    public void setTitle(int position) {
        setTitle(timeFrames[position]);
    }

    public List<DummyItem> getCurrentList() {
        Extractors extractorsInstance = Extractors.getInstance(this);
        List<List<DummyItem>> schoolLists = new ArrayList<>(extractorsInstance.EXTRACTORS.length);
        mAllListsLoaded = true;
        for (int i = 0; i < extractorsInstance.EXTRACTORS.length; i++
             ) {
            DanceClassExtractor danceClassExtractor = extractorsInstance.EXTRACTORS[i];

            // Continue if the list is not ready
            List<DummyItem>[] data = new List[1];
            if (!mDanceClassCache.Load(danceClassExtractor.getKey(), data)) {
                schoolLists.add(new ArrayList<DummyItem>());
                mAllListsLoaded = false;
                continue;
            }

            // Save the list
            List<DummyItem> schoolList = data[0];
            schoolLists.add(schoolList);
        }

        // Merge the school lists
        List<DummyItem> dummyItemList = new ArrayList<>();
        for (List<DummyItem> schoolList:schoolLists
             ) {
            dummyItemList.addAll(schoolList);
        }

        // Show/Hide loading circle depending on whether all lists are loaded
        if (mAllListsLoaded) {
            findViewById(R.id.loadingCircle).setVisibility(View.GONE);
        }
        else
            findViewById(R.id.loadingCircle).setVisibility(View.VISIBLE);

        // Return the list of dummy items
        return dummyItemList;
    }

    public boolean areAllListsLoaded() {
        return mAllListsLoaded;
    }

    public void updateFromDownload(List<DummyItem> result) {
        // Do nothing
    }

    public void startDownload(String key) {
        if (!mDownloading && mNetworkFragment != null) {
            // Execute the async download
            DanceClassExtractor danceClassExtractor = Extractors.getInstance(this).getExtractor(key);
            mNetworkFragment.startDownload(key, danceClassExtractor);
            mDownloading = true;
        }
    }

    public void updateFromResult(List<DummyItem> result) {
        // Update your UI here based on result of download.

        // Reload current fragment
        Fragment currentFragment = this.getCurrentFragment();
        this.reloadFragment(currentFragment);
    }

    public DanceClassPropertySelector getCurrentPropertySelector() {
        Fragment currentFragment = this.getCurrentFragment();
        if (currentFragment instanceof MultiDayFragment)
            return ((MultiDayFragment) currentFragment).getCurrentPropertySelector();
        else return null;
    }

    public void setCurrentPropertySelector(DanceClassPropertySelector propertySelector) {
        Fragment currentFragment = this.getCurrentFragment();
        if (currentFragment instanceof MultiDayFragment)
            ((MultiDayFragment) currentFragment).setCurrentPropertySelector(propertySelector);
    }

    private Fragment getCurrentFragment() {
        Fragment myFragment = getSupportFragmentManager().findFragmentByTag(SingleDayFragment.TAG);
        if (myFragment != null && myFragment.isVisible()) {
            return myFragment;
        }
        else {
            myFragment = getSupportFragmentManager().findFragmentByTag(MultiDayFragment.TAG);
            if (myFragment != null && myFragment.isVisible()) {
                return myFragment;
            }
        }
        return null;
    }

    private void reloadFragment(Fragment fragment) {
        final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.detach(fragment);

        ft.attach(fragment);
        ft.commit();
    }

    public NetworkInfo getActiveNetworkInfo() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo;
    }

    public void onProgressUpdate(DownloadTaskProgress progressCode, DownloadTaskProgress percentComplete) {
        switch(progressCode.GetProgressCode()) {
            // You can add UI behavior for progress updates here.
            case IProgress.ERROR:

                break;
            case IProgress.CONNECT_SUCCESS:

                break;
            case IProgress.GET_INPUT_STREAM_SUCCESS:

                break;
            case IProgress.PROCESS_INPUT_STREAM_IN_PROGRESS:

                break;
            case IProgress.PROCESS_INPUT_STREAM_SUCCESS:

                break;
        }
    }

    public void finishDownloading() {
        mDownloading = false;
        if (mNetworkFragment != null) {
            mNetworkFragment.cancelDownload();
        }
    }
}
