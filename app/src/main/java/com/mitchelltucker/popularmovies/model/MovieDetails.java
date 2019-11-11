package com.mitchelltucker.popularmovies.model;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;


import com.mitchelltucker.popularmovies.R;
import com.mitchelltucker.popularmovies.utils.encode_objects.Movie;

import com.mitchelltucker.popularmovies.utils.encode_objects.Movies;
import com.mitchelltucker.popularmovies.utils.layout_res.screen_metrics;
import com.mitchelltucker.popularmovies.utils.network_res.NetworkUtils;
import com.mitchelltucker.popularmovies.utils.encode_objects.Review;
import com.mitchelltucker.popularmovies.utils.encode_objects.Reviews;
import com.mitchelltucker.popularmovies.utils.encode_objects.Videos;

import com.mitchelltucker.popularmovies.utils.search_lists;

import com.squareup.picasso.Picasso;

import android.content.Intent;
import android.content.res.Configuration;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import static com.mitchelltucker.popularmovies.utils.network_res.NetworkUtils.watchYoutubeVideo;

public class MovieDetails extends AppCompatActivity implements View.OnClickListener {
    // placeholders
    ImageView posterBack;
    Button favButton;

    ScrollView scrollView;

    LinearLayout dataHolder;
    LinearLayout videoHolder;
    LinearLayout reviewHolder;
    LinearLayout reviewAndRatingHolder;

    RelativeLayout horizontalForVideoPreview;
    RelativeLayout horizontalForSimilarTitles;

    TextView title;
    TextView movie_rating;
    TextView overview;
    TextView genre_id;
    TextView release_date;

    LinearLayout linearLayoutForHorizontalContainer;

    String language;
    String landScapePosterUrl;
    String portScapePosterUrl;

    Movie movieDetails;
    // custom dataTypes related to selected movie
    public Movies similarMovies;
    public Videos allVideos;
    public Reviews allReviews;

    private MovieViewModel mMovieViewModel;

    public boolean isFavorite;

