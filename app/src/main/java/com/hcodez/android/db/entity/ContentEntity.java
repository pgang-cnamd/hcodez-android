package com.hcodez.android.db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.net.URI;

@Entity(tableName = "content")
public class ContentEntity {

    @PrimaryKey
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "description")
    private String description;

    @ColumnInfo(name = "resource_uri")
    private URI resourceURI;


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
}
