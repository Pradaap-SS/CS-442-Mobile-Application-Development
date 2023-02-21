package com.example.androidnotes;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NotesViewHolder extends RecyclerView.ViewHolder
{
    TextView notesTitle, notesDate, notesContent;

    public NotesViewHolder(@NonNull View itemView)
    {
        super(itemView);
        notesTitle = itemView.findViewById(R.id.notes_title);
        notesDate = itemView.findViewById(R.id.notes_date_modified);
        notesContent = itemView.findViewById(R.id.notes_content);
    }
}
