package com.hcodez.android.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.hcodez.android.R;

public class EnterTextContentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_text_content);

        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
    }
}
