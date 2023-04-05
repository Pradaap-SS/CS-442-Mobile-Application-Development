package com.example.civiladvocacyapp;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class OfficialsPhotoActivity extends AppCompatActivity {
    private static final String TAG = "OfficialPhotoActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_official_photo);

        Objects.requireNonNull(getSupportActionBar()).setTitle("Civil Advocacy");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#391365")));

        TextView textViewLocation = findViewById(R.id.officialLocation);
        TextView name = findViewById(R.id.officialName);
        TextView title = findViewById(R.id.officialTitle);
        ImageView profilePic = findViewById(R.id.imageViewOP);
        ImageView partyPic = findViewById(R.id.officialParty);
        ConstraintLayout layout = findViewById(R.id.ContraintLayoutOP);

        Intent intent = getIntent();
        if (intent.hasExtra("DATA_OFFICIAL")) {
            Officials official = (Officials) intent.getSerializableExtra("DATA_OFFICIAL");
            String location = intent.getStringExtra("DATA_LOC");

            textViewLocation.setText(location);
            name.setText(official.getName());
            title.setText(official.getTitle());

            if (official.getParty().equals("Republican Party")) {
                layout.setBackgroundColor(Color.RED);

                int iconID = getResources().getIdentifier("rep_logo", "drawable", getPackageName());
                if (iconID == 0) {
                    Log.d(TAG, "onCreate: Couldn't get logo");
                }
                partyPic.setImageResource(iconID);
            } else if (official.getParty().equals("Democratic Party")) {
                layout.setBackgroundColor(Color.BLUE);
            } else {
                layout.setBackgroundColor(Color.BLACK);
                partyPic.setVisibility(View.INVISIBLE);
            }

            if (official.getPhotoURL() != null) {
                Log.d(TAG, "onBindViewHolder: getting - " + official.getPhotoURL());

                Picasso.get()
                        .load(official.getPhotoURL())
                        .error(R.drawable.brokenimage)
                        .into(profilePic, new Callback() {
                            @Override
                            public void onSuccess() {
                                Log.d(TAG, "onSuccess: for " + official.getName());
                            }

                            @Override
                            public void onError(Exception e) {
                                Log.d(TAG, "onError: " + e);
                            }
                        });
            } else {
                profilePic.setImageResource(R.drawable.missing);
            }
        }
    }

}