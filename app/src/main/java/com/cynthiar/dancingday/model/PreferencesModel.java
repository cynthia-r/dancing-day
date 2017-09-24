package com.cynthiar.dancingday.model;

import java.util.Set;

/**
 * Created by cynthiar on 8/13/2017.
 */

public class PreferencesModel {
    public final Set<String> FavoriteSet;
    public final Set<String> CardSet;

    public static PreferencesModel instantiate(Set<String> favoriteSet, Set<String> cardSet) {
        return new PreferencesModel(favoriteSet, cardSet);
    }

    private PreferencesModel(Set<String> favoriteSet, Set<String> cardSet) {
        this.FavoriteSet = favoriteSet;
        this.CardSet = cardSet;
    }
}
