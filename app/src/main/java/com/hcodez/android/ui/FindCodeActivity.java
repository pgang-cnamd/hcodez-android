package com.hcodez.android.ui;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.hcodez.android.R;

public class FindCodeActivity extends Activity {

    private static final String TAG = "FindCodeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate() called with: savedInstanceState = [" + savedInstanceState + "]");
        setContentView(R.layout.activity_find_code);

    }
}