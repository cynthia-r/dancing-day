package com.cynthiar.dancingday;

//import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.cynthiar.dancingday.model.DanceClassLevel;
import com.cynthiar.dancingday.model.DummyItem;
import com.cynthiar.dancingday.model.classActivity.ClassActivity;
import com.cynthiar.dancingday.model.classActivity.PaymentType;
import com.cynthiar.dancingday.model.comparer.SingleDayDummyItemComparer;
import com.cynthiar.dancingday.database.AppDatabase;
import com.cynthiar.dancingday.model.DanceClassCard;
import com.cynthiar.dancingday.database.ClassActivityDao;
import com.cynthiar.dancingday.database.DanceClassCardDao;
import com.cynthiar.dancingday.model.Schools;
import com.cynthiar.dancingday.model.time.DanceClassTime;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;

/**
 * Created by CynthiaR on 12/27/2017.
 */
@RunWith(AndroidJUnit4.class)

public class DatabaseTests {

    @Before
    public void createDb() {
        Context context = InstrumentationRegistry.getTargetContext();
        AppDatabase.initializeDb(context, true);
        AppDatabase.clearDb();
    }

    @After
    public void closeDb() throws IOException {
        AppDatabase.clearDb();
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

    @Test
    public void registerAndCheckActivity() throws Exception {
        // Initialize test data
        DateTime activityDate = new DateTime(2018, 1, 1, 0, 0);
        PaymentType paymentType = PaymentType.PunchCard;
        DanceClassCard danceClassCard = new DanceClassCard(Schools.KDC_COMPANY, 3, new DateTime(2017, 12, 29, 00, 00), DateTime.now().plusDays(1));
        DummyItem danceClass = new DummyItem("Monday", new DanceClassTime(7, 0, 8, 30), Schools.KDC_SCHOOL, "Jerry", DanceClassLevel.Advanced);
        ClassActivity classActivity = new ClassActivity(danceClass, activityDate, paymentType, danceClassCard);

        // Save card
        DanceClassCardDao danceClassCardDao = new DanceClassCardDao();
        long cardId = danceClassCardDao.saveCard(danceClassCard);

        // Register the activity
        ClassActivityDao classActivityDao = new ClassActivityDao();
        classActivityDao.registerActivity(classActivity);

        // Check that the activity is registered
        List<ClassActivity> classActivityList = classActivityDao.getRecentActivityList();
        assertEquals(1, classActivityList.size());
        ClassActivity registeredActivity = classActivityList.get(0);
        assertEquals(activityDate, registeredActivity.getDate());
        assertEquals(paymentType, registeredActivity.getPaymentType());
        assertEquals(0, new SingleDayDummyItemComparer().compare(danceClass, registeredActivity.getDanceClass()));
        assertEquals(cardId, registeredActivity.getDanceClassCardId());

        // Check that the corresponding card has been debited
        DanceClassCard updatedCard = danceClassCardDao.getClassCardById(cardId);
        assertNotNull(updatedCard);
        assertEquals(2, updatedCard.getCount());
    }

    @Test
    public void processActivities() throws Exception {
        // Initialize test data
        DateTime activityDate1 = new DateTime(2018, 1, 1, 0, 0);
        DateTime activityDate2 = new DateTime(2018, 2, 9, 14, 30);
        DateTime activityDate3 = DateTime.now().minusDays(3);
        DateTime activityDate4 = DateTime.now().minusDays(2);
        DateTime activityDate5 = DateTime.now().plusHours(1);
        DummyItem danceClass = new DummyItem("Monday", new DanceClassTime(7, 0, 8, 30), Schools.KDC_SCHOOL, "Jerry", DanceClassLevel.Advanced);
        ClassActivity classActivity1 = new ClassActivity(danceClass, activityDate1, PaymentType.SingleTicket, null);
        classActivity1.confirm();
        ClassActivity classActivity2 = new ClassActivity(danceClass, activityDate2, PaymentType.SingleTicket, null);
        classActivity2.confirm();
        ClassActivity classActivity3 = new ClassActivity(danceClass, activityDate3, PaymentType.SingleTicket, null);
        classActivity3.confirm();
        ClassActivity classActivity4 = new ClassActivity(danceClass, activityDate4, PaymentType.SingleTicket, null);
        ClassActivity classActivity5 = new ClassActivity(danceClass, activityDate5, PaymentType.SingleTicket, null);

        // Save the activities
        ClassActivityDao classActivityDao = new ClassActivityDao();
        long activityId1 = classActivityDao.registerActivity(classActivity1);
        long activityId2 = classActivityDao.registerActivity(classActivity2);
        long activityId3 = classActivityDao.registerActivity(classActivity3);
        long activityId4 = classActivityDao.registerActivity(classActivity4);
        long activityId5 = classActivityDao.registerActivity(classActivity5);

        // Confirm pending activities: 4
        int confirmedActivityCount = classActivityDao.confirmPendingActivities();

        // Delete old activities: 1 and 2
        int deletedOldActivityCount = classActivityDao.deleteOldActivities();

        // Check that activity 4 was confirmed
        assertEquals(1, confirmedActivityCount);
        ClassActivity recentClassActivity4 = classActivityDao.getActivityById(activityId4);
        assertTrue(recentClassActivity4.isConfirmed());

        // Check that activities 1 and 2 were deleted
        assertEquals(2, deletedOldActivityCount);
        ClassActivity oldActivity1 = classActivityDao.getActivityById(activityId1);
        ClassActivity oldActivity2 = classActivityDao.getActivityById(activityId2);
        assertNull(oldActivity1);
        assertNull(oldActivity2);

        // Check that activity 3 and 5 were not modified
        ClassActivity activity3 = classActivityDao.getActivityById(activityId3);
        ClassActivity activity5 = classActivityDao.getActivityById(activityId5);
        assertNotNull(activity3);
        assertNotNull(activity5);
    }
}
