package com.hcodez.android.services.content;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class IntentOpener implements ContentOpener {

    private static final String TAG = "IntentOpener";

    private final String action;
    private final Uri    uri;

    public IntentOpener(@Nullable String action, @Nonnull Uri uri) {
        Log.d(TAG, "IntentOpener() called with: action = [" + action + "], uri = [" + uri + "]");
        if (action == null) {
            this.action = Intent.ACTION_DEFAULT;
        } else {
            this.action = action;
        }
        this.uri = uri;
    }

    @Override
    public Intent getOpenerIntent() {
        return new Intent(action, uri);
    }
}
