package com.snt.phoney.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import com.snt.phoney.R
import kotlinx.android.synthetic.main.vip_card_view.view.*

class VipCardView : RelativeLayout {
    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : this(context, attrs, defStyleAttr, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int = 0, defStyleRes: Int = 0) : super(context, attrs, defStyleAttr, defStyleRes) {
        LayoutInflater.from(context).inflate(R.layout.vip_card_view, this, true)
    }

    public fun setTitle(title: CharSequence) {
        this.title.text = title
    }

    public fun setDuration(duration: CharSequence) {
        this.duration.text = duration
    }

    public fun setPrice(price: CharSequence) {
        this.price.text = price
    }
}