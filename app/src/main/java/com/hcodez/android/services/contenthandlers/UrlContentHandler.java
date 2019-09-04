package com.hcodez.android.services.contenthandlers;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.hcodez.android.services.ContentHandler;

public class UrlContentHandler extends ContentHandler {

    private static final String TAG = "UrlContentHandler";

    /**
     * Create a new ContentHandler instance
     *
     * @param uri the resource uri
     */
    public UrlContentHandler(Uri uri) {
        super(uri);
    }

    @Override
    public Intent getIntent() {
        Log.d(TAG, "open() called");

        return new IntentContentHandler(uri).getIntent();
    }
}
