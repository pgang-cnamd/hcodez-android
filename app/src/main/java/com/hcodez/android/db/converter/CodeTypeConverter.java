package com.hcodez.android.db.converter;

import android.util.Log;

import com.hcodez.codeengine.model.CodeType;

import androidx.room.TypeConverter;

public class CodeTypeConverter {

    private static final String TAG = "CodeTypeConverter";

    @TypeConverter
    public static CodeType toCodeType(String codeType) {
        Log.d(TAG, "toCodeType() called with: codeType = [" + codeType + "]");
        if (codeType == null) {
            return null;
        }
        if (!CodeType.fromString(codeType).isPresent()) {
            return null;
        }
        return CodeType.fromString(codeType).get();
    }

    @TypeConverter
    public static String toString(CodeType codeType) {
        Log.d(TAG, "toString() called with: codeType = [" + codeType + "]");
        return codeType == null ? null : codeType.toString();
    }
}