package hu.janos.elek.udacity.android.movies.details;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.LinkedList;
import java.util.List;

import hu.janos.elek.udacity.android.movies.R;
import hu.janos.elek.udacity.android.movies.data.Movie;
import hu.janos.elek.udacity.android.movies.data.Video;

class DetailsAdapter extends RecyclerView.Adapter<ViewHolders> {

    private Movie movie;
    private List<Video> videos = new LinkedList<>();

    DetailsAdapter(Movie Movie) {
        this.movie = Movie;
    }

    Video getVideo(int position) {
        return videos.get(position - 1);
    }

    public ViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(viewType, null);
        switch (viewType) {
            case R.layout.movie_details_header:
                return new PlotViewHolder(view, movie);
            case R.layout.video_list:
                return new VideoViewHolder(view, this);
            default:
                return new PlotViewHolder(view, movie);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return R.layout.movie_details_header;
        } else {
            return R.layout.video_list;
        }
    }

    void addVideos(List<Video> videos) {
        this.videos.addAll(videos);
    }

    @Override
    public void onBindViewHolder(ViewHolders holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return 1 + videos.size();
    }
}
