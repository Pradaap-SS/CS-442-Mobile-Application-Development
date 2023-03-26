package com.example.visualcrossingweatherapp;

public class SevenDaysWeather {

    private static final String TAG = "SevenDayWeather";
    private String weeklyDateTime, weeklyTemp, weeklyDescription, weeklyPrecipitation, weeklyUvIndex
    , weeklyMorningTemp, weeklyAfternoonTemp, weeklyEveningTemp, weeklyNightTemp;
    private Integer weeklyImageView;

    SevenDaysWeather(String weeklyDateTimeEpoch, Integer weeklyImageView, String weeklyTemp, String weeklyDescription, String weeklyPrecipitation, String weeklyUvIndex, String weeklyMorningTemp, String weeklyAfternoonTemp, String weeklyEveningTemp, String weeklyNightTemp) {
        this.weeklyDateTime = weeklyDateTimeEpoch;
        this.weeklyTemp = weeklyTemp;
        this.weeklyDescription = weeklyDescription;
        this.weeklyPrecipitation = weeklyPrecipitation;
        this.weeklyUvIndex = weeklyUvIndex;
        this.weeklyMorningTemp = weeklyMorningTemp;
        this.weeklyAfternoonTemp = weeklyAfternoonTemp;
        this.weeklyEveningTemp = weeklyEveningTemp;
        this.weeklyNightTemp = weeklyNightTemp;
        this.weeklyImageView = weeklyImageView;
    }

    public String getWeeklyDateTime() {
        return weeklyDateTime;
    }

    public String getWeeklyTemp() {
        return weeklyTemp;
    }

    public String getWeeklyDescription() {
        return weeklyDescription;
    }

    public String getWeeklyPrecipitation() {
        return weeklyPrecipitation;
    }

    public String getWeeklyUvIndex() {
        return weeklyUvIndex;
    }

    public String getWeeklyMorningTemp() {
        return weeklyMorningTemp;
    }

    public String getWeeklyAfternoonTemp() {
        return weeklyAfternoonTemp;
    }

    public String getWeeklyEveningTemp() {
        return weeklyEveningTemp;
    }

    public String getWeeklyNightTemp() {
        return weeklyNightTemp;
    }

    public Integer getWeeklyImageView() {
        return weeklyImageView;
    }
}
