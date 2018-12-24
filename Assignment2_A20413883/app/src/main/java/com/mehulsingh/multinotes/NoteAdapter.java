package com.mehulsingh.multinotes;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mehulsingh.multi_notepad.R;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<MyViewHolder> {

    private static final String TAG = "NoteAdapter";
    private List<Note> notesList;
    private MainActivity mainAct;

    public NoteAdapter(List<Note> empList, MainActivity mactivity) {
        this.notesList = empList;
        mainAct = mactivity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notelistviewactivity, parent, false);

        itemView.setOnClickListener(mainAct);
        itemView.setOnLongClickListener((View.OnLongClickListener) mainAct);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Note myNote = notesList.get(position);
        holder.nTitle.setText(myNote.getTitle());
        if (myNote.getNoteContent().length() < 80 ){
            holder.nText.setText(myNote.getNoteContent());
        }
        else {
            holder.nText.setText(myNote.getNoteContent().substring(0,80) + " ...");
        }
        holder.noteSavedDate.setText(myNote.getNoteSavedDate());
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }

}
