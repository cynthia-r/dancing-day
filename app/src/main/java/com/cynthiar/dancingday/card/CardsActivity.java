package com.cynthiar.dancingday.card;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.cynthiar.dancingday.R;
import com.cynthiar.dancingday.model.DanceClassCard;
import com.cynthiar.dancingday.model.Preferences;

import java.util.List;

import static android.view.View.GONE;

/**
 * Created by cynthiar on 8/13/2017.
 */

/*
    Activity showing the list of class cards.
 */
public class CardsActivity extends AppCompatActivity implements BaseCardFragment.CardDialogListener {
    private Toolbar myToolbar;
    private CardListViewAdapter mCardListViewAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cards);

        // Setup toolbar
        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitle(R.string.title_activity_cards);
        setSupportActionBar(myToolbar);

        // Setup action bar buttons
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Override
    public void onStart() {
        super.onStart();

        // Refresh the list
        this.refreshList();
    }

    /*
        Refreshes the list of cards
     */
    private void refreshList() {
        // Retrieve list of items
        List<DanceClassCard> danceClassCardList = Preferences.getInstance(this).getClassCardList();

        // Get the list view and the empty state view
        ListView listView = (ListView) this.findViewById(R.id.card_list_view);
        TextView emptyStateTextView = (TextView) this.findViewById(R.id.emptyList);

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
        mCardListViewAdapter = new CardListViewAdapter(danceClassCardList, this);
        listView.setAdapter(mCardListViewAdapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cards, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_new_card:
                // User chose the "New card" item, launch the new card activity...
                new NewCardFragment().show(getSupportFragmentManager(), NewCardFragment.TAG);
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
