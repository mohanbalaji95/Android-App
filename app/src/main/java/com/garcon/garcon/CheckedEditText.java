package com.garcon.garcon;

import android.annotation.TargetApi;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.EditText;

/**
 * Created by raja on 7/28/2016.
 */
@TargetApi(21)
public class CheckedEditText extends EditText implements Checkable {

    private static final int[] CheckedStateSet = { android.R.attr.state_checked };
    private boolean mChecked = false;

    public CheckedEditText(Context context){
        super(context);
    }

    public CheckedEditText(Context context, AttributeSet attrs){
        super(context,attrs);
    }
    public CheckedEditText(Context context, AttributeSet attrs, int defStyleAttr){
        super(context,attrs,defStyleAttr);
    }
    public CheckedEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes){
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public boolean isChecked (){
        return mChecked;
    }

    public void setChecked (boolean checked){
        mChecked = checked;
        refreshDrawableState();
    }

    public void toggle (){
        mChecked = !mChecked;
        refreshDrawableState();
    }

    @Override
    protected int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        if (isChecked()) {
            mergeDrawableStates(drawableState, CheckedStateSet);
        }
        return drawableState;
    }

}
