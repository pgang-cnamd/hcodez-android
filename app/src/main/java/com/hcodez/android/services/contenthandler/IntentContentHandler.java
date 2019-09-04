package com.hcodez.android.services.contenthandler;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.hcodez.android.ui.EnterTextContentActivity;

import java.util.Collection;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class IntentContentHandler extends ContentHandler {

    private static final String TAG = "IntentContentHandler";

    /**
     * The action used by the intent
     */
    private final String intentAction;

    private final Bundle intentExtra;

    private final Collection<String> intentCategories;

    private final Collection<Integer> intentFlags;

    /**
     * Create a new ContentHandler instance
     *
     * @param uri the resource uri
     */
    protected IntentContentHandler(@Nonnull Uri uri) {
        super(uri);
        Log.d(TAG, "IntentContentHandler() called with: uri = [" + uri + "]");
        this.intentAction = Intent.ACTION_VIEW;
        this.intentExtra = null;
        this.intentCategories = null;
        this.intentFlags = null;
    }


    /**
     * Create a new ContentHandler instance
     *
     * @param uri the resource uri
     * @param intentAction action for the intent
     */
    protected IntentContentHandler(@Nonnull Uri uri,
                                   @Nonnull String intentAction) {
        super(uri);
        Log.d(TAG, "IntentContentHandler() called with: uri = [" + uri + "], intentAction = [" + intentAction + "]");
        this.intentAction = intentAction;
        this.intentExtra = null;
        this.intentCategories = null;
        this.intentFlags = null;
    }


    /**
     * Create a new ContentHandler instance
     *
     * @param uri the resource uri
     * @param intentAction action for the intent
     * @param intentExtra extra for the intent
     */
    protected IntentContentHandler(@Nonnull Uri uri,
                                   @Nonnull String intentAction,
                                   @Nullable Bundle intentExtra) {
        super(uri);
        Log.d(TAG, "IntentContentHandler() called with: uri = [" + uri + "], intentAction = [" + intentAction + "], intentExtra = [" + intentExtra + "]");
        this.intentAction = intentAction;
        this.intentExtra = intentExtra;
        this.intentCategories = null;
        this.intentFlags = null;
    }


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
                                   @Nullable Collection<String> intentCategories,
                                   @Nullable Collection<Integer> intentFlags) {
        super(uri);
        Log.d(TAG, "IntentContentHandler() called with: uri = [" + uri + "], intentAction = [" + intentAction + "], intentExtra = [" + intentExtra + "], intentCategories = [" + intentCategories + "], intentFlags = [" + intentFlags + "]");
        this.intentAction = intentAction;
        this.intentExtra = intentExtra;
        this.intentCategories = intentCategories;
        this.intentFlags = intentFlags;
    }


    @Override
    public Intent getOpenerIntent() {
        Log.d(TAG, "getOpenerIntent() called");

        final Intent intent = new Intent(intentAction, uri);

        if (intentExtra != null) {
            Log.d(TAG, "getOpenerIntent: adding extras");
            intent.putExtras(intentExtra);
        }

        if (intentCategories != null) {
            if (!intentCategories.isEmpty()) {
                Log.d(TAG, "getOpenerIntent: adding intent categories");
                for (String intentCategory : intentCategories) {
                    intent.addCategory(intentCategory);
                }
            }
        }

        if (intentFlags != null) {
            if (!intentFlags.isEmpty()) {
                Log.d(TAG, "getOpenerIntent: adding intent flags");
                for (Integer intentCategory : intentFlags) {
                    if (intentCategory == null) {
                        Log.d(TAG, "getOpenerIntent: null flag, skipping");
                        continue;
                    }
                    intent.addFlags(intentCategory);
                }
            }
        }

        return intent;
    }

    @Override
    public Intent getCreatorIntent(Context context) {
        Log.d(TAG, "getCreatorIntent() called with: context = [" + context + "]");
        return new Intent(context, EnterTextContentActivity.class);
    }
}
