package cs.ua.edu.flavortown;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
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
    int loginValue = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText editEmail = (EditText) findViewById(R.id.email);
        final EditText editPassword = (EditText) findViewById(R.id.password);
        final Button btnLogin = (Button) findViewById(R.id.email_sign_in_button);

        mRootRef = FirebaseDatabase.getInstance().getReference();
        mUserTableRef = mRootRef.child("UserTable");

        btnLogin.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent arg1) {

                String email = editEmail.getText().toString();
                String password = editPassword.getText().toString();
                if (arg1.getAction()==MotionEvent.ACTION_DOWN) {
                    attemptLogin(email, password);
                }else if (arg1.getAction() == MotionEvent.ACTION_UP){
                    if (loginValue == 0) {
                        Toast.makeText(v.getContext(), "Username Incorrect", Toast.LENGTH_SHORT).show();
                    } else if (loginValue == 1) {
                        Toast.makeText(v.getContext(), "Password Incorrect", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(v.getContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                        Intent nextScreen = new Intent(v.getContext(), MainActivity.class);
                        startActivity(nextScreen);
                    }
                }
                return true;
            }
        });
    }



    private void attemptLogin(String email, final String password) {
        enteredInfo = new User();
        enteredInfo.setEmail(email);
        enteredInfo.setPassword(password);

        final FirebaseDatabase db = FirebaseDatabase.getInstance();
        mUserTableRef = db.getReference("userTable");
        mUserTableRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try{
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