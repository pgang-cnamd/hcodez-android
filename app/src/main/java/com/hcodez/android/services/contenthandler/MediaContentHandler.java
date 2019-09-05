package com.hcodez.android.services.contenthandler;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class MediaContentHandler extends ContentHandler {

    /**
     * Create a new ContentHandler instance
     *
     * @param uri the resource uri
     */
    MediaContentHandler(Uri uri) {
        super(uri);
    }

    @Override
    public Intent getOpenerIntent() {
        return null;
    }

    @Override
    public Intent getCreatorIntent(Context context) {
        return null;
    }
}
