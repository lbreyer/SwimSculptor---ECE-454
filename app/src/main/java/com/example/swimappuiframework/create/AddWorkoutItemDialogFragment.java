package com.example.swimappuiframework.create;

import android.content.Context;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;

import android.app.Dialog;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.Button;

import com.example.swimappuiframework.MyApp;
import com.example.swimappuiframework.R;
import com.example.swimappuiframework.data.WorkoutItem;
import com.example.swimappuiframework.database.DatabaseViewModel;

import java.util.List;

public class AddWorkoutItemDialogFragment extends DialogFragment {

    private DatabaseViewModel databaseViewModel;

    private RecyclerView mRecyclerView;

    private OnDataPassedListener onDataPassedListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_add_workout_item_dialog, null);

        databaseViewModel = ((MyApp) requireActivity().getApplication()).getWorkoutItemViewModel();

        // Set up the recycler view in the popup
        mRecyclerView = view.findViewById(R.id.recyclerview);
       //  workoutItemViewModel.getAllWorkoutItems().;
        List<WorkoutItem> items =  databaseViewModel.getAllWorkoutItems().getValue();
        WorkoutItemAdapter adapter = new WorkoutItemAdapter(view.getContext(), items, this);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        databaseViewModel = ((MyApp) requireActivity().getApplication()).getWorkoutItemViewModel();

        Button btnClose = view.findViewById(R.id.btnClose);

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
            onDataPassedListener.onDataPassed(data);
            dismiss();
        }
    }

    public interface OnDataPassedListener {
        void onDataPassed(WorkoutItem data); // Define methods to pass data
    }
}