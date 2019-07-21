package com.hcodez.android.db.converter;

import java.net.MalformedURLException;
import java.net.URL;

import androidx.room.TypeConverter;

public class URLConverter {

    @TypeConverter
    public static URL toURL(String url) {
        try {
            return url == null ? null : new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @TypeConverter
    public static String toString(URL url) {
        return url == null ? null : url.toString();
    }
}
