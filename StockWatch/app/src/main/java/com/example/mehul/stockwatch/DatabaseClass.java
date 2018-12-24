package com.example.mehul.stockwatch;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseClass extends SQLiteOpenHelper{
    private SQLiteDatabase db;
    private static final String TABLE = "StockWatchTable";
    private static final String TAG = "DatabaseHandler";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "StockWatchDB";
    //Database column
    private static final String SYMBOL = "Symbol";
    private static final String CNAME = "CompanyName";
    private MainActivity ma;

    public DatabaseClass(MainActivity context) {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
        ma = context;
            db = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
       database.execSQL( "CREATE TABLE " + TABLE + " (" + SYMBOL + " TEXT not null unique," + CNAME + " TEXT not null)");
    }

    public void dumpdbtoLog() {
        Cursor cursor = db.rawQuery("Select * from " + TABLE,null);
        if(cursor!=null){
            cursor.moveToFirst();
            Log.d(TAG,"======================Dump data to log========================");
            for(int i=0;i<cursor.getCount();i++){
                String symbol = cursor.getString(0);
                String companyName = cursor.getString(1);
                Log.d(TAG,"Dump data : " + String.format("%s %-18s",SYMBOL + ":",symbol) + String.format("%s %-18s",CNAME +":",companyName));
                cursor.moveToNext();
            }
            cursor.close();
        }
        Log.d(TAG,"======================Dump data to log========================");
    }

    public ArrayList<String[]> loadStocks() {
        ArrayList<String[]> stockList = new ArrayList<>();
        Cursor cursor = db.query(
                TABLE,
                new String[]{SYMBOL, CNAME},
                null,
                null,
                null,
                null,
                null);
        if (cursor != null) {
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                String symbol = cursor.getString(0);
                String company = cursor.getString(1);
                stockList.add(new String[] {symbol, company});
                cursor.moveToNext();
            }
            cursor.close();
        }
        Log.d(TAG, "loadStocks: DONE");
        return stockList;
    }

    public void addStock(ArrayList<StockData> stockVal) {
        ContentValues values = new ContentValues();
        values.put(SYMBOL, stockVal.get(0).getStockSymbol());
        values.put(CNAME, stockVal.get(0).getStockName());
        long key = db.insert(TABLE, null, values);
        }

    public void updateStock(StockData stock) {
        ContentValues values = new ContentValues();
        values.put(SYMBOL, stock.getStockSymbol());
        values.put(CNAME, stock.getStockName());
        long key = db.update(TABLE, values, SYMBOL + " = ?", new String[]{stock.getStockSymbol()});
    }

    public void deleteStock(String symbolName) {
        int cnt = db.delete(TABLE, SYMBOL + " = ?", new String[]{symbolName});
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
