package com.snt.phoney.ui.dating.list


import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.snt.phoney.R
import com.snt.phoney.base.addFragmentSafely
import com.snt.phoney.domain.model.Dating
import com.snt.phoney.extensions.colorOf
import com.snt.phoney.extensions.dip
import com.snt.phoney.ui.dating.detail.DatingDetailFragment
import com.snt.phoney.utils.data.Constants
import kotlinx.android.synthetic.main.fragment_dating_item.view.*

/**
 */
class DatingListRecyclerViewAdapter(private val fragment: DatingListFragment, private val viewModel: DatingViewModel, private val type: Int) : RecyclerView.Adapter<DatingListRecyclerViewAdapter.ViewHolder>() {

    var data: List<Dating>? = null
        set(value) {
            value?.let {
                field = it
                notifyDataSetChanged()
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_dating_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(data!![position])
    }

    override fun getItemCount(): Int = data?.size ?: 0

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val context: Context = mView.context
        val point: Drawable = mView.context.getDrawable(R.drawable.point_primary)

        private val datingAddress: TextView = mView.datingAddress
        private val datingTime: TextView = mView.datingTime
        private val datingJoiner: TextView = mView.datingJoiner
        private val endingTime: TextView = mView.endingTime
        private val viewApply: TextView = mView.viewApply
        private val datingState: TextView = mView.datingState
        private val cancelDating: TextView = mView.cancelDating

        init {
            val d = context.dip(8)
            point.setBounds(0, 0, d, d)
            datingAddress.setCompoundDrawables(point, null, null, null)
            datingTime.setCompoundDrawables(point, null, null, null)
            datingJoiner.setCompoundDrawables(point, null, null, null)
        }

        fun setData(dating: Dating) {
            datingAddress.text = context.getString(R.string.dating_address_template, dating.city)
            datingTime.text = context.getString(R.string.dating_time_template, dating.formatTime())
            if (type == DatingListFragment.TYPE_PUBLISH) {
                cancelDating.visibility = View.GONE
                datingJoiner.text = context.getString(R.string.dating_join_template,
                        dating.attendNumber.toString())

                viewApply.setOnClickListener {
                    fragment.addFragmentSafely(ApplicantListFragment.newInstance(Bundle().apply {
                        putParcelable(Constants.Extra.DATA, dating)
                    }), "applicants", true)
                }
            } else if (type == DatingListFragment.TYPE_JOINED) {
                datingJoiner.text = context.getString(R.string.dating_with_template, dating.nickname
                        ?: "")
                /**
                 * 0 未开始 1 进行中 2 过期 3 结束 5 取消
                 */
                if (dating.state != 2) {
                    cancelDating.visibility = View.VISIBLE
                    /**
                     * 0 审核中  1 通过  2 拒绝   3 取消
                     */
                    if (dating.applyState == 0) {
                        cancelDating.setText(R.string.under_review)
                    } else if (dating.applyState == 1) {
                        cancelDating.setText(R.string.cancel_dating)
                        cancelDating.setOnClickListener {
                            viewModel.quitDating(dating)
                        }
                    } else if (dating.applyState == 2) {
                        cancelDating.setText(R.string.has_rejected)
                    } else if (dating.applyState == 3) {
                        cancelDating.setText(R.string.has_canceled)
                    }
                }

                viewApply.setText(R.string.view_the_dating)
                viewApply.setOnClickListener {
                    fragment.addFragmentSafely(DatingDetailFragment.newInstance(Bundle().apply {
                        putString(Constants.Extra.UUID, dating.safeUuid)
                    }), "dating_detail", true)
                }
            }

            if (dating.state == 0 || dating.state == 1) {
                datingState.setTextColor(context.colorOf(R.color.colorPrimaryMale))
                datingState.text = context.getString(R.string.on_going)
                endingTime.text = context.getString(R.string.dating_ending_time_template, dating.remainingTime())
            } else if (dating.state == 2) {
                datingState.setTextColor(context.colorOf(R.color.dark_gray))
                val hasOutTime = context.getString(R.string.has_out_of_time)
                datingState.text = hasOutTime
                endingTime.text = context.getString(R.string.dating_ending_time_template, hasOutTime)
            } else if (dating.state == 3) {
                datingState.setTextColor(context.colorOf(R.color.dark_gray))
                val hasFinish = context.getString(R.string.has_finish)
                datingState.text = hasFinish
                endingTime.text = context.getString(R.string.dating_ending_time_template, hasFinish)
            } else if (dating.state == 5) {
                datingState.setTextColor(context.colorOf(R.color.dark_gray))
                val hasCanceled = context.getString(R.string.has_canceled)
                datingState.text = hasCanceled
                endingTime.text = context.getString(R.string.dating_ending_time_template, hasCanceled)
            }


        }
    }
}
