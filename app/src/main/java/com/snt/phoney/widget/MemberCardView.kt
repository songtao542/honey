package com.snt.phoney.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import com.snt.phoney.R
import kotlinx.android.synthetic.main.member_card_view.view.*
import java.text.DecimalFormat


class MemberCardView : RelativeLayout {
    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : this(context, attrs, defStyleAttr, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int = 0, defStyleRes: Int = 0) : super(context, attrs, defStyleAttr, defStyleRes) {
        LayoutInflater.from(context).inflate(R.layout.member_card_view, this, true)
    }

    fun setTitle(title: CharSequence) {
        this.title.text = title
    }

    fun setDuration(duration: CharSequence) {
        this.duration.text = duration
    }

    fun setDurationPrice(length: Double, price: Double) {
        this.duration.text = context.getString(R.string.member_duration_template, DecimalFormat.getInstance().format(length))
        this.price.text = context.getString(R.string.member_price_template, DecimalFormat.getInstance().format(price))
    }

    fun setPrice(price: CharSequence) {
        this.price.text = price
    }

    fun setRecommend(recommend: Boolean) {
        this.recommend.visibility = if (recommend) View.VISIBLE else View.GONE
    }


}