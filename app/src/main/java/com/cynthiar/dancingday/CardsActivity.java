package com.cynthiar.dancingday;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.cynthiar.dancingday.dummy.DanceClassCard;
import com.cynthiar.dancingday.dummy.DummyItem;
import com.cynthiar.dancingday.dummy.DummyUtils;
import com.cynthiar.dancingday.dummy.Schools;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;

/**
 * Created by cynthiar on 8/13/2017.
 */

public class CardsActivity extends AppCompatActivity {
    private Toolbar myToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cards);

        // Setup toolbar
        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitle(getResources().getString(R.string.title_activity_cards));
        setSupportActionBar(myToolbar);

        // Setup action bar buttons
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        // Retrieve list of items
        List<DanceClassCard> danceClassCardList = new ArrayList<>();
        danceClassCardList.add(new DanceClassCard(Schools.DanceSchool.fromString("KDC"), 5, new DateTime(2017, 4, 10, 0, 0), new DateTime(2017, 5, 5, 0, 0)));
        danceClassCardList.add(new DanceClassCard(Schools.DanceSchool.fromString("ADI"), 2, new DateTime(2017, 3, 27, 0, 0), new DateTime(2017, 4, 30, 0, 0)));

        // Get the list view and the empty state view
        ListView listView = (ListView) this.findViewById(R.id.card_list_view);
        TextView emptyStateTextView = (TextView) this.findViewById(R.id.emptyList);

        // Display empty state if no results
        if (0 == danceClassCardList.size()) {
            listView.setVisibility(GONE);
            emptyStateTextView.setText("No class cards");
            emptyStateTextView.setVisibility(View.VISIBLE);
            return;
        }

        // Otherwise show the list view
        listView.setVisibility(View.VISIBLE);
        emptyStateTextView.setVisibility(View.GONE);

        // Setup list adapter
        CardListViewAdapter adapter = new CardListViewAdapter(danceClassCardList, this);
        listView.setAdapter(adapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
