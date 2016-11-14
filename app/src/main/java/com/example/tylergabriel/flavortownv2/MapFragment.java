package com.example.tylergabriel.flavortownv2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.google.ads.AdRequest.LOGTAG;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class MapFragment extends Fragment{ //extends SupportMapFragment implements OnMapReadyCallback

    private Button openPlacePicker;
    private TextView placeName;
    private TextView placeLon;
    private TextView placeLat;
    private TextView placeTag;

    private static final String LOGTAG = "MapFragment";
    //private GoogleMap mMap;
    private static String gpURL = "https://maps.googleapis.com/maps/api/place/details/json?&key=AIzaSyDU5KCvghYUqvJdkMY7OBo2mr8jsAEvHqY";

    int PLACE_PICKER_REQUEST = 1;


    public void onCreate(Bundle savedInstanceState)
    {
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
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(requestCode == PLACE_PICKER_REQUEST)
        {
            if(resultCode==RESULT_OK)
            {
                Place place = PlacePicker.getPlace(getContext(), data);//getPlace(data,getContext());
                String name = String.format("Place: %s", place.getName());
                String lon = String.format("Lon: %s", place.getLatLng());
                String tags = "Tags = None of importance";
                List<Integer> tagResults = place.getPlaceTypes();
                while(!tagResults.isEmpty())
                {
                    if(tagResults.get(0) == 38)
                    {
                        tags = "Tags = Food";
                        tagResults.clear();
                    }
                    else
                    {
                        tagResults.remove(0);
                    }
                }

                Log.d("DEBUG", "Name = " + name);
                placeName.setText(name);
                placeLon.setText(lon);
                placeTag.setText(tags);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_map, container, false);
        openPlacePicker = (Button) v.findViewById(R.id.openPP);
        placeName = (TextView) v.findViewById(R.id.placeNameView);
        placeLon = (TextView) v.findViewById(R.id.placeLonView);
        placeLat = (TextView) v.findViewById(R.id.placeLatView);;
        placeTag = (TextView) v.findViewById(R.id.placeTagView);;


        openPlacePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

                //try{
                //startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
                //Intent intent = new Intent(this.getContext(), PlacePicker.class);
                try {
                    Intent intent = builder.build(getActivity());
                    //AutocompleteFilter typeFilter = new AutocompleteFilter.Builder().setTypeFilter(AutocompleteFilter.TYPE_FILTER_ESTABLISHMENT).build();
                    //intent.setF
                    startActivityForResult(intent, PLACE_PICKER_REQUEST);

                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }

            }
        });
        return v;
    }
    /* Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getMapAsync(this);
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Log.v(LOGTAG, "map is ready");

        LatLng dennyChimes = new LatLng(33.209896, -87.546315);
        mMap.addMarker(new MarkerOptions().position(dennyChimes).title("Denny Chimes"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(dennyChimes));
        Log.v(LOGTAG, "Denny Chimes");
    }
*/


/*
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public MapFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MapFragment.
     */

    /*
    // TODO: Rename and change types and number of parameters
    public static MapFragment newInstance(String param1, String param2) {
        MapFragment fragment = new MapFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


}
