package com.example.swimappuiframework.data;

import java.io.Serializable;

public class WorkoutSummaryItem implements Serializable {
    private String date;
    private String totalTime;
    private String totalYards;

    public WorkoutSummaryItem(String date, String totalTime, String totalYards) {
        this.date = date;
        this.totalTime = totalTime;
        this.totalYards = totalYards;
    }

    public String getDate() {
        return date;
    }

    public String getTotalTime() {
        return totalTime;
    }

    public String getTotalYards() {
        return totalYards;
    }
}
