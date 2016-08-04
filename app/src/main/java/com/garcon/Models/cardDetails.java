package com.garcon.Models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kritikagopalakrishnan on 7/7/16.
 */
@IgnoreExtraProperties
public class cardDetails {
        private String cardID;
        private String cardHolder;
        private String cardNumber;
        private String cardType;
        private String cvv;
        private String expiry;
        private Map<String, Boolean> stars = new HashMap<>();

        public cardDetails() {
            // Default constructor required for calls to DataSnapshot.getValue(Post.class)
        }
    public void setCardID(String cardID) {
        this.cardID = cardID;
    }

    public String getCardID() {
        return cardID;
    }

    public void setCardHolder(String cardHolder) { this.cardHolder = cardHolder; }

    public String getCardHolder() {
        return cardHolder;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getCardType() {
        return cardType;
    }

    public void setcvv(String cvv) {
        this.cvv = cvv;
    }

    public String getcvv() {
        return cvv;
    }

    public void setExpiry(String expiry) {
        this.expiry = expiry;
    }

    public String getExpiry() {
        return expiry;
    }

//    public cardDetails(String cardHolder, double cardNumber, String cardType, int cvv, String expiry, String cardID) {
//            this.cardHolder = cardHolder;
//            this.cardNumber = cardNumber;
//            this.cardType = cardType;
//            this.cvv = cvv;
//            this.expiry = expiry;
//            this.cardID = cardID;
//        }

        @Exclude
        public Map<String, Object> toMap() {
            HashMap<String, Object> result = new HashMap<>();
            result.put("cardHolder", cardHolder);
            result.put("cardNumber", cardNumber);
            result.put("cardType", cardType);
            result.put("cvv", cvv);
            result.put("expiry:", expiry);
            result.put("cardID:",cardID);

            return result;
        }


}
