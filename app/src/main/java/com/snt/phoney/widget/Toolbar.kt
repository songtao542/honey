package com.snt.phoney.widget

import android.content.Context
import android.util.AttributeSet
import android.view.WindowInsets
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.appcompat.widget.Toolbar
import com.snt.phoney.extensions.getStatusBarHeight

class Toolbar : androidx.appcompat.widget.Toolbar {

    private var statusBarHeight: Int = 0

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
        setPadding(paddingLeft, statusBarHeight, paddingRight, paddingBottom)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val hm = MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(heightMeasureSpec) + statusBarHeight, MeasureSpec.getMode(heightMeasureSpec))
        setMeasuredDimension(widthMeasureSpec, hm)
    }

    override fun dispatchApplyWindowInsets(insets: WindowInsets?): WindowInsets {
        statusBarHeight = insets?.systemWindowInsetTop ?: statusBarHeight
        return super.dispatchApplyWindowInsets(insets)
    }

    override fun onApplyWindowInsets(insets: WindowInsets?): WindowInsets {
        return super.onApplyWindowInsets(insets)
    }


}