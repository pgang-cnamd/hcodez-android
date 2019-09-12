package com.hcodez.android.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.hcodez.android.R;
import com.hcodez.android.services.contenthandler.ContentType;
import com.hcodez.android.services.contenthandler.ContentTypeMetadata;

import java.util.ArrayList;

public class AddContentActivity extends MainMenuActivity {

    private static final String TAG = "AddContentActivity";

    public  static final String INTENT_STRING_URI_KEY           = "content_resource_uri";

    private ListView             mContentTypesListView;
    private ArrayAdapter<String> mContentTypesListAdapter;


    private AdapterView.OnItemClickListener itemClickListener = (parent, view, position, id) -> {
        Log.d(TAG, "itemClickListener.onClick() called");

        if (!(mContentTypesListView.getAdapter() instanceof ArrayAdapter)) {
            Log.e(TAG, "itemClickListener.onClick: can't get list adapter");
            return;
        }
        ContentTypeMetadata metadata = ContentType.valueOf(mContentTypesListAdapter.getItem(position)).getMetadata();
        startActivityForResult(metadata.buildCreatorIntent(getApplicationContext()), metadata.getCreator().getRequestCode());
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate() called with: savedInstanceState = [" + savedInstanceState + "]");
        setContentView(R.layout.activity_add_content);

        mContentTypesListView = findViewById(R.id.add_content_content_types_list_view);
        ArrayList<String> mContentList = new ArrayList<>();
        for (ContentType contentType : ContentType.values()) {
            mContentList.add(contentType.toString());
        }

        mContentTypesListAdapter = new ArrayAdapter<>(this,
                R.layout.item_content_types_list,
                R.id.content_list_text_view,
                mContentList);

        mContentTypesListView.setAdapter(mContentTypesListAdapter);

        mContentTypesListView.setOnItemClickListener(itemClickListener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult() called with: requestCode = [" + requestCode + "], resultCode = [" + resultCode + "], data = [" + data + "]");

        if (data == null) {
            Log.d(TAG, "onActivityResult: received null data");
            return;
        }

        if (resultCode != RESULT_OK) {
            Toast.makeText(this, "Could not get your input", Toast.LENGTH_LONG).show();
            return;
        }
        String result = data.getStringExtra(EnterTextContentActivity.INTENT_STRING_EXTRA_KEY);

        Intent contentData = new Intent();
        contentData.putExtra(INTENT_STRING_URI_KEY, result);
        if (getParent() == null) {
            setResult(RESULT_OK, contentData);
        } else {
            getParent().setResult(RESULT_OK, contentData);
        }
        finish();
    }
}
