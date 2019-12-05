package com.example.movieandtvwidget.tvshow;

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

public class TvShowAdapter extends RecyclerView.Adapter<TvShowAdapter.ListTvShowsHolder>  {
    public ArrayList<TvShow> mData = new ArrayList<>();
    private OnItemClickCallback onItemClickCallback;

    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    public void setData (ArrayList<TvShow> items) {
        mData.clear();
        mData.addAll(items);
        notifyDataSetChanged();
    }

    public interface OnItemClickCallback {
        void onItemClicked(TvShow data);
    }


    @NonNull
    @Override
    public ListTvShowsHolder onCreateViewHolder(@NonNull final ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_row_tvshow, viewGroup, false);
        return new ListTvShowsHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final ListTvShowsHolder holder, int position) {
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

    public class ListTvShowsHolder extends RecyclerView.ViewHolder {
        ImageView imgPhoto;
        TextView tvName, tvPopular, tvDescription;

        public ListTvShowsHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.txt_name);
            tvPopular = itemView.findViewById(R.id.txt_popular);
            tvDescription = itemView.findViewById(R.id.txt_description);
            imgPhoto = itemView.findViewById(R.id.img_photo);
        }


        public void bind(TvShow tvShow) {
            Glide.with(itemView.getContext())
                    .load(tvShow.getPhoto())
                    .apply(new RequestOptions().override(92,92))
                    .into(imgPhoto);

            tvName.setText(tvShow.getName());
            tvPopular.setText(tvShow.getPopular());
            tvDescription.setText(tvShow.getDescription());
        }
    }


}
