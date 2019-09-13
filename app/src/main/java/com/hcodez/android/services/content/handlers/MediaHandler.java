package com.hcodez.android.services.content.handlers;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.hcodez.android.services.content.ContentHandler;

public class MediaHandler implements ContentHandler {

    private static final String TAG = "MediaHandler";

    private final Uri uri;

    public MediaHandler(Uri uri) {
        Log.d(TAG, "MediaHandler() called with: uri = [" + uri + "]");
        this.uri = uri;
    }

    @Override
    public Intent getOpenerIntent() {
        Log.d(TAG, "getOpenerIntent() called");
        return null;
    }

    @Override
    public Intent getCreatorIntent() {
        Log.d(TAG, "getCreatorIntent() called");

        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");

        Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

        return chooserIntent;
    }
}
