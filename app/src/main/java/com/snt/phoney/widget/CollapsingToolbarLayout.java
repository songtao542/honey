/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.snt.phoney.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntDef;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import android.support.annotation.StyleRes;
//import android.support.design.R;
import android.support.design.widget.AppBarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.math.MathUtils;
import android.support.v4.text.TextDirectionHeuristicsCompat;
import android.support.v4.util.ObjectsCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.WindowInsetsCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.snt.phoney.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static android.support.annotation.RestrictTo.Scope.LIBRARY_GROUP;

/**
 * CollapsingToolbarLayout is a wrapper for {@link Toolbar} which implements a collapsing app bar.
 * It is designed to be used as a direct child of a {@link AppBarLayout}.
 * CollapsingToolbarLayout contains the following features:
 *
 * <h4>Collapsing title</h4>
 * A title which is larger when the layout is fully visible but collapses and becomes smaller as
 * the layout is scrolled off screen. You can set the title to display via
 * {@link #setTitle(CharSequence)}. The title appearance can be tweaked via the
 * {@code collapsedTextAppearance} and {@code expandedTextAppearance} attributes.
 *
 * <h4>Content scrim</h4>
 * A full-bleed scrim which is show or hidden when the scroll position has hit a certain threshold.
 * You can change this via {@link #setContentScrim(Drawable)}.
 *
 * <h4>Status bar scrim</h4>
 * A scrim which is show or hidden behind the status bar when the scroll position has hit a certain
 * threshold. You can change this via {@link #setStatusBarScrim(Drawable)}. This only works
 * on {@link android.os.Build.VERSION_CODES#LOLLIPOP LOLLIPOP} devices when we set to fit system
 * windows.
 *
 * <h4>Parallax scrolling children</h4>
 * Child views can opt to be scrolled within this layout in a parallax fashion.
 * See {@link LayoutParams#COLLAPSE_MODE_PARALLAX} and
 * {@link LayoutParams#setParallaxMultiplier(float)}.
 *
 * <h4>Pinned position children</h4>
 * Child views can opt to be pinned in space globally. This is useful when implementing a
 * collapsing as it allows the {@link Toolbar} to be fixed in place even though this layout is
 * moving. See {@link LayoutParams#COLLAPSE_MODE_PIN}.
 *
 * <p><strong>Do not manually add views to the Toolbar at run time</strong>.
 * We will add a 'dummy view' to the Toolbar which allows us to work out the available space
 * for the title. This can interfere with any views which you add.</p>
 *
 * @attr ref android.support.design.R.styleable#CollapsingToolbarLayout_collapsedTitleTextAppearance
 * @attr ref android.support.design.R.styleable#CollapsingToolbarLayout_expandedTitleTextAppearance
 * @attr ref android.support.design.R.styleable#CollapsingToolbarLayout_contentScrim
 * @attr ref android.support.design.R.styleable#CollapsingToolbarLayout_expandedTitleMargin
 * @attr ref android.support.design.R.styleable#CollapsingToolbarLayout_expandedTitleMarginStart
 * @attr ref android.support.design.R.styleable#CollapsingToolbarLayout_expandedTitleMarginEnd
 * @attr ref android.support.design.R.styleable#CollapsingToolbarLayout_expandedTitleMarginBottom
 * @attr ref android.support.design.R.styleable#CollapsingToolbarLayout_statusBarScrim
 * @attr ref android.support.design.R.styleable#CollapsingToolbarLayout_toolbarId
 */
public class CollapsingToolbarLayout extends FrameLayout {

    private static final int DEFAULT_SCRIM_ANIMATION_DURATION = 600;

    private boolean mRefreshToolbar = true;
    private int mToolbarId;
    private Toolbar mToolbar;
    private boolean mToolbarAutoChangeAlpha = false;
    private View mToolbarDirectChild;
    private View mDummyView;

    private int mExpandedMarginStart;
    private int mExpandedMarginTop;
    private int mExpandedMarginEnd;
    private int mExpandedMarginBottom;

    private final Rect mTmpRect = new Rect();
    final CollapsingTextHelper mCollapsingTextHelper;
    private boolean mCollapsingTitleEnabled;
    private boolean mCollapsingTitleScalable;
    private boolean mDrawCollapsingTitle;

    private Drawable mContentScrim;
    Drawable mStatusBarScrim;
    private int mScrimAlpha;
    private boolean mScrimsAreShown;
    private ValueAnimator mScrimAnimator;
    private long mScrimAnimationDuration;
    private int mScrimVisibleHeightTrigger = -1;

    private AppBarLayout.OnOffsetChangedListener mOnOffsetChangedListener;
    private OnFractionChangeListener mOnFractionChangeListener;

    int mCurrentOffset;

    WindowInsetsCompat mLastInsets;

    public CollapsingToolbarLayout(Context context) {
        this(context, null);
    }

    public CollapsingToolbarLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CollapsingToolbarLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        ThemeUtils.checkAppCompatTheme(context);

