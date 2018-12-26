package com.snt.phoney.ui.wallet

import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.snt.phoney.R
import com.snt.phoney.base.BaseFragment
import com.snt.phoney.base.Page
import com.snt.phoney.domain.model.MibiRule
import com.snt.phoney.extensions.addFragmentSafely
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

    private var mNeedRefresh = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_wallet, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        enableOptionsMenu(toolbar, true)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(WalletViewModel::class.java)

        toolbar.setNavigationOnClickListener { activity?.finish() }

        viewModel.setActivity(requireActivity())
        viewModel.mibiWallet.observe(this, Observer {
            it?.let { wallet ->
                amount.text = df.format(wallet.mibi)
                wallet.rules?.let { rules ->
                    setMibiCombos(rules)
                }
                return@let
            }
        })

        withdraw.setOnClickListener {
            addFragmentSafely(Page.WITHDRAW, "wallet_withdraw", true,
                    enterAnimation = R.anim.slide_in_up, popExitAnimation = R.anim.slide_out_down)
        }

        refreshWallet()
    }

    override fun onResume() {
        super.onResume()
        //onResume 时重新加载钱包数据，支付完成后即可显示结果
        refreshWallet()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("mNeedRefresh", mNeedRefresh)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        mNeedRefresh = savedInstanceState?.getBoolean("mNeedRefresh") ?: false
        refreshWallet()
    }

    private fun refreshWallet() {
        if (mNeedRefresh) {
            mNeedRefresh = false
            viewModel.getMibiWallet()
        }
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
                            mNeedRefresh = true
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
                addFragmentSafely(Page.WALLET_DETAIL, "wallet_detail", true,
                        enterAnimation = R.anim.slide_in_up, popExitAnimation = R.anim.slide_out_down)
            }
        }
        return super.onOptionsItemSelected(item)
    }

}
