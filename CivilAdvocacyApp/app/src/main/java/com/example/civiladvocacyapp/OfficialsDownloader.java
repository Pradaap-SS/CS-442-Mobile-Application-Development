package com.example.civiladvocacyapp;

import android.net.Uri;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class OfficialsDownloader {

    private static final String TAG = "OfficialDownloader";

    private static MainActivity mainActivity;
    private static RequestQueue queue;
    private static final String dataUrl = "https://www.googleapis.com/civicinfo/v2/representatives";
    private static final String apiKey ="AIzaSyDxDgSxmKLFzHy_3m79wDgmyVdHTMWprn8";

    public static void downloadData(MainActivity mainActivityInput,
                                       String city) {
        mainActivity = mainActivityInput;
        queue = Volley.newRequestQueue(mainActivity);

        Uri.Builder buildURL = Uri.parse(dataUrl).buildUpon();
        buildURL.appendQueryParameter("key", apiKey);
        buildURL.appendQueryParameter("address", city);
        String finalUrl = buildURL.build().toString();

        Response.Listener<JSONObject> listener =
                response -> parseJSON(response.toString());

        Response.ErrorListener error =
                error1 -> {
                    try {
                        mainActivity.updateData(null, null);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                };
        JsonObjectRequest jsonObjectRequest =
                new JsonObjectRequest(Request.Method.GET, finalUrl,
                        null, listener, error);
        queue.add(jsonObjectRequest);
    }

    private static void parseJSON(String str) {
        List<Officials> officialList = new ArrayList<>();

        try {
            JSONObject jsonObj = new JSONObject(str);
            JSONObject addressObj = jsonObj.getJSONObject("normalizedInput");

            String line1 = addressObj.getString("line1");
            String city = addressObj.getString("city");
            String state = addressObj.getString("state");
            String zip = addressObj.getString("zip");

            String address = String.join(", ", Arrays.asList(line1, city, state, zip)
                    .stream()
                    .filter(s -> !s.isEmpty())
                    .collect(Collectors.toList()));
            Log.d(TAG, "parseJSON: address is: " + address);

            JSONArray offices = jsonObj.getJSONArray("offices");
            JSONArray jsonArrayOfficials = jsonObj.getJSONArray("officials");

            for (int i = 0; i < offices.length(); i++) {
                String title = offices.getJSONObject(i).getString("name");
                JSONArray officials = offices.getJSONObject(i).getJSONArray("officialIndices");

                for (int j = 0; j < officials.length(); j++) {
                    int officialIndex = officials.getInt(j);
                    JSONObject joOfficialData = jsonArrayOfficials.getJSONObject(officialIndex);
                    String name = joOfficialData.getString("name");
                    String officialAddress = null;
                    String officialCity = null;
                    String officialState = null;
                    String officialZip = null;
                    String party = null;
                    String phone = null;
                    String url = null;
                    String email = null;
                    String photoUrl = null;
                    String fbID = null;
                    String twitterID = null;
                    String youtubeID = null;


                    if (joOfficialData.has("address")) {
                        officialAddress = "";
                        JSONObject officialAddressJO = joOfficialData.getJSONArray("address").getJSONObject(0);
                        if (officialAddressJO.has("line1"))
                            officialAddress += (officialAddressJO.getString("line1"));
                        if (officialAddressJO.has("line2"))
                            officialAddress += (" " + officialAddressJO.getString("line2"));
                        if (officialAddressJO.has("line3"))
                            officialAddress += (" " + officialAddressJO.getString("line3"));
                        if (officialAddressJO.has("city"))
                            officialCity = (officialAddressJO.getString("city"));
                        if (officialAddressJO.has("state"))
                            officialState = (officialAddressJO.getString("state"));
                        if (officialAddressJO.has("zip"))
                            officialZip = (officialAddressJO.getString("zip"));

                    }
                    if (joOfficialData.has("party")) party = joOfficialData.getString("party");
                    if (joOfficialData.has("phones"))
                        phone = joOfficialData.getJSONArray("phones").getString(0);
                    if (joOfficialData.has("urls")) url = joOfficialData.getJSONArray("urls").getString(0);
                    if (joOfficialData.has("emails"))
                        email = joOfficialData.getJSONArray("emails").getString(0);
                    if (joOfficialData.has("photoUrl")) photoUrl = joOfficialData.getString("photoUrl");

                    if (joOfficialData.has("channels")) {
                        JSONArray officialChannels = joOfficialData.getJSONArray("channels");
                        for (int k = 0; k < officialChannels.length(); k++) {
                            String channelType = officialChannels.getJSONObject(k).getString("type");
                            String channelID = officialChannels.getJSONObject(k).getString("id");
                            if (channelType.equals("Facebook")) fbID = channelID;
                            if (channelType.equals("Twitter")) twitterID = channelID;
                            if (channelType.equals("YouTube")) youtubeID = channelID;
                        }
                    }
                    Officials official = new Officials(name, title, party, photoUrl, fbID, twitterID,
                            youtubeID, officialAddress, officialCity, officialState, officialZip,
                            phone, email, url);
                    officialList.add(official);
                }
            }
            mainActivity.updateData(officialList, address);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
