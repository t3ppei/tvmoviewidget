package com.example.movieandtvwidget.tvshow;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.movieandtvwidget.R;

import java.util.ArrayList;


public class TvShowFragment extends Fragment {
   private RecyclerView rvTvShows;
   private TvShowAdapter adapter;
   private ProgressBar progressBar;
   private TvViewModel tvViewModel;

    public TvShowFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tv_show, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressBar = view.findViewById(R.id.tvprogressBar);

        rvTvShows = view.findViewById(R.id.rv_tvshows);
        rvTvShows.setHasFixedSize(true);

        showRecyclerList();

        tvViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(TvViewModel.class);

        tvViewModel.setTvShow();
        showLoading(true);

        tvViewModel.getTvShows().observe(this, new Observer<ArrayList<TvShow>>() {
            @Override
            public void onChanged(ArrayList<TvShow> tvShows) {
                if (tvShows != null) {
                    adapter.setData(tvShows);
                    showLoading(false);
                }
            }
        });

    }

    private void showRecyclerList() {
        rvTvShows.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new TvShowAdapter();
        rvTvShows.setAdapter(adapter);

        adapter.setOnItemClickCallback(new TvShowAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(TvShow data) {
                showSelectedTvShow(data);
            }
        });
    }

    private void showSelectedTvShow(TvShow detail) {
        Intent intent = new Intent(getActivity(), DetailTvShow.class);
        intent.putExtra("KEY_EXTRA", detail);
        startActivity(intent);
    }

    private void showLoading(boolean state) {
        if (state) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

}
