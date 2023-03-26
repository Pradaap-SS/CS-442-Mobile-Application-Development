package com.example.visualcrossingweatherapp;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class SevenDaysWeatherView extends RecyclerView.ViewHolder {
    private static final String TAG = "SevenDaysWeatherView";
     TextView weeklyDateTime, weeklyTemp, weeklyDescription, weeklyPrecipitation
     , weeklyUvIndex, weeklyMorningTemp, weeklyAfternoonTemp, weeklyEveningTemp, weeklyNightTemp;
     ImageView weeklyImageView;

    SevenDaysWeatherView(View view){
        super(view);
        Log.d(TAG, "SevenDaysWeatherView: ");

        weeklyDateTime = view.findViewById(R.id.weeklyDateTime);
        weeklyTemp = view.findViewById(R.id.weeklyTemp);
        weeklyDescription = view.findViewById(R.id.weeklyDescription);
        weeklyPrecipitation = view.findViewById(R.id.weeklyPrecipitation);
        weeklyUvIndex = view.findViewById(R.id.weeklyUvIndex);
        weeklyMorningTemp = view.findViewById(R.id.weeklyMorningTemp);
        weeklyAfternoonTemp = view.findViewById(R.id.weeklyAfternoonTemp);
        weeklyEveningTemp = view.findViewById(R.id.weeklyEveningTemp);
        weeklyNightTemp = view.findViewById(R.id.weeklyNightTemp);
        weeklyImageView = view.findViewById(R.id.weeklyImageView);
    }
}
