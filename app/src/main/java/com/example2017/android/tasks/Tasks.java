package com.example2017.android.tasks;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by M7moud on 12-Nov-18.
 */
public class Tasks extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

    }
}
