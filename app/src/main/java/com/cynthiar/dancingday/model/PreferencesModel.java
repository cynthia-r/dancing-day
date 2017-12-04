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

    public List<DanceClassCard> getClassCardList() {
        if (cardSet.isEmpty())
            return new ArrayList<>();
        List<DanceClassCard> danceClassCardList = new ArrayList<>();
        List<String> invalidCardKeyList = new ArrayList<>();
        for (String cardKey:cardSet
                ) {
            DanceClassCard danceClassCard = DanceClassCard.fromKey(cardKey);
            if (null != danceClassCard)
                danceClassCardList.add(danceClassCard);
            else invalidCardKeyList.add(cardKey);
        }
        for (String invalidCardKey:invalidCardKeyList) {
            this.cardSet.remove(invalidCardKey);
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

    public void registerActivity(ClassActivity classActivity) throws Exception {
        if (null == classActivity)
            return;

        // Debit the corresponding card if needed
        if (PaymentType.PunchCard == classActivity.paymentType) {
            // Debit
            DanceClassCard cardToUse = classActivity.danceClassCard;
            String currentCardKey = cardToUse.toKey();
            cardToUse.debit();

            // Update the card
            this.updateCard(currentCardKey, cardToUse);
        }

        // Register the activity
        activitySet.add(classActivity.toKey());
    }

    public void cancelActivity(ClassActivity classActivity) {
        if (null == classActivity)
            return;

        // Credit the corresponding card if needed
        if (PaymentType.PunchCard == classActivity.paymentType) {
            // Credit
            DanceClassCard cardToUse = classActivity.danceClassCard;
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
