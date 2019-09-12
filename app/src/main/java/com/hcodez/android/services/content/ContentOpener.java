package com.hcodez.android.services.content;

import android.content.Intent;

/**
 * Interface for opening content
 */
public interface ContentOpener {
    /**
     * Get an intent for opening the content
     * @return the intent
     */
    Intent getOpenerIntent();
}
