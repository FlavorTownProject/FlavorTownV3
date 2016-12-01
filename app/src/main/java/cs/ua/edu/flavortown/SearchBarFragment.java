package cs.ua.edu.flavortown;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.LinkedList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SearchBarFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SearchBarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchBarFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private boolean checked; //false = restaurant true = food

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    String[] displayItems;
    RestaurantInfo[] restaurantArr;
    Food[] foodArr;

    String search;

    public SearchBarFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchBarFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchBarFragment newInstance(String param1, String param2) {
        SearchBarFragment fragment = new SearchBarFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment_search_bar, container, false);
        final EditText SearchBar = (EditText) v.findViewById(R.id.SearchBar);
        final Button SearchButton = (Button)  v.findViewById(R.id.SearchButton);
        //final TextView a = new TextView(getContext());
        final ListView restaurantList = (ListView) v.findViewById(R.id.restaurantList);
        //String[] displayItems = databaseReturn();
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(v.getContext(),
                android.R.layout.simple_list_item_1, android.R.id.text1);
        //final ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this.getContext(),android.R.layout.two_line_list_item,displayItems);
        //ArrayList<ListEntry> items = new ArrayList<ListEntry>();
        final RadioGroup radioGroup = (RadioGroup) v.findViewById(R.id.radioGroup);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                RadioButton rb =(RadioButton) v.findViewById(checkedId);
                int index = radioGroup.indexOfChild(rb);
                switch (index) {
                    case 0: // Restaurant
                        checked = false;
                        Toast.makeText(v.getContext(), "Selected button  " + rb.getText(),  Toast.LENGTH_SHORT).show();
                        break;
                    case 1: // FOOD
                        checked = true;
                        Toast.makeText(v.getContext(), "Selected button  " + rb.getText(),  Toast.LENGTH_SHORT).show();
                        break;
                }


            }
        });

        SearchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                adapter.clear();
                String searchString = SearchBar.getText().toString();
                search = searchString;
                if (checked) {
                    foodDatabaseReturn(searchString);
                    displayItems = new String[foodArr.length];
                }
                else{
                    restaurantDatabaseReturn(searchString);
                    displayItems = new String[restaurantArr.length];
                    for(int i = 0; i < displayItems.length; i++){
                        String restName = restaurantArr[i].getRestName();
                        String restAddress = restaurantArr[i].getAddress();
                        displayItems[i] = restName.concat("\n").concat(restAddress);
                    }
                }

/*
                // Get Firebase Database reference
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                final DatabaseReference searchRef = database.getReference(searchString);

                restaurantList.setAdapter(adapter);
                searchRef.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        String value = dataSnapshot.getValue(String.class);
                        adapter.add(value);
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                        String value = dataSnapshot.getValue(String.class);
                        adapter.remove(value);
                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w("TAG:", "Failed to read value.");
                    }
                });*/
            }
        });


        restaurantList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            //@Override
            public  void onItemClick(AdapterView<?> parent,View v,int position, long id){
                //Object a = restaurantList.getSelectedItem();
                String a =  (String) parent.getItemAtPosition(position);
                int location = 0;
                if (checked){
                    for(int i = 0; i < foodArr.length; i++){
                        if(foodArr[i].getFoodItem().equals(a)) {
                            location = i;
                            break;
                        }
                    }
                    /* JOE INSERT YOUR FOOD STUFF HERE */
                }
                else{
                    for(int i = 0; i < restaurantArr.length; i++){
                        if(restaurantArr[i].getRestName().equals(a)){
                            location = i;
                            break;
                        }
                    }
                    Intent nextScreen = new Intent(getActivity(), MenuActivity.class);
                    nextScreen.putExtra("restaurantName", restaurantArr[location].getRestName());
                    nextScreen.putExtra("restaurantAddress",restaurantArr[location].getAddress());
                    nextScreen.putExtra("restaurantID",restaurantArr[location].getGoogleID()); //We are going to need to do the menu look up on the MenuActivity as a Menu object can't be passed.
                    startActivity(nextScreen);

                }
                Toast.makeText(v.getContext(), a, Toast.LENGTH_SHORT).show();
            }


        });



        return v;
        //return inflater.inflate(R.layout.fragment_search_bar, container, false);
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radioRestaurant:
                if (checked)
                    break;
            case R.id.radioFood:
                if (checked)
                    break;
        }
    }



/*
    public String[] databaseReturn(){

        //Todo: add database query that inserts data into the array
        String[] a  = {"Burger A","Burger B","Burger C","Burger D","Burger E","Burger F","Burger G"};
        return a;
    }*/

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public void restaurantDatabaseReturn(final String search){
        final FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference restaurantTable = db.getReference("restaurantTable");

        restaurantTable.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int numOfTimes = 0;
                LinkedList<RestaurantInfo> list = new LinkedList<RestaurantInfo>();
                try {
                    for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                        if (numOfTimes == 10) {
                            break;
                        } else {
                            String name = (String) messageSnapshot.child("restName").getValue();
                            String ID = (String) messageSnapshot.child("id").getValue();
                            String address = (String) messageSnapshot.child("address").getValue();
                            if (name.startsWith(search)) {
                                RestaurantInfo currRestaurant = new RestaurantInfo();
                                currRestaurant.setRestName(name);
                                currRestaurant.setGoogleID(ID);
                                currRestaurant.setAddress(address);

                                list.add(currRestaurant);

                                numOfTimes++;
                            }
                        }
                    }
                    restaurantArr = list.toArray(new RestaurantInfo[list.size()]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }@Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void foodDatabaseReturn(String search){ return;}
        /*// choice false=rest true=food
        //Todo: add database query that inserts data into the array
        final FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference foodTable = db.getReference("foodTable");

        //DatabaseReference foodTable = restaurantTable.child("foodTable");
        foodTable.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int numOfTimes = 0;
                LinkedList<String> list = new LinkedList<String>();
                try{
                    for(DataSnapshot messageSnapshot: dataSnapshot.getChildren()){
                        if (numOfTimes == 10){
                            break;
                        }
                        else{
                            String name = (String) messageSnapshot.child("name").getValue();
                            if(name.startsWith(search)){
                                list.add(name);
                                numOfTimes++;
                            }
                        }
                    }
                }
                catch(Exception e){
                    e.printStackTrace();
                }
                foodArr = list.toArray(new String[list.size()]);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }*/

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }

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
