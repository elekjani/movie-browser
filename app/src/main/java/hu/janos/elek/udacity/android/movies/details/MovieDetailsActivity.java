package hu.janos.elek.udacity.android.movies.details;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.LinkedList;
import java.util.List;

import hu.janos.elek.udacity.android.movies.R;
import hu.janos.elek.udacity.android.movies.common.Utils;
import hu.janos.elek.udacity.android.movies.common.UtilsException;
import hu.janos.elek.udacity.android.movies.data.Movie;
import hu.janos.elek.udacity.android.movies.data.Video;

public class MovieDetailsActivity extends AppCompatActivity {

    private Toast mToast;
    DetailsAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_details);

        RecyclerView mNumbersList = (RecyclerView) findViewById(R.id.details);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mNumbersList.setLayoutManager(linearLayoutManager);

        mNumbersList.setHasFixedSize(true);

        Intent startingIntent = getIntent();
        if (startingIntent.hasExtra(Movie.class.getName())) {
            Movie movie = (Movie) startingIntent.getSerializableExtra(Movie.class.getName());
            mAdapter = new DetailsAdapter(movie);
            mNumbersList.setAdapter(mAdapter);

            new GetVideosTask().execute(movie.getId());
        }
    }

    public void showError(int resId) {
        if (mToast != null) mToast.cancel();
        mToast = Toast.makeText(this, resId, Toast.LENGTH_LONG);
        mToast.show();
    }

    public void likeIt(View view) {
        if (mToast != null) mToast.cancel();
        mToast = Toast.makeText(this, "Liek It!", Toast.LENGTH_LONG);
        mToast.show();
    }

    private class GetVideosTask extends AsyncTask<Integer, Void, List<Video>> {
        @Override
        protected List<Video> doInBackground(Integer... params) {
            try {
                return Utils.getMovieTrailers(params[0]);
            } catch (UtilsException e) {
                Log.d(this.getClass().getName(), e.getMessage());
//                showError(e.getMessageId());
                return new LinkedList<>();
            }
        }

        @Override
        protected void onPostExecute(List<Video> videos) {
            mAdapter.addVideos(videos);
            mAdapter.notifyDataSetChanged();
        }
    }
}
