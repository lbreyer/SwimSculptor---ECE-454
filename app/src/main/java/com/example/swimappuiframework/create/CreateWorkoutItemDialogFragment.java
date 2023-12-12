package com.example.swimappuiframework.create;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;

import android.app.Dialog;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.swimappuiframework.MyApp;
import com.example.swimappuiframework.R;
import com.example.swimappuiframework.data.Pace;
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

    private Spinner spinner;
    private ArrayAdapter<Pace> spinnerAdapter;
    private List<Pace> paceList;
    private Pace selectedPace;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_create_workout_item_dialog, null);

        final EditText editTextName = view.findViewById(R.id.editTextName);
        final EditText editTextCount = view.findViewById(R.id.editTextCount);
        final EditText editTextDistance = view.findViewById(R.id.editTextDistance);
        final EditText editTextNotes = view.findViewById(R.id.editTextNotes);
        spinner = view.findViewById(R.id.spinner);

        Button btnSave = view.findViewById(R.id.btnSave);
        Button btnClose = view.findViewById(R.id.btnClose);

        databaseViewModel = ((MyApp) requireActivity().getApplication()).getWorkoutItemViewModel();

        List<Pace> paces = databaseViewModel.getAllPaces().getValue();
        if (paces == null || paces.isEmpty()) {
            paces = new ArrayList<>();
        }
        paces.add(new Pace("Test Pace 1", "Notes", new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));
        paces.add(new Pace("Test Pace 2", "Notes", new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));
        paces.add(new Pace("Test Pace 3", "Notes", new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));

        paceList = paces;
        ArrayAdapter<Pace> spinnerAdapter = new ArrayAdapter<Pace>(requireContext(), R.layout.list_item_pace, paceList) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                // Inflate your custom layout for the Spinner item
                if (convertView == null) {
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_pace, parent, false);
                }

                // Get the TextView from your custom layout
                TextView textViewItem = convertView.findViewById(R.id.spinner_item_text);

                // Set the text to display the name of the Pace object
                Pace pace = paceList.get(position);
                if (pace != null) {
                    textViewItem.setText(pace.getName());
                }

                return convertView;
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                // Use the same custom layout for dropdown view if needed
                return getView(position, convertView, parent);
            }
        };
        spinnerAdapter.setDropDownViewResource(R.layout.list_item_pace);
        spinner.setAdapter(spinnerAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                selectedPace = paceList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Do something if nothing is selected
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Retrieve the entered data from the EditText fields
                String name = editTextName.getText().toString();
                int count = Integer.parseInt(editTextCount.getText().toString());
                int distance = Integer.parseInt(editTextDistance.getText().toString());
                String notes = editTextNotes.getText().toString();

                // Create a WorkoutItem object with the entered data
                WorkoutItem workoutItem = new WorkoutItem();
                workoutItem.setName(name);
                workoutItem.setCount(count);
                workoutItem.setDistance(distance);
                workoutItem.setPace(selectedPace);
                workoutItem.setNotes(notes);

                databaseViewModel.insert(workoutItem);

                // Retrieve the existing list of workouts from SharedPreferences
                SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", MODE_PRIVATE);
                Gson gson = new Gson();
                String json = sharedPreferences.getString("workoutItemListKey", null);

                List<WorkoutItem> workoutItemList;

                // If the list doesn't exist yet, create a new one
                if (json == null) {
                    workoutItemList = new ArrayList<>();
                } else {
                    // Convert the JSON string back to a List<Workout>
                    Type type = new TypeToken<List<WorkoutItem>>() {}.getType();
                    workoutItemList = gson.fromJson(json, type);
                }

                // Add the new Workout to the list
                workoutItemList.add(workoutItem);

                // Convert the updated list to JSON
                String updatedJson = gson.toJson(workoutItemList);

                // Save the updated JSON string back to SharedPreferences
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("workoutItemListKey", updatedJson);
                editor.apply();

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
