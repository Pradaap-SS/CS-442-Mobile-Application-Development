package com.example.androidnotes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesViewHolder> {
    private final List<Notes> notesList;
    private final MainActivity mainActivity;

    public NotesAdapter(List<Notes> nList, MainActivity mActivity) {
        notesList = nList;
        mainActivity = mActivity;
    }
    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View noteView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_view_notes, parent, false); // inflate the view notes layout
        noteView.setOnClickListener(mainActivity);
        noteView.setOnLongClickListener(mainActivity);
        return new NotesViewHolder(noteView);
    }
    @Override
    public void onBindViewHolder(@NonNull NotesViewHolder notesViewHolder, int pos) {
        Notes notesObj = notesList.get(pos);
        notesViewHolder.notesTitle.setText(notesObj.getNotesTitle());
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd, hh:mm a"); // date format initialization
        notesViewHolder.notesDate.setText(dateFormat.format(new Date(notesObj.getNotesLastModified())));
        // condition to check text size exceeds 80 characters
        int contentLength;
        contentLength = notesObj.getNotesContent().length();
        if(contentLength < 80)
            notesViewHolder.notesContent.setText(notesObj.getNotesContent());
        else
        {
            String strDisplay = notesObj.getNotesContent().substring(0,80) + "...";
            notesViewHolder.notesContent.setText(strDisplay );
        }
    }
    @Override
    public int getItemCount()
    {
        return notesList.size();
    }
}
