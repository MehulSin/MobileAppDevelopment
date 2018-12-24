package com.mehul.knowyourgovernment;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.location.Geocoder;
import android.app.ActionBar;
import android.widget.TextView;

import java.util.List;

public class OfficialAdapter extends RecyclerView.Adapter<OfficialViewHolder> {

    private List<Official> officialDataList;
    private MainActivity mainact;


    public OfficialAdapter(List<Official> offList, MainActivity ma){
        this.officialDataList = offList;
        mainact = ma;
    }

    @Override
    public OfficialViewHolder onCreateViewHolder(final ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.official_list_row2, parent, false);

        itemView.setOnClickListener(mainact);
        itemView.setOnLongClickListener(mainact);

        return new OfficialViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(OfficialViewHolder holder, int position){
        Official official = officialDataList.get(position);
        holder.name.setText(String.format("%s (%s)",official.getName(),official.getParty()));
        holder.office.setText(official.getOffice());
    }

    @Override
    public int getItemCount(){ return officialDataList.size();}
}
