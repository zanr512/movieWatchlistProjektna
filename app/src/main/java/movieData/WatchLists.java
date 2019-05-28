package movieData;

import java.util.List;

public class WatchLists {
    private List<Movie> watched;
    private List<Movie> toWatch;
    private List<Genre> genresList;
    private String UUID;

    public List<Movie> getWatched() {
        return watched;

    }

    public void setWatched(List<Movie> watched) {
        this.watched = watched;
    }

    public List<Movie> getToWatch() {
        return toWatch;
    }

    public void setToWatch(List<Movie> toWatch) {
        this.toWatch = toWatch;
    }

    public List<Genre> getGenresList() {
        return genresList;
    }

    public void setGenresList(List<Genre> genresList) {
        this.genresList = genresList;
    }

    public String getUUID() {
        return UUID;
    }
}
