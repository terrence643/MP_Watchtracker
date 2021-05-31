package com.mobdeve.cait.mp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class SearchActivity extends AppCompatActivity {

    private static String searchMovie = "https://api.themoviedb.org/3/search/movie?api_key="+BuildConfig.TMDB_API+"&query="/*Insert edittext.gettext here*/;
    private static String searchTv = "https://api.themoviedb.org/3/search/tv?api_key="+BuildConfig.TMDB_API+"&query=" /*Insert edittext.gettext here*/;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        
    }
}