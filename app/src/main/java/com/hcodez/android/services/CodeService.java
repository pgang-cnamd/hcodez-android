package com.hcodez.android.services;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.hcodez.android.HcodezApp;
import com.hcodez.android.db.AppDatabase;
import com.hcodez.android.db.entity.CodeEntity;
import com.hcodez.android.db.entity.ContentEntity;
import com.hcodez.codeengine.IdentifierGenerator;

/**
 * Service thad handles code operations
 */
public class CodeService {

    private static final String TAG = "CodeService";

    /**
     * The code service instance
     */
    private static CodeService sInstance;

    /**
     * The content service
     */
    private ContentService contentService;

    /**
     * The app database
     */
    private AppDatabase database;


    private CodeService(HcodezApp application) {
        contentService = ContentService.getInstance(application);
        database = application.getDatabase();
    }


    public static CodeService getInstance(HcodezApp application) {
        if (sInstance == null) {
            synchronized (CodeService.class) {
                if (sInstance == null) {
                    sInstance = new CodeService(application);
                    Log.d(TAG, "getInstance: created CodeService instance");
                }
            }
        }
        return sInstance;
    }


    /**
     * Generates a random identifier and checks it against the codes that already exist
     * @return the generated identifier
     */
    private String generateRandomIdentifier() {
        Log.d(TAG, "generateRandomIdentifier() called");
        String identifier = IdentifierGenerator.getInstance().generateIdentifier();

        for (CodeEntity codeEntity : database.codeDao().loadAllCodesSync()) {
            if (codeEntity.getIdentifier().equals(identifier))
                Log.d(TAG, "generateRandomIdentifier: found duplicate, running a recursion");
                return generateRandomIdentifier();
        }

        Log.d(TAG, "generateRandomIdentifier() returned: " + identifier);
        return identifier;
    }

    /**
     * Add a new code entity to the database
     * @param codeEntity the code entity to be added
     * @param contentEntity the new content entity to be added(id is needed for the code entity)
     * @return the newly added code entity(with the id assigned)
     */
    public LiveData<CodeEntity> addNewCode(final CodeEntity codeEntity,
                                              final ContentEntity contentEntity) {

        Log.d(TAG, "addNewCode() called with: codeEntity = [" + codeEntity + "], contentEntity = [" + contentEntity + "]");

        final MediatorLiveData<CodeEntity> liveData = new MediatorLiveData<>();

        new Thread(() -> {
            Log.d(TAG, "addNewCode: started thread");

            Log.d(TAG, "addNewCode: adding content");
            final LiveData<ContentEntity> newContentEntityData = contentService.addNewContent(contentEntity);

            final Observer<ContentEntity> observer = new Observer<ContentEntity>() {
                @Override
                public void onChanged(ContentEntity observedContentEntity) {
                    if (observedContentEntity == null) {
                        Log.d(TAG, "onChanged: null observed content entity, returning");
                        return;
                    }
                    Log.d(TAG, "onChanged: content entity not null");

                    codeEntity.setContentId(observedContentEntity.getId());
                    codeEntity.setIdentifier(generateRandomIdentifier());

                    long id = database.codeDao().insert(codeEntity);
                    final CodeEntity newCodeEntity = database.codeDao().loadCodeSync((int) id);
                    Log.d(TAG, "onChanged: code entity successfully saved in the database with id " + id);

                    final MutableLiveData<CodeEntity> data = new MutableLiveData<>();
                    liveData.addSource(data, liveData::postValue);
                    Log.d(TAG, "onChanged: added live data source");

                    data.postValue(newCodeEntity);
                    Log.d(TAG, "onChanged: posted data to livedata");

                    newContentEntityData.removeObserver(this);
                    Log.d(TAG, "onChanged: removed observer");
                }
            };

            Log.d(TAG, "addNewCode: attaching observer to content livedata");
            newContentEntityData.observeForever(observer);

        }).start();

        return liveData;
    }
}
