package corp.zan.moviewatchlistprojektna;

import android.app.Application;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import movieData.MovieList;
import movieData.Result;

public class searchAdapter extends RecyclerView.Adapter<searchAdapter.ViewHolder> {
    List<Result> mv;



    public searchAdapter(List<Result> mv){
        this.mv = mv;
    }






    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }
    private OnItemClickListener listener;
    // Define the method that allows the parent activity or fragment to define the listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView txtTitl;
        public TextView txtDesc;
        public ImageView iv;


        public ViewHolder(View v) {
            super(v);

            txtTitl = v.findViewById(R.id.txtTitle);
            txtDesc = v.findViewById(R.id.txtDescription);
            iv = v.findViewById(R.id.imgPoster);


            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Triggers click upwards to the adapter on click
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(itemView, position);
                        }
                    }

                }
            });
        }
    }


    @Override
    public searchAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        // Inflate the custom layout
        View view = inflater.inflate(R.layout.search_layout, parent, false);
        // Return a new holder instance
        searchAdapter.ViewHolder viewHolder = new searchAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(searchAdapter.ViewHolder holder, int position) {
        // Get the data model based on position
        final Result trenutni = mv.get(position);

        String tmp = trenutni.getOverview();
        if(tmp.length() >= 200)
        {
            tmp = tmp.substring(0,200);
            tmp = tmp + "...";
        }


        holder.txtTitl.setText(trenutni.getOriginalTitle());
        holder.txtDesc.setText(tmp);
        Picasso.get().load("https://image.tmdb.org/t/p/w500/" + trenutni.getPosterPath()).placeholder(R.drawable.ic_launcher_foreground).into(holder.iv);
    }
    @Override
    public int getItemCount() {
        return mv.size();
    }



}
