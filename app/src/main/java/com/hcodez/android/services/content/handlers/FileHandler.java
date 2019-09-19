package com.hcodez.android.services.content.handlers;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.hcodez.android.services.content.ContentHandler;

public class FileHandler implements ContentHandler {

    private static final String TAG = "FileHandler";

    private final Uri uri;

    public FileHandler(Uri uri) {
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
        return new Intent(Intent.ACTION_GET_CONTENT)
                .setType( "*/*")
                .addCategory(Intent.CATEGORY_OPENABLE);
    }
}
