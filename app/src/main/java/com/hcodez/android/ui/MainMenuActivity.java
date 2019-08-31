package com.hcodez.android.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hcodez.android.HcodezApp;
import com.hcodez.android.R;
import com.hcodez.android.ui.adapter.CodeAdapter;
import com.hcodez.android.ui.callback.CodeClickCallback;
import com.hcodez.android.viewmodel.CodeListViewModel;

import java.io.Serializable;

public class MainMenuActivity extends AppCompatActivity {

    private static final String TAG = "MainMenuActivity";

    private FloatingActionButton  mAddCodeFloatingActionButton;

    private FloatingActionButton  mFindCodeFloatingActionButton;

    private SearchView            mCodeSearchView;

    private RecyclerView          mCodeListRecyclerView;

    private CodeAdapter           mCodeAdapter;

    private HcodezApp             app;

    private CodeClickCallback codeClickCallback = codeEntity -> {
        Log.d(TAG, "codeClickCallback.onClick() called with: codeEntity = [" + codeEntity + "]");
        Intent intent = new Intent(getApplicationContext(), CodeDetailsActivity.class);

        Bundle bundle = new Bundle();
        bundle.putInt("code_id", codeEntity.getId());
        bundle.putInt("content_id", codeEntity.getContentId());

        intent.putExtras(bundle);

        startActivity(intent);
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate() called with: savedInstanceState = [" + savedInstanceState + "]");
        setContentView(R.layout.activity_main_menu);

        mAddCodeFloatingActionButton  = findViewById(R.id.buttonAdd);
        mFindCodeFloatingActionButton = findViewById(R.id.buttonFind);
        mCodeSearchView               = findViewById(R.id.codeSearch);
        mCodeListRecyclerView         = findViewById(R.id.codeList);
        app                           = new HcodezApp();

        mAddCodeFloatingActionButton.setOnClickListener(
                view -> startActivity(
                        new Intent(MainMenuActivity.this, AddCodeActivity.class)));

        mFindCodeFloatingActionButton.setOnClickListener(
                view -> startActivity(
                        new Intent(MainMenuActivity.this, FindCodeActivity.class)));

        mCodeSearchView.setOnClickListener(
                view -> mCodeSearchView.setIconified(false));

        mCodeAdapter = new CodeAdapter(codeClickCallback);
        mCodeListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mCodeListRecyclerView.setAdapter(mCodeAdapter);

        final CodeListViewModel model = ViewModelProviders.of(this).get(CodeListViewModel.class);

        model.getCodes().observe(this, codeEntities -> {
            Log.d(TAG, "onCreate: observed change in code list from view model");
            if (codeEntities != null) {
                if (codeEntities.size() != 0) {
                    mCodeAdapter.updateList(codeEntities);
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");

        ViewModelProviders.of(this)
                .get(CodeListViewModel.class)
                .getCodes()
                .observe(this,
                        codeEntities -> {
                    Log.d(TAG, "onResume: observed change in code list from view model");
                    mCodeAdapter.updateList(codeEntities);
                });
    }

    /**
     * Method used for hiding the keyboard when touching outside the text
     */
    public void hideKeyboard(View view) {
        Log.d(TAG, "hideKeyboard() called with: view = [" + view + "]");
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
