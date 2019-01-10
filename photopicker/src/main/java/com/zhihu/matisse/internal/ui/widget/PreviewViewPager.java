/*
 * Copyright 2017 Zhihu Inc.
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
package com.zhihu.matisse.internal.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

import it.sephiroth.android.library.imagezoom.ImageViewTouch;

public class PreviewViewPager extends ViewPager {

    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;

    private int mOrientation = HORIZONTAL;

    @Override
    protected boolean canScroll(View v, boolean checkV, int dx, int x, int y) {
        if (v instanceof ImageViewTouch) {
            return ((ImageViewTouch) v).canScroll(dx) || super.canScroll(v, checkV, dx, x, y);
        }
        return super.canScroll(v, checkV, dx, x, y);
    }

    public PreviewViewPager(Context context) {
        super(context);
        init();
    }

    public PreviewViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    private void init() {
        if (mOrientation == VERTICAL) {
            setPageTransformer(true, new VerticalPageTransformer());
            setOverScrollMode(OVER_SCROLL_NEVER);
        }
    }

    public void setOrientation(int orientation) {
        if (orientation == VERTICAL) {
            this.mOrientation = VERTICAL;
        } else {
            this.mOrientation = HORIZONTAL;
        }
    }

    private class VerticalPageTransformer implements ViewPager.PageTransformer {

        @Override
        public void transformPage(@NonNull View view, float position) {
            if (position < -1) {
                view.setAlpha(0);
            } else if (position <= 1) {
                view.setAlpha(1);
                view.setTranslationX(view.getWidth() * -position);
                float yPosition = position * view.getHeight();
                view.setTranslationY(yPosition);
            } else {
                view.setAlpha(0);
            }
        }
    }

    private MotionEvent swapXY(MotionEvent ev) {
        float width = getWidth();
        float height = getHeight();
        float newX = (ev.getY() / height) * width;
        float newY = (ev.getX() / width) * height;
        ev.setLocation(newX, newY);
        return ev;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (mOrientation == VERTICAL) {
            boolean intercepted = super.onInterceptTouchEvent(swapXY(ev));
            swapXY(ev);
            return intercepted;
        } else {
            return super.onInterceptTouchEvent(ev);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mOrientation == VERTICAL) {
            return super.onTouchEvent(swapXY(ev));
        } else {
            return super.onTouchEvent(ev);
        }
    }
}
