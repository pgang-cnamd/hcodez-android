package com.hcodez.android.services;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.hcodez.android.services.contenthandlers.UrlContentHandler;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Abstract class that defines the behaviour of a content opener
 */
public abstract class ContentHandler {

    private static final String TAG = "ContentHandler";

    /**
     * The kind of content that can be handled
     */
    public enum ContentType {
        URL,
        CONTACT,
        IMAGE;

        private static final String TAG = "ContentType";

        /**
         * Map of content types and schemes
         */
        private static Map<ContentType, Set<String>> schemes;

        /**
         * Get the schemes that a content type has
         * @return the schemes
         */
        public Set<String> getSchemes() {
            Log.d(TAG, "getSchemes() called");

            if (schemes == null) {
                synchronized (ContentType.class) {
                    if (schemes == null) {
                        schemes = getAllSchemes();
                    }
                }
            }
            return schemes.get(this);
        }

        /**
         * Get all the schemes
         * @return the schemes
         */
        private static Map<ContentType, Set<String>> getAllSchemes() {
            Log.d(TAG, "getSchemes() called");

            if (schemes == null) {
                synchronized (ContentType.class) {
                    if (schemes == null) {
                        schemes = new HashMap<>();
                        schemes.put(ContentType.URL, new HashSet<>(Arrays.asList("http", "https")));
                        schemes.put(ContentType.CONTACT, new HashSet<>(Collections.singletonList("contact")));
                        schemes.put(ContentType.IMAGE, new HashSet<>(Collections.singletonList("file")));
                        Log.i(TAG, "getSchemes: created schemes");
                    }
                }
            }
            return schemes;
        }

        public static ContentType get(Uri uri) {
            Log.d(TAG, "get() called with: uri = [" + uri + "]");
            if (uri == null) {
                Log.w(TAG, "get: null uri");
                return null;
            }
            if (uri.getScheme() == null) {
                Log.w(TAG, "get: null scheme");
                return null;
            }

            Log.d(TAG, "get: calling getAllSchemes() in case the scheme map has not been initialized");
            getAllSchemes();

            Log.d(TAG, "get: cchecking scheme");
            for (ContentType contentType : schemes.keySet()) {
                for (String scheme : contentType.getSchemes()) {
                    if (uri.getScheme().equals(scheme)) {
                        return contentType;
                    }
                }
            }
            return null;
        }
    }

    /**
     * The Uri that needs to be opened
     */
    protected final Uri uri;


    /**
     * Create a new ContentHandler instance
     * @param uri the resource uri
     */
    protected ContentHandler(Uri uri) {
        Log.d(TAG, "ContentHandler() called with: uri = [" + uri + "]");
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
        ContentType contentType = ContentType.get(uri);
        if (contentType == null) {
            Log.d(TAG, "get: received null content type");
            return null;
        }
        switch (contentType) {
            case URL:
                return new UrlContentHandler(uri);
//            case IMAGE:
//                return null;
//            case CONTACT:
//                return null;
            default:
                return null;
        }
    }
}
