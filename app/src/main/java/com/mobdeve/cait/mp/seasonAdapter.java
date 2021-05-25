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

public class seasonAdapter extends RecyclerView.Adapter<seasonAdapter.MyViewHolder> {

    private Context seasonContext;
    private List<seasonClass> seasonData;
    private OnItemClickListener seasonListener ;

    public interface OnItemClickListener{
        void onItemClick(int position) ;
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        seasonListener = listener ;
    }

    public seasonAdapter(Context seasonContext, List<seasonClass> seasonData) {
        this.seasonContext = seasonContext;
        this.seasonData = seasonData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.season_layout, parent, false) ;
        MyViewHolder myViewHolder = new MyViewHolder(view, seasonListener) ;
        return myViewHolder ;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Glide.with(seasonContext).load("https://image.tmdb.org/t/p/w500"+seasonData.get(position).getPoster_path()).into(holder.seasonPoster);
    }

    @Override
    public int getItemCount() {
        return seasonData.size();
    }



    public static class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView seasonPoster;

        public MyViewHolder(@NonNull View itemView, OnItemClickListener listener){
            super(itemView);
            seasonPoster = itemView.findViewById(R.id.seasonPoster);

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