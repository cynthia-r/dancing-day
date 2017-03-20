package com.cynthiar.dancingday.dummy;


import android.content.Context;
import android.content.SharedPreferences;

import com.cynthiar.dancingday.R;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by CynthiaR on 3/19/2017.
 */

public class Preferences {
    private static Preferences mPreferencesInstance;

    private static volatile Object syncObject = new Object();

    private Preferences(Set<String> favoriteSet) {
        this.favoriteSet = favoriteSet;
    }

    private Set<String> favoriteSet;

    public static Preferences getInstance(Context context) {
        if (null != mPreferencesInstance)
            return mPreferencesInstance;

        synchronized (syncObject) {
            if (null == mPreferencesInstance) {
                Set<String> favoriteSet = loadFavorites(context);
                mPreferencesInstance = new Preferences(favoriteSet);
            }
        }
        return mPreferencesInstance;
    }

    private static Set<String> loadFavorites(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                context.getString(R.string.preferences_file_key), Context.MODE_PRIVATE);
        Set<String> favoriteSet = sharedPreferences.getStringSet(context.getString(R.string.favorites_key), new HashSet<String>());
        return favoriteSet;
    }

    public boolean isFavorite(String key) {
        return favoriteSet.contains(key);
    }

    public void changeFavoriteStatus(Context context, String key, boolean previousStatus) {
        /*SharedPreferences sharedPreferences = context.getSharedPreferences(
                context.getString(R.string.preferences_file_key), Context.MODE_PRIVATE);

        // Retrieve previous set of favorites
        String favoritesPreferencesKey = context.getString(R.string.favorites_key);
        Set<String> preferenceFavoriteSet = sharedPreferences.getStringSet(favoritesPreferencesKey, new HashSet<String>());*/

        // Un-mark as favorite
        if (previousStatus)
            favoriteSet.remove(key);
            // Mark as favorite
        else
            favoriteSet.add(key);

        /*// Update the preferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet(favoritesPreferencesKey, preferenceFavoriteSet);
        editor.commit();

        // Update the instance set
        favoriteSet = preferenceFavoriteSet;*/
    }

    public void save(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                context.getString(R.string.preferences_file_key), Context.MODE_PRIVATE);

        // Update the preferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String favoritesPreferencesKey = context.getString(R.string.favorites_key);
        editor.putStringSet(favoritesPreferencesKey, favoriteSet);
        editor.commit();
    }
}
