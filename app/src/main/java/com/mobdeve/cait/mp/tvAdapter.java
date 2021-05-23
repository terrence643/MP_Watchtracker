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
    private OnItemClickListener tvListener ;

    public interface OnItemClickListener{
        void onItemClick(int position) ;
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        tvListener = listener ;
    }

    public tvAdapter(Context tvContext, List<TvClass> tvData) {
        this.tvContext = tvContext;
        this.tvData = tvData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.tv_layout, parent, false) ;
        MyViewHolder myViewHolder = new MyViewHolder(v, tvListener) ;
        return myViewHolder ;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Glide.with(tvContext).load("https://image.tmdb.org/t/p/w500"+tvData.get(position).getImg()).into(holder.tvPoster);

    }

    @Override
    public int getItemCount() {
        return tvData.size();
    }



    public static class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView tvPoster;

        public MyViewHolder(@NonNull View itemView, OnItemClickListener listener){
            super(itemView);
            tvPoster = itemView.findViewById(R.id.tvPoster);

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