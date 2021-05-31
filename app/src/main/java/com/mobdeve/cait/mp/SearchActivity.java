package com.mobdeve.cait.mp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
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

public class SearchActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tabName ;
    private ImageView tabHome ;
    private ImageView tabDiscover ;
    private EditText editSearch ;
    private ImageButton btnSearch ;
    private Intent intent ;
    private String type ;
    private String search ;

    private TMDBAdapter searchAdapter ;
    private RecyclerView recycleSearch ;
    private List<TMDBClass> searchLists ;
    private TMDBClass tmdbObject;

    private static final String TAG = "SEARCH//" ;
    private static String searchMovie = "https://api.themoviedb.org/3/search/movie?api_key="+BuildConfig.TMDB_API+"&query="/*Insert edittext.gettext here*/;
    private static String searchTv = "https://api.themoviedb.org/3/search/tv?api_key="+BuildConfig.TMDB_API+"&query=" /*Insert edittext.gettext here*/;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Intent intent = getIntent() ;
        type = intent.getStringExtra("type") ;
        search = intent.getStringExtra("search") ;
        buildheader();

        searchLists = new ArrayList<>() ;
        if(type.equals("Movie")){
            GetSearchMovie getSearchMovie = new GetSearchMovie() ;
            getSearchMovie.execute();
        }
        if(type.equals("TV")){
            GetSearchTv getSearchTv = new GetSearchTv() ;
            getSearchTv.execute() ;
        }
    }

    //get search results for movie
    public class GetSearchMovie extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {

            String current = "";
            String searchThis = searchMovie + search ;
            try{
                URL url;
                HttpURLConnection urlConnection = null;

                try{
                    url = new URL(searchThis);
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
            Log.d(TAG, "onPostExecute: EXEC");
            try{
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray =  jsonObject.getJSONArray("results");
                Log.d(TAG, "onPostExecute: size = " + jsonArray.length());
                for(int i = 0; i < jsonArray.length() ; i++){

                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    Log.d(TAG, "onPostExecute: id = " + jsonObject1.getString("original_title"));

                    TMDBClass model = new TMDBClass();
                    model.setImg(jsonObject1.getString("poster_path"));
                    model.setId(jsonObject1.getString("id"));
                    model.setName(jsonObject1.getString("original_title"));
                    model.setOverview(jsonObject1.getString("overview"));
                    model.setLanguage(jsonObject1.getString("original_language"));
                    model.setAirdate(jsonObject1.getString("release_date"));
                    model.setType("Movie");
                    searchLists.add(model);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            dataInSearch(searchLists);
        }
    }

    //get search results for tv
    public class GetSearchTv extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {

            String current = "";
            String searchThis = searchTv + search ;
            try{
                URL url;
                HttpURLConnection urlConnection = null;

                try{
                    url = new URL(searchThis);
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
            Log.d(TAG, "onPostExecute: EXEC");
            try{
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray =  jsonObject.getJSONArray("results");
                Log.d(TAG, "onPostExecute: size = " + jsonArray.length());
                for(int i = 0; i < jsonArray.length() ; i++){

                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    Log.d(TAG, "onPostExecute: name = " + jsonObject1.getString("original_language"));

                    TMDBClass model = new TMDBClass();
                    model.setImg(jsonObject1.getString("poster_path"));
                    model.setId(jsonObject1.getString("id"));
                    model.setName(jsonObject1.getString("name"));
                    model.setLanguage(jsonObject1.getString("original_language"));
                    model.setOverview(jsonObject1.getString("overview"));
                    model.setAirdate(jsonObject1.getString("first_air_date"));
                    model.setType("Tv");
                    searchLists.add(model);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            dataInSearchTv(searchLists);
        }
    }


    //put data in search recycler
    public void dataInSearch(List<TMDBClass> tmdbList){
        this.searchAdapter = new TMDBAdapter(getBaseContext(), tmdbList) ;
        this.recycleSearch = findViewById(R.id.recycle_Search) ;
        recycleSearch.setLayoutManager(new GridLayoutManager(getBaseContext(), 3));
        recycleSearch.setAdapter(searchAdapter);

        searchAdapter.setOnItemClickListener(new TMDBAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

                Intent i = new Intent(getBaseContext(), TMDBViewActivity.class);
                if(searchLists.get(position).getType().equals("Movie"))
                    createMovie(position, searchLists);
                else
                    createTV(position, searchLists);

                i.putExtra("movieParcel", tmdbObject) ;
                i.putExtra("poster_path",searchLists.get(position).getImg());
                i.putExtra("status", "") ;
                Log.d("MOVIECLICK", "onItemClick: " + position);
                Log.d("MOVIECLICK", "onItemClick: " +  searchLists.get(position).getName());
                startActivity(i);

            }
        });
    }

    //put data in search recycler
    public void dataInSearchTv(List<TMDBClass> tmdbList){
        this.searchAdapter = new TMDBAdapter(getBaseContext(), tmdbList) ;
        this.recycleSearch = findViewById(R.id.recycle_Search) ;
        recycleSearch.setLayoutManager(new GridLayoutManager(getBaseContext(), 3));
        recycleSearch.setAdapter(searchAdapter);

        searchAdapter.setOnItemClickListener(new TMDBAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

                Intent i = new Intent(getBaseContext(), TMDBViewActivity.class);
                if(searchLists.get(position).getType().equals("Movie"))
                    createMovie(position, searchLists);
                else
                    createTV(position, searchLists);

                i.putExtra("movieParcel", tmdbObject) ;
                i.putExtra("poster_path",searchLists.get(position).getImg());
                i.putExtra("status", "") ;
                Log.d("MOVIECLICK", "onItemClick: " + position);
                Log.d("MOVIECLICK", "onItemClick: " +  searchLists.get(position).getName());
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

        tabName.setVisibility(View.GONE);
        editSearch.setVisibility(View.VISIBLE);
        btnSearch.setVisibility(View.VISIBLE);
        tabHome.setOnClickListener(this);
        tabDiscover.setOnClickListener(this);
        btnSearch.setOnClickListener(this);

        editSearch.setText(search);

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