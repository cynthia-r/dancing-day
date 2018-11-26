package com.cynthiar.dancingday.model;

import org.joda.time.DateTime;

import java.util.List;

/**
 * Represents the list of current class cards for a given school.
 * Mostly used for display purposes
 */
public class DanceClassCards {
    private int remainingCardClasses;
    private boolean hasCards;
    private DateTime cardExpirationDate;

    public DanceClassCards(List<DanceClassCard> cardList) {
        this.hasCards = !cardList.isEmpty();

        if (this.hasCards) {
            // Compute the total count of classes and the latest expiration date
            int totalCountClasses = 0;
            DateTime cardExpirationDate = DateTime.now(); // todo
            for (DanceClassCard card : cardList
                    ) {
                totalCountClasses += card.getCount();
                if (card.getExpirationDate().isAfter(cardExpirationDate))
                    cardExpirationDate = card.getExpirationDate();
            }

            // Set card properties
            this.remainingCardClasses = totalCountClasses;
            this.cardExpirationDate = cardExpirationDate;
        }
    }

    public int getRemainingCardClasses() {
        return this.remainingCardClasses;
    }

    public boolean hasFewRemainingClasses() {
        return this.hasCards && this.remainingCardClasses <= 2;
    }

    public boolean isExpiringSoon() {
        return this.hasCards && cardExpirationDate.minusWeeks(1).isBeforeNow();
    }

    public boolean hasExpired() {
        return this.hasCards && cardExpirationDate.isBeforeNow();
    }
}
