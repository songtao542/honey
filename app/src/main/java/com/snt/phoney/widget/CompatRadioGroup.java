package com.snt.phoney.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.IdRes;
import androidx.annotation.Nullable;

public class CompatRadioGroup extends LinearLayout implements CompatRadioButton.OnCheckedChangeListener {

    private OnCheckedChangeListener mOnCheckedChangeListener;

    private CompatRadioButton mChecked;
    private int mCheckedId = 0;

    public CompatRadioGroup(Context context) {
        super(context);
    }

    public CompatRadioGroup(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CompatRadioGroup(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CompatRadioGroup(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        if (child instanceof CompatRadioButton) {
            CompatRadioButton button = (CompatRadioButton) child;
            button.setGroupMode(true);
            if (button.isChecked()) {
                if (mChecked != null) {
                    mChecked.setChecked(false);
                }
                mChecked = button;
                mCheckedId = button.getId();
            }
            button.setOnCheckedChangeListener(this);
        }
    }

    @Override
    public void onCheckedChanged(CompatRadioButton view, boolean isChecked) {
        if (isChecked && mCheckedId != view.getId()) {
            if (mChecked != null) {
                mChecked.setChecked(false);
            }
            mChecked = view;
            mCheckedId = view.getId();
            if (mOnCheckedChangeListener != null) {
                mOnCheckedChangeListener.onCheckedChanged(this, mCheckedId);
            }
        }
    }

    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        mOnCheckedChangeListener = listener;
    }

    public int getCheckedRadioButtonId() {
        return mCheckedId;
    }

    public void check(@IdRes int id) {
        if (id != 0 && (id == mCheckedId)) {
            return;
        }
        if (id != 0) {
            if (mCheckedId != 0) {
                setCheckedStateForView(mCheckedId, false);
            }
            mChecked = setCheckedStateForView(id, true);
            mCheckedId = id;
            if (mOnCheckedChangeListener != null) {
                mOnCheckedChangeListener.onCheckedChanged(this, mCheckedId);
            }
        }
    }

    private CompatRadioButton setCheckedStateForView(int viewId, boolean checked) {
        View checkedView = findViewById(viewId);
        if (checkedView instanceof CompatRadioButton) {
            CompatRadioButton button = ((CompatRadioButton) checkedView);
            button.setChecked(checked);
            return button;
        }
        return null;
    }

    public interface OnCheckedChangeListener {
        void onCheckedChanged(CompatRadioGroup group, @IdRes int checkedId);
    }

}
