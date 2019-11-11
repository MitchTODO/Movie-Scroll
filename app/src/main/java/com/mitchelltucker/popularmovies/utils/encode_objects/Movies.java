package com.mitchelltucker.popularmovies.utils.encode_objects;

import java.util.List;
import com.mitchelltucker.popularmovies.utils.encode_objects.Movie;


public class Movies {

    private int page;
    private int total_results;
    private int total_pages;
    private List<Movie> results = null;

    /**
     * No args constructor for use in serialization
     */
    public Movies() {
    }

    public Movies(int page, int total_results, int total_pages, List<Movie> results) {
        this.page = page;
        this.total_results = total_results;
        this.total_pages = total_pages;
        this.results = results;
    }

    public int getpage() {
        return page;
    }

    public void setpage(int page) {
        this.page = page;
    }

    public int getTotal_results() {
        return total_results;
    }

    public void setTotal_results(int total_results) {
        this.total_results = total_results;
    }

    public int getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(int total_pages) { this.total_pages = total_pages; }

    public List<Movie> results() {
        return results;
    }

    public void setResults(List<Movie> results) { this.results = results; }
}
