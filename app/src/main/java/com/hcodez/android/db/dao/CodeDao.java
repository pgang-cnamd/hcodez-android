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

    @Query("SELECT * FROM code")
    List<CodeEntity> loadAllCodesSync();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(CodeEntity code);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insertAll(List<CodeEntity> code);

    @Query("SELECT * FROM code WHERE id = :codeId")
    LiveData<CodeEntity> loadCode(int codeId);

    @Query("SELECT * FROM code WHERE id = :codeId")
    CodeEntity loadCodeSync(int codeId);

    @Query("SELECT * FROM code WHERE identifier IN (:identifiers)")
    LiveData<List<CodeEntity>> loadCodesWithIdentifier(List<String> identifiers);

    @Query("SELECT code.* FROM code JOIN codeFts ON (code.id = codeFts.rowid) "
            + "WHERE codeFts MATCH :query")
    LiveData<List<CodeEntity>> searchAllCodes(String query);

    @Query("SELECT code.* FROM code JOIN codeFts ON (code.id = codeFts.rowid) "
            + "WHERE codeFts MATCH :query")
    List<CodeEntity> searchAllCodesSync(String query);

    @Delete
    void delete(CodeEntity code);
}
