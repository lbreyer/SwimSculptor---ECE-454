package com.example.swimappuiframework.create;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.swimappuiframework.MyApp;
import com.example.swimappuiframework.R;
import com.example.swimappuiframework.data.WorkoutItem;
import com.example.swimappuiframework.database.DatabaseViewModel;

import java.util.List;

public class SelectedWorkoutItemFragment extends DialogFragment {

    private OnDataPassedListener onDataPassedListener;

    private static final String ARG_WORKOUT_ITEM = "workout_item";

    private WorkoutItem selectedItem;

    public static SelectedWorkoutItemFragment newInstance(WorkoutItem workoutItem) {
        SelectedWorkoutItemFragment fragment = new SelectedWorkoutItemFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_WORKOUT_ITEM, workoutItem);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_selected_workout_item, null);

        TextView nameTextView = view.findViewById(R.id.name_textview);
        TextView notesTextView = view.findViewById(R.id.notes_textview);
        TextView distanceTextView = view.findViewById(R.id.distance_textview);
        TextView countTextView = view.findViewById(R.id.count_textview);
        TextView paceTextView = view.findViewById(R.id.pace_textview);
        Button backButton = view.findViewById(R.id.back_button);
        Button removeButton = view.findViewById(R.id.remove_button);

        if (getArguments() != null) {
            selectedItem = (WorkoutItem) getArguments().getSerializable(ARG_WORKOUT_ITEM);

            nameTextView.setText(selectedItem.getName());
            notesTextView.setText("Notes: " + selectedItem.getNotes());
            distanceTextView.setText("Distance: " + selectedItem.getDistance() + "m");
            countTextView.setText("Count: " + selectedItem.getCount());
            paceTextView.setText("Pace: " + selectedItem.getPaceObject().getName());
        }

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Dismiss the dialog
                dismiss();
            }
        });

        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendDataToActivity(selectedItem);
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
        if (context instanceof OnDataPassedListener) {
            onDataPassedListener = (OnDataPassedListener) context;
        } else {
            throw new ClassCastException(context.toString() + " must implement OnDataPassedListener");
        }
    }

    public void sendDataToActivity(WorkoutItem data) {
        if (onDataPassedListener != null) {
            onDataPassedListener.onDataPassed(data, 1);
            dismiss();
        }
    }

    public interface OnDataPassedListener {
        void onDataPassed(WorkoutItem data, int mode); // Define methods to pass data
    }
}