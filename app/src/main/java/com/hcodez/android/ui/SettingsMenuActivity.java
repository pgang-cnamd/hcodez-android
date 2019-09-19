package com.hcodez.android.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.hcodez.android.R;

public class SettingsMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_menu);
    }

    @Override
    public void onPause()
    {
        super.onPause();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right_simple);
    }
}
