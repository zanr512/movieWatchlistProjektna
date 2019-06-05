package corp.zan.moviewatchlistprojektna;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import movieData.WatchLists;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link toWatch.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link toWatch#newInstance} factory method to
 * create an instance of this fragment.
 */
public class toWatch extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";



    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    WatchLists watchLists;
    RecyclerView rv;
    watclistAdapter adapter;

    private OnFragmentInteractionListener mListener;

    public toWatch() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment toWatch.
     */
    // TODO: Rename and change types and number of parameters
    public static toWatch newInstance() {
        toWatch fragment = new toWatch();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_to_watch, container, false);
        //rv = getView().findViewById(R.id.toWatchRec);

        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rv = getView().findViewById(R.id.toWatchRec);

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

                    adapter = new watclistAdapter(watchLists.getToWatch());
                    rv.setAdapter(adapter);
                    rv.setLayoutManager(new LinearLayoutManager(getContext()));
                    adapter.setOnItemClickListener(new watclistAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View itemView, int position) {
                            int tmp1 = watchLists.getToWatch().get(position).getId();
                            //Toast.makeText(MainActivity.this,tmp,Toast.LENGTH_LONG).show();
                            Intent i = new Intent(getActivity().getBaseContext(), movieInfoActivity.class);
                            i.putExtra("MovieId", tmp1);
                            startActivity(i);
                        }
                    });
                }
            }
        });

    }
}
