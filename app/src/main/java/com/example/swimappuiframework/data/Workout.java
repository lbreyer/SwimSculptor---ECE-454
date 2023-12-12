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

    public Workout() {}

    public Workout(String name, String notes, List<WorkoutItem> workoutItems) {
        this.name = name;
        this.notes = notes;
        this.pojoWorkoutItems = listToPojo(workoutItems);
    }

    private String listToPojo(List<WorkoutItem> workoutItems) {
        String wi = "";
        for (WorkoutItem item : workoutItems) {
            wi += String.valueOf(item.id) + " ";
        }
        return wi;
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
}
