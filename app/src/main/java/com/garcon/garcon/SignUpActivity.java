package com.garcon.garcon;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.garcon.Constants.Constants;
import com.garcon.Models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Map;
import java.util.concurrent.Executor;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {

    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextFirstName;
    private EditText editTextLastName;
    private EditText editTextUserName;
    private EditText editTextConfirmPassword;
    private EditText editTextPhoneNumber;



    private Button buttonSave;

    private View mProgressView;
    private View mSignUpFormView;

    private UserSignUpTask mSignupTask = null;

    //Firebase
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    //private FirebaseAuth.AuthStateListener mAuthListener;
    private static final String PASSWORD_PATTERN = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,20})";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        buttonSave = (Button) findViewById(R.id.buttonSave);
        editTextEmail = (EditText) findViewById(R.id.signup_email);
        editTextPassword = (EditText) findViewById(R.id.signup_password);
        editTextFirstName = (EditText) findViewById(R.id.signup_firstname);
        editTextLastName = (EditText) findViewById(R.id.signup_lastname);
        editTextUserName = (EditText) findViewById(R.id.signup_username);
        editTextConfirmPassword = (EditText) findViewById(R.id.signup_confirmpassword);
        editTextPhoneNumber = (EditText) findViewById(R.id.signup_phonenumber);


        mAuth = FirebaseAuth.getInstance();

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
    @Override
    public void onStart() {
        super.onStart();
        //mAuth.addAuthStateListener(mAuthListener);
    }
    @Override
    public void onStop() {
        super.onStop();
        /*
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }*/
    }

    private void attemptSignUp() {
        if (mSignupTask != null) {
            return;
        }

        // Reset errors.
        editTextEmail.setError(null);
        editTextPassword.setError(null);

        // Store values at the time of the login attempt.
        String firstName = editTextFirstName.getText().toString();
        String lastName =  editTextLastName.getText().toString();
        String userName = editTextUserName.getText().toString();
        String phoneNumber = editTextPhoneNumber.getText().toString();
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();
        String confirmPassword = editTextConfirmPassword.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            editTextPassword.setError(getString(R.string.error_invalid_password));
            focusView = editTextPassword;
            cancel = true;
        }

        if(!confirmPassword.equals(password)){
            editTextConfirmPassword.setError(getString(R.string.error_incorrect_confirmpassword));
            focusView = editTextConfirmPassword;
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
            mSignupTask = new UserSignUpTask(email, password,firstName,lastName,phoneNumber,userName);
            mSignupTask.execute((Void) null);
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        Pattern passwordPattern = Pattern.compile(PASSWORD_PATTERN);
        Matcher passwordMatcher = passwordPattern.matcher(password);

        return passwordMatcher.matches();
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

        //private final String mEmail;
        private final String mPassword;
        User newUser = new User();
        boolean signUpFlag = false;
        boolean doneFlag = false;
        UserSignUpTask(String email,String password,String firstName, String lastName, String phoneNumber, String userName){
            //mEmail=email;
            mPassword=password;
            newUser.seteMail(email);
            newUser.setFirstName(firstName);
            newUser.setLastName(lastName);
            newUser.setPhoneNumber(phoneNumber);
            newUser.setUserName(userName);

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



                /*
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
               */

            mAuth.createUserWithEmailAndPassword(newUser.geteMail(), mPassword)
                    .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            System.out.println( "createUserWithEmail:onComplete:" + task.isSuccessful());

                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful()) {
                                Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                System.out.println("Error Code: " + task.getException());
                                updateSignUpFlag(false);
                                /*System.out.println("Error : " + firebaseError.getMessage());
                                if (firebaseError.getCode() == -18) {
                                    editTextEmail.setError(getString(R.string.error_email_exists));
                                    editTextEmail.requestFocus();
                                } else {
                                    Toast.makeText(SignUpActivity.this, R.string.error_oops, Toast.LENGTH_LONG).show();
                                }*/
                                updateDoneFlag(true);
                            }
                            else {
                                System.out.println("Successfully created user account with uid: "+ mAuth.getCurrentUser().getUid());
                                Toast.makeText(SignUpActivity.this, R.string.success_account_created, Toast.LENGTH_LONG).show();
                                newUser.setUserUID(mAuth.getCurrentUser().getUid());
                                writeSignUpData(newUser);
                                updateSignUpFlag(true);
                                updateDoneFlag(true);
                            }

                            // ...
                        }
                    });
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

        public void writeSignUpData(User newUser){
            mDatabase = FirebaseDatabase.getInstance().getReference();
            mDatabase.child("users").child(newUser.getUserUID()).setValue(newUser);

        }

    }
}
