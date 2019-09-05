package com.hcodez.android.db.converter;

import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;

import androidx.room.TypeConverter;

public class URLConverter {

    private static final String TAG = "URLConverter";

    @TypeConverter
    public static URL toURL(String url) {
        Log.d(TAG, "toURL() called with: url = [" + url + "]");
        try {
            return url == null ? null : new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @TypeConverter
    public static String toString(URL url) {
        Log.d(TAG, "toString() called with: url = [" + url + "]");
        return url == null ? null : url.toString();
    }
}
