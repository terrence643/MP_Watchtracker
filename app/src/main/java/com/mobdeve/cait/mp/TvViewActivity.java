package com.mobdeve.cait.mp;

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

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

    DataBaseHelper myDb = new DataBaseHelper(this);
    Button btn_tvUpdate;
    public RadioButton radioButton;
    public TextView tv_TVViewTitle;
    public RecyclerView seasonRecycler;
    public seasonAdapter seasonAdapter;
    public List<TvClass> tvList;
    public List<seasonClass> seasonList;
    public android.widget.ImageView tv_TVPoster;
    public TextView tv_TVLanguage;
    public TextView tv_TVOverview;
    public TextView txt_TVStatus;
    public RadioGroup radioGroup ;
    public CheckBox checkFavorites;
    public String radiotext;

    @Override
    protected void onCreate(Bundle savedInstancedState) {
        super.onCreate(savedInstancedState);
        setContentView(R.layout.activity_tv_view);
        Intent i = getIntent();

        buildHeader();
        this.radioGroup = findViewById(R.id.rg_TVgroup) ;
        btn_tvUpdate = findViewById(R.id.btn_tvUpdate);
        this.checkFavorites = findViewById(R.id.check_TVFavorite);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                radioButton = (RadioButton) findViewById(checkedId);
                radiotext = radioButton.getText().toString();
            }
        });

        i.getStringExtra("position");
        i.getStringExtra("id");
        Log.d("tv to be displayed", i.getStringExtra("id"));

        GetTv getTv = new GetTv();
        getTv.execute();

        //        btn_tvAdd.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//
//                myDb.insertData(i.getStringExtra("id"),radiotext);
//                txt_TVStatus.setText(radiotext);
//            }
//        });

        btn_tvUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDb.updateData(i.getStringExtra("id"),radiotext);
                txt_TVStatus.setText(radiotext);
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

    public class GetTv extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            Intent i = getIntent();
            String current = "";
            lookTv = "https://api.themoviedb.org/3/tv/"+ i.getStringExtra("id")+"?api_key="+BuildConfig.TMDB_API;
            Log.d("looktv",lookTv);
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
            Log.d("logdbeforetry",s);
            try{
                JSONObject jsonObject = new JSONObject(s);

                JSONArray jsonArray =  jsonObject.getJSONArray("seasons");
                Log.d("logd", jsonArray.toString());
                for(int i = 0; i < jsonArray.length() ; i++){

                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                    TvClass model = new TvClass();
                    seasonClass seasonList = new seasonClass();
                    model.setImg(jsonObject1.getString("poster_path"));
                    model.setId(jsonObject1.getString("id"));
                    model.setName(jsonObject1.getString("original_title"));
                    tvList.add(model);

                    seasonList.setSeason_number(jsonObject1.getInt("season_number"));
                    seasonList.setPoster_path(jsonObject1.getString("poster_path"));
                    seasonList.setOverview(jsonObject1.getString("overview"));
                    seasonList.setAir_date(jsonObject1.getString("air_date"));
                    seasonList.setEpisode_count(jsonObject1.getInt("episode_count"));
                    seasonList.setId(jsonObject1.getInt("id"));
                    seasonList.setName(jsonObject1.getString("name"));

                    Log.d("tvlisthere",tvList.toString());
                }


            } catch (JSONException e) {
                Log.d("logdcatch",e.toString());
                e.printStackTrace();
            }
            Log.d("logdafter","helloafter");
            dataInTv(tvList);
            seasonRecyclerView(seasonList);
        }
    }


    // This function gets the TV data and displays it, if the status is empty it is automatically Not Watching
    private void dataInTv(List<TvClass> tvList){


        Intent i = getIntent();

        tv_TVViewTitle = findViewById(R.id.tv_TVViewTitle);
        tv_TVLanguage = findViewById(R.id.tv_TVLanguage);
        tv_TVOverview = findViewById(R.id.tv_TVOverview);
        txt_TVStatus = findViewById(R.id.txt_TVStatus);
        tv_TVPoster = findViewById(R.id.tv_TVPoster);


        tv_TVViewTitle.setText(i.getStringExtra("name"));
        tv_TVLanguage.setText(i.getStringExtra("original_language"));
        tv_TVOverview.setText(i.getStringExtra("overview"));


        Picasso.get().load("https://image.tmdb.org/t/p/w500"+i.getStringExtra("poster_path")).into(tv_TVPoster);

        if (txt_TVStatus.getText().toString().isEmpty()){
            txt_TVStatus.setText("Not Watching");
        }

    }


    //This function loads the seasons of the specified television series
    private void seasonRecyclerView(List<seasonClass> seasonList){
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        this.seasonAdapter = new seasonAdapter(this, seasonList);
        this.seasonRecycler = findViewById(R.id.recycle_TVSeasons) ;
        seasonRecycler.setLayoutManager(layoutManager);
        seasonRecycler.setAdapter(seasonAdapter);
    }

    // This function allows for the swapping between main activity and discover activity
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
