package com.example.swimappuiframework.create;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.swimappuiframework.R;
import com.example.swimappuiframework.data.WorkoutItem;

import java.util.ArrayList;
import java.util.List;

public class CWWorkoutItemAdapter extends
        RecyclerView.Adapter<CWWorkoutItemAdapter.CreateWorkoutViewHolder> {

    private final LayoutInflater mInflater;

    public List<WorkoutItem> mSelectedWorkoutItemList;

    public CWWorkoutItemAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        mSelectedWorkoutItemList = new ArrayList<>();
    }

    @NonNull
    @Override
    public CWWorkoutItemAdapter.CreateWorkoutViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.workoutitem_item, parent, false);

        return new CWWorkoutItemAdapter.CreateWorkoutViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull CWWorkoutItemAdapter.CreateWorkoutViewHolder holder, int position) {
        WorkoutItem current = mSelectedWorkoutItemList.get(position);

        holder.workoutItemView.setText(current.getName());
    }

    @Override
    public int getItemCount() {
        return mSelectedWorkoutItemList.size();
    }

    public class CreateWorkoutViewHolder extends RecyclerView.ViewHolder {
        public final TextView workoutItemView;  // Public final TextView variable
        public final CWWorkoutItemAdapter mAdapter;  // Final variable for WordListAdapter

        public CreateWorkoutViewHolder(View itemView, CWWorkoutItemAdapter adapter) {
            super(itemView);
            workoutItemView = itemView.findViewById(R.id.name);
            mAdapter = adapter;
        }
    }

}
