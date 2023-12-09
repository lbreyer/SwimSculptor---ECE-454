package com.example.swimappuiframework.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
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

    public HistoryItem(long workoutId, LocalDateTime date, long time, List<List<Double>> correlations) {
        this.workoutId = workoutId;
        this.date = date.toString();
        this.time = time;
        this.corrValues = listToSting(correlations);
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

    public static String listToSting(List<List<Double>> listOfLists) {
        StringBuilder sb = new StringBuilder();

        for (List<Double> innerList : listOfLists) {
            for (Double value : innerList) {
                sb.append(value).append(" ");
            }
            sb.append("\n");
        }

        return sb.toString();
    }
}
