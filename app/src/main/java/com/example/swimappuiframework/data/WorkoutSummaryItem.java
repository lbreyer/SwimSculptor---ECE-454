package com.example.swimappuiframework.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class WorkoutSummaryItem implements Serializable {
    private String date;
    private long totalTime;
    private String totalYards;
    private long workoutId;
    private List<List<Double>> correlationScores;

    public WorkoutSummaryItem() {}

    public WorkoutSummaryItem(String date, long totalTime, String totalYards) {
        this.date = date;
        this.totalTime = totalTime;
        this.totalYards = totalYards;
    }

    public WorkoutSummaryItem(HistoryItem item) {
        this.date = item.getDate();
        this.totalTime = item.getTime();
        this.totalYards = "50 yards"; // TODO update this with total yards
        this.workoutId = item.getWorkoutId();
        this.correlationScores = stringToList(item.getCorrValues());
    }

    public String getDate() {
        return date;
    }
    public void setDate(String date) { this.date = date; }

    public long getTotalTime() {
        return totalTime;
    }
    public void setTotalTime(long totalTime) { this.totalTime = totalTime; }

    public String getTotalYards() {
        return totalYards;
    }
    public void setTotalYards(String totalYards) { this.totalYards = totalYards; }

    public long getWorkoutId() { return workoutId; }
    public void setWorkoutId(long id) { this.workoutId = id; }

    public List<List<Double>> getCorrelationScores() { return correlationScores; }
    public void setCorrelationScores(List<List<Double>> scores) { this.correlationScores = correlationScores; }

    public static List<List<Double>> stringToList(String input) {
        List<List<Double>> result = new ArrayList<>();
        String[] lines = input.split("\n");

        for (String line : lines) {
            List<Double> innerList = new ArrayList<>();
            String[] values = line.trim().split(" ");

            for (String value : values) {
                innerList.add(Double.parseDouble(value));
            }

            result.add(innerList);
        }

        return result;
    }
}
