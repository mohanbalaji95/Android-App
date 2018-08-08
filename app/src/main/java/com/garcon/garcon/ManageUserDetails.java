package com.garcon.garcon;

import android.app.AlertDialog;
import android.content.DialogInterface;
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


        btSave = (Button) view.findViewById(R.id.btSaveDetails);
        btCancel = (Button) view.findViewById(R.id.btCancelDetails);

        //-------------------signup button visibility set hidden-----------------------//
        btSignUp = (Button) view.findViewById(R.id.buttonSave);
        btSignUp.setVisibility(View.GONE);
        etPassword = (EditText) view.findViewById(R.id.signup_password);
        etPassword.setVisibility(View.GONE);
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
        //userID = user.getUid();

        if( FirebaseAuth.getInstance().getCurrentUser()==null)
        {


            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("If you are a guest, please create a account to see your details.")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent myIntent = new Intent(getContext(), SignUpActivity.class);
                            startActivity(myIntent);

                        }
                    }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // do nothing
                }
            }).show();

        }
        else{

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
                        //etPassword.setText(user.getUserUID());

                    }

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
        //String Password = etPassword.getText().toString().trim();

        String UserID = mAuth.getCurrentUser().getUid();

        // Check for a valid password, if the user entered one.

        if (TextUtils.isEmpty(firstName)) {
            etFirstName.setError("First Name required");
            etFirstName.requestFocus();
        }else

        if (TextUtils.isEmpty(lastName)) {
            etLastName.setError("Last Name required");
            etLastName.requestFocus();
        }else


        if (!isPhoneNumberValid(contact)||TextUtils.isEmpty(contact)) {
            etContact.setError(getString(R.string.error_incorrect_phonenumber));
            etContact.requestFocus();
        }else

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Invalid Email ID");
            etEmail.requestFocus();
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
                // myRef.child(mAuth.getCurrentUser().getUid()).child("userUID").setValue(user.getUserUID());

                Toast.makeText(getContext(), "Data is saved successfully",
                        Toast.LENGTH_SHORT).show();


            } else {
                Toast.makeText(getContext(), "Data is saved unsuccessfully",
                        Toast.LENGTH_SHORT).show();
            }
        }

    }

    //-------------validation checking sof----------------------------//


    private boolean isPhoneNumberValid(String contact) {
        Log.d(TAG, "phone Number entered --> " + contact);

        if (contact.length() == 10)
            return true;
        else
            return false;
    }

    //-------------validation checking eof----------------------------//


}
