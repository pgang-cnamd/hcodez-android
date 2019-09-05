package com.hcodez.android;

import android.app.Application;
import android.util.Log;

import com.hcodez.android.db.AppDatabase;

public class HcodezApp extends Application {

    private static final String TAG = "HcodezApp";

    private AppExecutors mAppExecutors;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate() called");

        this.mAppExecutors = new AppExecutors();
    }

    public AppDatabase getDatabase() {
        Log.d(TAG, "getDatabase() called");
        return AppDatabase.getInstance(this, this.mAppExecutors);
    }

    public DataRepository getRepository() {
        Log.d(TAG, "getRepository() called");
        return DataRepository.getInstance(this.getDatabase());
    }
}
