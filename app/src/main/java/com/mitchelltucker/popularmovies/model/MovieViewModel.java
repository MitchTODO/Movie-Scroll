package com.mitchelltucker.popularmovies.model;


import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import android.app.Application;


import java.util.List;

public class MovieViewModel extends AndroidViewModel {

    private MovieRepository mRepository;
    private LiveData<List<Word>> mAllMovies;


    public MovieViewModel (Application application) {
        super(application);

        mRepository = new MovieRepository(application);
        mAllMovies = mRepository.getAllMovie();

    }



    LiveData<List<Word>> getAllMovie() {return mAllMovies; }

    void insert(Word movie) {
        mRepository.insert(movie);
    }

    void delete(Word movie) {
        mRepository.delete(movie);
    }

}
