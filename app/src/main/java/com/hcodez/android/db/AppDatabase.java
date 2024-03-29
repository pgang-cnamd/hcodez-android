package com.hcodez.android.db;

import android.content.Context;
import android.util.Log;

import com.hcodez.android.AppExecutors;
import com.hcodez.android.db.converter.CodeTypeConverter;
import com.hcodez.android.db.converter.ContentTypeConverter;
import com.hcodez.android.db.converter.InstantConverter;
import com.hcodez.android.db.converter.UriConverter;
import com.hcodez.android.db.converter.URLConverter;
import com.hcodez.android.db.dao.CodeDao;
import com.hcodez.android.db.dao.ContentDao;
import com.hcodez.android.db.entity.CodeEntity;
import com.hcodez.android.db.entity.CodeFtsEntity;
import com.hcodez.android.db.entity.ContentEntity;
import com.hcodez.android.db.entity.ContentFtsEntity;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {
            CodeEntity.class,
            CodeFtsEntity.class,
            ContentEntity.class,
            ContentFtsEntity.class},
        version = 4)
@TypeConverters({InstantConverter.class,
        URLConverter.class,
        CodeTypeConverter.class,
        UriConverter.class,
        ContentTypeConverter.class})
public abstract class AppDatabase extends RoomDatabase {

    private static final String TAG = "AppDatabase";

    /**
     * Static database instance.
     */
    private static AppDatabase appDatabaseInstance;

    /**
     * Database name
     */
    public static final String DATABASE_NAME = "hcodez-local-db";

    /**
     * Dao methods
     */
    public abstract CodeDao codeDao();
    public abstract ContentDao contentDao();


    private final MutableLiveData<Boolean> mIsDatabaseCreated = new MutableLiveData<>();


    public static AppDatabase getInstance(final Context context, final AppExecutors executors) {
        Log.d(TAG, "getInstance() called with: context = [" + context + "], executors = [" + executors + "]");
        if (appDatabaseInstance == null) {
            synchronized (AppDatabase.class) {
                if (appDatabaseInstance == null) {
                    appDatabaseInstance = buildDatabase(context.getApplicationContext(), executors);
                    appDatabaseInstance.updateDatabaseCreated(context.getApplicationContext());
                    Log.i(TAG, "getInstance: created AppDatabase instance");
                }
            }
        }
        return appDatabaseInstance;
    }

    /**
     * Build the database. {@link Builder#build()} only sets up the database configuration and
     * creates a new instance of the database.
     * The SQLite database is only created when it's accessed for the first time.
     */
    private static AppDatabase buildDatabase(final Context appContext,
                                             final AppExecutors executors) {
        Log.d(TAG, "buildDatabase() called with: appContext = [" + appContext + "], executors = [" + executors + "]");
        return Room.databaseBuilder(appContext, AppDatabase.class, DATABASE_NAME)
                .addCallback(new Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);
                        executors.diskIO().execute(() -> {
                            /*Add a delay to simulate a long-running operation*/
                            addDelay();

                            AppDatabase database = AppDatabase.getInstance(appContext, executors);

                            /*Generate the data for pre-population*/
                            final List<CodeEntity> codes = DataGenerator.generateCodes();
                            final List<ContentEntity> content = DataGenerator.generateContent();
//                            insertData(database, codes, content);

                            /*notify that the database was created and it's ready to be used*/
                            database.setDatabaseCreated();
                        });
                    }
                })
                .addMigrations(Migrations.MIGRATION_1_2,
                        Migrations.MIGRATION_2_3,
                        Migrations.MIGRATION_3_4)
                .build();
    }

    /**
     * Check whether the database already exists and expose it via {@link #getDatabaseCreated()}
     */
    private void updateDatabaseCreated(final Context context) {
        Log.d(TAG, "updateDatabaseCreated() called with: context = [" + context + "]");
        if (context.getDatabasePath(DATABASE_NAME).exists()) {
            setDatabaseCreated();
        }
    }

    private void setDatabaseCreated() {
        Log.d(TAG, "setDatabaseCreated() called");
        mIsDatabaseCreated.postValue(true);
    }

    private static void insertData(final AppDatabase database,
                                   final List<CodeEntity> codes,
                                   final List<ContentEntity> content) {
        Log.d(TAG, "insertData() called with: database = [" + database + "], codes = [" + codes + "], content = [" + content + "]");
        database.runInTransaction(() -> {
            database.codeDao().insertAll(codes);
            database.contentDao().insertAll(content);
        });
    }

    private static void addDelay() {
        Log.d(TAG, "addDelay() called");
        try {
            Thread.sleep(4000);
        } catch (InterruptedException ignored) {
        }
    }

    public LiveData<Boolean> getDatabaseCreated() {
        Log.d(TAG, "getDatabaseCreated() called");
        return mIsDatabaseCreated;
    }
}

