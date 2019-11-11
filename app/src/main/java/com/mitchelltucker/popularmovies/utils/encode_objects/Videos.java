package com.mitchelltucker.popularmovies.utils.encode_objects;

import com.mitchelltucker.popularmovies.utils.encode_objects.Video;

import java.util.List;

public class Videos {

    private int id;
    private List<Video> results = null;

    public Videos() {

    }
    public Videos(int id, List<Video> results){
        this.id = id;
        this.results = results;

    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setResults(List<Video> results) {
        this.results = results;
    }

    public List<Video> getResults() {
        return results;
    }

}
