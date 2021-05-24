package com.mobdeve.cait.mp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

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

public class TvViewActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tabName ;
    private ImageView tabHome ;
    private ImageView tabDiscover ;
    private Intent intent;

    private  String lookTv = new String();

    public TextView tv_TVViewTitle;
    public List<TvClass> tvList;
    public List<seasonClass> seasonList;
    public android.widget.ImageView tv_TVPoster;
    public TextView tv_TVLanguage;
    public TextView tv_TVOverview;
    public TextView txt_TVStatus;
    public RadioGroup radioGroup ;
    public CheckBox checkFavorites;

    @Override
    protected void onCreate(Bundle savedInstancedState) {
        super.onCreate(savedInstancedState);
        setContentView(R.layout.activity_tv_view);
        Intent i = getIntent();

        buildHeader();
        this.radioGroup = findViewById(R.id.rg_TVgroup) ;
        this.checkFavorites = findViewById(R.id.check_TVFavorite);

        i.getStringExtra("position");
        i.getStringExtra("id");
        Log.d("tv to be displayed", i.getStringExtra("id"));

        GetTvMovie getTvMovie = new GetTvMovie();
        getTvMovie.execute();

    }

    public void buildHeader(){
        this.tabHome = findViewById(R.id.img_tabHome) ;
        this.tabDiscover = findViewById(R.id.img_tabDiscover) ;
        this.tabName = findViewById(R.id.txt_tabname) ;

        tabName.setText("HOME");

        tabHome.setOnClickListener(this);
        tabDiscover.setOnClickListener(this);
    }

    public class GetTvMovie extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            Intent i = getIntent();
            String current = "";
            lookTv = "https://api.themoviedb.org/3/tv/"+ i.getIntExtra("id",0)+"?api_key="+BuildConfig.TMDB_API;
            try{
                URL url;
                HttpURLConnection urlConnection = null;

                try{
                    url = new URL(lookTv);
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
                    model.setName(jsonObject1.getString("original_title"));
                    tvList.add(model);

                }
                JSONObject jsonObject2 = new JSONObject(s);
                JSONArray jsonArray1 = jsonObject2.getJSONArray("seasons");

                for(int i = 0; i <jsonArray1.length(); i++){
                    JSONObject jsonObject3 = jsonArray1.getJSONObject(i);

                    seasonClass season = new seasonClass();
                    season.setName(jsonObject3.getString("name"));
                    season.setId(jsonObject3.getInt("id"));
                    season.setAir_date(jsonObject3.getString("air_date"));
                    season.setEpisode_count(jsonObject3.getInt("episode_count"));
                    season.setOverview(jsonObject3.getString("overview"));
                    season.setPoster_path(jsonObject3.getString("poster_pasth"));
                    season.setSeason_number(jsonObject3.getInt("season_number"));

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            dataInTv(tvList);
            dataInSeason(seasonList);
        }
    }

    private void dataInSeason(List<seasonClass> seasonList){

    }

    private void dataInTv(List<TvClass> tvList){


        Intent i = getIntent();

        tv_TVViewTitle = findViewById(R.id.tv_TVViewTitle);
        tv_TVLanguage = findViewById(R.id.tv_TVLanguage);
        tv_TVOverview = findViewById(R.id.tv_TVOverview);
        txt_TVStatus = findViewById(R.id.txt_TVStatus);
        tv_TVPoster = findViewById(R.id.tv_TVPoster);

//        tv_MovieViewTitle = findViewById(R.id.tv_MovieViewTitle);
//        tv_Language = findViewById(R.id.tv_Language);
//        tv_Overview = findViewById(R.id.tv_Overview);
//        tv_status = findViewById(R.id.tv_status);
//        ImageView = findViewById(R.id.img_MoviePoster);

        tv_TVViewTitle.setText(i.getStringExtra("name"));
        tv_TVLanguage.setText(i.getStringExtra("original_language"));
        tv_TVOverview.setText(i.getStringExtra("overview"));


        Picasso.get().load("https://image.tmdb.org/t/p/w500"+i.getStringExtra("poster_path")).into(tv_TVPoster);

        if (txt_TVStatus.getText().toString().isEmpty()){
            txt_TVStatus.setText("Not Watching");
        }

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
