package com.snt.phoney.ui.wallet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.snt.phoney.R
import kotlinx.android.synthetic.main.fragment_wallet_withdraw.*


/**
 *
 */
class WithdrawFragment : Fragment() {


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


        confirmButton.setOnClickListener {
            when (radioGroup.checkedRadioButtonId) {
                R.id.alipayRadio -> {
                    viewModel.withdraw()
                }
                R.id.wechatPayRadio -> {
                    viewModel.withdraw()
                }
                else -> {
                }
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
