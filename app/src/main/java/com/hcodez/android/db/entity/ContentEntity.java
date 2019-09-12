package com.hcodez.android.db.entity;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.hcodez.android.services.content.ContactOpener;
import com.hcodez.android.services.content.ContentOpener;
import com.hcodez.android.services.content.ContentType;
import com.hcodez.android.services.content.IntentOpener;

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
    public ContentOpener getOpener(Context context) {
        Log.d(TAG, "getOpener() called");

        if (this.getResourceURI() == null) {
            Log.e(TAG, "getOpener: null resource uri");
            return null;
        }
        if (this.getContentType() == null) {
            Log.e(TAG, "getOpener: null content type");
            return null;
        }

        switch (this.getContentType()) {
            case URL:
                return new IntentOpener(null, this.getResourceURI());
            case CONTACT:
                return new ContactOpener(context, this.getResourceURI());
        }
        return null;
    }
}
