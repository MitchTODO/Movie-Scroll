package com.mitchelltucker.popularmovies.model;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

class MovieRepository {

    private MovieDao mMovieDao;
    private LiveData<List<Word>> mAllMovies;

    MovieRepository(Application application) {
        MovieRoomDataBase db = MovieRoomDataBase.getDatabase(application);
        mMovieDao = db.movieDao();
        mAllMovies = mMovieDao.getFavMovies();
    }

    LiveData<List<Word>> getAllMovie() {
        return mAllMovies;
    }

    void insert(Word movie) {
        new insertAsyncTask(mMovieDao).execute(movie);
    }
    void delete(Word movie) {
        new deleteAsyncTask(mMovieDao).execute(movie);
    }

    private static class insertAsyncTask extends AsyncTask<Word,Void,Void> {
        private MovieDao mAsyncTaskDao;

        insertAsyncTask(MovieDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Word... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }


    private static class deleteAsyncTask extends AsyncTask<Word,Void,Void> {
        private MovieDao mAsyncTaskDao;

        deleteAsyncTask(MovieDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Word... params) {
            mAsyncTaskDao.delete(params[0]);
            return null;
        }
    }

}
