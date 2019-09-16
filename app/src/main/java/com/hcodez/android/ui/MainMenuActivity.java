package com.hcodez.android.ui;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hcodez.android.HcodezApp;
import com.hcodez.android.R;
import com.hcodez.android.ui.adapter.CodeAdapter;
import com.hcodez.android.ui.callback.CodeClickCallback;
import com.hcodez.android.ui.callback.CodeLongClickCallback;
import com.hcodez.android.viewmodel.CodeListViewModel;

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

        startActivity(CodeDetailsActivity
                .craftIntent(getApplicationContext(), codeEntity.getId(), codeEntity.getContentId()));
    };

    private CodeLongClickCallback codeLongClickCallback = codeEntity -> {
        Log.d(TAG, "codeLongClickCallback.onLongClick() called with: codeEntity = [" + codeEntity + "]");

        ClipboardManager clipboard = (ClipboardManager)
                getSystemService(Context.CLIPBOARD_SERVICE);

        ClipData clip = ClipData.newPlainText("code", codeEntity.toString());

        clipboard.setPrimaryClip(clip);
        Toast.makeText(getApplicationContext(), "Code copied to clipboard", Toast.LENGTH_SHORT).show();
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

        mCodeSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mCodeAdapter.getFilter().filter(newText);
                return false;
            }
        });

        mAddCodeFloatingActionButton.setOnClickListener(
                view -> startActivity(
                        new Intent(MainMenuActivity.this, AddCodeActivity.class)));

        mFindCodeFloatingActionButton.setOnClickListener(
                view -> startActivity(
                        new Intent(MainMenuActivity.this, FindCodeActivity.class)));

        mCodeSearchView.setOnClickListener(
                view -> {
                    mCodeSearchView.setIconified(false);
                });

        mCodeAdapter = new CodeAdapter(codeClickCallback, codeLongClickCallback);
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
     * If focus for window is changed, the code list will update
     * @param hasFocus Checking the window focus
     */
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if(!hasFocus) {
            mCodeSearchView.setQuery(null, false);
            mCodeSearchView.clearFocus();
            mCodeSearchView.onActionViewCollapsed();
        }
    }

    /**
     * Method used for hiding the keyboard when touching outside the text
     */
    public void hideKeyboard(View view) {
        Log.d(TAG, "hideKeyboard() called with: view = [" + view + "]");
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsMenuActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
