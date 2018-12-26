package com.snt.phoney.ui.user


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.snt.phoney.R
import com.snt.phoney.base.CommonActivity
import com.snt.phoney.base.Page
import com.snt.phoney.domain.model.User
import com.snt.phoney.extensions.addFragmentSafely
import com.snt.phoney.utils.data.Constants
import kotlinx.android.synthetic.main.fragment_followme.view.*

/**
 */
class FollowMeRecyclerViewAdapter(private val activity: CommonActivity) : RecyclerView.Adapter<FollowMeRecyclerViewAdapter.ViewHolder>() {

    var data: List<User>? = null
        set(value) {
            value?.let {
                field = it
                notifyDataSetChanged()
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_followme, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(data!![position])
    }

    override fun getItemCount(): Int = data?.size ?: 0

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {

        fun setData(user: User) {
            Glide.with(mView).load(user.portrait).apply(RequestOptions().circleCrop()).transition(DrawableTransitionOptions.withCrossFade()).into(mView.head)
            mView.nickname.text = user.nickname
            mView.selfDescription.text = user.introduce
            mView.visitTime.text = "刚刚"
            mView.viewVisitor.setOnClickListener {
                activity?.addFragmentSafely(Page.USER_INFO, "view_user_info", true, argument = Bundle().apply {
                    putParcelable(Constants.Extra.USER, user)
                })
            }
        }
    }
}
