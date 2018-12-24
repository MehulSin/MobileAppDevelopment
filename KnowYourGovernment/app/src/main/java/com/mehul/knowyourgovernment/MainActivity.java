package com.mehul.knowyourgovernment;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.text.InputType;
import android.util.JsonWriter;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener, View.OnLongClickListener {

    private MainActivity mainActivity = this;
    private RecyclerView recycle;
    private List<Official> officialList = new ArrayList<>();
    private OfficialAdapter officialAdapter;
    private TextView locView;
    private Locator locator;
    private Map<String, String> states = new HashMap<String, String>();

    private Map<String, String> reverseStates= new HashMap<String, String>();
    boolean isMapInitzialized = false ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().getDecorView().setBackgroundColor( getResources().getColor( R.color.purple));
        recycle = findViewById(R.id.recycleID);
        officialAdapter = new OfficialAdapter(officialList, this);
        recycle.setAdapter(officialAdapter);
        recycle.setLayoutManager(new LinearLayoutManager(this));

        locView = findViewById(R.id.locationValueID);
        locView.setTextColor(getResources().getColor(R.color.white));
        validateLocation();
    }



    private void validateLocation(){
        if(connected()) {
            locator = new Locator(this);
            locator.shutdown();
        } else{
            noInternetDialog();
        }
    }



    private void saveData(){
        try {
            FileOutputStream fos = getApplicationContext().openFileOutput(getString(R.string.file_name), Context.MODE_PRIVATE);
            JsonWriter writer = new JsonWriter(new OutputStreamWriter(fos, getString(R.string.encoding)));
            writer.setIndent(("  "));
            writer.beginObject();
            writer.name("norminput").value(locView.getText().toString());
            writer.endObject();
            writer.beginArray();
            for(int i = 0; i < officialList.size(); i++){
                writer.beginObject();
                writer.endObject();
            }
            writer.endArray();
            writer.close();
            } catch (Exception e){
            e.getStackTrace();
        }
    }
   public void setOfficialList(Object[] results){
        try {
            if (results == null) {
                textData();
                officialList.clear();
            } else {
                locView.setText(results[0].toString());
                officialList.clear();
                ArrayList<Official> offList = (ArrayList<Official>) results[1];
                for (int i = 0; i < offList.size(); i++) {
                    officialList.add(offList.get(i));
                }
            }
            officialAdapter.notifyDataSetChanged();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    public void textData()
    {
        locView.setText("No Data For Location");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        try{
        if (requestCode == 5) {
            for (int i = 0; i < permissions.length; i++) {
                if (permissions[i].equals(Manifest.permission.ACCESS_FINE_LOCATION)) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        locator.setUpLocationManager();
                        locator.determineLocation();
                        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
                    } else {
                        Toast.makeText(this, "Location permission was denied - cannot determine address", Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }


    void locationDetail()
    {
        try{

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }


    public void setAddress(double latitude, double longitude) {

        List<Address> addresses = null;
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            try {
                addresses = geocoder.getFromLocation(latitude, longitude, 1);
                Address ad = addresses.get(0);
                String zipcode = ad.getPostalCode();
                new AsyncOfficialLoader(mainActivity).execute(zipcode);

            } catch (Exception e) {
                e.printStackTrace();
                }
    }



    public void noLocationAvailable() {
        Toast.makeText(this, "No location providers were available", Toast.LENGTH_LONG).show();
    }


    @Override
    public void onClick(View v){
        Intent intent = new Intent(MainActivity.this, OfficialActivity.class);
        int pos = recycle.getChildLayoutPosition(v);
        Official o = officialList.get(pos);
        intent.putExtra("header", locView.getText().toString() );
        Bundle bundle = new Bundle();
        bundle.putSerializable("official", o);
        intent.putExtras(bundle);
        startActivity(intent);

    }

    @Override
    public boolean onLongClick(View v){
        int pos = recycle.getChildLayoutPosition(v);
        onClick(v);
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.action_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.searchLocation:
                if(connected()){
                    searchDialog();
                }
                else{
                    noInternetDialog();
                }
                return true;
            case R.id.aboutActivityID:
                Intent intent = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intent);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void searchDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final EditText et = new EditText(this);
        et.setInputType(InputType.TYPE_CLASS_TEXT );
        et.setGravity(Gravity.CENTER_HORIZONTAL);
        builder.setView(et);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String input = et.getText().toString();
                new AsyncOfficialLoader(mainActivity).execute(input);

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                }
        });

        builder.setMessage("Enter a City, State, or Zip Code:");

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void noInternetDialog(){
        textData();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("No Network Connection");
        builder.setMessage("Data cannot be accessed/loaded without an internet connection.");
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void getNewAdress(){
        final EditText input = new EditText(this);
        input.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        input.setInputType(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
        input.setGravity(Gravity.CENTER_HORIZONTAL);
        AlertDialog builder = new AlertDialog.Builder(this)
                .setTitle("Enter a City, State or a Zip Code :")
                .setView(input)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {public void onClick(DialogInterface dialog, int which) {}})
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {public void onClick(DialogInterface dialog, int which) {}})
                .show();
    }

    private boolean connected(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }



    @Override
    protected void onResume(){
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop(){
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}