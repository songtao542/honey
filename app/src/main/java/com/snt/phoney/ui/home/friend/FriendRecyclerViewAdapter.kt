package com.snt.phoney.ui.home.friend


import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.snt.phoney.R
import com.snt.phoney.ui.home.friend.dummy.DummyContent.DummyItem
import com.snt.phoney.ui.user.UserInfoActivity
import com.snt.phoney.ui.user.UserInfoFragment

/**
 */
class FriendRecyclerViewAdapter(private val mValues: List<DummyItem>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    init {

    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            0 -> ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.fragment_friend, parent, false))
            else -> ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.fragment_friend, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        init {
            mView.setOnClickListener { mView.context.startActivity(UserInfoActivity.newIntent(mView.context)) }
        }
    }
}
