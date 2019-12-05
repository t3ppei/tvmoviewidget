package com.example.movieandtvwidget.favorite;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.movieandtvwidget.R;
import com.example.movieandtvwidget.movie.DetailActivity;

import java.util.ArrayList;

public class MovieFavAdapter extends RecyclerView.Adapter<MovieFavAdapter.MovieFavViewHolder> {
    private final ArrayList<FavoriteItem> favMovies = new ArrayList<>();
    private final Activity activity;


    public MovieFavAdapter(Activity activity) {
        this.activity = activity;
    }

    public ArrayList<FavoriteItem> getFavMovies() {
        return favMovies;
    }

    public void setFavMovies(ArrayList<FavoriteItem> favMovies) {
        if (favMovies.size() > 0) {
            this.favMovies.clear();
        }
        this.favMovies.addAll(favMovies);
        notifyDataSetChanged();
    }

    public void addItem(FavoriteItem favoriteItem) {
        this.favMovies.add(favoriteItem);
        notifyItemInserted(favMovies.size() - 1);
    }

    public void removeItem(int position) {
        this.favMovies.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, favMovies.size());
    }

    @NonNull
    @Override
    public MovieFavViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_movie, parent, false);
        return new MovieFavViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieFavViewHolder holder, int position) {
        holder.bind(favMovies.get(position));
        holder.cvMovie.setOnClickListener(new CustomOnItemClickListener(position, new CustomOnItemClickListener.OnItemClickCallback() {
            @Override
            public void onItemClicked(View view, int position) {
                Intent intent = new Intent(activity, DetailActivity.class);
                intent.putExtra(DetailActivity.EXTRA_POSITION, position);
                intent.putExtra(DetailActivity.EXTRA_FAVORITE, favMovies.get(position));
                activity.startActivityForResult(intent, DetailActivity.REQUEST_UPDATE);
            }
        }));
    }

    @Override
    public int getItemCount() {
        return favMovies.size();
    }

    class MovieFavViewHolder extends RecyclerView.ViewHolder {
        final ImageView imgPhoto;
        final TextView tvName, tvPopular, tvDescription;
        final CardView cvMovie;

        MovieFavViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.txt_name);
            tvPopular = itemView.findViewById(R.id.txt_popular);
            tvDescription = itemView.findViewById(R.id.txt_description);
            imgPhoto = itemView.findViewById(R.id.img_photo);
            cvMovie = itemView.findViewById(R.id.cvMovie);
        }

        public void bind(FavoriteItem favoriteItem) {
            Glide.with(itemView.getContext())
                    .load(favoriteItem.getPhoto())
                    .apply(new RequestOptions().override(92,92))
                    .into(imgPhoto);

            tvName.setText(favoriteItem.getName());
            tvPopular.setText(favoriteItem.getPopular());
            tvDescription.setText(favoriteItem.getDescription());
        }
    }


}
