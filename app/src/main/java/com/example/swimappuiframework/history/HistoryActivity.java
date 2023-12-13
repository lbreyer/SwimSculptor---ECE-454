package com.example.swimappuiframework.history;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.swimappuiframework.MainActivity;
import com.example.swimappuiframework.MyApp;
import com.example.swimappuiframework.R;
import com.example.swimappuiframework.data.HistoryItem;
import com.example.swimappuiframework.data.Workout;
import com.example.swimappuiframework.data.WorkoutItem;
import com.example.swimappuiframework.data.WorkoutSummaryItem;
import com.example.swimappuiframework.database.DatabaseViewModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    private DatabaseViewModel databaseViewModel;
    private List<HistoryItem> retrievedHistoryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        Button btnBack = findViewById(R.id.btnBack);

        ListView listViewWorkouts = findViewById(R.id.listViewWorkouts);

        // Create a list of WorkoutItems (you should define the WorkoutItem class).
        ArrayList<WorkoutSummaryItem> workoutSummaryItems = new ArrayList<>();

        databaseViewModel = ((MyApp) getApplication()).getWorkoutItemViewModel();
        List<HistoryItem> items =  databaseViewModel.getAllHistoryItems().getValue();

        // Populate the list with workout data.
        //workoutSummaryItems.add(new WorkoutSummaryItem("11/4/2023", "2:15:56", "2800"));
        //workoutSummaryItems.add(new WorkoutSummaryItem("11/5/2023", "1:45:36", "2450"));
        //workoutSummaryItems.add(new WorkoutSummaryItem("11/5/2023", "1:34:29", "2100"));


        // Retrieve the stored JSON string from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String json = sharedPreferences.getString("historyListKey", null);

        // If the JSON string exists
        if (json != null) {
            // Convert the JSON string back to a List<Workout>
            Gson gson = new Gson();
            Type type = new TypeToken<List<HistoryItem>>() {
            }.getType();
            retrievedHistoryList = gson.fromJson(json, type);
        }

        if(retrievedHistoryList != null && !retrievedHistoryList.isEmpty()){
            for (HistoryItem item : retrievedHistoryList) {
                workoutSummaryItems.add(new WorkoutSummaryItem(item));
            }
        }

//        if(items != null && !items.isEmpty()){
//            for (HistoryItem item : items) {
//                workoutSummaryItems.add(new WorkoutSummaryItem(item));
//            }
//        }

//        WorkoutSummaryItem temp = new WorkoutSummaryItem();
//        temp.setDate("12/12/2023");
//        temp.setTotalTime(120);
//        temp.setTotalYards("2400 yards");
//        temp.setWorkoutId(0);
//        temp.setCorrelationScores(new ArrayList<>());
//        workoutSummaryItems.add(temp);

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
