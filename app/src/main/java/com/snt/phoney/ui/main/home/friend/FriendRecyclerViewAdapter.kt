package com.snt.phoney.ui.main.home.friend


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.snt.phoney.R
import com.snt.phoney.base.CommonActivity
import com.snt.phoney.base.Page
import com.snt.phoney.domain.model.User
import com.snt.phoney.ui.user.UserActivity
import com.snt.phoney.utils.data.Constants
import kotlinx.android.synthetic.main.fragment_friend.view.*

/**
 */
class FriendRecyclerViewAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    var data: List<User>? = null
        set(value) {
            value?.let {
                field = it
                this@FriendRecyclerViewAdapter.notifyDataSetChanged()
            }
        }


    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> 1
            else -> 1
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            0 -> TagViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.fragment_friend_tag, parent, false))
            else -> ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.fragment_friend, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            holder.setData(data!![position])
        }
    }

    override fun getItemCount(): Int = data?.size ?: 0


    inner class TagViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {

    }

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        private var user: User? = null

        init {
            mView.setOnClickListener {
                mView.context.startActivity(CommonActivity.newIntent<UserActivity>(mView.context, Page.VIEW_USER_INFO, Bundle().apply {
                    putParcelable(Constants.Extra.USER, user)
                }))
            }
        }

        fun setData(user: User) {
            this.user = user
            Glide.with(mView).load(user.portrait).into(mView.image)
            //Glide.with(mView).load(user.portrait).apply(RequestOptions().circleCrop()).transition(DrawableTransitionOptions.withCrossFade()).into(mView.image)
            mView.label.text = user.tag
            mView.renQi.text = "${user.followedSize}"
            mView.lastOnLineTime.text = getUpdateTime(user.updateTime)
            mView.distance.text = getDistance()
            mView.des.text = user.nickname
        }

        private fun getUpdateTime(time: Long): String {
            return "刚刚在线"
        }

        private fun getDistance(): String {
            return mView.context.getString(R.string.distance_of_template, "23km")
        }
    }
}