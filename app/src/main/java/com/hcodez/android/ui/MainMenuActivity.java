package com.hcodez.android.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.SearchView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hcodez.android.R;
import com.hcodez.android.ui.adapter.CodeAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainMenuActivity extends Activity implements CodeAdapter.OnNoteListener {

    private FloatingActionButton mAddCodeFloatingActionButton;
    private FloatingActionButton mFindCodeFloatingActionButton;
    private SearchView           mCodeSearchView;
    private RecyclerView         mCodeListRecyclerView;
    private CodeAdapter          mCodeAdapter;
    private ArrayList<String>    mCodeItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        mAddCodeFloatingActionButton  = findViewById(R.id.buttonAdd);
        mFindCodeFloatingActionButton = findViewById(R.id.buttonFind);
        mCodeSearchView               = findViewById(R.id.codeSearch);
        mCodeListRecyclerView         = findViewById(R.id.codeList);

        createAdapter();

        mAddCodeFloatingActionButton.setOnClickListener(view -> startActivity(new Intent(MainMenuActivity.this, CodeAddActivity.class)));

        mFindCodeFloatingActionButton.setOnClickListener(view -> startActivity(new Intent(MainMenuActivity.this, CodeFindActivity.class)));

        mCodeSearchView.setOnClickListener(view -> mCodeSearchView.setIconified(false));
    }

    /**
     * Method used for hiding the keyboard when touching outside the text
     */
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * Method for creating the adapter and adding an ArrayList
     */
    public void createAdapter(){
        mCodeAdapter = new CodeAdapter();
        mCodeListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mCodeListRecyclerView.setAdapter(mCodeAdapter);

        /**
         * List made as an simple demonstration
         */
        List<String> listExample = new ArrayList<>();
        listExample.add("code1");
        listExample.add("code2");
        listExample.add("code3");
        listExample.add("code4");

        mCodeItems = new ArrayList<>();
        mCodeItems.addAll(listExample);
        mCodeItems.addAll(listExample);
        mCodeItems.addAll(listExample);
        mCodeItems.addAll(listExample);
        mCodeItems.addAll(listExample);
        mCodeItems.addAll(listExample);
        mCodeItems.addAll(listExample);
        mCodeItems.addAll(listExample);

        mCodeAdapter.setItems(mCodeItems, this);
    }

    @Override
    public void onNoteClick(int position) {
        mCodeItems.get(position);
        Intent intent = new Intent(this, ContentRetrievalActivity.class);
        startActivity(intent);
    }
}
