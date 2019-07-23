package com.hcodez.android;

import com.hcodez.android.db.AppDatabase;
import com.hcodez.android.db.entity.CodeEntity;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

public class DataRepository {

    private static DataRepository sInstance;

    private final AppDatabase mDatabase;

    private MediatorLiveData<List<CodeEntity>> mObservableCodes;


    private DataRepository(final AppDatabase database) {
        this.mDatabase = database;
        this.mObservableCodes = new MediatorLiveData<>();

        this.mObservableCodes.addSource(this.mDatabase.codeDao().loadAllCodes(), codeEntities -> {
            if (this.mDatabase.getDatabaseCreated().getValue() != null) {
                this.mObservableCodes.postValue(codeEntities);
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


    public LiveData<List<CodeEntity>> getCodes() {
        return this.mObservableCodes;
    }

    public LiveData<CodeEntity> getCodeById(final int codeId) {
        return this.mDatabase.codeDao().loadCode(codeId);
    }
}
