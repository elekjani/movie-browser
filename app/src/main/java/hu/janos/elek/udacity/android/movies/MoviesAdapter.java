package hu.janos.elek.udacity.android.movies;

import hu.janos.elek.udacity.android.movies.data.Movie;
import hu.janos.elek.udacity.android.movies.data.Video;
import hu.janos.elek.udacity.android.movies.common.Utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.LinkedList;
import java.util.List;

class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.PosterViewHolder> {

    final private ListItemClickListener mOnClickListener;
    final private MovieLoader movieLoader;

    private List<Movie> movies = new LinkedList<>();
    private int page = 0;

    interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    interface MovieLoader {
        void load(int page);
    }

    MoviesAdapter(ListItemClickListener listener, MovieLoader movieLoader) {
        mOnClickListener = listener;
        this.movieLoader = movieLoader;
    }

    void addMovies(List<Movie> movies) {
        this.movies.addAll(movies);
    }

    void reset() {
        this.movies.clear();
        notifyDataSetChanged();
    }

    Movie getMovie(int index) {
        return movies.get(index);
    }

    @Override
    public PosterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.movie_list;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, viewGroup, false);
        return new PosterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PosterViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    class PosterViewHolder extends RecyclerView.ViewHolder
            implements OnClickListener {

        ImageView moviePoster;
        Context context;

        PosterViewHolder(View itemView) {
            super(itemView);

            moviePoster = (ImageView) itemView.findViewById(R.id.movie_poster);
            itemView.setOnClickListener(this);
            context = itemView.getContext();
        }

        void bind(int listIndex) {
            String posterPath = movies.get(listIndex).getPosterPath();
            Picasso.with(context)
                    .load(Utils.buildImageUrlString(posterPath))
                    .into(moviePoster);

            if (listIndex == (movies.size() - 1)) {
                movieLoader.load(++page);
            }
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);
        }
    }
}
