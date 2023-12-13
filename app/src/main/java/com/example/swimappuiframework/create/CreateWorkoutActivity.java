package com.example.swimappuiframework.create;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.swimappuiframework.MyApp;
import com.example.swimappuiframework.R;
import com.example.swimappuiframework.data.Workout;
import com.example.swimappuiframework.data.WorkoutItem;
import com.example.swimappuiframework.database.DatabaseViewModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CreateWorkoutActivity extends AppCompatActivity implements AddWorkoutItemDialogFragment.OnDataPassedListener, SelectedWorkoutItemFragment.OnDataPassedListener {

    private DatabaseViewModel databaseViewModel;

    private RecyclerView mRecyclerView;

    private CWWorkoutItemAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_workout);

        databaseViewModel = ((MyApp) getApplication()).getWorkoutItemViewModel();

        // Set up the recycler view in the popup
        mRecyclerView = findViewById(R.id.recyclerview);
        mAdapter = new CWWorkoutItemAdapter(this, getSupportFragmentManager(), 0);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        Button btnBack = findViewById(R.id.btnBack);
        Button btnAddItem = findViewById(R.id.btnAddItem);
        Button btnSave = findViewById(R.id.btnSave);

        final EditText editTextName = findViewById(R.id.editTextWorkoutName);
        final EditText editTextNotes = findViewById(R.id.editTextNotes);


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btnAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showWorkoutItemPopup();
            }
        });

        // Observe changes in the workout items
//        databaseViewModel.getAllWorkoutItems().observe(this, new Observer<List<WorkoutItem>>() {
//            @Override
//            public void onChanged(List<WorkoutItem> workoutItems) {
//                updateWorkoutItemsUI(workoutItems);
//            }
//        });
        CreateWorkoutActivity activity = this;

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Retrieve the entered data from the EditText fields
                String name = editTextName.getText().toString();
                String notes = editTextNotes.getText().toString();

                // Create a WorkoutItem object with the entered data
                Workout workout = new Workout();
                workout.setName(name);
                workout.setNotes(notes);
                workout.setPojoWorkoutItems(mAdapter.mSelectedWorkoutItemList);
                workout.setTotalDistance(mAdapter.mSelectedWorkoutItemList);

                databaseViewModel.insert(workout);

                Random random = new Random();
                int randomNumber = random.nextInt(1000000);

                workout.id = randomNumber;

                // Retrieve the existing list of workouts from SharedPreferences
                SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                Gson gson = new Gson();
                String json = sharedPreferences.getString("workoutListKey", null);

                List<Workout> workoutList;

                // If the list doesn't exist yet, create a new one
                if (json == null) {
                    workoutList = new ArrayList<>();
                } else {
                    // Convert the JSON string back to a List<Workout>
                    Type type = new TypeToken<List<Workout>>() {}.getType();
                    workoutList = gson.fromJson(json, type);
                }

                // Add the new Workout to the list
                workoutList.add(workout);

                // Convert the updated list to JSON
                String updatedJson = gson.toJson(workoutList);

                // Save the updated JSON string back to SharedPreferences
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("workoutListKey", updatedJson);
                editor.apply();

                onBackPressed();
            }
        });
    }

    private void showWorkoutItemPopup() {
        AddWorkoutItemDialogFragment dialogFragment = new AddWorkoutItemDialogFragment();
        dialogFragment.show(getSupportFragmentManager(), "add_workout_item_dialog");
    }

//    private void updateWorkoutItemsUI(List<WorkoutItem> workoutItems) {
//        LinearLayout llWorkoutItemsContainer = findViewById(R.id.llWorkoutItemsContainer);
//        llWorkoutItemsContainer.removeAllViews();
//
//        for (WorkoutItem workoutItem : workoutItems) {
//            // Create a TextView for each workout item
//            TextView textView = new TextView(this);
//            textView.setText(workoutItem.getName());
//            llWorkoutItemsContainer.addView(textView);
//        }
//    }

    @Override
    public void onDataPassed(WorkoutItem data, int mode) {
        if (mode == 0) {
            mAdapter.mSelectedWorkoutItemList.add(data);
        }
        else if (mode == 1) {
            mAdapter.mSelectedWorkoutItemList.remove(data);
        }
        mAdapter.notifyDataSetChanged();
    }
}
