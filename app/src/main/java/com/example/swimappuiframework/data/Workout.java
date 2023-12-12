package com.example.swimappuiframework.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Entity(tableName = "workouts")
public class Workout implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "notes")
    private String notes;

    @ColumnInfo(name = "workoutItems")
    private String pojoWorkoutItems;

    @ColumnInfo(name = "distance")
    private int totalDistance;

    public Workout() {}

    public Workout(String name, String notes, List<WorkoutItem> workoutItems) {
        this.name = name;
        this.notes = notes;
        this.pojoWorkoutItems = listToPojo(workoutItems);
        this.totalDistance = calcTotalDistance(workoutItems);
    }

    private String listToPojo(List<WorkoutItem> workoutItems) {
        String wi = "";
        for (WorkoutItem item : workoutItems) {
            wi += String.valueOf(item.id) + " ";
        }
        return wi;
    }

    private int calcTotalDistance(List<WorkoutItem> items) {
        int distance = 0;
        for (WorkoutItem item : items) {
            distance += item.getCount() * item.getDistance();
        }
        return distance;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getNotes() {
        return notes;
    }
    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getPojoWorkoutItems() {
        return pojoWorkoutItems;
    }
    public void setPojoWorkoutItems(List<WorkoutItem> workoutItems) {
        this.pojoWorkoutItems = listToPojo(workoutItems);
    }
    public void setPojoWorkoutItems(String workoutItems) {
        this.pojoWorkoutItems = workoutItems;
    }

    public int getTotalDistance() { return totalDistance; }
    public void setTotalDistance(int totalDistance) { this.totalDistance = totalDistance; }

    public void setTotalDistance(List<WorkoutItem> items) { this.totalDistance = calcTotalDistance(items); }
}
