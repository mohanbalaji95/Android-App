package com.garcon.Models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kritikagopalakrishnan on 8/27/16.
 */
@IgnoreExtraProperties
public class Receipt {

    private String subTotal;
    private String Tip;
    private String Total;
    private String dateAndTime;
    private String cardTypeUsed;
    private String cardNumberUsed;
    private String cardHolderUsed;

    private Map<String, Boolean> stars = new HashMap<>();

    public Receipt() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }
    public void setsubTotal(String subTotal) {
        this.subTotal = subTotal;
    }

    public String getsubTotal() {
        return subTotal;
    }

    public void setTip(String Tip) { this.Tip = Tip; }

    public String getTip() {
        return Tip;
    }

    public void setTotal(String Total) {
        this.Total = Total;
    }

    public String getTotal() {
        return Total;
    }

    public void setdateAndTime(String dateAndTime) {
        this.dateAndTime = dateAndTime;
    }

    public String getdateAndTime() {
        return dateAndTime;
    }

    public void setcardTypeUsed(String cardTypeUsed) {
        this.cardTypeUsed = cardTypeUsed;
    }

    public String getcardTypeUsed() {
        return cardTypeUsed;
    }

    public void setcardNumberUsed(String cardNumberUsed) { this.cardNumberUsed = cardNumberUsed; }

    public String getcardNumberUsed() {
        return cardNumberUsed;
    }

    public void setcardHolderUsed(String cardHolderUsed) { this.cardHolderUsed = cardHolderUsed; }

    public String getcardHolderUsed() {
        return cardHolderUsed;
    }

    public Receipt(String subTotal, String Tip, String Total, String dateAndTime, String cardTypeUsed, String cardNumberUsed, String cardHolderUsed) {
        this.subTotal = subTotal;
        this.Tip = Tip;
        this.Total = Total;
        this.dateAndTime = dateAndTime;
        this.cardTypeUsed = cardTypeUsed;
        this.cardNumberUsed = cardNumberUsed;
        this.cardHolderUsed = cardHolderUsed;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("subTotal", subTotal);
        result.put("Tip", Tip);
        result.put("Total", Total);
        result.put("dateAndTime", dateAndTime);
        result.put("cardTypeUsed", cardTypeUsed);
        result.put("cardNumberUsed",cardNumberUsed);
        result.put("cardHolderUsed",cardHolderUsed);


        return result;
    }


}


