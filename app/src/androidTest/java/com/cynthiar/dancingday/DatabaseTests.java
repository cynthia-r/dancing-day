package com.cynthiar.dancingday;

//import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.cynthiar.dancingday.model.database.AppDatabase;
import com.cynthiar.dancingday.model.DanceClassCard;
import com.cynthiar.dancingday.model.database.DanceClassCardDao;
import com.cynthiar.dancingday.model.Schools;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

/**
 * Created by CynthiaR on 12/27/2017.
 */
@RunWith(AndroidJUnit4.class)

public class DatabaseTests {

    @Before
    public void createDb() {
        Context context = InstrumentationRegistry.getTargetContext();
        context.deleteDatabase(AppDatabase.DATABASE_NAME);
        AppDatabase.initializeDb(context);
    }

    @After
    public void closeDb() throws IOException {
        AppDatabase.finalizeDb();
    }

    @Test
    public void insertCardAndSelect() throws Exception {
        // Initialize test data
        Schools.DanceCompany danceCompany = Schools.ADI_COMPANY;
        int count = 2;
        DateTime expirationDate = new DateTime(2017, 12, 23, 00, 00, 00);
        DateTime purchaseDate = new DateTime(2017, 12, 18, 00, 00, 00);
        DanceClassCard classCard = new DanceClassCard(danceCompany, count, purchaseDate, expirationDate);

        // Save card
        DanceClassCardDao danceClassCardDao = new DanceClassCardDao();
        long cardId = danceClassCardDao.saveCard(classCard);

        // Retrieve card
        List<DanceClassCard> allCards = danceClassCardDao.getClassCardList();

        // Check results
        assertNotNull(cardId);
        assertEquals(1, allCards.size());
        DanceClassCard danceClassCard = allCards.get(0);
        assertEquals(danceCompany, danceClassCard.getCompany());
        assertEquals(count, danceClassCard.getCount());
        assertEquals(expirationDate, danceClassCard.getExpirationDate());
        assertEquals(purchaseDate, danceClassCard.getPurchaseDate());

        // Update card
        int newCount = 3;
        danceClassCard.setCount(newCount);
        DateTime newExpirationDate = new DateTime(2017, 12, 27, 00, 00, 00);
        danceClassCard.setExpirationDate(newExpirationDate);
        int updatedCount = danceClassCardDao.updateCard(danceClassCard);

        // Check results
        assertEquals(1, updatedCount);
        danceClassCard = danceClassCardDao.getClassCardList().get(0);
        assertEquals(danceCompany, danceClassCard.getCompany());
        assertEquals(newCount, danceClassCard.getCount());
        assertEquals(newExpirationDate, danceClassCard.getExpirationDate());
        assertEquals(purchaseDate, danceClassCard.getPurchaseDate());

        // Delete card
        int deletedCount = danceClassCardDao.deleteCard(danceClassCard);

        // Check results
        assertEquals(1, deletedCount);
        allCards = danceClassCardDao.getClassCardList();
        assertEquals(0, allCards.size());
    }
}
