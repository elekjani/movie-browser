package hu.janos.elek.udacity.android.movies;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class MovieDetailsActivity extends AppCompatActivity {

    TextView plotView = null;
    private Toast mToast;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_details);


        Intent startingIntent = getIntent();

        if (startingIntent.hasExtra(Movie.class.getName())) {
            Movie movie = (Movie) startingIntent.getSerializableExtra(Movie.class.getName());
            Log.d(this.getClass().getName(), "Title: " + movie.getTitle() + ", Release: " + movie.getReleaseDate() +
                    ", Vote: " + movie.getVoteAverage());

            TextView title = (TextView) findViewById(R.id.title_view);
            title.setText(movie.getTitle());
            TextView release_date = (TextView) findViewById(R.id.release_view);
            release_date.setText(movie.getReleaseDate());
            RatingBar vote = (RatingBar) findViewById(R.id.average_vote);
            vote.setRating(movie.getVoteAverage());

            plotView = (TextView) findViewById(R.id.plot_view);

            String posterPath = movie.getPosterPath();
            Picasso.with(this)
                    .load(Utils.buildImageUrlString(posterPath))
                    .into((ImageView) findViewById(R.id.details_poster));

            new GetPlotTask().execute(movie.getId());
        }
    }

    public void showError(int resId) {
        if (mToast != null) mToast.cancel();
        mToast = Toast.makeText(this, resId, Toast.LENGTH_LONG);
        mToast.show();
    }

    private class GetPlotTask extends AsyncTask<Integer, Void, String> {
        @Override
        protected String doInBackground(Integer... params) {
            try {
                return Utils.getMoviePlot(params[0]);
            } catch (UtilsException e) {
                showError(e.getMessageId());
                return "";
            }
        }

        @Override
        protected void onPostExecute(String plot) {
            if (plotView != null) {
                plotView.setText(plot);
            }
        }
    }
}
