package com.hcodez.android.db.entity;

import android.net.Uri;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.hcodez.android.services.contenthandler.ContentType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity(tableName = "content")
public class ContentEntity {

    @PrimaryKey
    @ColumnInfo(name = "id")
    private Integer id;

    @ColumnInfo(name = "description")
    private String description;

    @ColumnInfo(name = "resource_uri")
    private Uri resourceURI;

    @ColumnInfo(name = "content_type")
    private ContentType contentType;
}
