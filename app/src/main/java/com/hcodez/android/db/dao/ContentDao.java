package com.hcodez.android.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.hcodez.android.db.entity.ContentEntity;

import java.util.List;

@Dao
public interface ContentDao {
    @Query("SELECT * FROM content")
    LiveData<List<ContentEntity>> loadAllContent();

    @Query("SELECT * FROM content")
    List<ContentEntity> loadAllContentSync();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ContentEntity content);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<ContentEntity> content);

    @Query("SELECT * FROM content WHERE id = :contentId")
    LiveData<ContentEntity> loadContent(int contentId);

    @Query("SELECT * FROM content WHERE id = :contentId")
    ContentEntity loadContentSync(int contentId);

    @Query("SELECT content.* FROM content JOIN contentFts ON (content.id = contentFts.rowid) "
            + "WHERE contentFts MATCH :query")
    LiveData<List<ContentEntity>> searchAllContentByDescription(String query);

    @Query("SELECT content.* FROM content JOIN contentFts ON (content.id = contentFts.rowid) "
            + "WHERE contentFts MATCH :query")
    List<ContentEntity> searchAllContentByDescriptionSync(String query);

    @Delete
    void delete(ContentEntity code);
}
