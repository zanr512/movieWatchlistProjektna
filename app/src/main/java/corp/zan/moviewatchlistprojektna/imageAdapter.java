package corp.zan.moviewatchlistprojektna;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import movieData.Backdrop;
import movieData.Result;

public class imageAdapter extends RecyclerView.Adapter<imageAdapter.ViewHolder>  {
    List<Backdrop> list;

    public imageAdapter(List<Backdrop> l){
        this.list = l;
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
        public ImageView iv;


        public ViewHolder(View v) {
            super(v);

            iv = v.findViewById(R.id.imgImageLayoutImage);


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
    public imageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        // Inflate the custom layout
        View view = inflater.inflate(R.layout.image_layout, parent, false);
        // Return a new holder instance
        imageAdapter.ViewHolder viewHolder = new imageAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(imageAdapter.ViewHolder holder, int position) {
        // Get the data model based on position
        final Backdrop trenutni = list.get(position);




        Picasso.get().load("https://image.tmdb.org/t/p/w500/" + trenutni.getFilePath()).placeholder(R.drawable.ic_launcher_foreground).into(holder.iv);
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

}
