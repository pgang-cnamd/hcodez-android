package com.hcodez.android.services;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.hcodez.android.HcodezApp;
import com.hcodez.android.db.AppDatabase;
import com.hcodez.android.db.entity.ContentEntity;

/**
 * Service that handles content operations
 */
public class ContentService {

    private static final String TAG = "ContentService";

    /**
     * The content service instance
     */
    private static ContentService sInstance;

    /**
     * The app database
     */
    private AppDatabase database;


    private ContentService(HcodezApp application) {
        database = application.getDatabase();
    }


    public static ContentService getInstance(HcodezApp application) {
        if (sInstance == null) {
            synchronized (ContentService.class) {
                if (sInstance == null) {
                    sInstance = new ContentService(application);
                    Log.d(TAG, "getInstance: created ContentEntity instance");
                }
            }
        }
        return sInstance;
    }


    /**
     * Add a new content entity to the database
     * @param contentEntity the new content entity to be added
     * @return the newly added content entity(with the id assigned)
     */
    public LiveData<ContentEntity> addNewContent(final ContentEntity contentEntity) {
        Log.d(TAG, "addNewContent() called with: contentEntity = [" + contentEntity + "]");

        final MediatorLiveData<ContentEntity> liveData = new MediatorLiveData<>();

        new Thread(() -> {
            Log.d(TAG, "addNewContent: started thread");

            long id = database.contentDao().insert(contentEntity);
            final ContentEntity newContentEntity = database.contentDao().loadContentSync((int) id);
            Log.d(TAG, "addNewContent: content entity successfully saved in the database with id " + id);

            final MutableLiveData<ContentEntity> data = new MutableLiveData<>();
            liveData.addSource(data, liveData::postValue);
            Log.d(TAG, "addNewContent: added live data source");

            data.postValue(newContentEntity);
            Log.d(TAG, "addNewContent: posted data to livedata");
        }).start();

        return liveData;
    }
}
