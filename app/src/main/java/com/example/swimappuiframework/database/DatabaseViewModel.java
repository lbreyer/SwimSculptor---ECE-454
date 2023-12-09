package com.example.swimappuiframework.database;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.swimappuiframework.data.HistoryItem;
import com.example.swimappuiframework.data.Pace;
import com.example.swimappuiframework.data.Workout;
import com.example.swimappuiframework.data.WorkoutItem;
import com.example.swimappuiframework.database.WorkoutRepository;

import java.util.List;

public class DatabaseViewModel extends AndroidViewModel {

    private WorkoutRepository repository;
    private LiveData<List<WorkoutItem>> allWorkoutItems;
    private LiveData<List<Workout>> allWorkouts;
    private LiveData<List<HistoryItem>> allHistoryItems;

    private LiveData<List<Pace>> allPaces;

    public DatabaseViewModel(@NonNull Application application) {
        super(application);
        repository = new WorkoutRepository(application);
        allWorkoutItems = repository.getAllWorkoutItems();
        allWorkouts = repository.getAllWorkouts();
        allHistoryItems = repository.getAllHistoryItems();
        allPaces = repository.getAllPaces();
    }

    public void insert(WorkoutItem workoutItem) {
        repository.insert(workoutItem);
    }

    public void insert(Workout workout) {
        repository.insert(workout);
    }

    public void insert(HistoryItem historyItem) { repository.insert(historyItem); }

    public void insert(Pace pace) { repository.insert(pace); }

    public LiveData<List<WorkoutItem>> getAllWorkoutItems() {
        return allWorkoutItems;
    }

    public LiveData<List<Workout>> getAllWorkouts() {
        return allWorkouts;
    }

    public LiveData<List<HistoryItem>> getAllHistoryItems() { return allHistoryItems; }

    public LiveData<List<Pace>> getAllPaces() { return allPaces; }
}
