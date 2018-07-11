package com.garcon.garcon;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.garcon.Models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.*;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Activity for requesting details from the user to signup.
 * The phone number and passwords are validated.
 */
public class SignUpActivity extends AppCompatActivity {

    public static final String MyPREFERENCES = "GarconPref";
    public static final String FirstName = "firstnameKey";
    public static final String Phone = "phoneKey";
    public static final String LastName = "lastnameKey";
    private static final String TAG = SignUpActivity.class.getName();
    SharedPreferences sharedpreferences;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextFirstName;
    private EditText editTextLastName;
    private EditText editTextPhoneNumber;
    private Button buttonSave;
    private Button buttonSaveDetails;
    private Button buttonCancelDetails;
    private View mSignUpFormView;
    private View mProgressView;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        buttonSave = (Button) findViewById(R.id.buttonSave);
        //---------buttons visibility for manage profile set hidden-----------//
        buttonSaveDetails = (Button) findViewById(R.id.btSaveDetails);
        buttonCancelDetails = (Button) findViewById(R.id.btCancelDetails);
        buttonSaveDetails.setVisibility(View.GONE);
        buttonCancelDetails.setVisibility(View.GONE);
        //-----------------------------------------------------------------//
        editTextEmail = (EditText) findViewById(R.id.signup_email);
        editTextPassword = (EditText) findViewById(R.id.signup_password);
        editTextFirstName = (EditText) findViewById(R.id.signup_firstname);
        editTextLastName = (EditText) findViewById(R.id.signup_lastname);
        editTextPhoneNumber = (EditText) findViewById(R.id.signup_phonenumber);
        mSignUpFormView = findViewById(R.id.signup_form);
        mProgressView = findViewById(R.id.signup_progress);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        //---------buttons visibility for manage profile set hidden-----------//
        buttonSaveDetails = (Button) findViewById(R.id.btSaveDetails);
        buttonCancelDetails = (Button) findViewById(R.id.btCancelDetails);
        buttonSaveDetails.setVisibility(View.GONE);
        buttonCancelDetails.setVisibility(View.GONE);
        //-----------------------------------------------------------------//

        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptSignUp();
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();

    }

    private void attemptSignUp() {

        // Reset errors.
        editTextEmail.setError(null);
        editTextPassword.setError(null);

        String firstName = editTextFirstName.getText().toString();
        String lastName = editTextLastName.getText().toString();
        String phoneNumber = editTextPhoneNumber.getText().toString();
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(FirstName, firstName);
        editor.putString(Phone, phoneNumber);
        editor.putString(LastName, lastName);
        editor.commit();
        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.

        if ( (firstName.length()==0) ) {
            editTextFirstName.setError("Name required");
            focusView = editTextFirstName;
            cancel = true;
        }

        if ( (lastName.length()==0) ) {
            editTextLastName.setError("Name required");
            focusView = editTextLastName;
            cancel = true;
        }


        if ( (password.length()==0) || !isPasswordValid(password) ) {
            editTextPassword.setError(getString(R.string.error_invalid_password));
            focusView = editTextPassword;
            cancel = true;
        }

        if (!isPhoneNumberValid(phoneNumber)) {
            editTextPhoneNumber.setError(getString(R.string.error_incorrect_phonenumber));
            focusView = editTextPhoneNumber;
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
            focusView.requestFocus();
        } else {

            final String mPassword;
            final User newUser = new User();
            mPassword = password;
            newUser.seteMail(email);
            newUser.setFirstName(firstName);
            newUser.setLastName(lastName);
            newUser.setPhoneNumber(phoneNumber);
            mProgressView.setVisibility(View.VISIBLE);

            mAuth.createUserWithEmailAndPassword(newUser.geteMail(), mPassword)
                    .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful()) {
                                if (!task.getException().toString().contains("already in use")) {

                                    mProgressView.setVisibility(View.GONE);
                                    Toast.makeText(SignUpActivity.this, "Some Error has occured",
                                            Toast.LENGTH_SHORT).show();
                                    Log.i("TAG", task.getException().toString());

                                }
                            } else {
                                mProgressView.setVisibility(View.GONE);
                                Toast.makeText(SignUpActivity.this, R.string.success_account_created, Toast.LENGTH_LONG).show();
                                newUser.setUserUID(mAuth.getCurrentUser().getUid());
                                writeSignUpData(newUser);
                                FirebaseAuth.getInstance().signOut();
                                finish();
                            }

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        public void onFailure(@NonNull Exception e) {
                            mProgressView.setVisibility(View.GONE);
                            Toast.makeText(SignUpActivity.this, e.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });


        }
    }

    public void writeSignUpData(User newUser) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("users").child(newUser.getUserUID()).setValue(newUser);

    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        if (password.length() >= 6)
            return true;
        else
            return false;
    }

    private boolean isPhoneNumberValid(String phoneNumber) {
        Log.d(TAG, "phone Number entered --> " + phoneNumber);

        if (phoneNumber.length() == 10)
            return true;
        else
            return false;
    }

}


