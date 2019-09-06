package com.hcodez.android.db.converter;

import android.util.Log;

import org.joda.time.Instant;

import androidx.room.TypeConverter;

public class InstantConverter {

    private static final String TAG = "InstantConverter";

    @TypeConverter
    public static Instant toInstant(Long timestamp) {
        Log.d(TAG, "toInstant() called with: timestamp = [" + timestamp + "]");
        return timestamp == null ? null : new Instant(timestamp);
    }

    @TypeConverter
    public static Long toTimestamp(Instant instant) {
        Log.d(TAG, "toTimestamp() called with: instant = [" + instant + "]");
        return instant == null ? null : instant.getMillis();
    }
}
