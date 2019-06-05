package corp.zan.moviewatchlistprojektna;

import android.app.VoiceInteractor;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.android.volley.*;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import movieData.*;


public class MainActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 100;
    final String API_URL = "https://api.themoviedb.org/3/movie/550?api_key=f53eae365339a0ca4c3062e895a8fb21";


    Gson gson;
    TextView textView;
    Movie m;
    MovieList mv;

    RecyclerView rc;

    searchAdapter adapter;

    Button btnSearch;
    EditText txtSearch;
    private FirebaseAuth mAuth;
    FloatingActionButton btn;

    GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //textView = findViewById(R.id.txtTest);

        rc = findViewById(R.id.recSearch);


        findViewById(R.id.loadingPanelMain).setVisibility(View.GONE);


        mAuth = FirebaseAuth.getInstance();


        m = new Movie();
        mv = new MovieList();

        txtSearch = findViewById(R.id.txtSearch);
        btnSearch = findViewById(R.id.btnSearch);


        btn = findViewById(R.id.btnList);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),tabs.class);
                startActivity(i);
            }
        });

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



        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this,gso);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        if(account == null) {
            signIn();
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Hello " + mAuth.getCurrentUser().getDisplayName(), Toast.LENGTH_LONG).show();
        }



    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account){
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(getApplicationContext(), user.getEmail(), Toast.LENGTH_LONG).show();


                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(getApplicationContext(), "Ne dela", Toast.LENGTH_LONG).show();
                        }

                        // ...
                    }
                });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == 100) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, 100);
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

        try {
            JsonObjectRequest request = new JsonObjectRequest(JsonRequest.Method.GET, API_URL1, (JSONObject) null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    findViewById(R.id.loadingPanelMain).setVisibility(View.GONE);
                    mv = getGson().fromJson(response.toString(), MovieList.class);
                    adapter = new searchAdapter(mv.getResults());

                    rc.setAdapter(adapter);
                    rc.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    adapter.setOnItemClickListener(new searchAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View itemView, int position) {
                            int tmp = mv.get(position).getId();
                            //Toast.makeText(MainActivity.this,tmp,Toast.LENGTH_LONG).show();
                            Intent i = new Intent(getBaseContext(), movieInfoActivity.class);
                            i.putExtra("MovieId", tmp);
                            startActivity(i);
                        }
                    });
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    findViewById(R.id.loadingPanelMain).setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG);
                }
            });
            requestQueue.add(request);
        }catch (Error e){
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG);
        }
    }

}
