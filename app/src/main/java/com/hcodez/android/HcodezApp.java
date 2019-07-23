package com.hcodez.android;

import android.app.Application;

import com.hcodez.android.db.AppDatabase;

public class HcodezApp extends Application {

    private AppExecutors mAppExecutors;

    @Override
    public void onCreate() {
        super.onCreate();

        this.mAppExecutors = new AppExecutors();
    }

    public AppDatabase getDatabase() {
        return AppDatabase.getInstance(this, this.mAppExecutors);
    }

    public DataRepository getRepository() {
        return DataRepository.getInstance(this.getDatabase());
    }
}
