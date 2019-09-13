package com.hcodez.android.db.entity;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.hcodez.android.services.content.handlers.ContactHandler;
import com.hcodez.android.services.content.ContentHandler;
import com.hcodez.android.services.content.ContentType;
import com.hcodez.android.services.content.handlers.MediaHandler;
import com.hcodez.android.services.content.handlers.UrlHandler;

import javax.annotation.Nullable;

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

    private static final String TAG = "ContentEntity";

    @PrimaryKey
    @ColumnInfo(name = "id")
    private Integer id;

    @ColumnInfo(name = "description")
    private String description;

    @ColumnInfo(name = "resource_uri")
    private Uri resourceURI;

    @ColumnInfo(name = "content_type")
    private ContentType contentType;

    /**
     * Get the content opener for this content type
     * @return the content opener
     */
    @Nullable
    public ContentHandler getContentHandler(Context context) {
        Log.d(TAG, "getContentHandler() called");

        if (this.getResourceURI() == null) {
            Log.e(TAG, "getContentHandler: null resource uri");
            return null;
        }
        if (this.getContentType() == null) {
            Log.e(TAG, "getContentHandler: null content type");
            return null;
        }

        switch (this.getContentType()) {
            case URL:
                return new UrlHandler(context, resourceURI);
            case CONTACT:
                return new ContactHandler(context, resourceURI);
            case MEDIA:
                return new MediaHandler(resourceURI);
        }
        return null;
    }
}
