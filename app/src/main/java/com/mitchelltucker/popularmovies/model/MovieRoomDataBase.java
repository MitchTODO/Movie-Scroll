package com.mitchelltucker.popularmovies.model;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;


@Database(entities = {Word.class}, version = 1, exportSchema = false)
public abstract class MovieRoomDataBase extends RoomDatabase {

    public abstract MovieDao movieDao();

    private static volatile MovieRoomDataBase INSTANCE;

    static MovieRoomDataBase getDatabase(final Context context) {
        if(INSTANCE == null) {
            synchronized (MovieRoomDataBase.class) {
                if(INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            MovieRoomDataBase.class,"word_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
