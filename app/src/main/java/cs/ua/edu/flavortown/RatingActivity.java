package cs.ua.edu.flavortown;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

public class RatingActivity extends AppCompatActivity {

    RatingBar mRatingBar;
    TextView mRatingValue;
    Button mButtonSubmit;

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mRatingRef = mRootRef.child("rating");
    DatabaseReference mRatingListRef = mRootRef.child("ratinglist");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating_bar);

        mRatingBar = (RatingBar) findViewById(R.id.barRatingBar);
        mRatingValue = (TextView) findViewById(R.id.txtRatingValue);
        mButtonSubmit = (Button) findViewById(R.id.buttonSubmit);

        //addListenerOnRatingBar();
        //addListenerOnButton();
    }


    public void addListenerOnRatingBar() {

        //if rating value is changed,
        //display the current rating value in the result (textview) automatically
        mRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {
                mRatingValue.setText(String.valueOf(rating));
            }
        });
    }

    public void addListenerOnButton() {

        //if click on me, then display the current rating value.
        mButtonSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(RatingActivity.this,
                        String.valueOf(mRatingBar.getRating()),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mRatingRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                float rate = dataSnapshot.getValue(float.class);
                mRatingBar.setRating(rate);
                mRatingValue.setText(Float.toString(rate));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mButtonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float rate = mRatingBar.getRating();
                mRatingRef.setValue(rate);
                mRatingListRef.push().setValue(rate);
            }
        });
    }
}