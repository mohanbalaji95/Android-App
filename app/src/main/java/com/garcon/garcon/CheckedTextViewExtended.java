package com.garcon.garcon;

import android.annotation.TargetApi;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.CheckedTextView;

/**
 * Created by raja on 7/29/2016.
 * A custom designed TextView
 */

@TargetApi(21)
public class CheckedTextViewExtended extends CheckedTextView {

    private String groupID = "";
    //including in constructor would probably prevent hardcoding in xml res

    public CheckedTextViewExtended(Context context){
        super(context);
    }
    public CheckedTextViewExtended(Context context, AttributeSet attrs){
        super(context,attrs);
    }
    public CheckedTextViewExtended(Context context, AttributeSet attrs, int defStyleAttr){
        super(context, attrs, defStyleAttr);
    }
    public CheckedTextViewExtended(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes){
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public String getGroupID(){return this.groupID;}
    public void setGroupID(String id){this.groupID = id;}

}
