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
    private boolean checked = false; //false = restaurant true = food

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    String[] displayItems;
    RestaurantInfo[] restaurantArr;
    Food[] foodArr;
    ArrayAdapter<String> adapter;
    ListView restaurantList;

    String search;
    String LOGTAG = "SearchBarFragment";

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
        restaurantList = (ListView) v.findViewById(R.id.restaurantList);
        //String[] displayItems = databaseReturn();
        adapter = new ArrayAdapter<>(v.getContext(), android.R.layout.simple_list_item_1, android.R.id.text1);//final ArrayAdapter<String> adapter = new ArrayAdapter<>(v.getContext(),
                //android.R.layout.simple_list_item_1, android.R.id.text1);
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

        //SearchButton.setOnTouchListener(new View.onTouchListener(){

        restaurantList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            //@Override
            public  void onItemClick(AdapterView<?> parent,View v,int position, long id){
                //Object a = restaurantList.getSelectedItem();
               String a = (String) parent.getItemAtPosition(position);
                if(checked){
                    //Pass a into Food Fragment
                    //Intent foodActivity = new Intent(getActivity(), FoodActivity.class);
                    Intent foodScreen = new Intent(getActivity(), FoodActivity.class);
                    foodScreen.putExtra("foodName", foodArr[position].getFoodItem());
                    foodScreen.putExtra("foodIterator",foodArr[position].getIterTag());
                    foodScreen.putExtra("foodCurrRating", foodArr[position].getCurrRating());
                    foodScreen.putExtra("foodNumOfRatings", foodArr[position].getNumOfRating());
                    foodScreen.putExtra("foodRatings", foodArr[position].getRatings());
                    foodScreen.putExtra("restaurantID",foodArr[position].getRestTag());
                    foodScreen.putExtra("restaurantKey", foodArr[position].getRestID());
                    startActivity(foodScreen);
                }
                else{
                    int location = 0;
                    Log.v("SearchBarFragment", "clickListener: restaruantArr is null :" + String.valueOf(restaurantArr == null));
                    for(int i = 0; i < restaurantArr.length; i++)
                    {
                        Log.v("SearchBarFragment", "restaurantArr["+String.valueOf(i)+"] = "+ restaurantArr[i].getRestName());
                        if(restaurantArr[i].getRestName().equalsIgnoreCase(a)){
                            location = i;
                        }
                    }
                    Intent menuActivity = new Intent(getActivity(), MenuActivity.class);
                    menuActivity.putExtra("restaurantName", restaurantArr[position].getRestName());
                    menuActivity.putExtra("restaurantID", restaurantArr[position].getGoogleID());
                    startActivity(menuActivity);
                }
            }
        });

        SearchButton.setOnClickListener(new View.OnClickListener() {//SearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){//onClick(View v) {
                String searchString = SearchBar.getText().toString();
                Log.v("SearchFragment", "searchString after click : " +searchString);

                search = searchString;
                Log.v("SearchFragment", "search after click : " +search);
                if (checked) {
                    adapter.clear();
                    foodDatabaseReturn();
                    //displayItems = new String[foodArr.length];
                }
                else{
                    adapter.clear();
                    boolean dummy = restaurantDatabaseReturn(searchString);
                    Log.v("SearchBarFragment", "restaurantDBR completed");
                   /* if (restaurantArr != null) {
                        Log.v("SearchBarFragment", "starting to display items");
                        displayItems = new String[restaurantArr.length];
                        for (int i = 0; i < displayItems.length; i++) {
                            String restName = restaurantArr[i].getRestName();
                            String restAddress = restaurantArr[i].getAddress();
                            displayItems[i] = restName.concat("\n").concat(restAddress);
                            adapter.add(displayItems[i]);
                        }
                        restaurantArr = null;
                        displayItems = null;
                    }else{
                        adapter.add("No results found");
                    }*/
                }
                //restaurantList.setAdapter(adapter);
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

    public boolean restaurantDatabaseReturn(String search1){
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        final DatabaseReference restaurantTable = db.getReference("restaurantTable");
        Log.v("SearchFragment", "in restaurantDatabaseReturn");
        Log.v("SearchFragment", "searchString in dbReturn : "+search1);
        Log.v("SearchFragment", "search at the beginning of dbReturn : " +search);
        restaurantTable.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Toast.makeText(getContext(), search,  Toast.LENGTH_SHORT).show();
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
                            Log.v("SearchBarFragment", "Starting compare");
                            Log.v("SearchBarFragment", "name = " +name);
                            Log.v("SearchBarFragment", "search = "+search);
                            Log.v("SearchBarFragment", "name.toLowerCase().startsWith(search.toLowerCase()) = " + String.valueOf(name.toLowerCase().startsWith(search.toLowerCase())));

                            if (name.toLowerCase().startsWith(search.toLowerCase())) {
                                Log.v("SearchBarFragment", "Found a valid restaurant name!");
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
                    Log.v("SearchBarFragment", "restaruantArr is null :" + String.valueOf(restaurantArr == null));

                    if (restaurantArr != null) {
                        Log.v("SearchBarFragment", "starting to display items");
                        displayItems = new String[restaurantArr.length];
                        for (int i = 0; i < displayItems.length; i++) {
                            String restName = restaurantArr[i].getRestName();
                            String restAddress = restaurantArr[i].getAddress();
                            displayItems[i] = restName.concat("\n").concat(restAddress);
                            adapter.add(displayItems[i]);
                        }
                        //restaurantArr = null;
                        //displayItems = null;
                    }else{
                        adapter.add("No results found");
                    }
                    restaurantList.setAdapter(adapter);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return true;
    }
    public void foodDatabaseReturn() {
        // choice false=rest true=food
        //Todo: add database query that inserts data into the array
        final FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference restaurantTable = db.getReference("restaurantTable");

        //DatabaseReference foodTable = restaurantTable.child("foodTable");
        restaurantTable.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int numOfTimes = 0;
                int restNumber = 0;
                LinkedList<Food> list = new LinkedList<Food>();
                try {
                    for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                        if (numOfTimes == 10) {
                            break;
                        } else {
                            String rest = (String) messageSnapshot.child("restName").getValue();
                            int menuLen = messageSnapshot.child("menuLength").getValue(int.class);
                            Food temp = new Food();
                            String tagIter;
                            for(int x = 0; x < menuLen && numOfTimes < 10; x++){
                                tagIter = "food".concat(String.valueOf(x));
                                temp.setFoodItem( (String) messageSnapshot.child("menu").child(tagIter).child("name").getValue());
                                temp.setNumOfRating(messageSnapshot.child("menu").child(tagIter).child("numOfRating").getValue(int.class));
                            //    temp.setCurrRating((float)messageSnapshot.child("menu").child(tagIter).child("currRating").getValue());
                                Log.v(LOGTAG, String.valueOf(restNumber));
                                Log.v(LOGTAG, tagIter);
                                if(temp.getNumOfRating() > 0)
                                {
                                    Log.v(LOGTAG,"Getting ratings");
                                    Log.v(LOGTAG, String.valueOf(temp.getNumOfRating()));
                                    float tempRate[] = new float[temp.getNumOfRating()];
                                    for(int y = 0; y < tempRate.length; y++) {
                                        tempRate[y] =  messageSnapshot.child("menu").child(tagIter).child("rating").child(String.valueOf(y)).getValue(float.class);
                                        Log.v(LOGTAG, "Rating "+ String.valueOf(y) +" = "+ String.valueOf(tempRate[y]) );
                                    }
                                    temp.setRatings(tempRate);
                                    Log.v(LOGTAG, "Set ratings (may need double check)" );
                                    temp.calcCurrRating();//do the math for currentRating
                                    Log.v(LOGTAG,"currRating set to "+ String.valueOf(temp.getCurrRating()));
                                }
                                else{
                                    Log.v(LOGTAG, "No ratings found");
                                    temp.setRatings(null);
                                    Log.v(LOGTAG, "Set ratings to null and currRating to 0");
                                    temp.setCurrRating(0);
                                }
                                temp.setFlag((String) messageSnapshot.child("menu").child(tagIter).child("flag").getValue());
                                temp.setIterTag(tagIter);


                                if(temp.getFoodItem().startsWith(search)){
                                    //temp.setIterTag(tagIter);
                                    temp.setRestTag("rest".concat(String.valueOf(restNumber)));
                                    temp.setRestaurant(rest);
                                    temp.setRestID((String)messageSnapshot.child("id").getValue());
                                    list.add(temp);
                                    numOfTimes++;
                                }
                            }
                        }
                        restNumber++;
                    }
                    foodArr = list.toArray(new Food[list.size()]);
                    Log.v("SearchBarFragment", "foodarray "+foodArr[0].getRestaurant());

                    if (foodArr != null) {
                        Log.v("SearchBarFragment", "starting to display food items");
                        displayItems = new String[foodArr.length];
                        for (int i = 0; i < displayItems.length; i++) {
                            String foodName = foodArr[i].getFoodItem();
                            String foodAddress = foodArr[i].getRestaurant();
                            //String foodRating = String.valueOf(foodArr[i].getCurrRating());
                            displayItems[i] = foodName.concat("\n").concat(foodAddress);
                            adapter.add(displayItems[i]);
                        }
                        //restaurantArr = null;
                        //displayItems = null;
                    }else{
                        adapter.add("No results found");
                    }
                    restaurantList.setAdapter(adapter);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

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
