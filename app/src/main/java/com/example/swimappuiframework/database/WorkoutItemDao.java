package com.example.swimappuiframework.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.swimappuiframework.data.WorkoutItem;

import java.util.List;

@Dao
public interface WorkoutItemDao {
    @Insert
    long insert(WorkoutItem workoutItem);

    @Query("SELECT * FROM workout_items")
    LiveData<List<WorkoutItem>> getAllWorkoutItems();

    @Query("SELECT * FROM workout_items WHERE id = :itemId")
    LiveData<WorkoutItem> getWorkoutItemById(long itemId);
}
