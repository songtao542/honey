package com.snt.phoney.ui.wallet

import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.snt.phoney.R
import com.snt.phoney.base.BaseFragment
import com.snt.phoney.domain.model.MibiRule
import com.snt.phoney.ui.picker.PayPickerFragment
import com.snt.phoney.widget.RechargeView
import kotlinx.android.synthetic.main.fragment_wallet.*
import java.text.DecimalFormat

class WalletFragment : BaseFragment() {

    companion object {
        @JvmStatic
        fun newInstance(arguments: Bundle? = null) = WalletFragment().apply {
            this.arguments = arguments
        }
    }

    private lateinit var viewModel: WalletViewModel

    private val df = DecimalFormat(",###.##")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_wallet, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        enableOptionsMenu(toolbar, true)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(WalletViewModel::class.java)

        toolbar.setNavigationOnClickListener { activity?.finish() }

        viewModel.mibiWallet.observe(this, Observer {
            it?.let { wallet ->
                amount.text = df.format(wallet.mibi)
                wallet.rules?.let { rules ->
                    setMibiCombos(rules)
                }
                return@let
            }
        })

        viewModel.getMibiWallet()
    }

    private fun setMibiCombos(rules: List<MibiRule>) {
        context?.let { context ->
            for (rule in rules) {
                val ruleView = RechargeView(context)
                ruleView.setBackgroundResource(R.drawable.underline_gray)
                ruleView.setPrice(context.getString(R.string.money_template, df.format(rule.money)))
                ruleView.setText(context.getString(R.string.wallet_mibi_combo_template, df.format(rule.money)))
                ruleView.setOnRechargeClickListener {
                    PayPickerFragment.newInstance().apply {
                        setOnResultListener { which ->
                            if (which == PayPickerFragment.WECHAT) {
                                viewModel.buyMibiWithWechat(rule.safeUuid)
                            } else {
                                viewModel.buyMibiWithAlipay(rule.safeUuid)
                            }
                        }
                    }.show(childFragmentManager, "pay_picker")
                }
                rechargeContainer.addView(ruleView, LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT))
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.wallet, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.view_records -> {

            }
        }
        return super.onOptionsItemSelected(item)
    }

}
