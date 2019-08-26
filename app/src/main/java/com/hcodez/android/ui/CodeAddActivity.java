package com.hcodez.android.ui;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.TextView;

import com.hcodez.android.R;

public class CodeAddActivity extends MainMenuActivity{

    private TextView mCodeName;
    private TextView mUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_add_pop_up);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width  = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * .8), (int) (height * .6));

        mCodeName = findViewById(R.id.codeName);
        mUrl = findViewById(R.id.url);

    }
}