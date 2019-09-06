package com.hcodez.android;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import androidx.annotation.NonNull;

public class AppExecutors {

    private static final String TAG = "AppExecutors";

    private final Executor mDiskIO;

    private final Executor mNetworkIO;

    private final Executor mMainThread;

    private AppExecutors(Executor diskIO, Executor networkIO, Executor mainThread) {
        Log.d(TAG, "AppExecutors() called with: diskIO = [" + diskIO + "], networkIO = [" + networkIO + "], mainThread = [" + mainThread + "]");
        this.mDiskIO = diskIO;
        this.mNetworkIO = networkIO;
        this.mMainThread = mainThread;
    }

    public AppExecutors() {
        this(Executors.newSingleThreadExecutor(), Executors.newFixedThreadPool(3),
                new MainThreadExecutor());
        Log.d(TAG, "AppExecutors() called");
    }

    public Executor diskIO() {
        Log.d(TAG, "diskIO() called");
        return mDiskIO;
    }

    public Executor networkIO() {
        Log.d(TAG, "networkIO() called");
        return mNetworkIO;
    }

    public Executor mainThread() {
        Log.d(TAG, "mainThread() called");
        return mMainThread;
    }

    private static class MainThreadExecutor implements Executor {

        private static final String TAG = "AppExecutorsMainThreadExecutor";

        private Handler mainThreadHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable command) {
            Log.d(TAG, "execute() called with: command = [" + command + "]");
            mainThreadHandler.post(command);
        }
    }
}
