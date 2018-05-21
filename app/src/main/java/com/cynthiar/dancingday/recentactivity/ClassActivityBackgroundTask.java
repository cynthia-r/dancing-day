package com.cynthiar.dancingday.recentactivity;

import android.os.AsyncTask;

import com.cynthiar.dancingday.dataprovider.IConsumerCallback;
import com.cynthiar.dancingday.database.ClassActivityDao;

/**
 * Created by cynthiar on 2/25/2018.
 */

public class ClassActivityBackgroundTask extends AsyncTask<Void, Void, ClassActivityBackgroundTask.ClassActivityBackgroundResult> {

    // DAO
    private ClassActivityDao classActivityDao;
    private IConsumerCallback<ClassActivityBackgroundResult> consumerCallback;

    public ClassActivityBackgroundTask(ClassActivityDao classActivityDao, IConsumerCallback<ClassActivityBackgroundResult> consumerCallback) {
        this.classActivityDao = classActivityDao;
        this.consumerCallback = consumerCallback;
    }

    @Override
    protected ClassActivityBackgroundResult doInBackground(Void... params) {
        // Confirm the pending activities
        int confirmedCount = this.classActivityDao.confirmPendingActivities();

        // Delete the old activities (older than 60 days)
        int deletedCount = this.classActivityDao.deleteOldActivities();

        // Return the results
        return new ClassActivityBackgroundResult(confirmedCount, deletedCount);
    }

    /**
     * Updates the IConsumerCallback with the result.
     */
    @Override
    protected void onPostExecute(ClassActivityBackgroundResult result) {
        consumerCallback.updateFromResult(result);
    }

    public class ClassActivityBackgroundResult {
        private int confirmedActivityCount;
        private int deletedOldActivityCount;

        public ClassActivityBackgroundResult(int confirmedActivityCount, int deletedOldActivityCount) {
            this.confirmedActivityCount = confirmedActivityCount;
            this.deletedOldActivityCount = deletedOldActivityCount;
        }

        public int getConfirmedActivityCount() {
            return confirmedActivityCount;
        }

        public int getDeletedOldActivityCount() {
            return deletedOldActivityCount;
        }
    }
}
