package cs.ua.edu.flavortown;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

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
                try {
                    Log.v(LOGTAG, "in onDataChange");
                    HashMap hashMap = (HashMap) dataSnapshot.getValue();//jsonTable =  dataSnapshot.getValue(JSONObject.class);
                    jsonTable = new JSONObject(hashMap.toString());
                    Log.v(LOGTAG, jsonTable.toString());//Log.v(LOGTAG,hashMap.toString());
                    //getResturantMenu();
                }
                catch(JSONException e){
                    e.printStackTrace();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

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
}
