package cs.ua.edu.flavortown;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FoodActivity extends AppCompatActivity {

    //Have to get from Restaurant View
    String restName = "rest1";
    String foodName = "food1";
    // Hardcoded for right now
    String queryName = "restaurantTable/" + restName + "/menu/" + foodName;
    //String queryName = "chat";

    TextView mFoodName;
    RatingBar mFoodRatingBar;
    TextView mFoodRatingValue;
    Button mFoodButtonSubmit;

    DatabaseReference mRootRef;
    DatabaseReference mFoodRef;
    DatabaseReference mCurrRating;
    DatabaseReference mNumOfRating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);

        mRootRef = FirebaseDatabase.getInstance().getReference();
        mFoodRef = mRootRef.child(queryName);
        mCurrRating = mFoodRef.child("currRating");
        mNumOfRating = mFoodRef.child("numOfRating");

        mFoodName = (TextView) findViewById(R.id.txtFoodName);
        mFoodRatingBar = (RatingBar) findViewById(R.id.barFoodRatingBar);
        mFoodRatingValue = (TextView) findViewById(R.id.txtFoodRatingValue);
        mFoodButtonSubmit = (Button) findViewById(R.id.buttonFoodSubmit);

        //addListenerOnRatingBar();
        //addListenerOnButton();
    }

    public void addListenerOnRatingBar() {

        //if rating value is changed,
        //display the current rating value in the result (textview) automatically
        mFoodRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {
                mFoodRatingValue.setText(String.valueOf(rating));
            }
        });
    }


    public void addListenerOnButton() {

        //if click on me, then display the current rating value.
        mFoodButtonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(FoodActivity.this,
                        String.valueOf(mFoodRatingBar.getRating()),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mFoodRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Food foodItem = dataSnapshot.getValue(Food.class);
                Log.v("TEST_CurrRating", Float.toString(foodItem.getCurrRating()));
                Log.v("TEST_Flag", foodItem.getFlag());
                Log.v("TEST_FoodItem", foodItem.getFoodItem());
                Log.v("TEST_NumOfRating", Float.toString(foodItem.getNumOfRating()));

                float rate = foodItem.getCurrRating();
                mFoodName.setText(foodItem.getFoodItem());
                mFoodRatingBar.setRating(rate);
                mFoodRatingValue.setText(Float.toString(rate));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mNumOfRating.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mFoodButtonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float yourRate = mFoodRatingBar.getRating();
                /*
                float rate = mRatingBar.getRating();
                mRatingRef.setValue(rate);
                mRatingListRef.push().setValue(rate);
                */
            }
        });
    }
}