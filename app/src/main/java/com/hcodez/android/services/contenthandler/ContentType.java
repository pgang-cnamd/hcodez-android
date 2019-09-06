package com.hcodez.android.services.contenthandler;

import android.net.Uri;
import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.hcodez.codeengine.json.serialization.GsonUtil;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

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
                    loadSchemes();
                }
            }
        }
        return schemes.get(this);
    }

    /**
     * Load all schemes
     */
    private static void loadSchemes() {
        Log.d(TAG, "getSchemes() called");

        if (schemes == null) {
            synchronized (ContentType.class) {
                if (schemes == null) {
                    Log.d(TAG, "loadSchemes: loading schemes from resource file");
                    schemes = new HashMap<>();
                    InputStream inputStream = ContentType.class.getResourceAsStream("/res/raw/content_types.json");
                    if (inputStream == null) {
                        Log.e(TAG, "loadSchemes: failed to open resource file");
                        return;
                    }

                    Map<String, Set<String>> data;
                    Type typeToken = new TypeToken<Map<String, Set<String>>>(){}.getType();
                    data = GsonUtil.getGsonInstance().fromJson(new InputStreamReader(inputStream), typeToken);

                    if (data == null) {
                        Log.e(TAG, "loadSchemes: failed to extract content types");
                        return;
                    }

                    for (ContentType contentType : ContentType.values()) {
                        Set<String> set = data.getOrDefault(contentType.toString(), new HashSet<>());
                        if (set == null) {
                            Log.w(TAG, "loadSchemes: failed to load schemes for content type " + contentType.toString());
                            continue;
                        }
                        schemes.put(contentType, set);
                    }

                    Log.i(TAG, "getSchemes: loaded schemes");

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

        if (schemes == null) {
            loadSchemes();
        }

        Log.d(TAG, "get: checking scheme");
        for (ContentType contentType : schemes.keySet()) {
            for (String scheme : contentType.getSchemes()) {
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
}
