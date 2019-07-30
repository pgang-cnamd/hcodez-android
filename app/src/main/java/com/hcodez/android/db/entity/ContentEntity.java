package com.hcodez.android.db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.net.URI;

@Entity(tableName = "content",
        foreignKeys = @ForeignKey(
                entity = CodeEntity.class,
                parentColumns = "id",
                childColumns = "code_id"))
public class ContentEntity {

    @PrimaryKey
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "description")
    private String description;

    @ColumnInfo(name = "resource_uri")
    private URI resourceURI;

    @ColumnInfo(name = "code_id")
    private int codeId;


    public ContentEntity() {}


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public URI getResourceURI() {
        return resourceURI;
    }

    public void setResourceURI(URI resourceURI) {
        this.resourceURI = resourceURI;
    }

    public int getCodeId() {
        return codeId;
    }

    public void setCodeId(int codeId) {
        this.codeId = codeId;
    }
}
