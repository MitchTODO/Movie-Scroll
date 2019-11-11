package com.mitchelltucker.popularmovies.model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Word movie);

    @Delete
    void delete(Word movie);

    @Query("DELETE FROM word_table")
    void deleteAll();

    @Query("SELECT * from word_table")
    LiveData<List<Word>> getFavMovies();
}
