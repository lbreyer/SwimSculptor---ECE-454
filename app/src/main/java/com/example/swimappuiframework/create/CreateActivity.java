package com.example.swimappuiframework.create;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.example.swimappuiframework.R;

public class CreateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        Button btnBack = findViewById(R.id.btnBack);
        Button btnCreateWorkout = findViewById(R.id.btnCreateWorkout);
        Button btnCreateWorkoutItem = findViewById(R.id.btnCreateWorkoutItem);
        Button btnCreatePace = findViewById(R.id.btnCreatePace);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btnCreateWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Launch the CreateWorkoutActivity
                Intent intent = new Intent(CreateActivity.this, CreateWorkoutActivity.class);
                startActivity(intent);
            }
        });

        btnCreateWorkoutItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateWorkoutItemDialogFragment dialogFragment = new CreateWorkoutItemDialogFragment();
                dialogFragment.show(getSupportFragmentManager(), "create_workout_item_dialog");
            }
        });

        btnCreatePace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Launch the CreatePaceActivity
                Intent intent = new Intent(CreateActivity.this, CreatePaceActivity.class);
                startActivity(intent);
            }
        });
    }
}
