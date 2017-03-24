package hu.janos.elek.udacity.android.movies.common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import hu.janos.elek.udacity.android.movies.R;
import hu.janos.elek.udacity.android.movies.SortOrder;

import hu.janos.elek.udacity.android.movies.data.Movie;
import hu.janos.elek.udacity.android.movies.data.Video;

import static hu.janos.elek.udacity.android.movies.common.Constants.TMDB_API_KEY;
import static hu.janos.elek.udacity.android.movies.common.Constants.TMDB_BASE_URL;
import static hu.janos.elek.udacity.android.movies.common.Constants.TMDB_IMAGE_BASE_URL;
import static hu.janos.elek.udacity.android.movies.common.Constants.TMDB_MOVIE;
import static hu.janos.elek.udacity.android.movies.common.Constants.TMDB_MOVIE_LIST_POPULAR;
import static hu.janos.elek.udacity.android.movies.common.Constants.TMDB_MOVIE_LIST_TOPRATED;
import static hu.janos.elek.udacity.android.movies.common.Constants.TMDB_MOVIE_PAGE;
import static hu.janos.elek.udacity.android.movies.common.Constants.TMDB_TOKEN;
import static hu.janos.elek.udacity.android.movies.common.Constants.TMDB_VIDEOS;
import static hu.janos.elek.udacity.android.movies.common.Constants.TMPDB_API_MOVIE_DETAILS_OVERVIEW;
import static hu.janos.elek.udacity.android.movies.common.Constants.TMPDB_API_MOVIE_LIST_RESULTS;
import static hu.janos.elek.udacity.android.movies.common.Constants.TMPDB_API_MOVIE_LIST_RESULTS_ID;
import static hu.janos.elek.udacity.android.movies.common.Constants.TMPDB_API_MOVIE_LIST_RESULTS_POSTER_PATH;
import static hu.janos.elek.udacity.android.movies.common.Constants.TMPDB_API_MOVIE_LIST_RESULTS_RELEASE_DATE;
import static hu.janos.elek.udacity.android.movies.common.Constants.TMPDB_API_MOVIE_LIST_RESULTS_TITLE;
import static hu.janos.elek.udacity.android.movies.common.Constants.TMPDB_API_MOVIE_LIST_RESULTS_VOTE_AVERAGE;
import static hu.janos.elek.udacity.android.movies.common.Constants.TMPDB_API_VIDEO_RESULTS;
import static hu.janos.elek.udacity.android.movies.common.Constants.TMPDB_API_VIDEO_RESULTS_ID;
import static hu.janos.elek.udacity.android.movies.common.Constants.TMPDB_API_VIDEO_RESULTS_ISO_3166;
import static hu.janos.elek.udacity.android.movies.common.Constants.TMPDB_API_VIDEO_RESULTS_ISO_639;
import static hu.janos.elek.udacity.android.movies.common.Constants.TMPDB_API_VIDEO_RESULTS_KEY;
import static hu.janos.elek.udacity.android.movies.common.Constants.TMPDB_API_VIDEO_RESULTS_NAME;
import static hu.janos.elek.udacity.android.movies.common.Constants.TMPDB_API_VIDEO_RESULTS_SITE;
import static hu.janos.elek.udacity.android.movies.common.Constants.TMPDB_API_VIDEO_RESULTS_SIZE;
import static hu.janos.elek.udacity.android.movies.common.Constants.TMPDB_API_VIDEO_RESULTS_TYPE;

public class Utils {

    public static String buildImageUrlString(String imagePath) {
        Uri uri = Uri.parse(TMDB_IMAGE_BASE_URL).buildUpon()
                .appendPath(imagePath.replaceFirst("/", ""))
                .build();
        return uri.toString();
    }

    private static Uri.Builder baseUriBuilder() {
        return Uri.parse(TMDB_BASE_URL).buildUpon()
                .appendQueryParameter(TMDB_API_KEY, TMDB_TOKEN)
                .appendPath(TMDB_MOVIE);
    }

    private static Uri buildMovieTrailersUri(int movieId) {
        Uri.Builder builder = baseUriBuilder();

        builder.appendPath(Integer.toString(movieId))
                .appendPath(TMDB_VIDEOS);

        return builder.build();
    }

    private static Uri buildMovieDetailsUri(int movieId) {
        Uri.Builder builder = baseUriBuilder();

        builder.appendPath(Integer.toString(movieId));

        return builder.build();
    }

