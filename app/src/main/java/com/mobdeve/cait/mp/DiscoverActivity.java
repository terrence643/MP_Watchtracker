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
    public List<MovieClass> movieList;
    public List<TvClass> tvList;
    public RecyclerView recycler_Movie;
    public RecyclerView recycler_Tv;
    public tvAdapter TvAdapter;
    public MovieAdapter movieAdapter ;
    private TextView tabName ;
    private ImageView tabHome ;
    private ImageView tabDiscover ;
    private Intent intent ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discover);

        buildView();
        movieList = new ArrayList<>();
        tvList = new ArrayList<>();


        GetDataMovie getDataMovie = new GetDataMovie();
        getDataMovie.execute();
        GetDataTv getDataTv = new GetDataTv();
        getDataTv.execute();
    }

    public void buildView(){
        this.tabHome = findViewById(R.id.img_tabHome) ;
        this.tabDiscover = findViewById(R.id.img_tabDiscover) ;
        this.tabName = findViewById(R.id.txt_tabname) ;

        tabName.setText("DISCOVER");

        tabHome.setOnClickListener(this);
        tabDiscover.setOnClickListener(this);
    }

    public void buildRecylcler(){


    }

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
                    movieList.add(model);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            dataInRecyclerMovie(movieList);
        }
    }

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
                    tvList.add(model);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            dataInRecyclerTv(tvList);
        }
    }

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
//                Intent i = new Intent(this,movieDisplay);
//                i.putExtra("position",position);
//                startActivity(i);
                Log.d("MOVIECLICK", "onItemClick: " + position);
            }
        });
    }
    private void dataInRecyclerTv(List<TvClass> tvList){
        //TV list
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        this.TvAdapter = new tvAdapter(this, tvList);
        this.recycler_Tv = findViewById(R.id.recycler_Current) ;
        recycler_Tv.setLayoutManager(layoutManager);
        recycler_Tv.setAdapter(TvAdapter);


        TvAdapter.setOnItemClickListener(new tvAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
//                Intent i = new Intent(this,tvDisplay);
//                i.putExtra("position",position);
//                startActivity(i);
                Log.d("TVCLICK", "onItemClick: " + position);
            }
        });
    }
}