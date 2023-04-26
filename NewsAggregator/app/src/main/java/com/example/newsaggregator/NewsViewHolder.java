package com.example.newsaggregator;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NewsViewHolder extends RecyclerView.ViewHolder {
        TextView author, time, title, description, pageCount;
        ImageView imageView;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            author = itemView.findViewById(R.id.Author);
            time = itemView.findViewById(R.id.DateAndTime);
            title = itemView.findViewById(R.id.Title);
            description = itemView.findViewById(R.id.Description);
            pageCount = itemView.findViewById(R.id.Count);
            imageView = itemView.findViewById(R.id.Image);

        }
}
