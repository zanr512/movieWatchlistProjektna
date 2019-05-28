package corp.zan.moviewatchlistprojektna;

import android.app.Application;

import movieData.WatchLists;

public class MyApplication extends Application {
    private WatchLists list = new WatchLists();


    MyApplication(){
        super();


    }


    public WatchLists getList() {
        return list;
    }

    public void setList(WatchLists list) {
        this.list = list;
    }
}