    /**
     * onCreate
     * @param savedInstanceState: Bundle instance
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        // setup room database
        mMovieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);
        // setup actionBar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setHomeButtonEnabled(false); // disable the button
            actionBar.setDisplayHomeAsUpEnabled(false); // remove the left caret
            actionBar.setDisplayShowHomeEnabled(false); // remove the icon
        }
        // setup poster image layout params
        int width = screen_metrics.getRawWidth();
        int height = screen_metrics.getRawHeight();
        posterBack = findViewById(R.id.posterBack);
        posterBack.getLayoutParams().height = (int) (width * 1.5);
        posterBack.getLayoutParams().width = width;
        // find views
        favButton = findViewById(R.id.saveToFav);

        scrollView = findViewById(R.id.showDetails);

        dataHolder = findViewById(R.id.movieInfoHolder);
        dataHolder.setPadding(0,height / 3,0,0);
        videoHolder = findViewById(R.id.shapeForVideoPreview);
        reviewHolder= findViewById(R.id.movieRatingAndReview);
        reviewAndRatingHolder = findViewById(R.id.reviewsAndRating);

        horizontalForVideoPreview = findViewById(R.id.horizontalForVideoPreview);
        horizontalForSimilarTitles = findViewById(R.id.horizontalForSimilarTitles);
        linearLayoutForHorizontalContainer = findViewById(R.id.shapeForTitles);

        title = findViewById(R.id.mainTitle);
        movie_rating = findViewById(R.id.movieRating);
        overview = findViewById(R.id.overView);
        genre_id = findViewById(R.id.genre_id);
        release_date = findViewById(R.id.release_Date);
        // get language from main activity
        language = MainActivity.By_language;
        // getIntent
        Intent intent = getIntent();
        // get selected movie
        movieDetails = MainActivity.mMovies.get(intent.getIntExtra(MainActivity.EXTRA_MESSAGE,MainActivity.selectedMovie));
        // boolean if selected movie is favorite
        isFavorite = intent.getBooleanExtra(MainActivity.EXTRA_MESSAGE_FOR_FAV,MainActivity.isFavorite);
        if(isFavorite){
            favButton.setBackgroundResource(R.drawable.fav_on_4);
        }else{
            favButton.setBackgroundResource(R.drawable.fav_off_4);
        }

        //set title,rating,overview,release_date,genres
        title.setText(movieDetails.getTitle());
        String StringRating =  String.valueOf(movieDetails.getVote_average()).concat("/10 ");
        movie_rating.setText(StringRating);
        overview.setText(movieDetails.getOverview());
        release_date.setText(movieDetails.getRelease_date());
        for(int r = 0; r < movieDetails.getGenre_ids().size(); r++){
            if (search_lists.genresSearch.contains(movieDetails.getGenre_ids().get(r))){
                int indexOfObject = search_lists.genresSearch.indexOf(movieDetails.getGenre_ids().get(r));
                genre_id.append(search_lists.genres.get(indexOfObject) + ", ");
            }
        }
        // network request for related assets
        requestTrailers();
        requestReviews();
        requestSimilarMovies();

        // build urls for landscape and portrait backgrounds images
        portScapePosterUrl = NetworkUtils.imageUrlBuilder(movieDetails.getPoster_path());
        landScapePosterUrl = NetworkUtils.imageUrlBuilder(movieDetails.getBackdrop_path());


        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Picasso.get()
                    .load(landScapePosterUrl)
                    .into(posterBack);
        }else {
            Picasso.get()
                    .load(portScapePosterUrl)
                    .into(posterBack);
       }
    }

    /**
     * finish
     * Animation on finish
     */

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }

    /**
     * loadVideoViews
     * @param requestStatus: boolean, network request was successful
     * Description: make request for related videos (trailers, behind the scenes)
     */
    public void loadVideoViews(boolean requestStatus){
        switch (NetworkUtils.checkRequest(requestStatus,allVideos.getResults().size())){
            case "success":
                // loop through all the videos
                for (int v = 0; v < allVideos.getResults().size(); v++) {
                    // build video preview url
                    String videoUrlString = NetworkUtils.videoUrlPlayerUrl(allVideos.getResults().get(v).getKey());
                    // create a imageview for each video preview
                    ImageView previewForVideo = View_creators.createImageView(this,"videoSelected",v);

                    if(screen_metrics.getRawHeight() > screen_metrics.getRawWidth()){
                        int height = screen_metrics.getRawHeight() / 4;
                        int width = (int) (height * 1.8);
                        previewForVideo.setLayoutParams(new LinearLayout.LayoutParams(width,height));
                    }else{
                        // height width flipped
                        int height = screen_metrics.getRawWidth() / 4;
                        int width = (int) (height * 1.8);
                        previewForVideo.setLayoutParams(new LinearLayout.LayoutParams(width,height));
                    }

                    // setClickListener
                    previewForVideo.setOnClickListener(this);
                    // add each imageView to videoHolder
                    videoHolder.addView(previewForVideo);
                    // request video preview image
                    Picasso.get()
                            .load(videoUrlString)
                            .into(previewForVideo);
                }
                break;
            case "no_content":
                // make TextView for no content
                TextView noTrailerTextView = View_creators.createTextView(this,R.string.error_trailer,20);
                videoHolder.addView(noTrailerTextView);
                break;
            case "network_error":
                // make TextView for no connection
                TextView noResponseTextView = View_creators.createTextView(this,R.string.error_network,20);
                videoHolder.addView(noResponseTextView);
                break;
        }
    }

    /**
     * loadReviewViews
     * @param requestStatus:
     * NOTE: very similar to loadVideoViews function
     */

    public void loadReviewViews(boolean requestStatus){
        switch(NetworkUtils.checkRequest(requestStatus,allReviews.getResults().size())){
            case "success":
                for (int v = 0; v < allReviews.getResults().size(); v++) {
                    Review newReview = allReviews.getResults().get(v);
                    // concat strings
                    String authorText = "Author: ".concat(newReview.getAuthor());
                    String contentText = "Review: ".concat(newReview.getContent());
                    // create textViews with default string res
                    TextView authorTextView = View_creators.createTextView(this,R.string.rating,30);
                    TextView contentTextView = View_creators.createTextView(this,R.string.rating,20);
                    // setText with concat string
                    authorTextView.setText(authorText);
                    contentTextView.setText(contentText);
                    // add views to "reviewHolder"
                    reviewHolder.addView(authorTextView);
                    reviewHolder.addView(contentTextView);
                }
                break;
            case "no_content":
                TextView noReviewsTextView = View_creators.createTextView(this,R.string.error_reviews,20);
                reviewHolder.addView(noReviewsTextView);
                break;
            case "network_error":
                TextView noResponseTextView = View_creators.createTextView(this,R.string.error_network,20);
                reviewHolder.addView(noResponseTextView);
                break;
        }
    }

    /**
     * loadSimilarMovieViews
     * @param requestStatus:
     * NOTE: similar to "loadVideoViews"
     */

    public void loadSimilarMovieViews(Boolean requestStatus){
        switch (NetworkUtils.checkRequest(requestStatus,similarMovies.results().size())){
            case "success":
                for (int m = 0; m < similarMovies.results().size(); m++) {
                    Movie movie = similarMovies.results().get(m);
                    ImageView newImage = View_creators.createImageView(this,"imageSelected",m);
                    newImage.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
                    newImage.setOnClickListener(this);
                    String posterUrl = NetworkUtils.imageUrlBuilder(movie.getPoster_path());
                    linearLayoutForHorizontalContainer.addView(newImage);
                    Picasso.get()
                            .load(posterUrl)
                            .into(newImage);
                }
                break;
            case "no_content":
                TextView noSimilarMovieTextView = View_creators.createTextView(this,R.string.error_similar,20);
                linearLayoutForHorizontalContainer.addView(noSimilarMovieTextView);
                break;
            case "network_error":
                TextView noResponseTextView = View_creators.createTextView(this,R.string.error_network,20);
                linearLayoutForHorizontalContainer.addView(noResponseTextView);
                break;
        }
    }

    /**
     * onClick
     * @param view: view selected
     * NOTE: handles trailer and similar movie onClick
     */

    @Override
    public void onClick(View view) {
        if (view.getTag().equals("imageSelected")) {
            // remove all assets
            linearLayoutForHorizontalContainer.removeAllViews();
            reviewHolder.removeAllViews();
            videoHolder.removeAllViews();
            genre_id.setText("");
            // use id to get index of similar movie pressed
            movieDetails = similarMovies.results().get(view.getId());
            isFavorite = MainActivity.idsForWatchList.contains(movieDetails.getId());
            if(isFavorite){
                favButton.setBackgroundResource(R.drawable.fav_on_4);
            }else{
                favButton.setBackgroundResource(R.drawable.fav_off_4);
            }

            // build urls for landscape and portrait backgrounds images
            portScapePosterUrl = NetworkUtils.imageUrlBuilder(movieDetails.getPoster_path());
            landScapePosterUrl = NetworkUtils.imageUrlBuilder(movieDetails.getBackdrop_path());

            int orientation = getResources().getConfiguration().orientation;
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                Picasso.get()
                        .load(landScapePosterUrl)
                        .into(posterBack);
            }else {
                Picasso.get()
                        .load(portScapePosterUrl)
                        .into(posterBack);
            }

            // set up rating and title
            String StringRating =  String.valueOf(movieDetails.getVote_average()).concat("/10 ");
            title.setText(movieDetails.getTitle());
            movie_rating.setText(StringRating);


            // get new movie assets
            requestTrailers();
            requestReviews();
            requestSimilarMovies();

            // setText for genres,overview,release_date
            for(int r = 0; r < movieDetails.getGenre_ids().size(); r++){
                if (search_lists.genresSearch.contains(movieDetails.getGenre_ids().get(r))){
                    int indexOfObject = search_lists.genresSearch.indexOf(movieDetails.getGenre_ids().get(r));
                    genre_id.append(search_lists.genres.get(indexOfObject) + ", ");
                }

            }
            overview.setText(movieDetails.getOverview());
            release_date.setText(movieDetails.getRelease_date());
        } else if (view.getTag().equals("videoSelected")){
            // call NetworkUtils.watchYoutubeVideo to handle action
            watchYoutubeVideo(this,allVideos.getResults().get(view.getId()).getKey());
        }
    }

    /**
     * requestTrailers
     * calls async task for movie trailer
     */
    private void requestTrailers(){
        new Async_tasks.FetchVideoTask(this).execute(String.valueOf(movieDetails.getId()),language);
    }

    /**
     * requestReviews
     * calls async task for movie reviews
     */
    private void requestReviews(){
        new Async_tasks.FetchReviewTask(this).execute(String.valueOf(movieDetails.getId()),language);
    }

    /**
     * requestSimilarMovies
     * calls async task for similar movies
     */
    private void requestSimilarMovies() {
        new Async_tasks.FetchSimilarMovies(this).execute(String.valueOf(movieDetails.getId()),language);
    }


    /**
     * saveToFav
     * @param view: ButtonView
     * NOTE: saving and deletion of moves in watchlist
     */
    public void saveToFav(View view){
        if (!isFavorite) {
            // create data entry
            Word word = new Word(String.valueOf(movieDetails.getId()),
                    movieDetails.getPopularity(),
                    movieDetails.getVote_count(),
                    movieDetails.getPoster_path(),
                    movieDetails.getId(),
                    movieDetails.getBackdrop_path(),
                    movieDetails.getOriginal_title(),
                    movieDetails.getGenre_ids().get(0),
                    movieDetails.getTitle(),
                    movieDetails.getVote_average(),
                    movieDetails.getOverview(),
                    movieDetails.getRelease_date());
            // insert new data entry
            mMovieViewModel.insert(word);
            favButton.setBackgroundResource(R.drawable.fav_on_4);
            isFavorite = true;
            // toast user
            Toast toast = Toast.makeText(this, movieDetails.getTitle() + " added to watch list \uD83C\uDF7F", Toast.LENGTH_LONG);
            toast.show();
        }else{
            Word word = new Word(String.valueOf(movieDetails.getId()),
                    movieDetails.getPopularity(),
                    movieDetails.getVote_count(),
                    movieDetails.getPoster_path(),
                    movieDetails.getId(),
                    movieDetails.getBackdrop_path(),
                    movieDetails.getOriginal_title(),
                    movieDetails.getGenre_ids().get(0),
                    movieDetails.getTitle(),
                    movieDetails.getVote_average(),
                    movieDetails.getOverview(),
                    movieDetails.getRelease_date());

            mMovieViewModel.delete(word);
            // NOTE: bug within a deletion of entry wont be fired on MainActivity without observer implemented here
            mMovieViewModel.getAllMovie().observe(this, new Observer<List<Word>>() {
                /**
                 * onChanged
                 * @param words: observer of livedata list
                 * Note: needed observer to be listening for changes
                 */
                @Override
                public void onChanged(List<Word> words) {
                    Log.i("Room","Movie Removed");
                }
            });

            isFavorite = false;
            favButton.setBackgroundResource(R.drawable.fav_off_4);
            // toast user
            Toast toast = Toast.makeText(this, movieDetails.getTitle() + " removed from watch list \uD83D\uDD96", Toast.LENGTH_LONG);
            toast.show();
        }
    }

    /**
     * buttonManager
     * @param view: View containing information
     * manages views for buttons touches
     */
    private void buttonManager(View view){
        if( view.getVisibility() == View.GONE ){
            view.setVisibility(View.VISIBLE);
            view.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
        }else{
            view.setVisibility(View.GONE);
            view.getLayoutParams().height = 0;
        }
    }

    /**
     * displayView
     * @param view: ButtonView
     * NOTE: handles displaying information when corresponding buttons are pressed
     */
    public void displayView(View view) {
        switch (view.getId()){
            case R.id.buttonViewForTrailer:
                buttonManager(horizontalForVideoPreview);
                break;
            case R.id.buttonViewForOverview:
                buttonManager(overview);
                break;
            case R.id.buttonViewForGenre:
                buttonManager(genre_id);
                break;
            case R.id.buttonViewForRating:
                buttonManager(reviewAndRatingHolder);
                break;
            case R.id.buttonViewForReleaseDate:
                buttonManager(release_date);
                break;
            case R.id.buttonViewForSimilarTitles:
                buttonManager(horizontalForSimilarTitles);
        }
    }


    /**
     * onConfigurationChanged
     * @param newConfig: Configuration
     * Changes backdrop image on orientation change
     */
    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        screen_metrics.updateScreenSize(this,null);
        // Check the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Picasso.get()
                    .load(landScapePosterUrl) // load landscape poster
                    .into(posterBack);

            dataHolder.setPadding(0, screen_metrics.getRawHeight() / 2,0,0);
            posterBack.getLayoutParams().width = screen_metrics.getRawWidth();

        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            Picasso.get()
                    .load(portScapePosterUrl) // load portrait poster
                    .into(posterBack);

            dataHolder.setPadding(0,screen_metrics.getRawHeight() / 2,0,0);
            posterBack.getLayoutParams().height = (int) (screen_metrics.getRawWidth() * 1.5);
            posterBack.getLayoutParams().width = screen_metrics.getRawWidth();
        }
    }
}
