package com.cynthiar.dancingday.model;


import android.content.Context;
import android.content.SharedPreferences;

import com.cynthiar.dancingday.R;
import com.cynthiar.dancingday.model.classActivity.ClassActivity;

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

    private Preferences(Context context, PreferencesModel preferencesModel) {
        this.preferencesModel = preferencesModel;
        mContext = context;
    }

    private PreferencesModel preferencesModel;
    private Context mContext;

    public static Preferences getInstance(Context context) {
        if (null != mPreferencesInstance)
            return mPreferencesInstance;

        synchronized (syncObject) {
            if (null == mPreferencesInstance) {
                PreferencesModel preferencesModel = load(context);
                mPreferencesInstance = new Preferences(context, preferencesModel);
            }
        }
        return mPreferencesInstance;
    }

    private static PreferencesModel load(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                context.getString(R.string.preferences_file_key), Context.MODE_PRIVATE);
        Set<String> favoriteSet = sharedPreferences.getStringSet(context.getString(R.string.favorites_key), new HashSet<String>());
        Set<String> cardSet = sharedPreferences.getStringSet(context.getString(R.string.cards_key), new HashSet<String>());
        Set<String> activitySet = sharedPreferences.getStringSet(context.getString(R.string.activities_key), new HashSet<String>());

        PreferencesModel preferencesModel = PreferencesModel.instantiate(favoriteSet, cardSet, activitySet);
        return preferencesModel;
    }

    public void save(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                context.getString(R.string.preferences_file_key), Context.MODE_PRIVATE);

        // Update the preferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        String favoritesPreferencesKey = context.getString(R.string.favorites_key);
        editor.putStringSet(favoritesPreferencesKey, this.preferencesModel.getFavoriteSet());
        String cardsPreferencesKey = context.getString(R.string.cards_key);
        editor.putStringSet(cardsPreferencesKey, this.preferencesModel.getCardSet());
        String activitiesPreferencesKey = context.getString(R.string.activities_key);
        editor.putStringSet(activitiesPreferencesKey, this.preferencesModel.getActivitySet());
        editor.apply();
    }

    public boolean isFavorite(String key) {
        return this.preferencesModel.isFavorite(key);
    }

    public void changeFavoriteStatus(String key, boolean previousStatus) {
        this.preferencesModel.changeFavoriteStatus(key, previousStatus);
    }

    public void registerActivity(ClassActivity classActivity) {
        try {
            this.preferencesModel.registerActivity(classActivity);
        } catch (Exception e) {
            DummyUtils.toast(mContext, "Failed to register activity.");
        }
    }

    public void cancelActivity(ClassActivity classActivity) {
        this.preferencesModel.cancelActivity(classActivity);
    }

    public List<ClassActivity> getActivityList() {
        return this.preferencesModel.getActivityList();
    }
}
