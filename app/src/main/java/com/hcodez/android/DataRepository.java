package com.hcodez.android;

import android.util.Log;

import com.hcodez.android.db.AppDatabase;
import com.hcodez.android.db.entity.CodeEntity;
import com.hcodez.android.db.entity.ContentEntity;

import java.util.Arrays;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

public class DataRepository {

    private static final String TAG = "DataRepository";

    private static DataRepository sInstance;

    private final AppDatabase mDatabase;

    private MediatorLiveData<List<CodeEntity>> mObservableCodes;

    private MediatorLiveData<List<ContentEntity>> mObservableContent;

  
    public DataRepository(final AppDatabase database) {
        Log.d(TAG, "DataRepository() called with: database = [" + database + "]");
        this.mDatabase = database;
        this.mObservableCodes = new MediatorLiveData<>();
        this.mObservableContent = new MediatorLiveData<>();

        this.mObservableCodes.addSource(this.mDatabase.codeDao().loadAllCodes(), codeEntities -> {
            if (this.mDatabase.getDatabaseCreated().getValue() != null) {
                this.mObservableCodes.postValue(codeEntities);
            }
        });
        this.mObservableContent.addSource(this.mDatabase.contentDao().loadAllContent(), contentEntities -> {
            if (this.mDatabase.getDatabaseCreated().getValue() != null) {
                this.mObservableContent.postValue(contentEntities);
            }
        });
    }


    public static DataRepository getInstance(final AppDatabase database) {
        Log.d(TAG, "getInstance() called with: database = [" + database + "]");
        if (sInstance == null) {
            synchronized (DataRepository.class) {
                if (sInstance == null) {
                    sInstance = new DataRepository(database);
                    Log.i(TAG, "getInstance: created DataRepository instance");
                }
            }
        }
        return sInstance;
    }

    /*
     * CodeEntity methods
     */
    public LiveData<List<CodeEntity>> getCodes() {
        Log.d(TAG, "getCodes() called");
        return this.mObservableCodes;
    }

    public LiveData<CodeEntity> loadCode(final int codeId) {
        Log.d(TAG, "loadCode() called with: codeId = [" + codeId + "]");
        return this.mDatabase.codeDao().loadCode(codeId);
    }

    public long insertCode(final CodeEntity codeEntity) {
        Log.d(TAG, "insertCode() called with: codeEntity = [" + codeEntity + "]");
        return this.mDatabase.codeDao().insert(codeEntity);
    }

    public List<Long> insertCodes(final List<CodeEntity> codeEntities) {
        Log.d(TAG, "insertCodes() called with: codeEntities = [" + codeEntities + "]");
        return this.mDatabase.codeDao().insertAll(codeEntities);
    }

    public List<Long> insertCodes(final CodeEntity... codeEntities) {
        Log.d(TAG, "insertCodes() called with: codeEntities = [" + codeEntities + "]");
        return this.mDatabase.codeDao().insertAll(Arrays.asList(codeEntities));
    }

    public void deleteCode(final CodeEntity codeEntity) {
        Log.d(TAG, "deleteCode() called with: codeEntity = [" + codeEntity + "]");
        this.mDatabase.codeDao().delete(codeEntity);
    }

    public LiveData<List<CodeEntity>> searchCodes(String query) {
        Log.d(TAG, "searchCodes() called with: query = [" + query + "]");
        return this.mDatabase.codeDao().searchAllCodes(query);
    }

    /*
     * ContentEntity methods
     */
    public LiveData<List<ContentEntity>> getContent() {
        Log.d(TAG, "getContent() called");
        return this.mObservableContent;
    }

    public LiveData<ContentEntity> loadContent(final int contentId) {
        Log.d(TAG, "loadContent() called with: contentId = [" + contentId + "]");
        return this.mDatabase.contentDao().loadContent(contentId);
    }

    public long insertContent(final ContentEntity contentEntity) {
        Log.d(TAG, "insertContent() called with: contentEntity = [" + contentEntity + "]");
        return this.mDatabase.contentDao().insert(contentEntity);
    }

    public List<Long> insertContent(final List<ContentEntity> contentEntities) {
        Log.d(TAG, "insertContent() called with: contentEntities = [" + contentEntities + "]");
        return this.mDatabase.contentDao().insertAll(contentEntities);
    }

    public List<Long> insertContent(final ContentEntity... contentEntities) {
        Log.d(TAG, "insertContent() called with: contentEntities = [" + Arrays.toString(contentEntities) + "]");
        return this.mDatabase.contentDao().insertAll(Arrays.asList(contentEntities));
    }

    public void deleteContent(final ContentEntity contentEntity) {
        Log.d(TAG, "deleteContent() called with: contentEntity = [" + contentEntity + "]");
        this.mDatabase.contentDao().delete(contentEntity);
    }

    public LiveData<List<ContentEntity>> searchContentByDescription(String query) {
        Log.d(TAG, "searchContentByDescription() called with: query = [" + query + "]");
        return this.mDatabase.contentDao().searchAllContentByDescription(query);
    }
}
