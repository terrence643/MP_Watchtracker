package com.mobdeve.cait.mp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

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

//This activity is to show the details of a movie or a show
public class TMDBViewActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tabName ;
    private ImageView tabHome ;
    private ImageView tabDiscover ;
    private Intent intent ;

    private static String popularMovie = "https://api.themoviedb.org/3/movie/popular?api_key="+ BuildConfig.TMDB_API;
    private  String lookMovie = new String();
    private String recommendMovie = new String();
    private int status_list;
    private String status_holder;

    private DataBaseHelper myDb = new DataBaseHelper(this);
    private Button btn_movieUpdate;
    private Button btn_movieAdd;
    private RadioButton radioButton;

    private TextView tv_MovieViewTitle;
    private List<TMDBClass> movieList;
    private ImageView ImageView;
    private TextView tv_Language;
    private TextView tv_Overview;
    private TextView tv_status;
    private TextView tv_Airdate ;
    private RadioGroup radioGroup ;
    private String radiotext;

    private RecyclerView recycler_Recommend ;
    private TMDBAdapter movieAdapter ;
    private TMDBClass movie ;
    private String status ;
    private String movieID ;
    private String type ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_view);

        Intent i = getIntent();
        movie = i.getParcelableExtra("movieParcel") ;
        status = i.getStringExtra("status") ;
        Log.d("MOVIE//", "onCreate: status = " + status);
        movieID = movie.getId() ;
        type = movie.getType() ;
        Log.d("MOVIE//", "onCreate: movieID = " + movieID);
        buildHeader();
        buildViews();
        displayMovie();

        if(movie.getType().equals("Movie")){
            GetDataMovie getDataMovie = new GetDataMovie();
            getDataMovie.execute();
        }
        if(movie.getType().equals("TV")){
            GetDataTv getDataTv = new GetDataTv() ;
            getDataTv.execute();
        }


    }

    //itnialize header
    public void buildHeader(){
        this.tabHome = findViewById(R.id.img_tabHome) ;
        this.tabDiscover = findViewById(R.id.img_tabDiscover) ;
        this.tabName = findViewById(R.id.txt_tabname) ;

        tabName.setText("Movie Details");

        tabHome.setOnClickListener(this);
        tabDiscover.setOnClickListener(this);
    }

    //initialize variables
    public void buildViews(){

        //initialize List
        this.movieList = new ArrayList<>() ;

        //initialize views
        this.tv_MovieViewTitle = findViewById(R.id.tv_MovieViewTitle);
        this.tv_Language = findViewById(R.id.tv_Language);
        this.tv_Overview = findViewById(R.id.tv_Overview);
        this.tv_status = findViewById(R.id.tv_status);
        this.tv_Airdate = findViewById(R.id.tv_Airdate) ;
        this.ImageView = findViewById(R.id.img_MoviePoster);
        this.radioGroup = findViewById(R.id.rg_group) ;
        this.btn_movieUpdate = findViewById(R.id.btn_movieUpdate);
        this.btn_movieAdd = findViewById(R.id.btn_movieAdd);

        if(status.equals("Currently watching")){
            radioGroup.clearCheck();
            radioGroup.check(R.id.radio_Current);
            btn_movieAdd.setVisibility(View.INVISIBLE);
            tv_status.setText(status);
        }
        if(status.equals("To watch")){
            radioGroup.clearCheck();
            radioGroup.check(R.id.radio_ToWatch);
            btn_movieAdd.setVisibility(View.INVISIBLE);
            tv_status.setText(status);
        }
        if(status.equals("Finished watching")){
            radioGroup.clearCheck();
            radioGroup.check(R.id.radio_Finished);
            btn_movieAdd.setVisibility(View.INVISIBLE);
            tv_status.setText(status);
        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                radioButton = (RadioButton) findViewById(checkedId);
                radiotext = radioButton.getText().toString();

            }
        });

        btn_movieAdd.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                myDb.insertData(movieID,radiotext, type);
                tv_status.setText(radiotext);
                Toast toast = Toast.makeText(getApplicationContext(),"Added",Toast.LENGTH_LONG);
                toast.show();

            }
        });

        btn_movieUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (radiotext.equalsIgnoreCase("Not watching")){
                    myDb.deleteData(movieID);
                }
                else {
                    myDb.updateData(movie.getId(), radiotext);
                }
                tv_status.setText(radiotext);
            }
        });
    }

    //set the onclick function of the tabs
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

    //get the data to be displayed in the recommended
    public class GetDataMovie extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {

            String current = "";
            recommendMovie =  "https://api.themoviedb.org/3/movie/"+movie.getId()+"/recommendations?api_key="+BuildConfig.TMDB_API;
            lookMovie = "https://api.themoviedb.org/3/movie/"+movie.getId()+"?api_key="+BuildConfig.TMDB_API;
            try{
                URL url;
                HttpURLConnection urlConnection = null;

                try{
                    url = new URL(recommendMovie);
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

                    TMDBClass model = new TMDBClass();
                    model.setImg(jsonObject1.getString("poster_path"));
                    model.setId(jsonObject1.getString("id"));
                    model.setName(jsonObject1.getString("original_title"));
                    model.setOverview(jsonObject1.getString("overview"));
                    model.setLanguage(jsonObject1.getString("original_language"));
                    model.setAirdate(jsonObject1.getString("release_date"));
                    model.setType("Movie");
                    movieList.add(model);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            dataInMovie(movieList);
        }
    }

    //get data for recommended tv shows
    public class GetDataTv extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {

            String current = "";
            String recommendTv =  "https://api.themoviedb.org/3/tv/"+movie.getId()+"/recommendations?api_key="+BuildConfig.TMDB_API;
            String similarTV = "https://api.themoviedb.org/3/tv/"+movie.getId()+"/similar?api_key="+BuildConfig.TMDB_API ;
//            Log.d(TAG, "doInBackground: recommendtv url = " + recommendTv);
//            Log.d(TAG, "doInBackground: GUMANA");
            try{
                URL url;
                HttpURLConnection urlConnection = null;

                try{
                    url = new URL(recommendTv);
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

                    TMDBClass model = new TMDBClass();
                    model.setImg(jsonObject1.getString("poster_path"));
                    model.setId(jsonObject1.getString("id"));
                    model.setName(jsonObject1.getString("name"));
                    model.setLanguage(jsonObject1.getString("original_language"));
                    model.setOverview(jsonObject1.getString("overview"));
                    model.setAirdate(jsonObject1.getString("first_air_date"));
                    model.setType("TV");
                    movieList.add(model);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            dataInMovie(movieList);
        }
    }

    //populate the recyclerview for recommended
    private void dataInMovie(List<TMDBClass> movieList){
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        movieAdapter = new TMDBAdapter(TMDBViewActivity.this, movieList);
        this.recycler_Recommend = findViewById(R.id.recycle_Recommend) ;
        recycler_Recommend.setLayoutManager(layoutManager);
        recycler_Recommend.setAdapter(movieAdapter);


        movieAdapter.setOnItemClickListener(new TMDBAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

                Intent i = new Intent(getBaseContext(), TMDBViewActivity.class);
                if(movieList.get(position).getType().equals("Movie"))
                    createMovie(position);
                if(movieList.get(position).getType().equals("TV"))
                    createTV(position);


                i.putExtra("movieParcel", movie) ;
                i.putExtra("poster_path",movieList.get(position).getImg());
                i.putExtra("status", "") ;
                Log.d("MOVIECLICK", "onItemClick: " + position);
                Log.d("MOVIECLICK", "onItemClick: " +  movieList.get(position).getName());
                startActivity(i);

            }
        });

    }


    //display the details of the selected movie
    public void displayMovie(){
        Intent i = getIntent();


        tv_MovieViewTitle.setText(movie.getName());
        tv_Language.setText(movie.getLanguage());
        tv_Overview.setText(movie.getOverview());
        tv_Airdate.setText(movie.getAirdate());


        Picasso.get().load("https://image.tmdb.org/t/p/w500"+i.getStringExtra("poster_path")).into(ImageView);

        if (tv_status.getText().toString().isEmpty()){
            tv_status.setText("Not Watching");
        }
    }

    //create a movie object
    public void createMovie(int position){
        movie = new TMDBClass() ;
        movie.setId(movieList.get(position).getId());
        movie.setName(movieList.get(position).getName());
        movie.setImg(movieList.get(position).getImg());
        movie.setOverview(movieList.get(position).getOverview());
        movie.setLanguage(movieList.get(position).getLanguage());
        movie.setAirdate(movieList.get(position).getAirdate());
        movie.setType("Movie");
    }

    //create a movie object
    public void createTV(int position){
        movie = new TMDBClass() ;
        movie.setId(movieList.get(position).getId());
        movie.setName(movieList.get(position).getName());
        movie.setImg(movieList.get(position).getImg());
        movie.setOverview(movieList.get(position).getOverview());
        movie.setLanguage(movieList.get(position).getLanguage());
        movie.setAirdate(movieList.get(position).getAirdate());
        movie.setType("TV");
    }
}