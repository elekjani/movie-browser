package hu.janos.elek.udacity.android.movies.details;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import hu.janos.elek.udacity.android.movies.R;

class DetailsAdapter extends RecyclerView.Adapter<ViewHolders> {

    private Intent startingIntent;

    DetailsAdapter(Intent startingIntent) {
        this.startingIntent = startingIntent;
    }

    public ViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(viewType, null);
        switch (viewType) {
            default:
                return new PlotViewHolder(view, startingIntent);
//            case R.layout.movie_details_header:
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

    @Override
    public void onBindViewHolder(ViewHolders holder, int position) {
        holder.bind();
    }

    @Override
    public int getItemCount() {
        return 1;
    }
}
