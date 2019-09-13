package com.hcodez.android.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.hcodez.android.R;
import com.hcodez.android.services.content.ContentHandler;
import com.hcodez.android.services.content.ContentType;

import java.util.ArrayList;

public class AddContentActivity extends MainMenuActivity {

    private static final String TAG = "AddContentActivity";

    public static final String INTENT_CONTENT_TYPE_KEY = "content_type";

    private ListView             mContentTypesListView;
    private ArrayAdapter<String> mContentTypesListAdapter;

    private static final String AWAITED_CONTENT_TYPE_KEY   = "awaited_content_type";
    private ContentType         awaitedContentType         = null;
    private boolean             canClearAwaitedContentType = false;

    private AdapterView.OnItemClickListener itemClickListener = (parent, view, position, id) -> {
        Log.d(TAG, "itemClickListener.onClick() called");

        canClearAwaitedContentType = false;
        awaitedContentType = ContentType.valueOf(mContentTypesListAdapter.getItem(position));
        ContentHandler handler = awaitedContentType.getContentHandler(getApplicationContext(), Uri.parse("https://example.com"));
        if (handler == null) {
            Log.e(TAG, "itemClickListener.onClick: can't create content handler");
            return;
        }
        startActivityForResult(handler.getCreatorIntent(), 0);
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate() called with: savedInstanceState = [" + savedInstanceState + "]");
        setContentView(R.layout.activity_add_content);

        mContentTypesListView = findViewById(R.id.add_content_content_types_list_view);
        ArrayList<String> mContentList = new ArrayList<>();
        for (ContentType contentType : ContentType.values()) {
            if (contentType == null) {
                Log.w(TAG, "onCreate: null content type, skipping");
                continue;
            }
            mContentList.add(contentType.toString());
        }

        mContentTypesListAdapter = new ArrayAdapter<>(this,
                R.layout.item_content_types_list,
                R.id.content_list_text_view,
                mContentList);

        mContentTypesListView.setAdapter(mContentTypesListAdapter);

        mContentTypesListView.setOnItemClickListener(itemClickListener);

        awaitedContentType = savedInstanceState != null ?
                savedInstanceState.getString(AWAITED_CONTENT_TYPE_KEY) != null ?
                        ContentType.valueOf(savedInstanceState.getString(AWAITED_CONTENT_TYPE_KEY))
                        : null
                : null;
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

        Log.d(TAG, "onActivityResult: data " + data.toString());

        final String resultUri;

        resultUri = data.getData() != null ? data.getData().toString() : null;

        Log.d(TAG, "onActivityResult: building intent");
        Intent contentData = new Intent();
        contentData.setData(resultUri != null ?
                Uri.parse(resultUri)
                : null);
        contentData.putExtra(INTENT_CONTENT_TYPE_KEY, awaitedContentType.toString());
        if (getParent() == null) {
            setResult(RESULT_OK, contentData);
        } else {
            getParent().setResult(RESULT_OK, contentData);
        }
        canClearAwaitedContentType = true;
        finish();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState() called with: outState = [" + outState + "]");
        if (canClearAwaitedContentType) {
            Log.d(TAG, "onSaveInstanceState: removing awaited content type");
            outState.remove(AWAITED_CONTENT_TYPE_KEY);
            return;
        }
        Log.d(TAG, "onSaveInstanceState: writing awaited content type");
        outState.putString(AWAITED_CONTENT_TYPE_KEY, awaitedContentType.toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d(TAG, "onRestoreInstanceState() called with: savedInstanceState = [" + savedInstanceState + "]");
        if (savedInstanceState != null) {
            Log.d(TAG, "onRestoreInstanceState: non null bundle");
                awaitedContentType = savedInstanceState.getString(AWAITED_CONTENT_TYPE_KEY) != null ?
                        ContentType.valueOf(savedInstanceState.getString(AWAITED_CONTENT_TYPE_KEY))
                        : null;
        }
    }
}
