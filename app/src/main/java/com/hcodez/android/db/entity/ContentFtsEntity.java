package com.hcodez.android.db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Fts4;
import androidx.room.PrimaryKey;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@Entity(tableName = "contentFts")
@Fts4(contentEntity = ContentEntity.class)
public class ContentFtsEntity {

    @PrimaryKey
    @ColumnInfo(name = "rowid")
    private final int rowId;

    @ColumnInfo(name = "description")
    private final String description;

    public ContentFtsEntity(int rowId, String description) {
        this.rowId = rowId;
        this.description = description;
    }
}
