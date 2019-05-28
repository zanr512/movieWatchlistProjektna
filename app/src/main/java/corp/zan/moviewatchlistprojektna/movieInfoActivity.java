package corp.zan.moviewatchlistprojektna;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;


import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import movieData.Images;
import movieData.Movie;
import movieData.Poster;
import movieData.Backdrop;

public class movieInfoActivity extends AppCompatActivity {
    Movie m;
    Gson gson;
    Integer movieId;

    ImageView imgPoster;
    TextView txtTitle,txtGenre,txtActor,txtRelease,txtDesc;
    FloatingActionButton actionButton;

    imageAdapter adapter;
    RecyclerView rc;

    Images img;
    private FirebaseAuth mAuth;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_info);

        rc = findViewById(R.id.imageRec);
        img = new Images();

        txtTitle = findViewById(R.id.txtTitle);
        txtActor = findViewById(R.id.txtActors);
        txtGenre = findViewById(R.id.txtGenre);
        txtRelease = findViewById(R.id.txtRelease);
        txtDesc = findViewById(R.id.txtMovieDescInfo);
        imgPoster = findViewById(R.id.imgPoster);
        actionButton = findViewById(R.id.btnAddToList);

        findViewById(R.id.constraintLayout).setVisibility(View.GONE);
        mAuth = FirebaseAuth.getInstance();


        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(movieInfoActivity.this);
                builder.setTitle("Add movie to the list");
                int selected = 0;

                String[] list = {"Watched", "Will watch"};
                builder.setSingleChoiceItems(list, selected, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(),"OK",Toast.LENGTH_LONG).show();
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        db.collection("users").document(mAuth.getUid()).set(m);
                    }
                });

                builder.setNegativeButton("Cancel", null);

                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        Bundle extra = getIntent().getExtras();
        if(extra != null){
            movieId = extra.getInt("MovieId");
            //Toast.makeText(getApplicationContext(),movieId.toString(),Toast.LENGTH_LONG).show();
            getMovieData(movieId);
            getImageData(movieId);
        }
    }

    private void getMovieData(int id){
        final String API_URL = "https://api.themoviedb.org/3/movie/" + id + "?api_key=f53eae365339a0ca4c3062e895a8fb21";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest request = new JsonObjectRequest(JsonRequest.Method.GET, API_URL, (JSONObject) null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                m = getGson().fromJson(response.toString() , Movie.class);

                txtGenre.setText(m.getGenres().replace("[","").replace("]",""));
                txtTitle.setText(m.getOriginalTitle() + " (" + m.getReleaseDate().substring(0,4) + ")");
                txtRelease.setText("Release date: " + m.getReleaseDate().substring(8,10) + "." + m.getReleaseDate().substring(5,7) + "." + m.getReleaseDate().substring(0,4));
                txtActor.setText(m.getHomepage());
                txtActor.setMovementMethod(LinkMovementMethod.getInstance());
                txtDesc.setText(m.getOverview());
                Picasso.get().load("https://image.tmdb.org/t/p/w500/" + m.getPosterPath()).placeholder(R.drawable.ic_launcher_foreground).into(imgPoster);
                findViewById(R.id.constraintLayout).setVisibility(View.VISIBLE);
                findViewById(R.id.loadingPanel).setVisibility(View.GONE);



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString(), Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(request);
    }


    private void getImageData(int id){
        final String API_URL = "https://api.themoviedb.org/3/movie/" + id + "/images?api_key=f53eae365339a0ca4c3062e895a8fb21";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest request = new JsonObjectRequest(JsonRequest.Method.GET, API_URL, (JSONObject) null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                JsonParser parser = new JsonParser();
                JsonObject element = (JsonObject)parser.parse(response.toString());
                JsonElement responseWrapper = element.get("backdrops");
                Type lType = new TypeToken<List<Backdrop>>(){}.getType();
                List<Backdrop> tmp = getGson().fromJson(responseWrapper,lType);
                tmp.sort(new Comparator<Backdrop>() {
                    @Override
                    public int compare(Backdrop o1, Backdrop o2) {
                        return o1.getVoteCount() > o2.getVoteCount() ? -1 : o1.getVoteCount() < o2.getVoteCount() ? 1 : 0;
                    }
                });
                if(tmp.size() >= 10)
                    tmp = tmp.subList(0,9);
                adapter = new imageAdapter(tmp);
                final List<Backdrop> t = tmp;
                rc.setAdapter(adapter);
                rc.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false));

                adapter.setOnItemClickListener(new imageAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View itemView, int position) {
                        String path = t.get(position).getFilePath();
                        Toast.makeText(getApplicationContext(),path,Toast.LENGTH_LONG).show();
                        Intent i = new Intent(getBaseContext(),testni.class);
                        i.putExtra("Slike", path);
                        startActivity(i);
                    }
                });

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString(), Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(request);
    }


    private Gson getGson() {
        if (gson == null)
            gson = new GsonBuilder().setPrettyPrinting().create();
        return gson;
    }
}
