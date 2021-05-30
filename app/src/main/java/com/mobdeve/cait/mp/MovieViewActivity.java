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
import android.widget.CheckBox;
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

public class MovieViewActivity extends AppCompatActivity implements View.OnClickListener {

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
    private RadioButton radioButton;
    private TextView tv_MovieViewTitle;
    private List<MovieClass> movieList;
    private ImageView ImageView;
    private TextView tv_Language;
    private TextView tv_Overview;
    private TextView tv_status;
    private RadioGroup radioGroup ;
    private CheckBox checkFavorites;
    private String radiotext;
    private RecyclerView recycler_Recommend ;
    private MovieAdapter movieAdapter ;
    private MovieClass movie ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_view);

        Intent i = getIntent();
        movie = i.getParcelableExtra("movieParcel") ;

        buildHeader();
        buildViews();
        displayMovie();

        GetDataMovie getDataMovie = new GetDataMovie();
        getDataMovie.execute();

//        btn_movieAdd.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//        if (myDb.insertData(i.getStringExtra("id"),radiotext)){
//            Toast toast = Toast.makeText(getApplicationContext(),"Cannot Add",Toast.LENGTH_LONG);
//        }
//        else {
//              myDb.insertData(i.getStringExtra("id"),radiotext);
//              tv_status.setText(radiotext);
//        }
//            }
//        });

    }

    //itnialize header
    public void buildHeader(){
        this.tabHome = findViewById(R.id.img_tabHome) ;
        this.tabDiscover = findViewById(R.id.img_tabDiscover) ;
        this.tabName = findViewById(R.id.txt_tabname) ;

        tabName.setText("HOME");

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
        this.ImageView = findViewById(R.id.img_MoviePoster);
        this.radioGroup = findViewById(R.id.rg_group) ;
        this.btn_movieUpdate = findViewById(R.id.btn_movieUpdate);
        this.checkFavorites = findViewById(R.id.check_favorite) ;

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                radioButton = (RadioButton) findViewById(checkedId);
                radiotext = radioButton.getText().toString();

            }
        });

        btn_movieUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                myDb.updateData(movie.getId(),radiotext);
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
            dataInMovie(movieList);
        }
    }


    //populate the recyclerview for recommended
    private void dataInMovie(List<MovieClass> movieList){
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        movieAdapter = new MovieAdapter(MovieViewActivity.this, movieList);
        this.recycler_Recommend = findViewById(R.id.recycle_Recommend) ;
        recycler_Recommend.setLayoutManager(layoutManager);
        recycler_Recommend.setAdapter(movieAdapter);


        movieAdapter.setOnItemClickListener(new MovieAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

                Intent i = new Intent(getBaseContext(),MovieViewActivity.class);
                createMovie(position);
                i.putExtra("movieParcel", movie) ;
                i.putExtra("poster_path",movieList.get(position).getImg());
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

        Picasso.get().load("https://image.tmdb.org/t/p/w500"+i.getStringExtra("poster_path")).into(ImageView);

        if (tv_status.getText().toString().isEmpty()){
            tv_status.setText("Not Watching");
        }
    }


    //create a movie object
    public void createMovie(int position){
        movie = new MovieClass() ;
        movie.setId(movieList.get(position).getId());
        movie.setName(movieList.get(position).getName());
        movie.setImg(movieList.get(position).getImg());
        movie.setOverview(movieList.get(position).getOverview());
        movie.setLanguage(movieList.get(position).getLanguage());
    }
}