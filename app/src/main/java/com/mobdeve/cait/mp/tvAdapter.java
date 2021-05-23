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

public class tvAdapter extends RecyclerView.Adapter<tvAdapter.MyViewHolder> {

    private Context tvContext;
    private List<TvClass> tvData;

    public tvAdapter(Context tvContext, List<MovieClass> movieData) {
        this.tvContext = tvContext;
        this.tvData = tvData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        LayoutInflater inflater = LayoutInflater.from(tvContext);
        v = inflater.inflate(R.layout.tv_layout,parent,false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Glide.with(tvContext).load("https://image.tmdb.org/t/p/w500"+tvData.get(position).getImg()).into(holder.tvPoster);
        holder.tvPoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return tvData.size();
    }



    public static class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView tvPoster;

        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            tvPoster = itemView.findViewById(R.id.moviePoster);

        }
    }
}