package com.hcodez.android.db.converter;

import android.net.Uri;
import android.util.Log;

import androidx.room.TypeConverter;

public class UriConverter {

    private static final String TAG = "UriConverter";

    @TypeConverter
    public static Uri toURI(String uri) {
        Log.d(TAG, "toURI() called with: uri = [" + uri + "]");
        return uri == null ? null : Uri.parse(uri);
    }

    @TypeConverter
    public static String toString(Uri uri) {
        Log.d(TAG, "toString() called with: uri = [" + uri + "]");
        return uri == null ? null : uri.toString();
    }
}
