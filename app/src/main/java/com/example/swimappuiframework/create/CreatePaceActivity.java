package com.example.swimappuiframework.create;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.swimappuiframework.MyApp;
import com.example.swimappuiframework.R;
import com.example.swimappuiframework.data.DataHandler;
import com.example.swimappuiframework.data.Pace;
import com.example.swimappuiframework.data.WorkoutItem;
import com.example.swimappuiframework.database.DatabaseViewModel;

import java.util.ArrayList;
import java.util.List;

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
        EditText editTextName = findViewById(R.id.editTextName);
        EditText editTextNotes = findViewById(R.id.editTextNotes);

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

                    onBackPressed();
                }
            }
        });

        // Add code to set up your pace creation components here
    }
}
