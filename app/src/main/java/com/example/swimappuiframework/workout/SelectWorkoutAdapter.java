package com.example.swimappuiframework.workout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.swimappuiframework.R;
import com.example.swimappuiframework.data.Workout;
import com.example.swimappuiframework.data.WorkoutSummaryItem;

import java.util.List;

public class SelectWorkoutAdapter extends ArrayAdapter<Workout> {
    public SelectWorkoutAdapter(Context context, List<Workout> items) {
        super(context, 0, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Workout item = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_select_workout, parent, false);
        }

        TextView textViewName = convertView.findViewById(R.id.textViewName);
        TextView textViewTotalYards = convertView.findViewById(R.id.textViewTotalYards);

        textViewName.setText(item.getName());
        textViewTotalYards.setText("Total Distance: " + item.getTotalDistance() + " yards");

        return convertView;
    }
}

