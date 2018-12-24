package com.example.mehul.stockwatch;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class StockAdapter extends RecyclerView.Adapter<StockViewHolder> {

    private List<StockData> stockList;
    private MainActivity ma;

    public StockAdapter(List<StockData> stklist, MainActivity mainAct)
    {
        this.stockList = stklist;
        ma=mainAct;
    }

    @NonNull
    @Override
    public StockViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.stock_layout,viewGroup,false);
        itemView.setOnClickListener(ma);
        itemView.setOnLongClickListener((View.OnLongClickListener) ma);
        return new StockViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull StockViewHolder stockViewHolder, int position) {
        StockData stockData =stockList.get(position);
        stockViewHolder.stockSymbol.setText(stockData.getStockSymbol());
        stockViewHolder.stockVal.setText(String.format("%f",stockData.getStockVal()));
        stockViewHolder.stockDiff.setText(String.format("%f",stockData.getStockDiff()));
        String stockDiff = (String.format("%f",stockData.getStockDiffInPercent()));
        stockViewHolder.stockDiffPercent.setText("(" + stockDiff + "%)");
        stockViewHolder.stockName.setText(stockData.getStockName());

        if(stockData.getStockDiff()<0){
            stockViewHolder.stockSymbol.setTextColor(Color.parseColor("#FF0040"));
            stockViewHolder.stockName.setTextColor(Color.parseColor("#FF0040"));
            stockViewHolder.stockVal.setTextColor(Color.parseColor("#FF0040"));
            stockViewHolder.stockDiff.setText('\u25BC'+stockData.getStockDiff().toString());
            stockViewHolder.stockDiff.setTextColor(Color.parseColor("#FF0040"));
            stockViewHolder.stockDiffPercent.setTextColor(Color.parseColor("#FF0040"));
        }
        else {
            stockViewHolder.stockSymbol.setTextColor(Color.parseColor("#00FF00"));
            stockViewHolder.stockName.setTextColor(Color.parseColor("#00FF00"));
            stockViewHolder.stockVal.setTextColor(Color.parseColor("#00FF00"));
            stockViewHolder.stockDiff.setText('\u25B2'+stockData.getStockDiff().toString());
            stockViewHolder.stockDiff.setTextColor(Color.parseColor("#00FF00"));
            stockViewHolder.stockDiffPercent.setTextColor(Color.parseColor("#00FF00"));

        }


    }

    @Override
    public int getItemCount() {
        return stockList.size();
    }
}
