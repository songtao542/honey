package com.snt.phoney.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.widget.AppCompatEditText;

import com.snt.phoney.R;

import java.util.ArrayList;
import java.util.List;

public class PasswordInputView extends AppCompatEditText {

    private Paint boxPaint;
    private Paint textPaint;
    private Rect textRect;
    private String text = "";
    private List<RectF> list = new ArrayList<>();
    private boolean isFocus = false;
    /**
     * 是否密文显示
     */
    private boolean isPassword;
    /**
     * 是否密文显示
     */
    private boolean isBackgroundSolid;
    /**
     * 横向间距
     */
    private int widthSpace;
    /**
     * 纵向间距
     */
    private int heightSpace;
    /**
     * 密码框的宽度
     */
    private int boxStrokeWidth;
    /**
     * 字体大小
     */
    private int textSize;
    /**
     * 边框圆角
     */
    private int boxRadius;
    /**
     * 密码长度
     */
    private int length = 4;

    /**
     * 字体颜色
     */
    private int textColor;

    /**
     * 默认框框颜色
     */
    private int boxNormalColor;
    /**
     * 选中框框颜色
     */
    private int boxSelectColor;

    /**
     * 密码显示方式
     */
    private PasswordShape passwordShape;

    /**
     * 是否需要在输入完成后关闭键盘
     */
    private boolean isAutoCloseKeyBoard = true;

    public enum PasswordShape {
        CIRCLE,
        ASTERISK
    }

    public void setPasswordShape(PasswordShape passwordShape) {
        this.passwordShape = passwordShape;
    }

    private int passwordTypeCircleRadius;

    private onInputOverListener onInputOverListener;

    public void setOnInputOverListener(PasswordInputView.onInputOverListener onInputOverListener) {
        this.onInputOverListener = onInputOverListener;
    }

    public PasswordInputView(Context context) {
        super(context);
        init(null, 0);
    }

    public PasswordInputView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public PasswordInputView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

    public boolean isFocus() {
        return isFocus;
    }

    public void setFocus(boolean focus) {
        isFocus = focus;
    }

    public boolean isShowPassword() {
        return isPassword;
    }

    public void setShowPassword(boolean pwd) {
        isPassword = pwd;
    }

    public void setWidthSpace(int widthSpace) {
        this.widthSpace = widthSpace;
    }

    public void setHeightSpace(int heightSpace) {
        this.heightSpace = heightSpace;
    }

    public void setBoxStrokeWidth(int boxStrokeWidth) {
        this.boxStrokeWidth = boxStrokeWidth;
    }

    public void setTextSize(int txtSize) {
        this.textSize = txtSize;
    }

    public void setBackgroundSolid(boolean isBackgroundSolid) {
        this.isBackgroundSolid = isBackgroundSolid;
        if (isBackgroundSolid) {
            boxPaint.setStyle(Paint.Style.FILL);
        } else {
            boxPaint.setStyle(Paint.Style.STROKE);
        }
    }

    public void setNumberLength(int numLength) {
        this.length = numLength;
        list.clear();
    }

    public String getPassword() {
        return text;
    }

    public void setBoxSelectColor(int boxSelectColor) {
        this.boxSelectColor = boxSelectColor;
    }

    public void setBoxNormalColor(int boxNormalColor) {
        this.boxNormalColor = boxNormalColor;
    }

