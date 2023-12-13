package com.example.swimappuiframework.create;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.swimappuiframework.MainActivity;
import com.example.swimappuiframework.MyApp;
import com.example.swimappuiframework.R;
import com.example.swimappuiframework.data.DataHandler;
import com.example.swimappuiframework.data.Pace;
import com.example.swimappuiframework.data.Workout;
import com.example.swimappuiframework.data.WorkoutItem;
import com.example.swimappuiframework.database.DatabaseViewModel;
import com.example.swimappuiframework.create.RecordActivity;
import com.example.swimappuiframework.workout.ActiveWorkoutActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CreatePaceActivity extends AppCompatActivity {

    private DatabaseViewModel databaseViewModel;

    private boolean hasData;

    private List<Double> xAverage;
    private List<Double> yAverage;
    private List<Double> zAverage;
    private List<Double> rollAverage;
    private List<Double> pitchAverage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_pace);

        databaseViewModel = ((MyApp) getApplication()).getWorkoutItemViewModel();

        // Retrieve arrays from the intent
        double[][] X = (double[][]) getIntent().getSerializableExtra("X");
        double[][] Y = (double[][]) getIntent().getSerializableExtra("Y");
        double[][] Z = (double[][]) getIntent().getSerializableExtra("Z");
        double[][] Roll = (double[][]) getIntent().getSerializableExtra("Roll");
        double[][] Pitch = (double[][]) getIntent().getSerializableExtra("Pitch");

        hasData = (X != null && Y != null && Z != null && Roll != null && Pitch != null);

        Button btnBack = findViewById(R.id.btnBack);
        Button btnSave = findViewById(R.id.btnSave);
        Button btnRecord = findViewById(R.id.btnRecord);
        EditText editTextName = findViewById(R.id.editTextName);
        EditText editTextNotes = findViewById(R.id.editTextNotes);

        btnRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Launch the History activity
                Intent intent = new Intent(CreatePaceActivity.this, RecordActivity.class);
                startActivity(intent);
            }
        });

        if (hasData) {
            DataHandler handler = new DataHandler();

            List<List<Double>> xVals = handler.arrayToList(X);
            List<List<Double>> yVals = handler.arrayToList(Y);
            List<List<Double>> zVals = handler.arrayToList(Z);
            List<List<Double>> rollVals = handler.arrayToList(Roll);
            List<List<Double>> pitchVals = handler.arrayToList(Pitch);

            List<List<Integer>> turns = new ArrayList<>();
            for (List<Double> swim : zVals) {
                turns.add(handler.identifyTurns(swim));
            }
            List<List<Integer>> strokes = new ArrayList<>();
            for (List<Double> swim : yVals) {
                strokes.add(handler.identifyStrokes(swim));
            }

            List<List<Double>> xStrokes = new ArrayList<>();
            List<List<Double>> yStrokes = new ArrayList<>();
            List<List<Double>> zStrokes = new ArrayList<>();
            List<List<Double>> rollStrokes = new ArrayList<>();
            List<List<Double>> pitchStrokes = new ArrayList<>();

            for (int i = 0; i < xVals.size(); i++) {
                List<List<Double>> xNorm = handler.normalizeStrokes(handler.parseStrokes(xVals.get(i), strokes.get(i), turns.get(i)));
                List<List<Double>> yNorm = handler.normalizeStrokes(handler.parseStrokes(yVals.get(i), strokes.get(i), turns.get(i)));
                List<List<Double>> zNorm = handler.normalizeStrokes(handler.parseStrokes(zVals.get(i), strokes.get(i), turns.get(i)));
                List<List<Double>> rollNorm = handler.normalizeStrokes(handler.parseStrokes(rollVals.get(i), strokes.get(i), turns.get(i)));
                List<List<Double>> pitchNorm = handler.normalizeStrokes(handler.parseStrokes(pitchVals.get(i), strokes.get(i), turns.get(i)));

                for (int j = 0; j < xNorm.size(); j++) {
                    xStrokes.add(xNorm.get(i));
                    yStrokes.add(yNorm.get(i));
                    zStrokes.add(zNorm.get(i));
                    rollStrokes.add(rollNorm.get(i));
                    pitchStrokes.add(pitchNorm.get(i));
                }
            }

            xAverage = handler.averageStrokes(xStrokes);
            yAverage = handler.averageStrokes(yStrokes);
            zAverage = handler.averageStrokes(zStrokes);
            rollAverage = handler.averageStrokes(rollStrokes);
            pitchAverage = handler.averageStrokes(pitchStrokes);
        }


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
                if (hasData) {
                    // Retrieve the entered data from the EditText fields
                    String name = editTextName.getText().toString();
                    String notes = editTextNotes.getText().toString();

                    // Create a WorkoutItem object with the entered data
                    Pace pace = new Pace();
                    pace.setName(name);
                    pace.setNotes(notes);
                    pace.setXPaceList(xAverage);
                    pace.setYPaceList(yAverage);
                    pace.setZPaceList(zAverage);
                    pace.setRollPaceList(rollAverage);
                    pace.setPitchPaceList(pitchAverage);

                    databaseViewModel.insert(pace);

                    Random random = new Random();
                    int randomNumber = random.nextInt(1000000);

                    pace.id = randomNumber;

                    // Retrieve the existing list from SharedPreferences
                    SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                    Gson gson = new Gson();
                    String json = sharedPreferences.getString("paceListKey", null);

                    List<Pace> paceList;

                    // If the list doesn't exist yet, create a new one
                    if (json == null) {
                        paceList = new ArrayList<>();
                    } else {
                        // Convert the JSON string back to a List<Workout>
                        Type type = new TypeToken<List<Pace>>() {}.getType();
                        paceList = gson.fromJson(json, type);
                    }

                    // Add the new Workout to the list
                    paceList.add(pace);

                    // Convert the updated list to JSON
                    String updatedJson = gson.toJson(paceList);

                    // Save the updated JSON string back to SharedPreferences
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("paceListKey", updatedJson);
                    editor.apply();

                    onBackPressed();
                }
            }
        });

        // Add code to set up your pace creation components here
    }
}
