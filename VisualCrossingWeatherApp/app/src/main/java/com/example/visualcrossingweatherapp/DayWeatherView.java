package com.example.visualcrossingweatherapp;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class DayWeatherView extends RecyclerView.ViewHolder {
    private static final String TAG = "DailyWeatherViewHolder";
    TextView day;
    TextView dateTime;
    ImageView icon;
    TextView temperature;
    TextView conditions;

    DayWeatherView(View view){
        super(view);
        Log.d(TAG, "DayWeatherView: ");
        day = view.findViewById(R.id.day);
        dateTime = view.findViewById(R.id.dateTime);
        icon = view.findViewById(R.id.icon);
        temperature = view.findViewById(R.id.temperature);
        conditions = view.findViewById(R.id.conditions);
    }
}