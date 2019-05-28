package corp.zan.moviewatchlistprojektna;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;

public class testni extends AppCompatActivity {

    PhotoView tmp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testni);

        tmp = findViewById(R.id.photo_view);


    }

    @Override
    protected void onResume() {
        super.onResume();
        Bundle extra = getIntent().getExtras();
        if(extra != null){
            String path = extra.getString("Slike");
            //Toast.makeText(getApplicationContext(),movieId.toString(),Toast.LENGTH_LONG).show();

            showImage(path);
        }
    }

    private void showImage(String path){
        Picasso.get().load("https://image.tmdb.org/t/p/w500/" + path).placeholder(R.drawable.ic_launcher_foreground).into(tmp);
    }
}
