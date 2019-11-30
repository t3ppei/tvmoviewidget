package com.example.movieandtv.favorite;


import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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

import com.example.movieandtv.R;
import com.example.movieandtv.db.FavoriteHelper;
import com.example.movieandtv.db.MappingHelper;
import com.google.android.material.snackbar.Snackbar;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Objects;

import static androidx.constraintlayout.widget.Constraints.TAG;


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


        favoriteHelper = FavoriteHelper.getInstance(getActivity().getApplicationContext());
        favoriteHelper.open();

//        proses ambil data
        new LoadFavoritesAsync(favoriteHelper, this).execute();

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
            showSnackbarMessage("Tidak ada data saat ini");
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
            Log.d(TAG, "cek query");
            return MappingHelper.mapCursorToArrayList(dataCursor);
        }

        @Override
        protected void onPostExecute(ArrayList<FavoriteItem> favoriteItems) {
            super.onPostExecute(favoriteItems);
            weakCallback.get().postExecute(favoriteItems);
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
}

interface LoadFavoritesCallback {
    void preExecute();
    void postExecute(ArrayList<FavoriteItem> favoriteItems);
}
