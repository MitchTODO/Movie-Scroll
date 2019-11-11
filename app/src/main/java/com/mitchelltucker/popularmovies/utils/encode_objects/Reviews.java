package com.mitchelltucker.popularmovies.utils.encode_objects;

import com.mitchelltucker.popularmovies.utils.encode_objects.Review;

import java.util.List;

public class Reviews {

    private int id;
    private List<Review> results = null;

    public Reviews() {

    }
    public Reviews(int id, List<Review> results){
        this.id = id;
        this.results = results;

    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setResults(List<Review> results) {
        this.results = results;
    }

    public List<Review> getResults() {
        return results;
    }
}
