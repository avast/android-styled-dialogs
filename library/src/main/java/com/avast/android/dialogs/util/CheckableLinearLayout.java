package com.avast.android.dialogs.util;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Checkable;
import android.widget.LinearLayout;

import java.util.HashSet;
import java.util.Set;

public class CheckableLinearLayout extends LinearLayout implements Checkable {
    private static final int[] CHECKED_STATE_SET = {android.R.attr.state_checked};

    private Set<Checkable> mCheckablesSet = new HashSet<>();
    private boolean mChecked;

    public CheckableLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        // find checkable items
        int childCount = getChildCount();
        for (int i = 0; i < childCount; ++i) {
            View v = getChildAt(i);
            if (v instanceof Checkable) {
                mCheckablesSet.add((Checkable) v);
            }
        }
    }

    @Override
    public boolean isChecked() {
        return mChecked;
    }

    @Override
    public void setChecked(boolean checked) {
        if (checked == this.mChecked) {
            return;
        }
        this.mChecked = checked;
        for (Checkable checkable : mCheckablesSet) {
            checkable.setChecked(checked);
        }
        refreshDrawableState();
    }

    @Override
    public void toggle() {
        setChecked(!mChecked);
    }


    public int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        if (isChecked()) {
            mergeDrawableStates(drawableState, CHECKED_STATE_SET);
        }
        return drawableState;
    }
}