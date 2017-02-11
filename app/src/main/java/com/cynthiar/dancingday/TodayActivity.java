package com.cynthiar.dancingday;

import android.content.Context;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.util.Pair;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.cynthiar.dancingday.dummy.DummyContent;

import java.util.ArrayList;
import java.util.List;

public class TodayActivity extends AppCompatActivity
        implements IDownloadCallback<List<DummyContent.DummyItem>>/*, DataProvider<List<DummyContent.DummyItem>>*/
        , IConsumerCallback<List<DummyContent.DummyItem>> {

    public static final String TODAY_KEY = "Today";
    private String[] timeFrames;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;

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

    private DataCache<List<DummyContent.DummyItem>> mDanceClassCache;
    private List<DummyContent.DummyItem> mDummyItemList;

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
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // Set the adapter for the drawer list view
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, timeFrames));
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
                getSupportActionBar().setTitle(mDrawerTitle);
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        // Setup cache
        DanceClassDataProvider danceClassDataProvider = new DanceClassDataProvider(this);
        mDanceClassCache = new DataCache<List<DummyContent.DummyItem>>(danceClassDataProvider, this);

        // Add the fragment to the 'fragment_container' FrameLayout
        mDummyItemList = new ArrayList<>();
        SingleDayFragment firstFragment = SingleDayFragment.newInstance(1, mDummyItemList);
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_frame, firstFragment, SingleDayFragment.TAG).commit();

        mNetworkFragment = NetworkFragment.getInstance(getSupportFragmentManager(), "https://www.google.com");
        mNetworkFragment.setConsumerCallback(danceClassDataProvider);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
        //startDownload();
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

    /*@Override
    public List<DummyContent.DummyItem> GiveMeTheData() {
        this.startDownload();
    }*/

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
            fragment = SingleDayFragment.newInstance(1, mDummyItemList);
            fragmentTag = SingleDayFragment.TAG;
        }
        else
        {
            fragment = new MultiDayFragment();
            fragmentTag = "MultiDayFragment";
        }

        Bundle args = new Bundle();
        args.putInt(SingleDayFragment.ARG_NUMBER, position + 1);
        fragment.setArguments(args);

        /*// Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_frame, fragment)
                .commit();*/
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.fragment_frame, fragment, fragmentTag);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();

        // Highlight the selected item, update the title, and close the drawer
        mDrawerList.setItemChecked(position, true);
        setTitle(timeFrames[position]);
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title.toString();
        myToolbar.setTitle(title);
    }

    public List<DummyContent.DummyItem> getCurrentList() {
        List<DummyContent.DummyItem> dummyItemList = mDanceClassCache.Load(TODAY_KEY);
        if (null == dummyItemList)
            return new ArrayList<>();
        return dummyItemList;
    }

    public void updateFromDownload(List<DummyContent.DummyItem> result) {
        //this.updateFromDownload(result);
        // Do nothing
    }

    public void startDownload() {
        if (!mDownloading && mNetworkFragment != null) {
            // Execute the async download.
            mNetworkFragment.startDownload();
            mDownloading = true;
        }
    }

    public void updateFromResult(List<DummyContent.DummyItem> result) {
        // Update your UI here based on result of download.
        /*TextView downloadResultTextView = (TextView) findViewById(R.id.downloadResult);
        downloadResultTextView.setText(result);*/
        mDummyItemList = result;

        // Reload current fragment
        Fragment frg = null;
        frg = getSupportFragmentManager().findFragmentByTag(SingleDayFragment.TAG);
        final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.detach(frg);

        // setArguments();
        SingleDayFragment singleDayFragment = (SingleDayFragment)frg;
        singleDayFragment.setData(result);

        ft.attach(frg);
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
            case Progress.ERROR:

                break;
            case Progress.CONNECT_SUCCESS:

                break;
            case Progress.GET_INPUT_STREAM_SUCCESS:

                break;
            case Progress.PROCESS_INPUT_STREAM_IN_PROGRESS:

                break;
            case Progress.PROCESS_INPUT_STREAM_SUCCESS:

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
