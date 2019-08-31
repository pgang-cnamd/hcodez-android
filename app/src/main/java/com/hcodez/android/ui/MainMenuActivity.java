package com.hcodez.android.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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

public class MainMenuActivity extends AppCompatActivity {


    private FloatingActionButton  mAddCodeFloatingActionButton;

    private FloatingActionButton  mFindCodeFloatingActionButton;

    private SearchView            mCodeSearchView;

    private RecyclerView          mCodeListRecyclerView;

    private CodeAdapter           mCodeAdapter;

    private HcodezApp             app;

    private CodeClickCallback codeClickCallback = codeEntity -> {
        Intent intent = new Intent(getApplicationContext(), RetrieveContentActivity.class);

        Bundle bundle = new Bundle();
        bundle.putString("codeItem", codeEntity.toString());
        intent.putExtras(bundle);

        startActivity(intent);
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        ViewModelProviders.of(this)
                .get(CodeListViewModel.class)
                .getCodes()
                .observe(this,
                        codeEntities -> mCodeAdapter.updateList(codeEntities));
    }

    /**
     * Method used for hiding the keyboard when touching outside the text
     */
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
