package cs.ua.edu.flavortown;

import android.*;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

/**
 * Created by Archangel on 11/26/2016.
 */

class GPSManager extends Service implements LocationListener {


    public static float updateDistance = 10; // goes by meters
    public static final long minTimeBetweenUpdates = 1000 * 60; //Goes by ms, so 1000*60 is 1 minute.
    private LocationManager locationManager;
    boolean canGetLocation;


    private final Context gpsContext;
    private Location location;
    private double latitude;
    private double longitude;
    private android.location.LocationListener locationListener = new android.location.LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            Log.v("GPSManager","First onLocationChanged");
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            setLocation(location);
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

    public GPSManager (Context context) {
        Log.v("GPSManager","Initialzing");
        gpsContext= context;
        if(gpsContext == null)
            Log.v("GPSManager", "gpsContext is null");
        location = getLocation();
        Log.v("GPSManager","Initialized");
    }

    public Location getLocation() {
        boolean isGPSEnabled;
        boolean isNetworkEnabled;
        try {
            Log.v("GPSManager","Attempting getLocation");
            if(gpsContext == null)
                Log.v("GPSManager", "In getLocation: gpsContext = null");
            locationManager = (LocationManager) gpsContext.getSystemService(Context.LOCATION_SERVICE);
            Log.v("GPSManager","locationManager initialized");
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            Log.v("GPSManager","isGPSEnabled initialized");
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            Log.v("GPSManager","isNetworkEnabled initialized");
            if (ActivityCompat.checkSelfPermission(gpsContext, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){//(gpsContext.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Log.v("GPSManager","Hit else for checkSelfPermission");
                Log.v("GPSManager", String.valueOf(ActivityCompat.checkSelfPermission(gpsContext, android.Manifest.permission.ACCESS_FINE_LOCATION)) );
                Log.v("GPSManager", String.valueOf(PackageManager.PERMISSION_GRANTED) );
                Log.v("GPSManager", String.valueOf(PackageManager.PERMISSION_DENIED) );
                ActivityCompat.requestPermissions((Activity)gpsContext, new String[] {android.Manifest.permission.ACCESS_FINE_LOCATION}, 10);
            }
            else{
                Log.v("GPSManager","Passed checkSelfPermission");
                if (!isGPSEnabled && isNetworkEnabled) {
                    Log.v("GPSManager","Both GPS and Network are disabled");
                } else {
                    Log.v("GPSManager","Either GPS or Network is enabled");
                    this.canGetLocation = true;
                    if (isNetworkEnabled) {
                        Log.v("GPSManager","Network is enabled");
                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, minTimeBetweenUpdates, updateDistance, locationListener); //(LocationManager.NETWORK_PROVIDER, (long) minTimeBetweenUpdates,(float) updateDistance, locationListener);
                        if(locationManager != null) {
                            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                            if(location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }//end of if(location != null)
                        }//end of if(locationManager !=null)
                    }//end of if(isNetworkEnabled)
                    if(isGPSEnabled) {
                        Log.v("GPSManager","GPS is enabled");
                        if(location == null) {
                            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER , minTimeBetweenUpdates, updateDistance, locationListener);
                            if(locationManager != null) {
                                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                                if(location != null) {
                                    latitude = location.getLatitude();
                                    longitude = location.getLongitude();
                                }
                            }
                        }
                    }//end of if(isGPSEnabled)
                }
            }
        } catch (Exception e) {
            Log.v("GPSManager","Hit exception");
            e.printStackTrace();
        }
        return location;
    }//end of getLocation

    //@Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 10:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLocation();
                }
                return;
        }
    }

    private void setLocation(Location loc)
    {
        location = loc;
    }
    public double getLatitude() {
        if(location != null)
            latitude = location.getLatitude();
        return latitude;

    }
    public double getLongitude() {
        if(location != null)
            longitude = location.getLongitude();
        return  longitude;
    }

    public void showSettingsAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(gpsContext);//uses the context set on creation of the object
        alertDialog.setTitle("GPS Settings");
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                gpsContext.startActivity(intent);
            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        alertDialog.show();
    }


    //Required due to android studio. - Ian
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    //Required due to android studio. - Ian
    @Override
    public void onLocationChanged(Location location) {
        Log.v("GPSManager","Second onLocationChanged");
        this.location = getLocation();
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
}//end of GPSManager
