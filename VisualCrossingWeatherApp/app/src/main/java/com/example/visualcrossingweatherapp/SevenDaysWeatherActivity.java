package com.example.visualcrossingweatherapp;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
public class SevenDaysWeatherActivity extends AppCompatActivity {
    private static final String TAG = "SevenDaysWeatherActivity";
    private String temperatureUnit = "us";
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private SevenDaysWeatherAdapter sevenDaysWeatherAdapter;
    private final List<SevenDaysWeather> dayWeatherObj = new ArrayList<>();
    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weekly_forecast);
        initializeActionBar();
        initializeLinearLayoutView();
        if (getIntent().hasExtra("TITLE")) {
            String name = getIntent().getStringExtra("TITLE");
            setTitle(name + " 15 Day");
        }
        if (getIntent().hasExtra("UNIT")) {
            temperatureUnit = getIntent().getStringExtra("UNIT");
        }
        if (getIntent().hasExtra("DATA")) {
            String data = getIntent().getStringExtra("DATA");
            try {
                JSONObject json =  new JSONObject(data);
                JSONArray days = json.getJSONArray("days");
                for(int i = 0; i<15 ; i++) {
                    JSONArray hours = ((JSONObject) days.get(i)).getJSONArray("hours");
                        String unit = getTemperatureUnit();

                        String weeklyDateTime = getDateString(((JSONObject) days.get(i)).getLong("datetimeEpoch"));
                        String weeklyTemp = ((JSONObject) days.get(i)).getString("tempmax") + unit +"/" + ((JSONObject) days.get(i)).getString("tempmin") + unit;
                        String weeklyImageView = ((JSONObject) days.get(i)).getString("icon");
                        String weeklyDescription = ((JSONObject) days.get(i)).getString("description");
                        String weeklyPrecipitation = "(" + ((JSONObject) days.get(i)).getString("precipprob") + "% precip.)";
                        String weeklyUvIndex = "UV Index: " + ((JSONObject) days.get(i)).getString("uvindex");
                        String weeklyMorningTemp = ((JSONObject) hours.get(8)).getString("temp") + unit;
                        String weeklyAfternoonTemp = ((JSONObject) hours.get(13)).getString("temp") + unit;
                        String weeklyEveningTemp = ((JSONObject) hours.get(17)).getString("temp") + unit;
                        String weeklyNightTemp = ((JSONObject) hours.get(22)).getString("temp") + unit;

                        SevenDaysWeather newWeather = new SevenDaysWeather(weeklyDateTime, getIconId(weeklyImageView), weeklyTemp, weeklyDescription, weeklyPrecipitation, weeklyUvIndex, weeklyMorningTemp, weeklyAfternoonTemp,  weeklyEveningTemp, weeklyNightTemp);
                        dayWeatherObj.add(newWeather);
                }
                sevenDaysWeatherAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
    }
    private void initializeActionBar(){
        setTitle("Weather App");
        ActionBar actionBar = getSupportActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#61553f"));
        if(actionBar != null){
            actionBar.setBackgroundDrawable(colorDrawable);
        }
    }
    private void initializeLinearLayoutView(){
        recyclerView = findViewById(R.id.seven_day_recycler);
        sevenDaysWeatherAdapter = new SevenDaysWeatherAdapter(dayWeatherObj, this);
        recyclerView.setAdapter(sevenDaysWeatherAdapter);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
    }
    private String getDateString(Long epoch){
        Date dateTime = new Date(epoch * 1000);
        SimpleDateFormat dayDate = new SimpleDateFormat("EEEE, dd", Locale.getDefault());
        return dayDate.format(dateTime);
    }
    private String getTemperatureUnit(){
        return "°" + (temperatureUnit.equals("us") ? "F" : "C");
    }
    private Integer getIconId(String s){
        String icon = s.replace("-", "_");
        int iconId = SevenDaysWeatherActivity.this.getResources().getIdentifier(icon, "drawable", SevenDaysWeatherActivity.this.getPackageName());
        if (iconId == 0) {
            Log.d(TAG, "parseCurrentRecord: CANNOT FIND ICON " + icon);
        }
        return iconId;
    }
}