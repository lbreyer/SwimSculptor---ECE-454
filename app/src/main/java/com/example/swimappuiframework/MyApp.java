package com.example.swimappuiframework;

import android.app.Application;

import androidx.lifecycle.ViewModelProvider;

import com.example.swimappuiframework.database.DatabaseViewModel;

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
