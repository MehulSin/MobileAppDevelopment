package com.example.mehul.stockwatch;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class StockViewHolder extends RecyclerView.ViewHolder {

    public TextView stockSymbol;
    public TextView stockVal;
    public TextView stockDiff;
    public TextView stockName;
    public TextView stockDiffPercent;

    public StockViewHolder(View v){
        super(v);
        stockSymbol = v.findViewById(R.id.stockSymbol);
        stockVal = v.findViewById(R.id.stockVal);
        stockDiff = v.findViewById(R.id.stockDiff);
        stockName = v.findViewById(R.id.stockName);
        stockDiffPercent = v.findViewById(R.id.stockdiffPercent);
    }

}
