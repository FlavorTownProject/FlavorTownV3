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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

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
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            final DatabaseReference myRef = database.getReference("restaurantTable");
            @Override
            public void onClick(View v){
                String top10query = "a";
                adapter = new ArrayAdapter<>(v.getContext(),
                        android.R.layout.simple_list_item_1, android.R.id.text1);
                mainList.setAdapter(adapter);
                myRef.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        String value = dataSnapshot.getValue(String.class);
                        adapter.add(value);

                        String restID, restKey,restName;
                        Food tempFood;
                        int mLength = 0;

                        List<Food> FoodList = new List<Food>() {
                            @Override
                            public int size() {
                                return 0;
                            }

                            @Override
                            public boolean isEmpty() {
                                return false;
                            }

                            @Override
                            public boolean contains(Object o) {
                                return false;
                            }

                            @NonNull
                            @Override
                            public Iterator<Food> iterator() {
                                return null;
                            }

                            @NonNull
                            @Override
                            public Object[] toArray() {
                                return new Object[0];
                            }

                            @NonNull
                            @Override
                            public <T> T[] toArray(T[] ts) {
                                return null;
                            }

                            @Override
                            public boolean add(Food food) {
                                return false;
                            }

                            @Override
                            public boolean remove(Object o) {
                                return false;
                            }

                            @Override
                            public boolean containsAll(Collection<?> collection) {
                                return false;
                            }

                            @Override
                            public boolean addAll(Collection<? extends Food> collection) {
                                return false;
                            }

                            @Override
                            public boolean addAll(int i, Collection<? extends Food> collection) {
                                return false;
                            }

                            @Override
                            public boolean removeAll(Collection<?> collection) {
                                return false;
                            }

                            @Override
                            public boolean retainAll(Collection<?> collection) {
                                return false;
                            }

                            @Override
                            public void clear() {

                            }

                            @Override
                            public Food get(int i) {
                                return null;
                            }

                            @Override
                            public Food set(int i, Food food) {
                                return null;
                            }

                            @Override
                            public void add(int i, Food food) {

                            }

                            @Override
                            public Food remove(int i) {
                                return null;
                            }

                            @Override
                            public int indexOf(Object o) {
                                return 0;
                            }

                            @Override
                            public int lastIndexOf(Object o) {
                                return 0;
                            }

                            @Override
                            public ListIterator<Food> listIterator() {
                                return null;
                            }

                            @NonNull
                            @Override
                            public ListIterator<Food> listIterator(int i) {
                                return null;
                            }

                            @NonNull
                            @Override
                            public List<Food> subList(int i, int i1) {
                                return null;
                            }
                        };
                        String foodIter;
                        try {
                            Log.v(LOGTAG, "in onDataChange");
                            for(DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {
                                restKey = messageSnapshot.getKey();
                                restName = (String) messageSnapshot.child("restName").getValue();
                                Log.v(LOGTAG, "name = " + restName);
                                restID = (String) messageSnapshot.child("id").getValue();
                                Log.v(LOGTAG, "id = " + restID);
                                for (int x = 0; x < mLength; x++) {
                                    tempFood = new Food();
                                    tempFood.setRestKey(restKey);
                                    foodIter = stringIncrementer("food", x);
                                    tempFood.setFoodItem(messageSnapshot.child("menu").child(foodIter).child("name").getValue(String.class));
                                    tempFood.setNumOfRating(messageSnapshot.child("menu").child(foodIter).child("numOfRatings").getValue(int.class));
                                    if (tempFood.getNumOfRating() > 0) {
                                        Log.v(LOGTAG, "Getting ratings");
                                        float tempRate[] = new float[tempFood.getNumOfRating()];
                                        for (int y = 0; y < tempRate.length; y++) {
                                            tempRate[y] = messageSnapshot.child("menu").child(foodIter).child("rating").child(String.valueOf(y)).getValue(float.class);
                                            Log.v(LOGTAG, "Rating " + String.valueOf(y) + " = " + String.valueOf(tempRate[y]));
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
                                    FoodList.add(tempFood); //currMenu.addToFoodList(temp);
                                }
                            }
                            //Lists are filled, commence calculations

                            Iterator<Food> foodIterator = FoodList.iterator();


                            Food topTenFood[] = new Food[10];
                            Food bump1 = new Food();
                            Food bump2 = new Food();
                            int pulled = 0;
                            while(foodIterator.hasNext())
                            {
                                tempFood = foodIterator.next();
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
                                        else if ( x ==  9 && tempFood.getCurrRating() >= topTenFood[x].getCurrRating())
                                        {
                                            bump1.copyFood(topTenFood[x]);
                                            topTenFood[x].copyFood(tempFood);
                                            for(int y = x - 1; y >= 0; y--)
                                            {
                                                bump2.copyFood(topTenFood[y]);
                                                topTenFood[y].copyFood(bump1);
                                                bump1.copyFood(bump2);
                                            }
                                            break;
                                        }
                                    }
                                }
                            }//end of while iter.hasNext()


                        }
                        catch( Exception e){
                            e.printStackTrace();
                        }

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

        mainList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                final DatabaseReference myRef = database.getReference("history");

                Query myQuery = myRef.orderByValue().equalTo((String)
                        mainList.getItemAtPosition(position));

                myQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChildren()) {
                            DataSnapshot firstChild = dataSnapshot.getChildren().iterator().next();
                            firstChild.getRef().removeValue();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                })
                ;}
        });

        mainList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public  void onItemClick(AdapterView<?> parent,View v,int position, long id){
                String a =  (String) parent.getItemAtPosition(position);
                Intent nextScreen = new Intent(v.getContext(), FoodActivity.class);
                nextScreen.putExtra("foodName", a);
                startActivity(nextScreen);
            }
        });
        return v;
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
    }
}