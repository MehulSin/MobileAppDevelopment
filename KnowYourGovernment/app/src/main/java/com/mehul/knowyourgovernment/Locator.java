package com.mehul.knowyourgovernment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;


public class Locator {

    private MainActivity ma;
    private LocationManager locationManager;
    private LocationListener locationListener;

    public Locator(MainActivity activity){
        ma = activity;

        if(checkPermission()){
            setUpLocationManager();
            determineLocation();
        }
    }


    void locationDetail()
    {
        try{
            setUpLocationManager();
            determineLocation();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void setUpLocationManager(){

        if(locationManager != null){return;}
        if(!checkPermission()){ return;}
       locationManager = (LocationManager) ma.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                ma.setAddress(location.getLatitude(), location.getLongitude());
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };

        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,1000, 0, locationListener);

    }

    public void shutdown(){
        locationManager.removeUpdates(locationListener);
        locationManager = null;
    }

    public android.location.LocationListener getLocationListener() {
        return locationListener;
    }

   public void determineLocation(){

        if(!checkPermission()){return;}

        if(locationManager == null){setUpLocationManager();}

       if (locationManager != null) {
           Location loc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
           if (loc != null) {
               ma.setAddress(loc.getLatitude(), loc.getLongitude());
               return;
           }
       }

       if (locationManager != null) {
           Location loc = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
           if (loc != null) {
               ma.setAddress(loc.getLatitude(), loc.getLongitude());
               return;
           }
       }

       if (locationManager != null) {
           Location loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
           if (loc != null) {
               ma.setAddress(loc.getLatitude(), loc.getLongitude());
               return;
           }
       }
       ma.noLocationAvailable();
       return;
   }

   private boolean checkPermission(){
       if (ContextCompat.checkSelfPermission(ma, Manifest.permission.ACCESS_FINE_LOCATION) !=
               PackageManager.PERMISSION_GRANTED) {
           ActivityCompat.requestPermissions(ma,
                   new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 5);
           return false;
       }
       return true;
   }
}
