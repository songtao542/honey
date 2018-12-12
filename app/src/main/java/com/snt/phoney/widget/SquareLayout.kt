package com.snt.phoney.widget

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.annotation.AttrRes
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.annotation.StyleRes
import com.snt.phoney.R

class SquareLayout : FrameLayout {

    private var spanCount: Int = 0
    private var spaceWidth: Int = 0
    private var hasEdgeScape: Boolean = true

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(@NonNull context: Context, @Nullable attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(@NonNull context: Context, @Nullable attrs: AttributeSet, @AttrRes defStyleAttr: Int, @StyleRes defStyleRes: Int)
            : super(context, attrs, defStyleAttr, defStyleRes) {
        init(context, attrs)
    }


    private fun init(context: Context, @Nullable attrs: AttributeSet?) {
        attrs?.let {
            val a = context.obtainStyledAttributes(attrs, R.styleable.SquareLayout)
            spanCount = a.getInt(R.styleable.SquareLayout_spanCount, 0)
            spaceWidth = a.getDimensionPixelSize(R.styleable.SquareLayout_spaceWidth, 0)
            hasEdgeScape = a.getBoolean(R.styleable.SquareLayout_hasEdgeScape, true)
            a.recycle()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        //val size = MeasureSpec.getSize(widthMeasureSpec)
        if (spanCount > 0) {
            val mode = MeasureSpec.getMode(widthMeasureSpec)
            val spaceCount = if (hasEdgeScape) spanCount + 1 else spanCount - 1
            val width = (context.resources.displayMetrics.widthPixels - spaceCount * spaceWidth) / spanCount
            val widthMeasure = MeasureSpec.makeMeasureSpec(width, mode)
            super.onMeasure(widthMeasure, widthMeasure)
        } else {
            super.onMeasure(widthMeasureSpec, widthMeasureSpec)
        }
    }

}