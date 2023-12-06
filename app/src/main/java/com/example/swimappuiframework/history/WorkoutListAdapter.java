package com.example.swimappuiframework.history;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.swimappuiframework.R;
import com.example.swimappuiframework.data.WorkoutSummaryItem;

import java.util.List;

public class WorkoutListAdapter extends ArrayAdapter<WorkoutSummaryItem> {

    public WorkoutListAdapter(Context context, List<WorkoutSummaryItem> items) {
        super(context, 0, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        WorkoutSummaryItem item = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_workout, parent, false);
        }

        TextView textViewDate = convertView.findViewById(R.id.textViewDate);
        TextView textViewTotalTime = convertView.findViewById(R.id.textViewTotalTime);
        TextView textViewTotalYards = convertView.findViewById(R.id.textViewTotalYards);

        textViewDate.setText("Date: " + item.getDate());
        textViewTotalTime.setText("Total Time: " + item.getTotalTime());
        textViewTotalYards.setText("Total Yards: " + item.getTotalYards());

        return convertView;
    }
}
