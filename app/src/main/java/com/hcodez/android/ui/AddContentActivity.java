package com.hcodez.android.ui;

import android.app.Activity;
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

        ArrayAdapter contentListAdapter = new ArrayAdapter(this, R.layout.activity_content_list_text_view, R.id.content_list_text_view, mContentList);

        mContentListView.setAdapter(contentListAdapter);

        mContentListView.setOnItemClickListener(itemClickListener);
    }

    private AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(AddContentActivity.this, EnterTextContentActivity.class);
            startActivityForResult(intent, 1);
            /**
             * Data returned to the first activity
             */
            Intent returnIntent = new Intent();
            setResult(Activity.RESULT_CANCELED, returnIntent);
            finish();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getStringExtra("result");
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                String result = null;
            }
        }
    }
}
