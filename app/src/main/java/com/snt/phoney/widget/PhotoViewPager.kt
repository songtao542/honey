package com.snt.phoney.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.viewpager.widget.ViewPager
import it.sephiroth.android.library.imagezoom.ImageViewTouch

class PhotoViewPager : ViewPager {

    companion object {
        const val HORIZONTAL = 0
        const val VERTICAL = 1
    }

    private var mOrientation = HORIZONTAL

    override fun canScroll(v: View, checkV: Boolean, dx: Int, x: Int, y: Int): Boolean {
        return if (v is ImageViewTouch) {
            v.canScroll(dx) || super.canScroll(v, checkV, dx, x, y)
        } else super.canScroll(v, checkV, dx, x, y)
    }

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }


    private fun init() {
        if (mOrientation == VERTICAL) {
            setPageTransformer(true, VerticalPageTransformer())
            overScrollMode = View.OVER_SCROLL_NEVER
        }
    }

    fun setOrientation(orientation: Int) {
        if (orientation == VERTICAL) {
            this.mOrientation = VERTICAL
        } else {
            this.mOrientation = HORIZONTAL
        }
    }

    private fun swapXY(ev: MotionEvent): MotionEvent {
        val width = width.toFloat()
        val height = height.toFloat()
        val newX = ev.y / height * width
        val newY = ev.x / width * height
        ev.setLocation(newX, newY)
        return ev
    }

    @Suppress("LiftReturnOrAssignment")
    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        if (mOrientation == VERTICAL) {
            val intercepted = super.onInterceptTouchEvent(swapXY(ev))
            swapXY(ev)
            return intercepted
        } else {
            return super.onInterceptTouchEvent(ev)
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        for (i in 0 until childCount) {

        }
        return super.dispatchTouchEvent(ev)
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        return if (mOrientation == VERTICAL) {
            super.onTouchEvent(swapXY(ev))
        } else {
            super.onTouchEvent(ev)
        }
    }

    private inner class VerticalPageTransformer : ViewPager.PageTransformer {

        @Suppress("CascadeIf")
        override fun transformPage(view: View, position: Float) {
            if (position < -1) {
                view.alpha = 0f
            } else if (position <= 1) {
                view.alpha = 1f
                view.translationX = view.width * -position
                val yPosition = position * view.height
                view.translationY = yPosition
            } else {
                view.alpha = 0f
            }
        }
    }
}
