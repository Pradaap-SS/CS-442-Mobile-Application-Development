package com.example.visualcrossingweatherapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SevenDaysWeatherAdapter extends RecyclerView.Adapter<SevenDaysWeatherView> {

    private static final String TAG = "SevenDayWeatherAdapter";
    private final List<SevenDaysWeather> sevenDaysWeatherObj;

    public SevenDaysWeatherAdapter(List<SevenDaysWeather> sevenDaysWeatherItems, SevenDaysWeatherActivity sevenDaysWeatherActivity) {
        this.sevenDaysWeatherObj = sevenDaysWeatherItems;
    }

    @NonNull
    @Override
    public SevenDaysWeatherView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.weekly_weather_view, parent, false);
        return new SevenDaysWeatherView(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SevenDaysWeatherView holder, int position) {
        SevenDaysWeather data = sevenDaysWeatherObj.get(position);
        holder.weeklyDateTime.setText(data.getWeeklyDateTime());
        holder.weeklyTemp.setText(data.getWeeklyTemp());
        holder.weeklyDescription.setText(data.getWeeklyDescription());
        holder.weeklyPrecipitation.setText(data.getWeeklyPrecipitation());
        holder.weeklyUvIndex.setText(data.getWeeklyUvIndex());
        holder.weeklyMorningTemp.setText(data.getWeeklyMorningTemp());
        holder.weeklyAfternoonTemp.setText(data.getWeeklyAfternoonTemp());
        holder.weeklyEveningTemp.setText(data.getWeeklyEveningTemp());
        holder.weeklyNightTemp.setText(data.getWeeklyNightTemp());
        holder.weeklyImageView.setImageResource(data.getWeeklyImageView());
    }

    @Override
    public int getItemCount() {
        return sevenDaysWeatherObj.size();
    }
}
