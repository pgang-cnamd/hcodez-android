package com.hcodez.android.services.content;

import android.content.Intent;

/**
 * Interface for opening content
 */
public interface ContentHandler {
    /**
     * Get an intent for opening the content
     * @return the intent
     */
    Intent getOpenerIntent();

    /**
     * Get an intent used for creating the content
     * @return the intent
     */
    Intent getCreatorIntent();
}
