package com.snt.phoney.ui.main.home.friend


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.snt.phoney.R
import com.snt.phoney.base.Page
import com.snt.phoney.domain.model.User
import com.snt.phoney.extensions.startActivity
import com.snt.phoney.ui.user.UserActivity
import com.snt.phoney.utils.DistanceFormat
import com.snt.phoney.utils.TimeFormat
import com.snt.phoney.utils.data.Constants
import kotlinx.android.synthetic.main.fragment_friend.view.*
import kotlinx.android.synthetic.main.fragment_friend_tag.view.*

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
        init {
            mView.moreTag.setOnClickListener {

            }
        }
    }

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        private var user: User? = null

        private val context = mView.context
        private val image = mView.image
        private val label = mView.label
        private val renQi = mView.renQi
        private val lastOnLineTime = mView.lastOnLineTime
        private val distanceView = mView.distance
        private val name = mView.name

        init {
            mView.setOnClickListener {
                mView.context.startActivity<UserActivity>(Page.USER_INFO, Bundle().apply {
                    putParcelable(Constants.Extra.USER, user)
                })
            }
        }

        fun setData(user: User) {
            this.user = user
            Glide.with(mView).load(user.avatar).into(image)
            //Glide.with(mView).load(user.avatar).apply(RequestOptions().circleCrop()).transition(DrawableTransitionOptions.withCrossFade()).into(mView.image)
            label.text = user.tag
            renQi.text = "${user.caredSize}"
            setUpdateTime(user.updateTime)
            setDistance(user.distance)
            name.text = user.nickname
        }

        private fun setUpdateTime(time: Long) {
            lastOnLineTime.text = TimeFormat.format(context, time)
        }

        private fun setDistance(distance: Double) {
            if (distance > 0) {
                distanceView.text = context.getString(R.string.distance_of_template, DistanceFormat.format(context, distance))
            } else {
                distanceView.visibility = View.GONE
            }
        }
    }
}
