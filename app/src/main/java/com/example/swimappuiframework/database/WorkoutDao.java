package com.example.swimappuiframework.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.swimappuiframework.data.Workout;
import com.example.swimappuiframework.data.WorkoutItem;

import java.util.List;

@Dao
public interface WorkoutDao {
    @Insert
    long insert(Workout workout);

    @Query("SELECT * FROM workouts")
    LiveData<List<Workout>> getAllWorkouts();

    @Query("SELECT * FROM workouts WHERE id = :itemId")
    LiveData<Workout> getWorkoutById(long itemId);
}
