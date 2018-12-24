package com.mehulsingh.multinotes;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.mehulsingh.multi_notepad.R;

/**
 * Created by belle on 03/02/2018.
 */

public class MyViewHolder extends RecyclerView.ViewHolder {

    public TextView nTitle;
    public TextView nText;
    public TextView noteSavedDate;

    public MyViewHolder(View view) {
        super(view);
        nTitle = (TextView) view.findViewById(R.id.noteTitle);
        nText = (TextView) view.findViewById(R.id.noteText);
        noteSavedDate = (TextView) view.findViewById(R.id.noteLastSavedDate);
    }

}
