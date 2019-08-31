package com.hcodez.android.services;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.hcodez.android.HcodezApp;
import com.hcodez.android.db.AppDatabase;
import com.hcodez.android.db.entity.ContentEntity;

/**
 * DatabaseService that handles content operations
 */
public class ContentService implements DatabaseService<ContentEntity> {

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
        Log.d(TAG, "ContentService() called with: application = [" + application + "]");
        database = application.getDatabase();
    }


    public static ContentService getInstance(HcodezApp application) {
        Log.d(TAG, "getInstance() called with: application = [" + application + "]");
        if (sInstance == null) {
            synchronized (ContentService.class) {
                if (sInstance == null) {
                    sInstance = new ContentService(application);
                    Log.i(TAG, "getInstance: created ContentEntity instance");
                }
            }
        }
        return sInstance;
    }

    @Override
    public LiveData<ContentEntity> addNew(ContentEntity entity) {
        Log.d(TAG, "addNew() called with: entity = [" + entity + "]");

        final MutableLiveData<ContentEntity> liveData = new MutableLiveData<>();

        new Thread(() -> {
            Log.d(TAG, "addNew: started thread");
            liveData.postValue(addNewSync(entity));
        }).start();

        return liveData;
    }

    @Override
    public ContentEntity addNewSync(ContentEntity entity) {
        Log.d(TAG, "addNewSync() called with: entity = [" + entity + "]");

        long id = database.contentDao().insert(entity);
        final ContentEntity contentEntity = database.contentDao().loadContentSync((int) id);
        Log.i(TAG, "addNewSync: content entity successfully saved in the database with id " + id);

        return contentEntity;
    }
}
