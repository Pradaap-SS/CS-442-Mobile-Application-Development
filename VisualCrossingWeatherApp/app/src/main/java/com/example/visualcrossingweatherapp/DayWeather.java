package com.example.visualcrossingweatherapp;

public class DayWeather {

    private static final String TAG = "DayWeather";
    private String day, dateTime, temperature, conditions;
    private Integer icon;


    DayWeather(String day, String dateTime, String temperature, String conditions, Integer icon) {
        this.day = day;
        this.dateTime = dateTime;
        this.temperature = temperature;
        this.conditions = conditions;
        this.icon = icon;
    }

    public String getDay() {
        return day;
    }
    public String getDateTime() { return dateTime; }
    public String getTemperature() {
        return temperature;
    }
    public String getConditions() {
        return conditions;
    }
    public Integer getIcon() {
        return icon;
    }
}
