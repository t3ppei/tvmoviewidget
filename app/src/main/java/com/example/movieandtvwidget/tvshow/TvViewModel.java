package com.example.movieandtvwidget.tvshow;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.movieandtvwidget.BuildConfig;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class TvViewModel extends ViewModel {
    private static final String API_KEY = BuildConfig.TMDB_API_KEY;
    private MutableLiveData<ArrayList<TvShow>> listTvShows = new MutableLiveData<>();

    void setTvShow() {
        AsyncHttpClient client = new AsyncHttpClient();
        final ArrayList<TvShow> listItems = new ArrayList<>();
        String url = "https://api.themoviedb.org/3/discover/tv?api_key=" + API_KEY + "&language=en-US";

        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    JSONObject responseObject = new JSONObject(result);
                    JSONArray list = responseObject.getJSONArray("results");


                    for (int i=0;i < 10; i++){
                        JSONObject tvShows = list.getJSONObject(i);
                        String url_photo = "https://image.tmdb.org/t/p/w185" + tvShows.getString("poster_path");
                        TvShow tvShow = new TvShow();
                        tvShow.setPhoto(url_photo);
                        tvShow.setId(tvShows.getInt("id"));
                        tvShow.setName(tvShows.getString("name"));
                        tvShow.setPopular(tvShows.getString("popularity"));
                        tvShow.setDescription(tvShows.getString("overview"));
                        listItems.add(tvShow);
                    }
                    listTvShows.postValue(listItems);
                } catch (Exception e) {
                    Log.d("Exception", e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("onFailure", error.getMessage());
            }
        });
    }

    LiveData<ArrayList<TvShow>> getTvShows() {
        return listTvShows;
    }

}
