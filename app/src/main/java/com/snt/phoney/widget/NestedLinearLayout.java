package com.snt.phoney.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.OverScroller;

import androidx.annotation.Nullable;


public class NestedLinearLayout extends LinearLayout {

    private static final String TAG = "NestedParentLayout";

    private View mNestedView;
    private View mTopView;
    private boolean mTopScaleable = false;
    private OnTopVisibleHeightChangeListener mTopHeightChangeListener;

    private int mDirection = 0;

    private int mMinHeight = 0;
    private boolean mAutoScroll = true;
    private boolean mCalculateMaxScrollHeight = false;
    private int mMaxScrollHeight;
    private int mTopViewHeight;

    private OverScroller mScroller;

    public NestedLinearLayout(Context context) {
        super(context);
        init(context);
    }

    public NestedLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public NestedLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public NestedLinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        setOrientation(LinearLayout.VERTICAL);
        mScroller = new OverScroller(context);
    }

    public void setMinHeight(int minHeight) {
        this.mMinHeight = minHeight;
    }

    public void setAutoScroll(boolean autoScroll) {
        mAutoScroll = autoScroll;
    }

    public void setCalculateMaxScrollHeight(boolean calculate) {
        mCalculateMaxScrollHeight = calculate;
    }

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        //Log.d(TAG, "onStartNestedScroll target=" + target);
        if (mNestedView != null && mNestedView instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) mNestedView;
            //内容太少，填不满NestedView
            if (viewGroup.getChildCount() > 0) {
                View lastChild = viewGroup.getChildAt(viewGroup.getChildCount() - 1);
                int[] l1 = new int[2];
                //获取最后一个子View在屏幕的位置
                lastChild.getLocationOnScreen(l1);
                int[] l2 = new int[2];
                //获取ViewGroup在屏幕的位置
                getLocationOnScreen(l2);
                //当未滑动之前，最后一个子View的底部坐标 比 ViewGroup 的底部坐标还要小，则不进行拦截
                if (getScrollY() == 0 && (l1[1] + lastChild.getHeight()) < (l2[1] + getHeight())) {
                    return false;
                }
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
        if (mAutoScroll && mScroller.isFinished() && getScrollY() > 0 && getScrollY() < mMaxScrollHeight - mMinHeight) {
            // mDirection>0 向下
            mScroller.forceFinished(true);
            int scrollY = getScrollY();
            if (mDirection > 0) {
                mScroller.startScroll(0, scrollY, 0, -scrollY);
            } else {
                mScroller.startScroll(0, scrollY, 0, mMaxScrollHeight - mMinHeight - scrollY);
            }
            invalidate();
            mDirection = 0;
        }
    }

    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        //Log.d(TAG, "onNestedScroll");
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        //Log.d(TAG, "onNestedPreScroll dy-->" + dy);
        //往上滑动时 dy>0
        if (mDirection != 0) {
            mDirection = dy;
        }
        boolean hiddenTop = dy > 0 && getScrollY() < mMaxScrollHeight - mMinHeight;
        boolean showTop = dy < 0 && getScrollY() > 0 && !target.canScrollVertically(-1);

        int maxCanConsumed = mMaxScrollHeight - mMinHeight - getScrollY();
        int consumedY = Math.min(dy, maxCanConsumed);
        if (hiddenTop || showTop) {
            scrollBy(0, consumedY);
            consumed[1] = consumedY;
        }
    }

    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        //往上滑动时，velocityY>0
        if (getScrollY() != 0) {
            mScroller.fling(getScrollX(), getScrollY(), // start
                    0, (int) velocityY, // velocities
                    0, 0, // x
                    Integer.MIN_VALUE, Integer.MAX_VALUE, // y
                    0, 0);
            invalidate();
            return true;
        }
        return false;
    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        //不做拦截 可以传递给子View
        if (getScrollY() != mMaxScrollHeight - mMinHeight) {
            mScroller.fling(getScrollX(), getScrollY(), // start
                    0, (int) velocityY, // velocities
                    0, 0, // x
                    Integer.MIN_VALUE, Integer.MAX_VALUE, // y
                    0, 0);
            invalidate();
            return true;
        }
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        final int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                if (!mScroller.isFinished()) {
                    mScroller.forceFinished(true);
                }
            }
        }
        return super.onInterceptTouchEvent(ev);
    }


    @Override
    public int getNestedScrollAxes() {
        //Log.d(TAG, "getNestedScrollAxes");
        return 0;
    }

    @Override
    public void computeScroll() {
        if (!mScroller.isFinished() && mScroller.computeScrollOffset()) {
            int y = mScroller.getCurrY();
            scrollTo(0, y);
            invalidate();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (getChildCount() >= 2) {
            mTopView = getChildAt(0);
            mNestedView = getFirstNestedScrollableView();
            if (mNestedView != null) {
                View topView = getChildAt(0);
                topView.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
                if (mCalculateMaxScrollHeight) {
                    mNestedView.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
                    int maxScrollHeight = topView.getMeasuredHeight() + mNestedView.getMeasuredHeight() - getMeasuredHeight() + mMinHeight;
                    int topViewHeight = topView.getMeasuredHeight();
                    if (topView.getMeasuredHeight() > maxScrollHeight) {
                        mMaxScrollHeight = maxScrollHeight;
                        ViewGroup.LayoutParams params = mNestedView.getLayoutParams();
                        params.height = mNestedView.getMeasuredHeight();
                        mNestedView.setLayoutParams(params);
                    } else {
                        mMaxScrollHeight = topViewHeight;
                        ViewGroup.LayoutParams params = mNestedView.getLayoutParams();
                        params.height = getMeasuredHeight() - mMinHeight;
                        mNestedView.setLayoutParams(params);
                    }
                } else {
                    mMaxScrollHeight = topView.getMeasuredHeight();
                    ViewGroup.LayoutParams params = mNestedView.getLayoutParams();
                    params.height = getMeasuredHeight() - mMinHeight;
                    mNestedView.setLayoutParams(params);
                }
                mTopViewHeight = topView.getMeasuredHeight();
            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
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
    public void scrollTo(int x, int y) {
        if (y > mMaxScrollHeight - mMinHeight) {
            y = mMaxScrollHeight - mMinHeight;
        }
        if (y < 0) {
            y = 0;
        }

        if (y != getScrollY()) {
            super.scrollTo(x, y);
            if (mTopScaleable && mTopView != null) {
                float scale = 1 - y * 1f / mMaxScrollHeight;
                mTopView.setPivotY(mMaxScrollHeight);
                mTopView.setScaleX(scale);
                mTopView.setScaleY(scale);
                mTopView.setAlpha(scale);
            }
            if (mTopHeightChangeListener != null) {
                mTopHeightChangeListener.onTopVisibleHeightChange(mMaxScrollHeight >= mTopViewHeight, mMaxScrollHeight - mMinHeight, y);
            }
        }
    }

    /**
     * @param scaleable
     */
    public void setTopScaleable(boolean scaleable) {
        mTopScaleable = scaleable;
    }

    /**
     * @param listener
     */
    public void setOnTopVisibleHeightChangeListener(OnTopVisibleHeightChangeListener listener) {
        mTopHeightChangeListener = listener;
    }

    public interface OnTopVisibleHeightChangeListener {
        void onTopVisibleHeightChange(boolean heightEnough, int totalHeight, int visibleHeight);
    }

}
