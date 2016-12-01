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
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.app.Activity.RESULT_OK;
import static android.content.Context.LOCATION_SERVICE;

public class MapFragment extends SupportMapFragment implements OnMapReadyCallback,GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnInfoWindowClickListener, android.location.LocationListener {

    private static final String LOGTAG = "MapFragment";
    private GoogleMap mMap;

    //original key: AIzaSyDZzxLsGBZ2aefPmsyGzpdB63OVpvc8PNY
    //Back up Key (Flavortown): 	AIzaSyA2pbTswWHv5NslX4e35AWEu-PiCbMe3wY
    //Back up 2: AIzaSyDK6GB-RTPuNRZIKpsLd0mUE667t3KXqM4
    private static String gpURL = "https://maps.googleapis.com/maps/api/place/textsearch/json?&key=AIzaSyDK6GB-RTPuNRZIKpsLd0mUE667t3KXqM4&query=hamburgers";//AIzaSyDU5KCvghYUqvJdkMY7OBo2mr8jsAEvHqY";
    static String detailURL = "https://maps.googleapis.com/maps/api/place/details/json?&key=AIzaSyDK6GB-RTPuNRZIKpsLd0mUE667t3KXqM4";


    private JSONObject jsonIDS;
    private RestaurantInfo restaurants[] = new RestaurantInfo[20];
    private Marker markers[] = new Marker[20];

    private GPSManager gpsManager;
    private Thread networkRequests;

    //int PLACE_PICKER_REQUEST = 1;
    LatLng currentLocation;

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
        gpsManager = new GPSManager(getActivity());

        for (int x = 0; x < 20; x++)
            restaurants[x] = new RestaurantInfo();

        networkRequests = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    findRestaurants();
                    getDetails();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        networkRequests.start();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Log.v(LOGTAG, "map is ready");

