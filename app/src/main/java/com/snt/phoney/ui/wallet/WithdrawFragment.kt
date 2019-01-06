package com.snt.phoney.ui.wallet

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.snt.phoney.R
import com.snt.phoney.base.BaseFragment
import com.snt.phoney.domain.model.PreWithdraw
import com.snt.phoney.extensions.TAG
import com.snt.phoney.extensions.snackbar
import com.snt.phoney.ui.browser.WebBrowserActivity
import com.snt.phoney.utils.AlipayApi
import com.snt.phoney.utils.data.Constants
import kotlinx.android.synthetic.main.app_toolbar.*
import kotlinx.android.synthetic.main.fragment_wallet_withdraw.*
import java.text.DecimalFormat


/**
 *
 */
class WithdrawFragment : BaseFragment() {


    private lateinit var viewModel: WalletViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_wallet_withdraw, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(WalletViewModel::class.java)

        toolbar.setNavigationOnClickListener { activity?.onBackPressed() }
        titleTextView.setText(R.string.wallet_withdraw_title)

        viewModel.preWithdraw.observe(this, Observer {
            setWithdrawInfo(it)
        })

        viewModel.error.observe(this, Observer {
            snackbar(it)
        })

        viewModel.alipaySign.observe(this, Observer { sign ->
            bindAlipay(sign)
        })

        confirmButton.setOnClickListener {
            val amount = getAmount()
            if (amount > 0) {
                viewModel.withdraw(amount)
            }
        }

        viewModel.preWithdraw()
    }

    private fun getAmount(): Double {
        try {
            if (viewModel.preWithdraw.value?.isAlipayBind != true) {
                snackbar(R.string.has_not_bind_alipay)
                return -1.0
            }
            val amount = withdrawEdit.text.toString().toDouble()
            val available = viewModel.preWithdraw.value?.available?.toDouble() ?: 0.0
            return if (amount <= available) {
                amount
            } else {
                snackbar(R.string.amount_out_of_range)
                -1.0
            }
        } catch (e: Exception) {
            return -1.0
        }
    }

    private fun setWithdrawInfo(preWithdraw: PreWithdraw) {
        moneyToMibi.text = preWithdraw.rule
        canWithdraw.text = getString(R.string.amount_can_withdraw_template, preWithdraw.available)
        bindAlipy.text = if (preWithdraw.isAlipayBind) getString(R.string.has_bind) else getString(R.string.not_bind)
        withdrawLimit.text = preWithdraw.limit
        withdrawInfo.text = preWithdraw.info

        try {
            val available = viewModel.preWithdraw.value?.available?.toDouble() ?: 0.0
            if (available > 0.0) {
                withdrawEdit.text = Editable.Factory.getInstance().newEditable(DecimalFormat.getInstance().format(available))
                canWithdraw.text = getString(R.string.amount_can_withdraw_template, DecimalFormat.getInstance().format(available))
            }
        } catch (e: Exception) {
            Log.e(TAG, "error:${e.message}")
        }
        if (!preWithdraw.isAlipayBind) {
            bindToAlipay.setOnClickListener {
                context?.let { context ->
                    //                    val uri = Uri.parse("${Constants.Api.BIND_ALIPAY_URL}${viewModel.getAccessToken()}")
//                    val intent = Intent(Intent.ACTION_VIEW, uri)
//                    startActivity(intent)

//                    context.startActivity(Intent(context, WebBrowserActivity::class.java).apply {
//                        putExtra(Constants.Extra.TITLE, getString(R.string.bind_alipay))
//                        putExtra(Constants.Extra.URL, "${Constants.Api.BIND_ALIPAY_URL}${viewModel.getAccessToken()}")
//                    })

                    viewModel.bindAlipay()

                }
            }
        }
    }

    private fun bindAlipay(sign: String) {
        AlipayApi.auth(requireActivity(), sign) { status, authCode ->
            authCode?.let {
                viewModel.uploadAuthCode(authCode)
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(arguments: Bundle? = null) = WithdrawFragment().apply {
            this.arguments = arguments
        }
    }
}
