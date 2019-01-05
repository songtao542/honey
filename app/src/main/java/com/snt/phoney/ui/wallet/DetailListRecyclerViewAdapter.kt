package com.snt.phoney.ui.wallet


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.snt.phoney.R
import com.snt.phoney.domain.model.OrderRecord
import com.snt.phoney.extensions.addFragmentSafely
import com.snt.phoney.utils.data.Constants
import kotlinx.android.synthetic.main.fragment_wallet_detail_list_consume_item.view.*
import kotlinx.android.synthetic.main.fragment_wallet_detail_list_recharge_item.view.*

/**
 */
class DetailListRecyclerViewAdapter(private val fragment: DetailListFragment, private val type: Int) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var data: List<OrderRecord>? = null
        set(value) {
            value?.let {
                field = it
                notifyDataSetChanged()
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (type == DetailListFragment.TYPE_RECHARGE) {
            RechargeViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.fragment_wallet_detail_list_recharge_item, parent, false))
        } else {
            ConsumeViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.fragment_wallet_detail_list_consume_item, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is RechargeViewHolder) {
            holder.setData(position, data!![position])
        } else if (holder is ConsumeViewHolder) {
            holder.setData(position, data!![position])
        }
    }

    override fun getItemCount(): Int = data?.size ?: 0

    inner class ConsumeViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        //private val context: Context = mView.context
        private val createTime = mView.consumeCreateTime
        private val consumeIcon = mView.consumeIcon
        private val consumeName = mView.consumeName
        private val consumeAmount = mView.consumeAmount

        fun setData(position: Int, record: OrderRecord) {
            if (position == 0) {
                createTime.visibility = View.VISIBLE
                createTime.text = record.formatCreateTime()
            } else {
                createTime.visibility = View.GONE
            }
            when (record.type) {
                -1 -> {
                    consumeIcon.setImageResource(R.drawable.ic_order_withdraw)
                    mView.setOnClickListener {
                        fragment.addFragmentSafely(WithdrawDetailFragment.newInstance(Bundle().apply {
                            putString(Constants.Extra.UUID, record.uuid)
                        }), "withdraw_detail", true,
                                enterAnimation = R.anim.slide_in_up, popExitAnimation = R.anim.slide_out_down)
                    }
                }
                0 -> consumeIcon.setImageResource(R.drawable.ic_order_cost)
                1 -> consumeIcon.setImageResource(R.drawable.ic_order_vip)
                10 -> consumeIcon.setImageResource(R.drawable.ic_order_mibi)
                11 -> consumeIcon.setImageResource(R.drawable.ic_order_mibi)
                12 -> consumeIcon.setImageResource(R.drawable.ic_order_mibi)
            }
            consumeName.text = record.title
            consumeAmount.text = record.price
        }
    }

    inner class RechargeViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        //private val context: Context = mView.context
        private val createTime = mView.rechargeCreateTime
        private val rechargeIcon = mView.rechargeIcon
        private val rechargeName = mView.rechargeName
        private val rechargeAmount = mView.rechargeAmount
        private val rechargeBalance = mView.rechargeBalance
        private val rechargeType = mView.rechargeType

        fun setData(position: Int, record: OrderRecord) {
            if (position == 0) {
                createTime.visibility = View.VISIBLE
                createTime.text = record.formatCreateTime()
            } else {
                createTime.visibility = View.GONE
            }
            when (record.type) {
                -1 -> {
                    rechargeIcon.setImageResource(R.drawable.ic_order_withdraw)
                    mView.setOnClickListener {
                        fragment.addFragmentSafely(WithdrawDetailFragment.newInstance(Bundle().apply {
                            putString(Constants.Extra.UUID, record.uuid)
                        }), "withdraw_detail", true,
                                enterAnimation = R.anim.slide_in_up, popExitAnimation = R.anim.slide_out_down)
                    }
                }
                0 -> {
                    rechargeIcon.setImageResource(R.drawable.ic_order_cost)
//                    mView.setOnClickListener {
//                        fragment.addFragmentSafely(WithdrawDetailFragment.newInstance(Bundle().apply {
//                            putString(Constants.Extra.UUID, record.uuid)
//                        }), "withdraw_detail", true,
//                                enterAnimation = R.anim.slide_in_up, popExitAnimation = R.anim.slide_out_down)
//                    }
                }
                1 -> rechargeIcon.setImageResource(R.drawable.ic_order_vip)
                10 -> rechargeIcon.setImageResource(R.drawable.ic_order_mibi)
                11 -> rechargeIcon.setImageResource(R.drawable.ic_order_mibi)
                12 -> rechargeIcon.setImageResource(R.drawable.ic_order_mibi)
            }
            rechargeName.text = record.title
            rechargeAmount.text = record.price
            rechargeBalance.text = record.info
            rechargeType.text = record.payType
        }
    }
}
