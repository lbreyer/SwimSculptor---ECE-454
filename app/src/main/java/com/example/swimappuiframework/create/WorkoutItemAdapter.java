package com.example.swimappuiframework.create;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.swimappuiframework.R;
import com.example.swimappuiframework.data.WorkoutItem;

import java.util.LinkedList;
import java.util.List;

public class WorkoutItemAdapter extends
        RecyclerView.Adapter<WorkoutItemAdapter.WorkoutViewHolder> {
    private final List<WorkoutItem> mWorkoutItemList;
    private final LayoutInflater mInflater;

    private AddWorkoutItemDialogFragment mFrag;

    public WorkoutItemAdapter(Context context, List<WorkoutItem> workoutItemList, AddWorkoutItemDialogFragment frag) {
        mInflater = LayoutInflater.from(context);
        mWorkoutItemList = workoutItemList;
        mFrag = frag;
    }

    @NonNull
    @Override
    public WorkoutItemAdapter.WorkoutViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.workoutitem_item, parent, false);

        return new WorkoutViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkoutItemAdapter.WorkoutViewHolder holder, int position) {
        WorkoutItem current = mWorkoutItemList.get(position);

        holder.workoutItemView.setText(current.getName());
    }

    @Override
    public int getItemCount() {
        return mWorkoutItemList.size();
    }

    public class WorkoutViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView workoutItemView;  // Public final TextView variable
        public final WorkoutItemAdapter mAdapter;  // Final variable for WordListAdapter

        public WorkoutViewHolder(View itemView, WorkoutItemAdapter adapter) {
            super(itemView);
            workoutItemView = itemView.findViewById(R.id.name);
            mAdapter = adapter;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            // Get the position of the item that was clicked.
            int mPosition = getLayoutPosition();
            // Use that to access the affected item in mWordList.
            WorkoutItem element = mWorkoutItemList.get(mPosition);
            // Notify the adapter, that the data has changed so it can
            // update the RecyclerView to display the data.
            if (mFrag != null) {
                mFrag.sendDataToActivity(element);
            }
        }
    }
}
