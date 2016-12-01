package cs.ua.edu.flavortown;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StreamDownloadTask;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MainFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class MainFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private Button top10Button;
    private Button moodButton;
    private Button historyButton;
    private ArrayAdapter<String> adapter;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    String LOGTAG = "MainFragment";
    //private OnFragmentInteractionListener mListener;

    public MainFragment() {
        // Required empty public constructor
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainFragment newInstance(String param1, String param2) {
        MainFragment fragment = new MainFragment();
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
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        top10Button = (Button) v.findViewById(R.id.top10Button);
        historyButton =(Button) v.findViewById(R.id.historyButton);
        moodButton = (Button) v.findViewById(R.id.MoodButton);

        adapter = new ArrayAdapter<>(v.getContext(), android.R.layout.simple_list_item_1, android.R.id.text1);

        moodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getContext(), MoodActivity.class);
                startActivity(intent);
            }
        });


        final ListView mainList = (ListView) v.findViewById(R.id.WFlistview);

        //FirebaseDatabase database = FirebaseDatabase.getInstance();
        //final DatabaseReference myRef = database.getReference("top10");

        top10Button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v){
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                final DatabaseReference myRef = database.getReference("restaurantTable");
                String top10query = "a";
                //adapter = new ArrayAdapter<>(v.getContext(),
                //        android.R.layout.simple_list_item_1, android.R.id.text1);
               // mainList.setAdapter(adapter);
                myRef.addValueEventListener(new ValueEventListener() {

                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //String value = dataSnapshot.getValue(String.class);
                        //adapter.add(value);

                        String restID, restKey,restName;
                        Food tempFood;
                        int mLength = 0;
                        //FoodStack foodStack = new FoodStack();
                        Food[] foodList = new Food[100];
                        int currentIndex = 0;
                        for(int x = 0 ; x < 100; x++)
                            foodList[x] = new Food();

                        String foodIter;
                        try {
                            Log.v(LOGTAG, "in onDataChange");
                            for(DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {
                                restKey = messageSnapshot.getKey();
                                restName = (String) messageSnapshot.child("restName").getValue();
                                Log.v(LOGTAG, "name = " + restName);
                                restID = (String) messageSnapshot.child("id").getValue();
                                Log.v(LOGTAG, "id = " + restID);
                                mLength =  messageSnapshot.child("menuLength").getValue(int.class);
                                for (int x = 0; x < mLength; x++) {
                                    tempFood = new Food();
                                    tempFood.setRestKey(restKey);
                                    foodIter = stringIncrementer("food", x);
                                    tempFood.setFoodItem(messageSnapshot.child("menu").child(foodIter).child("name").getValue(String.class));
                                    Log.v(LOGTAG, "foodItem" + tempFood.getFoodItem());
                                    tempFood.setNumOfRating(messageSnapshot.child("menu").child(foodIter).child("numOfRating").getValue(int.class));
                                    if (tempFood.getNumOfRating() > 0) {
                                        Log.v(LOGTAG, "Getting ratings");
                                        float tempRate[] = new float[tempFood.getNumOfRating()];
                                        int y =0;
                                        for (DataSnapshot ratings: messageSnapshot.child("menu").child(foodIter).child("rating").getChildren()){ //int y = 0; y < tempRate.length; y++) {
                                            tempRate[y] = ratings.getValue(float.class); //tempRate[y] = messageSnapshot.child("menu").child(foodIter).child("rating").child(String.valueOf(y)).getValue(float.class);
                                            Log.v(LOGTAG, "Rating " + String.valueOf(y) + " = " + String.valueOf(tempRate[y]));
                                            y++;
                                        }
                                        tempFood.setRatings(tempRate);
                                        Log.v(LOGTAG, "Set ratings (may need double check)");
                                        tempFood.calcCurrRating();//do the math for currentRating
                                        Log.v(LOGTAG, "currRating set to " + String.valueOf(tempFood.getCurrRating()));
                                    } else {
                                        Log.v(LOGTAG, "No ratings found");
                                        tempFood.setRatings(null);
                                        Log.v(LOGTAG, "Set ratings to null and currRating to 0");
                                        tempFood.setCurrRating(0);
                                    }
                                    tempFood.setFlag((String) messageSnapshot.child("menu").child(foodIter).child("flag").getValue());
                                    tempFood.setIterTag(foodIter);
                                    Log.v(LOGTAG, "flag = " + tempFood.getFlag());
                                    Log.v(LOGTAG, "adding food to menu");
                                    foodList[currentIndex].copyFood(tempFood); //currMenu.addToFoodList(temp);
                                    currentIndex++;
                                }
                            }
                            //Lists are filled, commence calculations



                            tempFood = new Food();
                            Food topTenFood[] = new Food[10];
                            Food highestItem = new Food();//Food bump1 = new Food();
                            //Food bump2 = new Food();
                            int usedIndices[] = new int[10];//int pulled = 0;
                            for(int x = 0; x < 10; x++) {
                                topTenFood[x] = new Food();
                                usedIndices[x] = -1;
                            }
                            int indexFound = 0;

                            for(int x = 0; x < 10 ; x++) {
                                highestItem = new Food();//Food bump1 = new Food();
                                    for(int y = 0; y < currentIndex; y++) {
                                        if((highestItem == null || highestItem.getCurrRating() < foodList[y].getCurrRating() )&& !usedIndex(usedIndices,y)) {
                                            indexFound = y;
                                            highestItem.copyFood(foodList[y]);
                                        }
                                        else if(highestItem.getCurrRating() < foodList[y].getCurrRating() && foodList[y].getNumOfRating() > highestItem.getNumOfRating() && !usedIndex(usedIndices,y)){
                                            indexFound = y;
                                            highestItem.copyFood(foodList[y]);
                                        }
                                    }
                                usedIndices[x] = indexFound;
                                topTenFood[x].copyFood(highestItem);
                            }

                            /*
                            for(int i = 0 ; i < currentIndex; i++)
                            {
                                Log.v(LOGTAG, "tempFood.Fooditem = " +tempFood.getFoodItem());
                                tempFood.copyFood(foodList[i]);
                                if(pulled < 10)
                                {
                                    topTenFood[pulled] = tempFood;
                                    pulled++;
                                    if(pulled == 10)
                                        sortTopTen(topTenFood);
                                }
                                else{
                                    for(int x = 0; x < 10; x++)
                                    {
                                        if ( x == 0 && tempFood.getCurrRating() <= topTenFood[x].getCurrRating())
                                            break;
                                        else if(tempFood.getCurrRating() <= topTenFood[x].getCurrRating())
                                        {
                                            bump1.copyFood(topTenFood[x - 1]);
                                            topTenFood[x - 1].copyFood(tempFood);
                                            for(int y = x - 2; y >= 0; y--)
                                            {
                                                bump2.copyFood(topTenFood[y]);
                                                topTenFood[y].copyFood(bump1);
                                                bump1.copyFood(bump2);
                                            }
                                            break;
                                        }
                                        /*
                                        else if ( x ==  9 && tempFood.getCurrRating() >= topTenFood[x].getCurrRating())
                                        {
                                            bump1.copyFood(topTenFood[x]);
                                            topTenFood[x].copyFood(tempFood);
                                            for(int y = x - 2; y >= 0; y--)
                                            {
                                                bump2.copyFood(topTenFood[y]);
                                                topTenFood[y].copyFood(bump1);
                                                bump1.copyFood(bump2);
                                            }
                                            break;
                                        }
                                    }
                                    //logPrintArray(topTenFood);
                                }
                            }//end of while iter.hasNext()

                            */
                            String topTenNames[] = new String[10];
                            for(int x = 0 ; x < 10 ; x++)
                            {
                                Log.v(LOGTAG, "topTenFood["+String.valueOf(x)+"].name = "+topTenFood[x].getFoodItem() );
                                adapter.add(topTenFood[x].getFoodItem()); //topTenNames[x] = topTenFood[x].getFoodItem();
                            }

                            mainList.setAdapter(adapter);

                        }
                        catch( Exception e){
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w("TAG:", "Failed to read value.");
                    }
                });
            }
        });

        historyButton.setOnClickListener(new View.OnClickListener() {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            final DatabaseReference myRef = database.getReference("history");
            @Override
            public void onClick(View v){
                String historyquery = "b";
                adapter = new ArrayAdapter<>(v.getContext(),
                        android.R.layout.simple_list_item_1, android.R.id.text1);
                mainList.setAdapter(adapter);
                myRef.addChildEventListener(new ChildEventListener() {
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
                });
            }
        });

        mainList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            //@Override
            public  void onItemClick(AdapterView<?> parent,View v,int position, long id){
                //Object a = restaurantList.getSelectedItem();
                String a =  (String) parent.getItemAtPosition(position);

                Toast.makeText(v.getContext(), a, Toast.LENGTH_SHORT).show();
            }


        });

        return v;
    }

    public String[] databaseReturn(String query){

        //Todo: add database query that inserts data into the array
        String[] result  = {query,query+query,query+query+query,query+query+query+query,query+query+query+query+query,query+query+query+query,"Burger G"};
        return result;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @NonNull
    private String stringIncrementer(String baseString, int value) {
        return baseString.concat(String.valueOf(value));
    }

    private void sortTopTen(Food[] top)
    {
        Food temp = new Food();
        for(int x = 0; x < 10 ; x++)
        {
            for(int y = x+1 ; y < 10 ; y++)
            {
                if(top[x].getCurrRating() < top[y].getCurrRating())
                {
                    temp.copyFood(top[x]);
                    top[x].copyFood(top[y]);
                    top[y].copyFood(temp);
                }
            }
        }
        logPrintArray(top);
    }
    private void logPrintArray(Food[] top)
    {
        for(int x = 0 ; x < 10; x++) {
            Log.v("MainFragment", "topTen[" + String.valueOf(x) + "].name = " + top[x].getFoodItem());
            Log.v("MainFragment", "topTen[" + String.valueOf(x) + "].currRate = " + top[x].getCurrRating());
        }

    }
    private boolean usedIndex(int[] array , int value)
    {
        for(int x = 0 ; x < array.length ; x++)
        {
            if(array[x] == value)
                return true;
        }
        return  false;
    }

}
