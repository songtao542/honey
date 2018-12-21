package com.snt.phoney.ui.picker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.snt.phoney.R
import com.snt.phoney.base.NoInjectBaseDialogFragment
import kotlinx.android.synthetic.main.fragment_pay_picker.*

class PayPickerFragment : NoInjectBaseDialogFragment() {

    companion object {
        @JvmStatic
        fun newInstance() = PayPickerFragment()

        @JvmStatic
        val WECHAT: Int = 1
        @JvmStatic
        val ALIPAY: Int = 2
    }

    private lateinit var selectView: View

    private var listener: ((which: Int) -> Unit)? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_pay_picker, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        selectView = wechat
        wechat.isSelected = true
        wechat.setOnClickListener { setCheck(it) }
        alipay.setOnClickListener { setCheck(it) }

        confirm.background = requireContext().getDrawable(R.drawable.pay_picker_button_selector)

        confirm.setOnClickListener {
            dismiss()
            val which = if (selectView == wechat) WECHAT else ALIPAY
            listener?.invoke(which)
        }

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

    fun setOnResultListener(listener: ((which: Int) -> Unit)) {
        this.listener = listener
    }

}
