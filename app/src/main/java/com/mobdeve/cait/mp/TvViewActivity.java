package com.mobdeve.cait.mp;

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
import java.util.ArrayList;
import java.util.List;

public class TvViewActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tabName ;
    private ImageView tabHome ;
    private ImageView tabDiscover ;
    private Intent intent;


    private  String lookTv = new String();

    private DataBaseHelper myDb = new DataBaseHelper(this);
    private Button btn_tvUpdate;
    private RadioButton radioButton;
    private TextView tv_TVViewTitle;
    private RecyclerView recycler_TVRecs;
    private TMDBAdapter TMDBAdapter;
    private ImageView tv_TVPoster;
    private TextView tv_TVLanguage;
    private TextView tv_TVOverview;
    private TextView txt_TVStatus;
    private TextView tv_TVAirdate ;
    private RadioGroup radioGroup ;
    private Button btn_tvAdd ;
    private String radiotext;

    private List<TMDBClass> tvList;
    private TMDBClass tvShow ;

    private static final String TAG ="TVView//" ;
    @Override
    protected void onCreate(Bundle savedInstancedState) {
        super.onCreate(savedInstancedState);
        setContentView(R.layout.activity_tv_view);

        Intent i = getIntent();
        tvShow = i.getParcelableExtra("tvParcel") ;
        Log.d(TAG, "onCreate: id " + tvShow.getId());
        buildHeader();
        buildViews();
        displayTV();

        GetTv getTv = new GetTv();
        getTv.execute();





    }

    //initialize the header
    public void buildHeader(){
        this.tabHome = findViewById(R.id.img_tabHome) ;
        this.tabDiscover = findViewById(R.id.img_tabDiscover) ;
        this.tabName = findViewById(R.id.txt_tabname) ;

        tabName.setText("TV Show Details");

        tabHome.setOnClickListener(this);
        tabDiscover.setOnClickListener(this);
    }

    //initialize the views
    public void buildViews(){
        this.tv_TVViewTitle = findViewById(R.id.tv_TVViewTitle);
        this.tv_TVLanguage = findViewById(R.id.tv_TVLanguage);
        this.tv_TVOverview = findViewById(R.id.tv_TVOverview);
        this.txt_TVStatus = findViewById(R.id.txt_TVStatus);
        this.tv_TVPoster = findViewById(R.id.tv_TVPoster);
        this.tv_TVAirdate = findViewById(R.id.tv_TVAirdate) ;
        this.radioGroup = findViewById(R.id.rg_TVgroup) ;
        this.btn_tvUpdate = findViewById(R.id.btn_tvUpdate);
        this.btn_tvAdd = findViewById(R.id.btn_tvAdd) ;

        //initialize the Lists
        this.tvList = new ArrayList<>() ;


//        set onclick(s)
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                radioButton = (RadioButton) findViewById(checkedId);
                radiotext = radioButton.getText().toString();
            }
        });
//

        btn_tvAdd.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                myDb.insertData(tvShow.getId(),radiotext, "TV");
                txt_TVStatus.setText(radiotext);
                Toast toast = Toast.makeText(getApplicationContext(),"Added",Toast.LENGTH_LONG);
                toast.show();
            }
        });
        btn_tvUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (radiotext.equalsIgnoreCase("not watching")){
                    myDb.deleteData(intent.getStringExtra("id"));
                }
                else{
                myDb.updateData(tvShow.getId(),radiotext);
                }
                txt_TVStatus.setText(radiotext);
            }
        });
    }


    public class GetTv extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {

            String current = "";
            String recommendTv =  "https://api.themoviedb.org/3/tv/"+tvShow.getId()+"/recommendations?api_key="+BuildConfig.TMDB_API;
            String similarTV = "https://api.themoviedb.org/3/tv/"+tvShow.getId()+"/similar?api_key="+BuildConfig.TMDB_API ;
            Log.d(TAG, "doInBackground: recommendtv url = " + recommendTv);
            Log.d(TAG, "doInBackground: GUMANA");
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
                    tvList.add(model);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            dataInRecyclerTv(tvList);
        }
    }


    // This function gets the TV data and displays it, if the status is empty it is automatically Not Watching
    private void displayTV(){
        Intent i = getIntent();

        tv_TVViewTitle.setText(tvShow.getName());
        tv_TVLanguage.setText(tvShow.getLanguage());
        tv_TVOverview.setText(tvShow.getOverview());
        tv_TVAirdate.setText(tvShow.getAirdate());


        Picasso.get().load("https://image.tmdb.org/t/p/w500"+i.getStringExtra("poster_path")).into(tv_TVPoster);

        if (txt_TVStatus.getText().toString().isEmpty()){
            txt_TVStatus.setText("Not Watching");
        }

    }


    //This function loads the seasons of the specified television series
    private void dataInRecyclerTv(List<TMDBClass> tvList){
        //TV list
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        TMDBAdapter = new TMDBAdapter(TvViewActivity.this, tvList);
        this.recycler_TVRecs = findViewById(R.id.recycle_TVRecs) ;
        recycler_TVRecs.setLayoutManager(layoutManager);
        recycler_TVRecs.setAdapter(TMDBAdapter);

        Log.d(TAG, "dataInRecyclerTv: NA CREATE RECYCLE");
//        Log.d(TAG, "dataInRecyclerTv: TVSHOW 0 ID = " + tvList.get(0).getId() );


        TMDBAdapter.setOnItemClickListener(new TMDBAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent i2 = new Intent(getBaseContext(),TvViewActivity.class);
                createTV(position);
                i2.putExtra("tvParcel", tvShow) ;
                i2.putExtra("poster_path",tvList.get(position).getImg());
                i2.putExtra("status", "") ;
                startActivity(i2);
                Log.d("TVCLICK", "onItemClick: " + position);
            }
        });
    }
    //create tv object
    public  void createTV(int position){
        tvShow = new TMDBClass() ;
        tvShow.setId(tvList.get(position).getId());
        tvShow.setName(tvList.get(position).getName());
        tvShow.setImg(tvList.get(position).getImg());
        tvShow.setOverview(tvList.get(position).getOverview());
        tvShow.setLanguage(tvList.get(position).getLanguage());
        tvShow.setAirdate(tvList.get(position).getAirdate());
        tvShow.setType("TV");

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
