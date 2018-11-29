package com.snt.phoney.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.annotation.Nullable;


public class NestedParentLayout extends LinearLayout {

    private static final String TAG = "NestedParentLayout";

    private int mTopViewHeight;
    private ValueAnimator mOffsetAnimator;
    private View mNestedView;

    private int mMinHeight = 0;

    public NestedParentLayout(Context context) {
        super(context);
        setOrientation(LinearLayout.VERTICAL);
    }

    public NestedParentLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setOrientation(LinearLayout.VERTICAL);
    }

    public NestedParentLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(LinearLayout.VERTICAL);
    }

    public NestedParentLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setOrientation(LinearLayout.VERTICAL);
    }

    public void setMinHeight(int minHeight) {
        this.mMinHeight = minHeight;
    }

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        //Log.d(TAG, "onStartNestedScroll target=" + target);
        setWillNotDraw(false);
        if (mNestedView != null && mNestedView instanceof ListView) {
            ListView listView = (ListView) mNestedView;
            View lastChild = listView.getChildAt(listView.getChildCount() - 1);
            int[] l1 = new int[2];
            lastChild.getLocationOnScreen(l1);
            int[] l2 = new int[2];
            getLocationOnScreen(l2);
            if (getScrollY() == 0 && (l1[1] + lastChild.getHeight()) < l2[1] + getHeight()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onNestedScrollAccepted(View child, View target, int nestedScrollAxes) {
        //Log.d(TAG, "onNestedScrollAccepted");
    }

    @Override
    public void onStopNestedScroll(View target) {
        //Log.d(TAG, "onStopNestedScroll");
        final int currentOffset = getScrollY();
        final int topHeight = mTopViewHeight;
        if (currentOffset > topHeight / 2) {
            Log.d(TAG, "onStopNestedScroll currentOffset1=" + currentOffset);
            animateScroll(2000, 160);
        } else {
            Log.d(TAG, "onStopNestedScroll currentOffset2=" + currentOffset);
            animateScroll(-2000, 160);
        }
    }

    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        Log.d(TAG, "onNestedScroll dyConsumed=" + dyConsumed);
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        //Log.d(TAG, "onNestedPreScroll dy-->" + dy);
        //往上滑动时 dy>0
        boolean hiddenTop = dy > 0 && getScrollY() < mTopViewHeight - mMinHeight;
        boolean showTop = dy < 0 && getScrollY() > 0 && !target.canScrollVertically(-1);

        if (hiddenTop || showTop) {
            scrollBy(0, dy);
            consumed[1] = dy;
        }
    }

    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        //往上滑动时，velocityY>0
        Log.d(TAG, "onNestedFling velocityY=" + velocityY + "  consumed=" + consumed);
        if (!consumed) {
            animateScroll(velocityY, 160);
            return true;
        }
        return false;
    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        //不做拦截 可以传递给子View
        return false;
    }

    @Override
    public int getNestedScrollAxes() {
        //Log.d(TAG, "getNestedScrollAxes");
        return 0;
    }

    /**
     * 根据速度计算滚动动画持续时间
     *
     * @param velocityY
     * @return
     */
    private int computeDuration(float velocityY) {
        int distance;
        //往上滑动时，velocityY>0
        if (velocityY > 0) {
            distance = Math.abs(mTopViewHeight - getScrollY());
        } else {
            distance = Math.abs(getScrollY());
        }
        return Math.round(100 * (distance * 10 / Math.abs(velocityY)));
    }

    private void animateScroll(float velocityY, int duration) {
        if (mOffsetAnimator != null && mOffsetAnimator.isRunning()) {
            Log.d(TAG, "Running------------");
            return;
        }
        final int currentOffset = getScrollY();
        final int topHeight = mTopViewHeight;
        if (mOffsetAnimator == null) {
            mOffsetAnimator = new ValueAnimator();
            mOffsetAnimator.addUpdateListener(animation -> {
                if (animation.getAnimatedValue() instanceof Integer) {
                    scrollTo(0, (Integer) animation.getAnimatedValue());
                }
            });
        }
        mOffsetAnimator.setDuration(Math.min(duration, 200));
        if (velocityY >= 0) {
            mOffsetAnimator.setIntValues(currentOffset, topHeight - mMinHeight);
            mOffsetAnimator.start();
        } else {
            mOffsetAnimator.setIntValues(currentOffset, 0);
            mOffsetAnimator.start();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (getChildCount() >= 2) {
            mNestedView = getFirstNestedScrollableView();
            if (mNestedView != null) {
                View topView = getChildAt(0);
                topView.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
                mTopViewHeight = topView.getMeasuredHeight();
                ViewGroup.LayoutParams params = mNestedView.getLayoutParams();
                params.height = getMeasuredHeight();
            }
        }
    }

    private View getFirstNestedScrollableView() {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View view = getChildAt(i);
            if (view.isNestedScrollingEnabled()) {
                return view;
            }
        }
        return null;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (getChildCount() >= 1) {
            View topView = getChildAt(0);
            mTopViewHeight = topView.getMeasuredHeight();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mOffsetAnimator != null && mOffsetAnimator.isRunning()) {
            return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void scrollTo(int x, int y) {
        if (y < 0) {
            y = 0;
        }
        if (y > mTopViewHeight) {
            y = mTopViewHeight;
        }
        if (y != getScrollY()) {
            super.scrollTo(x, y);
        }
    }
}
