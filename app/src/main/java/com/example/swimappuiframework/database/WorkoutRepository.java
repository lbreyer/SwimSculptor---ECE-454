package com.example.swimappuiframework.database;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.swimappuiframework.data.HistoryItem;
import com.example.swimappuiframework.data.Workout;
import com.example.swimappuiframework.data.WorkoutItem;

import java.util.List;

public class WorkoutRepository {

    private WorkoutItemDao workoutItemDao;
    private WorkoutDao workoutDao;
    private HistoryDao historyDao;
    private LiveData<List<WorkoutItem>> allWorkoutItems;
    private LiveData<List<Workout>> allWorkouts;
    private LiveData<List<HistoryItem>> allHistoryItems;

    public WorkoutRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        workoutItemDao = database.workoutItemDao();
        workoutDao = database.workoutDao();
        historyDao = database.historyDao();
        allWorkoutItems = workoutItemDao.getAllWorkoutItems();
        allWorkouts = workoutDao.getAllWorkouts();
        allHistoryItems = historyDao.getAllHistoryItems();
    }

    public void insert(WorkoutItem workoutItem) {
        // Perform database insert on a background thread
        new InsertWorkoutItemAsyncTask(workoutItemDao).execute(workoutItem);
    }

    public void insert(Workout workout) {
        // Perform database insert on a background thread
        new InsertWorkoutAsyncTask(workoutDao).execute(workout);
    }

    public void insert(HistoryItem historyItem) {
        // Perform database insert on a background thread
        new InsertHistoryItemAsyncTask(historyDao).execute(historyItem);
    }

    public LiveData<List<WorkoutItem>> getAllWorkoutItems() {
        return allWorkoutItems;
    }

    public LiveData<List<Workout>> getAllWorkouts() {
        return allWorkouts;
    }

    public LiveData<List<HistoryItem>> getAllHistoryItems() { return allHistoryItems; }

    private static class InsertWorkoutItemAsyncTask extends AsyncTask<WorkoutItem, Void, Void> {
        private WorkoutItemDao workoutItemDao;

        private InsertWorkoutItemAsyncTask(WorkoutItemDao workoutItemDao) {
            this.workoutItemDao = workoutItemDao;
        }

        @Override
        protected Void doInBackground(WorkoutItem... workoutItems) {
            workoutItemDao.insert(workoutItems[0]);
            return null;
        }
    }

    private static class InsertWorkoutAsyncTask extends AsyncTask<Workout, Void, Void> {
        private WorkoutDao workoutDao;

        private InsertWorkoutAsyncTask(WorkoutDao workoutDao) {
            this.workoutDao = workoutDao;
        }

        @Override
        protected Void doInBackground(Workout... workouts) {
            workoutDao.insert(workouts[0]);
            return null;
        }
    }

    private static class InsertHistoryItemAsyncTask extends AsyncTask<HistoryItem, Void, Void> {
        private HistoryDao historyDao;

        private InsertHistoryItemAsyncTask(HistoryDao historyDao) {
            this.historyDao = historyDao;
        }

        @Override
        protected Void doInBackground(HistoryItem... historyItems) {
            historyDao.insert(historyItems[0]);
            return null;
        }
    }
}
