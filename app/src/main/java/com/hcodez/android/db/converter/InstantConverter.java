package com.hcodez.android.db.converter;

import org.joda.time.Instant;

import androidx.room.TypeConverter;

public class InstantConverter {

    @TypeConverter
    public static Instant toInstant(Long timestamp) {
        return timestamp == null ? null : new Instant(timestamp);
    }

    @TypeConverter
    public static Long toTimestamp(Instant instant) {
        return instant == null ? null : instant.getMillis();
    }
}
