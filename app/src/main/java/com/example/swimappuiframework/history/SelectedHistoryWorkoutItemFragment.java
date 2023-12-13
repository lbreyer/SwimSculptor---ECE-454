package com.example.swimappuiframework.history;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.swimappuiframework.R;
import com.example.swimappuiframework.data.WorkoutItem;

import java.util.List;

public class SelectedHistoryWorkoutItemFragment extends DialogFragment {

    private static final String ARG_WORKOUT_ITEM = "workout_item";
    private static final String ARG_ITEM_SUGGESTIONS = "item_suggestions";

    private WorkoutItem selectedItem;

    private String itemSuggestions;

    public static SelectedHistoryWorkoutItemFragment newInstance(WorkoutItem workoutItem, String itemSuggestions) {
        SelectedHistoryWorkoutItemFragment fragment = new SelectedHistoryWorkoutItemFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_WORKOUT_ITEM, workoutItem);
        args.putSerializable(ARG_ITEM_SUGGESTIONS, itemSuggestions);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_selected_history_workout_item, null);

        TextView nameTextView = view.findViewById(R.id.name_textview);
        TextView notesTextView = view.findViewById(R.id.notes_textview);
        TextView distanceTextView = view.findViewById(R.id.distance_textview);
        TextView countTextView = view.findViewById(R.id.count_textview);
        TextView paceTextView = view.findViewById(R.id.pace_textview);
        TextView suggTextView = view.findViewById(R.id.textViewSugg);
        Button backButton = view.findViewById(R.id.back_button);

        if (getArguments() != null) {
            selectedItem = (WorkoutItem) getArguments().getSerializable(ARG_WORKOUT_ITEM);
            itemSuggestions = (String) getArguments().getSerializable(ARG_ITEM_SUGGESTIONS);

            nameTextView.setText(selectedItem.getName());
            notesTextView.setText("Notes: " + selectedItem.getNotes());
            distanceTextView.setText("Distance: " + selectedItem.getDistance() + "m");
            countTextView.setText("Count: " + selectedItem.getCount());
            paceTextView.setText("Pace: " + selectedItem.getPaceObject().getName());
            suggTextView.setText(itemSuggestions);
        }

        backButton.setOnClickListener(new View.OnClickListener() {
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

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }
}