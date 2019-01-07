package com.snt.phoney.widget

import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import com.snt.phoney.R
import kotlinx.android.synthetic.main.widget_dropdown_label_view.view.*

class DropdownLabelView : LinearLayout {

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        init(context, attrs)
    }

    private var mTextId: Int? = 0
    private var mText: CharSequence? = null

    fun init(context: Context, attrs: AttributeSet?) {
        attrs?.let {
            val a = context.obtainStyledAttributes(attrs, R.styleable.DropdownLabelView)
            try {
                mTextId = a.getResourceId(R.styleable.DropdownLabelView_labelText, 0)
                mText = context.getString(mTextId!!)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                a.recycle()
            }
        }
        LayoutInflater.from(context).inflate(R.layout.widget_dropdown_label_view, this, true)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        mText?.let {
            setText(it)
        }
    }

    fun setText(text: CharSequence) {
        this.text.text = text
    }

    fun getIndicatorView(): ImageView {
        return this.indicator
    }

    fun expandIndicator() {
        val rotationAnimator = ObjectAnimator.ofFloat(indicator, "rotation", 0f, 180f)
        rotationAnimator.start()
    }

    fun collapseIndicator() {
        val rotationAnimator = ObjectAnimator.ofFloat(indicator, "rotation", 180f, 0f)
        rotationAnimator.start()
    }

    fun showProgress() {
        indicator.visibility = View.GONE
        progress.visibility = View.VISIBLE
    }

    fun hideProgress() {
        indicator.visibility = View.VISIBLE
        progress.visibility = View.GONE
    }

}