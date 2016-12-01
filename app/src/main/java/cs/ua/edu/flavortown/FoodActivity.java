package cs.ua.edu.flavortown;

import android.content.Intent;
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
    String restKey;
    String foodKey;
    // Hardcoded for right now
    String queryName;
    //String queryName = "chat";

    TextView mFoodName;
    RatingBar mFoodRatingBar;
    TextView mFoodRatingValue;
    Button mFoodButtonRate;

    DatabaseReference mRootRef;
    DatabaseReference mFoodRef;
    DatabaseReference mCurrRating;
    DatabaseReference mNumOfRating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);

        final Bundle extras = getIntent().getExtras();
        restKey = extras.getString("restaurantKey");
        foodKey = extras.getString("foodIterator");
        queryName = "restaurantTable/" + restKey + "/menu/" + foodKey;

        mFoodName = (TextView) findViewById(R.id.txtFoodName);
        mFoodRatingBar = (RatingBar) findViewById(R.id.barFoodRatingBar);
        mFoodRatingValue = (TextView) findViewById(R.id.txtFoodRatingValue);
        mFoodButtonRate = (Button) findViewById(R.id.buttonFoodRate);

        mFoodName.setText(extras.getString("foodName"));
        mFoodRatingBar.setRating(extras.getFloat("foodCurrRating"));
        mFoodRatingValue.setText(String.valueOf(extras.getFloat("foodCurrRating")));

        mFoodName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getBaseContext(), "Rating calculated using "+ String.valueOf(extras.getInt("foodNumOfRatings")), Toast.LENGTH_LONG).show();
            }
        });

        mFoodButtonRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v("FoodActivity", "Rate Button Pressed");
                //Intent foodScreen = new Intent(getBaseContext(), RatingActivity.class);

                //startActivity(foodScreen);
            }
        });


    }

}