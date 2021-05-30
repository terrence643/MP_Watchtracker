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

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MyViewHolder> {

    private Context movieContext;
    private List<MovieClass> movieData;
    private OnItemClickListener movieListener ;

    public interface OnItemClickListener{
        void onItemClick(int position) ;
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        movieListener = listener ;
    }

    public MovieAdapter(Context movieContext, List<MovieClass> movieData) {
        this.movieContext = movieContext;
        this.movieData = movieData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.icon_img_layout, parent, false) ;
        MyViewHolder myViewHolder = new MyViewHolder(view, movieListener) ;
        return myViewHolder ;
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

        public MyViewHolder(@NonNull View itemView, OnItemClickListener listener){
            super(itemView);
            moviePoster = itemView.findViewById(R.id.iconPoster);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}