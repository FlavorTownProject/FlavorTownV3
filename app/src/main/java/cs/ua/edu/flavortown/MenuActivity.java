package cs.ua.edu.flavortown;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.auth.api.model.StringList;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MenuActivity extends AppCompatActivity {

    RestaurantInfo currentRestuarant;
    TextView restaurantName;
    DatabaseReference restaurantTable;
    JSONObject jsonTable;

    String LOGTAG = "MenuActvity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        restaurantName = (TextView) findViewById(R.id.restaurantTextView);

        currentRestuarant = new RestaurantInfo();

        Bundle extras = getIntent().getExtras();
        currentRestuarant.setRestName(extras.getString("restaurantName"));
        currentRestuarant.setGoogleID(extras.getString("restaurantID"));
        restaurantName.setText(extras.getString("restaurantName"));

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        // Get a reference to the todoItems child items it the database
        restaurantTable = database.getReference("restaurantTable");
        restaurantTable.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String ID = "";
                try {
                    Log.v(LOGTAG, "in onDataChange");
                    for(DataSnapshot messageSnapshot: dataSnapshot.getChildren()){
                        String name = (String) messageSnapshot.child("restName").getValue();
                        Log.v(LOGTAG,"name = "+ name);
                        ID = (String) messageSnapshot.child("id").getValue();
                        Log.v(LOGTAG,"id = "+ ID);
                        if(ID.equals(currentRestuarant.getGoogleID())) {
                            Log.v(LOGTAG, messageSnapshot.getValue().toString());
                            Log.v(LOGTAG, "menuLength = "+ String.valueOf(messageSnapshot.child("menuLength").getValue(int.class)));
                            Menu currMenu = new Menu(messageSnapshot.child("menuLength").getValue(int.class));
                            Food temp = new Food();
                            String tagIter;
                            for(int x = 0 ; x < currMenu.length; x++)
                            {
                                tagIter = stringIncrementer("food", x);
                                Log.v(LOGTAG, "tagIter = "+ tagIter);
                                temp.setFoodItem( (String) messageSnapshot.child("menu").child(tagIter).child("name").getValue());
                                Log.v(LOGTAG, "foodItem = "+ temp.getFoodItem());
                                temp.setNumOfRating(messageSnapshot.child("menu").child(tagIter).child("numOfRating").getValue(int.class));
                                Log.v(LOGTAG, "# of ratings = "+ temp.getNumOfRating());
                                if(temp.getNumOfRating() > 0)
                                {
                                    Log.v(LOGTAG,"Getting ratings");
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
                                Log.v(LOGTAG, "flag = "+ temp.getFlag());
                                Log.v(LOGTAG, "adding food to menu");
                                currMenu.addToFoodList(temp);

                            }
                            if(currMenu.length > 0){
                                Log.v(LOGTAG, "Populated menu");
                                currentRestuarant.setMenu(currMenu);
                            }
                            else
                            {
                                Log.v(LOGTAG, "No menu detected...");
                                currentRestuarant.setMenu(null);
                            }
                            break;//found the restaurant and retrieved the menu
                        }//end of if

                    }//end of for loop

                    /*
                    Log.v(LOGTAG, "in onDataChange");
                    HashMap databaseValue = (HashMap) dataSnapshot.getValue();//jsonTable =  dataSnapshot.getValue(JSONObject.class);
                    Log.v(LOGTAG, "databaseValue before JSON");
                    Log.v(LOGTAG, databaseValue.toString());
                    jsonTable = dataSnapshot.getValue(JSONObject.class);//new JSONObject(databaseValue.toString());
                    Log.v(LOGTAG, jsonTable.toString());//Log.v(LOGTAG,hashMap.toString());
                    //getResturantMenu();*/
                }
                catch( Exception e){
                    e.printStackTrace();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private String stringIncrementer(String baseString, int value)
    {
        return baseString.concat(String.valueOf(value));
    }
    /*
    private JSONObject getResturantMenu ()
    {
        JSONObject menu = null;
        try {
            Log.v(LOGTAG, "Getting Menu");
            menu = jsonTable.getJSONObject(currentRestuarant.getGoogleID());
            Log.v(LOGTAG, menu.toString());
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return menu;
    }
    */
}
