package com.snt.phoney.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import androidx.annotation.Nullable
import com.snt.phoney.R

const val WECHAT: Int = 1
const val ALIPAY: Int = 2

class PayPickerView : LinearLayout {


    private lateinit var wechat: LinearLayout
    private lateinit var alipay: LinearLayout
    private lateinit var confirm: Button

    private lateinit var selectView: View

    private var listener: ((which: Int) -> Unit)? = null

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, @Nullable attrs: AttributeSet) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, @Nullable attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) :
            super(context, attrs, defStyleAttr, defStyleRes) {
        init(context)
    }


    private fun init(context: Context) {
        LayoutInflater.from(context).inflate(R.layout.pay_picker_view, this, true)

        selectView = wechat
        wechat = findViewById(R.id.wallet)
        alipay = findViewById(R.id.alipay)
        confirm = findViewById(R.id.confirm)

        wechat.isSelected = true
        wechat.setOnClickListener { setCheck(it) }
        alipay.setOnClickListener { setCheck(it) }

        confirm.background = context.getDrawable(R.drawable.pay_picker_button_selector)

        confirm.setOnClickListener {
            val which = if (selectView == wechat) WECHAT else ALIPAY
            listener?.invoke(which)
        }
    }

    fun setOnPickListener(listener: ((which: Int) -> Unit)) {
        this.listener = listener
    }

    private fun setCheck(view: View) {
        if (view != selectView) {
            selectView = view
            if (view == wechat) {
                wechat.isSelected = true
                alipay.isSelected = false
            } else {
                wechat.isSelected = false
                alipay.isSelected = true
            }
        }
    }

}