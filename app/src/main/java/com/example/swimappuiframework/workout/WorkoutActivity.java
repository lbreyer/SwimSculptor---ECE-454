package com.example.swimappuiframework.workout;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.example.swimappuiframework.create.CreateWorkoutActivity;
import com.example.swimappuiframework.R;

public class WorkoutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);

        Button btnBack = findViewById(R.id.btnBack);
        Button btnSelectWorkout = findViewById(R.id.btnSelectWorkout);
        Button btnCreateNewWorkout = findViewById(R.id.btnCreateNewWorkout);

        // Handle the back button click to return to the MainActivity
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        // Handle the "Select Workout" button click here

        btnSelectWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Implement the logic to select a workout
            }
        });

        // Handle the "Create New Workout" button click to go to CreateWorkoutActivity
        btnCreateNewWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WorkoutActivity.this, CreateWorkoutActivity.class);
                startActivity(intent);
            }
        });
    }
}
