package com.snt.phoney.widget

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.snt.phoney.R
import com.snt.phoney.extensions.dip
import expandable.widget.ExpandableLayout
import java.util.*
import kotlin.collections.ArrayList

class ExpandableFilterView : ExpandableLayout {

    lateinit var filterContainer: LinearLayout

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
        LayoutInflater.from(context).inflate(R.layout.expandable_filter_view, this, true)
        filterContainer = findViewById(R.id.filterContainer)
    }


    private val cache = ArrayList<View>()

    private fun getCachedTextView(index: Int): View? {
        return if (index < cache.size) {
            cache[index]
        } else {
            null
        }
    }

    fun setFilter(filters: List<String>?) {
        filters?.let {
            filterContainer.removeAllViews()
            for ((index, filter) in filters.withIndex()) {
                var textView: TextView? = getCachedTextView(index) as? TextView
                if (textView == null) {
                    textView = TextView(context)
                    textView!!.gravity = Gravity.CENTER
                    textView!!.layoutParams = LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, dip(40))
                    cache.add(textView!!)
                }
                textView.text = filter
                filterContainer.addView(textView)
            }
        }
    }

}