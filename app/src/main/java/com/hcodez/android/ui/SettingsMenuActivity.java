package com.hcodez.android.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.hcodez.android.R;

public class SettingsMenuActivity extends AppCompatActivity {

    private Button mLogOutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_menu);

        mLogOutButton = findViewById(R.id.log_out_button);

        mLogOutButton.setOnClickListener(v -> {
//            Intent intent = new Intent(SettingsMenuActivity.this, LoginActivity.class);
//            startActivity(intent);
            finish();
        });
    }
}
