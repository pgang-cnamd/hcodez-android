package com.hcodez.android.services.content.handlers;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.hcodez.android.services.content.ContentHandler;
import com.hcodez.android.ui.EnterTextContentActivity;

public class UrlHandler implements ContentHandler {

    private static final String TAG = "UrlHandler";

    private final Context context;

    private final Uri uri;

    public UrlHandler(Context context, Uri uri) {
        Log.d(TAG, "UrlHandler() called with: context = [" + context + "], uri = [" + uri + "]");
        this.context = context;
        this.uri = uri;
    }

    @Override
    public Intent getOpenerIntent() {
        Log.d(TAG, "getOpenerIntent() called");
        return new Intent(Intent.ACTION_VIEW, uri);
    }

    @Override
    public Intent getCreatorIntent() {
        Log.d(TAG, "getCreatorIntent() called");
        return new Intent(context, EnterTextContentActivity.class);
    }
}
