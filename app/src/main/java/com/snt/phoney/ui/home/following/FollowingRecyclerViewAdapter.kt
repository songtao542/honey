package com.snt.phoney.ui.home.following


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.snt.phoney.R

/**
 */
class FollowingRecyclerViewAdapter() : RecyclerView.Adapter<FollowingRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_following, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

    }

    override fun getItemCount(): Int = 50

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {

    }
}
