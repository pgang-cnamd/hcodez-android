package com.hcodez.android.services.content;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.hcodez.android.services.content.handlers.ContactHandler;
import com.hcodez.android.services.content.handlers.MediaHandler;
import com.hcodez.android.services.content.handlers.UrlHandler;
import com.hcodez.codeengine.json.serialization.GsonUtil;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Nullable;

/**
 * The kind of content that can be handled
 */
public enum ContentType {
    URL,
    CONTACT,
    FILE,
    MEDIA;

    private static final String TAG = "ContentType";

    @Override
    public String toString() {
        switch (this) {
            case CONTACT:
                return "CONTACT";
            case URL:
                return "URL";
            case FILE:
                return "FILE";
            case MEDIA:
                return "MEDIA";
            default:
                return null;
        }
    }

    /**
     * Get the content opener for this content type
     * @return the content opener
     */
    @Nullable
    public ContentHandler getContentHandler(Context context,
                                            Uri uri) {
        Log.d(TAG, "getContentHandler() called");

        if (uri == null) {
            Log.e(TAG, "getContentHandler: null resource uri");
            return null;
        }

        switch (this) {
            case URL:
                return new UrlHandler(context, uri);
            case CONTACT:
                return new ContactHandler(context, uri);
            case MEDIA:
                return new MediaHandler(uri);
        }
        return null;
    }
}
