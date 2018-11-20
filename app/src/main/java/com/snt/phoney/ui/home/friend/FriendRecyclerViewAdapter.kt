package com.snt.phoney.ui.home.friend


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.snt.phoney.R
import com.snt.phoney.base.CommonActivity
import com.snt.phoney.base.Page

/**
 */
class FriendRecyclerViewAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    init {

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

    }

    override fun getItemCount(): Int = 50


    inner class TagViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {

    }

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        init {
            mView.setOnClickListener { mView.context.startActivity(CommonActivity.newIntent(mView.context, Page.VIEW_USER_INFO)) }
        }
    }
}
