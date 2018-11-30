package com.snt.phoney.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.Scroller;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;


public class NestedParentLayout extends LinearLayout {

    private static final String TAG = "NestedParentLayout";

    private int mTopViewHeight;
    private ValueAnimator mOffsetAnimator;
    private View mNestedView;
    private View mTopView;
    private boolean mTopScaleable = false;
    private OnTopVisibleHeightChangeListener mTopHeightChangeListener;

    private int mDrection = 0;

    private int mMinHeight = 0;

    Scroller mScroller;

    public NestedParentLayout(Context context) {
        super(context);
        init(context);
    }

    public NestedParentLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public NestedParentLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public NestedParentLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        setOrientation(LinearLayout.VERTICAL);
        mScroller = new Scroller(context);
    }

    public void setMinHeight(int minHeight) {
        this.mMinHeight = minHeight;
    }

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        //Log.d(TAG, "onStartNestedScroll target=" + target);
        if (mNestedView != null && mNestedView instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) mNestedView;
            if (viewGroup.getChildCount() > 0) {
                View lastChild = viewGroup.getChildAt(viewGroup.getChildCount() - 1);
                int[] l1 = new int[2];
                lastChild.getLocationOnScreen(l1);
                int[] l2 = new int[2];
                getLocationOnScreen(l2);
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
        Log.d(TAG, "onStopNestedScroll mDrection=" + mDrection);
        if (mScroller.isFinished()) {
//            final int currentOffset = getScrollY();
//            final int topHeight = mTopViewHeight;
//            if (currentOffset > topHeight / 2 && currentOffset != topHeight) {
//                animateScroll(2000, 160);
//            } else if (currentOffset < topHeight / 2 && currentOffset != 0) {
//                animateScroll(-2000, 160);
//            }
            // >0 向上
            mScroller.forceFinished(true);
            int scrollY = getScrollY();
            if (mDrection > 0) {
                mScroller.startScroll(0, scrollY, 0, -scrollY);
            } else {
                mScroller.startScroll(0, scrollY, 0, mTopViewHeight - mMinHeight - scrollY);
            }
            invalidate();
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
        if (mDrection != 0) {
            mDrection = dy;
        }
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
        Log.d("TTTT", "onNestedFling velocityY=" + velocityY + "  consumed=" + consumed);
        if (!consumed) {
            //animateScroll(velocityY, 160);
            return true;
        }
        return false;
    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        //不做拦截 可以传递给子View
        if (getScrollY() != mTopViewHeight - mMinHeight) {
            if (mScroller.isFinished()) {
                int scrollY = getScrollY();
                mScroller.forceFinished(true);
                if (velocityY > 0) {
                    Log.d("TTTT", "uuuuuuuuuuuuuuuuuuuuuuu scrollY =" + scrollY + " uuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuu velocityY=" + velocityY);
                    mScroller.startScroll(0, scrollY, 0, mTopViewHeight - mMinHeight - scrollY);
                } else {
                    Log.d("TTTT", "xxxxxxxxxxxxxxxxxxxxxxx scrollY =" + scrollY + " xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx velocityY=" + velocityY);
                    mScroller.startScroll(0, scrollY, 0, -scrollY);
                }
                invalidate();
            }
            return true;
        }
        return false;
    }

    @Override
    public int getNestedScrollAxes() {
        //Log.d(TAG, "getNestedScrollAxes");
        return 0;
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) { //or
            Log.e("TTTT", "computeScroll ======" + mScroller.getCurrY());
            scrollTo(0, mScroller.getCurrY());
            invalidate();
        } else
            Log.i("TTTT", "have done the scoller -----");
    }

    private void stopFilingScroll() {
        if (mNestedView instanceof RecyclerView) {
            ((RecyclerView) mNestedView).stopScroll();
        }
    }

    /**
     * 根据速度计算滚动动画持续时间
     *
     * @param velocityY
     * @return
     */
//    private int computeDuration(float velocityY) {
//        int distance;
//        //往上滑动时，velocityY>0
//        if (velocityY > 0) {
//            distance = Math.abs(mTopViewHeight - getScrollY());
//        } else {
//            distance = Math.abs(getScrollY());
//        }
//        return Math.round(100 * (distance * 10f / Math.abs(velocityY)));
//    }
//
    private void animateScroll(float velocityY, int duration) {
        if (mOffsetAnimator != null && mOffsetAnimator.isRunning()) {
            return;
        }
        final int currentOffset = getScrollY();
        final int topHeight = mTopViewHeight;
        if (mOffsetAnimator == null) {
            mOffsetAnimator = new ValueAnimator();
//            mOffsetAnimator.setInterpolator(new AccelerateInterpolator());
            mOffsetAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
            mOffsetAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    Log.d(TAG, "animateScroll animateScroll animateScroll");
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
            mTopView = getChildAt(0);
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
            mTopView = getChildAt(0);
            mTopViewHeight = mTopView.getMeasuredHeight();
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
            if (mTopScaleable && mTopView != null) {
                float scale = 1 - y * 1f / mTopViewHeight;
                mTopView.setPivotY(mTopViewHeight);
                mTopView.setScaleX(scale);
                mTopView.setScaleY(scale);
                mTopView.setAlpha(scale);
            }
            if (mTopHeightChangeListener != null) {
                mTopHeightChangeListener.onTopVisibleHeightChange(mTopViewHeight, y);
            }
        }
    }

    public void setTopScaleable(boolean scaleable) {
        mTopScaleable = scaleable;
    }

    public void setOnTopVisibleHeightChangeListener(OnTopVisibleHeightChangeListener listener) {
        mTopHeightChangeListener = listener;
    }

    public interface OnTopVisibleHeightChangeListener {
        void onTopVisibleHeightChange(int totalHeight, int visibleHeight);
    }

}
