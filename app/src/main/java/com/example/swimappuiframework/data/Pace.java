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

    @ColumnInfo(name = "xPace")
    private String xPace;

    @ColumnInfo(name = "yPace")
    private String yPace;

    @ColumnInfo(name = "zPace")
    private String zPace;

    @ColumnInfo(name = "rollPace")
    private String rollPace;

    @ColumnInfo(name = "pitchPace")
    private String pitchPace;

    public Pace() {}

    public Pace(String name, String notes, List<Double> x, List<Double> y, List<Double> z, List<Double> roll, List<Double> pitch) {
        this.name = name;
        this.notes = notes == null ? "" : notes;
        this.xPace = convertListToString(x);
        this.yPace = convertListToString(y);
        this.zPace = convertListToString(z);
        this.rollPace = convertListToString(roll);
        this.pitchPace = convertListToString(pitch);
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

    public List<Double> getXPaceList() { return convertStringToList(xPace); }
    public void setXPaceList(List<Double> pace) { this.xPace = convertListToString(pace); }

    public List<Double> getYPaceList() { return convertStringToList(yPace); }
    public void setYPaceList(List<Double> pace) { this.yPace = convertListToString(pace); }

    public List<Double> getZPaceList() { return convertStringToList(zPace); }
    public void setZPaceList(List<Double> pace) { this.zPace = convertListToString(pace); }

    public List<Double> getRollPaceList() { return convertStringToList(rollPace); }
    public void setRollPaceList(List<Double> pace) { this.rollPace = convertListToString(pace); }

    public List<Double> getPitchPaceList() { return convertStringToList(pitchPace); }
    public void setPitchPaceList(List<Double> pace) { this.pitchPace = convertListToString(pace); }


    // DO NOT USE
    public String getXPace() { return xPace; }
    public void setXPace(String pace) { this.xPace = pace; }

    public String getYPace() { return yPace; }
    public void setYPace(String pace) { this.yPace = pace; }

    public String getZPace() { return zPace; }
    public void setZPace(String pace) { this.zPace = pace; }

    public String getRollPace() { return rollPace; }
    public void setRollPace(String pace) { this.rollPace = pace; }

    public String getPitchPace() { return pitchPace; }
    public void setPitchPace(String pace) { this.pitchPace = pace; }
}
