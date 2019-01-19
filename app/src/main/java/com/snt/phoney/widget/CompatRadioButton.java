package com.snt.phoney.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.AttrRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;
import androidx.annotation.StyleRes;
import androidx.appcompat.widget.AppCompatRadioButton;

import com.snt.phoney.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static androidx.annotation.RestrictTo.Scope.LIBRARY_GROUP;

@SuppressWarnings("unused")
public class CompatRadioButton extends LinearLayout implements View.OnClickListener {

    @RestrictTo(LIBRARY_GROUP)
    @IntDef({
            RADIO_GRAVITY_LEFT,
            RADIO_GRAVITY_RIGHT,
    })
    @Retention(RetentionPolicy.SOURCE)
    @interface RadioGravity {
    }

    public static final int RADIO_GRAVITY_LEFT = 0;
    public static final int RADIO_GRAVITY_RIGHT = 1;

    public CompatRadioButton(@NonNull Context context) {
        super(context);
        init(context, null, 0);
    }

    public CompatRadioButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public CompatRadioButton(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    public CompatRadioButton(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr);
    }

    private int iconResId;
    private int iconSize;
    private int iconPadding;
    private int radioResId;
    private int radioGravity = RADIO_GRAVITY_RIGHT;
    private int radioSize;
    private int textSize;
    private int textColor;
    private CharSequence text;
    private boolean checked = false;
    /**
     * groupMode == true 时，点击自身不能取消选中状态，只有通过 setChecked() 来更新选中状态
     */
    private boolean groupMode = false;

    private TextView textView;
    private ImageView imageView;
    private AppCompatRadioButton radioButton;

    private View.OnClickListener mClickListener;
    private OnCheckedChangeListener mCheckedChangeListener;

    private void init(@NonNull Context context, AttributeSet attrs, int defStyleAttr) {
        setOrientation(LinearLayout.HORIZONTAL);

        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CompatRadioButton, defStyleAttr, 0);
            radioResId = a.getResourceId(R.styleable.CompatRadioButton_radio, 0);
            radioGravity = a.getInt(R.styleable.CompatRadioButton_radioGravity, RADIO_GRAVITY_RIGHT);
            radioSize = a.getDimensionPixelSize(R.styleable.CompatRadioButton_radioSize, 0);
            textSize = a.getDimensionPixelSize(R.styleable.CompatRadioButton_radioTextSize, 0);
            textColor = a.getColor(R.styleable.CompatRadioButton_radioTextColor, -1);
            text = a.getString(R.styleable.CompatRadioButton_radioText);
            checked = a.getBoolean(R.styleable.CompatRadioButton_checked, false);
            groupMode = a.getBoolean(R.styleable.CompatRadioButton_groupMode, false);

            iconResId = a.getResourceId(R.styleable.CompatRadioButton_icon, 0);
            iconSize = a.getDimensionPixelSize(R.styleable.CompatRadioButton_iconSize, 0);
            iconPadding = a.getDimensionPixelSize(R.styleable.CompatRadioButton_iconPadding, 0);

