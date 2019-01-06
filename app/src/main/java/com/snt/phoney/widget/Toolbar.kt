package com.snt.phoney.widget

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.WindowInsets
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import com.snt.phoney.extensions.dip
import com.snt.phoney.extensions.forEach
import com.snt.phoney.extensions.getStatusBarHeight


class Toolbar : androidx.appcompat.widget.Toolbar {

    private var statusBarHeight: Int = 0
    private var actionBarSize: Int = 0

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    constructor(@NonNull context: Context, @Nullable attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context)
    }

    private fun init(context: Context) {
        fitsSystemWindows = false
        statusBarHeight = context.getStatusBarHeight()
        val tv = TypedValue()
        if (context.theme.resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarSize = TypedValue.complexToDimensionPixelSize(tv.data, resources.displayMetrics)
        }
        if (actionBarSize == 0) actionBarSize = dip(56)
        setPadding(paddingLeft, statusBarHeight, paddingRight, paddingBottom)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        //originalHeight = MeasureSpec.getSize(heightMeasureSpec)
        val heightMeasure = MeasureSpec.makeMeasureSpec(actionBarSize + statusBarHeight, MeasureSpec.getMode(heightMeasureSpec))
        setMeasuredDimension(widthMeasureSpec, heightMeasure)
        resetChildLayoutParams()
        super.onMeasure(widthMeasureSpec, heightMeasure)
    }

    override fun dispatchApplyWindowInsets(insets: WindowInsets?): WindowInsets {
        statusBarHeight = insets?.systemWindowInsetTop ?: statusBarHeight
        return super.dispatchApplyWindowInsets(insets)
    }

    override fun onApplyWindowInsets(insets: WindowInsets?): WindowInsets {
        return super.onApplyWindowInsets(insets)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        resetChildLayoutParams()
    }

    private fun resetChildLayoutParams() {
        forEach { view ->
            val lp = view.layoutParams
            if (lp.height != actionBarSize) {
                lp.height = actionBarSize
                view.layoutParams = lp
            }
        }
    }

}