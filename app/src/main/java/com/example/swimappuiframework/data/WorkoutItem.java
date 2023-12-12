package com.example.swimappuiframework.data;

import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;

@Entity(tableName = "workout_items")
public class WorkoutItem implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "count")
    private int count;

    @ColumnInfo(name = "distance")
    private int distance;

    @ColumnInfo(name = "pace")
    private String pace;

    @ColumnInfo(name = "notes")
    private String notes;

    public WorkoutItem() {}

    public WorkoutItem(String name, int count, int distance, String pace, String notes) {
        this.name = name;
        this.count = count;
        this.distance = distance;
        this.pace = pace;
        this.notes = notes;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }
    public void setCount(int count) {
        this.count = count;
    }

    public int getDistance() {
        return distance;
    }
    public void setDistance(int distance) {
        this.distance = distance;
    }

    public String getPace() {
        return pace;
    }
    public void setPace(String pace) {
        this.pace = pace;
    }

    public String getNotes() {
        return notes;
    }
    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Pace getPaceObject() {
        Gson gson = new Gson();
        Type type = new TypeToken<Pace>() {}.getType();
        return gson.fromJson(pace, type);
    }
    public void setPace(Pace pace) {
        Gson gson = new Gson();
        this.pace = gson.toJson(pace);
    }

}
