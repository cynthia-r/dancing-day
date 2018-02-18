package com.cynthiar.dancingday;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.cynthiar.dancingday.model.DummyItem;
import com.cynthiar.dancingday.recentactivity.RecentActivityDetailsActivity;

/**
 * Manages building the notification for class activities.
 */
public class ClassActivityNotification {
    public static final int NOTIFICATION_ID = 1;
    public static final String CLASS_ACTIVITY_ID_KEY = "Class_Activity_Id";
    public static final String CLASS_ACTIVITY_CONFIRMED_KEY = "Class_Activity_Confirmed";
    public static final String NOTIFICATION_ACTION_KEY = "Notification";

    private Context context;

    public ClassActivityNotification(Context context) {
        this.context = context;
    }

    /**
     * Builds a notification for the specified class activity.
     * @param classActivityId: Id of the class activity that was registered.
     * @return: The notification for the class activity.
     */
    public Notification buildNotification(long classActivityId, DummyItem danceClass) {
        // Setup the details activity to open on notification click
        Intent detailsActivityIntent = new Intent(context, DetailsActivity.class);
        Bundle detailsActivityBundle = DetailsActivity.toBundle(danceClass);
        detailsActivityIntent.putExtra(DetailsActivity.DANCE_CLASS_KEY, detailsActivityBundle);
        PendingIntent detailsActivityPendingIntent = this.buildPendingIntent(0, DetailsActivity.class, detailsActivityIntent);

        // Setup the activities to open on action click
        // The "Recent Activity" will be opened on "Confirm"
        Intent recentActivityConfirmedIntent = new Intent(context, RecentActivityDetailsActivity.class);
        recentActivityConfirmedIntent.putExtra(ClassActivityNotification.CLASS_ACTIVITY_ID_KEY, classActivityId);
        recentActivityConfirmedIntent.putExtra(ClassActivityNotification.NOTIFICATION_ACTION_KEY, true);
        recentActivityConfirmedIntent.putExtra(ClassActivityNotification.CLASS_ACTIVITY_CONFIRMED_KEY, true);

        // The "Main Activity" will be opened on "Cancelled"
        Intent todayActivityCancelledIntent = new Intent(context, TodayActivity.class);
        todayActivityCancelledIntent.putExtra(ClassActivityNotification.CLASS_ACTIVITY_ID_KEY, classActivityId);
        todayActivityCancelledIntent.putExtra(ClassActivityNotification.NOTIFICATION_ACTION_KEY, true);
        todayActivityCancelledIntent.putExtra(ClassActivityNotification.CLASS_ACTIVITY_CONFIRMED_KEY, false);

        // Setup the pending intents
        PendingIntent recentActivityConfirmedPendingIntent = this.buildPendingIntent(1, RecentActivityDetailsActivity.class, recentActivityConfirmedIntent);
        PendingIntent recentActivityCancelledPendingIntent = this.buildPendingIntent(2, TodayActivity.class, todayActivityCancelledIntent);

        // Create a notification
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.star)
                        .setContentTitle("Dancing day")
                        .setContentText("Time for class")
                        .setContentIntent(detailsActivityPendingIntent)
                        .addAction(android.R.drawable.ic_menu_save, "Confirm", recentActivityConfirmedPendingIntent)
                        .addAction(android.R.drawable.ic_menu_close_clear_cancel, "Cancel", recentActivityCancelledPendingIntent);

        // Build and return the notification
        return mBuilder.build();
    }

    /**
     * Builds a pending intent from the specified information.
     * @param requestCode: The request code for this intent.
     * @param activityClass: The class of the activity to open.
     * @param intent: The intent.
     * @return: The pending intent.
     */
    private PendingIntent buildPendingIntent(int requestCode, Class activityClass, Intent intent) {
        // Create a stack
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        // Add the back stack
        stackBuilder.addParentStack(activityClass);
        // Add the Intent to the top of the stack
        stackBuilder.addNextIntent(intent);
        // Get a PendingIntent containing the entire back stack
        PendingIntent pendingIntent =
                stackBuilder.getPendingIntent(requestCode, PendingIntent.FLAG_UPDATE_CURRENT);
        return pendingIntent;
    }
}