        mCollapsingTextHelper = new CollapsingTextHelper(this);
        mCollapsingTextHelper.setTextSizeInterpolator(AnimationUtils.DECELERATE_INTERPOLATOR);

        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.CollapsingToolbarLayout, defStyleAttr,
                R.style.Widget_Design_CollapsingToolbar);

        mCollapsingTextHelper.setExpandedTextGravity(
                a.getInt(R.styleable.CollapsingToolbarLayout_expandedTitleGravity,
                        GravityCompat.START | Gravity.BOTTOM));
        mCollapsingTextHelper.setCollapsedTextGravity(
                a.getInt(R.styleable.CollapsingToolbarLayout_collapsedTitleGravity,
                        GravityCompat.START | Gravity.CENTER_VERTICAL));

        mExpandedMarginStart = mExpandedMarginTop = mExpandedMarginEnd = mExpandedMarginBottom =
                a.getDimensionPixelSize(R.styleable.CollapsingToolbarLayout_expandedTitleMargin, 0);

        if (a.hasValue(R.styleable.CollapsingToolbarLayout_expandedTitleMarginStart)) {
            mExpandedMarginStart = a.getDimensionPixelSize(
                    R.styleable.CollapsingToolbarLayout_expandedTitleMarginStart, 0);
        }
        if (a.hasValue(R.styleable.CollapsingToolbarLayout_expandedTitleMarginEnd)) {
            mExpandedMarginEnd = a.getDimensionPixelSize(
                    R.styleable.CollapsingToolbarLayout_expandedTitleMarginEnd, 0);
        }
        if (a.hasValue(R.styleable.CollapsingToolbarLayout_expandedTitleMarginTop)) {
            mExpandedMarginTop = a.getDimensionPixelSize(
                    R.styleable.CollapsingToolbarLayout_expandedTitleMarginTop, 0);
        }
        if (a.hasValue(R.styleable.CollapsingToolbarLayout_expandedTitleMarginBottom)) {
            mExpandedMarginBottom = a.getDimensionPixelSize(
                    R.styleable.CollapsingToolbarLayout_expandedTitleMarginBottom, 0);
        }

        mCollapsingTitleEnabled = a.getBoolean(
                R.styleable.CollapsingToolbarLayout_titleEnabled, true);
        mCollapsingTitleScalable = a.getBoolean(
                R.styleable.CollapsingToolbarLayout_titleScalable, true);
        mToolbarAutoChangeAlpha = a.getBoolean(
                R.styleable.CollapsingToolbarLayout_toolbarAutoChangeAlpha, false);
        setTitle(a.getText(R.styleable.CollapsingToolbarLayout_title));
        mCollapsingTextHelper.setCollapsingTitleScalable(mCollapsingTitleScalable);
        // First load the default text appearances
        mCollapsingTextHelper.setExpandedTextAppearance(
                R.style.TextAppearance_Design_CollapsingToolbar_Expanded);
        mCollapsingTextHelper.setCollapsedTextAppearance(
                android.support.v7.appcompat.R.style.TextAppearance_AppCompat_Widget_ActionBar_Title);

        // Now overlay any custom text appearances
        if (a.hasValue(R.styleable.CollapsingToolbarLayout_expandedTitleTextAppearance)) {
            mCollapsingTextHelper.setExpandedTextAppearance(
                    a.getResourceId(
                            R.styleable.CollapsingToolbarLayout_expandedTitleTextAppearance, 0));
        }
        if (a.hasValue(R.styleable.CollapsingToolbarLayout_collapsedTitleTextAppearance)) {
            mCollapsingTextHelper.setCollapsedTextAppearance(
                    a.getResourceId(
                            R.styleable.CollapsingToolbarLayout_collapsedTitleTextAppearance, 0));
        }

        mScrimVisibleHeightTrigger = a.getDimensionPixelSize(
                R.styleable.CollapsingToolbarLayout_scrimVisibleHeightTrigger, -1);

        mScrimAnimationDuration = a.getInt(
                R.styleable.CollapsingToolbarLayout_scrimAnimationDuration,
                DEFAULT_SCRIM_ANIMATION_DURATION);

        setContentScrim(a.getDrawable(R.styleable.CollapsingToolbarLayout_contentScrim));
        setStatusBarScrim(a.getDrawable(R.styleable.CollapsingToolbarLayout_statusBarScrim));

        mToolbarId = a.getResourceId(R.styleable.CollapsingToolbarLayout_toolbarId, -1);

        a.recycle();

        setWillNotDraw(false);

        ViewCompat.setOnApplyWindowInsetsListener(this,
                new android.support.v4.view.OnApplyWindowInsetsListener() {
                    @Override
                    public WindowInsetsCompat onApplyWindowInsets(View v,
                                                                  WindowInsetsCompat insets) {
                        return onWindowInsetChanged(insets);
                    }
                });
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        // Add an OnOffsetChangedListener if possible
        final ViewParent parent = getParent();
        if (parent instanceof AppBarLayout) {
            // Copy over from the ABL whether we should fit system windows
            ViewCompat.setFitsSystemWindows(this, ViewCompat.getFitsSystemWindows((View) parent));

            if (mOnOffsetChangedListener == null) {
                mOnOffsetChangedListener = new OffsetUpdateListener();
            }
            ((AppBarLayout) parent).addOnOffsetChangedListener(mOnOffsetChangedListener);

            // We're attached, so lets request an inset dispatch
            ViewCompat.requestApplyInsets(this);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        // Remove our OnOffsetChangedListener if possible and it exists
        final ViewParent parent = getParent();
        if (mOnOffsetChangedListener != null && parent instanceof AppBarLayout) {
            ((AppBarLayout) parent).removeOnOffsetChangedListener(mOnOffsetChangedListener);
        }

        super.onDetachedFromWindow();
    }

    WindowInsetsCompat onWindowInsetChanged(final WindowInsetsCompat insets) {
        WindowInsetsCompat newInsets = null;

        if (ViewCompat.getFitsSystemWindows(this)) {
            // If we're set to fit system windows, keep the insets
            newInsets = insets;
        }

        // If our insets have changed, keep them and invalidate the scroll ranges...
        if (!ObjectsCompat.equals(mLastInsets, newInsets)) {
            mLastInsets = newInsets;
            requestLayout();
        }

        // Consume the insets. This is done so that child views with fitSystemWindows=true do not
        // get the default padding functionality from View
        return insets.consumeSystemWindowInsets();
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        // If we don't have a toolbar, the scrim will be not be drawn in drawChild() below.
        // Instead, we draw it here, before our collapsing text.
        ensureToolbar();
        if (mToolbar == null && mContentScrim != null && mScrimAlpha > 0) {
            mContentScrim.mutate().setAlpha(mScrimAlpha);
            mContentScrim.draw(canvas);
        }

        // Let the collapsing text helper draw its text
        if (mCollapsingTitleEnabled && mDrawCollapsingTitle) {
            mCollapsingTextHelper.draw(canvas);
        }

        // Now draw the status bar scrim
        if (mStatusBarScrim != null && mScrimAlpha > 0) {
            final int topInset = mLastInsets != null ? mLastInsets.getSystemWindowInsetTop() : 0;
            if (topInset > 0) {
                mStatusBarScrim.setBounds(0, -mCurrentOffset, getWidth(),
                        topInset - mCurrentOffset);
                mStatusBarScrim.mutate().setAlpha(mScrimAlpha);
                mStatusBarScrim.draw(canvas);
            }
        }
    }

    @Override
    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
        // This is a little weird. Our scrim needs to be behind the Toolbar (if it is present),
        // but in front of any other children which are behind it. To do this we intercept the
        // drawChild() call, and draw our scrim just before the Toolbar is drawn
        boolean invalidated = false;
        if (mContentScrim != null && mScrimAlpha > 0 && isToolbarChild(child)) {
            mContentScrim.mutate().setAlpha(mScrimAlpha);
            mContentScrim.draw(canvas);
            invalidated = true;
        }
        return super.drawChild(canvas, child, drawingTime) || invalidated;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (mContentScrim != null) {
            mContentScrim.setBounds(0, 0, w, h);
        }
    }

    private void ensureToolbar() {
        if (!mRefreshToolbar) {
            return;
        }

        // First clear out the current Toolbar
        mToolbar = null;
        mToolbarDirectChild = null;

        if (mToolbarId != -1) {
            // If we have an ID set, try and find it and it's direct parent to us
            mToolbar = findViewById(mToolbarId);
            if (mToolbar != null) {
                mToolbarDirectChild = findDirectChild(mToolbar);
            }
        }

        if (mToolbar == null) {
            // If we don't have an ID, or couldn't find a Toolbar with the correct ID, try and find
            // one from our direct children
            Toolbar toolbar = null;
            for (int i = 0, count = getChildCount(); i < count; i++) {
                final View child = getChildAt(i);
                if (child instanceof Toolbar) {
                    toolbar = (Toolbar) child;
                    break;
                }
            }
            mToolbar = toolbar;
        }

        updateDummyView();
        mRefreshToolbar = false;
    }

    private boolean isToolbarChild(View child) {
        return (mToolbarDirectChild == null || mToolbarDirectChild == this)
                ? child == mToolbar
                : child == mToolbarDirectChild;
    }

    /**
     * Returns the direct child of this layout, which itself is the ancestor of the
     * given view.
     */
    private View findDirectChild(final View descendant) {
        View directChild = descendant;
        for (ViewParent p = descendant.getParent(); p != this && p != null; p = p.getParent()) {
            if (p instanceof View) {
                directChild = (View) p;
            }
        }
        return directChild;
    }

    private void updateDummyView() {
        if (!mCollapsingTitleEnabled && mDummyView != null) {
            // If we have a dummy view and we have our title disabled, remove it from its parent
            final ViewParent parent = mDummyView.getParent();
            if (parent instanceof ViewGroup) {
                ((ViewGroup) parent).removeView(mDummyView);
            }
        }
        if (mCollapsingTitleEnabled && mToolbar != null) {
            if (mDummyView == null) {
                mDummyView = new View(getContext());
            }
            if (mDummyView.getParent() == null) {
                mToolbar.addView(mDummyView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        ensureToolbar();
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        final int mode = MeasureSpec.getMode(heightMeasureSpec);
        final int topInset = mLastInsets != null ? mLastInsets.getSystemWindowInsetTop() : 0;
        if (mode == MeasureSpec.UNSPECIFIED && topInset > 0) {
            // If we have a top inset and we're set to wrap_content height we need to make sure
            // we add the top inset to our height, therefore we re-measure
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(
                    getMeasuredHeight() + topInset, MeasureSpec.EXACTLY);
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        if (mLastInsets != null) {
            // Shift down any views which are not set to fit system windows
            final int insetTop = mLastInsets.getSystemWindowInsetTop();
            for (int i = 0, z = getChildCount(); i < z; i++) {
                final View child = getChildAt(i);
                if (!ViewCompat.getFitsSystemWindows(child)) {
                    if (child.getTop() < insetTop) {
                        // If the child isn't set to fit system windows but is drawing within
                        // the inset offset it down
                        ViewCompat.offsetTopAndBottom(child, insetTop);
                    }
                }
            }
        }

        // Update the collapsed bounds by getting it's transformed bounds
        if (mCollapsingTitleEnabled && mDummyView != null) {
            // We only draw the title if the dummy view is being displayed (Toolbar removes
            // views if there is no space)
            mDrawCollapsingTitle = ViewCompat.isAttachedToWindow(mDummyView)
                    && mDummyView.getVisibility() == VISIBLE;

            if (mDrawCollapsingTitle) {
                final boolean isRtl = ViewCompat.getLayoutDirection(this)
                        == ViewCompat.LAYOUT_DIRECTION_RTL;

                // Update the collapsed bounds
                final int maxOffset = getMaxOffsetForPinChild(
                        mToolbarDirectChild != null ? mToolbarDirectChild : mToolbar);
                ViewGroupUtils.getDescendantRect(this, mDummyView, mTmpRect);
                mCollapsingTextHelper.setCollapsedBounds(
                        mTmpRect.left + (isRtl
                                ? mToolbar.getTitleMarginEnd()
                                : mToolbar.getTitleMarginStart()),
                        mTmpRect.top + maxOffset + mToolbar.getTitleMarginTop(),
                        mTmpRect.right + (isRtl
                                ? mToolbar.getTitleMarginStart()
                                : mToolbar.getTitleMarginEnd()),
                        mTmpRect.bottom + maxOffset - mToolbar.getTitleMarginBottom());

                // Update the expanded bounds
                mCollapsingTextHelper.setExpandedBounds(
                        isRtl ? mExpandedMarginEnd : mExpandedMarginStart,
                        mTmpRect.top + mExpandedMarginTop,
                        right - left - (isRtl ? mExpandedMarginStart : mExpandedMarginEnd),
                        bottom - top - mExpandedMarginBottom);

                // Now recalculate using the new bounds
                mCollapsingTextHelper.recalculate();
            }
        }

        // Update our child view offset helpers. This needs to be done after the title has been
        // setup, so that any Toolbars are in their original position
        for (int i = 0, z = getChildCount(); i < z; i++) {
            getViewOffsetHelper(getChildAt(i)).onViewLayout();
        }

        // Finally, set our minimum height to enable proper AppBarLayout collapsing
        if (mToolbar != null) {
            if (mCollapsingTitleEnabled && TextUtils.isEmpty(mCollapsingTextHelper.getText())) {
                // If we do not currently have a title, try and grab it from the Toolbar
                mCollapsingTextHelper.setText(mToolbar.getTitle());
            }
            if (mToolbarDirectChild == null || mToolbarDirectChild == this) {
                setMinimumHeight(getHeightWithMargins(mToolbar));
            } else {
                setMinimumHeight(getHeightWithMargins(mToolbarDirectChild));
            }
        }

        updateScrimVisibility();
    }

    private static int getHeightWithMargins(@NonNull final View view) {
        final ViewGroup.LayoutParams lp = view.getLayoutParams();
        if (lp instanceof MarginLayoutParams) {
            final MarginLayoutParams mlp = (MarginLayoutParams) lp;
            return view.getHeight() + mlp.topMargin + mlp.bottomMargin;
        }
        return view.getHeight();
    }

    static ViewOffsetHelper getViewOffsetHelper(View view) {
        ViewOffsetHelper offsetHelper = (ViewOffsetHelper) view.getTag(R.id.view_offset_helper);
        if (offsetHelper == null) {
            offsetHelper = new ViewOffsetHelper(view);
            view.setTag(R.id.view_offset_helper, offsetHelper);
        }
        return offsetHelper;
    }

    /**
     * Sets the title to be displayed by this view, if enabled.
     *
     * @attr ref R.styleable#CollapsingToolbarLayout_title
     * @see #setTitleEnabled(boolean)
     * @see #getTitle()
     */
    public void setTitle(@Nullable CharSequence title) {
        mCollapsingTextHelper.setText(title);
    }

    /**
     * Returns the title currently being displayed by this view. If the title is not enabled, then
     * this will return {@code null}.
     *
     * @attr ref R.styleable#CollapsingToolbarLayout_title
     */
    @Nullable
    public CharSequence getTitle() {
        return mCollapsingTitleEnabled ? mCollapsingTextHelper.getText() : null;
    }

    /**
     * Sets whether this view should display its own title.
     *
     * <p>The title displayed by this view will shrink and grow based on the scroll offset.</p>
     *
     * @attr ref R.styleable#CollapsingToolbarLayout_titleEnabled
     * @see #setTitle(CharSequence)
     * @see #isTitleEnabled()
     */
    public void setTitleEnabled(boolean enabled) {
        if (enabled != mCollapsingTitleEnabled) {
            mCollapsingTitleEnabled = enabled;
            updateDummyView();
            requestLayout();
        }
    }

    /**
     * Returns whether this view is currently displaying its own title.
     *
     * @attr ref R.styleable#CollapsingToolbarLayout_titleEnabled
     * @see #setTitleEnabled(boolean)
     */
    public boolean isTitleEnabled() {
        return mCollapsingTitleEnabled;
    }

    /**
     * Set whether the content scrim and/or status bar scrim should be shown or not. Any change
     * in the vertical scroll may overwrite this value. Any visibility change will be animated if
     * this view has already been laid out.
     *
     * @param shown whether the scrims should be shown
     * @see #getStatusBarScrim()
     * @see #getContentScrim()
     */
    public void setScrimsShown(boolean shown) {
        setScrimsShown(shown, ViewCompat.isLaidOut(this) && !isInEditMode());
    }

    /**
     * Set whether the content scrim and/or status bar scrim should be shown or not. Any change
     * in the vertical scroll may overwrite this value.
     *
     * @param shown   whether the scrims should be shown
     * @param animate whether to animate the visibility change
     * @see #getStatusBarScrim()
     * @see #getContentScrim()
     */
    public void setScrimsShown(boolean shown, boolean animate) {
        if (mScrimsAreShown != shown) {
            if (animate) {
                animateScrim(shown ? 0xFF : 0x0);
            } else {
                setScrimAlpha(shown ? 0xFF : 0x0);
            }
            mScrimsAreShown = shown;
        }
    }

    private void animateScrim(int targetAlpha) {
        ensureToolbar();
        if (mScrimAnimator == null) {
            mScrimAnimator = new ValueAnimator();
            mScrimAnimator.setDuration(mScrimAnimationDuration);
            mScrimAnimator.setInterpolator(
                    targetAlpha > mScrimAlpha
                            ? AnimationUtils.FAST_OUT_LINEAR_IN_INTERPOLATOR
                            : AnimationUtils.LINEAR_OUT_SLOW_IN_INTERPOLATOR);
            mScrimAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animator) {
                    setScrimAlpha((int) animator.getAnimatedValue());
                }
            });
        } else if (mScrimAnimator.isRunning()) {
            mScrimAnimator.cancel();
        }

        mScrimAnimator.setIntValues(mScrimAlpha, targetAlpha);
        mScrimAnimator.start();
    }

    void setScrimAlpha(int alpha) {
        if (alpha != mScrimAlpha) {
            final Drawable contentScrim = mContentScrim;
            if (contentScrim != null && mToolbar != null) {
                ViewCompat.postInvalidateOnAnimation(mToolbar);
            }
            mScrimAlpha = alpha;
            ViewCompat.postInvalidateOnAnimation(CollapsingToolbarLayout.this);
        }
    }

    int getScrimAlpha() {
        return mScrimAlpha;
    }

    /**
     * Set the drawable to use for the content scrim from resources. Providing null will disable
     * the scrim functionality.
     *
     * @param drawable the drawable to display
     * @attr ref R.styleable#CollapsingToolbarLayout_contentScrim
     * @see #getContentScrim()
     */
    public void setContentScrim(@Nullable Drawable drawable) {
        if (mContentScrim != drawable) {
            if (mContentScrim != null) {
                mContentScrim.setCallback(null);
            }
            mContentScrim = drawable != null ? drawable.mutate() : null;
            if (mContentScrim != null) {
                mContentScrim.setBounds(0, 0, getWidth(), getHeight());
                mContentScrim.setCallback(this);
                mContentScrim.setAlpha(mScrimAlpha);
            }
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    /**
     * Set the color to use for the content scrim.
     *
     * @param color the color to display
     * @attr ref R.styleable#CollapsingToolbarLayout_contentScrim
     * @see #getContentScrim()
     */
    public void setContentScrimColor(@ColorInt int color) {
        setContentScrim(new ColorDrawable(color));
    }

    /**
     * Set the drawable to use for the content scrim from resources.
     *
     * @param resId drawable resource id
     * @attr ref R.styleable#CollapsingToolbarLayout_contentScrim
     * @see #getContentScrim()
     */
    public void setContentScrimResource(@DrawableRes int resId) {
        setContentScrim(ContextCompat.getDrawable(getContext(), resId));

    }

    /**
     * Returns the drawable which is used for the foreground scrim.
     *
     * @attr ref R.styleable#CollapsingToolbarLayout_contentScrim
     * @see #setContentScrim(Drawable)
     */
    @Nullable
    public Drawable getContentScrim() {
        return mContentScrim;
    }

    /**
     * Set the drawable to use for the status bar scrim from resources.
     * Providing null will disable the scrim functionality.
     *
     * <p>This scrim is only shown when we have been given a top system inset.</p>
     *
     * @param drawable the drawable to display
     * @attr ref R.styleable#CollapsingToolbarLayout_statusBarScrim
     * @see #getStatusBarScrim()
     */
    public void setStatusBarScrim(@Nullable Drawable drawable) {
        if (mStatusBarScrim != drawable) {
            if (mStatusBarScrim != null) {
                mStatusBarScrim.setCallback(null);
            }
            mStatusBarScrim = drawable != null ? drawable.mutate() : null;
            if (mStatusBarScrim != null) {
                if (mStatusBarScrim.isStateful()) {
                    mStatusBarScrim.setState(getDrawableState());
                }
                DrawableCompat.setLayoutDirection(mStatusBarScrim,
                        ViewCompat.getLayoutDirection(this));
                mStatusBarScrim.setVisible(getVisibility() == VISIBLE, false);
                mStatusBarScrim.setCallback(this);
                mStatusBarScrim.setAlpha(mScrimAlpha);
            }
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();

        final int[] state = getDrawableState();
        boolean changed = false;

        Drawable d = mStatusBarScrim;
        if (d != null && d.isStateful()) {
            changed |= d.setState(state);
        }
        d = mContentScrim;
        if (d != null && d.isStateful()) {
            changed |= d.setState(state);
        }
        if (mCollapsingTextHelper != null) {
            changed |= mCollapsingTextHelper.setState(state);
        }

        if (changed) {
            invalidate();
        }
    }

    @Override
    protected boolean verifyDrawable(Drawable who) {
        return super.verifyDrawable(who) || who == mContentScrim || who == mStatusBarScrim;
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);

        final boolean visible = visibility == VISIBLE;
        if (mStatusBarScrim != null && mStatusBarScrim.isVisible() != visible) {
            mStatusBarScrim.setVisible(visible, false);
        }
        if (mContentScrim != null && mContentScrim.isVisible() != visible) {
            mContentScrim.setVisible(visible, false);
        }
    }

    /**
     * Set the color to use for the status bar scrim.
     *
     * <p>This scrim is only shown when we have been given a top system inset.</p>
     *
     * @param color the color to display
     * @attr ref R.styleable#CollapsingToolbarLayout_statusBarScrim
     * @see #getStatusBarScrim()
     */
    public void setStatusBarScrimColor(@ColorInt int color) {
        setStatusBarScrim(new ColorDrawable(color));
    }

    /**
     * Set the drawable to use for the content scrim from resources.
     *
     * @param resId drawable resource id
     * @attr ref R.styleable#CollapsingToolbarLayout_statusBarScrim
     * @see #getStatusBarScrim()
     */
    public void setStatusBarScrimResource(@DrawableRes int resId) {
        setStatusBarScrim(ContextCompat.getDrawable(getContext(), resId));
    }

    /**
     * Returns the drawable which is used for the status bar scrim.
     *
     * @attr ref R.styleable#CollapsingToolbarLayout_statusBarScrim
     * @see #setStatusBarScrim(Drawable)
     */
    @Nullable
    public Drawable getStatusBarScrim() {
        return mStatusBarScrim;
    }

    /**
     * Sets the text color and size for the collapsed title from the specified
     * TextAppearance resource.
     *
     * @attr ref android.support.design.R.styleable#CollapsingToolbarLayout_collapsedTitleTextAppearance
     */
    public void setCollapsedTitleTextAppearance(@StyleRes int resId) {
        mCollapsingTextHelper.setCollapsedTextAppearance(resId);
    }

    /**
     * Sets the text color of the collapsed title.
     *
     * @param color The new text color in ARGB format
     */
    public void setCollapsedTitleTextColor(@ColorInt int color) {
        setCollapsedTitleTextColor(ColorStateList.valueOf(color));
    }

    /**
     * Sets the text colors of the collapsed title.
     *
     * @param colors ColorStateList containing the new text colors
     */
    public void setCollapsedTitleTextColor(@NonNull ColorStateList colors) {
        mCollapsingTextHelper.setCollapsedTextColor(colors);
    }

    /**
     * Sets the horizontal alignment of the collapsed title and the vertical gravity that will
     * be used when there is extra space in the collapsed bounds beyond what is required for
     * the title itself.
     *
     * @attr ref android.support.design.R.styleable#CollapsingToolbarLayout_collapsedTitleGravity
     */
    public void setCollapsedTitleGravity(int gravity) {
        mCollapsingTextHelper.setCollapsedTextGravity(gravity);
    }

    /**
     * Returns the horizontal and vertical alignment for title when collapsed.
     *
     * @attr ref android.support.design.R.styleable#CollapsingToolbarLayout_collapsedTitleGravity
     */
    public int getCollapsedTitleGravity() {
        return mCollapsingTextHelper.getCollapsedTextGravity();
    }

    /**
     * Sets the text color and size for the expanded title from the specified
     * TextAppearance resource.
     *
     * @attr ref android.support.design.R.styleable#CollapsingToolbarLayout_expandedTitleTextAppearance
     */
    public void setExpandedTitleTextAppearance(@StyleRes int resId) {
        mCollapsingTextHelper.setExpandedTextAppearance(resId);
    }

    /**
     * Sets the text color of the expanded title.
     *
     * @param color The new text color in ARGB format
     */
    public void setExpandedTitleColor(@ColorInt int color) {
        setExpandedTitleTextColor(ColorStateList.valueOf(color));
    }

    /**
     * Sets the text colors of the expanded title.
     *
     * @param colors ColorStateList containing the new text colors
     */
    public void setExpandedTitleTextColor(@NonNull ColorStateList colors) {
        mCollapsingTextHelper.setExpandedTextColor(colors);
    }

    /**
     * Sets the horizontal alignment of the expanded title and the vertical gravity that will
     * be used when there is extra space in the expanded bounds beyond what is required for
     * the title itself.
     *
     * @attr ref android.support.design.R.styleable#CollapsingToolbarLayout_expandedTitleGravity
     */
    public void setExpandedTitleGravity(int gravity) {
        mCollapsingTextHelper.setExpandedTextGravity(gravity);
    }

    /**
     * Returns the horizontal and vertical alignment for title when expanded.
     *
     * @attr ref android.support.design.R.styleable#CollapsingToolbarLayout_expandedTitleGravity
     */
    public int getExpandedTitleGravity() {
        return mCollapsingTextHelper.getExpandedTextGravity();
    }

    /**
     * Set the typeface to use for the collapsed title.
     *
     * @param typeface typeface to use, or {@code null} to use the default.
     */
    public void setCollapsedTitleTypeface(@Nullable Typeface typeface) {
        mCollapsingTextHelper.setCollapsedTypeface(typeface);
    }

    /**
     * Returns the typeface used for the collapsed title.
     */
    @NonNull
    public Typeface getCollapsedTitleTypeface() {
        return mCollapsingTextHelper.getCollapsedTypeface();
    }

    /**
     * Set the typeface to use for the expanded title.
     *
     * @param typeface typeface to use, or {@code null} to use the default.
     */
    public void setExpandedTitleTypeface(@Nullable Typeface typeface) {
        mCollapsingTextHelper.setExpandedTypeface(typeface);
    }

    /**
     * Returns the typeface used for the expanded title.
     */
    @NonNull
    public Typeface getExpandedTitleTypeface() {
        return mCollapsingTextHelper.getExpandedTypeface();
    }

    /**
     * Sets the expanded title margins.
     *
     * @param start  the starting title margin in pixels
     * @param top    the top title margin in pixels
     * @param end    the ending title margin in pixels
     * @param bottom the bottom title margin in pixels
     * @attr ref android.support.design.R.styleable#CollapsingToolbarLayout_expandedTitleMargin
     * @see #getExpandedTitleMarginStart()
     * @see #getExpandedTitleMarginTop()
     * @see #getExpandedTitleMarginEnd()
     * @see #getExpandedTitleMarginBottom()
     */
    public void setExpandedTitleMargin(int start, int top, int end, int bottom) {
        mExpandedMarginStart = start;
        mExpandedMarginTop = top;
        mExpandedMarginEnd = end;
        mExpandedMarginBottom = bottom;
        requestLayout();
    }

    /**
     * @return the starting expanded title margin in pixels
     * @attr ref android.support.design.R.styleable#CollapsingToolbarLayout_expandedTitleMarginStart
     * @see #setExpandedTitleMarginStart(int)
     */
    public int getExpandedTitleMarginStart() {
        return mExpandedMarginStart;
    }

    /**
     * Sets the starting expanded title margin in pixels.
     *
     * @param margin the starting title margin in pixels
     * @attr ref android.support.design.R.styleable#CollapsingToolbarLayout_expandedTitleMarginStart
     * @see #getExpandedTitleMarginStart()
     */
    public void setExpandedTitleMarginStart(int margin) {
        mExpandedMarginStart = margin;
        requestLayout();
    }

    /**
     * @return the top expanded title margin in pixels
     * @attr ref android.support.design.R.styleable#CollapsingToolbarLayout_expandedTitleMarginTop
     * @see #setExpandedTitleMarginTop(int)
     */
    public int getExpandedTitleMarginTop() {
        return mExpandedMarginTop;
    }

    /**
     * Sets the top expanded title margin in pixels.
     *
     * @param margin the top title margin in pixels
     * @attr ref android.support.design.R.styleable#CollapsingToolbarLayout_expandedTitleMarginTop
     * @see #getExpandedTitleMarginTop()
     */
    public void setExpandedTitleMarginTop(int margin) {
        mExpandedMarginTop = margin;
        requestLayout();
    }

    /**
     * @return the ending expanded title margin in pixels
     * @attr ref android.support.design.R.styleable#CollapsingToolbarLayout_expandedTitleMarginEnd
     * @see #setExpandedTitleMarginEnd(int)
     */
    public int getExpandedTitleMarginEnd() {
        return mExpandedMarginEnd;
    }

    /**
     * Sets the ending expanded title margin in pixels.
     *
     * @param margin the ending title margin in pixels
     * @attr ref android.support.design.R.styleable#CollapsingToolbarLayout_expandedTitleMarginEnd
     * @see #getExpandedTitleMarginEnd()
     */
    public void setExpandedTitleMarginEnd(int margin) {
        mExpandedMarginEnd = margin;
        requestLayout();
    }

    /**
     * @return the bottom expanded title margin in pixels
     * @attr ref android.support.design.R.styleable#CollapsingToolbarLayout_expandedTitleMarginBottom
     * @see #setExpandedTitleMarginBottom(int)
     */
    public int getExpandedTitleMarginBottom() {
        return mExpandedMarginBottom;
    }

    /**
     * Sets the bottom expanded title margin in pixels.
     *
     * @param margin the bottom title margin in pixels
     * @attr ref android.support.design.R.styleable#CollapsingToolbarLayout_expandedTitleMarginBottom
     * @see #getExpandedTitleMarginBottom()
     */
    public void setExpandedTitleMarginBottom(int margin) {
        mExpandedMarginBottom = margin;
        requestLayout();
    }

    /**
     * Set the amount of visible height in pixels used to define when to trigger a scrim
     * visibility change.
     *
     * <p>If the visible height of this view is less than the given value, the scrims will be
     * made visible, otherwise they are hidden.</p>
     *
     * @param height value in pixels used to define when to trigger a scrim visibility change
     * @attr ref android.support.design.R.styleable#CollapsingToolbarLayout_scrimVisibleHeightTrigger
     */
    public void setScrimVisibleHeightTrigger(@IntRange(from = 0) final int height) {
        if (mScrimVisibleHeightTrigger != height) {
            mScrimVisibleHeightTrigger = height;
            // Update the scrim visibility
            updateScrimVisibility();
        }
    }

    /**
     * Returns the amount of visible height in pixels used to define when to trigger a scrim
     * visibility change.
     *
     * @see #setScrimVisibleHeightTrigger(int)
     */
    public int getScrimVisibleHeightTrigger() {
        if (mScrimVisibleHeightTrigger >= 0) {
            // If we have one explicitly set, return it
            return mScrimVisibleHeightTrigger;
        }

        // Otherwise we'll use the default computed value
        final int insetTop = mLastInsets != null ? mLastInsets.getSystemWindowInsetTop() : 0;

        final int minHeight = ViewCompat.getMinimumHeight(this);
        if (minHeight > 0) {
            // If we have a minHeight set, lets use 2 * minHeight (capped at our height)
            return Math.min((minHeight * 2) + insetTop, getHeight());
        }

        // If we reach here then we don't have a min height set. Instead we'll take a
        // guess at 1/3 of our height being visible
        return getHeight() / 3;
    }

    /**
     * Set the duration used for scrim visibility animations.
     *
     * @param duration the duration to use in milliseconds
     * @attr ref android.support.design.R.styleable#CollapsingToolbarLayout_scrimAnimationDuration
     */
    public void setScrimAnimationDuration(@IntRange(from = 0) final long duration) {
        mScrimAnimationDuration = duration;
    }

    /**
     * Returns the duration in milliseconds used for scrim visibility animations.
     */
    public long getScrimAnimationDuration() {
        return mScrimAnimationDuration;
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    @Override
    public FrameLayout.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    @Override
    protected FrameLayout.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }

    public static class LayoutParams extends FrameLayout.LayoutParams {

        private static final float DEFAULT_PARALLAX_MULTIPLIER = 0.5f;

        /**
         * @hide
         */
        @RestrictTo(LIBRARY_GROUP)
        @IntDef({
                COLLAPSE_MODE_OFF,
                COLLAPSE_MODE_PIN,
                COLLAPSE_MODE_PARALLAX
        })
        @Retention(RetentionPolicy.SOURCE)
        @interface CollapseMode {
        }

        /**
         * The view will act as normal with no collapsing behavior.
         */
        public static final int COLLAPSE_MODE_OFF = 0;

        /**
         * The view will pin in place until it reaches the bottom of the
         * {@link CollapsingToolbarLayout}.
         */
        public static final int COLLAPSE_MODE_PIN = 1;

        /**
         * The view will scroll in a parallax fashion. See {@link #setParallaxMultiplier(float)}
         * to change the multiplier used.
         */
        public static final int COLLAPSE_MODE_PARALLAX = 2;

        int mCollapseMode = COLLAPSE_MODE_OFF;
        boolean mAutoChangeAlpha = false;
        boolean mAutoChangeChildrenAlpha = false;
        float mParallaxMult = DEFAULT_PARALLAX_MULTIPLIER;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);

            TypedArray a = c.obtainStyledAttributes(attrs,
                    R.styleable.CollapsingToolbarLayout_Layout);
            mCollapseMode = a.getInt(R.styleable.CollapsingToolbarLayout_Layout_layout_collapseMode, COLLAPSE_MODE_OFF);
            mAutoChangeAlpha = a.getBoolean(R.styleable.CollapsingToolbarLayout_Layout_layout_autoChangeAlpha, false);
            mAutoChangeChildrenAlpha = a.getBoolean(R.styleable.CollapsingToolbarLayout_Layout_layout_autoChangeChildrenAlpha, false);
            setParallaxMultiplier(a.getFloat(
                    R.styleable.CollapsingToolbarLayout_Layout_layout_collapseParallaxMultiplier,
                    DEFAULT_PARALLAX_MULTIPLIER));
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

        /**
         * Set the collapse mode.
         *
         * @param collapseMode one of {@link #COLLAPSE_MODE_OFF}, {@link #COLLAPSE_MODE_PIN}
         *                     or {@link #COLLAPSE_MODE_PARALLAX}.
         */
        public void setCollapseMode(@CollapseMode int collapseMode) {
            mCollapseMode = collapseMode;
        }

        /**
         * Returns the requested collapse mode.
         *
         * @return the current mode. One of {@link #COLLAPSE_MODE_OFF}, {@link #COLLAPSE_MODE_PIN}
         * or {@link #COLLAPSE_MODE_PARALLAX}.
         */
        @CollapseMode
        public int getCollapseMode() {
            return mCollapseMode;
        }

        /**
         * Set the parallax scroll multiplier used in conjunction with
         * {@link #COLLAPSE_MODE_PARALLAX}. A value of {@code 0.0} indicates no movement at all,
         * {@code 1.0f} indicates normal scroll movement.
         *
         * @param multiplier the multiplier.
         * @see #getParallaxMultiplier()
         */
        public void setParallaxMultiplier(float multiplier) {
            mParallaxMult = multiplier;
        }

        /**
         * Returns the parallax scroll multiplier used in conjunction with
         * {@link #COLLAPSE_MODE_PARALLAX}.
         *
         * @see #setParallaxMultiplier(float)
         */
        public float getParallaxMultiplier() {
            return mParallaxMult;
        }
    }

    /**
     * Show or hide the scrims if needed
     */
    final void updateScrimVisibility() {
        if (mContentScrim != null || mStatusBarScrim != null) {
            setScrimsShown(getHeight() + mCurrentOffset < getScrimVisibleHeightTrigger());
        }
    }

    /**
     * Show or hide the scrims if needed
     */
    final void updateScrimVisibility(float fraction) {
        if (mContentScrim != null || mStatusBarScrim != null) {
            mScrimsAreShown = true;
            setScrimAlpha((int) fraction * 0xFF);
        }
    }

    final int getMaxOffsetForPinChild(View child) {
        final ViewOffsetHelper offsetHelper = getViewOffsetHelper(child);
        final LayoutParams lp = (LayoutParams) child.getLayoutParams();
        return getHeight()
                - offsetHelper.getLayoutTop()
                - child.getHeight()
                - lp.bottomMargin;
    }

    private class OffsetUpdateListener implements AppBarLayout.OnOffsetChangedListener {
        OffsetUpdateListener() {
        }

        @Override
        public void onOffsetChanged(AppBarLayout layout, int verticalOffset) {
            mCurrentOffset = verticalOffset;

            final int insetTop = mLastInsets != null ? mLastInsets.getSystemWindowInsetTop() : 0;
            final int expandRange = getHeight() - ViewCompat.getMinimumHeight(CollapsingToolbarLayout.this) - insetTop;
            float fraction = Math.abs(verticalOffset) / (float) expandRange;

            for (int i = 0, z = getChildCount(); i < z; i++) {
                final View child = getChildAt(i);
                final LayoutParams lp = (LayoutParams) child.getLayoutParams();
                final ViewOffsetHelper offsetHelper = getViewOffsetHelper(child);

                switch (lp.mCollapseMode) {
                    case LayoutParams.COLLAPSE_MODE_PIN:
                        offsetHelper.setTopAndBottomOffset(MathUtils.clamp(
                                -verticalOffset, 0, getMaxOffsetForPinChild(child)));
                        break;
                    case LayoutParams.COLLAPSE_MODE_PARALLAX:
                        offsetHelper.setTopAndBottomOffset(Math.round(-verticalOffset * lp.mParallaxMult));
                        break;
                }

                if (lp.mAutoChangeAlpha) {
                    child.setAlpha(1 - fraction);
                }

                if (lp.mAutoChangeChildrenAlpha) {
                    if (child instanceof ViewGroup) {
                        ViewGroup vg = (ViewGroup) child;
                        int childrenSize = vg.getChildCount();
                        for (int j = 0; j < childrenSize; j++) {
                            final View cv = vg.getChildAt(j);
                            cv.setAlpha(1 - fraction);
                        }
                    }
                }
            }

            // Show or hide the scrims if needed
            //updateScrimVisibility();

            if (mStatusBarScrim != null && insetTop > 0) {
                ViewCompat.postInvalidateOnAnimation(CollapsingToolbarLayout.this);
            }

            // Update the collapsing text's fraction

            // Show or hide the scrims if needed
            updateScrimVisibility(fraction);
            mCollapsingTextHelper.setExpansionFraction(fraction);
            if (mOnFractionChangeListener != null) {
                mOnFractionChangeListener.onFractionChange(fraction);
            }
            if (mToolbar != null && mToolbarAutoChangeAlpha) {
                mToolbar.setAlpha(fraction);
            }
        }
    }

    TextView mToolbarTitleView;

    public void setOnFractionChangeListener(OnFractionChangeListener listener) {
        mOnFractionChangeListener = listener;
    }


    interface OnFractionChangeListener {
        void onFractionChange(float fraction);
    }

}


final class CollapsingTextHelper {

    // Pre-JB-MR2 doesn't support HW accelerated canvas scaled text so we will workaround it
    // by using our own texture
    private static final boolean USE_SCALING_TEXTURE = Build.VERSION.SDK_INT < 18;

    private static final boolean DEBUG_DRAW = false;
    private static final Paint DEBUG_DRAW_PAINT;

    static {
        DEBUG_DRAW_PAINT = DEBUG_DRAW ? new Paint() : null;
        if (DEBUG_DRAW_PAINT != null) {
            DEBUG_DRAW_PAINT.setAntiAlias(true);
            DEBUG_DRAW_PAINT.setColor(Color.MAGENTA);
        }
    }

    private final View mView;

    private boolean mDrawTitle;
    private float mExpandedFraction;

    private final Rect mExpandedBounds;
    private final Rect mCollapsedBounds;
    private final RectF mCurrentBounds;
    private int mExpandedTextGravity = Gravity.CENTER_VERTICAL;
    private int mCollapsedTextGravity = Gravity.CENTER_VERTICAL;
    private float mExpandedTextSize = 15;
    private float mCollapsedTextSize = 15;
    private ColorStateList mExpandedTextColor;
    private ColorStateList mCollapsedTextColor;

    private float mExpandedDrawY;
    private float mCollapsedDrawY;
    private float mExpandedDrawX;
    private float mCollapsedDrawX;
    private float mCurrentDrawX;
    private float mCurrentDrawY;
    private Typeface mCollapsedTypeface;
    private Typeface mExpandedTypeface;
    private Typeface mCurrentTypeface;

    private CharSequence mText;
    private CharSequence mTextToDraw;
    private boolean mIsRtl;

    private boolean mUseTexture;
    private Bitmap mExpandedTitleTexture;
    private Paint mTexturePaint;
    private float mTextureAscent;
    private float mTextureDescent;

    private float mScale;
    private float mCurrentTextSize;

    private int[] mState;

    private boolean mBoundsChanged;

    private final TextPaint mTextPaint;

    private Interpolator mPositionInterpolator;
    private Interpolator mTextSizeInterpolator;

    private float mCollapsedShadowRadius, mCollapsedShadowDx, mCollapsedShadowDy;
    private int mCollapsedShadowColor;

    private float mExpandedShadowRadius, mExpandedShadowDx, mExpandedShadowDy;
    private int mExpandedShadowColor;
    private boolean mCollapsingTitleScalable = true;

    public CollapsingTextHelper(View view) {
        mView = view;

        mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG | Paint.SUBPIXEL_TEXT_FLAG);

        mCollapsedBounds = new Rect();
        mExpandedBounds = new Rect();
        mCurrentBounds = new RectF();
    }

    public void setCollapsingTitleScalable(boolean collapsingTitleScalable) {
        mCollapsingTitleScalable = collapsingTitleScalable;
    }

    void setTextSizeInterpolator(Interpolator interpolator) {
        mTextSizeInterpolator = interpolator;
        recalculate();
    }

    void setPositionInterpolator(Interpolator interpolator) {
        mPositionInterpolator = interpolator;
        recalculate();
    }

    void setExpandedTextSize(float textSize) {
        if (mExpandedTextSize != textSize) {
            mExpandedTextSize = textSize;
            recalculate();
        }
    }

    void setCollapsedTextSize(float textSize) {
        if (mCollapsedTextSize != textSize) {
            mCollapsedTextSize = textSize;
            recalculate();
        }
    }

    void setCollapsedTextColor(ColorStateList textColor) {
        if (mCollapsedTextColor != textColor) {
            mCollapsedTextColor = textColor;
            recalculate();
        }
    }

    void setExpandedTextColor(ColorStateList textColor) {
        if (mExpandedTextColor != textColor) {
            mExpandedTextColor = textColor;
            recalculate();
        }
    }

    void setExpandedBounds(int left, int top, int right, int bottom) {
        if (!rectEquals(mExpandedBounds, left, top, right, bottom)) {
            mExpandedBounds.set(left, top, right, bottom);
            mBoundsChanged = true;
            onBoundsChanged();
        }
    }

    void setCollapsedBounds(int left, int top, int right, int bottom) {
        if (!rectEquals(mCollapsedBounds, left, top, right, bottom)) {
            mCollapsedBounds.set(left, top, right, bottom);
            mBoundsChanged = true;
            onBoundsChanged();
        }
    }

    void onBoundsChanged() {
        mDrawTitle = mCollapsedBounds.width() > 0 && mCollapsedBounds.height() > 0
                && mExpandedBounds.width() > 0 && mExpandedBounds.height() > 0;
    }

    void setExpandedTextGravity(int gravity) {
        if (mExpandedTextGravity != gravity) {
            mExpandedTextGravity = gravity;
            recalculate();
        }
    }

    int getExpandedTextGravity() {
        return mExpandedTextGravity;
    }

    void setCollapsedTextGravity(int gravity) {
        if (mCollapsedTextGravity != gravity) {
            mCollapsedTextGravity = gravity;
            recalculate();
        }
    }

    int getCollapsedTextGravity() {
        return mCollapsedTextGravity;
    }

    void setCollapsedTextAppearance(int resId) {
        TintTypedArray a = TintTypedArray.obtainStyledAttributes(mView.getContext(), resId,
                android.support.v7.appcompat.R.styleable.TextAppearance);
        if (a.hasValue(android.support.v7.appcompat.R.styleable.TextAppearance_android_textColor)) {
            mCollapsedTextColor = a.getColorStateList(
                    android.support.v7.appcompat.R.styleable.TextAppearance_android_textColor);
        }
        if (a.hasValue(android.support.v7.appcompat.R.styleable.TextAppearance_android_textSize)) {
            mCollapsedTextSize = a.getDimensionPixelSize(
                    android.support.v7.appcompat.R.styleable.TextAppearance_android_textSize,
                    (int) mCollapsedTextSize);
        }
        mCollapsedShadowColor = a.getInt(
                android.support.v7.appcompat.R.styleable.TextAppearance_android_shadowColor, 0);
        mCollapsedShadowDx = a.getFloat(
                android.support.v7.appcompat.R.styleable.TextAppearance_android_shadowDx, 0);
        mCollapsedShadowDy = a.getFloat(
                android.support.v7.appcompat.R.styleable.TextAppearance_android_shadowDy, 0);
        mCollapsedShadowRadius = a.getFloat(
                android.support.v7.appcompat.R.styleable.TextAppearance_android_shadowRadius, 0);
        a.recycle();

        if (Build.VERSION.SDK_INT >= 16) {
            mCollapsedTypeface = readFontFamilyTypeface(resId);
        }

        recalculate();
    }

    void setExpandedTextAppearance(int resId) {
        TintTypedArray a = TintTypedArray.obtainStyledAttributes(mView.getContext(), resId,
                android.support.v7.appcompat.R.styleable.TextAppearance);
        if (a.hasValue(android.support.v7.appcompat.R.styleable.TextAppearance_android_textColor)) {
            mExpandedTextColor = a.getColorStateList(
                    android.support.v7.appcompat.R.styleable.TextAppearance_android_textColor);
        }
        if (a.hasValue(android.support.v7.appcompat.R.styleable.TextAppearance_android_textSize)) {
            mExpandedTextSize = a.getDimensionPixelSize(
                    android.support.v7.appcompat.R.styleable.TextAppearance_android_textSize,
                    (int) mExpandedTextSize);
        }
        mExpandedShadowColor = a.getInt(
                android.support.v7.appcompat.R.styleable.TextAppearance_android_shadowColor, 0);
        mExpandedShadowDx = a.getFloat(
                android.support.v7.appcompat.R.styleable.TextAppearance_android_shadowDx, 0);
        mExpandedShadowDy = a.getFloat(
                android.support.v7.appcompat.R.styleable.TextAppearance_android_shadowDy, 0);
        mExpandedShadowRadius = a.getFloat(
                android.support.v7.appcompat.R.styleable.TextAppearance_android_shadowRadius, 0);
        a.recycle();

        if (Build.VERSION.SDK_INT >= 16) {
            mExpandedTypeface = readFontFamilyTypeface(resId);
        }

        recalculate();
    }

    private Typeface readFontFamilyTypeface(int resId) {
        final TypedArray a = mView.getContext().obtainStyledAttributes(resId,
                new int[]{android.R.attr.fontFamily});
        try {
            final String family = a.getString(0);
            if (family != null) {
                return Typeface.create(family, Typeface.NORMAL);
            }
        } finally {
            a.recycle();
        }
        return null;
    }

    void setCollapsedTypeface(Typeface typeface) {
        if (areTypefacesDifferent(mCollapsedTypeface, typeface)) {
            mCollapsedTypeface = typeface;
            recalculate();
        }
    }

    void setExpandedTypeface(Typeface typeface) {
        if (areTypefacesDifferent(mExpandedTypeface, typeface)) {
            mExpandedTypeface = typeface;
            recalculate();
        }
    }

    void setTypefaces(Typeface typeface) {
        mCollapsedTypeface = mExpandedTypeface = typeface;
        recalculate();
    }

    Typeface getCollapsedTypeface() {
        return mCollapsedTypeface != null ? mCollapsedTypeface : Typeface.DEFAULT;
    }

    Typeface getExpandedTypeface() {
        return mExpandedTypeface != null ? mExpandedTypeface : Typeface.DEFAULT;
    }

    /**
     * Set the value indicating the current scroll value. This decides how much of the
     * background will be displayed, as well as the title metrics/positioning.
     * <p>
     * A value of {@code 0.0} indicates that the layout is fully expanded.
     * A value of {@code 1.0} indicates that the layout is fully collapsed.
     */
    void setExpansionFraction(float fraction) {
        fraction = MathUtils.clamp(fraction, 0f, 1f);

        if (fraction != mExpandedFraction) {
            mExpandedFraction = fraction;
            calculateCurrentOffsets();
        }
    }

    final boolean setState(final int[] state) {
        mState = state;

        if (isStateful()) {
            recalculate();
            return true;
        }

        return false;
    }

    final boolean isStateful() {
        return (mCollapsedTextColor != null && mCollapsedTextColor.isStateful())
                || (mExpandedTextColor != null && mExpandedTextColor.isStateful());
    }

    float getExpansionFraction() {
        return mExpandedFraction;
    }

    float getCollapsedTextSize() {
        return mCollapsedTextSize;
    }

    float getExpandedTextSize() {
        return mExpandedTextSize;
    }

    private void calculateCurrentOffsets() {
        calculateOffsets(mExpandedFraction);
    }

    private void calculateOffsets(final float fraction) {
        interpolateBounds(fraction);
        mCurrentDrawX = lerp(mExpandedDrawX, mCollapsedDrawX, fraction,
                mPositionInterpolator);
        mCurrentDrawY = lerp(mExpandedDrawY, mCollapsedDrawY, fraction,
                mPositionInterpolator);

        setInterpolatedTextSize(lerp(mExpandedTextSize, mCollapsedTextSize,
                fraction, mTextSizeInterpolator));

        if (mCollapsedTextColor != mExpandedTextColor) {
            // If the collapsed and expanded text colors are different, blend them based on the
            // fraction
            mTextPaint.setColor(blendColors(
                    getCurrentExpandedTextColor(), getCurrentCollapsedTextColor(), fraction));
        } else {
            mTextPaint.setColor(getCurrentCollapsedTextColor());
        }

        mTextPaint.setShadowLayer(
                lerp(mExpandedShadowRadius, mCollapsedShadowRadius, fraction, null),
                lerp(mExpandedShadowDx, mCollapsedShadowDx, fraction, null),
                lerp(mExpandedShadowDy, mCollapsedShadowDy, fraction, null),
                blendColors(mExpandedShadowColor, mCollapsedShadowColor, fraction));

        ViewCompat.postInvalidateOnAnimation(mView);
    }

    @ColorInt
    private int getCurrentExpandedTextColor() {
        if (mState != null) {
            return mExpandedTextColor.getColorForState(mState, 0);
        } else {
            return mExpandedTextColor.getDefaultColor();
        }
    }

    @ColorInt
    private int getCurrentCollapsedTextColor() {
        if (mState != null) {
            return mCollapsedTextColor.getColorForState(mState, 0);
        } else {
            return mCollapsedTextColor.getDefaultColor();
        }
    }

    private void calculateBaseOffsets() {
        final float currentTextSize = mCurrentTextSize;

        // We then calculate the collapsed text size, using the same logic
        calculateUsingTextSize(mCollapsedTextSize);
        float width = mTextToDraw != null ?
                mTextPaint.measureText(mTextToDraw, 0, mTextToDraw.length()) : 0;
        final int collapsedAbsGravity = GravityCompat.getAbsoluteGravity(mCollapsedTextGravity,
                mIsRtl ? ViewCompat.LAYOUT_DIRECTION_RTL : ViewCompat.LAYOUT_DIRECTION_LTR);
        float textHeight = mTextPaint.descent() - mTextPaint.ascent();
        float textOffset = (textHeight / 2) - mTextPaint.descent();
        switch (collapsedAbsGravity & Gravity.VERTICAL_GRAVITY_MASK) {
            case Gravity.BOTTOM:
                mCollapsedDrawY = mCollapsedBounds.bottom;
                break;
            case Gravity.TOP:
                mCollapsedDrawY = mCollapsedBounds.top - mTextPaint.ascent() + textOffset / 4 * 3;
                break;
            case Gravity.CENTER_VERTICAL:
            default:
                //float textHeight = mTextPaint.descent() - mTextPaint.ascent();
                //float textOffset = (textHeight / 2) - mTextPaint.descent();
                mCollapsedDrawY = mCollapsedBounds.centerY() + textOffset;
                break;
        }
        switch (collapsedAbsGravity & GravityCompat.RELATIVE_HORIZONTAL_GRAVITY_MASK) {
            case Gravity.CENTER_HORIZONTAL:
                mCollapsedDrawX = mCollapsedBounds.centerX() - (width / 2);
                break;
            case Gravity.RIGHT:
                mCollapsedDrawX = mCollapsedBounds.right - width;
                break;
            case Gravity.LEFT:
            default:
                mCollapsedDrawX = mCollapsedBounds.left;
                break;
        }

        calculateUsingTextSize(mExpandedTextSize);
        width = mTextToDraw != null
                ? mTextPaint.measureText(mTextToDraw, 0, mTextToDraw.length()) : 0;
        final int expandedAbsGravity = GravityCompat.getAbsoluteGravity(mExpandedTextGravity,
                mIsRtl ? ViewCompat.LAYOUT_DIRECTION_RTL : ViewCompat.LAYOUT_DIRECTION_LTR);
        switch (expandedAbsGravity & Gravity.VERTICAL_GRAVITY_MASK) {
            case Gravity.BOTTOM:
                mExpandedDrawY = mExpandedBounds.bottom;
                break;
            case Gravity.TOP:
                mExpandedDrawY = mExpandedBounds.top - mTextPaint.ascent() + textOffset / 4 * 3;
                break;
            case Gravity.CENTER_VERTICAL:
            default:
                //float textHeight = mTextPaint.descent() - mTextPaint.ascent();
                //float textOffset = (textHeight / 2) - mTextPaint.descent();
                mExpandedDrawY = mExpandedBounds.centerY() + textOffset;
                break;
        }
        switch (expandedAbsGravity & GravityCompat.RELATIVE_HORIZONTAL_GRAVITY_MASK) {
            case Gravity.CENTER_HORIZONTAL:
                mExpandedDrawX = mExpandedBounds.centerX() - (width / 2);
                break;
            case Gravity.RIGHT:
                mExpandedDrawX = mExpandedBounds.right - width;
                break;
            case Gravity.LEFT:
            default:
                mExpandedDrawX = mExpandedBounds.left;
                break;
        }

        // The bounds have changed so we need to clear the texture
        clearTexture();
        // Now reset the text size back to the original
        setInterpolatedTextSize(currentTextSize);
    }

    private void interpolateBounds(float fraction) {
        mCurrentBounds.left = lerp(mExpandedBounds.left, mCollapsedBounds.left,
                fraction, mPositionInterpolator);
        mCurrentBounds.top = lerp(mExpandedDrawY, mCollapsedDrawY,
                fraction, mPositionInterpolator);
        mCurrentBounds.right = lerp(mExpandedBounds.right, mCollapsedBounds.right,
                fraction, mPositionInterpolator);
        mCurrentBounds.bottom = lerp(mExpandedBounds.bottom, mCollapsedBounds.bottom,
                fraction, mPositionInterpolator);
    }

    public void draw(Canvas canvas) {
        final int saveCount = canvas.save();

        if (!mCollapsingTitleScalable) {
            mScale = 1;
        }

        if (mTextToDraw != null && mDrawTitle) {
            float x = mCurrentDrawX;
            float y = mCurrentDrawY;

            final boolean drawTexture = mUseTexture && mExpandedTitleTexture != null;

            final float ascent;
            final float descent;
            if (drawTexture) {
                ascent = mTextureAscent * mScale;
                descent = mTextureDescent * mScale;
            } else {
                ascent = mTextPaint.ascent() * mScale;
                descent = mTextPaint.descent() * mScale;
            }

            if (DEBUG_DRAW) {
                // Just a debug tool, which drawn a magenta rect in the text bounds
                canvas.drawRect(mCurrentBounds.left, y + ascent, mCurrentBounds.right, y + descent,
                        DEBUG_DRAW_PAINT);
            }

            if (drawTexture) {
                y += ascent;
            }

            if (mScale != 1f) {
                canvas.scale(mScale, mScale, x, y);
            }

            if (drawTexture) {
                // If we should use a texture, draw it instead of text
                canvas.drawBitmap(mExpandedTitleTexture, x, y, mTexturePaint);
            } else {
                canvas.drawText(mTextToDraw, 0, mTextToDraw.length(), x, y, mTextPaint);
            }
        }

        canvas.restoreToCount(saveCount);
//        p.setColor(0x66ff0000);
//        canvas.drawRect(mCollapsedBounds,p);
//        p.setColor(0x6600ff00);
//        canvas.drawRect(mExpandedBounds,p);
//        p.setColor(0xffff0000);
//        canvas.drawLine(0,mCurrentDrawY,canvas.getWidth(),mCurrentDrawY,p);
//        p.setColor(0xff00ff00);
//        canvas.drawLine(0,mExpandedDrawY,canvas.getWidth(),mExpandedDrawY,p);
//        p.setColor(0xff0000ff);
//        canvas.drawLine(0,mCollapsedDrawY,canvas.getWidth(),mCollapsedDrawY,p);

    }

    Paint p = new Paint();

    private boolean calculateIsRtl(CharSequence text) {
        final boolean defaultIsRtl = ViewCompat.getLayoutDirection(mView)
                == ViewCompat.LAYOUT_DIRECTION_RTL;
        return (defaultIsRtl
                ? TextDirectionHeuristicsCompat.FIRSTSTRONG_RTL
                : TextDirectionHeuristicsCompat.FIRSTSTRONG_LTR).isRtl(text, 0, text.length());
    }

    private void setInterpolatedTextSize(float textSize) {
        calculateUsingTextSize(textSize);

        // Use our texture if the scale isn't 1.0
        mUseTexture = USE_SCALING_TEXTURE && mScale != 1f;

        if (mUseTexture) {
            // Make sure we have an expanded texture if needed
            ensureExpandedTexture();
        }

        ViewCompat.postInvalidateOnAnimation(mView);
    }

    private boolean areTypefacesDifferent(Typeface first, Typeface second) {
        return (first != null && !first.equals(second)) || (first == null && second != null);
    }

    private void calculateUsingTextSize(final float textSize) {
        if (mText == null) return;

        final float collapsedWidth = mCollapsedBounds.width();
        final float expandedWidth = mExpandedBounds.width();

        final float availableWidth;
        final float newTextSize;
        boolean updateDrawText = false;

        if (isClose(textSize, mCollapsedTextSize)) {
            newTextSize = mCollapsedTextSize;
            mScale = 1f;
            if (areTypefacesDifferent(mCurrentTypeface, mCollapsedTypeface)) {
                mCurrentTypeface = mCollapsedTypeface;
                updateDrawText = true;
            }
            availableWidth = collapsedWidth;
        } else {
            newTextSize = mExpandedTextSize;
            if (areTypefacesDifferent(mCurrentTypeface, mExpandedTypeface)) {
                mCurrentTypeface = mExpandedTypeface;
                updateDrawText = true;
            }
            if (isClose(textSize, mExpandedTextSize)) {
                // If we're close to the expanded text size, snap to it and use a scale of 1
                mScale = 1f;
            } else {
                // Else, we'll scale down from the expanded text size
                mScale = textSize / mExpandedTextSize;
            }

            final float textSizeRatio = mCollapsedTextSize / mExpandedTextSize;
            // This is the size of the expanded bounds when it is scaled to match the
            // collapsed text size
            final float scaledDownWidth = expandedWidth * textSizeRatio;

            if (scaledDownWidth > collapsedWidth) {
                // If the scaled down size is larger than the actual collapsed width, we need to
                // cap the available width so that when the expanded text scales down, it matches
                // the collapsed width
                availableWidth = Math.min(collapsedWidth / textSizeRatio, expandedWidth);
            } else {
                // Otherwise we'll just use the expanded width
                availableWidth = expandedWidth;
            }
        }

        if (availableWidth > 0) {
            updateDrawText = (mCurrentTextSize != newTextSize) || mBoundsChanged || updateDrawText;
            mCurrentTextSize = newTextSize;
            mBoundsChanged = false;
        }

        if (mTextToDraw == null || updateDrawText) {
            mTextPaint.setTextSize(mCurrentTextSize);
            mTextPaint.setTypeface(mCurrentTypeface);
            // Use linear text scaling if we're scaling the canvas
            mTextPaint.setLinearText(mScale != 1f);

            // If we don't currently have text to draw, or the text size has changed, ellipsize...
            final CharSequence title = TextUtils.ellipsize(mText, mTextPaint,
                    availableWidth, TextUtils.TruncateAt.END);
            if (!TextUtils.equals(title, mTextToDraw)) {
                mTextToDraw = title;
                mIsRtl = calculateIsRtl(mTextToDraw);
            }
        }
    }

    private void ensureExpandedTexture() {
        if (mExpandedTitleTexture != null || mExpandedBounds.isEmpty()
                || TextUtils.isEmpty(mTextToDraw)) {
            return;
        }

        calculateOffsets(0f);
        mTextureAscent = mTextPaint.ascent();
        mTextureDescent = mTextPaint.descent();

        final int w = Math.round(mTextPaint.measureText(mTextToDraw, 0, mTextToDraw.length()));
        final int h = Math.round(mTextureDescent - mTextureAscent);

        if (w <= 0 || h <= 0) {
            return; // If the width or height are 0, return
        }

        mExpandedTitleTexture = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);

        Canvas c = new Canvas(mExpandedTitleTexture);
        c.drawText(mTextToDraw, 0, mTextToDraw.length(), 0, h - mTextPaint.descent(), mTextPaint);

        if (mTexturePaint == null) {
            // Make sure we have a paint
            mTexturePaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
        }
    }

    public void recalculate() {
        if (mView.getHeight() > 0 && mView.getWidth() > 0) {
            // If we've already been laid out, calculate everything now otherwise we'll wait
            // until a layout
            calculateBaseOffsets();
            calculateCurrentOffsets();
        }
    }

    /**
     * Set the title to display
     *
     * @param text
     */
    void setText(CharSequence text) {
        if (text == null || !text.equals(mText)) {
            mText = text;
            mTextToDraw = null;
            clearTexture();
            recalculate();
        }
    }

    CharSequence getText() {
        return mText;
    }

    private void clearTexture() {
        if (mExpandedTitleTexture != null) {
            mExpandedTitleTexture.recycle();
            mExpandedTitleTexture = null;
        }
    }

    /**
     * Returns true if {@code value} is 'close' to it's closest decimal value. Close is currently
     * defined as it's difference being < 0.001.
     */
    private static boolean isClose(float value, float targetValue) {
        return Math.abs(value - targetValue) < 0.001f;
    }

    ColorStateList getExpandedTextColor() {
        return mExpandedTextColor;
    }

    ColorStateList getCollapsedTextColor() {
        return mCollapsedTextColor;
    }

    /**
     * Blend {@code color1} and {@code color2} using the given ratio.
     *
     * @param ratio of which to blend. 0.0 will return {@code color1}, 0.5 will give an even blend,
     *              1.0 will return {@code color2}.
     */
    private static int blendColors(int color1, int color2, float ratio) {
        Log.d("TTTT", "bbbbbbbbbbbbbb" + color1 + "  " + color2);
        final float inverseRatio = 1f - ratio;
        float a = (Color.alpha(color1) * inverseRatio) + (Color.alpha(color2) * ratio);
        float r = (Color.red(color1) * inverseRatio) + (Color.red(color2) * ratio);
        float g = (Color.green(color1) * inverseRatio) + (Color.green(color2) * ratio);
        float b = (Color.blue(color1) * inverseRatio) + (Color.blue(color2) * ratio);
        return Color.argb((int) a, (int) r, (int) g, (int) b);
    }

    private static float lerp(float startValue, float endValue, float fraction,
                              Interpolator interpolator) {
        if (interpolator != null) {
            fraction = interpolator.getInterpolation(fraction);
        }
        return AnimationUtils.lerp(startValue, endValue, fraction);
    }

    private static boolean rectEquals(Rect r, int left, int top, int right, int bottom) {
        return !(r.left != left || r.top != top || r.right != right || r.bottom != bottom);
    }


}

