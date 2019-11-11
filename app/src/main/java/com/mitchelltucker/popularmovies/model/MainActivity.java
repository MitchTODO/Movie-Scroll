package com.mitchelltucker.popularmovies.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.appcompat.app.AppCompatActivity;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Configuration;

import android.os.AsyncTask;
import android.os.Bundle;

import com.mitchelltucker.popularmovies.R;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.mitchelltucker.popularmovies.utils.layout_res.screen_metrics;
import com.mitchelltucker.popularmovies.utils.network_res.NetworkUtils;
import com.mitchelltucker.popularmovies.utils.encode_objects.Movies;
import com.mitchelltucker.popularmovies.utils.encode_objects.Movie;

import com.mitchelltucker.popularmovies.utils.network_res.jsonUtils;
import com.mitchelltucker.popularmovies.utils.search_lists;
import com.mitchelltucker.popularmovies.utils.view_adapters.myRecyclerViewAdapter;

import java.lang.ref.WeakReference;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity implements myRecyclerViewAdapter.ItemClickListener,View.OnClickListener {
    // AdapterView
    myRecyclerViewAdapter adapter;
    private RecyclerView mRecyclerView;
    // ProgressBar
    private ProgressBar mProgressBar;
    // HorizontalScrolls containing linearLayouts
    private HorizontalScrollView hScrollViewForYear;
    private HorizontalScrollView hScrollViewForGenres;
    private HorizontalScrollView hScrollViewForSort;
    // LinearLayouts containing search buttons
    private LinearLayout mLinearViewForYear;
    private LinearLayout mLinearViewForGenres;
    private LinearLayout mLinearViewSort;
    private LinearLayout linearViewForButtons;
    // Buttons for changing search parameters
    private Button genresButton;
    private Button sortButton;
    private Button yearButton;
    // Button View placeholders for mechanics
    View lastYearPressed;
    View lastGenres;
    View lastSort;
    View lastLanguage;


    /**
     * Class parameters.
     *
     *
     *
     */

    // Index used to get selected movie within recyclerView
    public static Integer selectedMovie;

    // Extra message used with intent
    public static final String EXTRA_MESSAGE = "com.MovieDetails.MovieIndex";
    public static final String EXTRA_MESSAGE_FOR_FAV = "com.MovieDetails.MovieFav";

    // get current year
    Calendar calendar = Calendar.getInstance();
    Integer year = calendar.get(Calendar.YEAR);

    // Default search parameters
    private String By_sort = "popularity.desc";
    private String By_year = "all";
    public static String By_language = "en";
    private static final String By_region = "US";
    private String By_genres = "";

    // start and stop page numbers
    // default is 5 pages
    private Integer pageNumberStart = 1;
    private Integer pageNumberStop = 6;

    //saves recycler adapter position
    private Integer page_position = 0;

    // holds movie id's for watchlist movies
    public static List<Integer> idsForWatchList = new ArrayList<>();

    // Used to populate mMovies, no re-query
    public static List<Movie> watchListMovies = new ArrayList<>();
    public static List<Movie> discoverMovies = new ArrayList<>();

    // RecyclerView Adapter data set
    public static List<Movie> mMovies = new ArrayList<>();

    // movie is in watchlist passed with intent
    static public boolean isFavorite = false;

    /**
     * OnCreate
     *
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set title, language and load savedInstance
        setTitle("Discover");
        By_language = Locale.getDefault().getLanguage();

        boolean loadOnDefault = true;
        if (savedInstanceState != null) {
            By_year = savedInstanceState.getString("year");
            By_sort = savedInstanceState.getString("sort");
            By_genres = savedInstanceState.getString("genres");
            pageNumberStart = savedInstanceState.getInt("start");
            pageNumberStop = savedInstanceState.getInt("stop");
            String show_type = savedInstanceState.getString("show_type");
            loadOnDefault = savedInstanceState.getBoolean("loadOnDefault");
            setTitle(show_type);
        }

        // load RoomDB class and Observe for changes to the db
        MovieViewModel mMovieViewModel;
        mMovieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);
        mMovieViewModel.getAllMovie().observe(this, new Observer<List<Word>>() {
            @Override
            public void onChanged(@Nullable final List<Word> movies) {
                idsForWatchList.clear();
                watchListMovies.clear();
                    if(movies != null) {
                        for (int e = 0; e < movies.size(); e++) {
                            Movie savedMovie = new Movie();
                            savedMovie.setPopularity(movies.get(e).getPopularity());
                            savedMovie.setVote_count(movies.get(e).getVote_count());
                            savedMovie.setVideo(false);
                            savedMovie.setPoster_path(movies.get(e).getPoster_path());
                            savedMovie.setId(movies.get(e).getId());
                            savedMovie.setAdult(false);
                            savedMovie.setBackdrop_path(movies.get(e).getBackdrop_path());
                            savedMovie.setOriginal_language(By_language);
                            savedMovie.setOriginal_title(movies.get(e).getOriginal_title());
                            savedMovie.setGenre_ids(Collections.singletonList(movies.get(e).getGenre()));
                            savedMovie.setTitle(movies.get(e).getTitle());
                            savedMovie.setVote_average(movies.get(e).getVote_average());
                            savedMovie.setOverview(movies.get(e).getOverview());
                            savedMovie.setRelease_date(movies.get(e).getRelease_date());
                            idsForWatchList.add(movies.get(e).getId());
                            watchListMovies.add(savedMovie);

                        }
                        if(getTitle() == "Watchlist"){
                            updateRecForWatchList(true);
                        }
                    }
            }

        });

        // get orientation of device and adjust row size
        int rowSize;
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            rowSize = 6;
        }else{
            rowSize = 2;
        }
        // Used to calculate height of each imageView in Adapter
        screen_metrics.updateScreenSize(this,rowSize);

        // Setup button style
        sortButton = findViewById(R.id.action_sort);
        genresButton = findViewById(R.id.action_genre);
        yearButton =  findViewById(R.id.action_year);
        sortButton.setBackgroundResource(android.R.drawable.btn_default);
        genresButton.setBackgroundResource(android.R.drawable.btn_default);
        yearButton.setBackgroundResource(android.R.drawable.btn_default);

        // findViews
        mLinearViewForYear = findViewById(R.id.shapeLayout);
        mLinearViewForGenres = findViewById(R.id.shapeForGenres);
        mLinearViewSort =  findViewById(R.id.shapeForSort);
        mProgressBar = findViewById(R.id.showProgress);
        linearViewForButtons = findViewById(R.id.linearForButton);
        hScrollViewForYear = findViewById(R.id.horizontalYears);
        hScrollViewForGenres = findViewById(R.id.horizontalGenres);
        hScrollViewForSort = findViewById(R.id.horizontalSort);

        // build search Menus
        buildSearchMenu();

        // Set up recyclerView
        mRecyclerView = findViewById(R.id.recyclerviewMovieImage);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, rowSize));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setClickable(true);
        mRecyclerView.setSaveEnabled(true);

        // setup adapter for recycler view
        adapter = new myRecyclerViewAdapter(this, mMovies);
        adapter.setClickListener(this);

        // setup adapter for recyclerView
        mRecyclerView.setAdapter(adapter);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            /**
             * RecyclerView OnScrollStateChanged
             * @param recyclerView: this.recyclerView
             * @param newState: detect bottom of recyclerView
             *
             * Note: when bottom is reached 5 more pages will load
             */
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (getTitle() == "Discover") {
                    if (!recyclerView.canScrollVertically(1)) {
                        pageNumberStart = pageNumberStop;
                        pageNumberStop += 6;
                        movieRequest();
                        mProgressBar.setVisibility(View.VISIBLE);
                        Toast.makeText(MainActivity.this, "Loading More From the TheMovieDB.org", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        // Fixed same pages being loaded twice
        if(loadOnDefault) {
            mProgressBar.setVisibility(View.VISIBLE);
            movieRequest();
        }
    }

    /**
     * OnSaveInstanceState
     * @param outState: Bundle used to save parameters
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        pageNumberStart = pageNumberStop;
        pageNumberStop += 6;
        outState.putString("show_type",String.valueOf(getTitle()));
        outState.putString("year",By_year);
        outState.putString("sort",By_sort);
        outState.putString("genres",By_genres);
        outState.putInt("start",pageNumberStart);
        outState.putInt("stop",pageNumberStop);
        outState.putBoolean("loadOnDefault",false);
        super.onSaveInstanceState(outState);
    }

    /**
     * onBackPressed
     * Note: Clear movie data on back press of Root Activity
     * InfoUrl:https://developer.android.com/guide/components/activities/state-changes
     */
    @Override
    public void onBackPressed(){
        super.onBackPressed();
        mMovies.clear();
        discoverMovies.clear();
        adapter.notifyDataSetChanged();
    }

    /**
     * onCreateOptionsMenu
     * @param menu: custom menu
     * @return true
     * Note: Will hide toolbar and change Icon if watchlist is selected
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        // Inflate the menu;
        getMenuInflater().inflate(R.menu.menu, menu);
        if(getTitle() == "Watchlist"){
            hideToolBar();
            MenuItem watchList = menu.findItem(R.id.watchList);
            watchList.setIcon(R.drawable.globe_for_discovery);
        }
        return true;
    }


    /**
     * onOptionsItemSelected
     * @param item: MenuItem selected
     * @return true
     * Note: jumps between showing users watchlist and discovering movies
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.watchList) {
            // handle event for WatchList
            if (getTitle() == "Discover"){
                setTitle("Watchlist");
                page_position = adapter.getPagePosition();
                // Hide all horizontal scrolls and toolbar
                hideToolBar();
                item.setIcon(R.drawable.globe_for_discovery);
                // update recyclerView
                updateRecForWatchList(true);

            } else {
                // show toolbar
                linearViewForButtons.setVisibility(View.VISIBLE);
                setTitle("Discover");
                item.setIcon(R.drawable.heart_for_watchlist);
                // clear adapter Data and repopulate with discoverMovies
                mMovies.clear();
                mMovies.addAll(discoverMovies);
                adapter.notifyDataSetChanged();
                // Scroll to last position
                mRecyclerView.scrollToPosition(page_position);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * onItemClick
     * @param view: view selected
     * @param position: integer used for indexing Adapter data
     * Note: Check if movie selected is a favorite
     */
    @Override
    public void onItemClick(View view, int position) {

        if (mRecyclerView.isClickable()) {
            isFavorite = false;
            // check if movie selected is in watchList
            isFavorite = idsForWatchList.contains(mMovies.get(position).getId());
            // Start intent for MovieDetails activity
            selectedMovie = position;
            Intent intent = new Intent(this, MovieDetails.class);
            intent.putExtra(EXTRA_MESSAGE, position);
            intent.putExtra(EXTRA_MESSAGE_FOR_FAV, isFavorite);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

        }
    }

    /**
     * onClick
     * @param view: button view selected
     * Note: all views are contained within horizontal scrolls at top of activity & grouped by Id
     * When selected a new query is made with new search parameters
     */
    @Override
    public void onClick(View view) {
        String tag = String.valueOf(view.getTag());
        switch(view.getId()){
            case 1:
                // Change button cosmetics
                if(lastYearPressed == null){
                    lastYearPressed = view;
                    view.setBackgroundColor(getResources().getColor(R.color.yearSearch));
                }else {
                    lastYearPressed.setBackgroundResource(android.R.drawable.btn_default);
                    lastYearPressed = view;
                    view.setBackgroundColor(getResources().getColor(R.color.yearSearch));
                }
                // update search parameter field
                By_year = tag;
                break;

            case 2:
                if(lastGenres == null){
                    lastGenres = view;
                    view.setBackgroundColor(getResources().getColor(R.color.genreSearch));
                }else {
                    lastGenres.setBackgroundResource(android.R.drawable.btn_default);
                    lastGenres = view;
                    view.setBackgroundColor(getResources().getColor(R.color.genreSearch));
                }
                By_genres = tag;
                break;

            case 3:
                if(lastSort == null){
                    lastSort = view;
                    view.setBackgroundColor(getResources().getColor(R.color.sortSearch));
                }else {
                    lastSort.setBackgroundResource(android.R.drawable.btn_default);
                    lastSort = view;
                    view.setBackgroundColor(getResources().getColor(R.color.sortSearch));
                }
                By_sort = tag;
                break;

            case 4:
                if(lastLanguage == null){
                    lastLanguage = view;
                    view.setBackgroundColor(getResources().getColor(R.color.languageSearch));
                }else {
                    lastLanguage.setBackgroundResource(android.R.drawable.btn_default);
                    lastLanguage = view;
                    view.setBackgroundColor(getResources().getColor(R.color.languageSearch));
                }
                By_language = tag;
                break;
            default:
        }

        // reset start and stop page
        pageNumberStart = 1;
        pageNumberStop = 6;
        updateRecForWatchList(false);

        // show a message with the button's ID
        Toast toast = Toast.makeText(this, "Updating ", Toast.LENGTH_LONG);
        toast.show();
    }

    /**
     * buildSearchMenu
     * Description: Programmable builds search buttonsView within each horizontal Scroll
     * Note: Only called once within onCreate
     */
    protected void buildSearchMenu() {
        // create button for each list item
        for (int i = 0; i < search_lists.sort.size(); i++){
            String sortString = search_lists.sort.get(i);
            String sortSearchString = search_lists.sortSearch.get(i);
            // call createButton function within View_creators class
            // Set id to handle button press
            Button sortButton = View_creators.createButton(this,sortSearchString,sortString,3);
            sortButton.setOnClickListener(this);
            // set default highlighted
            if (sortSearchString.equals(By_sort) || i == 0){
                if(lastSort != null){
                    lastSort.setBackgroundResource(android.R.drawable.btn_default);
                }
                lastSort = sortButton;
                sortButton.setBackgroundColor(getResources().getColor(R.color.sortSearch));
            }
            // Add the new button to the LinearLayout
            mLinearViewSort.addView(sortButton);
        }
        // Create a "All" button
        Button allGenresTag = View_creators.createButton(this,"","All",2);
        allGenresTag.setOnClickListener(this);
        allGenresTag.setBackgroundColor(getResources().getColor(R.color.genreSearch));

        for (int i = 0; i < search_lists.genres.size(); i++){
            String genresString = search_lists.genres.get(i);
            String genresSearchString = String.valueOf(search_lists.genresSearch.get(i));
            Button genresButton = View_creators.createButton(this,genresSearchString,genresString,2);
            genresButton.setOnClickListener(this);
            // change highlighted button to last save
            if(genresSearchString.equals(By_genres)){
                lastGenres = genresButton;
                genresButton.setBackgroundColor(getResources().getColor(R.color.genreSearch));
            }
            mLinearViewForGenres.addView(genresButton);
        }
        // if no last save then make default "All" button highlighted
        if(lastGenres == null) {
            lastGenres = allGenresTag;
            mLinearViewForGenres.addView(allGenresTag,0);
        }

        Button allYearsTag = View_creators.createButton(this,"all","All",1);
        allYearsTag.setOnClickListener(this);
        allYearsTag.setBackgroundColor(getResources().getColor(R.color.yearSearch));
        //build search buttons from current year to 1900
        for (Integer first_year = year; first_year >= 1900; first_year--) {
            String single_year = first_year.toString();
            Button btnTag = View_creators.createButton(this,single_year,single_year,1);
            btnTag.setOnClickListener(this);
            if(single_year.equals(By_year)){
                btnTag.setBackgroundColor(getResources().getColor(R.color.yearSearch));
                lastYearPressed = btnTag;
            }
            mLinearViewForYear.addView(btnTag);
        }
        if(lastYearPressed == null){
            lastYearPressed = allYearsTag;
            mLinearViewForYear.addView(allYearsTag,0);
        }
    }

    /**
     * updateRecForWatchList
     * @param isWatchListShow: boolean weather to show users watchlist or discovery
     */
    private void updateRecForWatchList(boolean isWatchListShow){
        mMovies.clear();
        if(isWatchListShow) {
            mProgressBar.setVisibility(View.GONE);
            mMovies.addAll(watchListMovies);
            if(mMovies.isEmpty()){
                    Toast.makeText(MainActivity.this, "No Movies in your Watchlist! \uD83C\uDFAC", Toast.LENGTH_LONG).show();
            }
        }else{
            mProgressBar.setVisibility(View.VISIBLE);
            discoverMovies.clear();
            movieRequest();
        }
        adapter.notifyDataSetChanged();
    }


    /**
     * hideToolbar
     * Description: hides search scroll bars and toolbar
     */
    private void hideToolBar(){
        linearViewForButtons.setVisibility(View.GONE);
        hScrollViewForYear.setVisibility(View.GONE);
        hScrollViewForSort.setVisibility(View.GONE);
        hScrollViewForGenres.setVisibility(View.GONE);
        // toolbar buttons back to default
        sortButton.setBackgroundResource(android.R.drawable.btn_default);
        genresButton.setBackgroundResource(android.R.drawable.btn_default);
        yearButton.setBackgroundResource(android.R.drawable.btn_default);
    }

    /**
     * toolBarButton
     * @param view: toolbar button pressed
     */
    public void toolBarButton(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.action_year:
                if (hScrollViewForYear.getVisibility() == View.VISIBLE) {
                    hScrollViewForYear.setVisibility(View.GONE);
                    view.setBackgroundResource(android.R.drawable.btn_default);
                } else {
                    hScrollViewForYear.setVisibility(View.VISIBLE);
                    view.setBackgroundColor(getResources().getColor(R.color.yearSearch));
                }
                break;
            case R.id.action_sort:
                if (hScrollViewForSort.getVisibility() == View.VISIBLE) {
                    hScrollViewForSort.setVisibility(View.GONE);
                    view.setBackgroundResource(android.R.drawable.btn_default);
                } else {
                    hScrollViewForSort.setVisibility(View.VISIBLE);
                    view.setBackgroundColor(getResources().getColor(R.color.sortSearch));
                }
                break;
            case R.id.action_genre:
                if (hScrollViewForGenres.getVisibility() == View.VISIBLE) {
                    hScrollViewForGenres.setVisibility(View.GONE);
                    view.setBackgroundResource(android.R.drawable.btn_default);
                } else {
                    hScrollViewForGenres.setVisibility(View.VISIBLE);
                    view.setBackgroundColor(getResources().getColor(R.color.genreSearch));
                }
                break;
        }
    }

    /**
     * movieRequest
     * Description: calls async task with search parameters
     */
    private void movieRequest() {
        new FetchMovieTask(this).execute(pageNumberStart.toString(), By_sort,By_language,By_year,By_region,By_genres);
    }

    /**
     * FetchMovieTask
     * Description: on separate thread:
     *              builds url,
     *              executes network request,
     *              populates adapter data
     *
     */
    public static class FetchMovieTask extends AsyncTask<String, Void, Movies> {

        private WeakReference<MainActivity> mainActivityWeakReference;
        // only retain a weak reference to the activity
        FetchMovieTask(MainActivity context) {
            mainActivityWeakReference = new WeakReference<>(context);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Movies doInBackground(String... params) {
            String searchUrl = NetworkUtils.searchBuilder(params[0],params[1],params[2],params[3],params[4],params[5]);
            try {
                String jsonMovieResponse = NetworkUtils.getResponseFromHttpUrl(searchUrl);
                return jsonUtils.parseJsonData(jsonMovieResponse);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }


        @Override
        protected void onPostExecute(Movies moviesData) {
            MainActivity mainActivity = mainActivityWeakReference.get();
            if (mainActivity.getTitle() == "Discover"){
                if (moviesData != null) {
                    discoverMovies.addAll(moviesData.results());
                    mMovies.addAll(moviesData.results());
                    mainActivity.adapter.notifyDataSetChanged();
                    mainActivity.pageNumberStart += 1;
                    // loads in 5 pages
                    if (mainActivity.pageNumberStart < mainActivity.pageNumberStop) {
                        mainActivity.movieRequest();
                    }else{
                        mainActivity.mProgressBar.setVisibility(View.GONE);
                    }
                } else {
                    mainActivity.mProgressBar.setVisibility(View.GONE);
                    NetworkUtils.showErrorMessage(mainActivity);
                }
            }
        }

    }


}

