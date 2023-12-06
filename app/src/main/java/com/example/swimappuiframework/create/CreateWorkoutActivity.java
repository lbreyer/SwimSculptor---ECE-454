package com.example.swimappuiframework.create;

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

import java.util.List;

public class CreateWorkoutActivity extends AppCompatActivity implements AddWorkoutItemDialogFragment.OnDataPassedListener {

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
        mAdapter = new CWWorkoutItemAdapter(this);
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
        databaseViewModel.getAllWorkoutItems().observe(this, new Observer<List<WorkoutItem>>() {
            @Override
            public void onChanged(List<WorkoutItem> workoutItems) {
                updateWorkoutItemsUI(workoutItems);
            }
        });

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

                databaseViewModel.insert(workout);

                onBackPressed();
            }
        });
    }

    private void showWorkoutItemPopup() {
        AddWorkoutItemDialogFragment dialogFragment = new AddWorkoutItemDialogFragment();
        dialogFragment.show(getSupportFragmentManager(), "add_workout_item_dialog");
    }

    private void updateWorkoutItemsUI(List<WorkoutItem> workoutItems) {
        LinearLayout llWorkoutItemsContainer = findViewById(R.id.llWorkoutItemsContainer);
        llWorkoutItemsContainer.removeAllViews();

        for (WorkoutItem workoutItem : workoutItems) {
            // Create a TextView for each workout item
            TextView textView = new TextView(this);
            textView.setText(workoutItem.getName());
            llWorkoutItemsContainer.addView(textView);
        }
    }

    @Override
    public void onDataPassed(WorkoutItem data) {
        mAdapter.mSelectedWorkoutItemList.add(data);
        mAdapter.notifyDataSetChanged();
    }
}
