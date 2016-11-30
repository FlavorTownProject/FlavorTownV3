package cs.ua.edu.flavortown;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

                int attempt = attemptLogin(email,password);

                if (attempt == 1) {//TODO: Credential Verification
                    Toast.makeText(v.getContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                    Intent nextScreen = new Intent(v.getContext(), MainActivity.class);
                    startActivity(nextScreen);
                    setContentView(R.layout.activity_main);
                } else {
                    Toast.makeText(v.getContext(), "Login Failed", Toast.LENGTH_SHORT).show();
                    editPassword.setText("");
                }

                /*
                if (attempt == 0){
                    Toast.makeText(v.getContext(), "Username Incorrect", Toast.LENGTH_SHORT).show();
                }
                else if (attempt == 1){
                    Toast.makeText(v.getContext(), "Password Incorrect", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(v.getContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                    Intent nextScreen = new Intent(v.getContext(), MainActivity.class);
                    startActivity(nextScreen);
                    setContentView(R.layout.activity_main);
                }

                 */
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

    private int attemptLogin(String email, String password) {
        return 1;
        /*
        if (!isEmailValid(email)){ return -1; }
        else if (!isPasswordValid(password)){ return 0; }
        return 1;

         */

    }


}