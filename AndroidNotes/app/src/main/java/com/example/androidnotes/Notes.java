package com.example.androidnotes;

import android.util.JsonWriter;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringWriter;

public class Notes implements Serializable {
    public String notesTitle, notesDateModified, notesContent;

    public Notes(){
    }
    public Notes(String title, String dateModified, String content) {
        notesTitle = title;
        notesDateModified = dateModified;
        notesContent = content;
    }
    public String getNotesTitle() {
        return notesTitle;
    }

    public void setNotesTitle(String notesTitle) {
        this.notesTitle = notesTitle;
    }

    public String getNotesLastModified() {
        return notesDateModified;
    }

    public void setNotesLastModified(String notesDateModified) {
        this.notesDateModified = notesDateModified;
    }

    public String getNotesContent() {
        return notesContent;
    }

    public void setNotesContent(String notesContent) {
        this.notesContent = notesContent;
    }

    @NonNull
    public String toString() {
        // function to return the all contents as json file format
        try
        {
            StringWriter strWriter = new StringWriter();            // Json writer initialization
            JsonWriter jsonWriter = new JsonWriter(strWriter);
            jsonWriter.setIndent("  ");                             // setting indentation
            jsonWriter.beginObject();
            jsonWriter.name("notes_title").value(getNotesTitle());  // saving data as key-value pairs
            jsonWriter.name("notes_date").value(getNotesLastModified());
            jsonWriter.name("notes_body").value(getNotesContent());
            jsonWriter.endObject();
            jsonWriter.close();
            // string variable to store and return the json contents
            String androidNotesObj;
            androidNotesObj = strWriter.toString();
            return androidNotesObj;
        }
        catch (IOException ex) {
            ex.printStackTrace();
            return "";
        }
    }
}