package com.hcodez.android.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hcodez.android.AppExecutors;
import com.hcodez.android.DataRepository;
import com.hcodez.android.HcodezApp;
import com.hcodez.android.R;
import com.hcodez.android.db.entity.CodeEntity;
import com.hcodez.android.ui.adapter.CodeAdapter;
import com.hcodez.android.ui.fragment.CodeListFragment;
import com.hcodez.android.viewmodel.CodeListViewModel;
import com.hcodez.codeengine.model.Code;

import static org.checkerframework.checker.nullness.Opt.get;

public class MainMenuActivity extends AppCompatActivity {

    private FloatingActionButton mAddCodeFloatingActionButton;
    private FloatingActionButton mFindCodeFloatingActionButton;
    private SearchView           mCodeSearchView;
    private HcodezApp            app;
    private TextView             mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        mAddCodeFloatingActionButton = findViewById(R.id.buttonAdd);
        mFindCodeFloatingActionButton = findViewById(R.id.buttonFind);
        mCodeSearchView = findViewById(R.id.codeSearch);

        mAddCodeFloatingActionButton.setOnClickListener(view -> startActivity(new Intent(MainMenuActivity.this, CodeAddActivity.class)));

        mFindCodeFloatingActionButton.setOnClickListener(view -> startActivity(new Intent(MainMenuActivity.this, CodFindActivity.class)));

        mCodeSearchView.setOnClickListener(view -> mCodeSearchView.setIconified(false));

        app = new HcodezApp();

        final CodeListViewModel model;
        model = ViewModelProviders.of(this).get(CodeListViewModel.class);

        model.getCodes().observe(this, codeEntities -> {
            if (codeEntities != null) {
                if (codeEntities.size() != 0) {
                    mTextView.setText(codeEntities.get(0).toLibraryCode().toString());
                } else {
                    mTextView.setText("empty list");
                }
            } else {
                mTextView.setText("null code list");
            }
        });
    }
}