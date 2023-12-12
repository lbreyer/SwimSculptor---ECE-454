package com.example.swimappuiframework.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.List;

@Entity(tableName = "workout_history")
public class HistoryItem implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "workoutId")
    private long workoutId;

    @ColumnInfo(name = "date")
    private String date;

    @ColumnInfo(name = "time")
    private long time;

    @ColumnInfo(name = "correlations")
    private String corrValues;

    // TODO: Impliment additional fields as collected.

    public HistoryItem() {}

    public HistoryItem(long workoutId, LocalDateTime date, long time, List<List<List<Double>>> correlations) {
        this.workoutId = workoutId;
        this.date = date.toString();
        this.time = time;
        Gson gson = new Gson();
        this.corrValues = gson.toJson(correlations);
    }

    public long getWorkoutId() { return workoutId; }
    public void setWorkoutId(long workoutId) { this.workoutId = workoutId; }

    public String getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date.toString(); }
    public void setDate(String date) { this.date = date; }

    public long getTime() { return time; }
    public void setTime(long time) { this.time = time; }

    public String getCorrValues() { return corrValues; }
    public void setCorrValues(String values) { this.corrValues = values; }
    public List<List<List<Double>>> getCorrValuesList() {
        Gson gson = new Gson();
        Type type = new TypeToken<List<List<List<Double>>>>() {}.getType();
        return gson.fromJson(corrValues, type);
    }
    public void setCorrValues(List<List<List<Double>>> values) {
        Gson gson = new Gson();
        this.corrValues = gson.toJson(values);
    }
}
