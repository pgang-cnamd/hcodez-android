package com.hcodez.android.db.dao;

import com.hcodez.android.db.entity.CodeEntity;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface CodeDao {
    @Query("SELECT * FROM code")
    LiveData<List<CodeEntity>> loadAllCodes();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<CodeEntity> codes);

    @Query("SELECT * FROM code WHERE id = :codeId")
    LiveData<CodeEntity> loadCode(int codeId);

    @Query("SELECT * FROM code WHERE id = :codeId")
    CodeEntity loadCodeSync(int codeId);

    @Query("SELECT * FROM code WHERE identifier IN (:identifiers)")
    LiveData<List<CodeEntity>> loadCodesWithIdentifier(List<String> identifiers);

    @Delete
    void delete(CodeEntity code);
}
