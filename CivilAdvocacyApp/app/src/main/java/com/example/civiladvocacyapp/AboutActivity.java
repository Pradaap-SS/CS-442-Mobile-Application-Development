package com.example.civiladvocacyapp;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        setupActionBar();
    }

    private void setupActionBar() {
        Objects.requireNonNull(getSupportActionBar()).setTitle("Civil Advocacy");
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#391365"));
        getSupportActionBar().setBackgroundDrawable(colorDrawable);
    }

    public void clickGoogleLink(View v) {
        String googleLinkURL = "https://developers.google.com/civic-information/";
        openWebPage(googleLinkURL);
    }

    private void openWebPage(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }


}