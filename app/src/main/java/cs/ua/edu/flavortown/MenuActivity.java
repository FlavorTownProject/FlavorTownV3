package cs.ua.edu.flavortown;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
    DatabaseReference restaurantTable;

    TextView restaurantName;
    Button topItemButton;
    ListView menuList;


    String LOGTAG = "MenuActvity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        restaurantName = (TextView) findViewById(R.id.restaurantNameTextView);
        currentRestuarant = new RestaurantInfo();

        Bundle extras = getIntent().getExtras();
        currentRestuarant.setRestName(extras.getString("restaurantName"));
        currentRestuarant.setGoogleID(extras.getString("restaurantID"));
        restaurantName.append(extras.getString("restaurantName"));

        topItemButton = (Button) findViewById(R.id.topItemButton);
        menuList = (ListView) findViewById(R.id.menuList);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        // Get a reference to the todoItems child items it the database
        restaurantTable = database.getReference("restaurantTable");
        restaurantTable.addValueEventListener(new ValueEventListener(){
            @Override//please don't touch this listener unless you have to. -Ian
            public void onDataChange(DataSnapshot dataSnapshot) {

                String ID = "";
                try {
                    Log.v(LOGTAG, "in onDataChange");
                    for(DataSnapshot messageSnapshot: dataSnapshot.getChildren()){
                        messageSnapshot.getKey();
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
                                temp.setIterTag(tagIter);
                                Log.v(LOGTAG, "flag = "+ temp.getFlag());
                                Log.v(LOGTAG, "adding food to menu");
                                currMenu.addToFoodList(temp);
                                Log.v(LOGTAG, "First two menu items after adding");
                                Log.v(LOGTAG, "0 = " + currMenu.foodList[0].getFoodItem());
                                Log.v(LOGTAG, "1 = " + currMenu.foodList[1].getFoodItem());
                            }
                            if(currMenu.length > 0){
                                Log.v(LOGTAG, "Populated menu");
                                //currMenu.sortItemsByRating();
                                Log.v(LOGTAG, "First two menu items before setting currRest");
                                Log.v(LOGTAG, "0 = " + currMenu.foodList[0].getFoodItem());
                                Log.v(LOGTAG, "1 = " + currMenu.foodList[1].getFoodItem());
                                currMenu.sortItemsByRating();
                                currentRestuarant.setMenu(currMenu);
                                Log.v(LOGTAG, "First two menu items after setting currRest");
                                Log.v(LOGTAG, "0 = " + currMenu.foodList[0].getFoodItem());
                                Log.v(LOGTAG, "1 = " + currMenu.foodList[1].getFoodItem());
                                populateMenu(true);
                            }
                            else
                            {
                                Log.v(LOGTAG, "No menu detected...");
                                currentRestuarant.setMenu(null);
                                populateMenu(false);
                            }
                            break;//found the restaurant and retrieved the menu
                        }//end of if

                    }//end of for loop

                }
                catch( Exception e){
                    e.printStackTrace();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

         topItemButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {

                 openFood(0);
             }
         });

        menuList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            //@Override
            public  void onItemClick(AdapterView<?> parent,View v,int position, long id){
                //Object a = restaurantList.getSelectedItem();
                Log.v(LOGTAG, "In menuList listener | position = " + String.valueOf(position));
                String a =  (String) parent.getItemAtPosition(position);
                openFood(position);
                Toast.makeText(v.getContext(), a, Toast.LENGTH_SHORT).show();
            }
        });




    }//end of onCreate

    private String stringIncrementer(String baseString, int value) {
        return baseString.concat(String.valueOf(value));
    }

    private String[] menuItemsToArray()
    {
        Food[] pullMenu = currentRestuarant.getMenu().getFoodList();
        String listItems[] = new String[currentRestuarant.getMenu().length];
        for(int x = 0; x < pullMenu.length; x++) {
            Log.v(LOGTAG, "pullMenu[x] = "+ pullMenu[x].getFoodItem());
            listItems[x] = pullMenu[x].getFoodItem();
            Log.v(LOGTAG, x + " = " + listItems[x]);
        }
        return  listItems;
    }

    private String[] menuRatingsToArray()
    {
        Food[] pullMenu = currentRestuarant.getMenu().getFoodList();
        String listItems[] = new String[currentRestuarant.getMenu().length];
        for(int x = 0; x < pullMenu.length; x++) {
            listItems[x] = String.valueOf(pullMenu[x].getCurrRating());
        }
        return  listItems;
    }

    private void populateMenu(boolean menuIsNotNull)
    {
        if (menuIsNotNull) {
            ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, menuItemsToArray());
            menuList.setAdapter(adapter);
            topItemButton.append(currentRestuarant.getMenu().getHighestRatedItem().getFoodItem());
        }
        else
        {
            topItemButton.setText("Menu is not available for this restaurant");
        }
    }

    private void openFood(int position)
    {

        Food[] foodList = currentRestuarant.getMenu().getFoodList();
            Intent foodScreen = new Intent(this, FoodActivity.class);
            foodScreen.putExtra("foodName", foodList[position].getFoodItem());
            foodScreen.putExtra("foodIterator",foodList[position].getIterTag());
            foodScreen.putExtra("foodCurrRating", foodList[position].getCurrRating());
            foodScreen.putExtra("foodNumOfRatings", foodList[position].getNumOfRating());
            foodScreen.putExtra("foodRatings", foodList[position].getRatings());
            foodScreen.putExtra("restaurantID",currentRestuarant.getGoogleID());
            foodScreen.putExtra("restaurantKey", currentRestuarant.getDbKey());
            startActivity(foodScreen);

    }

}
