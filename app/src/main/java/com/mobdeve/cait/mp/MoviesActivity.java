package com.mobdeve.cait.mp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.List;

public class MoviesActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView tabName ;
    private ImageView tabHome ;
    private ImageView tabDiscover ;
    private EditText editSearch ;
    private ImageButton btnSearch ;
    private Intent intent ;

    private RecyclerView recycleLatest ;
    private TvAdapter univAdapter ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);

        buildheader();
    }

    public class GetDataTv extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            return null;
        }
    }

    //
    public void dataInLatest(List<TMDBClass> tmdbList){

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        this.univAdapter = new TvAdapter(getBaseContext(), tmdbList) ;
        this.recycleLatest = findViewById(R.id.recycler_Latest) ;
        recycleLatest.setLayoutManager(layoutManager);
        recycleLatest.setAdapter(univAdapter);

    }

    //build the header
    public void buildheader(){
        this.tabHome = findViewById(R.id.img_tabHome) ;
        this.tabDiscover = findViewById(R.id.img_tabDiscover) ;
        this.tabName = findViewById(R.id.txt_tabname) ;
        this.btnSearch = findViewById(R.id.btn_search) ;
        this.editSearch = findViewById(R.id.edit_search) ;

        tabName.setVisibility(View.GONE);
        editSearch.setVisibility(View.VISIBLE);
        btnSearch.setVisibility(View.VISIBLE);
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