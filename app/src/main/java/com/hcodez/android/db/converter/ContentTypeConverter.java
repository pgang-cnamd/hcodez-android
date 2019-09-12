package com.hcodez.android.db.converter;

import android.util.Log;

import androidx.room.TypeConverter;

import com.hcodez.android.services.contenthandler.ContentType;

public class ContentTypeConverter {

    private static final String TAG = "ContentTypeConverter";

    @TypeConverter
    public static ContentType toContentType(String contentType) {
        Log.d(TAG, "toContentType() called with: contentType = [" + contentType + "]");
        if (contentType == null) {
            return null;
        }
        return ContentType.valueOf(contentType);
    }

    @TypeConverter
    public static String toString(ContentType contentType) {
        Log.d(TAG, "toString() called with: contentType = [" + contentType + "]");
        return contentType == null ? null : contentType.toString();
    }
}
