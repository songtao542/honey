package com.snt.phoney.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.snt.phoney.R;

public class ExpandableLayout extends LinearLayout {
    private boolean animateExpand = true;
    private int animateDuration = 300;
    private int indicatorId = 0;
    private View indicatorView = null;

    private boolean isExpanded = false;
    private boolean animating = false;
    protected int expandableHeight;

    private LinearLayout expandableView;
    private LayoutParams expandableViewParams;

    private ExpandListener expandListener;

    public ExpandableLayout(Context context) {
        this(context, null, 0);
    }

    public ExpandableLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ExpandableLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(VERTICAL);
        TypedArray ta = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.ExpandableLayout, 0, 0);
        animateExpand = ta.getBoolean(R.styleable.ExpandableLayout_animateExpand, true);
        indicatorId = ta.getResourceId(R.styleable.ExpandableLayout_indicatorId, 0);
        ta.recycle();
        ensureExpandableContainer();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (indicatorId != 0) {
            indicatorView = findViewById(indicatorId);
        }
    }


    public void removeAllExpandableViews() {
        if (expandableView != null) {
            expandableView.removeAllViews();
        }
    }

    public void setIndicatorView(View view) {
        indicatorView = view;
    }

    public LinearLayout getExpandableView() {
        return expandableView;
    }

    public void onExpandableHeightChange(int height) {
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (params instanceof LayoutParams) {
            if (((LayoutParams) params).getShowAtTop()) {
                super.addView(child, index, params);
            } else {
                if (expandableView.getParent() == null) {
                    super.addView(expandableView, index, expandableViewParams);
                }
                expandableView.addView(child, params);
            }
        } else {
            super.addView(child, index, params);
        }
    }


    private void ensureExpandableContainer() {
        if (expandableView == null) {
            expandableView = new LinearLayout(getContext());
            expandableView.setOrientation(VERTICAL);
            expandableView.addOnLayoutChangeListener(new OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View view, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                    int width = MeasureSpec.makeMeasureSpec(getResources().getDisplayMetrics().widthPixels, MeasureSpec.EXACTLY);
                    int height = MeasureSpec.makeMeasureSpec(getResources().getDisplayMetrics().heightPixels, MeasureSpec.AT_MOST);
                    expandableView.measure(width, height);
                    expandableHeight = expandableView.getMeasuredHeight();
                    onExpandableHeightChange(expandableHeight);
                }
            });
            expandableViewParams = new LayoutParams(LayoutParams.MATCH_PARENT, 0);
        }
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    @Override
    public LinearLayout.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    @Override
    protected LinearLayout.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        ViewGroup.LayoutParams lp = getLayoutParams();
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        setLayoutParams(lp);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void setIndicatorId(int indicatorId) {
        this.indicatorId = indicatorId;
        if (indicatorId != 0) {
            indicatorView = findViewById(indicatorId);
        }
    }

    public void toggle(int indicatorId) {
        setIndicatorId(indicatorId);
        toggle();
    }

    public void toggle() {
        if (isExpanded) {
            if (animateExpand) {
                collapseWithAnimation();
            } else {
                collapse();
            }
        } else {
            if (animateExpand) {
                expandWithAnimation();
            } else {
                expand();
            }
        }
    }

    public void expand() {
        expandableViewParams.height = expandableHeight;
        expandableView.setLayoutParams(expandableViewParams);
        isExpanded = true;
        if (expandListener != null) {
            expandListener.onExpand(this, isExpanded);
        }
    }

    public void expandWithAnimation() {
        animating = true;
        ObjectAnimator rotationAnimator = null;
        if (indicatorView != null) {
            rotationAnimator = ObjectAnimator.ofFloat(indicatorView, "rotation", 0f, 180f);
        }

        ValueAnimator expandAnimator = ValueAnimator.ofInt(0, expandableHeight);
        expandAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                expandableViewParams.height = (int) valueAnimator.getAnimatedValue();
                expandableView.setLayoutParams(expandableViewParams);
            }
        });

        AnimatorSet set = new AnimatorSet();
        if (rotationAnimator != null) {
            set.playTogether(rotationAnimator, expandAnimator);
        } else {
            set.playTogether(expandAnimator);
        }
        set.setInterpolator(new DecelerateInterpolator());
        set.setDuration(animateDuration);
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                isExpanded = true;
                animating = false;
                if (expandListener != null) {
                    expandListener.onExpand(ExpandableLayout.this, true);
                }
            }
        });

        set.start();
    }

    public void collapse() {
        expandableViewParams.height = 0;
        expandableView.setLayoutParams(expandableViewParams);
        if (indicatorView != null) {
            indicatorView.setRotation(0);
        }
        isExpanded = false;
        if (expandListener != null) {
            expandListener.onExpand(this, false);
        }
    }

    public void collapseWithAnimation() {
        animating = true;
        ObjectAnimator rotationAnimator = null;
        if (indicatorView != null) {
            rotationAnimator = ObjectAnimator.ofFloat(indicatorView, "rotation", 180, 0f);
        }

        ValueAnimator expandAnimator = ValueAnimator.ofInt(expandableHeight, 0);
        expandAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                expandableViewParams.height = (int) valueAnimator.getAnimatedValue();
                expandableView.setLayoutParams(expandableViewParams);
            }
        });

        AnimatorSet set = new AnimatorSet();
        if (rotationAnimator != null) {
            set.playTogether(rotationAnimator, expandAnimator);
        } else {
            set.playTogether(expandAnimator);
        }
        set.setInterpolator(new DecelerateInterpolator());
        set.setDuration(animateDuration);
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                animating = false;
                isExpanded = false;
                if (expandListener != null) {
                    expandListener.onExpand(ExpandableLayout.this, false);
                }
            }
        });

        set.start();
    }


    public boolean isExpanded() {
        return isExpanded;
    }
    public boolean isAnimating() {
        return animating;
    }

    public void setExpandListener(ExpandListener expandListener) {
        this.expandListener = expandListener;
    }

    public interface ExpandListener {
        void onExpand(View view, boolean isExpanded);
    }

    public static class LayoutParams extends LinearLayout.LayoutParams {

        boolean mShowAtTop = false;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            TypedArray a = c.obtainStyledAttributes(attrs, R.styleable.ExpandableLayout_Layout);
            mShowAtTop = a.getBoolean(R.styleable.ExpandableLayout_Layout_layout_showAtTop, false);
            a.recycle();
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(int width, int height, int gravity) {
            super(width, height, gravity);
        }

        public LayoutParams(ViewGroup.LayoutParams p) {
            super(p);
        }

        public LayoutParams(MarginLayoutParams source) {
            super(source);
        }

        @RequiresApi(19)
        public LayoutParams(FrameLayout.LayoutParams source) {
            // The copy constructor called here only exists on API 19+.
            super(source);
        }

        public void setShowAtTop(boolean showAtTop) {
            mShowAtTop = showAtTop;
        }


        public boolean getShowAtTop() {
            return mShowAtTop;
        }
    }
}
