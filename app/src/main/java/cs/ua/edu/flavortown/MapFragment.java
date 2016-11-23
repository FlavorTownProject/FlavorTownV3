package cs.ua.edu.flavortown;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.JsonReader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.app.Activity.RESULT_OK;
import static android.content.Context.LOCATION_SERVICE;



public class MapFragment extends SupportMapFragment implements OnMapReadyCallback {

    private static final String LOGTAG = "MapFragment";
    private GoogleMap mMap;
    private static String gpURL = "https://maps.googleapis.com/maps/api/place/details/json?&key=AIzaSyDZzxLsGBZ2aefPmsyGzpdB63OVpvc8PNY";//AIzaSyDU5KCvghYUqvJdkMY7OBo2mr8jsAEvHqY";



    private GPSManager gpsManager = new GPSManager(getContext());
    private Thread urlThread = new Thread(new Runnable() {
        @Override
        public void run() {
            try{
                findRestaurants();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    });

    int PLACE_PICKER_REQUEST = 1;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*}
        catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        }
        catch (GooglePlayServicesNotAvailableException e){
            e.printStackTrace();
        }*/


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Log.v(LOGTAG, "map is ready");

        if(ContextCompat.checkSelfPermission(getContext(),ACCESS_FINE_LOCATION) == 1)
        {
            mMap.setMyLocationEnabled(true);
        }
        if(gpsManager == null)
            Log.v("GPSManager","gpsManager null pointer");
        Log.v(LOGTAG, String.valueOf(gpsManager.getLongitude()));

        LatLng dennyChimes = new LatLng(33.209896, -87.546315);
        mMap.addMarker(new MarkerOptions().position(dennyChimes).title("Denny Chimes"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(dennyChimes));

        Log.v(LOGTAG, "Denny Chimes");
        Log.v(LOGTAG, "Starting urlThread");
        urlThread.start();
        Log.v(LOGTAG,"Thread started");

    }

    private void findRestaurants(){

        String temp = makeURLString(gpURL,"restaurant","500",gpsManager.getLatitude(),gpsManager.getLongitude());
        try {
            Log.v("MapFragment", "Printing URL");
            Log.v("MapFragment", temp);
            URL url = new URL(temp);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            temp = in.toString();
            Log.v(LOGTAG, temp);
            //JSONObject jsonFile = new JSONObject()


        }
        catch (MalformedURLException e) {
            Log.v("MapFragment", "MalformedURLException");
            e.printStackTrace();
        }
        catch (IOException e) {
            Log.v("MapFragment", "IOException");
            e.printStackTrace();
        }

    }

    private String makeURLString(String baseURL, String keywords, String rad, double lat, double lon)
    {
        String combinedString ="";
        //combinedString = baseURL + "&type=" + keywords + "&radius=" + rad + "&location=" + String.valueOf(lat) + String.valueOf(lon);
        combinedString = combinedString.concat(baseURL);
        Log.v("makeURL", combinedString);
        combinedString = combinedString.concat("&type=".concat(keywords));
        Log.v("makeURL", combinedString);
        combinedString = combinedString.concat("&radius=".concat(rad));
        Log.v("makeURL", combinedString);
        combinedString = combinedString.concat("&location=".concat(String.valueOf(lat).concat(String.valueOf(lon))));
        Log.v("makeURL", combinedString);
        return combinedString;
    }


}//end of MapFragment

class GPSManager extends Service implements LocationListener {


    public static float updateDistance = 10; // goes by meters
    public static final long minTimeBetweenUpdates = 1000 * 60; //Goes by ms, so 1000*60 is 1 minute.
    LocationManager locationManager;
    boolean canGetLocation;


    private final Context context;
    private Location location;
    private double latitude;
    private double longitude;
    private android.location.LocationListener locationListener = new android.location.LocationListener() {
        @Override
        public void onLocationChanged(Location location) {

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
        this.context = context;
        location = getLocation();
        Log.v("GPSManager","Initialized");
    }

    public Location getLocation() {
        boolean isGPSEnabled;
        boolean isNetworkEnabled;
        try {
            Log.v("GPSManager","Attempting getLocation");
            locationManager = (LocationManager) getApplicationContext().getSystemService(getApplicationContext().LOCATION_SERVICE);
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
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
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);//uses the context set on creation of the object
        alertDialog.setTitle("GPS Settings");
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivity(intent);
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

    }
}//end of GPSManager
