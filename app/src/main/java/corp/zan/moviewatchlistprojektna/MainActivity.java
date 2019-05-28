package corp.zan.moviewatchlistprojektna;

import android.app.VoiceInteractor;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.android.volley.*;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import movieData.*;


public class MainActivity extends AppCompatActivity {

    final String API_URL = "https://api.themoviedb.org/3/movie/550?api_key=f53eae365339a0ca4c3062e895a8fb21";


    Gson gson;
    TextView textView;
    Movie m;
    MovieList mv;

    RecyclerView rc;

    searchAdapter adapter;

    Button btnSearch;
    EditText txtSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //textView = findViewById(R.id.txtTest);

        rc = findViewById(R.id.recSearch);


        findViewById(R.id.loadingPanelMain).setVisibility(View.GONE);




        m = new Movie();
        mv = new MovieList();

        txtSearch = findViewById(R.id.txtSearch);
        btnSearch = findViewById(R.id.btnSearch);

        getPopular();

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    findViewById(R.id.loadingPanelMain).setVisibility(View.VISIBLE);
                    //Toast.makeText(getApplicationContext(),txtSearch.getText().toString(),Toast.LENGTH_LONG).show();
                    findMovie(txtSearch.getText().toString());
                }catch(Exception e){
                    Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
                }
            }
        });









    }

    private Gson getGson() {
        if (gson == null)
            gson = new GsonBuilder().setPrettyPrinting().create();
        return gson;
    }


    private void findMovie(String search){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String API_URL1 = "https://api.themoviedb.org/3/search/movie?api_key=f53eae365339a0ca4c3062e895a8fb21&query=" + search;

        JsonObjectRequest request = new JsonObjectRequest(JsonRequest.Method.GET, API_URL1, (JSONObject) null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                findViewById(R.id.loadingPanelMain).setVisibility(View.GONE);
                mv = getGson().fromJson(response.toString() , MovieList.class);
                adapter = new searchAdapter(mv.getResults());

                rc.setAdapter(adapter);
                rc.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                adapter.setOnItemClickListener(new searchAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View itemView, int position) {
                        int tmp = mv.get(position).getId();
                        //Toast.makeText(MainActivity.this,tmp,Toast.LENGTH_LONG).show();
                        Intent i = new Intent(getBaseContext(),movieInfoActivity.class);
                        i.putExtra("MovieId",tmp);
                        startActivity(i);
                    }
                });
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                findViewById(R.id.loadingPanelMain).setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG);
            }
        });
        requestQueue.add(request);
    }


    private void getPopular(){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final String API_URL1 = "https://api.themoviedb.org/3/movie/popular?api_key=f53eae365339a0ca4c3062e895a8fb21";

        JsonObjectRequest request = new JsonObjectRequest(JsonRequest.Method.GET, API_URL1, (JSONObject) null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                findViewById(R.id.loadingPanelMain).setVisibility(View.GONE);
                mv = getGson().fromJson(response.toString() , MovieList.class);
                adapter = new searchAdapter(mv.getResults());

                rc.setAdapter(adapter);
                rc.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                adapter.setOnItemClickListener(new searchAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View itemView, int position) {
                        int tmp = mv.get(position).getId();
                        //Toast.makeText(MainActivity.this,tmp,Toast.LENGTH_LONG).show();
                        Intent i = new Intent(getBaseContext(),movieInfoActivity.class);
                        i.putExtra("MovieId",tmp);
                        startActivity(i);
                    }
                });
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                findViewById(R.id.loadingPanelMain).setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG);
            }
        });
        requestQueue.add(request);
    }

}
