package hu.janos.elek.udacity.android.movies;

import java.io.Serializable;

class Movie implements Serializable{
    private int id;
    private String posterPath;
    private String title;
    private String releaseDate;
    private float voteAverage;
    private String overview;

    Movie(int id) {
        this.id = id;
    }

    int getId() {
        return id;
    }

    String getPosterPath() {
        return posterPath;
    }

    Movie setPosterPath(String posterPath) {
        this.posterPath = posterPath;
        return this;
    }

    String getTitle() {
        return title;
    }

    Movie setTitle(String title) {
        this.title = title;
        return this;
    }

    float getVoteAverage() {
        return voteAverage;
    }

    Movie setVoteAverage(float voteAverage) {
        this.voteAverage = voteAverage;
        return this;
    }

    String getReleaseDate() {
        return releaseDate;
    }

    Movie setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
        return this;
    }

    String getOverview() {
        return overview;
    }

    Movie setOverview(String overview) {
        this.overview = overview;
        return this;
    }
}
