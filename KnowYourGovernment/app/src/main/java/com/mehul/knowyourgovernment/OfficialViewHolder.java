package com.mehul.knowyourgovernment;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;


public class OfficialViewHolder extends RecyclerView.ViewHolder {

    public TextView office;
    public TextView name;

    public OfficialViewHolder(View view){
        super(view);
        office = view.findViewById(R.id.officeValID);
        name = view.findViewById(R.id.nameValID);
    }

}
