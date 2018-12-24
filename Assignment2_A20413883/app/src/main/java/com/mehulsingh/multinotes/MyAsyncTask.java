package com.mehulsingh.multinotes;

import android.os.AsyncTask;
import android.util.JsonReader;

import com.mehulsingh.multi_notepad.R;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MyAsyncTask extends AsyncTask<Integer, Integer, List<Note>>{

    private static final String TAG = "MyAsyncTask";
    private MainActivity mainActivity;
    public static boolean excuting = false;

    public MyAsyncTask(MainActivity mainact) {
        mainActivity = mainact;
    }

    @Override
    protected List<Note> doInBackground(Integer... para) {
        List<Note> notesList = new ArrayList<>();
        boolean listInitialized = false;
        try {
            InputStream is = mainActivity.getApplicationContext().openFileInput(mainActivity.getString(R.string.jsonfilename));
            JsonReader reader = new JsonReader(new InputStreamReader(is, mainActivity.getString(R.string.encoding)));

            reader.beginObject();
            while (reader.hasNext()) {
                String name = reader.nextName();
                if (name.equals("number notes")) {
                    int pos = Integer.parseInt(reader.nextString());
                    for (int i = 1; i <= pos; i++) {
                        notesList.add(new Note("Title " + i, "Text " + i, "Date " + i));
                        }
                    listInitialized = true;
                } else if (name.substring(0, 4).equals("date") && listInitialized) {
                    notesList.get(Integer.parseInt(name.substring(5)) - 1).setNoteSavedDate(reader.nextString());

                } else if (name.substring(0, 4).equals("text") && listInitialized) {
                    notesList.get(Integer.parseInt(name.substring(5)) - 1).setNoteContext(reader.nextString());

                } else if (name.substring(0, 5).equals("title") && listInitialized) {
                    notesList.get(Integer.parseInt(name.substring(6)) - 1).setTitle(reader.nextString());
                    } else {
                    reader.skipValue();
                    }
            }
            reader.endObject();

        } catch (FileNotFoundException e) {
           }
           catch (Exception e) {
            e.printStackTrace();
        }
       return notesList;

    }

    @Override
    protected void onPostExecute(List<Note> myList) {
        super.onPostExecute(myList);
        mainActivity.whenAsyncIsDone(myList);

        excuting = false;
        }

}
