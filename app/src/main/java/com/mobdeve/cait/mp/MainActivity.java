package com.mobdeve.cait.mp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.TextView;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

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
import java.util.concurrent.CompletableFuture;

public class MainActivity extends AppCompatActivity {

    private TabLayout tab ;
    private TabItem tabHome ;
    private TabItem tabAdd ;
    private TabItem tabDiscover ;
    private TextView tabName ;
    private ViewPager2 viewPager ;
    private FragmentAdapter fragmentAdapter ;
//
//    private static String popularMovie = "https://api.themoviedb.org/3/movie/popular?api_key="+ BuildConfig.TMDB_API;
//
//    List<movieClass> movieList;
//    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//
//        movieList = new ArrayList<>();
//        recyclerView = findViewById(R.id.recycler_Current);
//
//        GetData getData = new GetData();
//        getData.execute();

        buildView();

        FragmentManager fm = getSupportFragmentManager();
        this.fragmentAdapter = new FragmentAdapter(fm, getLifecycle()) ;
        viewPager.setAdapter(fragmentAdapter);

        tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        }) ;

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tab.selectTab(tab.getTabAt(position));

                switch (position){
                    case 0:
                        tabName.setText("Home");
                        break;
                    case 1:
                        tabName.setText("Add new list");
                        break;
                    case 2:
                        tabName.setText("Discover");
                        break;
                }
            }
        });

    }
//
//    public class GetData extends AsyncTask<String, String, String>{
//
//        @Override
//        protected String doInBackground(String... strings) {
//
//            String current = "";
//
//            try{
//                URL url;
//                HttpURLConnection urlConnection = null;
//
//                try{
//                    url = new URL(popularMovie);
//                    urlConnection = (HttpURLConnection) url.openConnection();
//
//                    InputStream inputStream = urlConnection.getInputStream();
//                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
//
//                    int data = inputStreamReader.read();
//
//                    while(data != -1){
//                        current += (char) data;
//                        data = inputStreamReader.read();
//                    }
//
//                    return current;
//
//                } catch (MalformedURLException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }finally{
//                    if(urlConnection != null){
//                        urlConnection.disconnect();
//                    }
//                }
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//            return current;
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//            try{
//                JSONObject jsonObject = new JSONObject(s);
//                JSONArray jsonArray =  jsonObject.getJSONArray("results");
//
//                for(int i = 0; i < jsonArray.length() ; i++){
//
//                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
//
//                    movieClass model = new movieClass();
//                    model.setImg(jsonObject1.getString("poster_path"));
//                    movieList.add(model);
//
//                }
//
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            dataInRecyclerView(movieList);
//        }
//    }
//
//    private void dataInRecyclerView(List<movieClass> movieList){
//
//        movieAdapter adapter = new movieAdapter(this, movieList);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setAdapter(adapter);
//    }

    public void buildView(){
        this.tab = findViewById(R.id.tab_header) ;
        this.tabHome = findViewById(R.id.tab_home) ;
        this.tabAdd = findViewById(R.id.tab_addnew) ;
        this.tabDiscover = findViewById(R.id.tab_discover) ;
        this.viewPager = findViewById(R.id.viewPager) ;
        this.tabName = findViewById(R.id.txt_tabname) ;
    }
}