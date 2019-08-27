package com.hcodez.android.ui;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProviders;

import com.hcodez.android.R;
import com.hcodez.android.ui.adapter.CodeAdapter;
import com.hcodez.android.viewmodel.CodeListViewModel;

import java.util.ArrayList;

public class ContentRetrievalActivity extends MainMenuActivity{

    private TextView  mTextView;
    private String    mCodeItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_retrieval_pop_up);

        Bundle bundle = getIntent().getExtras();
        mCodeItem     = bundle.getString("codeItem");

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width  = dm.widthPixels;
        int height = dm.heightPixels;

        mTextView = findViewById(R.id.textView);
        mTextView.setText(mCodeItem);

        getWindow().setLayout((int) (width * .8), (int) (height * .4));
    }
}
