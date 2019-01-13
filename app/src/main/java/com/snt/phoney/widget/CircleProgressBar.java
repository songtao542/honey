package com.snt.phoney.widget;


import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.snt.phoney.R;

import static android.graphics.Paint.Style.STROKE;
import static com.snt.phoney.extensions.ViewUtilKt.dip;

/**
 *
 */
@SuppressWarnings("unused")
public class CircleProgressBar extends View {

    private int progressForegroundColor;    //进度的颜色
    private int progressBackgroundColor;    //背景颜色
    private int progressTextColor;   //圆环内文字颜色
    private float progressTextSize;    //圆环内文字大小
    private float progressWidth;    //圆环的宽度
    private int maxProgress;    //最大进度
    private float progress;    //当前进度
    /**
     * 起始位置，上下左右
     */
    private int starting;    //进度从哪里开始(设置了4个值,上左下右)

    private boolean showPercent;

    private Paint paint;

    private RectF progressRectF = new RectF();
    private Rect textRect = new Rect();

    private ValueAnimator valueAnimator;

    enum DirectionEnum {
        LEFT(0, 180.0f),
        TOP(1, 270.0f),
        RIGHT(2, 0.0f),
        BOTTOM(3, 90.0f);

        private final int direction;
        private final float degree;

        DirectionEnum(int direction, float degree) {
            this.direction = direction;
            this.degree = degree;
        }

        public int getDirection() {
            return direction;
        }

        public float getDegree() {
            return degree;
        }

        public boolean equalsDescription(int direction) {
            return this.direction == direction;
        }

        public static DirectionEnum getDirection(int direction) {
            for (DirectionEnum enumObject : values()) {
                if (enumObject.equalsDescription(direction)) {
                    return enumObject;
                }
            }
            return RIGHT;
        }

        public static float getDegree(int direction) {
            DirectionEnum enumObject = getDirection(direction);
            if (enumObject == null) {
                return 0;
            }
            return enumObject.getDegree();
        }
    }

    public CircleProgressBar(Context context) {
        this(context, null);
    }

