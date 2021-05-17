package com.mobdeve.cait.mp;


import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
    public class FragmentHome extends Fragment {

        private static String popularMovie = "https://api.themoviedb.org/3/movie/popular?api_key="+ BuildConfig.TMDB_API;

    public FragmentHome(){
        //Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {



        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }
}