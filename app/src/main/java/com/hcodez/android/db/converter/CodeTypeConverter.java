package com.hcodez.android.db.converter;

import com.hcodez.codeengine.model.CodeType;

import androidx.room.TypeConverter;

public class CodeTypeConverter {

    @TypeConverter
    public static CodeType toCodeType(String codeType) {
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
        return codeType == null ? null : codeType.toString();
    }
}