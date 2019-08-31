package com.hcodez.android.services.contentopener;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;

public class UrlContentOpener extends ContentOpener {

    private static final String TAG = "UrlContentOpener";

    protected UrlContentOpener(Uri uri) {
        super(uri);
    }

    @Override
    public Intent getIntent() {
        Log.d(TAG, "open() called");

        return new Intent(Intent.ACTION_VIEW, uri);
    }
}
