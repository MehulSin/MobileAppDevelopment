package com.example.mehul.stockwatch;

import android.net.Uri;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class StockAsyncClass extends AsyncTask<String, Integer, String> {

    private static final String TAG = "StockSyncTask";
    private MainActivity mainActivity;
    public static String STOCK_URL = "https://api.iextrading.com/1.0/stock/";
    public static boolean excuting = false;
    public String REQUIRED_URL;
    private ArrayList<StockData> stockValues = new ArrayList<>();
    private String symbol;


    public StockAsyncClass(MainActivity mainact,String sym) {
        mainActivity = mainact;
        this.symbol = sym;
    }

    @Override
    protected void onPreExecute() {
        Toast.makeText(mainActivity, "Loading Stock Data...", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected String doInBackground(String... params) {
        REQUIRED_URL = STOCK_URL + symbol + "/quote";
        Uri stockDetailUri = Uri.parse(REQUIRED_URL);
        String urlToFinalUse = stockDetailUri.toString();
        StringBuilder wantedData = new StringBuilder();
        String sentence;
        try {
            URL urlNew = new URL(urlToFinalUse);
            HttpURLConnection conn1 = (HttpURLConnection) urlNew.openConnection();
            conn1.setRequestMethod("GET");
            InputStream is1 = conn1.getInputStream();
            BufferedReader reader1 = new BufferedReader(new InputStreamReader(is1));
            while ((sentence = reader1.readLine()) != null) {
                wantedData.append(sentence).append("/n");
            }

        } catch (Exception e) {
        }
        return wantedData.toString();
    }

    public ArrayList<String> getSymbol(String s){
        ArrayList<String> symList = new ArrayList<>();
        try {
            JSONArray jArray = new JSONArray(s);
            for (int i=0;i<jArray.length();i++ ){
                JSONObject jStock = (JSONObject) jArray.get(i);
                String symbol = jStock.getString("symbol");
                symList.add(symbol);
            }
        }
        catch (Exception e)
        {

        }
        return symList;
    }


    public ArrayList<StockData> parseJSON(String s){
        ArrayList<StockData> stockList = new ArrayList<>();
        try{
            if(!s.equals("httpserver.cc: Response Code 404")) {
                JSONObject jobj = new JSONObject(s);
                    String symbol = jobj.getString("symbol");
                        String cName = jobj.getString("companyName");
                        String latestPrice = jobj.getString("latestPrice");
                        Double latestStockVal = Double.parseDouble(latestPrice);
                        String change = jobj.getString("change");
                        Double stockChange = Double.parseDouble(change);
                        String stockDiffPercent = jobj.getString("changePercent");
                        Double stockPercentDiff = Double.parseDouble(stockDiffPercent);

                        stockList.add(
                                new StockData(symbol, cName, latestStockVal, stockChange, stockPercentDiff));

            }
        }
         catch (JSONException e1)
        {
            e1.printStackTrace();
        }
        return stockList;
    }

    @Override
    protected void onPostExecute(String s) {
        ArrayList<StockData> stockFullList = parseJSON(s);
        if (stockFullList != null)
            mainActivity.whenAsyncTaskStockDone(stockFullList);
            excuting = false;
    }
}
