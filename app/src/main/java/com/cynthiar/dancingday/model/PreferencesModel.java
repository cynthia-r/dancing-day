package com.cynthiar.dancingday.model;

import com.cynthiar.dancingday.model.classActivity.ClassActivity;
import com.cynthiar.dancingday.model.classActivity.PaymentType;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by cynthiar on 8/13/2017.
 */

public class PreferencesModel {
    private Set<String> favoriteSet;
    private Set<String> cardSet;
    private Set<String> activitySet;

    public static PreferencesModel instantiate(Set<String> favoriteSet, Set<String> cardSet, Set<String> activitySet) {
        return new PreferencesModel(favoriteSet, cardSet, activitySet);
    }

    private PreferencesModel(Set<String> favoriteSet, Set<String> cardSet, Set<String> activitySet) {
        this.favoriteSet = favoriteSet;
        this.cardSet = cardSet;
        this.activitySet = activitySet;
    }

    public Set<String> getFavoriteSet() {
        return this.favoriteSet;
    }

    public Set<String> getCardSet() {
        return this.cardSet;
    }

    public Set<String> getActivitySet() {
        return this.activitySet;
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

    public void updateCard(String oldCardKey, DanceClassCard danceClassCard) {
        if ((null == oldCardKey) || (null == danceClassCard))
            return;

        cardSet.remove(oldCardKey);
        String newCardKey = danceClassCard.toKey();
        cardSet.add(newCardKey);
    }

    public void registerActivity(ClassActivity classActivity) throws Exception {
        if (null == classActivity)
            return;

        // Debit the corresponding card if needed
        if (PaymentType.PunchCard == classActivity.getPaymentType()) {
            // Debit
            DanceClassCard cardToUse = classActivity.getDanceClassCard();
            String currentCardKey = cardToUse.toKey();
            cardToUse.debit();

            // Update the card
            this.updateCard(currentCardKey, cardToUse); // TODO replace by DB
        }

        // Register the activity
        activitySet.add(classActivity.toKey());
    }

    public void cancelActivity(ClassActivity classActivity) {
        if (null == classActivity)
            return;

        // Credit the corresponding card if needed
        if (PaymentType.PunchCard == classActivity.getPaymentType()) {
            // Credit
            DanceClassCard cardToUse = classActivity.getDanceClassCard();
            String currentCardKey = cardToUse.toKey();
            cardToUse.credit();

            // Update the card
            this.updateCard(currentCardKey, cardToUse);
        }

        // Cancel the activity
        activitySet.remove(classActivity.toKey());
    }

    public List<ClassActivity> getActivityList() {
        if (activitySet.isEmpty())
            return new ArrayList<>();
        List<ClassActivity> activityList = new ArrayList<>();
        List<String> invalidActivityKeyList = new ArrayList<>();
        for (String activityKey:activitySet
                ) {
            ClassActivity activity = ClassActivity.fromKey(activityKey);
            if (null != activity)
                activityList.add(activity);
            else invalidActivityKeyList.add(activityKey);
        }
        for (String invalidActivityKey:invalidActivityKeyList) {
            this.activitySet.remove(invalidActivityKey);
        }
        return activityList;
    }
}
