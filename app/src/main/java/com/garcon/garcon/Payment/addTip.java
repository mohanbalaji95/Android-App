package com.garcon.garcon.Payment;

import android.view.View;
import android.widget.TextView;


public class addTip extends CheckoutandPay {

    static int tip;
    static String tip_s;
    static int tipIn;


    public static String tipCalc(View view, double percent, TextView subtotal) {
        tip = (int) (Double.parseDouble(subtotal.getText().toString()) * percent);
        tip_s = "" + String.valueOf(tip);
        return tip_s;
    }

    public static void tipCalc(View view, String cTip) {

        tip_s = "" + cTip;
        tipIn = Integer.parseInt(cTip);
        mTip.setText(tip_s);
    }
}
