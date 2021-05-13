package com.mobdeve.cait.mp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

    private TabLayout tab ;
    private TabItem tabHome ;
    private TabItem tabAdd ;
    private TabItem tabDiscover ;
    private TextView tabName ;
    private ViewPager2 viewPager ;
    private FragmentAdapter fragmentAdapter ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

    public void buildView(){
        this.tab = findViewById(R.id.tab_header) ;
        this.tabHome = findViewById(R.id.tab_home) ;
        this.tabAdd = findViewById(R.id.tab_addnew) ;
        this.tabDiscover = findViewById(R.id.tab_discover) ;
        this.viewPager = findViewById(R.id.viewPager) ;
        this.tabName = findViewById(R.id.txt_tabname) ;
    }
}