        if (ContextCompat.checkSelfPermission(getActivity(), ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Log.v(LOGTAG, "My Location Enabled");
            mMap.setMyLocationEnabled(true);
            currentLocation = new LatLng(gpsManager.getLatitude(),gpsManager.getLongitude());
            Log.v(LOGTAG, "current lat = " + String.valueOf(currentLocation.latitude));
            Log.v(LOGTAG, "current lon = " + String.valueOf(currentLocation.longitude));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 12.0f));
        }
        if (gpsManager == null)
            Log.v("GPSManager", "gpsManager null pointer");
        Log.v(LOGTAG, String.valueOf(gpsManager.getLongitude()));

        //LatLng dennyChimes = new LatLng(33.209896, -87.546315);
        //mMap.addMarker(new MarkerOptions().position(dennyChimes).title("Denny Chimes"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(dennyChimes,12.0f));

        Log.v(LOGTAG, "Attempting to add first additional item");

        try {
            while (networkRequests.isAlive())
                networkRequests.join();
            if(jsonIDS.getString("status").equals("ZERO_RESULTS"))
                Toast.makeText(getActivity(),"No Results Found", Toast.LENGTH_LONG).show();
            else
                addMarkers();

        } catch (InterruptedException | JSONException e) {
            e.printStackTrace();
        }

        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnInfoWindowClickListener(this);
        Log.v(LOGTAG, "Denny Chimes");
        Log.v(LOGTAG, "Starting urlThread");

        Log.v(LOGTAG, "Thread started");

    }



    private void addMarkers(){
        Log.v("addMarkers", "List length ="+ String.valueOf(restaurants.length));
        for(int x = 0; x < restaurants.length && restaurants[x] != null; x++)
        {
            Log.v("addMarkers", "x = " + String.valueOf(x));
            LatLng test = new LatLng(restaurants[x].getLatitude(), restaurants[x].getLongitude());
            markers[x] = mMap.addMarker(new MarkerOptions().position(test).title(restaurants[x].getRestName()).snippet(restaurants[x].getAddress()));
            Log.v("addMarkers", restaurants[x].getRestName());
            Log.v("addMarkers", "Lat".concat(String.valueOf(restaurants[x].getLatitude())));
            Log.v("addMarkers", "Lon".concat(String.valueOf(restaurants[x].getLongitude())));
        }
    }

    private void findRestaurants(){
        try {
            String temp = makeURLString(gpURL, "restaurant", "5000", gpsManager.getLatitude(), gpsManager.getLongitude());
            jsonIDS = getJSONFile(new URL(temp));
        }
        catch (MalformedURLException e){
            e.printStackTrace();
        }

    }

    private void getDetails()
    {
        String funcTag = "getDetails";
        Log.v("getDetails", "Parsing restaurant IDs");

        try{
            Log.v(funcTag, jsonIDS.getJSONArray("results").toString());
            JSONArray results = jsonIDS.getJSONArray("results");
            JSONObject singleResult, placeInfo;
            String fullDetailURL;
            Log.v(funcTag, "Results.length = ".concat(String.valueOf(results.length())));
            for(int x = 0 ; x < results.length() ; x++)
            {
                Log.v(funcTag, "x = ".concat(String.valueOf(x)));
                singleResult = results.getJSONObject(x);
                if(singleResult == null)
                    Log.v(funcTag,"SingleResult null");
                else {
                    Log.v(funcTag, "SingleResult set");
                    Log.v(funcTag, singleResult.toString());
                }
                Log.v(funcTag, "Pulling latitude");
                Log.v(funcTag, String.valueOf(singleResult.getJSONObject("geometry").getJSONObject("location").getDouble("lat")));
                restaurants[x].setLatitude(singleResult.getJSONObject("geometry").getJSONObject("location").getDouble("lat"));
                Log.v(funcTag, "Pulling longitude");
                Log.v(funcTag, String.valueOf(singleResult.getJSONObject("geometry").getJSONObject("location").getDouble("lng")));
                restaurants[x].setLongitude(singleResult.getJSONObject("geometry").getJSONObject("location").getDouble("lng"));

                fullDetailURL = makeURLString(detailURL, singleResult.getString("place_id"));
                placeInfo = getJSONFile(new URL(fullDetailURL));

                if (placeInfo != null)
                {
                    placeInfo = placeInfo.getJSONObject("result");
                    restaurants[x].setAddress(placeInfo.getString("formatted_address"));
                    restaurants[x].setRestName(placeInfo.getString("name"));
                    restaurants[x].setGoogleID(placeInfo.getString("id"));
                    Log.v(funcTag, restaurants[x].getRestName() );
                }

            }

        }
        catch (IOException | JSONException e){
            e.printStackTrace();
        }
    }

    private JSONObject getJSONFile(URL url) {
        JSONObject jsonFile = null;
        try {
            Log.v("getJSONFile", "Querying URL");
            Log.v("getJSONFile", url.toString());
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader streamReader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            StringBuilder builder = new StringBuilder();

            String line;
            while ( (line = streamReader.readLine()) != null)
                builder.append(line);
            jsonFile =  new JSONObject(builder.toString());
            urlConnection.disconnect();//close connection after read is finished
            Log.v("getJSONFile", "Init jsonFile");
            Log.v("getJSONFile", builder.toString());
        }
        catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return jsonFile;
    }


    private String makeURLString(String baseURL, String keywords, String rad, double lat, double lon) {
        String combinedString ="";
        //combinedString = baseURL + "&type=" + keywords + "&radius=" + rad + "&location=" + String.valueOf(lat) + String.valueOf(lon);
        combinedString = combinedString.concat(baseURL);
        Log.v("makeURL", combinedString);
        combinedString = combinedString.concat("&type=".concat(keywords));
        Log.v("makeURL", combinedString);
        combinedString = combinedString.concat("&radius=".concat(rad));
        Log.v("makeURL", combinedString);
        combinedString = combinedString.concat("&location=".concat(String.valueOf(lat).concat(",".concat(String.valueOf(lon)))));
        Log.v("makeURL", combinedString);
        return combinedString;
    }

    private String makeURLString(String baseURL, String placeID) {
        return baseURL.concat("&placeid=".concat(placeID));
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Log.v("LocationButtonClick", "Button Clicked");
        Toast.makeText(getActivity(),"Click event registered",Toast.LENGTH_SHORT).show();
        currentLocation = new LatLng(gpsManager.getLatitude(),gpsManager.getLongitude());
        //MarkerOptions locationPin = new MarkerOptions().icon(BitmapDescriptorFactory.fromFile()); Need a decent pic
        mMap.addMarker(new MarkerOptions().position(currentLocation).title("Current Location!"));//mMap.addMarker(locationPin);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation,12.0f));


        return true;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Toast.makeText(getActivity(),"Click event registered",Toast.LENGTH_SHORT).show();
        RestaurantInfo currentRest = getRestaurantFromMarker(marker);
        Intent nextScreen = new Intent(getActivity(), MenuActivity.class);
        nextScreen.putExtra("restaurantName", currentRest.getRestName());
        nextScreen.putExtra("restaurantAddress",currentRest.getAddress());
        nextScreen.putExtra("restaurantID",currentRest.getGoogleID()); //We are going to need to do the menu look up on the MenuActivity as a Menu object can't be passed.
        startActivity(nextScreen);

    }

    private RestaurantInfo getRestaurantFromMarker(Marker marker){
        LatLng markerPosition = marker.getPosition();
        for(int x = 0; x < restaurants.length ; x++)
        {
            if (restaurants[x].getLatitude() == markerPosition.latitude && restaurants[x].getLongitude() == markerPosition.longitude)
                return restaurants[x];
        }
        return null;//if hit there is a problem as this function should not be called unless the restaurant is confirmed to be in the list.
    }

    @Override
    public void onLocationChanged(Location location) {
        try{
            Log.v(LOGTAG, "Hit Location Change");
            while(networkRequests.isAlive())
                networkRequests.join();
            for(int x = 0 ; x < markers.length ; x++){
                markers[x].remove();
            }
            //mMap.clear();
            gpsManager.onLocationChanged(location);
            networkRequests.start();
            while (networkRequests.isAlive())
                networkRequests.join();

            if(jsonIDS.getString("status").equals("ZERO_RESULTS"))
                Toast.makeText(getActivity(),"No Results Found", Toast.LENGTH_LONG).show();
            else
                addMarkers();


        }
        catch(InterruptedException |JSONException e)
        {
            e.printStackTrace();
        }


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
}//end of MapFragment
