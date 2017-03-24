package hu.janos.elek.udacity.android.movies.details;

import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import hu.janos.elek.udacity.android.movies.R;
import hu.janos.elek.udacity.android.movies.common.Utils;
import hu.janos.elek.udacity.android.movies.common.UtilsException;
import hu.janos.elek.udacity.android.movies.data.Movie;

abstract class ViewHolders extends RecyclerView.ViewHolder {
    View itemView;

    ViewHolders(View itemView) {
        super(itemView);
        this.itemView = itemView;
    }

    abstract void bind(int position);
}

class VideoViewHolder extends ViewHolders {

    private DetailsAdapter adapter;
    private TextView video_title;

    VideoViewHolder(View itemView, DetailsAdapter adapter) {
        super(itemView);
        this.adapter = adapter;

        video_title = (TextView) itemView.findViewById(R.id.video_title);
    }

    @Override
    void bind(int position) {
        video_title.setText(adapter.getVideo(position).getName());
    }
}

class PlotViewHolder extends ViewHolders {
    private TextView plotView = null;
    private Movie movie;

    PlotViewHolder(View itemView, Movie movie) {
        super(itemView);
        this.movie = movie;
    }

    void bind(int position) {
        TextView title = (TextView) itemView.findViewById(R.id.title_view);
        title.setText(movie.getTitle());
        TextView release_date = (TextView) itemView.findViewById(R.id.release_view);
        release_date.setText(movie.getReleaseDate());
        RatingBar vote = (RatingBar) itemView.findViewById(R.id.average_vote);
        vote.setRating(movie.getVoteAverage() / 10 * 5);

        plotView = (TextView) itemView.findViewById(R.id.plot_view);

        String posterPath = movie.getPosterPath();
        Picasso.with(itemView.getContext())
                .load(Utils.buildImageUrlString(posterPath))
                .into((ImageView) itemView.findViewById(R.id.details_poster));

        new GetPlotTask().execute(movie.getId());
    }

    private class GetPlotTask extends AsyncTask<Integer, Void, String> {
        @Override
        protected String doInBackground(Integer... params) {
            try {
                return Utils.getMoviePlot(params[0]);
            } catch (UtilsException e) {
//                                showError(e.getMessageId());
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
