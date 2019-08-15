package com.hcodez.android.ui;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.TextView;

import com.hcodez.android.R;

public class ContentRetrievalActivity extends MainMenuActivity{

    private TextView  mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_retrieval_pop_up);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width  = dm.widthPixels;
        int height = dm.heightPixels;

        mTextView = findViewById(R.id.textView);
        mTextView.setText("<Code>");

        getWindow().setLayout((int) (width * .8), (int) (height * .4));
    }
}