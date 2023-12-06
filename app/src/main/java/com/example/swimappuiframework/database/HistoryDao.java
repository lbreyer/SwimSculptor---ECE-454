package com.example.swimappuiframework.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.swimappuiframework.data.HistoryItem;
import com.example.swimappuiframework.data.WorkoutItem;

import java.util.List;

@Dao
public interface HistoryDao {
    @Insert
    long insert(HistoryItem historyItem);

    @Query("SELECT * FROM workout_history")
    LiveData<List<HistoryItem>> getAllHistoryItems();

    @Query("SELECT * FROM workout_history WHERE id = :itemId")
    LiveData<HistoryItem> getHistoryItemById(long itemId);
}
