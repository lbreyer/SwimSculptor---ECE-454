package com.example.swimappuiframework.history;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.swimappuiframework.MyApp;
import com.example.swimappuiframework.R;
import com.example.swimappuiframework.create.AddWorkoutItemDialogFragment;
import com.example.swimappuiframework.create.CWWorkoutItemAdapter;
import com.example.swimappuiframework.data.Workout;
import com.example.swimappuiframework.data.WorkoutItem;
import com.example.swimappuiframework.data.WorkoutSummaryItem;
import com.example.swimappuiframework.database.DatabaseViewModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class WorkoutSummaryActivity extends AppCompatActivity {

    private DatabaseViewModel databaseViewModel;
    private Workout selWorkout;
    private List<WorkoutItem> workoutItems;
    private RecyclerView mRecyclerView;
    private CWWorkoutItemAdapter mAdapter;

    private WorkoutSummaryItem workoutSummaryItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_summary);

        databaseViewModel = ((MyApp) getApplication()).getWorkoutItemViewModel();

        Button btnBack = findViewById(R.id.btnBack);

        TextView textViewDate = findViewById(R.id.textViewDate);
        TextView textViewTotalTime = findViewById(R.id.textViewTime);
        TextView textViewTotalYards = findViewById(R.id.textViewTotalYards);
        TextView textViewSugg = findViewById(R.id.textViewSuggestionList);

        // Retrieve workout data from the intent.
        Intent intent = getIntent();
        if (intent != null) {
            workoutSummaryItem = (WorkoutSummaryItem) intent.getSerializableExtra("workout_item");
            //String totalTime = intent.getStringExtra("total_time");
            //String totalYards = intent.getStringExtra("total_yards");

            // Set the text values for the TextViews.
            //textViewDate.setText(workoutSummaryItem.getDate());
            //textViewTotalTime.setText(workoutSummaryItem.getTotalTime() + " minutes");
            //textViewTotalYards.setText("Total Yards: " + workoutSummaryItem.getTotalYards());

            textViewDate.setText(workoutSummaryItem.getDate());
            textViewTotalTime.setText(workoutSummaryItem.getTotalTime() + " minutes");
            textViewTotalYards.setText("Total Yards: " + workoutSummaryItem.getTotalYards());
            String sugg = getString(R.string.Overall_Sugg) + getString(R.string.Overall_Sugg2);
            textViewSugg.setText(sugg);
        }

        // Get Workout Items
        List<Workout> retrievedWorkouts = new ArrayList<>();
        // Retrieve the stored JSON string from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String woJson = sharedPreferences.getString("workoutListKey", null);

        // If the JSON string exists
        if (woJson != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<Workout>>() {
            }.getType();
            retrievedWorkouts = gson.fromJson(woJson, type);
        }

        for (Workout wo : retrievedWorkouts) {
            if (wo.id == workoutSummaryItem.getWorkoutId()) {
                selWorkout = wo;
            }
        }

        // Get Workout Items
        List<WorkoutItem> retrievedWOItems = new ArrayList<>();
        // Retrieve the stored JSON string from SharedPreferences
        String wiJson = sharedPreferences.getString("workoutItemListKey", null);
        workoutItems = new ArrayList<>();

        // If the JSON string exists
        if (wiJson != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<WorkoutItem>>() {
            }.getType();
            retrievedWOItems = gson.fromJson(wiJson, type);
        }
        List<Integer> woi = new ArrayList<>();
        String[] numbersArray = selWorkout.getPojoWorkoutItems().split(" ");

        for (String number : numbersArray) {
            try {
                int num = Integer.parseInt(number);
                woi.add(num);
            } catch (NumberFormatException e) {
                // Handle parsing exceptions if needed
                e.printStackTrace();
            }
        }

        for (Integer i : woi) {
            for (WorkoutItem itemWo : retrievedWOItems) {
                if (itemWo.id == i) {
                    workoutItems.add(itemWo);
                    break;
                }
            }
        }

        // Define Text Fields
        TextView textViewName = findViewById(R.id.textViewName);
        textViewName.setText(selWorkout.getName());

        // DEMO ITEMS
        List<String> suggs = new ArrayList<>();
        String s1 = "(Warmup Pace Untracked)";
        String s2 = "(Warmup Pace Untracked)";
        String s3 = "- Make sure to maintain good body position out of turns and starts.\n" +
                "- Focus on holding a tight streamline longer out of turns.";
        String s4 = "- High average correlation. Good Job!";

        suggs.add(s1);
        suggs.add(s2);
        suggs.add(s3);
        suggs.add(s4);

        mRecyclerView = findViewById(R.id.recyclerview);
        mAdapter = new CWWorkoutItemAdapter(this, getSupportFragmentManager(), 1, suggs);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        for (WorkoutItem itemWof : workoutItems) {
            mAdapter.mSelectedWorkoutItemList.add(itemWof);
        }
        mAdapter.notifyDataSetChanged();


        // Handle the back button click to return to the HistoryActivity.
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }


}
