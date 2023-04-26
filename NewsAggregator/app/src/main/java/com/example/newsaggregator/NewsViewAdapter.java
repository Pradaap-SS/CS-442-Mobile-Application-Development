package com.example.newsaggregator;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class NewsViewAdapter extends RecyclerView.Adapter<NewsViewHolder>{
    MainActivity mainActivity;
    ArrayList<NewsEntity> newsEntityList;

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_info, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {

        NewsEntity newsEntity = newsEntityList.get(position);
        Date date = null;
        try {
            date = (new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")).parse(newsEntity.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String dateTimeFormat = (new SimpleDateFormat("MMM dd, yyyy hh:mm")).format(date);

        holder.imageView.setOnClickListener(v -> this.mainActivity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(newsEntity.getNewsUrl()))));
        holder.description.setOnClickListener(v -> this.mainActivity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(newsEntity.getNewsUrl()))));
        holder.title.setOnClickListener(v -> this.mainActivity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(newsEntity.getNewsUrl()))));

        if(newsEntity.getTitle().equals(null))
            holder.title.setText("");
        else
            holder.title.setText(newsEntity.getTitle());

        if(newsEntity.getDesc().equals(null))
            holder.description.setText("");
        else
            holder.description.setText(newsEntity.getDesc());

        if(newsEntity.getAuthor().equals(null))
            holder.author.setText("");
        else
            holder.author.setText(newsEntity.getAuthor());

        holder.time.setText(dateTimeFormat);
        holder.pageCount.setText((position + 1) + " of " + newsEntityList.size());

        if (!newsEntity.getUrlImage().equals("null")) {
            holder.imageView.setImageResource(R.drawable.loading);
            new NewsImage(holder.imageView, mainActivity).execute(newsEntity.getUrlImage());
        }
    }
    @Override
    public int getItemCount() {
        return newsEntityList.size();
    }

    public NewsViewAdapter(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public void setList(ArrayList<NewsEntity> newsEntityList){
        this.newsEntityList = newsEntityList;
    }

}
