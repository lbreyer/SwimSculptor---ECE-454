package com.example.swimappuiframework.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Entity(tableName = "paces")
public class Pace implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "notes")
    private String notes;

    @ColumnInfo(name = "pace")
    private String pace;

    public Pace() {}

    public Pace(String name, String notes, List<Double> pace) {
        this.name = name;
        this.notes = notes == null ? "" : notes;
        this.pace = convertListToString(pace);
    }

    public static String convertListToString(List<Double> doubleList) {
        String result = doubleList.stream()
                .map(Object::toString)
                .collect(Collectors.joining(","));
        return result;
    }

    public static List<Double> convertStringToList(String doubleString) {
        List<Double> result = Arrays.stream(doubleString.split(","))
                .map(Double::parseDouble)
                .collect(Collectors.toList());
        return result;
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

    public List<Double> getPaceList() { return convertStringToList(pace); }
    public void setPace(List<Double> pace) { this.pace = convertListToString(pace); }

    // DO NOT USE THIS METHOD
    public String getPace() { return pace; }
    // DO NOT USE THIS METHOD
    public void setPace(String pace) { this.pace = pace; }
}
