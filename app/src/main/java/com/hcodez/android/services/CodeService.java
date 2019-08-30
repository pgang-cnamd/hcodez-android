package com.hcodez.android.services;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.hcodez.android.HcodezApp;
import com.hcodez.android.db.AppDatabase;
import com.hcodez.android.db.entity.CodeEntity;
import com.hcodez.android.db.entity.ContentEntity;
import com.hcodez.codeengine.IdentifierGenerator;

/**
 * DatabaseService thad handles code operations
 */
public class CodeService implements DatabaseService<CodeEntity> {

    private static final String TAG = "CodeService";

    /**
     * The code service instance
     */
    private static CodeService sInstance;

    /**
     * The content service
     */
    private DatabaseService<ContentEntity> contentService;

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
        Log.d(TAG, "generateRandomIdentifier: generated " + identifier);

        for (CodeEntity codeEntity : database.codeDao().loadAllCodesSync()) {
            Log.d(TAG, "generateRandomIdentifier: comparing " + identifier + " to " + codeEntity.getIdentifier());
            if (identifier.compareTo(codeEntity.getIdentifier()) == 0) {
                Log.d(TAG, "generateRandomIdentifier: found duplicate, running a recursion");
                return generateRandomIdentifier();
            }
        }

        Log.d(TAG, "generateRandomIdentifier() returned: " + identifier);
        return identifier;
    }

    @Override
    public LiveData<CodeEntity> addNew(final CodeEntity entity) {
        Log.d(TAG, "addNew() called with: entity = [" + entity + "]");

        final MutableLiveData<CodeEntity> liveData = new MutableLiveData<>();

        new Thread(() -> {
            Log.d(TAG, "addNew: started thread");
            try {
                liveData.postValue(addNewSync(entity));
            } catch (IllegalArgumentException e) {
                Log.e(TAG, "addNew: " + e.getMessage());
                liveData.postValue(CodeEntity.builder().build());
            }
        }).start();

        return liveData;
    }

    @Override
    public CodeEntity addNewSync(final CodeEntity entity) throws IllegalArgumentException {
        Log.d(TAG, "addNewSync() called with: entity = [" + entity + "]");

        if (entity.getContentId() == null) {
            throw new IllegalArgumentException(entity.toString() + " does not have a content id");
        }

        entity.setIdentifier(generateRandomIdentifier());

        long id = database.codeDao().insert(entity);
        final CodeEntity codeEntity = database.codeDao().loadCodeSync((int) id);
        Log.d(TAG, "addNewSync: code entity successfully saved in the database with id " + id);

        return codeEntity;
    }

    public LiveData<CodeEntity> addNew(final CodeEntity entity,
                                       final ContentEntity content) {
        Log.d(TAG, "addNew() called with: entity = [" + entity + "], content = [" + content + "]");

        final MutableLiveData<CodeEntity> liveData = new MutableLiveData<>();

        new Thread(() -> {
            Log.d(TAG, "addNew: started thread");
            liveData.postValue(addNewSync(entity, content));
        }).start();

        return liveData;
    }

    public CodeEntity addNewSync(final CodeEntity entity,
                                 final ContentEntity content) {
        Log.d(TAG, "addNewSync() called with: entity = [" + entity + "], content = [" + content + "]");
        final ContentEntity contentEntity = contentService.addNewSync(content);

        entity.setContentId(contentEntity.getId());

        return addNewSync(entity);
    }
}
