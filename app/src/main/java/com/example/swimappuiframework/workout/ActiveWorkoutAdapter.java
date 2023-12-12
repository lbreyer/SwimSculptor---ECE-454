package com.example.swimappuiframework.workout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.swimappuiframework.R;
import com.example.swimappuiframework.create.SelectedWorkoutItemFragment;
import com.example.swimappuiframework.data.WorkoutItem;

import java.util.ArrayList;
import java.util.List;

public class ActiveWorkoutAdapter extends
        RecyclerView.Adapter<ActiveWorkoutAdapter.CreateWorkoutViewHolder> {

    private final LayoutInflater mInflater;

    public List<WorkoutItem> mSelectedWorkoutItemList;

    private SelectedWorkoutItemFragment mFrag;

    private Context context;

    private FragmentManager mFragManager;

    public ActiveWorkoutAdapter(Context context, FragmentManager mFragManager) {
        mInflater = LayoutInflater.from(context);
        mSelectedWorkoutItemList = new ArrayList<>();
        this.mFragManager = mFragManager;
    }

    @NonNull
    @Override
    public ActiveWorkoutAdapter.CreateWorkoutViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.workoutitem_item, parent, false);

        return new ActiveWorkoutAdapter.CreateWorkoutViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ActiveWorkoutAdapter.CreateWorkoutViewHolder holder, int position) {
        WorkoutItem current = mSelectedWorkoutItemList.get(position);
        String text = current.getName() + "\n" + current.getCount() + "x" + current.getDistance() + "s";
        if(!current.getPace().equals("")){
            text = text + " (" + current.getPaceObject().getName() + ")";
        }
        if(!current.getNotes().equals("")){
            text = text + "\nNotes: " + current.getNotes();
        }

        holder.workoutItemView.setText(text);
    }

    @Override
    public int getItemCount() {
        return mSelectedWorkoutItemList.size();
    }

    public class CreateWorkoutViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView workoutItemView;  // Public final TextView variable
        public final ActiveWorkoutAdapter mAdapter;  // Final variable for WordListAdapter

        public CreateWorkoutViewHolder(View itemView, ActiveWorkoutAdapter adapter) {
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
            WorkoutItem element = mSelectedWorkoutItemList.get(mPosition);
            // Notify the adapter, that the data has changed so it can
            // update the RecyclerView to display the data.

            SelectedWorkoutItemFragment mFrag = SelectedWorkoutItemFragment.newInstance(element);
            mFrag.show(mFragManager, "fragment_selected_workout_item");
        }
    }

}
