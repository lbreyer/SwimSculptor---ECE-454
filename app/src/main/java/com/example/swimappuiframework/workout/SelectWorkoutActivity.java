package com.example.swimappuiframework.workout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.swimappuiframework.MyApp;
import com.example.swimappuiframework.R;
import com.example.swimappuiframework.data.HistoryItem;
import com.example.swimappuiframework.data.Workout;
import com.example.swimappuiframework.data.WorkoutItem;
import com.example.swimappuiframework.data.WorkoutSummaryItem;
import com.example.swimappuiframework.database.DatabaseViewModel;
import com.example.swimappuiframework.history.HistoryActivity;
import com.example.swimappuiframework.history.WorkoutListAdapter;
import com.example.swimappuiframework.history.WorkoutSummaryActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SelectWorkoutActivity extends AppCompatActivity {

    private DatabaseViewModel databaseViewModel;
    private List<Workout> retrievedWorkoutList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_workout);

        databaseViewModel = ((MyApp) getApplication()).getWorkoutItemViewModel();

        Button btnBack = findViewById(R.id.btnBack);

        ListView listViewWorkouts = findViewById(R.id.listViewWorkouts);

        // Retrieve the stored JSON string from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String json = sharedPreferences.getString("workoutListKey", null);

        // If the JSON string exists
        if (json != null) {
            // Convert the JSON string back to a List<Workout>
            Gson gson = new Gson();
            Type type = new TypeToken<List<Workout>>() {
            }.getType();
            retrievedWorkoutList = gson.fromJson(json, type);
        }

        SelectWorkoutAdapter adapter = new SelectWorkoutAdapter(this, retrievedWorkoutList);
        listViewWorkouts.setAdapter(adapter);

        listViewWorkouts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Workout selectedWorkout = retrievedWorkoutList.get(position);
                // Start a new activity to display the workout summary.
                Intent intent = new Intent(SelectWorkoutActivity.this, ActiveWorkoutActivity.class);
                intent.putExtra("Workout", selectedWorkout);
                startActivity(intent);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}