package com.hcodez.android.db;

import androidx.annotation.NonNull;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

public final class Migrations {

    public static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE VIRTUAL TABLE IF NOT EXISTS `codeFts` USING FTS4(" +
                    "`identifier` TEXT, `owner` TEXT, `name` TEXT, content=`code`)");
            database.execSQL("INSERT INTO codeFts (`rowid`, `identifier`, `owner`, `name`) " +
                    "SELECT `id`, `identifier`, `owner`, `name` FROM code");
        }
    };
}