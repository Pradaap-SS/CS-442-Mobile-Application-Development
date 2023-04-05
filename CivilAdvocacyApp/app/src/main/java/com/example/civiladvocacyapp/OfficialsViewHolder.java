package com.example.civiladvocacyapp;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class OfficialsViewHolder extends RecyclerView.ViewHolder {

    TextView name,title,party;
    ImageView profilePic;

    public OfficialsViewHolder(@NonNull View view) {
        super(view);
        name = view.findViewById(R.id.odName);
        title = view.findViewById(R.id.odTitle);
        party = view.findViewById(R.id.odParty);
        profilePic = view.findViewById(R.id.odProfilePic);
    }
}
