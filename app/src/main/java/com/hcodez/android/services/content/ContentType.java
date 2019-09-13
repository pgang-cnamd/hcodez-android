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

    /**
     * Map of content types and metadata
     */
    private static Map<ContentType, ContentTypeMetadata> metadata;

    /**
     * Get the metadata that a content type has
     * @return the metadata
     */
    public ContentTypeMetadata getMetadata() {
        Log.d(TAG, "getMetadata() called");

        if (metadata == null) {
            synchronized (ContentType.class) {
                if (metadata == null) {
                    loadMetadata();
                }
            }
        }
        return metadata.get(this);
    }

    /**
     * Load all metadata
     */
    private static void loadMetadata() {
        Log.d(TAG, "getMetadata() called");

        if (metadata == null) {
            synchronized (ContentType.class) {
                if (metadata == null) {
                    Log.d(TAG, "loadMetadata: loading metadata from resource file");
                    metadata = new HashMap<>();
                    InputStream inputStream = ContentType.class.getResourceAsStream("/res/raw/content_types.json");
                    if (inputStream == null) {
                        Log.e(TAG, "loadMetadata: failed to open resource file");
                        return;
                    }

                    Map<String, ContentTypeMetadata> data;
                    Type typeToken = new TypeToken<Map<String, ContentTypeMetadata>>(){}.getType();
                    data = GsonUtil.getGsonInstance().fromJson(new InputStreamReader(inputStream), typeToken);

                    if (data == null) {
                        Log.e(TAG, "loadMetadata: failed to extract content type metadata");
                        return;
                    }

                    for (ContentType contentType : ContentType.values()) {
                        ContentTypeMetadata metadata = data.getOrDefault(contentType.toString(), null);
                        if (metadata == null) {
                            Log.w(TAG, "loadMetadata: failed to load metadata for content type " + contentType.toString());
                            continue;
                        }
                        ContentType.metadata.put(contentType, metadata);
                    }
                    Log.i(TAG, "getMetadata: loaded metadata");
                }
            }
        }
    }

    /**
     * Get the ContentType of a given Uri
     * @param uri an Uri
     * @return the ContentType of the Uri
     */
    public static Optional<ContentType> get(Uri uri) {
        Log.d(TAG, "get() called with: uri = [" + uri + "]");
        if (uri == null) {
            Log.w(TAG, "get: null uri");
            return Optional.empty();
        }
        if (uri.getScheme() == null) {
            Log.w(TAG, "get: null scheme");
            return Optional.empty();
        }

        if (metadata == null) {
            loadMetadata();
        }

        Log.d(TAG, "get: checking scheme");
        for (ContentType contentType : metadata.keySet()) {
            if (contentType.getMetadata().getSchemes() == null) {
                Log.w(TAG, "get: no schemes, cannot return content type");
                return Optional.empty();
            }
            for (String scheme : contentType.getMetadata().getSchemes()) {
                if (uri.getScheme().equals(scheme)) {
                    return Optional.of(contentType);
                }
            }
        }
        return Optional.empty();
    }

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
