package com.hcodez.android.services.contenthandler;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

/**
 * Abstract class that defines the behaviour of a content handler
 */
public abstract class ContentHandler {

    private static final String TAG = "ContentHandler";

    /**
     * The Uri that needs to be opened
     */
    protected final Uri uri;


    /**
     * Create a new ContentHandler instance
     * @param uri the resource uri
     */
    ContentHandler(Uri uri) {
        Log.d(TAG, "ContentHandler() called with: uri = [" + uri + "]");
        this.uri = uri;
    }


    /**
     * Get the intent used to open this content
     * @return the intent
     */
    public abstract Intent getOpenerIntent();

    /**
     * Get the intent used to open a window for creating this type of content
     * @param context the application context
     * @return the intent
     */
    public abstract Intent getCreatorIntent(Context context);


    /**
     * Get the appropriate content opener for an Uri
     * @param uri the uri
     * @return the content opener
     */
    public static ContentHandler get(Uri uri) {
        Log.d(TAG, "get() called with: uri = [" + uri + "]");
        if (uri == null) {
            Log.d(TAG, "get: null uri");
            return null;
        }
        if (uri.getScheme() == null) {
            Log.d(TAG, "get: null uri scheme");
            return null;
        }
        Log.d(TAG, "get: processing uri scheme");
        ContentType contentType = ContentType.get(uri).orElse(null);
        if (contentType == null) {
            Log.d(TAG, "get: received null content type");
            return null;
        }
        switch (contentType) {
            case URL:
                return new UrlContentHandler(uri);
            case MEDIA:
            case FILE:
            case CONTACT:
            default:
                return new IntentContentHandler(uri);
        }
    }
}
