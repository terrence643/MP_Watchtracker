package com.mobdeve.cait.mp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tabName ;
    private ImageView tabHome ;
    private ImageView tabDiscover ;
    private Intent intent ;

    private RecyclerView recycleCurrent ;
    private RecyclerView recycleTowatch ;
    private RecyclerView recyclerFinish ;
    private TMDBAdapter currentAdapter ;
    private TMDBAdapter towatchAdapter ;
    private TMDBAdapter finishAdapter ;

    private DataBaseHelper myDb;
    private List<TMDBClass> currentList ;
    private List<TMDBClass> towatchList ;
    private List<TMDBClass> finishList ;
    private List<String>tmdbIDs ;
    private List<String> tmdbTypes ;
    private List<String> tmdbStatus ;
    private TMDBClass tmdbClassObject ;

    private int indexCurrent;
    private int indexTowatch;
    private int indexFinish ;
    private int indexTVcurrent ;
    private int indexTVtowatch ;
    private int indexTVfinish ;

    private static final String TAG="MAIN//" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buildHeader();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: Nagstart");
        initLists();
        dataInRecyclerCurrent(currentList);
        dataInRecyclerTowatch(towatchList);
        dataInRecyclerFinish(finishList);

        myDb = new DataBaseHelper(this);
        getMovieData();
        populate();


    }

    //initialize the lists
    public void initLists(){
        this.currentList = new ArrayList<>() ;
        this.towatchList = new ArrayList<>() ;
        this.finishList = new ArrayList<>() ;

        this.tmdbIDs = new ArrayList<>() ;
        this.tmdbStatus = new ArrayList<>() ;
        this.tmdbTypes = new ArrayList<>() ;
    }

    //populate the lists
    public void populate(){
        int i ;
        for(i=0; i< tmdbIDs.size() ; i++ ){
            if(tmdbStatus.get(i).equals("Currently watching") && tmdbTypes.get(i).equals("Movie")){
                indexCurrent = i ;
                Log.d(TAG, "onCreate: idMovie current=" +tmdbIDs.get(i));
                GetMovieCurrent getDataMovie = new GetMovieCurrent();
                getDataMovie.execute();
            }
            if(tmdbStatus.get(i).equals("Currently watching") && tmdbTypes.get(i).equals("TV")){
                indexTVcurrent = i ;
                Log.d(TAG, "onCreate: idTV =" +tmdbIDs.get(i));
                GetTVCurrent getTv = new GetTVCurrent() ;
                getTv.execute() ;
            }
            if(tmdbStatus.get(i).equals("To watch") && tmdbTypes.get(i).equals("Movie")){
                indexTowatch = i ;
                Log.d(TAG, "onCreate: idMovie towatch =" +tmdbIDs.get(i));
                GetMovieTowatch getDataMovie = new GetMovieTowatch();
                getDataMovie.execute();
            }
            if(tmdbStatus.get(i).equals("To watch") && tmdbTypes.get(i).equals("TV")){
                indexTVtowatch = i ;
                Log.d(TAG, "onCreate: idTV =" +tmdbIDs.get(i));
                GetTVTowatch getTv = new GetTVTowatch() ;
                getTv.execute() ;
            }
            if(tmdbStatus.get(i).equals("Finished watching") && tmdbTypes.get(i).equals("Movie")){
                indexFinish = i ;
                Log.d(TAG, "onCreate: idMovie towatch =" +tmdbIDs.get(i));
                GetMovieFinish getDataMovie = new GetMovieFinish();
                getDataMovie.execute();
            }
            if(tmdbStatus.get(i).equals("Finished watching") && tmdbTypes.get(i).equals("TV")){
                indexTVfinish = i ;
                Log.d(TAG, "onCreate: idTV =" +tmdbIDs.get(i));
                GetTVFinish getTv = new GetTVFinish() ;
                getTv.execute() ;
            }

        }
    }

    //get all movie in db
    public void getMovieData(){
        Cursor cursor = myDb.getAllData() ;
        if(cursor.getCount() == 0){
            Toast.makeText(getBaseContext(), "EMPTY DATABASE!", Toast.LENGTH_SHORT).show();
        } else{
            while(cursor.moveToNext()){
                tmdbIDs.add(cursor.getString(0)) ;
                tmdbStatus.add(cursor.getString(1));
                tmdbTypes.add(cursor.getString(2));
            }
        }
    }

    //get the data for currently watching movie to show in recycler_movie
    public class GetMovieCurrent extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {

            String current = "";
            String detailsMovie = "https://api.themoviedb.org/3/movie/"+tmdbIDs.get(indexCurrent)+"?api_key="+ BuildConfig.TMDB_API ;

            try{
                URL url;
                HttpURLConnection urlConnection = null;

                try{
                    url = new URL(detailsMovie);
                    Log.d(TAG, "doInBackground: urlMovie = "+ url );
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
            Log.d(TAG, "onPostExecute: MOVIE S = " + s);
            try{
                JSONObject jsonObject = new JSONObject(s);
                Log.d(TAG, "onPostExecute: titleMovie = " + jsonObject.getString("original_title"));

                    TMDBClass model = new TMDBClass();
                    model.setImg(jsonObject.getString("poster_path"));
                    model.setId(jsonObject.getString("id"));
                    model.setName(jsonObject.getString("original_title"));
                    model.setOverview(jsonObject.getString("overview"));
                    model.setLanguage(jsonObject.getString("original_language"));
                    model.setAirdate(jsonObject.getString("release_date"));
                    model.setType("Movie");
                    currentList.add(model);

                } catch (JSONException jsonException) {
                jsonException.printStackTrace();
            }
            currentAdapter.notifyDataSetChanged();
            Log.d(TAG, "onPostExecute: CURRENTLIST SIZE after movie = " + currentList.size());
        }
    }

    //get the data for to watch movie to show in recycler_movie
    public class GetMovieTowatch extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {

            String current = "";
            String detailsMovie = "https://api.themoviedb.org/3/movie/"+tmdbIDs.get(indexTowatch)+"?api_key="+ BuildConfig.TMDB_API ;

            try{
                URL url;
                HttpURLConnection urlConnection = null;

                try{
                    url = new URL(detailsMovie);
                    Log.d(TAG, "doInBackground: urlMovie = "+ url );
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
            Log.d(TAG, "onPostExecute: MOVIE S = " + s);
            try{
                JSONObject jsonObject = new JSONObject(s);
                Log.d(TAG, "onPostExecute: titleMovie = " + jsonObject.getString("original_title"));

                TMDBClass model = new TMDBClass();
                model.setImg(jsonObject.getString("poster_path"));
                model.setId(jsonObject.getString("id"));
                model.setName(jsonObject.getString("original_title"));
                model.setOverview(jsonObject.getString("overview"));
                model.setLanguage(jsonObject.getString("original_language"));
                model.setAirdate(jsonObject.getString("release_date"));
                model.setType("Movie");
                towatchList.add(model);



            } catch (JSONException jsonException) {
                jsonException.printStackTrace();
            }
            towatchAdapter.notifyDataSetChanged();
        }
    }

    //get the data for finished watching movie to show in recycler_movie
    public class GetMovieFinish extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {

            String current = "";
            String detailsMovie = "https://api.themoviedb.org/3/movie/"+tmdbIDs.get(indexFinish)+"?api_key="+ BuildConfig.TMDB_API ;

            try{
                URL url;
                HttpURLConnection urlConnection = null;

                try{
                    url = new URL(detailsMovie);
                    Log.d(TAG, "doInBackground: urlMovie = "+ url );
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
            Log.d(TAG, "onPostExecute: MOVIE S = " + s);
            try{
                JSONObject jsonObject = new JSONObject(s);
                Log.d(TAG, "onPostExecute: titleMovie = " + jsonObject.getString("original_title"));

                TMDBClass model = new TMDBClass();
                model.setImg(jsonObject.getString("poster_path"));
                model.setId(jsonObject.getString("id"));
                model.setName(jsonObject.getString("original_title"));
                model.setOverview(jsonObject.getString("overview"));
                model.setLanguage(jsonObject.getString("original_language"));
                model.setAirdate(jsonObject.getString("release_date"));
                model.setType("Movie");
                finishList.add(model);

            } catch (JSONException jsonException) {
                jsonException.printStackTrace();
            }
            finishAdapter.notifyDataSetChanged();
        }
    }

    public class GetTVCurrent extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {

            String current = "";
            String detailsTV = "https://api.themoviedb.org/3/tv/"+tmdbIDs.get(indexTVcurrent)+"?api_key="+ BuildConfig.TMDB_API ;
            Log.d(TAG, "doInBackground: idTV = "+ tmdbIDs.get(indexTVcurrent) );
            try{
                URL url;
                HttpURLConnection urlConnection = null;

                try{
                    url = new URL(detailsTV);
                    Log.d(TAG, "doInBackground: urlTV = " + url);
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
            Log.d(TAG, "onPostExecute: TV S = " + s);
            try{
                JSONObject jsonObject = new JSONObject(s);
                Log.d(TAG, "onPostExecute: titleTV = " + jsonObject.getString("name"));

                TMDBClass model = new TMDBClass();
                model.setImg(jsonObject.getString("poster_path"));
                model.setId(jsonObject.getString("id"));
                model.setName(jsonObject.getString("name"));
                model.setOverview(jsonObject.getString("overview"));
                model.setLanguage(jsonObject.getString("original_language"));
                model.setAirdate(jsonObject.getString("first_air_date"));
                model.setType("TV");
                currentList.add(model);

            } catch (JSONException jsonException) {
                jsonException.printStackTrace();
            }
            currentAdapter.notifyDataSetChanged();
        }
    }

    public class GetTVTowatch extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {

            String current = "";
            String detailsTV = "https://api.themoviedb.org/3/tv/"+tmdbIDs.get(indexTVtowatch)+"?api_key="+ BuildConfig.TMDB_API ;
            Log.d(TAG, "doInBackground: idTV = "+ tmdbIDs.get(indexTVtowatch) );
            try{
                URL url;
                HttpURLConnection urlConnection = null;

                try{
                    url = new URL(detailsTV);
                    Log.d(TAG, "doInBackground: urlTV = " + url);
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
            Log.d(TAG, "onPostExecute: TV S = " + s);
            try{
                JSONObject jsonObject = new JSONObject(s);
                Log.d(TAG, "onPostExecute: titleTV = " + jsonObject.getString("name"));

                TMDBClass model = new TMDBClass();
                model.setImg(jsonObject.getString("poster_path"));
                model.setId(jsonObject.getString("id"));
                model.setName(jsonObject.getString("name"));
                model.setOverview(jsonObject.getString("overview"));
                model.setLanguage(jsonObject.getString("original_language"));
                model.setAirdate(jsonObject.getString("first_air_date"));
                model.setType("TV");
                towatchList.add(model);

            } catch (JSONException jsonException) {
                jsonException.printStackTrace();
            }
            towatchAdapter.notifyDataSetChanged();
        }
    }
    public class GetTVFinish extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {

            String current = "";
            String detailsTV = "https://api.themoviedb.org/3/tv/"+tmdbIDs.get(indexTVfinish)+"?api_key="+ BuildConfig.TMDB_API ;
            Log.d(TAG, "doInBackground: idTV = "+ tmdbIDs.get(indexTVfinish) );
            try{
                URL url;
                HttpURLConnection urlConnection = null;

                try{
                    url = new URL(detailsTV);
                    Log.d(TAG, "doInBackground: urlTV = " + url);
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
            Log.d(TAG, "onPostExecute: TV S = " + s);
            try{
                JSONObject jsonObject = new JSONObject(s);
                Log.d(TAG, "onPostExecute: titleTV = " + jsonObject.getString("name"));

                TMDBClass model = new TMDBClass();
                model.setImg(jsonObject.getString("poster_path"));
                model.setId(jsonObject.getString("id"));
                model.setName(jsonObject.getString("name"));
                model.setOverview(jsonObject.getString("overview"));
                model.setLanguage(jsonObject.getString("original_language"));
                model.setAirdate(jsonObject.getString("first_air_date"));
                model.setType("TV");
                finishList.add(model);

            } catch (JSONException jsonException) {
                jsonException.printStackTrace();
            }
            finishAdapter.notifyDataSetChanged();
        }
    }


    //This function loads the currently watching
    private void dataInRecyclerCurrent(List<TMDBClass> tvList) {
        //TV list
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        currentAdapter = new TMDBAdapter(getBaseContext(), tvList);
        this.recycleCurrent = findViewById(R.id.recycler_Current);
        recycleCurrent.setLayoutManager(layoutManager);
        recycleCurrent.setAdapter(currentAdapter);

        currentAdapter.setOnItemClickListener(new TMDBAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

                Intent i = new Intent(getBaseContext(), TMDBViewActivity.class);
                if(currentList.get(position).getType().equals("Movie"))
                    createMovie(position, currentList);
                else
                    createTV(position, currentList);

                i.putExtra("movieParcel", tmdbClassObject) ;
                i.putExtra("poster_path",currentList.get(position).getImg());
                i.putExtra("status", "Currently watching") ;
                Log.d(TAG, "onItemClick: " + position);
                Log.d(TAG, "onItemClick: " + tmdbClassObject.getId());

                startActivity(i);
            }
        });

    }

    //This function loads the to watch
    private void dataInRecyclerTowatch(List<TMDBClass> tvList) {
        //TV list
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        towatchAdapter = new TMDBAdapter(getBaseContext(), tvList);
        this.recycleTowatch = findViewById(R.id.recycler_Towatch);
        recycleTowatch.setLayoutManager(layoutManager);
        recycleTowatch.setAdapter(towatchAdapter);

        towatchAdapter.setOnItemClickListener(new TMDBAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

                Intent i = new Intent(getBaseContext(), TMDBViewActivity.class);
                if(towatchList.get(position).getType().equals("Movie"))
                    createMovie(position, towatchList);
                else
                    createTV(position, towatchList);

                i.putExtra("movieParcel", tmdbClassObject) ;
                i.putExtra("poster_path",towatchList.get(position).getImg());
                i.putExtra("status", "To watch") ;
                Log.d(TAG, "onItemClick: " + position);
                Log.d(TAG, "onItemClick: " + tmdbClassObject.getId());

                startActivity(i);
            }
        });
    }

    //This function loads the finished watching
    private void dataInRecyclerFinish(List<TMDBClass> tvList) {
        //TV list
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        finishAdapter = new TMDBAdapter(getBaseContext(), tvList);
        this.recyclerFinish = findViewById(R.id.recycler_Finish);
        recyclerFinish.setLayoutManager(layoutManager);
        recyclerFinish.setAdapter(finishAdapter);

        finishAdapter.setOnItemClickListener(new TMDBAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

                Intent i = new Intent(getBaseContext(), TMDBViewActivity.class);
                if(finishList.get(position).getType().equals("Movie"))
                    createMovie(position, finishList);
                else
                    createTV(position, finishList);

                i.putExtra("movieParcel", tmdbClassObject) ;
                i.putExtra("poster_path",finishList.get(position).getImg());
                i.putExtra("status", "Finished watching") ;
                Log.d(TAG, "onItemClick: " + position);
                Log.d(TAG, "onItemClick: " + tmdbClassObject.getId());

                startActivity(i);
            }
        });
    }

    //create movie object
    public void createMovie(int position, List<TMDBClass> tmdblist){
        tmdbClassObject = new TMDBClass() ;
        tmdbClassObject.setId(tmdblist.get(position).getId());
        tmdbClassObject.setName(tmdblist.get(position).getName());
        tmdbClassObject.setImg(tmdblist.get(position).getImg());
        tmdbClassObject.setOverview(tmdblist.get(position).getOverview());
        tmdbClassObject.setLanguage(tmdblist.get(position).getLanguage());
        tmdbClassObject.setAirdate(tmdblist.get(position).getAirdate());
        tmdbClassObject.setType("Movie");
    }

    //create tv object
    public  void createTV(int position, List<TMDBClass> tmdblist){
        tmdbClassObject = new TMDBClass() ;
        tmdbClassObject.setId(tmdblist.get(position).getId());
        tmdbClassObject.setName(tmdblist.get(position).getName());
        tmdbClassObject.setImg(tmdblist.get(position).getImg());
        tmdbClassObject.setOverview(tmdblist.get(position).getOverview());
        tmdbClassObject.setLanguage(tmdblist.get(position).getLanguage());
        tmdbClassObject.setAirdate(tmdblist.get(position).getAirdate());
        tmdbClassObject.setType("TV");
    }

    public void buildHeader(){
        this.tabHome = findViewById(R.id.img_tabHome) ;
        this.tabDiscover = findViewById(R.id.img_tabDiscover) ;
        this.tabName = findViewById(R.id.txt_tabname) ;

        tabName.setText("HOME");

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
        }
    }
}