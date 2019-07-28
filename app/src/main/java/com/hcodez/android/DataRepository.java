package com.hcodez.android;

import com.hcodez.android.db.AppDatabase;
import com.hcodez.android.db.entity.CodeEntity;

import java.util.Arrays;
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

    public LiveData<CodeEntity> loadCode(final int codeId) {
        return this.mDatabase.codeDao().loadCode(codeId);
    }

    public void insertCode(final CodeEntity codeEntity) {
        mDatabase.codeDao().insert(codeEntity);
    }

    public void insertCodes(final List<CodeEntity> codeEntities) {
        mDatabase.codeDao().insertAll(codeEntities);
    }

    public void insertCodes(final CodeEntity... codeEntities) {
        mDatabase.codeDao().insertAll(Arrays.asList(codeEntities));
    }

    public void deleteCode(final CodeEntity codeEntity) {
        mDatabase.codeDao().delete(codeEntity);
    }

    public LiveData<List<CodeEntity>> searchCodes(String query) {
        return mDatabase.codeDao().searchAllProducts(query);
    }
}
