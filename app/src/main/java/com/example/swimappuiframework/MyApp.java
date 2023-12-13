package com.example.swimappuiframework;

import android.app.Application;

import androidx.lifecycle.ViewModelProvider;

import com.example.swimappuiframework.data.Workout;
import com.example.swimappuiframework.database.DatabaseViewModel;

import java.util.ArrayList;
import java.util.List;

public class MyApp extends Application {

    private DatabaseViewModel databaseViewModel;
    @Override
    public void onCreate() {
        super.onCreate();
        databaseViewModel = new ViewModelProvider.AndroidViewModelFactory(this).create(DatabaseViewModel.class);
    }

    public DatabaseViewModel getWorkoutItemViewModel() {
        return databaseViewModel;
    }
}
