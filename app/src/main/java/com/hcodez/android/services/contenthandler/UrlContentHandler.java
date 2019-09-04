package com.hcodez.android.services.contenthandler;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

public class UrlContentHandler extends ContentHandler {

    private static final String TAG = "UrlContentHandler";

    /**
     * Create a new ContentHandler instance
     *
     * @param uri the resource uri
     */
    public UrlContentHandler(Uri uri) {
        super(uri);
        Log.d(TAG, "UrlContentHandler() called with: uri = [" + uri + "]");
    }

    @Override
    public Intent getOpenerIntent() {
        Log.d(TAG, "open() called");

        return new IntentContentHandler(uri).getOpenerIntent();
    }

    @Override
    public Intent getCreatorIntent(Context context) {
        Log.d(TAG, "getCreatorIntent() called with: context = [" + context + "]");
        return new IntentContentHandler(uri).getCreatorIntent(context);
    }
}
