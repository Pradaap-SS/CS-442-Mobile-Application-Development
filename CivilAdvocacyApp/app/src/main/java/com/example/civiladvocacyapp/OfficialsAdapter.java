package com.example.civiladvocacyapp;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

public class OfficialsAdapter extends RecyclerView.Adapter<OfficialsViewHolder> {
    private static final String TAG = "OfficialsAdapter";
    private final MainActivity mainActivity;
    private final List<Officials> officialsList;

    public OfficialsAdapter(MainActivity mainActivity, List<Officials> officialsList) {
        this.mainActivity = mainActivity;
        this.officialsList = officialsList;
    }

    @NonNull
    @Override
    public OfficialsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: ");
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.official_data, parent, false);
        itemView.setOnClickListener(mainActivity);
        return new OfficialsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull OfficialsViewHolder holder, int position) {
        Officials official = officialsList.get(position);
        Log.d(TAG, "onBindViewHolder: Filling recycler. Name: " + official.getName());
        holder.title.setText(official.getTitle());
        holder.name.setText(official.getName());
        holder.party.setText(String.format("(%s)", official.getParty()));
        setOfficialProfilePic(holder.profilePic, official.getPhotoURL());
    }

    private void setOfficialProfilePic(ImageView imageView, String photoUrl) {
        if (photoUrl != null) {
            Log.d(TAG, "onBindViewHolder: getting - " + photoUrl);
            Picasso.get()
                    .load(photoUrl)
                    .error(R.drawable.brokenimage)
                    .into(imageView, new Callback() {
                        @Override
                        public void onSuccess() {
                            Log.d(TAG, "onSuccess: for photoUrl");
                        }

                        @Override
                        public void onError(Exception e) {
                            Log.d(TAG, "onError: " + e);
                        }
                    });
        } else {
            imageView.setImageResource(R.drawable.missing);
        }
    }

    @Override
    public int getItemCount() {
        return officialsList.size();
    }

}
