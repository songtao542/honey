package com.snt.phoney.widget

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.RelativeLayout
import androidx.annotation.*

class InterceptibleRelativeLayout : RelativeLayout, GestureDetector.OnGestureListener {

    private var mOnLongPressListener: ((press: Boolean) -> Unit)? = null

    private lateinit var mGestureDetector: GestureDetector

    private var isLongPressStart = false

    constructor(@NonNull context: Context) : super(context) {
        init(context)
    }

    constructor(@NonNull context: Context, @Nullable attrs: AttributeSet) : super(context, attrs) {
        init(context)
    }

    constructor(@NonNull context: Context, @Nullable attrs: AttributeSet, @AttrRes defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(@NonNull context: Context, @Nullable attrs: AttributeSet,
                @AttrRes defStyleAttr: Int, @StyleRes defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        init(context)
    }

    private fun init(context: Context) {
        mGestureDetector = GestureDetector(context, this)
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        ev?.let {
            mGestureDetector.onTouchEvent(it)
            if (isLongPressStart) {
                if (it.action == MotionEvent.ACTION_MOVE) {
                    parent.requestDisallowInterceptTouchEvent(true)
                }
                if (it.action == MotionEvent.ACTION_UP || it.action == MotionEvent.ACTION_CANCEL) {
                    parent.requestDisallowInterceptTouchEvent(false)
                    isLongPressStart = false
                    mOnLongPressListener?.invoke(false)
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    fun setOnLongPressListener(listener: ((press: Boolean) -> Unit)?) {
        this.mOnLongPressListener = listener
    }

    override fun onShowPress(e: MotionEvent?) {
    }

    override fun onSingleTapUp(e: MotionEvent?): Boolean {
        return false
    }

    override fun onDown(e: MotionEvent?): Boolean {
        return true
    }

    override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
        return false
    }

    override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
        return false
    }

    override fun onLongPress(e: MotionEvent?) {
        mOnLongPressListener?.invoke(true)
        isLongPressStart = true
    }


}