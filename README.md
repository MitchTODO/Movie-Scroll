# Movie Scroll (Popular Movies) a Udacity Project

Description : Android app that allows the user to discover movies, within TheMovieDataBase.org.

![alt text](/readmePic/Screenshot_20191108-191748_Movie%20Scroll.jpg)

## Activitys

App consist of two views.

### MainActivity

As the root view, user is first presented with the most popular movies within a recycler view in gridLayout. Three buttons exist at the bottom of the view allowing the user to change the search results by genre, year and sort by popularity, revenue, rating and original title. When selected, the button color will change and a horizontal scroll bar is presented at the top of the screen containing search parameter buttons. If a search parameter button is selected, button will be highlighted in the same color as the button at the bottom of the Acivity. Also updating the recycler view with new movies based on new search parameters. Recycler view fills the entire screen and allows the user to scroll through the movies. When the user reaches the bottom of the recycler view more movies will be present. Two modes exist within the MainActivity discover and watchlist. Discover modes allows for searching and sorting of movies while the watchlist will show users saved movies. When a movie image is selected an intent is executed presenting MoviesDetails activity. 

### MovieDetails

Activity is responsible for displaying and saving movie information. Immediately shown is basic information rating, title, description, genres and release date. Get requests for addinital information is done for trailers, similar movies and reviews. Watchlist(favorites) button is displayed at the top right of the activity for saving and deleting movies. All information is hidden and presented when a corresponding button is selected. Note, User presented with a progress bars while waiting for response from the network. Movie trailers and similar movies have the same layout, that is, imageViews within horizontal scroll views. Trailer image is selected an Intent for the Youtube App is executed, if not avaiable uses the default browser. While a similar movie selected will reload all information details within the activity.

## Handling orentations and Positions

### MainActivity

The recycler views grid row count changes with orentation. 

<b>Landscape:</b> row count 6 images per row.

<b>Portrait:</b> row count 2 images per row.

RecyclerView positions are also kept preventing the recycler view jumping up to the top when orentations changes.

### MovieDetails

Backdrop image changes with orentation

## Potential Problems and Future Improvements

Problems:
- FIXED A observer is used for "liveData" on movies added to watchList however the "onChange" override was not firing within the MainActivity. This was <b>fixed</b> by adding the same observer, "liveData" and "onChange" to the MovieDetails Activity.
- Refactoring the Word class causes roomDB to stop working
- A crash does accoure but very rare, unkown point of origin

Improvements:
- The ability to search by actor
