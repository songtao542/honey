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

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.ListPopupWindow;

import com.zhihu.matisse.R;
import com.zhihu.matisse.internal.entity.Album;
import com.zhihu.matisse.internal.utils.Platform;

public class AlbumsSpinner {

    private static final int MAX_SHOWN_COUNT = 6;
    private CursorAdapter mAdapter;
    private TextView mSelected;
    private ListPopupWindow mListPopupWindow;
    private AdapterView.OnItemSelectedListener mOnItemSelectedListener;

    private View mDimView;

    private int mLastSelectPosition = -1;

    public AlbumsSpinner(@NonNull Context context) {
        //mListPopupWindow = new ListPopupWindow(context, null, R.attr.listPopupWindowStyle);
        mListPopupWindow = new ListPopupWindow(context, null, 0);

        TypedValue animation = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.listPopupWindowAnimationStyle, animation, true);
        mListPopupWindow.setAnimationStyle(animation.data);

        TypedValue background = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.listPopupWindowBackground, background, true);
        mListPopupWindow.setBackgroundDrawable(new ColorDrawable(background.data));

        mListPopupWindow.setModal(true);

        float width = context.getResources().getDisplayMetrics().widthPixels;
        mListPopupWindow.setContentWidth((int) width);


        mListPopupWindow.setOnItemClickListener((parent, view, position, id) -> {
            boolean valid = AlbumsSpinner.this.onItemSelected(parent.getContext(), position);
            if (valid && mOnItemSelectedListener != null) {
                mOnItemSelectedListener.onItemSelected(parent, view, position, id);
            }
        });

        mListPopupWindow.setOnDismissListener(() -> {
            if (mDimView != null) {
                mDimView.animate().alpha(0).setDuration(220).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation1) {
                        mDimView.setVisibility(View.GONE);
                    }
                }).start();
            }
        });
    }

    public void setOnItemSelectedListener(AdapterView.OnItemSelectedListener listener) {
        mOnItemSelectedListener = listener;
    }

    public void setSelection(Context context, int position) {
        mListPopupWindow.setSelection(position);
        onItemSelected(context, position);
    }

    private boolean onItemSelected(Context context, int position) {
        mListPopupWindow.dismiss();
        if (mLastSelectPosition == position) {
            return false;
        }
        mLastSelectPosition = position;
        Cursor cursor = mAdapter.getCursor();
        cursor.moveToPosition(position);
        Album album = Album.valueOf(cursor);
        String displayName = album.getDisplayName(context);
        if (mSelected.getVisibility() == View.VISIBLE) {
            mSelected.setText(displayName);
        } else {
            if (Platform.hasICS()) {
                mSelected.setAlpha(0.0f);
                mSelected.setVisibility(View.VISIBLE);
                mSelected.setText(displayName);
                mSelected.animate().alpha(1.0f).setDuration(context.getResources().getInteger(
                        android.R.integer.config_longAnimTime)).start();
            } else {
                mSelected.setVisibility(View.VISIBLE);
                mSelected.setText(displayName);
            }

        }
        return true;
    }

    public void setAdapter(CursorAdapter adapter) {
        mListPopupWindow.setAdapter(adapter);
        mAdapter = adapter;
    }

    public void setDimView(View view) {
        mDimView = view;
    }

    @SuppressLint("ClickableViewAccessibility")
    public void setSelectedTextView(TextView textView) {
        mSelected = textView;
        // tint dropdown arrow icon
        Drawable[] drawables = mSelected.getCompoundDrawables();
        Drawable right = drawables[2];
        TypedArray ta = mSelected.getContext().getTheme().obtainStyledAttributes(
                new int[]{R.attr.album_element_color});
        int color = ta.getColor(0, 0);
        ta.recycle();
        right.setColorFilter(color, PorterDuff.Mode.SRC_IN);

        mSelected.setVisibility(View.GONE);
        mSelected.setOnClickListener(v -> {
            int itemHeight = v.getResources().getDimensionPixelSize(R.dimen.album_item_height);
            mListPopupWindow.setHeight(
                    mAdapter.getCount() > MAX_SHOWN_COUNT ? itemHeight * MAX_SHOWN_COUNT
                            : itemHeight * mAdapter.getCount());
            if (mDimView != null) {
                mDimView.setVisibility(View.VISIBLE);
                mDimView.animate().alpha(0.6f).setDuration(220).setListener(null).start();
            }
            mListPopupWindow.show();
        });
        mSelected.setOnTouchListener(mListPopupWindow.createDragToOpenListener(mSelected));
    }


    public void setPopupAnchorView(View view) {
        mListPopupWindow.setAnchorView(view);
    }

}
