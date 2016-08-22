package com.garcon.garcon;
import com.garcon.garcon.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainReviewActivity extends Activity {

    private final static String LOG_TAG = MainReviewActivity.class.getSimpleName();
    private RelativeLayout payment_information;
    private int subtotal;
    private int tip;
    private int total;

    private boolean tip_selected = false;
    private View tip_view;

    private Timer timer = new Timer();
    private final long DELAY = 100;

    private boolean percentage_entered = false;
    private boolean dollar_entered = false;

    @Override
    protected void onCreate(Bundle savedInstance){

        Intent intent = getIntent();
        String instructions = intent.getStringExtra("instructions"); //comes from MainCartActivity
        String table_number = intent.getStringExtra("table");
        String table_area = intent.getStringExtra("area");

        super.onCreate(savedInstance);
        ArrayList<MenuItem> ordered_items = OrderSingleton.getInstance().getList();

        /*
        for(int i = 0; i < 10; i++)
            OrderSingleton.getInstance().addItem(new MenuItem("item "+String.valueOf(i)),0,"");
        */

        setContentView(R.layout.activity_main_review);

        TextView tvInstructions = (TextView) findViewById(R.id.specialInstructions);
        tvInstructions.setText(instructions);
        TextView tvTableNumber = (TextView) findViewById(R.id.tableNumber);
        tvTableNumber.setText(table_number);
        TextView tvTableArea = (TextView) findViewById(R.id.restaurantArea);
        tvTableArea.setText(table_area);

        BaseAdapter itemAdapter = new ReviewRowItemAdapter((ArrayList<MenuItem>) ordered_items, this);
        NonScrollableListView itemListView = (NonScrollableListView) findViewById(R.id.itemsList);
        itemListView.setAdapter(itemAdapter);

        payment_information = (RelativeLayout) findViewById(R.id.payment_information);
        payment_information.setVisibility(View.GONE);
        RadioGroup choosePayment = (RadioGroup) findViewById(R.id.paymentOption);
        choosePayment.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.payStore && payment_information.isShown()){
                    toggle_contents(payment_information);
                }
                else if(checkedId == R.id.payApp){
                    toggle_contents(payment_information);
                }
            }
        });

        ArrayList<String> cards = new ArrayList<String>();
        //TODO intent should pick up stored cards
        cards.add("AMERICAN EXPRESS");
        cards.add("DISCOVER");
        BaseAdapter cardAdapter = new OptionsAdapter(cards,this);
        NonScrollableListView cardsLV = (NonScrollableListView) findViewById(R.id.credit_cards);
        cardsLV.setAdapter(cardAdapter);
        cards.add("ADD NEW CARD");

        final TextView total_display = (TextView) findViewById(R.id.total_amount);

        final TextView subtotal_display = (TextView) findViewById(R.id.subtotal_amount);
        subtotal = 0;

        for(MenuItem m : OrderSingleton.getInstance().getList()){
            int base = m.getPrice();
            for(MenuItem.ModifierGroup mgrp : m.getModifierGroups()){
                for(MenuItem.ModifierGroup.ItemModifier im : mgrp.getModifierList()){
                    if(im.isAdded()){
                        base+=im.getPricePerUnit();
                    }
                }
            }
            subtotal += base*m.getNumOrdered();
        }
        subtotal_display.setText(String.valueOf(subtotal));

        final TextView tip_display = (TextView) findViewById(R.id.tip_amount);
        tip = 0;
        tip_display.setText(String.valueOf(tip));

        String str0 = ((CheckedTextView) findViewById(R.id.a)).getText().toString();
        str0 = str0.replaceAll("[^\\d]", "");
        final int tip0 = Integer.valueOf(str0)*subtotal/100;
        ((TextView) findViewById(R.id.aa)).setText(String.valueOf(tip0));

        ((CheckedTextView) findViewById(R.id.a)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v) {
                CheckedTextView view = (CheckedTextView) v;
                tipOptionSelected(view);
                if(view.isChecked()){
                    tip = tip0;
                    ((TextView) findViewById(R.id.tip_amount)).setText(String.valueOf(tip));
                }
                else{
                    tip = 0;
                    ((TextView) findViewById(R.id.tip_amount)).setText(String.valueOf(tip));
                }
                total = subtotal + tip;
                total_display.setText(String.valueOf(total));

            }

        });

        String str1 = ((CheckedTextView) findViewById(R.id.b)).getText().toString();
        str1 = str1.replaceAll("[^\\d]", "");
        final int tip1 = Integer.valueOf(str1)*subtotal/100;
        ((TextView) findViewById(R.id.bb)).setText(String.valueOf(tip1));

        ((CheckedTextView) findViewById(R.id.b)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v) {
                CheckedTextView view = (CheckedTextView) v;
                tipOptionSelected(view);
                if(view.isChecked()){
                    tip = tip1;
                    ((TextView) findViewById(R.id.tip_amount)).setText(String.valueOf(tip));
                }
                else{
                    tip = 0;
                    ((TextView) findViewById(R.id.tip_amount)).setText(String.valueOf(tip));
                }
                total = subtotal + tip;
                total_display.setText(String.valueOf(total));

            }

        });

        CheckedEditText percentageEditText = (CheckedEditText) findViewById(R.id.c);
        percentageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(final CharSequence s, int start, int before, int count) {
                if(timer != null)
                    timer.cancel();
            }
            @Override
            public void afterTextChanged(final Editable s) {
                //avoid triggering event when text is too short

                if (percentage_entered){

                    timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            // TODO: do what you need here (refresh list)
                            // you will probably need to use runOnUiThread(Runnable action) for some specific actions

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    String s = ((CheckedEditText) findViewById(R.id.c)).getText().toString();
                                    s = s.replaceAll("[^\\d]", "");
                                    if (s.length() > 0) {
                                        tip = Integer.valueOf(s) * subtotal / 100;
                                        ((TextView) findViewById(R.id.cc)).setText(String.valueOf(tip));
                                        ((TextView) findViewById(R.id.tip_amount)).setText(String.valueOf(tip));
                                    } else {
                                        tip = 0;
                                        ((TextView) findViewById(R.id.cc)).setText(String.valueOf(tip));
                                        ((TextView) findViewById(R.id.tip_amount)).setText(String.valueOf(tip));
                                    }
                                    total = subtotal + tip;
                                    total_display.setText(String.valueOf(total));

                                }
                            });
                        }
                    }, DELAY);
                }
            }
        });


        findViewById(R.id.c).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v) {
                CheckedEditText view = (CheckedEditText) v;
                tipOptionSelected(view);
                if(view.isChecked()){
                    percentage_entered = true;
                    if(((CheckedEditText) findViewById(R.id.c)).getText().length()>0){
                        String s = ((CheckedEditText) findViewById(R.id.c)).getText().toString();
                        s = s.replaceAll("[^\\d]", "");
                        tip = Integer.valueOf(s) * subtotal / 100;
                        ((TextView) findViewById(R.id.cc)).setText(String.valueOf(tip));
                        ((TextView) findViewById(R.id.tip_amount)).setText(String.valueOf(tip));
                        }
                }
                else{
                    percentage_entered = false;
                    tip = 0;
                    ((CheckedEditText) findViewById(R.id.c)).setText("");
                    ((TextView) findViewById(R.id.cc)).setText(String.valueOf(tip));
                    ((TextView) findViewById(R.id.tip_amount)).setText(String.valueOf(tip));
                }
                total = subtotal + tip;
                total_display.setText(String.valueOf(total));

            }

        });

        final CheckedEditText dollarEditText = (CheckedEditText) findViewById(R.id.d);
        dollarEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(final CharSequence s, int start, int before, int count) {
                if(timer != null)
                    timer.cancel();
            }
            @Override
            public void afterTextChanged(final Editable s) {

                if (dollar_entered) {

                    timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    String s = dollarEditText.getText().toString();
                                    s = s.replaceAll("[^\\d.]", "");
                                    if (s.length() > 0) {
                                        tip = Integer.valueOf(s);
                                        ((TextView) findViewById(R.id.tip_amount)).setText(String.valueOf(tip));
                                        total = subtotal + tip;
                                        total_display.setText(String.valueOf(total));
                                    }
                                }
                            });
                        }
                    }, DELAY);
                }
            }
        });

        findViewById(R.id.d).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v) {
                CheckedEditText view = (CheckedEditText) v;
                tipOptionSelected(view);
                if(view.isChecked()){
                    dollar_entered = true;
                    if(((CheckedEditText) findViewById(R.id.c)).getText().length()>0){
                        String s = ((CheckedEditText) findViewById(R.id.c)).getText().toString();
                        s = s.replaceAll("[^\\d]", "");
                        tip = Integer.valueOf(s);
                        ((TextView) findViewById(R.id.tip_amount)).setText(String.valueOf(tip));
                    }
                }
                else{
                    dollar_entered = false;
                    tip = 0;
                    ((CheckedEditText) findViewById(R.id.d)).setText("");
                    ((TextView) findViewById(R.id.tip_amount)).setText(String.valueOf(tip));
                }
                total = subtotal + tip;
                total_display.setText(String.valueOf(total));

            }

        });


    }

    public void toggle_contents(View view) {
        //no effects
        /*
        options.setVisibility(optionsListView.isShown()
                ? View.GONE
                : View.VISIBLE);
        */
        if (payment_information.isShown()) {
            UtilityFX.slide_up(this, payment_information);
            payment_information.setVisibility(View.GONE);
        } else {
            payment_information.setVisibility(View.VISIBLE);
            UtilityFX.slide_down(this, payment_information);
        }

    }

    public void see_checkout(View view){
        Intent i = new Intent(this, MainCheckoutActivity.class);
        i.putIntegerArrayListExtra("values", new ArrayList<Integer>(Arrays.asList(subtotal, tip, total)));
        i.putExtra("ticketID", ticketID);
        this.startActivity(i);
    }

    public void tipOptionSelected(View view){

        if(tip_selected){
            Log.i(LOG_TAG,"changing");
            if(view.equals(tip_view)){

                if(view instanceof CheckedTextView)
                    ((CheckedTextView)view).toggle();
                else if(view instanceof CheckedEditText)
                    ((CheckedEditText)view).toggle();

                tip_selected = false;
                tip = 0;
            }
            else{

                if(tip_view instanceof CheckedTextView)
                    ((CheckedTextView)tip_view).toggle();
                else if(tip_view instanceof CheckedEditText)
                    ((CheckedEditText)tip_view).toggle();

                if(view instanceof CheckedTextView)
                    ((CheckedTextView)view).toggle();
                else if(view instanceof CheckedEditText)
                    ((CheckedEditText)view).toggle();


                tip_view = view;
            }
        }
        else{

            if(view instanceof CheckedTextView){
                ((CheckedTextView)view).toggle();
            }
            else if(view instanceof CheckedEditText){
                ((CheckedEditText)view).toggle();

            }

            tip_selected = true;
            tip_view = view;
        }

    }

    public void addCard(View view){
        Log.i(LOG_TAG,"addCard called");
        //TODO integrate with adding a new card
    }

    public void cardSelected(View view){
        Log.i(LOG_TAG,((TextView)view).getText()+"card selected");
        //TODO integrate with selecting an existing card
    }

}
