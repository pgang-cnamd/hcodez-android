package com.hcodez.android.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.hcodez.android.R;

import java.util.ArrayList;

public class AddContentActivity extends MainMenuActivity {

    private ListView mContentListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_content);

        mContentListView = (ListView) findViewById(R.id.content_list_view);

        ArrayList<String> mContentList = new ArrayList<>();

        mContentList.add("content");

        ArrayAdapter contentListAdapter = new ArrayAdapter(this, R.layout.item_code_list, mContentList);

        mContentListView.setAdapter(contentListAdapter);

        mContentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(AddContentActivity.this, EnterTextContentActivity.class));
            }
        });
    }
}
