package com.mehulsingh.multinotes;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.mehulsingh.multi_notepad.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class NoteEditActivity extends AppCompatActivity {

    private static final String TAG = "NoteEditActivity";

    public static final int RESULT_NO_MODIFICATION = 5;

    private String actualText = "";
    private String actualTitle = "";

    private EditText editNoteText;
    private EditText editNoteTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editnoteactivity);

        editNoteText = (EditText) findViewById(R.id.editNote);
        editNoteTitle = (EditText) findViewById(R.id.editNoteTitle);

        editNoteText.setMovementMethod(new ScrollingMovementMethod());

        Intent intent = getIntent();
        if (intent.hasExtra(Intent.EXTRA_TEXT)) {
            actualTitle = intent.getStringExtra("Title");
            editNoteTitle.setText(actualTitle);
            actualText = intent.getStringExtra("Text");
            editNoteText.setText(actualText);
        }
        else {
            editNoteTitle.setText("");
            editNoteText.setText("");
        }

        Log.d(TAG, " Open ");

    }

    public void saveNote () {
        Log.d(TAG, "saveNote: Start");
        if(editNoteTitle.getText().toString().equals("")){
             setResult(RESULT_CANCELED);
        }
        else {
            if (editNoteTitle.getText().toString().equals(actualTitle) && editNoteText.getText().toString().equals(actualText) && !editNoteText.getText().toString().equals("")){
                setResult(RESULT_NO_MODIFICATION);
              }
            else {

                Intent data = new Intent(); // Used to hold results data to be returned to original activity
                data.putExtra("NOTE_TEXT", editNoteText.getText().toString());
                data.putExtra("NOTE_TITLE", editNoteTitle.getText().toString());
                data.putExtra("NOTE_DATE", "" + new SimpleDateFormat("dd MMM yyyy - HH:mm").format(Calendar.getInstance().getTime()));
                setResult(RESULT_OK, data);
            }
            Log.d(TAG, "saveNote: Title OK");
        }
        finish(); // This closes the current activity, returning us to the original activity
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        getMenuInflater().inflate(R.menu.editmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item){
        switch (item.getItemId()) {
            case R.id.editmenusave:
                Log.d(TAG, "onOptionsItemSelected : " + item.getTitle());
                saveNote();
                break;
            default:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if ( !editNoteText.getText().toString().equals(actualText) || !editNoteTitle.getText().toString().equals(actualTitle)){

            final AlertDialog alertDialog = new AlertDialog.Builder(this)
                    .setCancelable(false)
                    .setTitle("Confirmation")
                    .setMessage("Your note hasn't been saved ! \n Do you want to save note \" "+ editNoteTitle.getText()+"\" ?")
                    .setPositiveButton("Save",dialogClickListener)
                    .setNegativeButton("Don't save",dialogClickListener)
                    .show();

        }
        else {
            super.onBackPressed();
        }
    }

    public void onSoftBackPressed() {
          if ( !editNoteText.getText().toString().equals(actualText)){
           final AlertDialog alertDialog = new AlertDialog.Builder(this)
                    .setCancelable(false)
                    .setTitle("Confirmation")
                    .setMessage("Your note hasn't been saved ! \n Do you want to save note \" "+ editNoteTitle.getText()+"\" ?")
                    .setPositiveButton("Save",dialogClickListener)
                    .setNegativeButton("Don't save",dialogClickListener)
                    .show();
        }
        else {
            finish();
        }
    }

    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    saveNote();
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                     finish();
                    break;
            }
        }
    };
}


