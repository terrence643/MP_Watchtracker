package com.mobdeve.cait.mp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
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

public class MoviesActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView tabName ;
    private ImageView tabHome ;
    private ImageView tabDiscover ;
    private EditText editSearch ;
    private ImageButton btnSearch ;
    private TextView txt_Upcoming ;
    private Intent intent ;
    private String type ;

    private RecyclerView recycleLatest ;
    private RecyclerView recycleTop ;
    private RecyclerView recyclerUpcoming;
    private TMDBAdapter latestAdapter ;
    private TMDBAdapter topAdapter ;
    private TMDBAdapter upcomingAdapter ;
    private List<TMDBClass> latestLists ;
    private List<TMDBClass> topLists ;
    private List<TMDBClass> upcomingLists ;
    private TMDBClass tmdbObject ;



    private static final String TAG = "ACTIVITYMOVIE" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);

        buildheader();
        initLists();

        Intent intent = getIntent() ;
         type = intent.getStringExtra("type") ;

        Log.d("", "onCreate: type= "+ type);

        if(type.equals("Movie")){
            GetLatestDataMovie getLatestDataMovie = new GetLatestDataMovie() ;
            getLatestDataMovie.execute();
            GetTopDataMovie getTopDataMovie = new GetTopDataMovie() ;
            getTopDataMovie.execute() ;
            GetUpcomingDataMovie getUpcomingDataMovie = new GetUpcomingDataMovie() ;
            getUpcomingDataMovie.execute() ;
        }
        if(type.equals("TV")){
            GetLatestDataTv getLatestDataTv = new GetLatestDataTv() ;
            getLatestDataTv.execute();
            GetTopDataTv getTopDataTv = new GetTopDataTv() ;
            getTopDataTv.execute() ;
            txt_Upcoming.setVisibility(View.GONE);

        }

    }

    //initialize lists
    public void initLists(){
        this.latestLists = new ArrayList<>() ;
        this.topLists = new ArrayList<>() ;
        this.upcomingLists = new ArrayList<>() ;
    }

    //get the data to be displayed
    public class GetLatestDataMovie extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {

            String current = "";
            String latestMovie =  "https://api.themoviedb.org/3/movie/now_playing?api_key="+BuildConfig.TMDB_API;


            try{
                URL url;
                HttpURLConnection urlConnection = null;

                try{
                    url = new URL(latestMovie);
                    Log.d(TAG, "doInBackground: url = " + url);
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
                Log.d(TAG, "onPostExecute: jsonArraySIZE= " + jsonArray.length());

                for(int i = 0; i < jsonArray.length() ; i++){

                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                    TMDBClass model = new TMDBClass();
                    model.setImg(jsonObject1.getString("poster_path"));
                    model.setId(jsonObject1.getString("id"));
                    model.setName(jsonObject1.getString("original_title"));
                    Log.d("ACTIVITYMOVIE", "onPostExecute: title =" + jsonObject1.getString("original_title"));
                    model.setOverview(jsonObject1.getString("overview"));
                    model.setLanguage(jsonObject1.getString("original_language"));
                    model.setAirdate(jsonObject1.getString("release_date"));
                    model.setType("Movie");
                    latestLists.add(model);
                    Log.d("ACTIVITYMOVIE", "onPostExecute: latestlist size =" + latestLists.size());

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            dataInLatest(latestLists);
        }
    }

    //get the data to be displayed
    public class GetTopDataMovie extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {

            String current = "";
            String topMovie =  "https://api.themoviedb.org/3/movie/top_rated?api_key="+BuildConfig.TMDB_API;

            try{
                URL url;
                HttpURLConnection urlConnection = null;

                try{
                    url = new URL(topMovie);
                    Log.d(TAG, "doInBackground: url = " + url);
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
                Log.d(TAG, "onPostExecute: jsonArraySIZE= " + jsonArray.length());

                for(int i = 0; i < jsonArray.length() ; i++){

                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                    TMDBClass model = new TMDBClass();
                    model.setImg(jsonObject1.getString("poster_path"));
                    model.setId(jsonObject1.getString("id"));
                    model.setName(jsonObject1.getString("original_title"));
                    model.setOverview(jsonObject1.getString("overview"));
                    model.setLanguage(jsonObject1.getString("original_language"));
                    model.setAirdate(jsonObject1.getString("release_date"));
                    model.setType("Movie");
                    topLists.add(model);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            dataInTop(topLists);
        }
    }

    //get the data to be displayed
    public class GetUpcomingDataMovie extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {

            String current = "";
            String upMovie =  "https://api.themoviedb.org/3/movie/upcoming?api_key="+BuildConfig.TMDB_API;

            try{
                URL url;
                HttpURLConnection urlConnection = null;

                try{
                    url = new URL(upMovie);
                    Log.d(TAG, "doInBackground: url = " + url);
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
                Log.d(TAG, "onPostExecute: jsonArraySIZE= " + jsonArray.length());

                for(int i = 0; i < jsonArray.length() ; i++){

                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                    TMDBClass model = new TMDBClass();
                    model.setImg(jsonObject1.getString("poster_path"));
                    model.setId(jsonObject1.getString("id"));
                    model.setName(jsonObject1.getString("original_title"));
                    model.setOverview(jsonObject1.getString("overview"));
                    model.setLanguage(jsonObject1.getString("original_language"));
                    model.setAirdate(jsonObject1.getString("release_date"));
                    model.setType("Movie");
                    upcomingLists.add(model);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            dataInUpcoming(upcomingLists);
        }
    }

    public class GetLatestDataTv extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {

            String current = "";
            String latestMovie =  "https://api.themoviedb.org/3/tv/airing_today?api_key="+BuildConfig.TMDB_API;

            try{
                URL url;
                HttpURLConnection urlConnection = null;

                try{
                    url = new URL(latestMovie);
                    Log.d(TAG, "doInBackground: url = " + url);
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
                Log.d(TAG, "onPostExecute: jsonArraySIZE= " + jsonArray.length());

                for(int i = 0; i < jsonArray.length() ; i++){

                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                    TMDBClass model = new TMDBClass();
                    model.setImg(jsonObject1.getString("poster_path"));
                    model.setId(jsonObject1.getString("id"));
                    model.setName(jsonObject1.getString("name"));
                    model.setOverview(jsonObject1.getString("overview"));
                    model.setLanguage(jsonObject1.getString("original_language"));
                    model.setAirdate(jsonObject1.getString("first_air_date"));
                    model.setType("TV");
                    latestLists.add(model);
                    Log.d("ACTIVITYMOVIE", "onPostExecute: latestlist size =" + latestLists.size());

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            dataInLatest(latestLists);
        }
    }

    //get the data to be displayed
    public class GetTopDataTv extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {

            String current = "";
            String topMovie =  "https://api.themoviedb.org/3/tv/top_rated?api_key="+BuildConfig.TMDB_API;

            try{
                URL url;
                HttpURLConnection urlConnection = null;

                try{
                    url = new URL(topMovie);
                    Log.d(TAG, "doInBackground: url = " + url);
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
                Log.d(TAG, "onPostExecute: jsonArraySIZE= " + jsonArray.length());

                for(int i = 0; i < jsonArray.length() ; i++){

                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                    TMDBClass model = new TMDBClass();
                    model.setImg(jsonObject1.getString("poster_path"));
                    model.setId(jsonObject1.getString("id"));
                    model.setName(jsonObject1.getString("name"));
                    model.setOverview(jsonObject1.getString("overview"));
                    model.setLanguage(jsonObject1.getString("original_language"));
                    model.setAirdate(jsonObject1.getString("first_air_date"));
                    model.setType("TV");
                    topLists.add(model);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            dataInTop(topLists);
        }
    }

    //put data in latest recycler
    public void dataInLatest(List<TMDBClass> tmdbList){

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        this.latestAdapter = new TMDBAdapter(getBaseContext(), tmdbList) ;
        this.recycleLatest = findViewById(R.id.recycler_Latest) ;
        recycleLatest.setLayoutManager(layoutManager);
        recycleLatest.setAdapter(latestAdapter);

        latestAdapter.setOnItemClickListener(new TMDBAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

                Intent i = new Intent(getBaseContext(), TMDBViewActivity.class);
                if(latestLists.get(position).getType().equals("Movie"))
                    createMovie(position, latestLists);
                else
                    createTV(position, latestLists);

                i.putExtra("movieParcel", tmdbObject) ;
                i.putExtra("poster_path",latestLists.get(position).getImg());
                i.putExtra("status", "") ;
                Log.d("MOVIECLICK", "onItemClick: " + position);
                Log.d("MOVIECLICK", "onItemClick: " +  latestLists.get(position).getName());
                startActivity(i);

            }
        });
    }

    //put data in Top recycler
    public void dataInTop(List<TMDBClass> tmdbList){

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        this.topAdapter = new TMDBAdapter(getBaseContext(), tmdbList) ;
        this.recycleTop = findViewById(R.id.recycler_TopRated) ;
        recycleTop.setLayoutManager(layoutManager);
        recycleTop.setAdapter(topAdapter);

        topAdapter.setOnItemClickListener(new TMDBAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

                Intent i = new Intent(getBaseContext(), TMDBViewActivity.class);
                if(topLists.get(position).getType().equals("Movie"))
                    createMovie(position, topLists);
                else
                    createTV(position, topLists);

                i.putExtra("movieParcel", tmdbObject) ;
                i.putExtra("poster_path",topLists.get(position).getImg());
                i.putExtra("status", "") ;
                Log.d("MOVIECLICK", "onItemClick: " + position);
                Log.d("MOVIECLICK", "onItemClick: " +  topLists.get(position).getName());
                startActivity(i);

            }
        });
    }

    //put data in upcoming recycler
    public void dataInUpcoming(List<TMDBClass> tmdbList){

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        this.upcomingAdapter = new TMDBAdapter(getBaseContext(), tmdbList) ;
        this.recyclerUpcoming = findViewById(R.id.recycler_Upcoming) ;
        recyclerUpcoming.setLayoutManager(layoutManager);
        recyclerUpcoming.setAdapter(upcomingAdapter);

        upcomingAdapter.setOnItemClickListener(new TMDBAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

                Intent i = new Intent(getBaseContext(), TMDBViewActivity.class);
                if(upcomingLists.get(position).getType().equals("Movie"))
                    createMovie(position, upcomingLists);
                else
                    createTV(position, upcomingLists);

                i.putExtra("movieParcel", tmdbObject) ;
                i.putExtra("poster_path",upcomingLists.get(position).getImg());
                i.putExtra("status", "") ;
                startActivity(i);

            }
        });
    }

    //create movie object
    public void createMovie(int position, List<TMDBClass> movieList){
        tmdbObject = new TMDBClass() ;
        tmdbObject.setId(movieList.get(position).getId());
        tmdbObject.setName(movieList.get(position).getName());
        tmdbObject.setImg(movieList.get(position).getImg());
        tmdbObject.setOverview(movieList.get(position).getOverview());
        tmdbObject.setLanguage(movieList.get(position).getLanguage());
        tmdbObject.setAirdate(movieList.get(position).getAirdate());
        tmdbObject.setType("Movie");
    }

    //create tv object
    public  void createTV(int position, List<TMDBClass> movieList){
        tmdbObject = new TMDBClass() ;
        tmdbObject.setId(movieList.get(position).getId());
        tmdbObject.setName(movieList.get(position).getName());
        tmdbObject.setImg(movieList.get(position).getImg());
        tmdbObject.setOverview(movieList.get(position).getOverview());
        tmdbObject.setLanguage(movieList.get(position).getLanguage());
        tmdbObject.setAirdate(movieList.get(position).getAirdate());
        tmdbObject.setType("TV");
    }

    //build the header
    public void buildheader(){
        this.tabHome = findViewById(R.id.img_tabHome) ;
        this.tabDiscover = findViewById(R.id.img_tabDiscover) ;
        this.tabName = findViewById(R.id.txt_tabname) ;
        this.btnSearch = findViewById(R.id.btn_search) ;
        this.editSearch = findViewById(R.id.edit_search) ;
        this.txt_Upcoming = findViewById(R.id.txt_Upcoming) ;

        tabName.setVisibility(View.GONE);
        editSearch.setVisibility(View.VISIBLE);
        btnSearch.setVisibility(View.VISIBLE);
        btnSearch.setOnClickListener(this);
        tabHome.setOnClickListener(this);
        tabDiscover.setOnClickListener(this);

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
            case R.id.btn_search:
                intent = new Intent(getBaseContext(), SearchActivity.class) ;
                intent.putExtra("search", editSearch.getText().toString()) ;
                intent.putExtra("type", type) ;
                startActivity(intent);
                break;
        }

    }
}