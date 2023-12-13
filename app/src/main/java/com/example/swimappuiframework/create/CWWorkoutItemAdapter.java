package com.example.swimappuiframework.create;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.swimappuiframework.R;
import com.example.swimappuiframework.data.WorkoutItem;
import com.example.swimappuiframework.history.SelectedHistoryWorkoutItemFragment;

import java.util.ArrayList;
import java.util.List;

public class CWWorkoutItemAdapter extends
        RecyclerView.Adapter<CWWorkoutItemAdapter.CreateWorkoutViewHolder> {

    private final LayoutInflater mInflater;

    public List<WorkoutItem> mSelectedWorkoutItemList;

    private SelectedWorkoutItemFragment mFrag;

    private Context context;

    private FragmentManager mFragManager;
    private int mode;
    private List<String> itemSuggestions;

    public CWWorkoutItemAdapter(Context context, FragmentManager mFragManager, int mode) {
        mInflater = LayoutInflater.from(context);
        mSelectedWorkoutItemList = new ArrayList<>();
        this.mFragManager = mFragManager;
        this.mode = mode;
    }

    public CWWorkoutItemAdapter(Context context, FragmentManager mFragManager, int mode, List<String> itemSuggestions) {
        mInflater = LayoutInflater.from(context);
        mSelectedWorkoutItemList = new ArrayList<>();
        this.mFragManager = mFragManager;
        this.mode = mode;
        this.itemSuggestions = itemSuggestions;
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

    public class CreateWorkoutViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView workoutItemView;  // Public final TextView variable
        public final CWWorkoutItemAdapter mAdapter;  // Final variable for WordListAdapter

        public CreateWorkoutViewHolder(View itemView, CWWorkoutItemAdapter adapter) {
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

            if (mode == 0) {
                SelectedWorkoutItemFragment mFrag = SelectedWorkoutItemFragment.newInstance(element);
                mFrag.show(mFragManager, "fragment_selected_workout_item");
            }
            else if (mode == 1) {
                SelectedHistoryWorkoutItemFragment mFrag = SelectedHistoryWorkoutItemFragment.newInstance(element, "itemSuggestions.get(mPosition)");
                mFrag.show(mFragManager, "fragment_selected_history_workout_item");
            }
        }
    }

}
