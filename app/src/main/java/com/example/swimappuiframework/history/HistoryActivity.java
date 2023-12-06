package com.example.swimappuiframework.history;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.swimappuiframework.R;
import com.example.swimappuiframework.data.WorkoutSummaryItem;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        Button btnBack = findViewById(R.id.btnBack);
        ListView listViewWorkouts = findViewById(R.id.listViewWorkouts);

        // Create a list of WorkoutItems (you should define the WorkoutItem class).
        ArrayList<WorkoutSummaryItem> workoutSummaryItems = new ArrayList<>();
        // Populate the list with workout data.
        workoutSummaryItems.add(new WorkoutSummaryItem("11/4/2023", "2:15:56", "2800"));
        workoutSummaryItems.add(new WorkoutSummaryItem("11/5/2023", "1:45:36", "2450"));
        workoutSummaryItems.add(new WorkoutSummaryItem("11/5/2023", "1:34:29", "2100"));

        // Create a custom adapter for the list view.
        WorkoutListAdapter adapter = new WorkoutListAdapter(this, workoutSummaryItems);

        // Set the adapter to the list view.
        listViewWorkouts.setAdapter(adapter);

        // Handle item clicks to show the workout summary view.
        listViewWorkouts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                WorkoutSummaryItem selectedWorkout = workoutSummaryItems.get(position);
                // Start a new activity to display the workout summary.
                Intent intent = new Intent(HistoryActivity.this, WorkoutSummaryActivity.class);
                intent.putExtra("workout_item", selectedWorkout);
                startActivity(intent);
            }
        });

        // Handle the back button click to return to the main activity.
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}