    public CircleProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CircleProgressBar, defStyleAttr, 0);

        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(android.R.attr.colorAccent, typedValue, true);
        int colorPrimary = typedValue.data;

        progressForegroundColor = a.getColor(R.styleable.CircleProgressBar_progressForegroundColor, colorPrimary);
        progressBackgroundColor = a.getColor(R.styleable.CircleProgressBar_progressBackgroundColor, 0xffcccccc);
        progressTextColor = a.getColor(R.styleable.CircleProgressBar_progressTextColor, colorPrimary);
        progressTextSize = a.getDimension(R.styleable.CircleProgressBar_progressTextSize, dip(this, 14f));
        progressWidth = a.getDimension(R.styleable.CircleProgressBar_progressWidth, dip(this, 4f));
        progress = a.getFloat(R.styleable.CircleProgressBar_progress, 0f);
        maxProgress = a.getInt(R.styleable.CircleProgressBar_maxProgress, 100);
        starting = a.getInt(R.styleable.CircleProgressBar_starting, 3);
        showPercent = a.getBoolean(R.styleable.CircleProgressBar_showPercent, true);

        a.recycle();

        paint = new Paint();
        paint.setStyle(STROKE); //设置空心
        paint.setAntiAlias(true);  //消除锯齿
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int radius = Math.min(getWidth() / 2, getHeight() / 2);
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        //第一步:画背景(即内层圆)
        paint.setColor(progressBackgroundColor); //设置圆的颜色
        paint.setStrokeWidth(progressWidth); //设置圆的宽度
        progressRectF.left = centerX - radius + progressWidth;
        progressRectF.top = centerY - radius + progressWidth;
        progressRectF.right = centerX + radius - progressWidth;
        progressRectF.bottom = centerY + radius - progressWidth;
        //canvas.drawCircle(centerX, centerY, radius, paint); //画出圆
        canvas.drawArc(progressRectF, 0, 360, false, paint);  //根据进度画圆弧

        //第二步:画进度(圆弧)
        paint.setColor(progressForegroundColor);  //设置进度的颜色
        canvas.drawArc(progressRectF, DirectionEnum.getDegree(starting), 360 * (progress / maxProgress), false, paint);  //根据进度画圆弧

        //第三步:画圆环内百分比文字
        paint.setColor(progressTextColor);
        paint.setTextSize(progressTextSize);
        paint.setStrokeWidth(0);
        //圆环内文字
        String progressText = getProgressText();
        paint.getTextBounds(progressText, 0, progressText.length(), textRect);
        Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
        int baseline = (getMeasuredHeight() - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;  //获得文字的基准线
        //>> 1 等同于除以2
        canvas.drawText(progressText, (getMeasuredWidth() >> 1) - (textRect.width() >> 1), baseline, paint);
    }

    //中间的进度百分比
    private String getProgressText() {
        if (showPercent) {
            return (int) ((progress / maxProgress) * 100) + "%";
        } else {
            return String.valueOf((int) (progress / maxProgress));
        }
    }

    public int getProgressForegroundColor() {
        return progressForegroundColor;
    }

    public void setProgressForegroundColor(int progressForegroundColor) {
        this.progressForegroundColor = progressForegroundColor;
    }

    public int getProgressBackgroundColor() {
        return progressBackgroundColor;
    }

    public void setProgressBackgroundColor(int progressBackgroundColor) {
        this.progressBackgroundColor = progressBackgroundColor;
    }

    public int getProgressTextColor() {
        return progressTextColor;
    }

    public void setProgressTextColor(int progressTextColor) {
        this.progressTextColor = progressTextColor;
    }

    public float getProgressTextSize() {
        return progressTextSize;
    }

    public void setProgressTextSize(float progressTextSize) {
        this.progressTextSize = progressTextSize;
    }

    public float getProgressWidth() {
        return progressWidth;
    }

    public void setProgressWidth(float progressWidth) {
        this.progressWidth = progressWidth;
    }

    public synchronized int getMaxProgress() {
        return maxProgress;
    }

    public synchronized void setMaxProgress(int maxProgress) {
        if (maxProgress < 0) {
            //此为传递非法参数异常
            throw new IllegalArgumentException("maxProgress should not be less than 0");
        }
        this.maxProgress = maxProgress;
    }

    public synchronized float getProgress() {
        return progress;
    }

    //加锁保证线程安全,能在线程中使用
    public synchronized void setProgress(int progress) {
        if (progress < 0) {
            throw new IllegalArgumentException("progress should not be less than 0");
        }
        if (progress > maxProgress) {
            progress = maxProgress;
        }
        startAnim(progress);
    }

    private void startAnim(float from, float to) {
        valueAnimator = ObjectAnimator.ofFloat(from, to);
        valueAnimator.addUpdateListener(animation -> {
            progress = (float) animation.getAnimatedValue();
            postInvalidate();
        });
        valueAnimator.setDuration(2000);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.start();
    }

    public void startAnim(long duration) {
        startAnim(duration, null);
    }

    public void startAnim(long duration, AnimationEndListener listener) {
        valueAnimator = ObjectAnimator.ofFloat(0, maxProgress);
        valueAnimator.addUpdateListener(animation -> {
            progress = (float) animation.getAnimatedValue();
            postInvalidate();
        });
        if (listener != null) {
            valueAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    if (listener != null) {
                        listener.onAnimationEnd();
                    }
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                }
            });
        }
        valueAnimator.setDuration(duration);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.start();
    }

    public void cancelAnimation() {
        if (valueAnimator != null && valueAnimator.isRunning()) {
            valueAnimator.cancel();
        }
    }

    public interface AnimationEndListener {
        void onAnimationEnd();
    }
}
