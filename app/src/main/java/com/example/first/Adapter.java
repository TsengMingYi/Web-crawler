package com.example.first;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder>{
    private ArrayList<News> newsArrayList = new ArrayList<>();
    public ArrayList<News> getAirPlaneDataList() {
        return newsArrayList;
    }
    public void updateProductData(ArrayList<News> newsArrayList) {
        this.newsArrayList = newsArrayList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.news, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull Adapter.ViewHolder holder, int position) {
        final News news = newsArrayList.get(position);
        holder.newsTitle.setText(news.getNewsTitle());
        holder.newsUrl.setText(news.getNewsUrl());
        holder.newsTime.setText(news.getNewsTime());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(news.getNewsUrl()));
                try {
                    v.getContext().startActivity(intent);
                } catch (ActivityNotFoundException e) {

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return newsArrayList.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView newsTitle;
        TextView newsUrl;
        TextView newsTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            newsTitle = itemView.findViewById(R.id.newsTitle);
            newsUrl = itemView.findViewById(R.id.newsUrl);
            newsTime = itemView.findViewById(R.id.newsTime);
        }
//        public void updateView(SpotManager.Spot spot){
//            if(spot == null){
//                return;
//            }
//            attractions_name.setText(spot.getName());
//        }
    }
}
