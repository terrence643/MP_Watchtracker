package com.mobdeve.cait.mp;

import androidx.appcompat.app.AppCompatActivity;

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
import java.util.List;

public class MovieViewActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tabName ;
    private ImageView tabHome ;
    private ImageView tabDiscover ;
    private Intent intent ;

    myDbAdapter dbAdapter;
    private  String lookMovie = new String();
    private int status_list;
    private String status_holder;

    Button btn_movieUpdate;
    RadioButton radioButton;
    public TextView tv_MovieViewTitle;
    public List<MovieClass> movieList;
    public ImageView ImageView;
    public TextView tv_Language;
    public TextView tv_Overview;
    public TextView tv_status;
    public RadioGroup radioGroup ;
    public CheckBox checkFavorites;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_view);
        Intent i = getIntent();

        buildHeader();
        this.radioGroup = findViewById(R.id.rg_group) ;

        status_list= radioGroup.getCheckedRadioButtonId();

        btn_movieUpdate = findViewById(R.id.btn_movieUpdate);
        this.checkFavorites = findViewById(R.id.check_favorite) ;

        radioButton = (RadioButton)findViewById(status_list);
        status_holder= radioButton.getText().toString();




        i.getStringExtra("position");
        i.getStringExtra("id");
        Log.d("movie to be displayed", i.getStringExtra("id"));

        GetDataMovie getDataMovie = new GetDataMovie();
        getDataMovie.execute();

        btn_movieUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbAdapter.insertData(i.getStringExtra("id"),i.getStringExtra("original_title")
                        ,i.getStringExtra("overview"),i.getStringExtra("original_language"));
            }
        });
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

    public class GetDataMovie extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            Intent i = getIntent();
            String current = "";
            lookMovie = "https://api.themoviedb.org/3/movie/"+ i.getIntExtra("id",0)+"?api_key="+BuildConfig.TMDB_API;
            try{
                URL url;
                HttpURLConnection urlConnection = null;

                try{
                    url = new URL(lookMovie);
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
                    model.setId(jsonObject1.getString("id"));
                    model.setName(jsonObject1.getString("original_title"));
                    movieList.add(model);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            dataInMovie(movieList);
        }
    }

    private void dataInMovie(List<MovieClass> movieList){

        Intent i = getIntent();
        tv_MovieViewTitle = findViewById(R.id.tv_MovieViewTitle);
        tv_Language = findViewById(R.id.tv_Language);
        tv_Overview = findViewById(R.id.tv_Overview);
        tv_status = findViewById(R.id.tv_status);
        ImageView = findViewById(R.id.img_MoviePoster);

        tv_MovieViewTitle.setText(i.getStringExtra("name"));
        tv_Language.setText(i.getStringExtra("original_language"));
        tv_Overview.setText(i.getStringExtra("overview"));

        Picasso.get().load("https://image.tmdb.org/t/p/w500"+i.getStringExtra("poster_path")).into(ImageView);

        if (tv_status.getText().toString().isEmpty()){
            tv_status.setText("Not Watching");
        }

    }
}