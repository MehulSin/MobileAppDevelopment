package com.mehulsingh.multinotes;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.JsonWriter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.mehulsingh.multi_notepad.R;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
    implements View.OnClickListener, View.OnLongClickListener{

        private static final String TAG = "MainActivity";
        private List<Note> noteDataList = new ArrayList<>();
        private RecyclerView recycle;
        private NoteAdapter nAdapter;
        private int lastSelectedPos = 0;
        private static final int EDIT = 1;
        private static final int CREATE = 2;
        public int value;
        public String data ="";


        @Override
        protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recycle = (RecyclerView) findViewById(R.id.recyclerViewMainPage);
        nAdapter = new NoteAdapter(noteDataList, this);
        recycle.setAdapter(nAdapter);
        recycle.setLayoutManager(new LinearLayoutManager(this));

        MyAsyncTask.excuting = true;
        new MyAsyncTask(this).execute();
        }

    @Override
    public void onClick(View view) {

        int position = recycle.getChildLayoutPosition(view);
        Note ndata = noteDataList.get(position);
        lastSelectedPos = position;
        Intent intent = new Intent(MainActivity.this, NoteEditActivity.class);
        intent.putExtra(Intent.EXTRA_TEXT, "Edit note");
        intent.putExtra("Title", ndata.getTitle());
        intent.putExtra("Text", ndata.getNoteContent());
        startActivityForResult(intent, EDIT);
    }

    @Override
    public boolean onLongClick(View view) {
        int position = recycle.getChildLayoutPosition(view);
        Note m = noteDataList.get(position);
        lastSelectedPos = position;
        final AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setCancelable(false)
                .setTitle("Confirmation")
                .setMessage(" " + m.getTitle()+" ?")
                .setPositiveButton("Yes",dialogClickListener)
                .setNegativeButton("No",dialogClickListener)
                .show();

        return false;
    }


    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    noteDataList.remove(lastSelectedPos);
                    nAdapter.notifyDataSetChanged();
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    break;
            }
        }
    };



        @Override
        public boolean onCreateOptionsMenu (Menu menu){
        getMenuInflater().inflate(R.menu.mainmenu, menu);
        return true;
    }


        @Override
        public boolean onOptionsItemSelected (MenuItem item){
        switch (item.getItemId()) {
            case R.id.savemainmenu:
               lauchEditActivity();
                break;
            case R.id.menuinfo:
                Intent intent = new Intent(MainActivity.this, InfoDataActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void lauchEditActivity() {
        Intent intent = new Intent(MainActivity.this, NoteEditActivity.class);
        startActivityForResult(intent, CREATE);
    }

    public void lauchVal() {
        Intent intent = new Intent(MainActivity.this, NoteEditActivity.class);
        startActivityForResult(intent, CREATE);
    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intentval) {
        if (requestCode == CREATE) {
            if (resultCode == RESULT_OK) {
                String newNoteText = intentval.getStringExtra("NOTE_TEXT");
                String newNoteTitle = intentval.getStringExtra("NOTE_TITLE");
                String newNoteDate = intentval.getStringExtra("NOTE_DATE");
                if ( !newNoteTitle.equals("")){
                    noteDataList.add(0,new Note(newNoteTitle, newNoteText, newNoteDate));
                }
                nAdapter.notifyDataSetChanged();

            } else
                {
                Toast.makeText(this, "No title was present for the note and it didn't saved", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onActivityResult: result Code of data is " + resultCode);
            }

        }
        else if (requestCode == EDIT) {
            if (resultCode == RESULT_OK) {
                String newNoteText = intentval.getStringExtra("NOTE_TEXT");
                String newNoteTitle = intentval.getStringExtra("NOTE_TITLE");
                String newNoteDate = intentval.getStringExtra("NOTE_DATE");
                noteDataList.remove(lastSelectedPos);
                noteDataList.add(0,new Note(newNoteTitle, newNoteText, newNoteDate));
                nAdapter.notifyDataSetChanged();

            } else {
                Log.d(TAG, "onActivityResult: result Code of data is " + resultCode);
            }

        }
    }

    @Override
    protected void onPause() {
         saveFinalNotes();
        super.onPause();
    }

    @Override
    protected void onResume() {
         super.onResume();
    }

    @Override
    protected void onStart() {
         super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private void saveFinalNotes() {
      try {
            FileOutputStream fos = getApplicationContext().openFileOutput(getString(R.string.jsonfilename), Context.MODE_PRIVATE);
            JsonWriter writer = new JsonWriter(new OutputStreamWriter(fos, getString(R.string.encoding)));
            writer.setIndent("  ");
            writer.beginObject();
            writer.name("number notes").value(noteDataList.size());
            int realI = 1;
            for (int i = 1; i <= noteDataList.size(); i++){
                if (noteDataList.get(i-1).getTitle().equals("")){
                    Log.d(TAG, "saveFinalNotes: Element " + i + " doesn't have a title");
                }
                else{
                    writer.name("title " + realI).value(noteDataList.get(i-1).getTitle());
                    writer.name("date " + realI).value(noteDataList.get(i-1).getNoteSavedDate());
                    writer.name("text " + realI).value(noteDataList.get(i-1).getNoteContent());
                    realI ++;
                }
            }
            writer.endObject();
            writer.close();

            Log.d(TAG, "saveFinalNotes: Done");
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    public void whenAsyncIsDone(List<Note> noteDataList) {
        Log.d(TAG, "whenAsyncIsDone Start");
        this.noteDataList.clear();
        this.noteDataList.addAll(noteDataList);
        nAdapter.notifyDataSetChanged();
        Log.d(TAG, "whenAsyncIsDone ");

    }

    public void readList (List<Note> myList){
            for (int i = 0; i < myList.size();i++){
                Log.d(TAG, "Title "+i+" is " + myList.get(i).getTitle());
                Log.d(TAG, "Date  "+i+" is " + myList.get(i).getNoteSavedDate());
                Log.d(TAG, "Text  "+i+" is " + myList.get(i).getNoteContent());
            }
    }
}
