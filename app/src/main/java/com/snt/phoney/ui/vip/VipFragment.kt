package com.snt.phoney.ui.vip

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.snt.phoney.R
import com.snt.phoney.base.BaseFragment
import com.snt.phoney.domain.model.VipCombo
import com.snt.phoney.extensions.*
import com.snt.phoney.utils.data.Constants
import com.snt.phoney.widget.VipCardView
import kotlinx.android.synthetic.main.app_toolbar.*
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

    private var buySuccess: Boolean = false

    private val broadcast by autoCleared(ClearableBroadcast())

    private val permissions = arrayOf(Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_vip, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(VipViewModel::class.java)
        viewModel.setActivity(requireActivity())

        toolbar.setNavigationOnClickListener { activity?.finish() }
        titleTextView.setText(R.string.upgrade_vip_title)

        viewModel.vipCombos.observe(this, Observer {
            setVipCombos(it)
        })

        viewModel.listVipCombo()

        viewModel.error.observe(this, Observer {
            snackbar(it)
        })

        viewModel.success.observe(this, Observer {
            finish()
        })

        payButton.setOnClickListener {
            if (checkAndRequestPermission(*permissions)) {
                val selected = getChecked()
                selected?.let { combo ->
                    when (radioGroup.checkedRadioButtonId) {
                        R.id.alipayRadio -> {
                            viewModel.buyVipWithAlipay(combo.safeUuid)
                        }
                        R.id.wechatPayRadio -> {
                            viewModel.buyVipWithWechat(combo.safeUuid)
                        }
                        else -> {
                        }
                    }
                    return@let
                }
            }
        }

        broadcast.registerReceiver(requireContext(), "buy_success") {
            buySuccess = true
            finish()
        }

        checkAndRequestPermission(*permissions)
    }

    private fun finish() {
        if (buySuccess) {
            val data = Intent()
            data.putExtra(Constants.Extra.DATA, true)
            activity?.setResult(Activity.RESULT_OK, data)
            activity?.finish()
        } else {
            activity?.onBackPressed()
        }
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
                    val combo = it.tag as VipCombo
                    payAmount.text = getString(R.string.pay_amount_template, DecimalFormat.getInstance().format(combo.price))
                }
            }
            recommend?.let {
                it.isSelected = true
                val combo = it.tag as VipCombo
                payAmount.text = getString(R.string.pay_amount_template, DecimalFormat.getInstance().format(combo.price * combo.number))
            }
            return@let
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
