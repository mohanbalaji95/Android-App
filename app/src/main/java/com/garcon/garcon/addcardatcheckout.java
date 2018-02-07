package com.garcon.garcon;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.garcon.Constants.Constants;
import com.garcon.Models.cardDetails;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//When you checkout and pay, and want to add a card at that moment, this is what this activity does

/**
 * Activity to add cards at checkout. This code is no longer being used.
 * Cards storage would be moved to Spreedly
 *
 * 
 */
public class addcardatcheckout extends Activity {
    Button savenewcard;
    EditText addcardholder;
    EditText addcardnumber;
    EditText addexpiry;
    EditText addcvv;
    View focusView = null;
    Button canceladdnewcard;
    String decryptedcardno;
    static cardDetails card;
    public static final String MyPREFERENCES = "GarconPref";
    public static final String usercardno = "usercardnumberkey";
    public static final String[] cardID = {"1cardkey", "2cardkey" , "3cardkey", "4cardkey" , "5cardkey", "6cardkey" , "7cardkey", "8cardkey" , "9cardkey", "10cardkey"};
    public static final String[] CardNumber = {"1key", "2key" , "3key", "4key" , "5key", "6key" , "7key", "8key" , "9key", "10key"};
    public static final String[] CardHolder = {"1holderkey", "2holderkey" , "3holderkey", "4holderkey" , "5holderkey", "6holderkey" , "7holderkey", "8holderkey" , "9holderkey", "10holderkey"};
    public static final String CardAtCheckoutType = "cardtypekey";
    SharedPreferences sharedpreferences;
    static int usercardnumber;
    Spinner cardtype;
    String cardtypestring;
    static String cardUID;
    ArrayList<Integer> values;
    String ticketID=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addcardatcheckout);
        values = getIntent().getIntegerArrayListExtra("values");
        ticketID = getIntent().getStringExtra("ticketID");
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        savenewcard = (Button) findViewById(R.id.savenewcardatcheckout);
        cardtype = (Spinner) findViewById(R.id.cardtypeatcheckout);
        addcardholder = (EditText) findViewById(R.id.addnameoncardatcheckout);
        addcardnumber = (EditText) findViewById(R.id.addeditcardnumberatcheckout);
        addexpiry = (EditText) findViewById(R.id.addexpiryatcheckout);
        addcvv = (EditText) findViewById(R.id.addcvvatcheckout);
        canceladdnewcard = (Button) findViewById(R.id.canceladdnewcardatcheckout);
        cardtype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cardtypestring = parent.getItemAtPosition(position).toString();

            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        savenewcard.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Boolean cancel = false;
                if (TextUtils.isEmpty(addcardnumber.getText().toString())) {
                    addcardnumber.setError("cardnumber cannot be empty");
                    focusView = addcardnumber;
                    cancel = true;
                    cancelling(cancel);
                } else if (!iscardnumbervalid(addcardnumber.getText().toString())) {
                    addcardnumber.setError("Enter a valid cardnumber");
                    focusView = addcardnumber;
                    cancel = true;
                    cancelling(cancel);
                } else if (TextUtils.isEmpty(addcardholder.getText().toString())) {
                    addcardholder.setError("card holder cannot be empty");
                    focusView = addcardholder;
                    cancel = true;
                    cancelling(cancel);
                } else if (TextUtils.isEmpty(addcvv.getText().toString())) {
                    addcvv.setError("cvv holder cannot be empty");
                    focusView = addcvv;
                    cancel = true;
                    cancelling(cancel);
                } else if (!iscvvvalid(addcvv.getText().toString())) {
                    addcvv.setError("Enter valid cvv");
                    focusView = addcvv;
                    cancel = true;
                    cancelling(cancel);
                } else if (TextUtils.isEmpty(addexpiry.getText().toString())) {
                    addexpiry.setError("expiry holder cannot be empty");
                    focusView = addexpiry;
                    cancel = true;
                    cancelling(cancel);
                } else if (!isexpiryvalid(addexpiry.getText().toString())) {
                    addexpiry.setError("Expiry format 01-17");
                    focusView = addexpiry;
                    cancel = true;
                    cancelling(cancel);
                } else {
//                    addcardholder.setVisibility(View.INVISIBLE);
//                    addcardnumber.setVisibility(View.INVISIBLE);
//                    addcvv.setVisibility(View.INVISIBLE);
//                    addexpiry.setVisibility(View.INVISIBLE);
//                    savenewcard.setVisibility(View.INVISIBLE);
//                    canceladdnewcard.setVisibility(View.INVISIBLE);
//                    expiryformat.setVisibility(View.INVISIBLE);
//                    exactwarning.setVisibility(View.INVISIBLE);
//                    addcard.setVisibility(View.VISIBLE);
//                    nameoncard.setVisibility(View.INVISIBLE);
//                    expiry.setVisibility(View.INVISIBLE);
//                    numberoncard.setVisibility(View.INVISIBLE);
//                    cvv.setVisibility(View.INVISIBLE);
//                    cvvdefinition.setVisibility(View.INVISIBLE);
//                    addcard.setVisibility(View.VISIBLE);
////                    savecarddetails.setEnabled(true);
//                    cardtype.setVisibility(View.INVISIBLE);
                    final EditText edittext = new EditText(getApplicationContext());
                    edittext.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    edittext.setTextColor(Color.BLACK);
                    final FirebaseAuth mAuth = FirebaseAuth.getInstance();
                    card = new cardDetails();
//                    builder.setTitle("Enter Password");
//                    builder.setMessage("Enter password for "+ mAuth.getCurrentUser().getEmail());
//                    builder.setView(edittext);
//                    builder.setNegativeButton("Cancel",
//                            new DialogInterface.OnClickListener()
//                            {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which)
//                                {
//                                }
//                            });
//                    builder.setPositiveButton("Save",
//                            new DialogInterface.OnClickListener()
//                            {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which)
//                                {
//                                    //Do nothing here because we override this button later to change the close behaviour.
//                                    //However, we still need this because on older versions of Android unless we
//                                    //pass a handler the button doesn't get instantiated
//                                }
//                            });
//                    final AlertDialog dialog = builder.create();
//                    dialog.show();
//                    newcard[usercardnumber] = mInflater.inflate(R.layout.newcard, container, false);
//                    remove[usercardnumber] = (CheckBox) newcard[usercardnumber].findViewById(R.id.remove);
//                    cardholder[usercardnumber] = (TextView) newcard[usercardnumber].findViewById(R.id.cardholder);
//                    cardnumber[usercardnumber] = (TextView) newcard[usercardnumber].findViewById(R.id.cardnumber);
//                    editcardholder[usercardnumber] = (EditText) newcard[usercardnumber].findViewById(R.id.editablecardholder);
////                editcardholder[usercardnumber].setText(card.getCardHolder());
//                    editcardnumber[usercardnumber] = (EditText) newcard[usercardnumber].findViewById(R.id.editablecardnumber);
////                editcardnumber[usercardnumber].setText(card.getCardNumber().toString());
//                    remove[usercardnumber].setVisibility(View.INVISIBLE);
//
//
//                    editcardholder[usercardnumber].setEnabled(false);
//                    editcardnumber[usercardnumber].setEnabled(false);
//Overriding the handler immediately after show is probably a better approach than OnShowListener as described below
//                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
//                    {
//                        @Override
//                        public void onClick(View v) {
//                            final Boolean[] wantToCloseDialog = {false};
//                            //Do stuff, possibly set wantToCloseDialog to true then...
//                            mAuth.signInWithEmailAndPassword(mAuth.getCurrentUser().getEmail(), edittext.getText().toString())
//                                    .addOnCompleteListener(ProfileSettings.this, new OnCompleteListener<AuthResult>() {
//                                        boolean cancel = false;
//                                        View focusView = null;
//                                        String tempcardnumholder = addcardnumber.getText().toString();
//                                        String tempcardHolder = addcardholder.getText().toString();
//                                        @Override
//                                        public void onComplete(@NonNull Task<AuthResult> task) {
//                                            System.out.println("signInWithEmail:onComplete:" + task.isSuccessful());
//
//                                            // If sign in fails, display a message to the user. If sign in succeeds
//                                            // the auth state listener will be notified and logic to handle the
//                                            // signed in user can be handled in the listener.
//                                            if (!task.isSuccessful()) {
//                                                System.out.println("signInWithEmail" + task.getException());
//                                                Toast.makeText(ProfileSettings.this, "Authentication failed.",
//                                                        Toast.LENGTH_SHORT).show();
//                                                edittext.setError("invalid password");
//                                                focusView = edittext;
//                                                cancel = true;
//
//                                                //System.out.println("Error Code:"+firebaseError.getCode());
//                                                //System.out.println("Error:"+ firebaseError.getMessage());
//                                                //updateErrorCode(firebaseError.getCode());
//                                            } else {
//                                                //System.out.println("User ID: " + authData.getUid() + ", Provider: " + authData.getProvider());
//                                                System.out.println("Successful login");
//
//
//                                            }
//
//                                            if (cancel) {
//                                                // There was an error; don't attempt login and focus the first
//                                                // form field with an error.
//                                                focusView.requestFocus();
//                                            } else {
//                                                dialog.dismiss();
                    String SEED = mAuth.getCurrentUser().getUid();

                    try {
                        card.setCardNumber(encrypt.encryption(SEED, addcardnumber.getText().toString()));
//                                System.out.println("encrypted value"+card.getCardNumber());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
//                            try {
//                                String decMsg = encrypt.decrypt(SEED, card.getCardNumber());
//                                System.out.println("decrypted value"+decMsg);
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
                    try {
                        card.setcvv(encrypt.encryption(SEED, addcvv.getText().toString()));
//                                System.out.println("encrypted value"+card.getCardNumber());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    card.setCardHolder(addcardholder.getText().toString());
                    final DatabaseReference ref = FirebaseDatabase.getInstance()
                            .getReferenceFromUrl(Constants.FIREBASE_URL_USERS);
                    String key = ref.push().getKey();
                    card.setCardID(key);
                    cardUID = card.getCardID();
                    usercardnumber = sharedpreferences.getInt(usercardno, 0);
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString(cardID[usercardnumber], cardUID);
                    editor.apply();
                    System.out.println("cardID in savenewcard " + sharedpreferences.getString(cardID[usercardnumber], null));
//                    System.out.println("cardid" + cardUID[usercardnumber]);
                    card.setCardType(cardtypestring);
                    card.setExpiry(addexpiry.getText().toString());
                    decryptedcardno = "*************" + addcardnumber.getText().toString().substring(addcardnumber.getText().toString().length() - 4);
                    editor.putString(CardNumber[usercardnumber], "*************" + addcardnumber.getText().toString().substring(addcardnumber.getText().toString().length() - 4));
                    editor.putString(CardHolder[usercardnumber], card.getCardHolder());
                    editor.putString(CardAtCheckoutType, cardtypestring);
                    editor.apply();
                    Intent addnewcard = new Intent(getApplicationContext(), com.garcon.garcon.addnewcardatcheckout.class);
                    startService(addnewcard);
                    Intent i = new Intent(addcardatcheckout.this,MainCheckoutActivity.class);
//                    i.putExtra("MainDate", cardtypestring+decryptedcardno+sharedpreferences.getString(CardHolder[usercardnumber], null));
                    i.putIntegerArrayListExtra("values", values);
                    i.putExtra("ticketID", ticketID);
                    usercardnumber++;
                    System.out.println("usercardnumber in savenewcard" + usercardnumber);
                    editor.putInt(usercardno, usercardnumber);
                    editor.apply();
                    System.out.println("usercardnumber in savenewcard" + sharedpreferences.getInt(usercardno, 0));

                    addcardholder.setText(null);
                    addcardnumber.setText(null);
                    addexpiry.setText(null);
                    addcvv.setText(null);

                    startActivity(i);                }
            }
//                                    });
//                            if(wantToCloseDialog[0])
//                            {
//                                System.out.println("in want to close dialog");
//                                dialog.dismiss();
//                            }
            //else dialog stays open. Make sure you have an obvious way to close the dialog especially if you set cancellable to false.

        });

        canceladdnewcard.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    public void cancelling(Boolean cancel)
    {
        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        }
    }

    public Boolean iscardnumbervalid(String c )
    {
        int sum = 0;
        boolean alternate = false;
        for (int i = c.length() - 1; i >= 0; i--)
        {
            int n = Integer.parseInt(c.substring(i, i + 1));
            if (alternate)
            {
                n *= 2;
                if (n > 9)
                {
                    n = (n % 10) + 1;
                }
            }
            sum += n;
            alternate = !alternate;
        }
        return (sum % 10 == 0);
    }

    public Boolean isexpiryvalid(String expiry)
    {
        Pattern phonePattern = Pattern.compile("[0-1]{1}[1-9]{1}-[0-9]{2}");
        Matcher phoneMatcher = phonePattern.matcher(expiry);
        return phoneMatcher.matches();

    }
    public Boolean iscvvvalid(String expiry)
    {
        Pattern phonePattern = Pattern.compile("[0-9]{3}");
        Matcher phoneMatcher = phonePattern.matcher(expiry);
        return phoneMatcher.matches();

    }
}
