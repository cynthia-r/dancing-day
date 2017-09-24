package com.cynthiar.dancingday.dummy;


import android.content.Context;
import android.content.SharedPreferences;

import com.cynthiar.dancingday.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by CynthiaR on 3/19/2017.
 */

public class Preferences {
    private static Preferences mPreferencesInstance;

    private static volatile Object syncObject = new Object();

    private Preferences(PreferencesModel preferencesModel) {
        this.favoriteSet = preferencesModel.FavoriteSet;
        this.cardSet = preferencesModel.CardSet;
    }

    private Set<String> favoriteSet;

    private Set<String> cardSet;

    public static Preferences getInstance(Context context) {
        if (null != mPreferencesInstance)
            return mPreferencesInstance;

        synchronized (syncObject) {
            if (null == mPreferencesInstance) {
                PreferencesModel preferencesModel = load(context);
                mPreferencesInstance = new Preferences(preferencesModel);
            }
        }
        return mPreferencesInstance;
    }

    private static PreferencesModel load(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                context.getString(R.string.preferences_file_key), Context.MODE_PRIVATE);
        Set<String> favoriteSet = sharedPreferences.getStringSet(context.getString(R.string.favorites_key), new HashSet<String>());
        Set<String> cardSet = sharedPreferences.getStringSet(context.getString(R.string.cards_key), new HashSet<String>());

        PreferencesModel preferencesModel = PreferencesModel.instantiate(favoriteSet, cardSet);
        return preferencesModel;
    }

    public boolean isFavorite(String key) {
        return favoriteSet.contains(key);
    }

    public void changeFavoriteStatus(String key, boolean previousStatus) {
        // Un-mark as favorite
        if (previousStatus)
            favoriteSet.remove(key);
            // Mark as favorite
        else
            favoriteSet.add(key);
    }

    public List<DanceClassCard> getClassCardList() {
        if (cardSet.isEmpty())
            return new ArrayList<>();
        List<DanceClassCard> danceClassCardList = new ArrayList<>();
        for (String cardKey:cardSet
             ) {
            DanceClassCard danceClassCard = DanceClassCard.fromKey(cardKey);
            if (null != danceClassCard)
                danceClassCardList.add(danceClassCard);
            else this.cardSet.remove(cardKey);
        }
        return danceClassCardList;
    }

    public void saveCard(DanceClassCard danceClassCard) {
        if (null == danceClassCard)
            return;

        String cardKey = danceClassCard.toKey();
        cardSet.add(cardKey);
    }

    public void updateCard(String oldCardKey, DanceClassCard danceClassCard) {
        if ((null == oldCardKey) || (null == danceClassCard))
            return;

        cardSet.remove(oldCardKey);
        String newCardKey = danceClassCard.toKey();
        cardSet.add(newCardKey);
    }

    public void deleteCard(String cardKey) {
        if (null == cardKey)
            return;

        cardSet.remove(cardKey);
    }

    public void save(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                context.getString(R.string.preferences_file_key), Context.MODE_PRIVATE);

        // Update the preferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        String favoritesPreferencesKey = context.getString(R.string.favorites_key);
        editor.putStringSet(favoritesPreferencesKey, favoriteSet);
        String cardsPreferencesKey = context.getString(R.string.cards_key);
        editor.putStringSet(cardsPreferencesKey, cardSet);
        editor.apply();
    }
}