    private static Uri buildMovieListUri(SortOrder sortOrder, int page) {
        Uri.Builder builder = baseUriBuilder();

        switch (sortOrder) {
            case POPULAR:
                builder.appendPath(TMDB_MOVIE_LIST_POPULAR);
                break;
            case TOP_RATED:
                builder.appendPath(TMDB_MOVIE_LIST_TOPRATED);
                break;
        }

        builder.appendQueryParameter(TMDB_MOVIE_PAGE, Integer.toString(page));

        return builder.build();
    }

    private static String getContent(Uri uri) throws UtilsException {
        try {
            URL url = new URL(uri.toString());
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            InputStream in;
            if (urlConnection.getResponseCode() != 200) {
                in = urlConnection.getErrorStream();
            } else {
                in = urlConnection.getInputStream();
            }

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            if (scanner.hasNext()) {
                return scanner.next();
            }
        } catch (IOException e) {
            Log.e(Utils.class.getName(), "Unable to download content from " +
                    uri.toString() + ": " + e.getMessage());
            throw new UtilsException(R.string.faild_to_download);
        }

        return "";
    }

    public static List<Video> getMovieTrailers(int movieId) throws UtilsException {
        List<Video> videos = new LinkedList<>();
        Uri builtUri = buildMovieTrailersUri(movieId);
        String responseString = getContent(builtUri);

        try {
            JSONObject responseJson = new JSONObject(responseString);
            JSONArray videosJson = responseJson.getJSONArray(TMPDB_API_VIDEO_RESULTS);
            for (int i = 0; i != videosJson.length(); i++) {
                JSONObject videoJson = videosJson.getJSONObject(i);
                Video video = new Video();
                video.setId(videoJson.getString(TMPDB_API_VIDEO_RESULTS_ID));
                video.setIso_639_1(videoJson.getString(TMPDB_API_VIDEO_RESULTS_ISO_639));
                video.setIso_3166_1(videoJson.getString(TMPDB_API_VIDEO_RESULTS_ISO_3166));
                video.setKey(videoJson.getString(TMPDB_API_VIDEO_RESULTS_KEY));
                video.setName(videoJson.getString(TMPDB_API_VIDEO_RESULTS_NAME));
                video.setSite(videoJson.getString(TMPDB_API_VIDEO_RESULTS_SITE));
                video.setSize(videoJson.getInt(TMPDB_API_VIDEO_RESULTS_SIZE));
                video.setType(videoJson.getString(TMPDB_API_VIDEO_RESULTS_TYPE));

                videos.add(video);
            }
        } catch (JSONException e) {
            Log.e(Utils.class.getName(), "Unable to parse content \"" +
                    responseString + "\": " + e.getMessage());
            throw new UtilsException(R.string.faild_to_parse);
        }

        return videos;
    }

    public static String getMoviePlot(int movieId) throws UtilsException {
        Uri builtUri = buildMovieDetailsUri(movieId);
        String responseString = getContent(builtUri);

        try {
            JSONObject responseJson = new JSONObject(responseString);
            return responseJson.getString(TMPDB_API_MOVIE_DETAILS_OVERVIEW);
        } catch (JSONException e) {
            Log.e(Utils.class.getName(), "Unable to parse content \"" +
                    responseString + "\": " + e.getMessage());
            throw new UtilsException(R.string.faild_to_parse);
        }
    }

    public static List<Movie> getMovieList(SortOrder sortOrder, int page) throws UtilsException {
        List<Movie> movies = new LinkedList<>();

        Uri builtUri = buildMovieListUri(sortOrder, page);
        String responseString = getContent(builtUri);

        try {
            JSONObject responseJson = new JSONObject(responseString);
            JSONArray movieListJson = responseJson.getJSONArray(TMPDB_API_MOVIE_LIST_RESULTS);
            for (int i = 0; i != movieListJson.length(); i++) {
                JSONObject movieJson = movieListJson.getJSONObject(i);
                Movie movie = new Movie(movieJson.getInt(TMPDB_API_MOVIE_LIST_RESULTS_ID));
                movie.setTitle(movieJson.getString(TMPDB_API_MOVIE_LIST_RESULTS_TITLE));
                movie.setReleaseDate(movieJson.getString(TMPDB_API_MOVIE_LIST_RESULTS_RELEASE_DATE));
                double vote = movieJson.getDouble(TMPDB_API_MOVIE_LIST_RESULTS_VOTE_AVERAGE);
                movie.setVoteAverage(Double.valueOf(vote).floatValue());
                movie.setPosterPath(movieJson.getString(TMPDB_API_MOVIE_LIST_RESULTS_POSTER_PATH));
                movies.add(movie);
            }
        } catch (JSONException e) {
            Log.e(Utils.class.getName(), "Unable to parse content \"" +
                    responseString + "\": " + e.getMessage());
            throw new UtilsException(R.string.faild_to_parse);
        }

        return movies;
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
