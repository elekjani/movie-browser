package hu.janos.elek.udacity.android.movies;

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

import static hu.janos.elek.udacity.android.movies.Constants.*;

class Utils {

    static String buildImageUrlString(String imagePath) {
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

    static String getMoviePlot(int movieId) throws UtilsException {
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

    static List<Movie> getMovieList(SortOrder sortOrder, int page) throws UtilsException {
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
                movie.setVoteAverage(
                        Double.valueOf(movieJson.getDouble(TMPDB_API_MOVIE_LIST_RESULTS_VOTE_AVERAGE)
                        ).floatValue());
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

    static boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}

class UtilsException extends Exception {
    private int resId;

    UtilsException(int resId) {
        super("Some utils method failed.");
        this.resId = resId;
    }

    public int getMessageId() {
        return resId;
    }
}
