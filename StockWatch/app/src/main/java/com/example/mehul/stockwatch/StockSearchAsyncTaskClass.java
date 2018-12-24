package com.example.mehul.stockwatch;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class StockSearchAsyncTaskClass extends AsyncTask<String, Integer, String> {

    private static final String TAG = "StockSyncTask";
    private MainActivity mainActivity;
    public static String SYMBOL_URL = "https://api.iextrading.com/1.0/ref-data/symbols";
    public static boolean excuting = false;
    public String REQUIRED_URL;
    private ArrayList<StockData> stockValues = new ArrayList<>();
    private String symbol;
    ArrayList<String[]> symList = new ArrayList<>();
    private List<String[]> listSymbolCompany = new ArrayList<>();

    public StockSearchAsyncTaskClass(MainActivity mainact,String sym) {
        mainActivity = mainact;
        this.symbol = sym.replaceAll("\\s+","");
        if (this.symbol.indexOf('.') != -1 ){
            Log.d(TAG, "Async create ignore true");
            excuting = true;
        }
    }

    @Override
    protected void onPreExecute() {
        Toast.makeText(mainActivity, "Loading Stock Symbol...", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected String doInBackground(String... strings) {
        Uri stockUri = Uri.parse(SYMBOL_URL);
        String urlToUse = stockUri.toString();
        String sentence;

        StringBuilder wantedData = new StringBuilder();
        try {
            URL url = new URL(urlToUse);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = reader.readLine()) != null) {
                wantedData.append(line).append('\n');
            }
            ArrayList<String[]> symArray = getSymbol(wantedData.toString());
            for(int i=0;i<symArray.size();i++){
                if(symArray.get(i)[0].startsWith(symbol)){
                    String symbol =symArray.get(i)[0].toString();
                    String companyName=symArray.get(i)[1].toString();
                    listSymbolCompany.add(new String[]{symbol,companyName});
                }
            }


        }
        catch (Exception e){
            Log.e(TAG,"doInBackgroundException :",e);
        }
        return "done";

    }


    public ArrayList<String[]> getSymbol(String s){

        try {
            JSONArray jArray = new JSONArray(s);
            for (int i=0;i<jArray.length();i++ ){
                JSONObject jStock = (JSONObject) jArray.get(i);
                String symData = jStock.getString("symbol");
                String companyName = jStock.getString("name");
                symList.add(new String[]{ symData,companyName});

            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return symList;
    }





    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        mainActivity.whenAsyncTaskSymbolIsDone(listSymbolCompany);
        excuting = false;
    }
}
