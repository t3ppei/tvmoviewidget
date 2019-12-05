package com.example.movieandtvwidget.favorite;


import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movieandtvwidget.R;
import com.example.movieandtvwidget.db.FavoriteHelper;
import com.example.movieandtvwidget.db.MappingHelper;
import com.example.movieandtvwidget.movie.DetailActivity;
import com.google.android.material.snackbar.Snackbar;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Objects;



/**
 * A simple {@link Fragment} subclass.
 */
public class MovieFavFragment extends Fragment implements LoadFavoritesCallback {
    private MovieFavAdapter adapter;
    private ProgressBar progressBar;
    private FavoriteHelper favoriteHelper;
    private static final String EXTRA_STATE = "EXTRA_STATE";
    private RecyclerView rvMovies;



    public MovieFavFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movie, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressBar = view.findViewById(R.id.progressBar);

        rvMovies = view.findViewById(R.id.rv_movies);
        rvMovies.setHasFixedSize(true);
        rvMovies.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new MovieFavAdapter(getActivity());
        rvMovies.setAdapter(adapter);


        favoriteHelper = FavoriteHelper.getInstance(getContext());
        favoriteHelper.open();


        if (savedInstanceState == null) {
            new LoadFavoritesAsync(favoriteHelper, this).execute();
        } else {
            ArrayList<FavoriteItem> list = savedInstanceState.getParcelableArrayList(EXTRA_STATE);
            if (list != null) {
                adapter.setFavMovies(list);
                adapter.notifyDataSetChanged();
            }
        }



    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(EXTRA_STATE, adapter.getFavMovies());
    }

    @Override
    public void preExecute() {
        /*
        Callback yang akan dipanggil di onPreExecute Asyntask
        Memunculkan progressbar
        */
        Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void postExecute(ArrayList<FavoriteItem> favoriteItems) {
         /*
        Callback yang akan dipanggil di onPostExture Asynctask
        Menyembunyikan progressbar, kemudian isi adapter dengan data yang ada
         */
        progressBar.setVisibility(View.INVISIBLE);
        if (favoriteItems.size() > 0) {
            adapter.setFavMovies(favoriteItems);
        } else {
            adapter.setFavMovies(new ArrayList<FavoriteItem>());
            showSnackbarMessage(getString(R.string.no_fav));
        }
    }

    private class LoadFavoritesAsync extends AsyncTask<Void, Void, ArrayList<FavoriteItem>> {

        WeakReference<FavoriteHelper> weakFavoriteHelper;
        WeakReference<LoadFavoritesCallback> weakCallback;

        LoadFavoritesAsync(FavoriteHelper favoriteHelper, LoadFavoritesCallback callback) {
            weakFavoriteHelper = new WeakReference<>(favoriteHelper);
            weakCallback = new WeakReference<>(callback);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            weakCallback.get().preExecute();
        }

        @Override
        protected ArrayList<FavoriteItem> doInBackground(Void... voids) {
            Cursor dataCursor = weakFavoriteHelper.get().queryAll();
            return MappingHelper.mapCursorToArrayList(dataCursor);
        }

        @Override
        protected void onPostExecute(ArrayList<FavoriteItem> favoriteItems) {
            super.onPostExecute(favoriteItems);
            weakCallback.get().postExecute(favoriteItems);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (requestCode == DetailActivity.REQUEST_ADD) {
                if (resultCode == DetailActivity.RESULT_ADD) {
                    FavoriteItem favoriteItem = data.getParcelableExtra(DetailActivity.EXTRA_FAVORITE);

                    adapter.addItem(favoriteItem);
                    rvMovies.smoothScrollToPosition(adapter.getItemCount() - 1);
                    showSnackbarMessage(getString(R.string.add_fav1));
                }
            } else if (requestCode == DetailActivity.REQUEST_UPDATE) {
                if (resultCode == DetailActivity.RESULT_DELETE) {
                    int position = data.getIntExtra(DetailActivity.EXTRA_POSITION, 0);
                    adapter.removeItem(position);
                    adapter.notifyDataSetChanged();
                    showSnackbarMessage(getString(R.string.del_fav1));
                }
            }
        }
    }

    private void showSnackbarMessage(String message) {
        Snackbar.make(rvMovies, message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        favoriteHelper.close();
    }

    @Override
    public void onResume() {
        super.onResume();
        favoriteHelper.open();
        new LoadFavoritesAsync(favoriteHelper, this).execute();
    }
}

interface LoadFavoritesCallback {
    void preExecute();
    void postExecute(ArrayList<FavoriteItem> favoriteItems);
}
