package com.example.civiladvocacyapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    private FusedLocationProviderClient fusedLocationProviderClient;
    private static final int LOCATION_REQUEST = 111;
    private OfficialsAdapter officialsAdapter;
    private final List<Officials> officialsList = new ArrayList<>();

    private String userLocationInput = null;
    RecyclerView recyclerView;
    TextView textViewLocation;
    private boolean userLocationInputFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Grab location automatically
        fusedLocationProviderClient =
                LocationServices.getFusedLocationProviderClient(this);
        determineLocation();

        // Set up recycler view
        recyclerView = findViewById(R.id.recycler);
        officialsAdapter = new OfficialsAdapter(this, officialsList);
        recyclerView.setAdapter(officialsAdapter);
        recyclerView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        );

        // Set up location text view
        textViewLocation = findViewById(R.id.textViewLocation);

        // Set up UI based on network connectivity
        boolean hasNetworkConnection = hasNetworkConnection(this);
        if (!hasNetworkConnection) {
            Objects.requireNonNull(getSupportActionBar()).setTitle("Know Your Government");
            ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#A3A3A3"));
            getSupportActionBar().setBackgroundDrawable(colorDrawable);
            textViewLocation.setText("No Data For Location");
            findViewById(R.id.noNetworkTitle).setVisibility(View.VISIBLE);
            findViewById(R.id.noNetworkBody).setVisibility(View.VISIBLE);
            findViewById(R.id.divider).setVisibility(View.VISIBLE);
            findViewById(R.id.divider2).setVisibility(View.VISIBLE);
            findViewById(R.id.divider3).setVisibility(View.VISIBLE);
            findViewById(R.id.divider4).setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
        } else {
            Objects.requireNonNull(getSupportActionBar()).setTitle("Civil Advocacy");
            ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#391365"));
            getSupportActionBar().setBackgroundDrawable(colorDrawable);
            findViewById(R.id.noNetworkTitle).setVisibility(View.INVISIBLE);
            findViewById(R.id.noNetworkBody).setVisibility(View.INVISIBLE);
            findViewById(R.id.divider).setVisibility(View.INVISIBLE);
            findViewById(R.id.divider2).setVisibility(View.INVISIBLE);
            findViewById(R.id.divider3).setVisibility(View.INVISIBLE);
            findViewById(R.id.divider4).setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("loc", textViewLocation.getText().toString());
        outState.putBoolean("userInput", userLocationInputFlag);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (hasNetworkConnection()) {
            textViewLocation.setText(savedInstanceState.getString("loc"));
            userLocationInputFlag = savedInstanceState.getBoolean("userInput");
            getData();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuOptionAbout:
                Intent aboutIntent = new Intent(this, AboutActivity.class);
                startActivity(aboutIntent);
                return true;
            case R.id.menuOptionLoc:
                if (!hasNetworkConnection()) {
                    Toast.makeText(this, "This app needs network connectivity to work", Toast.LENGTH_SHORT).show();
                    return true;
                }
                showLocationDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showLocationDialog() {
        EditText locationEditText = new EditText(this);
        locationEditText.setInputType(InputType.TYPE_CLASS_TEXT);
        locationEditText.setGravity(Gravity.CENTER_HORIZONTAL);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(locationEditText);
        builder.setPositiveButton("OK", (dialog, which) -> {
            String userLocationInput = locationEditText.getText().toString();
            if (!TextUtils.isEmpty(userLocationInput)) {
                textViewLocation.setText(userLocationInput);
                getData();
            } else {
                Toast.makeText(this, "Please enter a location", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("CANCEL", (dialog, which) -> {
        });
        builder.setTitle("Enter a Location");
        builder.setMessage("Enter either a 'City, State' or a ZIP code.");
        AlertDialog locationDialog = builder.create();
        locationDialog.show();
    }

    private boolean hasNetworkConnection(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
        return networkCapabilities != null && (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI));
    }
    private boolean hasNetworkConnection() {
        ConnectivityManager connectivityManager = getSystemService(ConnectivityManager.class);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnectedOrConnecting());
    }

    private void determineLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST);
            return;
        }

        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
            if (location != null && hasNetworkConnection(this) && !userLocationInputFlag) {
                String locationString = getLocationDetails(location);
                Log.d(TAG, "Location: " + locationString);
                textViewLocation.setText(locationString);
                getData();
            }
        }).addOnFailureListener(this, e -> {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            textViewLocation.setText("No Data for Location");
        });
    }

    private String getLocationDetails(Location location) {
        StringBuilder addressBuilder = new StringBuilder();
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses;

        try {
            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            Address address = addresses.get(0);
            String city = address.getLocality();
            String state = address.getAdminArea();
            String addressLine = address.getAddressLine(0);
            Log.d(TAG, "Address line: " + addressLine);
            addressBuilder.append(addressLine);
        } catch (IOException e) {
            Log.e(TAG, "Error getting address", e);
        }

        return addressBuilder.toString();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode != LOCATION_REQUEST) {
            return;
        }

        if (permissions.length > 0 && grantResults.length > 0) {
            if (Manifest.permission.ACCESS_FINE_LOCATION.equals(permissions[0])) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    determineLocation();
                } else {
                    Log.d(TAG, "User denied location permission");
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        int position = recyclerView.getChildLayoutPosition(v);
        Log.d(TAG, "onClick: ofiicial position" + position);

        Officials official = officialsList.get(position);

        Intent intent = new Intent(this, OfficialsViewActivity.class);
        intent.putExtra("DATA_OFFICIAL", official);
        intent.putExtra("DATA_LOC", textViewLocation.getText().toString());

        startActivity(intent);
    }


    private void getData() {
        Log.d(TAG, "getData via OfficialDownloader class");
        OfficialsDownloader.downloadData(this, textViewLocation.getText().toString());
    }

    public void updateData(List<Officials> officials, String address) throws JSONException {
        if (officials == null) {
            Objects.requireNonNull(getSupportActionBar()).setTitle("Know Your Government");
            textViewLocation.setText("No Data For Location");
            Toast.makeText(this, "Please Enter a Valid location Name", Toast.LENGTH_SHORT).show();
            return;
        }
        textViewLocation.setText(address);
        officialsList.clear();
        officialsList.addAll(officials);
        officialsAdapter.notifyDataSetChanged();
    }
}