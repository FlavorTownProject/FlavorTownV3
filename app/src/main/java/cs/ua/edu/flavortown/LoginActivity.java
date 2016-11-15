package cs.ua.edu.flavortown;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        final EditText editEmail = (EditText) findViewById(R.id.email);
        final EditText editPassword = (EditText) findViewById(R.id.password);
        final Button btnLogin = (Button) findViewById(R.id.email_sign_in_button);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String email = editEmail.getText().toString();
                String password = editPassword.getText().toString();
                boolean failed = attemptLogin(email,password);
                if (!failed) {//TODO: Credential Verification
                    Toast.makeText(v.getContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                    Intent nextScreen = new Intent(v.getContext(), MainActivity.class);
                    startActivity(nextScreen);
                    setContentView(R.layout.activity_main);
                } else {
                    Toast.makeText(v.getContext(), "Login Failed", Toast.LENGTH_SHORT).show();
                    editPassword.setText("");
                }






            }
        });


    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    private boolean attemptLogin(String email, String password) {
        return false;
        //TODO: Add database stuff
       /* if (mAuthTask != null) {
            mEmailView.setError(getString(R.string.error_field_required));
            mPasswordView.setError(getString(R.string.error_field_required));
            return true;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;


        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password)){
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        } else if(!isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }



        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);
        }
        return cancel;
        */
    }


}