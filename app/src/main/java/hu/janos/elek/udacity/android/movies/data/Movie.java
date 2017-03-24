package hu.janos.elek.udacity.android.movies.data;

import java.io.Serializable;

public class Movie implements Serializable{
    private int id;
    private String posterPath;
    private String title;
    private String releaseDate;
    private float voteAverage;
    private String overview;

    public Movie(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public Movie setPosterPath(String posterPath) {
        this.posterPath = posterPath;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public Movie setTitle(String title) {
        this.title = title;
        return this;
    }

    public float getVoteAverage() {
        return voteAverage;
    }

    public Movie setVoteAverage(float voteAverage) {
        this.voteAverage = voteAverage;
        return this;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public Movie setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
        return this;
    }

    public String getOverview() {
        return overview;
    }

    public Movie setOverview(String overview) {
        this.overview = overview;
        return this;
    }
}
