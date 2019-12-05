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
import com.example.movieandtvwidget.tvshow.DetailTvShow;

import java.util.ArrayList;

public class TvFavAdapter extends RecyclerView.Adapter<TvFavAdapter.MovieFavViewHolder> {
    private final ArrayList<FavoriteItem> favTv = new ArrayList<>();
    private final Activity activity;


    public TvFavAdapter(Activity activity) {
        this.activity = activity;
    }

    public ArrayList<FavoriteItem> getFavTv() {
        return favTv;
    }

    public void setFavTv(ArrayList<FavoriteItem> favTv) {
        if (favTv.size() > 0) {
            this.favTv.clear();
        }
        this.favTv.addAll(favTv);
        notifyDataSetChanged();
    }

    public void addItem(FavoriteItem favoriteItem) {
        this.favTv.add(favoriteItem);
        notifyItemInserted(favTv.size() - 1);
    }

    public void removeItem(int position) {
        this.favTv.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, favTv.size());
    }

    @NonNull
    @Override
    public MovieFavViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_movie, parent, false);
        return new MovieFavViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieFavViewHolder holder, int position) {
        holder.bind(favTv.get(position));
        holder.cvMovie.setOnClickListener(new CustomOnItemClickListener(position, new CustomOnItemClickListener.OnItemClickCallback() {
            @Override
            public void onItemClicked(View view, int position) {
                Intent intent = new Intent(activity, DetailTvShow.class);
                intent.putExtra(DetailTvShow.EXTRA_POSITION, position);
                intent.putExtra(DetailTvShow.EXTRA_FAVORITE, favTv.get(position));
                activity.startActivityForResult(intent, DetailTvShow.REQUEST_UPDATE);
            }
        }));
    }

    @Override
    public int getItemCount() {
        return favTv.size();
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
// ubah ini sesuaikan dengan notes
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
