package com.hcodez.android.db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Fts4;
import androidx.room.PrimaryKey;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@Entity(tableName = "codeFts")
@Fts4(contentEntity = CodeEntity.class)
public class CodeFtsEntity {

    @PrimaryKey
    @ColumnInfo(name = "rowid")
    private final int rowId;

    @ColumnInfo(name = "identifier")
    private final String identifier;

    @ColumnInfo(name = "owner")
    private final String owner;

    @ColumnInfo(name = "name")
    private final String name;

    public CodeFtsEntity(int rowId, String identifier, String owner, String name) {
        this.rowId = rowId;
        this.identifier = identifier;
        this.owner = owner;
        this.name = name;
    }
}
