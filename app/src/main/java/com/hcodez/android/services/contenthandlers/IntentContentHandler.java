package com.hcodez.android.services.contenthandlers;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.hcodez.android.services.ContentHandler;

import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class IntentContentHandler extends ContentHandler {

    private static final String TAG = "IntentContentHandler";

    /**
     * The action used by the intent
     */
    private final String intentAction;

    private final Bundle intentExtra;

    private final Set<String> intentCategories;

    private final Set<Integer> intentFlags;

    /**
     * Create a new ContentHandler instance
     *
     * @param uri the resource uri
     * @param intentAction action for the intent
     * @param intentExtra extra for the intent
     * @param intentCategories categories for the intent
     * @param intentFlags flags for the intent
     */
    protected IntentContentHandler(@Nonnull Uri uri,
                                   @Nonnull String intentAction,
                                   @Nullable Bundle intentExtra,
                                   @Nullable Set<String> intentCategories,
                                   @Nullable Set<Integer> intentFlags) {
        super(uri);
        this.intentAction = intentAction;
        this.intentExtra = intentExtra;
        this.intentCategories = intentCategories;
        this.intentFlags = intentFlags;
    }

    @Override
    public Intent getIntent() {
        Log.d(TAG, "getIntent() called");

        final Intent intent = new Intent(intentAction, uri);

        if (intentExtra != null) {
            Log.d(TAG, "getIntent: adding extras");
            intent.putExtras(intentExtra);
        }

        if (intentCategories != null) {
            if (!intentCategories.isEmpty()) {
                Log.d(TAG, "getIntent: adding intent categories");
                for (String intentCategory : intentCategories) {
                    intent.addCategory(intentCategory);
                }
            }
        }

        if (intentFlags != null) {
            if (!intentFlags.isEmpty()) {
                Log.d(TAG, "getIntent: adding intent flags");
                for (Integer intentCategory : intentFlags) {
                    if (intentCategory == null) {
                        Log.d(TAG, "getIntent: null flag, skipping");
                        continue;
                    }
                    intent.addFlags(intentCategory);
                }
            }
        }

        return intent;
    }
}
