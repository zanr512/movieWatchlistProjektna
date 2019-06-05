package movieData;


import java.util.ArrayList;
import java.util.List;

public class WatchLists {
    private ArrayList<Movie> watched = new ArrayList<Movie>();
    private ArrayList<Movie> toWatch = new ArrayList<Movie>();


    public ArrayList<Movie> getWatched() {
        return watched;
    }

    public void setWatched(ArrayList<Movie> watched) {
        this.watched = watched;
    }

    public ArrayList<Movie> getToWatch() {
        return toWatch;
    }

    public void setToWatch(ArrayList<Movie> toWatch) {
        this.toWatch = toWatch;
    }

    public void addWatched(Movie id){

        this.watched.add(id);
    }

    public void addToWatch(Movie id){
        this.toWatch.add(id);
    }


    public boolean containsWatched(int id){
        for (Movie m : this.watched) {
            if(m.getId() == id)
                return true;
        }
        return false;
    }

    public boolean containsToWatch(int id){
        for (Movie m : this.toWatch) {
            if(m.getId() == id)
                return true;
        }
        return false;
    }


}
