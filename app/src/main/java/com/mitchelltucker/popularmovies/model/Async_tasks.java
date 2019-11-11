package com.mitchelltucker.popularmovies.model;

import android.os.AsyncTask;

import com.mitchelltucker.popularmovies.utils.encode_objects.Movies;
import com.mitchelltucker.popularmovies.utils.encode_objects.Reviews;
import com.mitchelltucker.popularmovies.utils.encode_objects.Videos;
import com.mitchelltucker.popularmovies.utils.network_res.NetworkUtils;
import com.mitchelltucker.popularmovies.utils.network_res.jsonUtils;

import java.lang.ref.WeakReference;

class Async_tasks {
    /**
     * FetchVideoTask
     * Description: Async network task for movie trailers
     */
    static class FetchVideoTask extends AsyncTask<String, Void , Videos> {
        private WeakReference<MovieDetails> detailsWeakReference;
        FetchVideoTask(MovieDetails context) {
            detailsWeakReference = new WeakReference<>(context);
        }


        @Override
        protected Videos doInBackground(String... params) {
            String videoUrl = NetworkUtils.videoUrlBuilder(params[0],params[1]);
            try {
                String jsonVideoResponse = NetworkUtils.getResponseFromHttpUrl(videoUrl);
                return jsonUtils.videoParse(jsonVideoResponse);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Videos videoData) {
            MovieDetails movieDetails = detailsWeakReference.get();
            if(videoData != null){
                movieDetails.allVideos = videoData;
                movieDetails.loadVideoViews(true);

            } else {
                movieDetails.loadVideoViews(false);
            }
        }
    }

    /**
     * FetchSimilarMovies
     * Async network task for similar movies
     */
    public static class FetchSimilarMovies extends AsyncTask<String, Void, Movies> {
        private WeakReference<MovieDetails> detailsWeakReference;
        FetchSimilarMovies(MovieDetails context) {
            detailsWeakReference = new WeakReference<>(context);
        }


        @Override
        protected Movies doInBackground(String... params) {
            String similarUrl = NetworkUtils.similarUrlBuilder(params[0],params[1]);
            try {
                String jsonMovieResponse = NetworkUtils.getResponseFromHttpUrl(similarUrl);
                return jsonUtils.parseJsonData(jsonMovieResponse);

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Movies movies) {
            MovieDetails movieDetails = detailsWeakReference.get();
            if (movies != null) {
                movieDetails.similarMovies = movies;
                movieDetails.loadSimilarMovieViews(true);
            } else {
                movieDetails.loadSimilarMovieViews(false);
            }
        }
    }

    /**
     * FetchReviewTask
     * Async network tasks for movie reviews
     */
    public static class FetchReviewTask extends AsyncTask<String, Void , Reviews> {
        private WeakReference<MovieDetails> detailsWeakReference;
        FetchReviewTask(MovieDetails context) {
            detailsWeakReference = new WeakReference<>(context);
        }

        @Override
        protected Reviews doInBackground(String... params) {
            String reviewUrl = NetworkUtils.reviewUrlBuilder(params[0],params[1]);
            try {
                String jsonVideoResponse = NetworkUtils.getResponseFromHttpUrl(reviewUrl);
                return jsonUtils.reviewParse(jsonVideoResponse);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Reviews reviewsData) {
            MovieDetails movieDetails = detailsWeakReference.get();
            if(reviewsData != null){
                movieDetails.allReviews = reviewsData;
                movieDetails.loadReviewViews(true);
            } else {
                movieDetails.loadReviewViews(false);
            }
        }
    }



}
