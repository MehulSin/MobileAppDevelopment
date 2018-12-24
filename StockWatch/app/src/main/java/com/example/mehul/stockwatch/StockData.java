package com.example.mehul.stockwatch;

import android.support.annotation.NonNull;

import java.io.Serializable;

public class StockData implements Serializable,Comparable<StockData>{

    private String stockSymbol;
    private String stockName;
    private Double stockVal;
    private Double stockDiff;
    private Double stockDiffInPercent;

    public StockData(){
    }

    public StockData( String stockSymbol,String stockName,Double stockVal, Double stockDiff,Double stockDiffInPercent) {
        this.stockSymbol = stockSymbol;
        this.stockVal = stockVal;
        this.stockDiff = stockDiff;
        this.stockName = stockName;
        this.stockDiffInPercent=stockDiffInPercent;
    }

    public String getStockSymbol() {
        return stockSymbol;
    }

    public Double getStockVal() {
        return stockVal;
    }

    public Double getStockDiff() {
        return stockDiff;
    }

    public String getStockName() {
        return stockName;
    }

    public Double getStockDiffInPercent() {
        return stockDiffInPercent;
    }

    public void setStockSymbol(String stockSymbol) {
        this.stockSymbol = stockSymbol;
    }

    public void setStockVal(Double stockVal) {
        this.stockVal = stockVal;
    }

    public void setStockDiff(Double stockDiff) {
        this.stockDiff = stockDiff;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public void setStockDiffInPercent(Double stockDiffInPercent){
        this.stockDiffInPercent = stockDiffInPercent;
    }

    @Override
    public String toString() {
        return this.stockSymbol + " " + this.stockName + " " + this.stockVal + " "+ this.stockDiff + " " + this.stockDiffInPercent;
    }

    public String toSaveFormat ()
    {
        return this.stockSymbol + " " + this.stockName + " " + this.stockVal + " "+ this.stockDiff + " " + this.stockDiffInPercent;
    }

    @Override
    public int compareTo(StockData sData) {
        /* For Ascending order*/
        return this.getStockSymbol().compareTo(sData.getStockSymbol());
    }
}
