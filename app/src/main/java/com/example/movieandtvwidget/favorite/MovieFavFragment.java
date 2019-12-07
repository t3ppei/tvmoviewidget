package com.example.movieandtvwidget.favorite;


import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
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

import com.example.movieandtvwidget.FavoriteActivity;
import com.example.movieandtvwidget.R;
import com.example.movieandtvwidget.db.DatabaseContract;
import com.example.movieandtvwidget.db.MappingHelper;
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



        HandlerThread handlerThread = new HandlerThread("DataObserver");
        handlerThread.start();
        Handler handler = new Handler(handlerThread.getLooper());

        DataObserver myObserver = new DataObserver(handler, getContext());
        getActivity().getContentResolver().registerContentObserver(DatabaseContract.MovieFavoriteColumns.CONTENT_URI, true, myObserver);

        if (savedInstanceState == null) {
            new LoadFavoritesAsync(getContext(), this).execute();
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

        Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void postExecute(ArrayList<FavoriteItem> favoriteItems) {

        progressBar.setVisibility(View.INVISIBLE);
        if (favoriteItems.size() > 0) {
            adapter.setFavMovies(favoriteItems);
        } else {
            adapter.setFavMovies(new ArrayList<FavoriteItem>());
            showSnackbarMessage(getString(R.string.no_fav));
        }
    }

    private static class LoadFavoritesAsync extends AsyncTask<Void, Void, ArrayList<FavoriteItem>> {

        private final WeakReference<Context> weakContext;
        private final WeakReference<LoadFavoritesCallback> weakCallback;

        LoadFavoritesAsync(Context context, LoadFavoritesCallback callback) {
            weakContext = new WeakReference<>(context);
            weakCallback = new WeakReference<>(callback);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            weakCallback.get().preExecute();
        }

        @Override
        protected ArrayList<FavoriteItem> doInBackground(Void... voids) {
            Context context = weakContext.get();
            Cursor dataCursor = context.getContentResolver().query(DatabaseContract.MovieFavoriteColumns.CONTENT_URI, null, null, null, null);
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

    public static class DataObserver extends ContentObserver {
        Context context;

        public DataObserver(Handler handler, Context context) {
            super(handler);
            this.context = context;
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            new LoadFavoritesAsync((FavoriteActivity) context, (LoadFavoritesCallback) context).execute();
        }

    }

}

interface LoadFavoritesCallback {
    void preExecute();
    void postExecute(ArrayList<FavoriteItem> favoriteItems);
}
