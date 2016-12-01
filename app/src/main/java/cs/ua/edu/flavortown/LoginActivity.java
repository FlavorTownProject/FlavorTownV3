package cs.ua.edu.flavortown;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    DatabaseReference mRootRef;
    DatabaseReference mUserTableRef;

    User enteredInfo;
    int loginValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText editEmail = (EditText) findViewById(R.id.email);
        final EditText editPassword = (EditText) findViewById(R.id.password);
        final Button btnLogin = (Button) findViewById(R.id.email_sign_in_button);

        mRootRef = FirebaseDatabase.getInstance().getReference();
        mUserTableRef = mRootRef.child("UserTable");

        btnLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                String email = editEmail.getText().toString();
                String password = editPassword.getText().toString();
                attemptLogin(email, password);

                /*
                if (loginValue == 0){
                    Toast.makeText(v.getContext(), "Username Incorrect", Toast.LENGTH_SHORT).show();
                }
                else if (loginValue == 1){
                    Toast.makeText(v.getContext(), "Password Incorrect", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(v.getContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                    Intent nextScreen = new Intent(v.getContext(), MainActivity.class);
                    startActivity(nextScreen);
                    setContentView(R.layout.activity_main);
                }
                */
                Toast.makeText(v.getContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                Intent nextScreen = new Intent(v.getContext(), MainActivity.class);
                startActivity(nextScreen);
                setContentView(R.layout.activity_main);
            }
        });


    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic

        /*
        Query queryRef = mUserTableRef.equalTo(email);
        return (queryRef != null);

         */
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        /*
        Query queryRef = mUserTableRef.equalTo(email);

        queryRef.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot snapshot, String previousChild) {
                    User userInfo = snapshot.getValue(User.class);
                    return (userInfo.getPassword() == password);
                }
            }
         return false;
         */
        return password.length() > 4;
    }

    private void attemptLogin(String email, final String password) {
        //return 1;
        enteredInfo = new User();
        enteredInfo.setEmail(email);
        enteredInfo.setPassword(password);

        final FirebaseDatabase db = FirebaseDatabase.getInstance();
        mUserTableRef = db.getReference("userTable");
        mUserTableRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String ID ="";
                try{
                    //Log.v(LOGTAG, "in onDataChange");
                    for(DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {
                        String emailDB = (String) messageSnapshot.child("email").getValue();
                        String passwordDB = (String) messageSnapshot.child("password").getValue();

                        if (!(enteredInfo.getEmail().equals(emailDB))) {
                            loginValue = 0;
                            break;
                        }
                        else if (!enteredInfo.getPassword().equals(passwordDB)) {
                            loginValue = 1;
                            break;
                        }
                        else if (enteredInfo.getEmail().equals(emailDB) && enteredInfo.getPassword().equals(passwordDB)){
                            loginValue = 2;
                        }
                    }
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


}