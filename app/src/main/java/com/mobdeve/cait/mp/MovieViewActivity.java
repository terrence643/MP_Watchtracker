package com.mobdeve.cait.mp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

public class MovieViewActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView tabName ;
    private ImageView tabHome ;
    private ImageView tabDiscover ;

    private TextView tv_MovieViewTitle;
    private CheckBox checkFavorite ;
    private RadioGroup radioGroup ;
    private TextView tvStatus ;
    private TextView tvLanguage ;
    private TextView tvOverview ;



    private Intent intent ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_view);

        buildHeader();
    }

    public void buildHeader(){
        this.tabHome = findViewById(R.id.img_tabHome) ;
        this.tabDiscover = findViewById(R.id.img_tabDiscover) ;
        this.tabName = findViewById(R.id.txt_tabname) ;

        tabName.setText("Movie Details");

        tabHome.setOnClickListener(this);
        tabDiscover.setOnClickListener(this);
    }

    public void buildView(){
        this.tv_MovieViewTitle = findViewById(R.id.tv_MovieViewTitle) ;
        this.checkFavorite = findViewById(R.id.check_Favorite) ;
        this.radioGroup = findViewById(R.id.rg_group) ;
        this.tvStatus = findViewById(R.id.tv_Status) ;
        this.tvLanguage = findViewById(R.id.tv_Language) ;
        this.tvOverview = findViewById(R.id.tv_Overview) ;
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