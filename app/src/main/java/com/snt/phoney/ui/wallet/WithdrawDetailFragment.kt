package com.snt.phoney.ui.wallet

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.snt.phoney.R
import com.snt.phoney.base.BaseFragment
import com.snt.phoney.domain.model.WithDrawItem
import com.snt.phoney.domain.model.WithdrawInfo
import com.snt.phoney.extensions.colorOf
import com.snt.phoney.utils.data.Constants
import kotlinx.android.synthetic.main.fragment_wallet_withdraw_detail.*
import kotlinx.android.synthetic.main.fragment_wallet_withdraw_detail_item.view.*
import java.text.DecimalFormat


/**
 *
 */
class WithdrawDetailFragment : BaseFragment() {

    companion object {
        @JvmStatic
        fun newInstance(arguments: Bundle? = null) = WithdrawDetailFragment().apply {
            this.arguments = arguments
        }
    }

    private lateinit var viewModel: WalletViewModel

    private var uuid: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            uuid = it.getString(Constants.Extra.UUID)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_wallet_withdraw_detail, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(WalletViewModel::class.java)

        viewModel.withdrawInfo.observe(this, Observer {
            it?.let { withdrawInfo ->
                setWithdrawInfo(withdrawInfo)
            }
        })

        uuid?.let {
            viewModel.getWithdrawInfo(it)
        }
    }

    private fun setWithdrawInfo(withdrawInfo: WithdrawInfo) {
        withdrawAmount.text = DecimalFormat.getInstance().format(withdrawInfo.money)
        withdrawProgress.text = withdrawInfo.stateMessage
        withdrawType.text = withdrawInfo.typeMessage
        withdrawTo.text = withdrawInfo.typeMessage
        createTime.text = withdrawInfo.formatCreateTime()

        context?.let { context ->
            withdrawInfo.items?.let { items ->
                for ((index, item) in items.withIndex()) {
                    val first = index == 0
                    val last = index == items.size
                    val itemView = WithdrawStateItemView(context)
                    itemLayout.addView(itemView)
                    itemView.setWithDrawItem(item, first, last)
                }
            }
        }
    }

}

class WithdrawStateItemView : RelativeLayout {
    constructor(context: Context) : super(context, null) {
        init(context)
    }

    private fun init(context: Context) {
        LayoutInflater.from(context).inflate(R.layout.fragment_wallet_withdraw_detail_item, this, true)
    }

    fun setWithDrawItem(withdrawItem: WithDrawItem, first: Boolean = false, last: Boolean = false) {
        if (first) {
            iconTopLine.visibility = View.GONE
            textTopLine.visibility = View.GONE
        }

        if (last) {
            iconUnderLine.visibility = View.GONE
            textUnderLine.visibility = View.GONE
        }
        if (withdrawItem.state == 1) {
            stepIcon.setImageResource(R.drawable.ic_state_complete)
            iconUnderLine.setBackgroundColor(context.colorOf(R.color.colorPrimaryMale))
        } else {
            stepIcon.setImageResource(R.drawable.ic_state_wait)
            iconUnderLine.setBackgroundColor(context.colorOf(R.color.ccc))
        }
        stepText.text = withdrawItem.title
        crateTimeText.text = withdrawItem.formatCreateTime()
    }

}
