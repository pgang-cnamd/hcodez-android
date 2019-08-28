package com.hcodez.android.db.converter;

import androidx.room.TypeConverter;

import java.net.URI;
import java.net.URISyntaxException;

public class URIConverter {

    @TypeConverter
    public static URI toURI(String uri) {
        try {
            return uri == null ? null : new URI(uri);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }

    @TypeConverter
    public static String toString(URI uri) {
        return uri == null ? null : uri.toString();
    }
}
