package com.snt.phoney.widget

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.snt.phoney.R
import com.snt.phoney.extensions.dip

class ExpandableFilterView : ExpandableLayout, View.OnClickListener {
    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context)
    }

    private fun init(context: Context) {
        LayoutInflater.from(context).inflate(R.layout.widget_expandable_filter_view, this, true)
    }


    override fun onExpandableHeightChange(height: Int) {
        if (waitExpand) {
            waitExpand = false
            toggle()
        }
    }

    private val cache = ArrayList<View>()
    private var filterItems: List<String>? = null
    private var waitExpand = false

    var onSelectChange: ((select: CharSequence) -> Unit)? = null

    private fun getCachedTextView(index: Int): View? {
        return if (index < cache.size) {
            cache[index]
        } else {
            null
        }
    }

    override fun onClick(v: View?) {
        toggle()
        if (v is TextView) {
            onSelectChange?.invoke(v.text)
        }
    }


    fun setFilter(filters: List<String>?, select: String? = null) {
        filters?.let {
            if (filterItems != filters) {
                waitExpand = true
                filterItems = filters
                removeAllExpandableViews()
                for ((index, filter) in filterItems!!.withIndex()) {
                    var textView: TextView? = getCachedTextView(index) as? TextView
                    if (textView == null) {
                        textView = TextView(context)
                        textView.gravity = Gravity.CENTER
                        textView.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, dip(40))
                        textView.setBackgroundResource(R.drawable.underline_selectable_item_background)
                        textView.setOnClickListener(this)
                        cache.add(textView)
                    }
                    textView.text = filter
                    addView(textView)
                }
            } else {
                toggle()
            }
        }
    }


}