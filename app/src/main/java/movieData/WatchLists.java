package movieData;

import java.util.ArrayList;
import java.util.List;

public class WatchLists {
    private ArrayList<Integer> watched = new ArrayList<Integer>();
    private ArrayList<Integer> toWatch = new ArrayList<Integer>();


    public ArrayList<Integer> getWatched() {
        return watched;
    }

    public void setWatched(ArrayList<Integer> watched) {
        this.watched = watched;
    }

    public ArrayList<Integer> getToWatch() {
        return toWatch;
    }

    public void setToWatch(ArrayList<Integer> toWatch) {
        this.toWatch = toWatch;
    }

    public void addWathed(int id){
        this.watched.add(id);
    }

    public void addToWatch(int id){
        this.toWatch.add(id);
    }


}
