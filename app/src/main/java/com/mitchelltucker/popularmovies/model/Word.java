package com.mitchelltucker.popularmovies.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "word_table")
public class Word {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "word")
    private String mWord;
    private int popularity;
    private int vote_count;
    private String poster_path;
    private int id;
    private String backdrop_path;
    private String original_title;
    private int genre;
    private String title;
    private float vote_average;
    private String overview;
    private String release_date;


    public Word(@NonNull String word,
                int popularity,
                int vote_count,
                String poster_path,
                int id,
                String backdrop_path,
                String original_title,
                int genre,
                String title,
                float vote_average,
                String overview,
                String release_date) {
        this.mWord = word;
        this.popularity = popularity;
        this.vote_count = vote_count;
        this.poster_path = poster_path;
        this.id = id;
        this.backdrop_path = backdrop_path;
        this.original_title = original_title;
        this.genre = genre;
        this.title = title;
        this.vote_average = vote_average;
        this.overview = overview;
        this.release_date = release_date;
    }

    @NonNull
    public String getWord() { return this.mWord; }

    public int getVote_count() {
        return this.vote_count;
    }

    public String getPoster_path() {
        return this.poster_path;
    }

    public int getId() {
        return this.id;
    }

    public int getGenre() {
        return this.genre;
    }

    public String getTitle() {
        return this.title;
    }

    public String getOverview() {
        return this.overview;
    }

    public String getOriginal_title() {
        return this.original_title;
    }


    public String getBackdrop_path() {
        return this.backdrop_path;
    }

    public int getPopularity() {
        return this.popularity;
    }

    public float getVote_average() {
        return this.vote_average;
    }

    public String getRelease_date() {
        return this.release_date;
    }

}
