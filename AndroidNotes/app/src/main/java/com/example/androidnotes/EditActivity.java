package com.example.androidnotes;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Date;

public class EditActivity extends AppCompatActivity {

    EditText editTitle, editContent;
    Notes notesObj;
    private int notePosition, editCounter;

    private void initialVariableValues(){
        editCounter = 0;
        notePosition = -1;
        notesObj = new Notes();
        editContent = findViewById(R.id.content);
        editTitle = findViewById(R.id.title);
    }

    private void getAndSetExistingValues() {
        Intent dataIntent = getIntent();
        if (!dataIntent.hasExtra("NOTES CLASS")) {
            editTitle.setText("");
            editContent.setText("");
        }
        else {
            notesObj = (Notes) dataIntent.getSerializableExtra("NOTES CLASS");
            if(notesObj != null) {
                notePosition = dataIntent.getIntExtra("position",-1);
                editTitle.setText(notesObj.getNotesTitle());
                editContent.setText(notesObj.getNotesContent());
            }
            else {
                Toast.makeText(this, "Returned Null value", Toast.LENGTH_SHORT).show();
                return;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        initialVariableValues();
        getAndSetExistingValues();
        editTitle.addTextChangedListener(new TextWatcher() {   // Title TextWatcher control
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                editCounter++;
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        editContent.addTextChangedListener(new TextWatcher() { // Content TextWatcher control
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                editCounter++;
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (editCounter == 0) {  //if note remains unchanged
            EditActivity.super.onBackPressed();
        }
        else
        {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
            dialogBuilder.setNegativeButton("No", (dialog, which) -> {dialog.dismiss();
                EditActivity.super.onBackPressed();
            });
            dialogBuilder.setPositiveButton("Yes", (dialog, which) -> { dialog.dismiss();
                saveEditedNote();
            });
            dialogBuilder.setTitle("Your Note is not saved!"+"\n"+"Save Note '"+editTitle.getText().toString()+"'?");
            AlertDialog alertDialog = dialogBuilder.create();
            alertDialog.show();
        }
    }

    public void saveEditedNote()
    {
        if (!TextUtils.isEmpty(editTitle.getText().toString())) { // check if title not null
            if(notesObj != null) {
                notesObj.setNotesTitle(editTitle.getText().toString()); // set edited values to note object
                notesObj.setNotesContent(editContent.getText().toString());
                notesObj.setNotesLastModified("" + new Date());
                Intent dataIntent = new Intent();     // new intent created to save the object and its position
                dataIntent.putExtra("NOTES CLASS", notesObj);
                dataIntent.putExtra("position", notePosition);
                setResult(RESULT_OK, dataIntent);
                finish();
            }
            else
                Toast.makeText(this, "Null value returned from saveEditedNote()", Toast.LENGTH_SHORT).show();
        }
        else {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
            dialogBuilder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
            dialogBuilder.setPositiveButton("OK", (dialog, which) -> { dialog.dismiss();
                EditActivity.super.onBackPressed();
            });
            dialogBuilder.setTitle("Note without a title will not be saved!\n Discard changes?");
            AlertDialog alertDialog = dialogBuilder.create();
            alertDialog.show();
            return;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_save_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() != R.id.save_menu) {
            return super.onOptionsItemSelected(item);
        }
        else {
            if (editCounter == 0) {  //if note remains unchanged
                EditActivity.super.onBackPressed();
                return false;
            }
            if(!TextUtils.isEmpty(editTitle.getText().toString())) { // check if title not null
                if(notesObj != null) {
                    notesObj.setNotesTitle(editTitle.getText().toString()); // set edited values to note object
                    notesObj.setNotesContent(editContent.getText().toString());
                    notesObj.setNotesLastModified("" + new Date());
                    Intent dataIntent = new Intent(); // new intent created to save the object and its position
                    dataIntent.putExtra("NOTES CLASS", notesObj);
                    dataIntent.putExtra("position", notePosition);
                    setResult(RESULT_OK, dataIntent);
                    finish();
                }
            }
            else {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
                dialogBuilder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
                dialogBuilder.setPositiveButton("Yes", (dialog, which) -> {
                    dialog.dismiss();
                    EditActivity.super.onBackPressed();
                });
                dialogBuilder.setTitle("Note without a title will not be saved!\n Do you want to exit?");
                AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.show();
            }
            return true;
        }
    }
}