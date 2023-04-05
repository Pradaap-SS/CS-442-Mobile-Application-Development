package com.example.civiladvocacyapp;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class OfficialsViewActivity extends AppCompatActivity {

    private static final String TAG = "ViewOfficialActivity";
    public Officials official;
    private String location;
    private TextView textViewLocation, name, title, party, address, phone
    , email, website, addressStatic, phoneStatic, emailStatic, websiteStatic;
    private ImageView fb, youtube, twitter, partyImage, profileImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_official);

        Objects.requireNonNull(getSupportActionBar()).setTitle("Civil Advocacy");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#391365")));

        textViewLocation = findViewById(R.id.voLocation);
        name = findViewById(R.id.voName);
        title = findViewById(R.id.voTitle);
        party = findViewById(R.id.voParty);
        address = findViewById(R.id.voAddressClick);
        phone = findViewById(R.id.voPhoneClick);
        email = findViewById(R.id.voEmailClick);
        website = findViewById(R.id.voWebsiteClick);
        addressStatic = findViewById(R.id.voAddress);
        phoneStatic = findViewById(R.id.voPhone);
        emailStatic = findViewById(R.id.voEmail);
        websiteStatic = findViewById(R.id.voWebsite);
        fb = findViewById(R.id.voFaceBook);
        youtube = findViewById(R.id.voYouTube);
        twitter = findViewById(R.id.voTwitter);
        partyImage = findViewById(R.id.voPartyClick);
        profileImage = findViewById(R.id.imageViewVO);
        ConstraintLayout layout = findViewById(R.id.constraintLayout);

        Intent intent = getIntent();
        if (intent == null || !intent.hasExtra("DATA_OFFICIAL")) {
            Log.d(TAG, "onCreate: Data sent was not an intent");
            return;
        }

        official = (Officials) intent.getSerializableExtra("DATA_OFFICIAL");
        location = intent.getStringExtra("DATA_LOC");
        textViewLocation.setText(location);
        name.setText(official.getName());
        title.setText(official.getTitle());
        party.setText(String.format("(%s)", official.getParty()));

        if (official.getAddress() != null) {
            String officialAddress = official.getAddress();
            String officialCity = official.getCity();
            String officialState = official.getState();
            String officialZip = official.getZip();
            String addressDisplay = "";
            if (!officialAddress.isEmpty()) {
                addressDisplay += officialAddress + "\n";
            }
            if (!officialCity.isEmpty()) {
                addressDisplay += officialCity + ", ";
            }
            if (!officialState.isEmpty()) {
                addressDisplay += officialState;
            }
            if (!officialZip.isEmpty()) {
                addressDisplay += ", " + officialZip;
            }
            address.setText(addressDisplay);
            address.setPaintFlags(address.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        } else {
            address.setVisibility(View.GONE);
            addressStatic.setVisibility(View.GONE);
        }

        if (official.getPhone() != null) {
            phone.setText(official.getPhone());
            phone.setPaintFlags(phone.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        } else {
            phone.setVisibility(View.GONE);
            phoneStatic.setVisibility(View.GONE);
        }

        if (official.getEmail() != null) {
            email.setText(official.getEmail());
            email.setPaintFlags(email.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        } else {
            email.setVisibility(View.GONE);
            emailStatic.setVisibility(View.GONE);
        }

        if (official.getWebsite() != null) {
            website.setText(official.getWebsite());
            website.setPaintFlags(website.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        } else {
            website.setVisibility(View.GONE);
            websiteStatic.setVisibility(View.GONE);
        }
        if (official.getFbAccountLink() != null) {
            Log.d(TAG, "onCreate: Has FB account");
        } else {
            fb.setVisibility(View.INVISIBLE);
        }
        if (official.getTwitterAccountLink() != null) {
            Log.d(TAG, "onCreate: Has Twitter account");
        } else {
            twitter.setVisibility(View.INVISIBLE);
        }
        if (official.getYoutubeAccountLink() != null) {
            Log.d(TAG, "onCreate: Has YouTube account");
        } else {
            youtube.setVisibility(View.INVISIBLE);
        }
        if (official.getParty().equals("Republican Party")) {
            layout.setBackgroundColor(Color.RED);
            int iconID = getResources().getIdentifier("rep_logo", "drawable", getPackageName());
            if (iconID == 0) {
                Log.d(TAG, "onCreate: Couldn't get icon");
            }
            partyImage.setImageResource(iconID);
        } else if (official.getParty().equals("Democratic Party")) {
            layout.setBackgroundColor(Color.BLUE);
        } else {
            layout.setBackgroundColor(Color.BLACK);
            partyImage.setVisibility(View.INVISIBLE);
        }
        if (official.getPhotoURL() != null) {
            Log.d(TAG, "onBindViewHolder: getting - " + official.getPhotoURL());
            Picasso.get()
                    .load(official.getPhotoURL())
                    .error(R.drawable.brokenimage)
                    .into(profileImage, new Callback() {
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
            profileImage.setImageResource(R.drawable.missing);
        }
    }

    public void onPhotoClick(View v) {
        Log.d(TAG, "onProfilePicClick: Clicked on profile pic");
        Intent intent = new Intent(this, OfficialsPhotoActivity.class);
        intent.putExtra("DATA_OFFICIAL", official);
        intent.putExtra("DATA_LOC", textViewLocation.getText().toString());
        if (official.getPhotoURL() != null) {
            startActivity(intent);
        }
    }

    public void onPartyClick(View v) {
        String partyLink = "";
        if (official.getParty().equals("Democratic Party")) {
            partyLink = "https://democrats.org/";
        } else {
            partyLink = "https://www.gop.com/";
        }
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(partyLink));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    public void onYoutubeClick(View v) {
        String name = official.getYoutubeAccountLink();
        Intent intent;
        try {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setPackage("com.google.android.youtube");
            intent.setData(Uri.parse("https://www.youtube.com/" + name));
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/" + name)));
        }
    }

    public void onTwitterClick(View view) {
        String user = official.getTwitterAccountLink();
        String twitterAppUrl = "twitter://user?screen_name=" + user;
        String twitterWebUrl = "https://twitter.com/" + user;

        Intent intent;
        if (isPackageInstalled("com.twitter.android")) {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(twitterAppUrl));
        } else {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(twitterWebUrl));
        }

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            makeErrorAlert("No application found that handles ACTION_VIEW (twitter/https) intents");
        }
    }

    public void onFacebookClick(View view) {
        String fbUrl = "https://www.facebook.com/" + official.getFbAccountLink();
        String fbAppUrl = "fb://facewebmodal/f?href=" + fbUrl;

        Intent intent;

        if (isPackageInstalled("com.facebook.katana")) {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(fbAppUrl));
        } else {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(fbUrl));
        }

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            makeErrorAlert("No application found that handles ACTION_VIEW (fb/https) intents");
        }
    }

    public void onEmailClick(View view) {
        String[] addresses = new String[]{official.getEmail()};
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, addresses);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            makeErrorAlert("No application found that handles SENDTO (mailto) intents");
        }
    }

    public void onAddressClick(View view) {
        String address = this.address.getText().toString();
        Uri mapUri = Uri.parse("geo:0,0?q=" + Uri.encode(address));
        Intent intent = new Intent(Intent.ACTION_VIEW, mapUri);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            makeErrorAlert("No application found that handles ACTION_VIEW (geo) intents");
        }
    }

    public void onPhoneClick(View view) {
        String number = official.getPhone();
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + number));

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            makeErrorAlert("No application found that handles ACTION_DIAL (tel) intents");
        }
    }

    public void onWebsiteClick(View view) {
        String url = official.getWebsite();
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }


    public boolean isPackageInstalled(String packageName) {
        try {
            return getPackageManager().getApplicationInfo(packageName, 0).enabled;
        }
        catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
    private void makeErrorAlert(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(msg);
        builder.setTitle("No App Found");

        AlertDialog dialog = builder.create();
        dialog.show();
    }

}