            a.recycle();
        }

        initView();
        super.setOnClickListener(this);
    }

    private void initView() {
        if (radioResId != 0) {
            Drawable drawable = getContext().getDrawable(radioResId);
            int width = radioSize != 0 ? radioSize : drawable.getMinimumWidth();
            int height = radioSize != 0 ? radioSize : drawable.getMinimumHeight();
            drawable.setBounds(0, 0, width, height);

            if (imageView == null) {
                imageView = new ImageView(getContext());
                LayoutParams iconLayoutParam = new LayoutParams(width, height);
                iconLayoutParam.gravity = Gravity.CENTER_VERTICAL;
                imageView.setLayoutParams(iconLayoutParam);
                imageView.setImageDrawable(drawable);
            }
        }

        if (imageView != null) {
            imageView.setSelected(checked);
        } else {
            if (radioButton == null) {
                radioButton = new AppCompatRadioButton(getContext());
                LayoutParams radioLayoutParam = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                radioLayoutParam.gravity = Gravity.CENTER_VERTICAL;
                radioButton.setLayoutParams(radioLayoutParam);
                radioButton.setClickable(false);
            }

            radioButton.setChecked(checked);
        }

        if (text != null) {
            if (textView == null) {
                textView = new TextView(getContext());
                LayoutParams textLayoutParam = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                textLayoutParam.gravity = Gravity.CENTER_VERTICAL;
                textLayoutParam.weight = 1;
                textView.setLayoutParams(textLayoutParam);
            }

            if (iconResId != 0) {
                Drawable icon = getContext().getDrawable(iconResId);
                int width = iconSize != 0 ? iconSize : icon.getMinimumWidth();
                int height = iconSize != 0 ? iconSize : icon.getMinimumHeight();
                icon.setBounds(0, 0, width, height);
                textView.setCompoundDrawablePadding(iconPadding);
                if (radioGravity == RADIO_GRAVITY_LEFT) {
                    textView.setCompoundDrawables(null, null, icon, null);
                } else {
                    textView.setCompoundDrawables(icon, null, null, null);
                }
            }

            textView.setSelected(checked);
            textView.setText(text);
            if (textColor != -1) {
                textView.setTextColor(textColor);
            }
            if (textSize != 0) {
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            }
        }

        if (radioGravity == RADIO_GRAVITY_LEFT) {
            removeAllViews();
            if (imageView != null) {
                addView(imageView);
            } else if (radioButton != null) {
                addView(radioButton);
            }
            if (textView != null) {
                addView(textView);
                textView.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
            }
        } else {
            removeAllViews();
            if (textView != null) {
                addView(textView);
                textView.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
            }
            if (imageView != null) {
                addView(imageView);
            } else if (radioButton != null) {
                addView(radioButton);
            }
        }
    }


    public void setRadioGravity(@RadioGravity int radioGravity) {
        this.radioGravity = radioGravity;
        initView();
    }

    /**
     * 含 radioText 时才生效
     *
     * @param resId
     */
    public void setIconImageResource(int resId) {
        this.iconResId = resId;
        initView();
    }

    /**
     * 含 radioText 时才生效
     *
     * @param iconSize
     */
    public void setIconSize(int iconSize) {
        this.iconSize = iconSize;
        initView();
    }

    /**
     * @param resId Selector drawable resource id
     */
    public void setRadioImageResource(@DrawableRes int resId) {
        radioResId = resId;
        initView();
    }

    public void setRadioSize(int radioSize) {
        this.radioSize = radioSize;
        initView();
    }

    public void setText(CharSequence text) {
        this.text = text;
        initView();
    }

    @Override
    public void setOnClickListener(@Nullable View.OnClickListener l) {
        mClickListener = l;
    }

    @Override
    public void onClick(View v) {
        boolean checked = isChecked();
        if (!checked || !groupMode) {
            setChecked(!isChecked());
        }
        if (mClickListener != null) {
            mClickListener.onClick(this);
        }
        if (mCheckedChangeListener != null) {
            mCheckedChangeListener.onCheckedChanged(this, isChecked());
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (radioButton != null) {
            MotionEvent event = MotionEvent.obtain(ev);
            event.setLocation(radioButton.getWidth() / 2, radioButton.getHeight() / 2);
            radioButton.onTouchEvent(event);
        }
        return super.onInterceptTouchEvent(ev);
    }

    public void setChecked(boolean checked) {
        if (imageView != null) {
            imageView.setSelected(checked);
        } else {
            radioButton.setChecked(checked);
        }
        if (textView != null) {
            textView.setSelected(checked);
        }
    }

    public boolean isChecked() {
        return imageView != null ? imageView.isSelected() : radioButton.isChecked();
    }

    /**
     * groupMode == true 时，点击自身不能取消选中状态，只有通过 setChecked() 来更新选中状态
     */
    public void setGroupMode(boolean groupMode) {
        this.groupMode = groupMode;
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
