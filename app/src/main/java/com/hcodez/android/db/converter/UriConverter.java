package com.hcodez.android.db.converter;

import android.util.Log;

import androidx.room.TypeConverter;

import java.net.URI;
import java.net.URISyntaxException;

public class URIConverter {

    private static final String TAG = "URIConverter";

    @TypeConverter
    public static URI toURI(String uri) {
        Log.d(TAG, "toURI() called with: uri = [" + uri + "]");
        try {
            return uri == null ? null : new URI(uri);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }

    @TypeConverter
    public static String toString(URI uri) {
        Log.d(TAG, "toString() called with: uri = [" + uri + "]");
        return uri == null ? null : uri.toString();
    }
}
