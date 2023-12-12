package com.example.swimappuiframework.create;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;

import android.app.Dialog;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import android.widget.Button;
import android.widget.EditText;

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

public class CreateWorkoutItemDialogFragment extends DialogFragment {

    private DatabaseViewModel databaseViewModel;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_create_workout_item_dialog, null);

        final EditText editTextName = view.findViewById(R.id.editTextName);
        final EditText editTextCount = view.findViewById(R.id.editTextCount);
        final EditText editTextDistance = view.findViewById(R.id.editTextDistance);
        final EditText editTextPace = view.findViewById(R.id.editTextPace);
        final EditText editTextNotes = view.findViewById(R.id.editTextNotes);

        Button btnSave = view.findViewById(R.id.btnSave);
        Button btnClose = view.findViewById(R.id.btnClose);

        databaseViewModel = ((MyApp) requireActivity().getApplication()).getWorkoutItemViewModel();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Retrieve the entered data from the EditText fields
                String name = editTextName.getText().toString();
                int count = Integer.parseInt(editTextCount.getText().toString());
                int distance = Integer.parseInt(editTextDistance.getText().toString());
                String pace = editTextPace.getText().toString();
                String notes = editTextNotes.getText().toString();

                // Create a WorkoutItem object with the entered data
                WorkoutItem workoutItem = new WorkoutItem();
                workoutItem.setName(name);
                workoutItem.setCount(count);
                workoutItem.setDistance(distance);
                workoutItem.setPace(pace);
                workoutItem.setNotes(notes);

                databaseViewModel.insert(workoutItem);

                // Dismiss the dialog
                dismiss();
            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Dismiss the dialog
                dismiss();
            }
        });

        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(view);

        return dialog;
    }
}
