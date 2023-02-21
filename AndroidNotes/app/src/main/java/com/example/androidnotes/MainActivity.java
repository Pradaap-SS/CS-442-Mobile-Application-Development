package com.example.androidnotes;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener, View.OnLongClickListener {
    private final List<Notes> notesList = new ArrayList<>();
    private RecyclerView recyclerView;

    // activity result launcher -> expecting and receiving return data
    private ActivityResultLauncher<Intent> activityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // reference to recycler view
        recyclerView = findViewById(R.id.recycler);

        // instantiating variable to expect results
        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), this::getAllNotes);

        // get notes list saved from last session saved in a JSON file
        getNotesFromJson();
    }

    private void getNotesFromJson(){
        try
        {
            BufferedReader fileBufferReader = new BufferedReader(
                    new InputStreamReader(getApplicationContext().openFileInput("AndroidNotes.json"), StandardCharsets.UTF_8));
            StringBuilder stringBuilder = new StringBuilder();
            if(fileBufferReader != null)
            {
                String strLine;
                while ((strLine = fileBufferReader.readLine()) != null)
                    stringBuilder.append(strLine);
            }
            else
                return;
            JSONArray jsonArr = new JSONArray(stringBuilder.toString());
            for (int i = 0; i < jsonArr.length(); i++) {
                JSONObject jsonObj = jsonArr.getJSONObject(i);

                String title = jsonObj.getString("notes_title"); // data fetched and stored using id's defined
                String dateModified = jsonObj.getString("notes_date_modified");
                String notesContent = jsonObj.getString("notes_content");

                Notes notesObj = new Notes(title, dateModified, notesContent); // Creating a note list of object Notes
                if(notesObj != null)
                    notesList.add(notesObj);
                else
                {
                   Toast.makeText(getApplicationContext(), "Null Notes object returned ", Toast.LENGTH_LONG).show();
                   return;
                }
            }
            setViewWithAllNotes(); // updated list of notes set in the view activity.
        }
        catch (Exception e) {
            e.printStackTrace();
            notesList.clear();
            setViewWithAllNotes();
        }
    }


    private void getAllNotes(ActivityResult activityResult)
    {
        try
        {
            if (activityResult == null || activityResult.getData() == null) {
                return;
            }
            Notes notesObj;
            Intent getData = activityResult.getData();
            if (activityResult.getResultCode() != RESULT_OK) {
                return;
            }
            notesObj = (Notes) getData.getSerializableExtra("NOTES CLASS");
            int notesPos = getData.getIntExtra("position",-1);
            if (notesObj == null) {
                return;
            }
            // if notes exits, store edited Notes object at existing position
                    // else add new notes at the end of Notes List.
            Notes newNotesObj;
            newNotesObj = new Notes(notesObj.getNotesTitle(),
                    notesObj.getNotesLastModified(), notesObj.getNotesContent());
            if (notesPos != -1) {
                notesList.set(notesPos, newNotesObj);
            }
            else {
                notesList.add(newNotesObj);
            }
            saveJsonFile();// Write and save the notes list in the json file.
        }
        catch (Exception ex)
        {
            ex.getStackTrace();
        }
    }

    private void saveJsonFile()
    {
        try {
            // Using file output stream, Save the notes list in the json file
            FileOutputStream fileOutputStream = getApplicationContext().openFileOutput("AndroidNotes.json", Context.MODE_PRIVATE);
            PrintWriter printWriter = new PrintWriter(fileOutputStream);
            printWriter.print(notesList);
            printWriter.close();
            fileOutputStream.close();
            setViewWithAllNotes(); // Updated view with all notes created.
        }
        catch(Exception ex)
        {
            ex.getStackTrace();
        }
    }

    public void setViewWithAllNotes()
    {
        try
        {
            setTitle("AndroidNotes (" + notesList.size() + ")"); // set title with total number of notes present

            // sort the notes based on last modified date and time.
            Collections.sort(notesList, (noteA, noteB) -> {
                if (noteA.getNotesLastModified() == null || noteB.getNotesLastModified() == null)
                {
                    return 0;
                }
                return noteA.getNotesLastModified().compareTo(noteB.getNotesLastModified());
            });
            Collections.reverse(notesList); // reverse the list to reorder in descending date and time.
            NotesAdapter notesAdaptor = new NotesAdapter(notesList, this);
            recyclerView.setAdapter(notesAdaptor);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(linearLayoutManager);
        }
        catch(Exception ex)
        {
            Toast.makeText(this, "Exception handle setViewWithAllNotes()", Toast.LENGTH_SHORT).show();
            ex.getStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.activity_add_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int menuId = item.getItemId();
        if (menuId == R.id.info) {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
            return true;
        }
        else if (menuId == R.id.add_notes) {
            Intent intent = new Intent(this, EditActivity.class);
            activityResultLauncher.launch(intent);
            return true;
        }
        else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View view) {
        int notePos = recyclerView.getChildLayoutPosition(view);
        Notes noteObj = notesList.get(notePos); // get the selected note
        if (noteObj != null)
        {
            Intent dataIntent = new Intent(this, EditActivity.class);
            dataIntent.putExtra("NOTES CLASS", noteObj);
            dataIntent.putExtra("position", notePos);
            activityResultLauncher.launch(dataIntent);   // launch Edit Activity
        }
    }

    @Override
    public boolean onLongClick(View view)
    {
        int notePos = recyclerView.getChildLayoutPosition(view);
        Notes noteObj = notesList.get(notePos);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setPositiveButton("Yes", (dialog, which) -> { dialog.dismiss();
            notesList.remove(notePos);
            setViewWithAllNotes();
            saveJsonFile();
        });
        dialogBuilder.setNegativeButton("NO", (dialog, which) -> dialog.dismiss());
        dialogBuilder.setTitle("Delete Note '"+ noteObj.getNotesTitle()+"'?");
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
        return false;
    }
}