package cs.ua.edu.flavortown;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

public class RatingActivity extends AppCompatActivity {

    RatingBar mRatingBar;
    TextView mTextRating;
    Button mButtonSubmit;
    Button mButtonCancel;
    EditText mComments;

    DatabaseReference mRootRef;
    DatabaseReference mRatingListRef;
    DatabaseReference mRatingNumber;
    DatabaseReference mNumOfRatingRef;
    DatabaseReference mFoodNameRef;

    String restID;
    String restKey;
    String foodKey;
    //Hardcoded for right now
    String queryName;
    int numOfRating;
    float currRating;


    //DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    //DatabaseReference mRatingRef = mRootRef.child("rating");
    //DatabaseReference mRatingListRef = mRootRef.child("ratinglist");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_food);

        final Bundle newRatings = getIntent().getExtras();
        restID = newRatings.getString("restaurantKey");
        foodKey = newRatings.getString("foodIterator");
        numOfRating = newRatings.getInt("foodNumOfRatings");
        currRating = newRatings.getFloat("foodCurrRating");
        queryName = "restaurantTable/" + restID + "/menu/" + foodKey;

        mRootRef = FirebaseDatabase.getInstance().getReference();
        String ratingList = queryName + "/rating";
        String ratingNumber = queryName + "/currRating";
        String foodName = queryName + "/name";
        String numOfReader = queryName + "/numOfRating";

        mRatingListRef = mRootRef.child(ratingList);
        mRatingNumber = mRootRef.child(ratingNumber);
        mNumOfRatingRef = mRootRef.child(numOfReader);
        mFoodNameRef = mRootRef.child(foodName);

        mRatingBar = (RatingBar) findViewById(R.id.rateButton);
        mComments = (EditText) findViewById(R.id.txt_description);
        mTextRating = (TextView) findViewById(R.id.txt_food);
        mButtonSubmit = (Button) findViewById(R.id.btn_submit);
        mButtonCancel = (Button) findViewById(R.id.btn_cancel);

        mRatingNumber.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                float number1 = newRatings.getFloat("foodCurrRating");
                //potential for it to be already there...
                //float number2 = dataSnapshot.getValue(float.class);
                mRatingBar.setRating(number1);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mButtonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                float number = mRatingBar.getRating();
                mRatingListRef.push().setValue(number);
                mNumOfRatingRef.runTransaction(new Transaction.Handler() {
                    @Override
                    public Transaction.Result doTransaction(MutableData mutableData) {
                        int numOfRatings = mutableData.getValue(int.class);
                        int newNum = numOfRatings + 1;
                        mutableData.setValue(newNum);
                        return Transaction.success(mutableData);
                    }
                    @Override
                    public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                    }
                });
                mRatingNumber.runTransaction(new Transaction.Handler() {
                    @Override
                    public Transaction.Result doTransaction(MutableData mutableData) {

                        return Transaction.success(mutableData);
                    }

                    @Override
                    public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {

                    }
                });
            }
        });

        mButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}