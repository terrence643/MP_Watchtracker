package com.mobdeve.cait.mp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class movieAdapter extends RecyclerView.Adapter<movieAdapter.MyViewHolder> {

    private Context movieContext;
    private List<MovieClass> movieData;

    public movieAdapter(Context movieContext, List<MovieClass> movieData) {
        this.movieContext = movieContext;
        this.movieData = movieData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        LayoutInflater inflater = LayoutInflater.from(movieContext);
        v = inflater.inflate(R.layout.movie_layout,parent,false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Glide.with(movieContext).load("https://image.tmdb.org/t/p/w500"+movieData.get(position).getImg()).into(holder.moviePoster);
    }

    @Override
    public int getItemCount() {
        return movieData.size();
    }



    public static class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView moviePoster;

        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            moviePoster = itemView.findViewById(R.id.moviePoster);

        }
    }
}