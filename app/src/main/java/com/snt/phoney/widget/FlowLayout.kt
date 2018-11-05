package com.snt.phoney.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.google.android.flexbox.*
import com.snt.phoney.R
import com.snt.phoney.extensions.dip
import com.snt.phoney.extensions.forEach


class FlowLayout : FlexboxLayout {
    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context)
    }

    var column: Int = 0

    @SuppressWarnings
    var space: Int = 0
        set(value) {
            field = dip(value)
        }

    private fun init(context: Context) {
        alignContent = AlignContent.FLEX_START
        alignItems = AlignItems.FLEX_START
        flexWrap = FlexWrap.WRAP
        justifyContent = JustifyContent.FLEX_START

        column = 4
        space = 10
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        forEach { index, child ->
            val width = MeasureSpec.getSize(widthMeasureSpec)
            val imageWidth = (width - paddingLeft - paddingRight - (space * (column - 1))) / column
            val lp = child?.layoutParams as? MarginLayoutParams
                    ?: MarginLayoutParams(imageWidth, imageWidth)
            lp.width = imageWidth
            lp.height = imageWidth
            if ((index % column) != column - 1) {
                lp.rightMargin = space
            }
            if (index > column - 1) {//第一行不要设置topMargin
                lp.topMargin = space
            }
            child?.layoutParams = lp
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    private var mOnItemClickListener: ((view: View, index: Int) -> Unit)? = null

    fun setOnItemClickListener(listener: (view: View, index: Int) -> Unit) {
        mOnItemClickListener = listener
    }

    var viewFactory: ViewFactory? = null
        set(value) {
            value?.let {
                removeAllViews()
                for (index in 0 until it.getItemCount()) {
                    val child = it.create(index)
                    child.setTag(R.id.tag_index, index)
                    child.setOnClickListener { view ->
                        mOnItemClickListener?.invoke(view, view.getTag(R.id.tag_index) as Int)
                    }
                    addView(child)
                }
            }
        }

    interface ViewFactory {
        fun create(index: Int): View
        fun getItemCount(): Int
    }
}

