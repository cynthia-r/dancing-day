package com.cynthiar.dancingday.card;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.cynthiar.dancingday.R;
import com.cynthiar.dancingday.TodayActivity;
import com.cynthiar.dancingday.model.DanceClassCard;
import com.cynthiar.dancingday.database.DanceClassCardDao;

import java.util.List;

import static android.view.View.GONE;

/**
 * Created by cynthiar on 8/13/2017.
 */

/*
    Activity showing the list of class cards.
 */
public class CardsMainFragment extends Fragment implements BaseCardFragment.CardDialogListener {
    public static final String TAG = "CardsMainFragment";
    public static final int POSITION = 3;

    private CardListViewAdapter mCardListViewAdapter;
    private DanceClassCardDao danceClassCardDao;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public CardsMainFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.cards_main_fragment, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onStart() {
        super.onStart();

        // Get the parent activity
        TodayActivity parentActivity = (TodayActivity)getActivity();

        // Setup dao
        danceClassCardDao = new DanceClassCardDao();

        // Refresh the list
        this.refreshList();

        // Set title
        parentActivity.setTitle(CardsMainFragment.POSITION);}

    /*
        Refreshes the list of cards
     */
    private void refreshList() {
        // Get the parent activity
        TodayActivity parentActivity = (TodayActivity)getActivity();

        // Retrieve list of items
        List<DanceClassCard> danceClassCardList = danceClassCardDao.getClassCardList();

        // Get the list view and the empty state view
        ListView listView = (ListView) parentActivity.findViewById(R.id.card_list_view);
        TextView emptyStateTextView = (TextView) parentActivity.findViewById(R.id.emptyList);

        // Display empty state if no results
        if (0 == danceClassCardList.size()) {
            listView.setVisibility(GONE);
            emptyStateTextView.setText(R.string.cards_empty_state);
            emptyStateTextView.setVisibility(View.VISIBLE);
            return;
        }

        // Otherwise show the list view
        listView.setVisibility(View.VISIBLE);
        emptyStateTextView.setVisibility(View.GONE);

        // Setup list adapter
        mCardListViewAdapter = new CardListViewAdapter(danceClassCardList, parentActivity, this);
        listView.setAdapter(mCardListViewAdapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_cards, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_new_card:
                // User chose the "New card" item, launch the new card fragment...
                NewCardFragment newCardFragment = new NewCardFragment();
                newCardFragment.setTargetFragment(this, NewCardFragment.REQUEST_CODE);
                newCardFragment.show(getFragmentManager(), NewCardFragment.TAG);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        this.refreshList();
    }
}
