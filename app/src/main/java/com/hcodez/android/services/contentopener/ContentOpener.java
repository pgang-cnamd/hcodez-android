package com.hcodez.android.services.contentopener;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;

/**
 * Abstract class that defines the behaviour of a content opener
 */
public abstract class ContentOpener {

    private static final String TAG = "ContentOpener";

    /**
     * The Uri that needs to be opened
     */
    protected final Uri uri;

//    /**
//     * The context required for starting the intent
//     */
//    protected final Context context;

    /**
     * Create a new ContentOpener instance
     * @param uri the resource uri
     */
    protected ContentOpener(Uri uri) {
        Log.d(TAG, "ContentOpener() called with: uri = [" + uri + "]");
        this.uri = uri;
    }

    /**
     * Open the Uri of this content opener
     */
    public abstract Intent getIntent();

    /**
     * Get the appropriate content opener for an Uri
     * @param uri the uri
     * @return the content opener
     */
    public static ContentOpener get(Uri uri) {
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
        switch (uri.getScheme()) {
            case "https":
            case "http":
                return new UrlContentOpener(uri);
            default:
                return null;
        }
    }
}
