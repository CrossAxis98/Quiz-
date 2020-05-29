package com.example.quizam;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface BestResultDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(BestResult newBestResult);

    @Query("DELETE FROM bestResults_table")
    void deleteAll();

    @Query("SELECT * from bestResults_table ORDER BY dbTotalGuessess ASC")
    LiveData<List<BestResult>> getSortedRecords();
}
