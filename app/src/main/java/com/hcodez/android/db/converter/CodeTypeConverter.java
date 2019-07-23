package com.hcodez.android.db.converter;

import com.hcodez.codeengine.model.CodeType;

import androidx.room.TypeConverter;

public class CodeTypeConverter {

    @TypeConverter
    public static CodeType toCodeType(String codeType) {
        return codeType == null ? null : CodeType.fromString(codeType);
    }

    @TypeConverter
    public static String toString(CodeType codeType) {
        return codeType == null ? null : codeType.toString();
    }
}
