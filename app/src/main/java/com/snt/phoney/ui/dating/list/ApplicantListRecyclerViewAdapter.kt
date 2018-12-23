package com.snt.phoney.ui.dating.list


import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.snt.phoney.R
import com.snt.phoney.domain.model.Applicant
import kotlinx.android.synthetic.main.fragment_dating_applicant_item.view.*

/**
 */
class ApplicantListRecyclerViewAdapter(private val viewModel: DatingViewModel) : RecyclerView.Adapter<ApplicantListRecyclerViewAdapter.ViewHolder>() {

    var data: List<Applicant>? = null
        set(value) {
            value?.let {
                field = it
                notifyDataSetChanged()
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_dating_applicant_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(data!![position])
    }

    override fun getItemCount(): Int = data?.size ?: 0

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val context: Context = mView.context

        private val head = mView.head
        private val name = mView.name
        private val content = mView.content
        private val stateText = mView.stateText
        private val agreeButton = mView.agreeButton
        private val rejectButton = mView.rejectButton

        fun setData(applicant: Applicant) {
            applicant.user?.portrait?.let { portrait ->
                Glide.with(mView).load(portrait).apply(RequestOptions().circleCrop()).transition(DrawableTransitionOptions.withCrossFade()).into(head)
            }
            name.text = applicant.user?.nickname
            content.text = applicant.user?.introduce
            if (applicant.state == 0) {
                stateText.visibility = View.GONE
                agreeButton.visibility = View.VISIBLE
                rejectButton.visibility = View.VISIBLE
                agreeButton.setOnClickListener {
                    viewModel.reviewDating(applicant, 1)
                }
                rejectButton.setOnClickListener {
                    viewModel.reviewDating(applicant, 2)
                }
            } else {
                stateText.visibility = View.VISIBLE
                agreeButton.visibility = View.GONE
                rejectButton.visibility = View.GONE
                when (applicant.state) {
                    1 -> stateText.text = context.getString(R.string.has_agree_h_join)
                    2 -> stateText.text = context.getString(R.string.has_rejected)
                    3 -> stateText.text = context.getString(R.string.has_canceled)
                }
            }
        }
    }
}
