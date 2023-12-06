package com.example.swimappuiframework;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.swimappuiframework.create.CreateActivity;
import com.example.swimappuiframework.history.HistoryActivity;
import com.example.swimappuiframework.workout.WorkoutActivity;

public class MainActivity extends AppCompatActivity {
    //public static AppDatabase db;
    //public WorkoutItemViewModel workoutItemViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Database
        //db = AppDatabase.getInstance(getApplicationContext());
        //workoutItemViewModel = new ViewModelProvider(this).get(WorkoutItemViewModel.class);

        Button btnWorkout = findViewById(R.id.btnWorkout);
        Button btnCreate = findViewById(R.id.btnCreate);
        Button btnHistory = findViewById(R.id.btnHistory);

        btnWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Launch the Workout activity
                Intent intent = new Intent(MainActivity.this, WorkoutActivity.class);
                startActivity(intent);
            }
        });

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Launch the Create activity
                Intent intent = new Intent(MainActivity.this, CreateActivity.class);
                startActivity(intent);
            }
        });

        btnHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Launch the History activity
                Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
                startActivity(intent);
            }
        });
    }
}
