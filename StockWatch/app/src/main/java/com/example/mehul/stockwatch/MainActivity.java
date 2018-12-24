package com.example.mehul.stockwatch;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.LinearLayout;
import android.widget.Toast;
import java.util.Collections;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static android.os.AsyncTask.*;
import static java.util.Collection.*;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,View.OnLongClickListener{

    private SwipeRefreshLayout swiper;
    private RecyclerView recycle;
    private StockAdapter sAdapter;
    private List<StockData> stockList =new ArrayList<>();
    private List<String> symList = new ArrayList<>();
    private DatabaseClass databaseClass;
    private StockData sdata;
    private int lastSelectedPostion;
    public static boolean dataValid = false;
    public String userInputValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recycle = findViewById(R.id.recycle);
        swiper = findViewById(R.id.swiper);
        sAdapter = new StockAdapter(stockList,this);
        recycle.setAdapter(sAdapter);
        recycle.setLayoutManager(new LinearLayoutManager(this));
        databaseClass = new DatabaseClass(this);
        databaseClass.dumpdbtoLog();
        ArrayList<String[]> list = databaseClass.loadStocks();
        for(int j=0;j<list.size();j++)
        {
            stockList.add(new StockData(list.get(j)[0],list.get(j)[1],0.0,0.0,0.0));
            symList.add(stockList.get(j).getStockSymbol());
        }

        Collections.sort(stockList,Collections.reverseOrder());


        if(stockList.size()==0){
            Toast.makeText(this, "No data added", Toast.LENGTH_SHORT).show();
        }
        else{
            if (internetFine()) {
                updateStock();
                sAdapter.notifyDataSetChanged();
            } else {
                errorInInternet();
            }}
        sAdapter.notifyDataSetChanged();
        if(internetFine()) {
            swiper.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    updateStock();
                    Toast.makeText(getApplicationContext(), "Swipper Refreshed", Toast.LENGTH_SHORT).show();
                    swiper.setRefreshing(false);
                }
            });
        }
        else{
            errorInInternet();
        }
    }



    public void updateStock(){
        if (internetFine()){
            int listSize = stockList.size();
            for(int i=0; i< listSize;i++){
                String symbol = stockList.get(stockList.size()-1).getStockSymbol();
                removeStock(stockList.size()-1);
                StockAsyncClass.excuting = true;
                new StockAsyncClass(this,symbol).execute();
            }
        }else{
            errorInInternet();
        }
        databaseClass.dumpdbtoLog();
        sAdapter.notifyDataSetChanged();
    }



    void removeStock(int pos){
        databaseClass.deleteStock(stockList.get(pos).getStockSymbol());
        stockList.remove(pos);
        symList.remove(pos);
        sAdapter.notifyDataSetChanged();
    }

    public void whenAsyncTaskStockDone (ArrayList<StockData> stockValues){
        if (stockValues.size() != 0){
            stockList.addAll(stockValues);
            Collections.sort(stockList);
            addStockVal(stockValues);
            sAdapter.notifyDataSetChanged();
        }else {
            errorNoStockPresent(stockValues.get(0).toString());
        }

    }


    void addStockVal(ArrayList<StockData> stockVal){
        if (! symList.contains(stockVal.get(0).getStockSymbol())) {
            databaseClass.addStock(stockVal);
            symList.add(0,stockVal.get(0).getStockSymbol());
        }
        sAdapter.notifyDataSetChanged();
    }

    public void errorNoStockPresent(String symVal){
        final AlertDialog alertDialogFDMatching = new AlertDialog.Builder(this)
                .setCancelable(true)
                .setTitle("Symbol not found : symVal")
                .setMessage("Data for stock symbol")
                .setNegativeButton("OK", new DialogInterface.OnClickListener() { public void onClick(DialogInterface dialog, int id) {    }})
                .show();
    }



    private boolean internetFine() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    private void errorInInternet () {
        final AlertDialog alertDialogDelete = new AlertDialog.Builder(this)
                .setCancelable(true)
                .setTitle("No Network Connection")
                .setMessage("Stock cannot be added without internet connection")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() { public void onClick(DialogInterface dialog, int id) {    }})
                .show();

    }


    public void whenAsyncIsDone(List<StockData> noteDataList) {

        this.stockList.clear();
        this.stockList.addAll(noteDataList);
        sAdapter.notifyDataSetChanged();

    }

    private void searchStockList(String stocksymbol){
        if(symList.contains(stocksymbol)){
            final AlertDialog alertDialogDuplicate = new AlertDialog.Builder(this)
                    .setCancelable(true)
                    .setTitle("Duplicate Stock")
                    .setMessage("Stock symbol" + stocksymbol+"already been added")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
        else if(stocksymbol.isEmpty() || stocksymbol.equals("")){
            final AlertDialog alertDialogDuplicate = new AlertDialog.Builder(this)
                    .setCancelable(true)
                    .setTitle("No symbol searched")
                    .setMessage("Please enter symbol to add")
                    .show();
        }
        else{

            StockSearchAsyncTaskClass.excuting = true;
            new StockSearchAsyncTaskClass(this,stocksymbol).execute();

        }

    }



    @Override

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.add_menu,menu);
        return true;
    }

    public void whenAsyncTaskSymbolIsDone(List<String[]> listSymbol) {
        if (listSymbol.size() == 0){
            final AlertDialog alertDialogStockMatching = new AlertDialog.Builder(this)
                    .setCancelable(true)
                    .setTitle("Symbol not found: "+ userInputValue)
                    .setMessage("Data for stock symbol")
                    .setNegativeButton("OK", new DialogInterface.OnClickListener() { public void onClick(DialogInterface dialog, int id) {    }})
                    .show();
        }
        else if (listSymbol.size() == 1)
        {
                     //StockAsyncClass.excuting = true;
                     //new StockAsyncClass(this,listSymbol.get(0)[0]).execute();
            openSelectedOne(listSymbol.get(0)[0]);
        }
         else{
                final String[] listPossibleSymbol = new String[listSymbol.size()];
                String[] listPossibleName = new String[listSymbol.size()];
                for (int i = 0; i< listSymbol.size(); i++){
                    listPossibleSymbol[i]=listSymbol.get(i)[0];
                    listPossibleName[i]=listSymbol.get(i)[0] + "-" + listSymbol.get(i)[1];
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Make a selection");
                builder.setNegativeButton("Nevermind",dialogClickListenerDelete);
                builder.setItems(listPossibleName, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {openSelectedOne(listPossibleSymbol[which]);}});
                AlertDialog dialog = builder.create();

                dialog.show();

            }

        sAdapter.notifyDataSetChanged();

    }




    public void openSelectedOne (String symbol)
    {
        if(symList.contains(symbol)){
            final AlertDialog alertDialogDuplicate = new AlertDialog.Builder(this)
                    .setCancelable(true)
                    .setTitle("Duplicate Stock")
                    .setMessage("Stock symbol "+ symbol + "is already displayed")
                    .show();
        }
        else{
            StockAsyncClass.excuting = true;
            new StockAsyncClass(this, symbol).execute();
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {


            case R.id.addStock:
                LayoutInflater li = LayoutInflater.from(MainActivity.this);
                View layout = li.inflate(R.layout.searchstock_layout, null);


                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setView(layout);
                final EditText userInput = (EditText) layout
                        .findViewById(R.id.searchStock);
                userInput.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
                userInput.setInputType(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
                userInput.setGravity(Gravity.CENTER_HORIZONTAL);

                final String stockSymbolAdded = userInput.getText().toString();
                userInputValue = userInput.getText().toString();
                if(internetFine()){
                builder.setTitle("Stock Selection")
                        .setMessage("Please enter a Stock Symbol")

                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                searchStockList(userInput.getText().toString());

                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getApplicationContext(),"Stock not added",Toast.LENGTH_SHORT).show();

                            }
                        })
                       // .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();}
                else{
                    errorInInternet();
                }

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onClick(View v) {
        int pos = recycle.getChildLayoutPosition(v);
        StockData stock = stockList.get(pos);
        if (internetFine()){
            goToStock(stock.getStockSymbol());

        }else {
            errorInInternet();

        }
    }

    @Override
    public boolean onLongClick(View v) {
        int pos = recycle.getChildLayoutPosition(v);
        StockData stock = stockList.get(pos);
        lastSelectedPostion = pos;
        final AlertDialog alertDialogDelete = new AlertDialog.Builder(this)
                .setCancelable(false)
                .setTitle("Delete Stock")
                .setMessage("Delete Stock Symbol" + stock.getStockSymbol()+" ?")
                .setPositiveButton("Yes",dialogClickListenerDelete)
                .setNegativeButton("No",dialogClickListenerDelete)
                .setIcon(android.R.drawable.ic_menu_delete)
                .show();
        return false;
    }

    DialogInterface.OnClickListener dialogClickListenerDelete = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    removeStock(lastSelectedPostion);
                    sAdapter.notifyDataSetChanged();
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    break;
            }
        }
    };




    public void goToStock(String symbol) {
        String url = "http://www.marketwatch.com/investing/stock/" + symbol;
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }


}
