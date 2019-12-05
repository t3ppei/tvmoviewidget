package com.example.movieandtvwidget.movie;

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

public class MovieViewModel extends ViewModel {
    private static final String API_KEY = BuildConfig.TMDB_API_KEY;
    private MutableLiveData<ArrayList<Movie>> listMovies = new MutableLiveData<>();

    void setMovie() {
        AsyncHttpClient client = new AsyncHttpClient();
        final ArrayList<Movie> listItems = new ArrayList<>();
        String url = "https://api.themoviedb.org/3/discover/movie?api_key=" + API_KEY + "&language=en-US";

        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    JSONObject responseObject = new JSONObject(result);
                    JSONArray list = responseObject.getJSONArray("results");


                    for (int i=0;i < 10; i++){
                        JSONObject movies = list.getJSONObject(i);
                        String url_photo = "https://image.tmdb.org/t/p/w185" + movies.getString("poster_path");
                        Movie movie = new Movie();
                        movie.setPhoto(url_photo);
                        movie.setId(movies.getInt("id"));
                        movie.setName(movies.getString("title"));
                        movie.setPopular(movies.getString("popularity"));
                        movie.setDescription(movies.getString("overview"));
                        listItems.add(movie);
                    }
                    listMovies.postValue(listItems);
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

    LiveData<ArrayList<Movie>> getMovies() {
        return listMovies;
    }

}
