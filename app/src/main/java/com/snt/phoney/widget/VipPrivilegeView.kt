package com.snt.phoney.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import com.snt.phoney.R
import kotlinx.android.synthetic.main.vip_privilege_view.view.*

class VipPrivilegeView : RelativeLayout {
    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : this(context, attrs, defStyleAttr, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int = 0, defStyleRes: Int = 0) : super(context, attrs, defStyleAttr, defStyleRes) {
        LayoutInflater.from(context).inflate(R.layout.vip_privilege_view, this, true)
    }

    public fun setIcon(icon: Int) {
        this.icon.setImageResource(icon)
    }

    public fun setName(name: CharSequence) {
        this.name.text = name
    }

    public fun setDescription(description: CharSequence) {
        this.description.text = description
    }
}