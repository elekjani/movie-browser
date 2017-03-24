package hu.janos.elek.udacity.android.movies;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.LinkedList;
import java.util.List;

import hu.janos.elek.udacity.android.movies.common.Constants;
import hu.janos.elek.udacity.android.movies.common.Utils;
import hu.janos.elek.udacity.android.movies.common.UtilsException;
import hu.janos.elek.udacity.android.movies.data.Movie;
import hu.janos.elek.udacity.android.movies.details.MovieDetailsActivity;

public class MainActivity extends AppCompatActivity
        implements MoviesAdapter.ListItemClickListener, MoviesAdapter.MovieLoader {

    private MoviesAdapter mAdapter;
    private Toast mToast;
    private SortOrder sortOrder = SortOrder.TOP_RATED;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView mNumbersList = (RecyclerView) findViewById(R.id.movies);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int numberOfColumns = displayMetrics.widthPixels / Constants.POSTER_WIDTH;

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, numberOfColumns);
        mNumbersList.setLayoutManager(gridLayoutManager);

        mNumbersList.setHasFixedSize(true);

        mAdapter = new MoviesAdapter(this, this);
        mNumbersList.setAdapter(mAdapter);

        Constants.TMDB_TOKEN = getString(R.string.token);

        if (!Utils.isOnline(this)) {
            showError(R.string.no_internet);
        } else {
            MovieDbTask task = new MovieDbTask();
            task.execute();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sortby_rating:
                sortOrder = SortOrder.TOP_RATED;
                break;
            case R.id.sortby_popularity:
                sortOrder = SortOrder.POPULAR;
                break;
        }

        mAdapter.reset();
        new MovieDbTask().execute();
        return true;
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        Intent detailsIntent = new Intent(MainActivity.this, MovieDetailsActivity.class);
        detailsIntent.putExtra(Movie.class.getName(), mAdapter.getMovie(clickedItemIndex));
        startActivity(detailsIntent);
    }

    void showError(int resId) {
        if (mToast != null) {
            mToast.cancel();
        }

        mToast = Toast.makeText(this, resId, Toast.LENGTH_LONG);

        mToast.show();
    }

    @Override
    public void load(int page) {
        new MovieDbTask().execute(page);
    }

    private class MovieDbTask extends AsyncTask<Integer, Void, List<Movie>> {

        private int errorResId = -1;

        @Override
        protected List<Movie> doInBackground(Integer... params) {
            List<Movie> movies = new LinkedList<>();
            try {
                if (params.length == 0) {
                    movies.addAll(Utils.getMovieList(sortOrder, 1));
                } else {
                    movies.addAll(Utils.getMovieList(sortOrder, params[0]));
                }
            } catch (UtilsException e) {
                errorResId = e.getMessageId();
            }

            return movies;
        }

        @Override
        protected void onPostExecute(List<Movie> movies) {
            if (errorResId != -1) {
                showError(errorResId);
            } else {
                mAdapter.addMovies(movies);
                mAdapter.notifyDataSetChanged();
            }
        }
    }
}
