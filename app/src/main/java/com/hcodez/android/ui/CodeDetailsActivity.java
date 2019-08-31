package com.hcodez.android.ui;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.TextView;

import com.hcodez.android.R;

public class CodeDetailsActivity extends MainMenuActivity{

    private TextView  mTextView;
    private String    mCodeItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_details);

        Bundle bundle = getIntent().getExtras();
        mCodeItem     = bundle != null ? bundle.getString("codeItem") : null;

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width  = dm.widthPixels;
        int height = dm.heightPixels;

        mTextView = findViewById(R.id.textView);
        mTextView.setText(mCodeItem != null ? mCodeItem : "empty");

        getWindow().setLayout((int) (width * .8), (int) (height * .4));
    }
}
