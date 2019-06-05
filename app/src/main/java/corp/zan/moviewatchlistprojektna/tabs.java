package corp.zan.moviewatchlistprojektna;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import corp.zan.moviewatchlistprojektna.ui.main.SectionsPagerAdapter;
import movieData.WatchLists;

public class tabs extends AppCompatActivity implements watched.OnFragmentInteractionListener, toWatch.OnFragmentInteractionListener{


    WatchLists watchLists;
    RecyclerView rv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabs);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        rv = findViewById(R.id.toWatchRec);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        final FirebaseFirestore tmp = FirebaseFirestore.getInstance();
        DocumentReference docRef = tmp.collection("users").document(mAuth.getUid());

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful())
                {
                    DocumentSnapshot document = task.getResult();
                    if(document.exists())
                    {
                        watchLists = document.toObject(WatchLists.class);

                    }
                    else{

                        watchLists = new WatchLists();
                    }




                }


            }
        });

    }

    @Override
    public void onFragmentInteraction(Uri uri){
        //you can leave it empty
    }
}