package com.example.swimappuiframework.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.swimappuiframework.data.HistoryItem;
import com.example.swimappuiframework.data.Pace;
import com.example.swimappuiframework.data.Workout;
import com.example.swimappuiframework.data.WorkoutItem;

@Database(entities = {WorkoutItem.class, Workout.class, HistoryItem.class, Pace.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract WorkoutItemDao workoutItemDao();
    public abstract WorkoutDao workoutDao();
    public abstract HistoryDao historyDao();
    public abstract PaceDao paceDao();

    private static AppDatabase INSTANCE;

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "app_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}

