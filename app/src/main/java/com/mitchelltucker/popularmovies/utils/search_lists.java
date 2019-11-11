package com.mitchelltucker.popularmovies.utils;

import java.util.Arrays;
import java.util.List;

public class search_lists {

    // Sort by list
    public static final List<String> sort = Arrays.asList(
            "Popularity",
            "Revenue",
            "Rating",
            "Original Title");
    public static final List<String> sortSearch = Arrays.asList(
            "popularity.desc",
            "revenue.desc",
            "vote_count.desc" ,
            "original_title.desc");

    // Genres
    public static final List<String> genres = Arrays.asList(
            "Action",
            "Adventure",
            "Animated",
            "Comedy",
            "Crime",
            "Documentary",
            "Drama",
            "Family",
            "Fantasy",
            "History",
            "Horror",
            "Music",
            "Mystery",
            "Romance",
            "Science Fiction",
            "TV Movie",
            "Thriller",
            "War",
            "Western");

    // Genre IDs
    public static final List<Integer> genresSearch = Arrays.asList(
            28,
            12,
            16,
            35,
            80,
            99,
            18,
            10751,
            14,
            36,
            27,
            10402,
            9648,
            10749,
            878,
            10770,
            53,
            10752,
            37);




    // MAY-BE USE WHEN UPDATED
    public static final List<String> regions_Search = Arrays.asList(
            "US",
            "DE",
            "FI",
            "FR",
            "IN",
            "IT",
            "JP",
            "KP",
            "RU",
            "SE",
            "CN",
            "HK",
            "MX",
            "CA"

    );

    public static final List<String> regions = Arrays.asList(
            "United States",
            "Germany",
            "finland",
            "France",
            "India",
            "Italy",
            "Japan",
            "South Korea",
            "Russia",
            "Sweden",
            "China",
            "Hong Kong",
            "Mexico",
            "Canada"
    );

}
