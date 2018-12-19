package com.snt.phoney.ui.vip

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.snt.phoney.R
import com.snt.phoney.base.BaseFragment
import com.snt.phoney.domain.model.OrderType
import com.snt.phoney.domain.model.VipCombo
import com.snt.phoney.extensions.*
import com.snt.phoney.widget.VipCardView
import kotlinx.android.synthetic.main.fragment_vip.*
import java.text.DecimalFormat

class VipFragment : BaseFragment() {

    companion object {
        @JvmStatic
        fun newInstance(arguments: Bundle? = null) = VipFragment().apply {
            this.arguments = arguments
        }
    }

    private lateinit var viewModel: VipViewModel

    private val permissions = arrayOf(Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_vip, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(VipViewModel::class.java)
        viewModel.setActivity(requireActivity())

        toolbar.setNavigationOnClickListener { activity?.finish() }

        viewModel.vipCombos.observe(this, Observer {
            setVipCombos(it)
        })

        viewModel.listVipCombo()?.disposedBy(disposeBag)

        viewModel.error.observe(this, Observer {
            snackbar(it)
        })

        viewModel.success.observe(this, Observer {
            activity?.finish()
        })

        payButton.setOnClickListener {
            if (checkAndRequestPermission(*permissions)) {
                val selected = getChecked()
                selected?.let { combo ->
                    when (radioGroup.checkedRadioButtonId) {
                        R.id.alipayRadio -> {
                            viewModel.buyVipWithAlipay(OrderType.BUY_VIP, combo.safeUuid)
                        }
                        R.id.wechatPayRadio -> {
                            viewModel.buyVipWithWechat(OrderType.BUY_VIP, combo.safeUuid)
                        }
                        else -> {
                        }
                    }
                    return@let
                }
            }
        }

        checkAndRequestPermission(*permissions)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requireContext().checkAppPermission(*permissions)) {

        }
    }

    private fun setVipCombos(combos: List<VipCombo>) {
        context?.let { context ->
            var recommend: View? = null
            for (combo in combos) {
                val card = VipCardView(context)
                card.tag = combo
                if (combo.isRecommend && recommend == null) {
                    recommend = card
                }
                card.setRecommend(combo.isRecommend)
                card.setDurationPrice(combo.number, combo.price)

                vipCardContainer.addView(card)
                card.setOnClickListener {
                    clearCheck(it)
                    it.isSelected = !it.isSelected
                }
            }
            recommend?.let {
                it.isSelected = true
                val combo = it.tag as VipCombo
                payAmount.text = getString(R.string.pay_amount_template, DecimalFormat.getInstance().format(combo.price))
            }
        }
    }

    private fun getChecked(): VipCombo? {
        vipCardContainer.asSequence().forEach {
            if (it.isSelected) {
                return it.tag as VipCombo
            }
        }
        return null
    }

    private fun clearCheck(view: View) {
        vipCardContainer.asSequence().forEach {
            if (view != it) {
                it.isSelected = false
            }
        }
    }

}