    @Override
    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int widthSize = heightSize * length + widthSpace * (length - 1);
        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        isFocus = focused;
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);

        if (this.text == null) {
            return;
        }

        if (this.text.length() < length) {
            this.text = this.text + text.toString();
        } else {
            if (onInputOverListener != null) {
                onInputOverListener.onInputOver(this.text);
                if (isAutoCloseKeyBoard) {
                    closeKeybord();
                }
            }

        }

        if (text.toString().length() != 0) {
            setText("");
        }

    }

    private void init(AttributeSet attrs, int defStyleAttr) {

        boxPaint = new Paint();
        textPaint = new Paint();
        textRect = new Rect();

        setBackgroundDrawable(null);
        setLongClickable(false);
        setTextIsSelectable(false);
        setCursorVisible(false);

        textPaint.setStyle(Paint.Style.FILL);

        widthSpace = dip(5);
        heightSpace = dip(5);
        isPassword = true;
        isAutoCloseKeyBoard = true;
        passwordTypeCircleRadius = dip(8);
        boxStrokeWidth = dip(1);
        textSize = dip(18);
        isBackgroundSolid = false;
        boxRadius = dip(2);
        length = 4;
        textColor = 0xff666666;
        boxNormalColor = 0xff808080;
        boxSelectColor = 0xff44ce61;
        passwordShape = PasswordShape.CIRCLE;
        if (attrs != null) {
            TypedArray a = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.PasswordInputView, defStyleAttr, 0);
            isPassword = a.getBoolean(R.styleable.PasswordInputView_showPassword, isPassword);
            isAutoCloseKeyBoard = a.getBoolean(R.styleable.PasswordInputView_autoCloseKeyBoard, isAutoCloseKeyBoard);
            widthSpace = a.getDimensionPixelSize(R.styleable.PasswordInputView_widthSpace, widthSpace);
            passwordTypeCircleRadius = a.getDimensionPixelSize(R.styleable.PasswordInputView_circleRadius, passwordTypeCircleRadius);
            heightSpace = a.getDimensionPixelSize(R.styleable.PasswordInputView_heightSpace, heightSpace);
            boxStrokeWidth = a.getDimensionPixelSize(R.styleable.PasswordInputView_boxStrokeWidth, boxStrokeWidth);
            textSize = a.getDimensionPixelSize(R.styleable.PasswordInputView_textSize, textSize);
            isBackgroundSolid = a.getBoolean(R.styleable.PasswordInputView_backgroundSolid, isBackgroundSolid);
            boxRadius = a.getDimensionPixelSize(R.styleable.PasswordInputView_boxRadius, boxRadius);
            length = a.getInt(R.styleable.PasswordInputView_length, length);
            textColor = a.getColor(R.styleable.PasswordInputView_textColor, textColor);
            boxNormalColor = a.getColor(R.styleable.PasswordInputView_boxNormalColor, boxNormalColor);
            boxSelectColor = a.getColor(R.styleable.PasswordInputView_boxSelectColor, boxSelectColor);
            passwordShape = a.getInt(R.styleable.PasswordInputView_passwordShape, 0) == 0 ? PasswordShape.CIRCLE : PasswordShape.ASTERISK;
            a.recycle();
        }
        boxPaint.setStrokeWidth(boxStrokeWidth);
        textPaint.setColor(textColor);
        textPaint.setTextSize(textSize);
        if (isBackgroundSolid) {
            boxPaint.setStyle(Paint.Style.FILL);
        } else {
            boxPaint.setStyle(Paint.Style.STROKE);
        }
    }

    private final int dip(int dip) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, dip, getResources().getDisplayMetrics());
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 67 && text.length() != 0) {
            text = text.substring(0, text.length() - 1);
            invalidate();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = Math.min(canvas.getHeight(), canvas.getWidth() / length);

        for (int i = 0; i < length; i++) {
            if (i <= text.length() && isFocus) {
                boxPaint.setColor(boxSelectColor);
            } else {
                boxPaint.setColor(boxNormalColor);
            }
            if (list.size() < length) {
                RectF rect = new RectF(i * width + widthSpace, heightSpace, i * width + width - widthSpace, width - heightSpace);
                list.add(rect);
                canvas.drawRoundRect(rect, boxRadius, boxRadius, boxPaint);
            } else {
                RectF rect = list.get(i);
                canvas.drawRoundRect(rect, boxRadius, boxRadius, boxPaint);
            }
        }

        for (int i = 0; i < text.length(); i++) {
            if (isPassword) {
                if (passwordShape == PasswordShape.CIRCLE) {
                    canvas.drawCircle(list.get(i).centerX(), list.get(i).centerY(), passwordTypeCircleRadius, textPaint);
                } else {
                    textPaint.getTextBounds("*", 0, 1, textRect);
                    canvas.drawText("*", list.get(i).left + (list.get(i).right - list.get(i).left) / 2 - textRect.width() / 2,
                            list.get(i).top + ((list.get(i).bottom - list.get(i).top) / 2) + textRect.height(), textPaint);
                }
            } else {
                textPaint.getTextBounds(text.substring(i, i + 1), 0, 1, textRect);
                canvas.drawText(text.substring(i, i + 1), list.get(i).left + (list.get(i).right - list.get(i).left) / 2 - textRect.width() / 2,
                        list.get(i).top + ((list.get(i).bottom - list.get(i).top) / 2) + textRect.height() / 2, textPaint);
            }

        }
    }

    public interface onInputOverListener {
        void onInputOver(String text);
    }

    /**
     * 关闭软键盘
     */
    public void closeKeybord() {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getWindowToken(), 0);
    }

}
