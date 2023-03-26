package com.example.visualcrossingweatherapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.text.SimpleDateFormat;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private static final String weatherURL = "https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/";
    private static final String myAPIKey = "NNGCPPN8WCF56YEXN36F4D2UX";
    private String temperatureUnit, currentLocation;
    SwipeRefreshLayout refreshLayout;
    SharedPreferences sharedPref;
    private RequestQueue queue;
    private TextView bannerText, wAppFeelsLike, wAppTemp, wAppCondition
    , wAppWindDir, wAppHumidity, wAppUVIndex, wAppVisibility, wAppSunRise
    , wAppSunSet, wAppMorningTemp, wAppAfternoonTemp, wAppEveningTemp, wAppNightTemp;
    private ImageView wAppIcon;
    private LinearLayout dayTempLinearlayout;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private DayWeatherAdapter dayWeatherAdapter;
    private final List<DayWeather> dayWeatherObj = new ArrayList<>();
    MenuItem item;
    private JSONObject cachedAPIResponse = null;
    private boolean hasNetworkConnection() {
        ConnectivityManager connectivityManager = getSystemService(ConnectivityManager.class);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnectedOrConnecting());
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Restore previous values
        restorePreviousValues();
        // Set ActionBar background color and title
        initializeActionBar();
        // Home Screen View Elements
        initializeViewReferences();
        // Home Screen Recycler View
        initializeHorizontalLinearLayoutView();
        queue = Volley.newRequestQueue(this);
        // Hide No-Internet text if internet connection is available and fetch API data
        if(hasNetworkConnection()){
            getTimelineWeatherData();
        }else{
            bannerText.setText(R.string.noInternetText);
            bannerText.setVisibility(View.VISIBLE);
        }
    }
    private void restorePreviousValues() {
        sharedPref = getSharedPreferences("myPref", MODE_PRIVATE);
        temperatureUnit = sharedPref.getString("temperatureUnit", "us");
        currentLocation = sharedPref.getString("currentLocation", "Chicago, IL");
    }
    private void initializeActionBar(){
        setTitle("Visual Crossing Weather App");
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#61553f")));
        }
    }
    private void initializeViewReferences(){
        refreshLayout = findViewById(R.id.swiperefresh);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(hasNetworkConnection()){
                    getTimelineWeatherData();
                }else{
                    Toast.makeText(MainActivity.this, "No Internet!", Toast.LENGTH_SHORT).show();
                }
                refreshLayout.setRefreshing(false);
            }
        });
        bannerText = findViewById(R.id.bannerText);
        wAppFeelsLike= findViewById(R.id.presentConditions_feelslike);
        wAppTemp= findViewById(R.id.presentConditions_temp);
        wAppIcon= findViewById(R.id.presentConditions_icon);
        wAppCondition= findViewById(R.id.presentConditions_conditions);
        wAppWindDir= findViewById(R.id.presentConditions_windir);
        wAppHumidity= findViewById(R.id.presentConditions_humidity);
        wAppUVIndex= findViewById(R.id.presentConditions_uvindex);
        wAppVisibility= findViewById(R.id.presentConditions_visibility);
        wAppSunRise= findViewById(R.id.presentConditions_sunriseEpoch);
        wAppSunSet= findViewById(R.id.presentConditions_sunsetEpoch);
        wAppMorningTemp= findViewById(R.id.morningTemp);
        wAppAfternoonTemp= findViewById(R.id.afternoonTemp);
        wAppEveningTemp= findViewById(R.id.eveningTemp);
        wAppNightTemp= findViewById(R.id.nightTemp);
        dayTempLinearlayout = findViewById(R.id.dayTempLinearlayout);
    }
    private void getTimelineWeatherData(){
        Log.d(TAG, "getTimelineWeatherData: ");
        Response.Listener<JSONObject> listener =
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            cachedAPIResponse = response;
                            runOnUiThread(() -> updateView(response));
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(MainActivity.this, "Something went wrong!", Toast.LENGTH_LONG).show();
                        }
                    }
                };
        Response.ErrorListener error = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    JSONObject jsonObject = new JSONObject(new String(error.networkResponse.data));
                    Toast.makeText(MainActivity.this, jsonObject.toString(), Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        String urlToUse = getWeatherAPIUrl(currentLocation);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, urlToUse, null, listener, error);
        queue.add(jsonObjectRequest);
    }
    private void initializeHorizontalLinearLayoutView(){
        recyclerView = findViewById(R.id.homeRecycler);
        dayWeatherAdapter = new DayWeatherAdapter(dayWeatherObj, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(dayWeatherAdapter);
    }
    private String getWeatherAPIUrl(String current){
        Uri.Builder buildURL = Uri.parse(weatherURL + current).buildUpon()
                .appendQueryParameter("unitGroup", temperatureUnit)
                .appendQueryParameter("lang", "en")
                .appendQueryParameter("key", myAPIKey);
        return buildURL.toString();
    }
    private String getTimeString(Long epoch){
        Date dateTime = new Date(epoch * 1000);
        SimpleDateFormat timeOnly = new SimpleDateFormat("h:mm a", Locale.getDefault());
        return timeOnly.format(dateTime);
    }
    private String getDateString(Long epoch){
        Date dateTime = new Date(epoch * 1000);
        SimpleDateFormat dayDate = new SimpleDateFormat("EEEE", Locale.getDefault());
        return dayDate.format(dateTime);
    }
    private String getDateTimeString(Long epoch){
        Date dateTime = new Date(epoch * 1000);
        SimpleDateFormat fullDate = new SimpleDateFormat("EEE MMM dd h:mm a, yyyy", Locale.getDefault());
        return fullDate.format(dateTime);
    }
    private Integer getIconId(String s){
        String icon = s.replace("-", "_"); // Replace all dashes with underscores
        int iconId = MainActivity.this.getResources().getIdentifier(icon, "drawable", MainActivity.this.getPackageName());
        if (iconId == 0) {
            Log.d(TAG, "parseCurrentRecord: CANNOT FIND ICON " + icon);
        }
        return iconId;
    }
    private String getTemperatureUnit(){
        return "°" + (temperatureUnit.equals("us") ? "F" : "C");
    }

    private String getVisibilityUnit(){
        return (temperatureUnit.equals("us") ? "mi" : "km");
    }
    private String getWindAndGustingUnit(){ return (temperatureUnit.equals("us") ? "mph" : "km/h"); }

    @SuppressLint({"SetTextI18n", "NotifyDataSetChanged"})
    private void updateView(JSONObject json){
        try {
            String address = json.getString("address");
            setTitle(address);
            currentLocation = address;
            sharedPref.edit().putString("currentLocation", address).apply();
            JSONObject currentConditions = json.getJSONObject("currentConditions");

            Long dateTimeEpoch = currentConditions.getLong("datetimeEpoch");
            bannerText.setText(getDateTimeString(dateTimeEpoch));
            bannerText.setVisibility(View.VISIBLE);
            String unit = getTemperatureUnit();

            wAppTemp.setText(currentConditions.getString("temp") + unit);
            wAppTemp.setVisibility(View.VISIBLE);

            wAppFeelsLike.setText("Feels like " + currentConditions.getString("feelslike") + unit);
            wAppFeelsLike.setVisibility(View.VISIBLE);

            wAppCondition.setText(currentConditions.getString("conditions") + " (" + currentConditions.getInt("cloudcover") + "% clouds)");
            wAppCondition.setVisibility(View.VISIBLE);

            String windGustUnit = getWindAndGustingUnit();
            String gust = currentConditions.getString("windgust").equals("null") ? "0" : currentConditions.getString("windgust");

            wAppWindDir.setText("Winds: " + getWindDirection(currentConditions.getLong("winddir")) + " at " + currentConditions.getString("windspeed") + " " + windGustUnit +" gusting to " + gust + " " + windGustUnit);
            wAppWindDir.setVisibility(View.VISIBLE);

            wAppHumidity.setText("Humidity: " + currentConditions.getString("humidity") + "%");
            wAppHumidity.setVisibility(View.VISIBLE);

            wAppUVIndex.setText("UV Index: " + currentConditions.getString("uvindex"));
            wAppUVIndex.setVisibility(View.VISIBLE);

            wAppIcon.setImageResource(getIconId(currentConditions.getString("icon")));
            wAppIcon.setVisibility(View.VISIBLE);

            wAppVisibility.setText("Visibility: " + currentConditions.getString("visibility") + " " + getVisibilityUnit());
            wAppVisibility.setVisibility(View.VISIBLE);

            wAppSunRise.setText("Sunrise: " + getTimeString(currentConditions.getLong("sunriseEpoch")));
            wAppSunRise.setVisibility(View.VISIBLE);

            wAppSunSet.setText("Sunset: " + getTimeString(currentConditions.getLong("sunsetEpoch")));
            wAppSunSet.setVisibility(View.VISIBLE);

            JSONArray days = json.getJSONArray("days");
            JSONArray hours = ((JSONObject) days.get(0)).getJSONArray("hours");

            wAppMorningTemp.setText(((JSONObject) hours.get(8)).getString("temp") + unit);
            wAppMorningTemp.setVisibility(View.VISIBLE);

            wAppAfternoonTemp.setText(((JSONObject) hours.get(13)).getString("temp") + unit);
            wAppAfternoonTemp.setVisibility(View.VISIBLE);

            wAppEveningTemp.setText(((JSONObject) hours.get(17)).getString("temp") + unit);
            wAppEveningTemp.setVisibility(View.VISIBLE);

            wAppNightTemp.setText(((JSONObject) hours.get(23)).getString("temp") + unit);
            wAppNightTemp.setVisibility(View.VISIBLE);

            dayTempLinearlayout.setVisibility(View.VISIBLE);
            // Reset if any previous data set exists
            if(dayWeatherObj.size() > 0){
                dayWeatherObj.clear();
            }
            for(int d = 0; d<3 ; d++) {
                JSONArray hourss = ((JSONObject) days.get(d)).getJSONArray("hours");
                for (int h = 0; h < 24; h++) {
                    String day = ((JSONObject) hourss.get(h)).getString("datetimeEpoch");
                    String temp = ((JSONObject) hourss.get(h)).getString("temp");
                    String icon = ((JSONObject) hourss.get(h)).getString("icon");
                    String conditions = ((JSONObject) hourss.get(h)).getString("conditions");
                    DayWeather newWeather = new DayWeather(d==0 ? "Today"  : getDateString(Long.parseLong(day)), getTimeString(Long.parseLong(day)), temp + unit, conditions, getIconId(icon));
                    dayWeatherObj.add(newWeather);
                }
            }
            dayWeatherAdapter.notifyDataSetChanged();

            Log.d(TAG, "onResponse: days: " + days.toString());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private String getWindDirection(double degrees) {
        if (degrees >= 337.5 || degrees < 22.5) return "N";
        if (degrees >= 22.5 && degrees < 67.5) return "NE";
        if (degrees >= 67.5 && degrees < 112.5) return "E";
        if (degrees >= 112.5 && degrees < 157.5) return "SE";
        if (degrees >= 157.5 && degrees < 202.5) return "S";
        if (degrees >= 202.5 && degrees < 247.5) return "SW";
        if (degrees >= 247.5 && degrees < 292.5) return "W";
        if (degrees >= 292.5 && degrees < 337.5) return "NW";
        return "X";
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "onCreateOptionsMenu: ");
        getMenuInflater().inflate(R.menu.home_screen_menu, menu);
        item = menu.findItem(R.id.menuA);
        if(Objects.equals(temperatureUnit, "us")){
            item.setIcon(R.drawable.units_f);
        }else{
            item.setIcon(R.drawable.units_c);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected: " + item.getItemId());
        if(!hasNetworkConnection()){
            Toast.makeText(MainActivity.this, "This Feature requires an active internet connection to work!", Toast.LENGTH_LONG).show();
            return super.onOptionsItemSelected(item);
        }
        if(item.getItemId() == R.id.menuA){
            // Toggle temperature unit
            if(Objects.equals(temperatureUnit, "us")){
                temperatureUnit = "metric";
                item.setIcon(R.drawable.units_c);
            }else{
                temperatureUnit = "us";
                item.setIcon(R.drawable.units_f);
            }
            sharedPref.edit().putString("temperatureUnit", temperatureUnit).apply();
            getTimelineWeatherData();
        }else if(item.getItemId() == R.id.menuB){
            Intent intent = new Intent(MainActivity.this, SevenDaysWeatherActivity.class);
            intent.putExtra("TITLE", currentLocation);
            intent.putExtra("UNIT", temperatureUnit);
            intent.putExtra("DATA", cachedAPIResponse.toString());
            startActivity(intent);
        }else if(item.getItemId() == R.id.menuC){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Enter a location");
            builder.setMessage("For US locations, enter as 'City', or 'City,State' \n\nFor international locations enter as 'City,Country'");

            final EditText locationName = new EditText(this);
            locationName.setInputType(InputType.TYPE_CLASS_TEXT);
            locationName.setGravity(Gravity.CENTER_HORIZONTAL);
            builder.setView(locationName);
            builder.setPositiveButton("OK", (dialog, id) -> {
                String enteredLocation = locationName.getText().toString();

                // Regular expression patterns for US and international location formats
                Pattern usPattern = Pattern.compile("^[a-zA-Z]+(?:,[ ]?[a-zA-Z]+)?$");
                Pattern internationalPattern = Pattern.compile("^[a-zA-Z]+(?:,[ ]?[a-zA-Z]+)+$");

                Matcher usMatcher = usPattern.matcher(enteredLocation);
                Matcher internationalMatcher = internationalPattern.matcher(enteredLocation);

                if (usMatcher.matches() || internationalMatcher.matches()) {
                    currentLocation = enteredLocation;
                    getTimelineWeatherData();
                } else{
                    Toast.makeText(MainActivity.this, "Invalid location format!", Toast.LENGTH_LONG).show();
                }
            });
            builder.setNegativeButton("CANCEL", (dialog, id) -> {});
            AlertDialog dialog = builder.create();
            dialog.show();
        }else{
            Toast.makeText(this, "Invalid Reference!", Toast.LENGTH_LONG).show();
            Log.d(TAG, "onOptionsItemSelected: Invalid Reference!");
        }
        return super.onOptionsItemSelected(item);
    }
}