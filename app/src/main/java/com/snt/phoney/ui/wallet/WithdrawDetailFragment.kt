package com.snt.phoney.ui.wallet

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.gson.Gson
import com.snt.phoney.R
import com.snt.phoney.base.BaseFragment
import com.snt.phoney.domain.model.WithDrawItem
import com.snt.phoney.domain.model.WithdrawInfo
import com.snt.phoney.extensions.colorOf
import com.snt.phoney.utils.data.Constants
import kotlinx.android.synthetic.main.app_toolbar.*
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
        toolbar.setNavigationOnClickListener { activity?.onBackPressed() }
        titleTextView.setText(R.string.wallet_withdraw_detail_title)

        viewModel.withdrawInfo.observe(this, Observer {
            it?.let { withdrawInfo ->
                setWithdrawInfo(withdrawInfo)
            }
        })

        uuid?.let {
            viewModel.getWithdrawInfo(it)
        }


        //val mock = """{"state_msg":"处理中","money":15,"openid":"支付宝账号208****2392","ctime":1544583177404,"type_msg":"支付宝提现","id":1,"state":0,"type":1,"uuid":"uo2018121210525700bf9b1b6694bd8b","items":[{"reason":"系统触发","ctime":1544583177404,"state":1,"title":"发起提现申请"},{"title":"审核中"},{"title":"转账成功"}]}"""
        //val gson = Gson()
        //val withdrawInfo = gson.fromJson<WithdrawInfo>(mock, WithdrawInfo::class.java)
        //setWithdrawInfo(withdrawInfo)
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
                    val last = index == items.size - 1
                    val front = if (index - 1 >= 0) items[index - 1] else null
                    val back = if (index + 1 < items.size) items[index + 1] else null
                    val itemView = WithdrawStateItemView(context)
                    itemLayout.addView(itemView)
                    itemView.setWithDrawItem(item, front, back, first, last)
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

    fun setWithDrawItem(withdrawItem: WithDrawItem, front: WithDrawItem?, back: WithDrawItem?, first: Boolean = false, last: Boolean = false) {
        if (first) {
            iconTopLine.visibility = View.INVISIBLE
            textTopLine.visibility = View.INVISIBLE
        }

        if (last) {
            iconUnderLine.visibility = View.INVISIBLE
            textUnderLine.visibility = View.INVISIBLE
        }
        if (withdrawItem.state == 1) {
            stepText.setTextColor(context.colorOf(R.color.colorPrimaryMale))
            stepIcon.setImageResource(R.drawable.ic_state_complete)
            iconTopLine.setBackgroundColor(context.colorOf(R.color.colorPrimaryMale))
            iconUnderLine.setBackgroundColor(context.colorOf(R.color.colorPrimaryMale))
        } else {
            stepText.setTextColor(context.colorOf(R.color.dark_gray))
            stepIcon.setImageResource(R.drawable.ic_state_wait)
            iconTopLine.setBackgroundColor(context.colorOf(R.color.ccc))
            iconUnderLine.setBackgroundColor(context.colorOf(R.color.ccc))
        }
        stepText.text = withdrawItem.title
        crateTimeText.text = withdrawItem.formatCreateTime()
    }

}
