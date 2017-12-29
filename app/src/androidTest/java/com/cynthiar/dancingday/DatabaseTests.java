package com.cynthiar.dancingday;

//import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.cynthiar.dancingday.model.AppDatabase;
import com.cynthiar.dancingday.model.DanceClassCard;
import com.cynthiar.dancingday.model.DanceClassCardDao;
import com.cynthiar.dancingday.model.Schools;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.List;

import static junit.framework.Assert.assertEquals;

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
    public void writeUserAndReadInList() throws Exception {
        DanceClassCard user = new DanceClassCard(Schools.DanceCompany.fromString("ADI"), 2, new DateTime(2017, 12, 23, 12, 45, 00), new DateTime(2017, 12, 23, 12, 45, 00));
        DanceClassCardDao danceClassCardDao = new DanceClassCardDao();
        danceClassCardDao.saveCard(user);
        List<DanceClassCard> allCards = danceClassCardDao.getClassCardList();
        assertEquals(1, allCards.size());
    }
}
