package com.garcon.garcon;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.garcon.Constants.Constants;

import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    private EditText editTextEmail;
    private EditText editTextPassword;
    private TextView textViewPersons;
    private Button buttonSave;

    private View mProgressView;
    private View mSignUpFormView;

    private UserSignUpTask mSignupTask = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        buttonSave = (Button) findViewById(R.id.buttonSave);
        editTextEmail = (EditText) findViewById(R.id.editTextName);
        editTextPassword = (EditText) findViewById(R.id.editTextAddress);

        Firebase.setAndroidContext(this);

        //Click Listener for button
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptSignUp();
            }
        });
        mProgressView = findViewById(R.id.signup_progress);
        mSignUpFormView = findViewById(R.id.signup_form);
    }

    private void attemptSignUp() {
        if (mSignupTask != null) {
            return;
        }

        // Reset errors.
        editTextEmail.setError(null);
        editTextPassword.setError(null);

        // Store values at the time of the login attempt.
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            editTextPassword.setError(getString(R.string.error_invalid_password));
            focusView = editTextPassword;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            editTextEmail.setError(getString(R.string.error_field_required));
            focusView = editTextEmail;
            cancel = true;
        } else if (!isEmailValid(email)) {
            editTextEmail.setError(getString(R.string.error_invalid_email));
            focusView = editTextEmail;
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
            mSignupTask = new UserSignUpTask(email, password);
            mSignupTask.execute((Void) null);
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mSignUpFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mSignUpFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mSignUpFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mSignUpFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
    
    
    public class UserSignUpTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        boolean signUpFlag = false;
        boolean doneFlag = false;
        UserSignUpTask(String email,String password){
            mEmail=email;
            mPassword=password;

        }

        public void updateSignUpFlag(boolean tflag){

            signUpFlag = tflag;
        }
        public void updateDoneFlag(boolean dflag){

            doneFlag = dflag;
        }
        @Override
        protected Boolean doInBackground(Void... params) {
            //Creating firebase object

                Firebase ref = new Firebase(Constants.FIREBASE_URL);


                ref.createUser(mEmail, mPassword, new Firebase.ValueResultHandler<Map<String, Object>>() {
                    @Override
                    public void onSuccess(Map<String, Object> result) {
                        System.out.println("Successfully created user account with uid: " + result.get("uid"));
                        Toast.makeText(SignUpActivity.this, R.string.success_account_created, Toast.LENGTH_LONG).show();
                        updateSignUpFlag(true);
                        updateDoneFlag(true);
                    }

                    @Override
                    public void onError(FirebaseError firebaseError) {
                        // there was an error
                        System.out.println("Error Code: " + firebaseError.getCode());
                        System.out.println("Error : " + firebaseError.getMessage());
                        updateSignUpFlag(false);
                        if (firebaseError.getCode() == -18) {
                            editTextEmail.setError(getString(R.string.error_email_exists));
                            editTextEmail.requestFocus();
                        } else {
                            Toast.makeText(SignUpActivity.this, R.string.error_oops, Toast.LENGTH_LONG).show();
                        }
                        updateDoneFlag(true);
                    }
                });
                //Thread.sleep(4000);
                while(!doneFlag){}
                if (signUpFlag) {
                    return true;
                } else {
                    return false;
                }



        }

        @Override
        protected void onPostExecute(Boolean success) {
            mSignupTask = null;
            showProgress(false);
            System.out.println("Async task complete");

            if(success){
                System.out.println("Finishing the activity");
                finish();
            }
        }
    }
}
