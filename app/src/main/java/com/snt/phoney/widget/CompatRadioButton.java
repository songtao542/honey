package com.snt.phoney.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;

import androidx.annotation.DrawableRes;
import androidx.annotation.IntDef;
import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;
import androidx.appcompat.widget.AppCompatTextView;

import com.snt.phoney.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static androidx.annotation.RestrictTo.Scope.LIBRARY_GROUP;

public class CompatRadioButton extends AppCompatTextView implements View.OnClickListener {

    /**
     * @hide
     */
    @RestrictTo(LIBRARY_GROUP)
    @IntDef({
            ICON_GRAVITY_LEFT,
            ICON_GRAVITY_RIGHT,
    })
    @Retention(RetentionPolicy.SOURCE)
    @interface IconGravity {
    }


    public static final int ICON_GRAVITY_LEFT = 0;
    public static final int ICON_GRAVITY_RIGHT = 1;

    private View.OnClickListener mClickListener;
    private OnCheckedChangeListener mCheckedChangeListener;

    private int iconResId;
    private int iconGravity;
    private int iconSize;

    public CompatRadioButton(Context context) {
        super(context);
        init(context, null, 0);
    }

    public CompatRadioButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public CompatRadioButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {

        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CompatRadioButton, defStyleAttr, 0);

            iconResId = a.getResourceId(R.styleable.CompatRadioButton_icon, ICON_GRAVITY_RIGHT);
            iconGravity = a.getInt(R.styleable.CompatRadioButton_icon_gravity, 1);
            iconSize = a.getDimensionPixelSize(R.styleable.CompatRadioButton_iconSize, 0);
            boolean checked = a.getBoolean(R.styleable.CompatRadioButton_checked, false);

            setIcon(iconResId);
            setChecked(checked);

            a.recycle();
        }
        setGravity(Gravity.CENTER_VERTICAL);
        super.setOnClickListener(this);
    }

    public void setIconGravity(@IconGravity int iconGravity) {
        this.iconGravity = iconGravity;
        setIcon(iconResId);
    }

    public void setIcon(@DrawableRes int resId) {
        if (resId != 0) {
            iconResId = resId;
            Drawable drawable = getContext().getDrawable(iconResId);
            int width = iconSize != 0 ? iconSize : drawable.getMinimumWidth();
            int height = iconSize != 0 ? iconSize : drawable.getMinimumHeight();
            drawable.setBounds(0, 0, width, height);
            if (iconGravity == 0) {
                setCompoundDrawables(drawable, null, null, null);
            } else {
                setCompoundDrawables(null, null, drawable, null);
            }
        }
    }

    @Override
    public void setOnClickListener(@Nullable View.OnClickListener l) {
        mClickListener = l;
    }

    @Override
    public void onClick(View v) {
        setChecked(!isSelected());
        if (mClickListener != null) {
            mClickListener.onClick(this);
        }
        if (mCheckedChangeListener != null) {
            mCheckedChangeListener.onCheckedChanged(this, getChecked());
        }
    }


    public void setChecked(boolean checked) {
        this.setSelected(checked);
    }

    public boolean getChecked() {
        return isSelected();
    }

    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        mCheckedChangeListener = listener;
    }


    public interface OnCheckedChangeListener {
        /**
         * Called when the checked state of a compound button has changed.
         *
         * @param view      The compound button view whose state has changed.
         * @param isChecked The new checked state of buttonView.
         */
        void onCheckedChanged(CompatRadioButton view, boolean isChecked);
    }
}
