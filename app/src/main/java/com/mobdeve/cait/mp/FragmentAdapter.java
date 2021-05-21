package com.mobdeve.cait.mp;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class FragmentAdapter extends FragmentStateAdapter {
    public FragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment = null ;
        switch (position){
            case 0:
                Log.d("TABITEM", "getItem: " + position);
                fragment = new FragmentView() ;
                Log.d("TABITEM", "createFragment: fragment view ok");
                break;
//            case 1:
//                Log.d("TABITEM", "getItem: " + position);
//                fragment = new FragmentAdd() ;
//                break;
            case 1:
                Log.d("TABITEM", "getItem: " + position);
                fragment = new FragmentDiscover() ;
                break;
        }
        return fragment;
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
