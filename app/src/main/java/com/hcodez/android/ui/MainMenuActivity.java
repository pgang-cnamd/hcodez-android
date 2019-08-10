package com.hcodez.android.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.SearchView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hcodez.android.R;

public class MainMenuActivity extends Activity {

    private FloatingActionButton mAddCodeFloatingActionButton;
    private FloatingActionButton mFindCodeFloatingActionButton;
    private SearchView           mCodeSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        mAddCodeFloatingActionButton  = findViewById(R.id.buttonAdd);
        mFindCodeFloatingActionButton = findViewById(R.id.buttonFind);
        mCodeSearchView               = findViewById(R.id.codeSearch);

        mAddCodeFloatingActionButton.setOnClickListener(view -> startActivity(new Intent(MainMenuActivity.this, CodeAddActivity.class)));

        mFindCodeFloatingActionButton.setOnClickListener(view -> startActivity(new Intent(MainMenuActivity.this, CodFindActivity.class)));

        mCodeSearchView.setOnClickListener(view -> mCodeSearchView.setIconified(false));
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}