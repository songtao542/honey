package expandable.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

public class ExpandableLayout extends LinearLayout {
    private boolean animateExpand = true;
    private int animateDuration = 3000;
    private int indicatorId = 0;
    private View indicatorView = null;

    private boolean isExpanded = false;
    private int expandableViewHeight;

    private View headView;
    private View expandableView;
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
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        headView = getChildAt(0);
        expandableView = getChildAt(1);
        if (headView == null || expandableView == null) {
            return;
        }
        if (getChildCount() > 3) {
            throw new IllegalStateException("ExpandableView can have only two child views, but find " + getChildCount() + "child!");
        }

        if (indicatorId != 0) {
            indicatorView = findViewById(indicatorId);
        }

        expandableViewParams = (LinearLayout.LayoutParams) expandableView.getLayoutParams();
        expandableViewHeight = expandableViewParams.height;
        expandableViewParams.height = 0;
        expandableView.setLayoutParams(expandableViewParams);
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

    private void expand() {
        expandableViewParams.height = expandableViewHeight;
        expandableView.setLayoutParams(expandableViewParams);
        isExpanded = true;
        if (expandListener != null) {
            expandListener.onExpand(this, isExpanded);
        }
    }

    private void expandWithAnimation() {
        ObjectAnimator rotationAnimator = null;
        if (indicatorView != null) {
            rotationAnimator = ObjectAnimator.ofFloat(indicatorView, "rotation", 0f, 180f);
        }

        ValueAnimator expandAnimator = ValueAnimator.ofInt(0, expandableViewHeight);
        expandAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                expandableViewParams.height = (int) valueAnimator.getAnimatedValue();
                expandableView.setLayoutParams(expandableViewParams);
                expandableView.requestLayout();
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
                if (expandListener != null) {
                    expandListener.onExpand(ExpandableLayout.this, true);
                }
            }
        });

        set.start();
    }

    private void collapse() {
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

    private void collapseWithAnimation() {
        ObjectAnimator rotationAnimator = null;
        if (indicatorView != null) {
            rotationAnimator = ObjectAnimator.ofFloat(indicatorView, "rotation", 180, 0f);
        }

        ValueAnimator expandAnimator = ValueAnimator.ofInt(expandableViewHeight, 0);
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

    public void setExpandListener(ExpandListener expandListener) {
        this.expandListener = expandListener;
    }

    public interface ExpandListener {
        void onExpand(View view, boolean isExpanded);
    }
}
