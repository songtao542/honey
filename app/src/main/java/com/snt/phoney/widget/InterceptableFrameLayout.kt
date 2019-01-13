package com.snt.phoney.widget

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.widget.FrameLayout
import androidx.annotation.AttrRes
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.annotation.StyleRes

class InterceptableFrameLayout : FrameLayout {

    private var mOnInterceptTouchEventListener: ((ev: MotionEvent) -> Boolean)? = null
    var intercept = false

    constructor(@NonNull context: Context) : super(context)

    constructor(@NonNull context: Context, @Nullable attrs: AttributeSet) : super(context, attrs)

    constructor(@NonNull context: Context, @Nullable attrs: AttributeSet, @AttrRes defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    constructor(@NonNull context: Context, @Nullable attrs: AttributeSet,
                @AttrRes defStyleAttr: Int, @StyleRes defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)


    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
//        ev?.let {
//            if (mOnInterceptTouchEventListener?.invoke(it) == true) {
//                return true
//            }
//        }
        if (intercept) {
            return true
        }
        return super.onInterceptTouchEvent(ev)
//        return true
    }

    fun setInterceptTouchEvent(intercept: Boolean) {
        this.intercept = intercept
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.let {
            val action = it.action
            Log.d("TTTT", "action===========>$action")
//            when (action) {
//
//            }
        }
        return super.onTouchEvent(event)
    }

    fun setOnInterceptTouchEventListener(listener: ((ev: MotionEvent) -> Boolean)) {
        this.mOnInterceptTouchEventListener = listener
    }

}