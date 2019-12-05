package com.example.movieandtvwidget.movie;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.movieandtvwidget.R;

import java.util.ArrayList;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ListMoviesHolder> {
    private ArrayList<Movie> mData = new ArrayList<>();
    private OnItemClickCallback onItemClickCallback;

    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }


    public void setData (ArrayList<Movie> items) {
        mData.clear();
        mData.addAll(items);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ListMoviesHolder onCreateViewHolder(@NonNull final ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_row_movie, viewGroup, false);
        return new ListMoviesHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ListMoviesHolder holder, int position) {
        holder.bind(mData.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickCallback.onItemClicked(mData.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public class ListMoviesHolder extends RecyclerView.ViewHolder {
        ImageView imgPhoto;
        TextView tvName, tvPopular, tvDescription;

        public ListMoviesHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.txt_name);
            tvPopular = itemView.findViewById(R.id.txt_popular);
            tvDescription = itemView.findViewById(R.id.txt_description);
            imgPhoto = itemView.findViewById(R.id.img_photo);
        }


        public void bind(Movie movie) {
            Glide.with(itemView.getContext())
                    .load(movie.getPhoto())
                    .apply(new RequestOptions().override(92,92))
                    .into(imgPhoto);

            tvName.setText(movie.getName());
            tvPopular.setText(movie.getPopular());
            tvDescription.setText(movie.getDescription());
        }
    }

    public interface OnItemClickCallback {
        void onItemClicked(Movie data);
    }

}
