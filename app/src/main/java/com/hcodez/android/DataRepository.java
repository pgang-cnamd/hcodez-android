package com.hcodez.android;

import com.hcodez.android.db.AppDatabase;
import com.hcodez.android.db.entity.CodeEntity;
import com.hcodez.android.db.entity.ContentEntity;
import com.hcodez.android.db.entity.ContentFtsEntity;

import java.util.Arrays;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

public class DataRepository {

    private static DataRepository sInstance;

    private final AppDatabase mDatabase;

    private MediatorLiveData<List<CodeEntity>> mObservableCodes;

    private MediatorLiveData<List<ContentEntity>> mObservableContent;

  
    public DataRepository(final AppDatabase database) {
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
        if (sInstance == null) {
            synchronized (DataRepository.class) {
                if (sInstance == null) {
                    sInstance = new DataRepository(database);
                }
            }
        }
        return sInstance;
    }

    /*
     * CodeEntity methods
     */
    public LiveData<List<CodeEntity>> getCodes() {
        return this.mObservableCodes;
    }

    public LiveData<CodeEntity> loadCode(final int codeId) {
        return this.mDatabase.codeDao().loadCode(codeId);
    }

    public void insertCode(final CodeEntity codeEntity) {
        this.mDatabase.codeDao().insert(codeEntity);
    }

    public void insertCodes(final List<CodeEntity> codeEntities) {
        this.mDatabase.codeDao().insertAll(codeEntities);
    }

    public void insertCodes(final CodeEntity... codeEntities) {
        this.mDatabase.codeDao().insertAll(Arrays.asList(codeEntities));
    }

    public void deleteCode(final CodeEntity codeEntity) {
        this.mDatabase.codeDao().delete(codeEntity);
    }

    public LiveData<List<CodeEntity>> searchCodes(String query) {
        return this.mDatabase.codeDao().searchAllCodes(query);
    }

    /*
     * ContentEntity methods
     */
    public LiveData<List<ContentEntity>> getContent() {
        return this.mObservableContent;
    }

    public LiveData<ContentEntity> loadCOntent(final int contentId) {
        return this.mDatabase.contentDao().loadContent(contentId);
    }

    public void insertContent(final ContentEntity contentEntity) {
        this.mDatabase.contentDao().insert(contentEntity);
    }

    public void insertContent(final List<ContentEntity> contentEntities) {
        this.mDatabase.contentDao().insertAll(contentEntities);
    }

    public void insertContent(final ContentEntity... contentEntities) {
        this.mDatabase.contentDao().insertAll(Arrays.asList(contentEntities));
    }

    public void deleteContent(final ContentEntity contentEntity) {
        this.mDatabase.contentDao().delete(contentEntity);
    }

    public LiveData<List<ContentEntity>> searchContentByDescription(String query) {
        return this.mDatabase.contentDao().searchAllContentByDescription(query);
    }
}
