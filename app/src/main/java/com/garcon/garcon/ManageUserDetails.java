package com.garcon.garcon;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.garcon.Models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ManageUserDetails extends Fragment {

    private static final String TAG = ManageUserDetails.class.getName();

    //add Firebase Database details
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;
    private String userID;

    //fields
    private EditText etFirstName;
    private EditText etLastName;
    private EditText etEmail;
    private EditText etContact;
    private EditText etPassword;
    private Button btSignUp;
    private Button btSave;
    private Button btCancel;

    private View view;
    View focusView = null;
    boolean cancel = false;


    //constructor
    public ManageUserDetails() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.activity_signup, container, false);

        etFirstName = (EditText) view.findViewById(R.id.signup_firstname);
        etLastName = (EditText) view.findViewById(R.id.signup_lastname);
        etEmail = (EditText)view.findViewById(R.id.signup_email);
        etContact = (EditText) view.findViewById(R.id.signup_phonenumber);
        etPassword = (EditText) view.findViewById(R.id.signup_password);

        btSave = (Button) view.findViewById(R.id.btSaveDetails);
        btCancel = (Button) view.findViewById(R.id.btCancelDetails);

        //-------------------signup button visibility set hidden-----------------------//
        btSignUp = (Button) view.findViewById(R.id.buttonSave);
        btSignUp.setVisibility(View.GONE);
        //---------------------------------------------------------------------------//

        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserDetails();
            }
        });

        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), homeactivity.class);
                startActivity(intent);
            }
        });

        //declare the database reference object. This is what we use to access the database.
        //NOTE: Unless you are signed in, this will not be useable.
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReferenceFromUrl("https://garcondatabase.firebaseio.com/users");
        FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();

        if(userID!=null)
        {

            getDataFromServer();

        }

        return view;
    }


    //-----------------retriving User information------------------------------//
    private void getDataFromServer() {

        myRef.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    User user = dataSnapshot.getValue(User.class);
                    if (user != null) {

                        etFirstName.setText(user.getFirstName());
                        etLastName.setText(user.getLastName());
                        etEmail.setText(user.geteMail());
                        etContact.setText(user.getPhoneNumber());
                        etPassword.setText(user.getUserUID());


                        Toast.makeText(getContext(),"User exists", Toast.LENGTH_LONG).show();


                    }
                    //Toast.makeText(getContext(),"DataSnapshot", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    //-------------------update user information-----------------//
    private void saveUserDetails() {
        String firstName = etFirstName.getText().toString().trim();
        String lastName = etLastName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String contact = etContact.getText().toString().trim();
        String Password = etPassword.getText().toString().trim();

        String UserID = mAuth.getCurrentUser().getUid();

        // Check for a valid password, if the user entered one.

        if ( (firstName.length()==0) ) {
            etFirstName.setError("Name required");
            focusView = etFirstName;
            cancel = true;
        }

        if ( (lastName.length()==0) ) {
            etLastName.setError("Name required");
            focusView = etLastName;
            cancel = true;
        }



        if (!isPhoneNumberValid(contact)) {
            etContact.setError(getString(R.string.error_incorrect_phonenumber));
            focusView = etContact;
            cancel = true;
        }
        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            etEmail.setError(getString(R.string.error_field_required));
            focusView = etEmail;
            cancel = true;
        } else if (!isEmailValid(email)) {
            etEmail.setError(getString(R.string.error_invalid_email));
            focusView = etEmail;
            cancel = true;
        }else {

            // create user object and set all the properties
            User user = new User();
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.seteMail(email);
            user.setPhoneNumber(contact);


            if (mAuth.getCurrentUser() != null) {
                myRef.child(mAuth.getCurrentUser().getUid()).child("firstName").setValue(firstName);
                myRef.child(mAuth.getCurrentUser().getUid()).child("lastName").setValue(lastName);
                myRef.child(mAuth.getCurrentUser().getUid()).child("eMail").setValue(email);
                myRef.child(mAuth.getCurrentUser().getUid()).child("phoneNumber").setValue(contact);
                myRef.child(mAuth.getCurrentUser().getUid()).child("userUID").setValue(user.getUserUID());

                Toast.makeText(getContext(), "Data is saved successfully",
                        Toast.LENGTH_SHORT).show();


            }
        }

    }

    //-------------validation checking sof----------------------------//
    private boolean isEmailValid(String email) {

        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {

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

    //-------------validation checking eof----------------------------//


}
