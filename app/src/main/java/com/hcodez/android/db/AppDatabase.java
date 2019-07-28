package com.hcodez.android.db;

import android.content.Context;

import com.hcodez.android.AppExecutors;
import com.hcodez.android.db.converter.CodeTypeConverter;
import com.hcodez.android.db.converter.InstantConverter;
import com.hcodez.android.db.converter.URLConverter;
import com.hcodez.android.db.dao.CodeDao;
import com.hcodez.android.db.entity.CodeEntity;
import com.hcodez.android.db.entity.CodeFtsEntity;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {CodeEntity.class, CodeFtsEntity.class}, version = 2)
@TypeConverters({InstantConverter.class, URLConverter.class, CodeTypeConverter.class})
public abstract class AppDatabase extends RoomDatabase {

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


    private final MutableLiveData<Boolean> mIsDatabaseCreated = new MutableLiveData<>();


    public static AppDatabase getInstance(final Context context, final AppExecutors executors) {
        if (appDatabaseInstance == null) {
            synchronized (AppDatabase.class) {
                if (appDatabaseInstance == null) {
                    appDatabaseInstance = buildDatabase(context.getApplicationContext(), executors);
                    appDatabaseInstance.updateDatabaseCreated(context.getApplicationContext());
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
        return Room.databaseBuilder(appContext, AppDatabase.class, DATABASE_NAME)
                .addCallback(new Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);
                        executors.diskIO().execute(() -> {
                            // Add a delay to simulate a long-running operation
                            addDelay();
                            // Generate the data for pre-population
                            AppDatabase database = AppDatabase.getInstance(appContext, executors);
                            List<CodeEntity> codes = DataGenerator.generateCodes();

                            insertData(database, codes);
                            // notify that the database was created and it's ready to be used
                            database.setDatabaseCreated();
                        });
                    }
                })
                .addMigrations(Migrations.MIGRATION_1_2)
                .build();
    }

    /**
     * Check whether the database already exists and expose it via {@link #getDatabaseCreated()}
     */
    private void updateDatabaseCreated(final Context context) {
        if (context.getDatabasePath(DATABASE_NAME).exists()) {
            setDatabaseCreated();
        }
    }

    private void setDatabaseCreated(){
        mIsDatabaseCreated.postValue(true);
    }

    private static void insertData(final AppDatabase database, final List<CodeEntity> codes) {
        database.runInTransaction(() -> {
            database.codeDao().insertAll(codes);
        });
    }

    private static void addDelay() {
        try {
            Thread.sleep(4000);
        } catch (InterruptedException ignored) {
        }
    }

    public LiveData<Boolean> getDatabaseCreated() {
        return mIsDatabaseCreated;
    }
}

