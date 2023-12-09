package com.example.swimappuiframework.create;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.swimappuiframework.MyApp;
import com.example.swimappuiframework.R;
import com.example.swimappuiframework.data.Pace;
import com.example.swimappuiframework.data.WorkoutItem;
import com.example.swimappuiframework.database.DatabaseViewModel;

public class CreatePaceActivity extends AppCompatActivity {

    private DatabaseViewModel databaseViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_pace);

        databaseViewModel = ((MyApp) getApplication()).getWorkoutItemViewModel();

        Button btnBack = findViewById(R.id.btnBack);
        Button btnSave = findViewById(R.id.btnSave);
        EditText editTextName = findViewById(R.id.editTextName);
        EditText editTextNotes = findViewById(R.id.editTextNotes);

        // Handle the back button click to return to the CreateActivity
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Retrieve the entered data from the EditText fields
                String name = editTextName.getText().toString();
                String notes = editTextNotes.getText().toString();

                // Create a WorkoutItem object with the entered data
                Pace pace = new Pace();
                pace.setName(name);
                pace.setNotes(notes);

                databaseViewModel.insert(pace);

                onBackPressed();
            }
        });

        // Add code to set up your pace creation components here
    }
}
