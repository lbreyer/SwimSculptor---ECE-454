package com.example.swimappuiframework.history;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.swimappuiframework.R;
import com.example.swimappuiframework.data.WorkoutSummaryItem;

public class WorkoutSummaryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_summary);

        Button btnBack = findViewById(R.id.btnBack);
        TextView textViewDate = findViewById(R.id.textViewDate);
        TextView textViewTotalTime = findViewById(R.id.textViewTotalTime);
        TextView textViewTotalYards = findViewById(R.id.textViewTotalYards);

        // Retrieve workout data from the intent.
        Intent intent = getIntent();
        if (intent != null) {
            WorkoutSummaryItem workoutSummaryItem = (WorkoutSummaryItem) intent.getSerializableExtra("workout_item");            //String totalTime = intent.getStringExtra("total_time");
            //String totalYards = intent.getStringExtra("total_yards");

            // Set the text values for the TextViews.
            textViewDate.setText("Date: " + workoutSummaryItem.getDate());
            textViewTotalTime.setText("Total Time: " + workoutSummaryItem.getTotalTime());
            textViewTotalYards.setText("Total Yards: " + workoutSummaryItem.getTotalYards());
        }

        // Handle the back button click to return to the HistoryActivity.
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}
