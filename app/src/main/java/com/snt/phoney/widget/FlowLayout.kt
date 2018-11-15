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
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    var column: Int = 0
    var space: Int = 0
    var square: Boolean = false

    private fun init(context: Context, attrs: AttributeSet?, defStyleAttr: Int = 0) {
        alignContent = AlignContent.FLEX_START
        alignItems = AlignItems.FLEX_START
        flexWrap = FlexWrap.WRAP
        justifyContent = JustifyContent.FLEX_START

        column = 4
        space = dip(10)

        attrs?.let {
            val a = context.obtainStyledAttributes(it, R.styleable.FlowLayout, defStyleAttr, R.style.Widget_FlowLayout)
            column = a.getInt(R.styleable.FlowLayout_column, 4)
            space = a.getDimensionPixelSize(R.styleable.FlowLayout_space, dip(10))
            square = a.getBoolean(R.styleable.FlowLayout_square, false)
            a.recycle()
        }

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        forEach { index, child ->
            val width = MeasureSpec.getSize(widthMeasureSpec)
            val childWidth = (width - paddingLeft - paddingRight - (space * (column - 1))) / column
            val childHeight = if (square) childWidth else MarginLayoutParams.WRAP_CONTENT
            val lp = child.layoutParams as? MarginLayoutParams
                    ?: MarginLayoutParams(childWidth, childHeight)
            lp.width = childWidth
            lp.height = childHeight
            if ((index % column) != column - 1) {
                lp.rightMargin = space
            }
            if (index > column - 1) {//第一行不要设置topMargin
                lp.topMargin = space
            }
            child.layoutParams = lp
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

