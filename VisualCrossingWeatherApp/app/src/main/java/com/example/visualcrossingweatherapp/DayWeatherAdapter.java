package com.example.visualcrossingweatherapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DayWeatherAdapter extends RecyclerView.Adapter<DayWeatherView> {

    private static final String TAG = "DayWeatherAdapter";
    private final List<DayWeather> dayWeatherObj;
    public DayWeatherAdapter(List<DayWeather> dayWeatherObj, MainActivity mainActivity) {
        this.dayWeatherObj = dayWeatherObj;
    }
    @NonNull
    @Override
    public DayWeatherView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.day_weather_view, parent, false);
        return new DayWeatherView(itemView);
    }
    @Override
    public void onBindViewHolder(@NonNull DayWeatherView holder, int position) {
        DayWeather data = dayWeatherObj.get(position);
        holder.day.setText(data.getDay());
        holder.dateTime.setText(data.getDateTime());
        holder.icon.setImageResource(data.getIcon());
        holder.temperature.setText(data.getTemperature());
        holder.conditions.setText(data.getConditions());
    }
    @Override
    public int getItemCount() {
        return dayWeatherObj.size();
    }
}
