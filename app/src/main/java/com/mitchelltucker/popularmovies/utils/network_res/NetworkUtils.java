package com.mitchelltucker.popularmovies.utils.network_res;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {

    /**
     * searchBuilder
     * @param page : String
     * @param sort_by : String
     * @param language : String
     * @param year_by : String
     * @param region_by : String
     * @param genre_by : String
     * @return String
     */
    public static String searchBuilder(String page,String sort_by,String language,String year_by,String region_by,String genre_by) {
        String url;
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("api.themoviedb.org")
                .appendPath("3")
                .appendPath("discover")
                .appendPath("movie")
                .appendQueryParameter("api_key", "YOUR KEY HERE")
                .appendQueryParameter("language",language)
                .appendQueryParameter("sort_by",sort_by)
                .appendQueryParameter("include_adult", "false")
                .appendQueryParameter("region",region_by)
                .appendQueryParameter("year",year_by)
                .appendQueryParameter("with_genres",genre_by)
                .appendQueryParameter("include_video","false")
                .appendQueryParameter("page",page);
        url = builder.build().toString();

        return url;
    }

    /**
     * imageUrlBuilder
     * @param moviePath : String
     * @return String
     */
    public static String imageUrlBuilder(String moviePath){
        String url;
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("image.tmdb.org")
                .appendPath("t")
                .appendPath("p")
                .appendPath("w500")
                .appendPath(moviePath.replaceFirst("/",""));
        url = builder.build().toString();
        return url;

    }

    /**
     * videoUrlBuilder
     * @param movieID : String
     * @param language : String
     * @return String
     */
    public static String videoUrlBuilder(String movieID,String language){
        String url;
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("api.themoviedb.org")
                .appendPath("3")
                .appendPath("movie")
                .appendPath(movieID)
                .appendPath("videos")
                .appendQueryParameter("api_key","YOUR KEY HERE")
                .appendQueryParameter("language", language);
        url = builder.build().toString();
        return url;
    }

    /**
     * reviewUrlBuilder
     * @param movieID : String
     * @param language : String
     * @return : String
     */
    public static String reviewUrlBuilder(String movieID,String language){
        String url;
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("api.themoviedb.org")
                .appendPath("3")
                .appendPath("movie")
                .appendPath(movieID)
                .appendPath("reviews")
                .appendQueryParameter("api_key","YOUR KEY HERE")
                .appendQueryParameter("language", language);
        url = builder.build().toString();
        return url;
    }

    /**
     * similarUrlBuilder
     * @param movieID : String
     * @param language String
     * @return
     */
    public static String similarUrlBuilder(String movieID, String language) {
        String url;
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("api.themoviedb.org")
                .appendPath("3")
                .appendPath("movie")
                .appendPath(movieID)
                .appendPath("similar")
                .appendQueryParameter("api_key","YOUR KEY HERE")
                .appendQueryParameter("language",language);
        url = builder.build().toString();
        return url;
    }

    // NOTE Used to load in video preview

    /**
     * videoUrlPlayerUrl
     * @param key : String
     * @return String
     */
    public static String videoUrlPlayerUrl(String key){
        String url;
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("img.youtube.com")
                .appendPath("vi")
                .appendPath(key)
                .appendPath("mqdefault.jpg");
        url = builder.build().toString();
        return url;
    }


    public static void watchYoutubeVideo(Context context, String id){
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("https://www.youtube.com/watch?v=" + id));
        try {
            context.startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            context.startActivity(webIntent);
        }
    }

    public static String reviewsUrlBuilder(String movieID,String language,String page){
        String url;
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("api.themoviedb.org")
                .appendPath("3")
                .appendPath("movie")
                .appendPath(movieID)
                .appendPath("reviews")
                .appendQueryParameter("api_key","YOUR KEY HERE")
                .appendQueryParameter("language", language)
                .appendQueryParameter("page",page);
        url = builder.build().toString();
        return url;
    }



    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param builtUrl : String The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(String builtUrl) throws IOException {
        URL url = null;
        try {
            url = new URL(builtUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    public static String checkRequest(boolean status, int size){
        if(status){
            if(size > 0){
                return "success";
            }else{
                return "no_content";
            }
        }else{
            return "network_error";
        }
    }

    // Present toast if no internet connection
    public static void showErrorMessage(Context context) {
        Toast toast = Toast.makeText(context, "\uD83E\uDD14 Check your Internet Connection", Toast.LENGTH_LONG);
        toast.show();
    }


}
