package com.mobdeve.cait.mp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DiscoverActivity extends AppCompatActivity implements View.OnClickListener {

    private static String popularMovie = "https://api.themoviedb.org/3/movie/popular?api_key="+ BuildConfig.TMDB_API;
    private static String popularTv = "https://api.themoviedb.org/3/tv/popular?api_key="+ BuildConfig.TMDB_API;
    private static String searchMovie = "https://api.themoviedb.org/3/search/movie?api_key="+BuildConfig.TMDB_API+"&query=";
    private static String searchTv = "https://api.themoviedb.org/3/search/tv?api_key="+BuildConfig.TMDB_API+"&query=";
    private List<MovieClass> movieList;
    private List<TvClass> tvList;
    private RecyclerView recycler_Movie;
    private RecyclerView recycler_Tv;
    private com.mobdeve.cait.mp.TvAdapter TvAdapter;
    private MovieAdapter movieAdapter ;
    private TextView tabName ;
    private ImageView tabHome ;
    private ImageView tabDiscover ;
    private Intent intent ;

    private MovieClass movie ;
    private TvClass tvShow ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discover);

        buildHeader();
        movieList = new ArrayList<>();
        tvList = new ArrayList<>();


        GetDataMovie getDataMovie = new GetDataMovie();
        getDataMovie.execute();
        GetDataTv getDataTv = new GetDataTv();
        getDataTv.execute();
    }

    //build the header
    public void buildHeader(){
        this.tabHome = findViewById(R.id.img_tabHome) ;
        this.tabDiscover = findViewById(R.id.img_tabDiscover) ;
        this.tabName = findViewById(R.id.txt_tabname) ;

        tabName.setText("DISCOVER");

        tabHome.setOnClickListener(this);
        tabDiscover.setOnClickListener(this);
    }

    //header tab onclick
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_tabHome:
                intent = new Intent(getBaseContext(), MainActivity.class);
                startActivity(intent);
                break ;
            case R.id.img_tabDiscover:
                intent = new Intent(getBaseContext(), DiscoverActivity.class);
                startActivity(intent);
                break ;
        }
    }

    //get the data for movie to show in recycler_movie
    public class GetDataMovie extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {

            String current = "";

            try{
                URL url;
                HttpURLConnection urlConnection = null;

                try{
                    url = new URL(popularMovie);
                    urlConnection = (HttpURLConnection) url.openConnection();

                    InputStream inputStream = urlConnection.getInputStream();
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

                    int data = inputStreamReader.read();

                    while(data != -1){
                        current += (char) data;
                        data = inputStreamReader.read();
                    }

                    return current;

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }finally{
                    if(urlConnection != null){
                        urlConnection.disconnect();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return current;
        }

        @Override
        protected void onPostExecute(String s) {

            try{
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray =  jsonObject.getJSONArray("results");

                for(int i = 0; i < jsonArray.length() ; i++){

                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                    MovieClass model = new MovieClass();
                    model.setImg(jsonObject1.getString("poster_path"));
                    Log.d("poster",jsonObject1.getString("poster_path"));
                    model.setId(jsonObject1.getString("id"));
                    model.setName(jsonObject1.getString("original_title"));
                    model.setOverview(jsonObject1.getString("overview"));
                    model.setLanguage(jsonObject1.getString("original_language"));

                    movieList.add(model);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            dataInRecyclerMovie(movieList);
        }
    }


    //get the data for tvshows to show in recycler_tv
    public class GetDataTv extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {

            String current = "";

            try{
                URL url;
                HttpURLConnection urlConnection = null;

                try{
                    url = new URL(popularTv);
                    urlConnection = (HttpURLConnection) url.openConnection();

                    InputStream inputStream = urlConnection.getInputStream();
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

                    int data = inputStreamReader.read();

                    while(data != -1){
                        current += (char) data;
                        data = inputStreamReader.read();
                    }

                    return current;

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }finally{
                    if(urlConnection != null){
                        urlConnection.disconnect();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return current;
        }

        @Override
        protected void onPostExecute(String s) {
            try{
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray =  jsonObject.getJSONArray("results");

                for(int i = 0; i < jsonArray.length() ; i++){

                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                    TvClass model = new TvClass();
                    model.setImg(jsonObject1.getString("poster_path"));
                    model.setId(jsonObject1.getString("id"));
                    model.setName(jsonObject1.getString("name"));
                    model.setLanguage(jsonObject1.getString("original_language"));
                    model.setOverview(jsonObject1.getString("overview"));
                    tvList.add(model);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            dataInRecyclerTv(tvList);
        }
    }

    //put movie data inside recycler
    private void dataInRecyclerMovie(List<MovieClass> movieList){
        //MOVIES
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        this.movieAdapter = new MovieAdapter(this, movieList);
        this.recycler_Movie = findViewById(R.id.recycler_discMovie) ;
        recycler_Movie.setLayoutManager(layoutManager);
        recycler_Movie.setAdapter(movieAdapter);


        movieAdapter.setOnItemClickListener(new MovieAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

                Intent i = new Intent(DiscoverActivity.this,MovieViewActivity.class);
                createMovie(position);
                i.putExtra("movieParcel", movie) ;
                i.putExtra("poster_path",movieList.get(position).getImg());
                Log.d("MOVIECLICK", "onItemClick: " + position);
                Log.d("MOVIECLICK", "onItemClick: " + movie.getId());
                startActivity(i);

            }
        });
    }

    //put tvshow data in recycler
    private void dataInRecyclerTv(List<TvClass> tvList){
        //TV list
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        this.TvAdapter = new TvAdapter(this, tvList);
        this.recycler_Tv = findViewById(R.id.recycler_Current) ;
        recycler_Tv.setLayoutManager(layoutManager);
        recycler_Tv.setAdapter(TvAdapter);


        TvAdapter.setOnItemClickListener(new com.mobdeve.cait.mp.TvAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent i2 = new Intent(DiscoverActivity.this,TvViewActivity.class);
                createTV(position);
                i2.putExtra("tvParcel", tvShow) ;
                i2.putExtra("poster_path",tvList.get(position).getImg());
                startActivity(i2);
                Log.d("TVCLICK", "onItemClick: " + position);
            }
        });
    }

    //create movie object
    public void createMovie(int position){
        movie = new MovieClass() ;
        movie.setId(movieList.get(position).getId());
        movie.setName(movieList.get(position).getName());
        movie.setImg(movieList.get(position).getImg());
        movie.setOverview(movieList.get(position).getOverview());
        movie.setLanguage(movieList.get(position).getLanguage());
    }

    //create tv object
    public  void createTV(int position){
        tvShow = new TvClass() ;
        tvShow.setId(tvList.get(position).getId());
        tvShow.setName(tvList.get(position).getName());
        tvShow.setImg(tvList.get(position).getImg());
        tvShow.setOverview(tvList.get(position).getOverview());
        tvShow.setLanguage(tvList.get(position).getLanguage());
    }
}