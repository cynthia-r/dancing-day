package com.cynthiar.dancingday.model;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created by CynthiaR on 12/27/2017.
 */
@Dao
public interface DanceClassCardDao {

    @Query("SELECT * FROM DanceClassCard")
    public List<DanceClassCard> getClassCardList();

    @Insert
    public void saveCard(DanceClassCard danceClassCard);

    /*public void updateCard(String oldCardKey, DanceClassCard danceClassCard) {
        this.preferencesModel.updateCard(oldCardKey, danceClassCard);
    }

    public void deleteCard(String cardKey) {
        this.preferencesModel.deleteCard(cardKey);
    }
*/
}
