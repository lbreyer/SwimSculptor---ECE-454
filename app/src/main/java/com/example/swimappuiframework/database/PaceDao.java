package com.example.swimappuiframework.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.swimappuiframework.data.HistoryItem;
import com.example.swimappuiframework.data.Pace;

import java.util.List;

@Dao
public interface PaceDao {
    @Insert
    long insert(Pace pace);

    @Query("SELECT * FROM paces")
    LiveData<List<Pace>> getAllPaces();

    @Query("SELECT * FROM paces WHERE id = :itemId")
    LiveData<Pace> getPaceById(long itemId);
}
