package com.cynthiar.dancingday;

import android.arch.persistence.room.Room;
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

/**
 * Created by CynthiaR on 12/27/2017.
 */
@RunWith(AndroidJUnit4.class)

public class DatabaseTests {
    private DanceClassCardDao mUserDao;
    private AppDatabase mDb;

    @Before
    public void createDb() {
        Context context = InstrumentationRegistry.getTargetContext();
        mDb = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
        mUserDao = mDb.getClassCardDao();
    }

    @After
    public void closeDb() throws IOException {
        mDb.close();
    }

    @Test
    public void writeUserAndReadInList() throws Exception {
        DanceClassCard user = new DanceClassCard(Schools.DanceCompany.fromString("ADI"), 2, new DateTime(2017, 12, 23, 12, 45, 00), new DateTime(2017, 12, 23, 12, 45, 00));

        mUserDao.saveCard(user);
        List<DanceClassCard> byName = mUserDao.getClassCardList();

    }
}
