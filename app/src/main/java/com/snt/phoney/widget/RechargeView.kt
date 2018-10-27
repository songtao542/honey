package com.snt.phoney.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.snt.phoney.R
import kotlinx.android.synthetic.main.recharge_view.view.*

class RechargeView : LinearLayout {
    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : this(context, attrs, defStyleAttr, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int = 0, defStyleRes: Int = 0) : super(context, attrs, defStyleAttr, defStyleRes) {
        LayoutInflater.from(context).inflate(R.layout.recharge_view, this, true)
    }

    public fun setIcon(icon: Int) {
        this.icon.setImageResource(icon)
    }

    public fun setText(text: CharSequence) {
        this.text.text = text
    }

    public fun setPrice(price: CharSequence) {
        this.price.text = price
    }
}