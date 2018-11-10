package com.snt.phoney.ui.message


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.snt.phoney.R
import kotlinx.android.synthetic.main.fragment_message.view.*

/**
 */
class MessageRecyclerViewAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    init {

    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> 0
            else -> 1
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            0 -> SearchViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.fragment_message_search, parent, false))
            else -> ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.fragment_message, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

    }

    override fun getItemCount(): Int = 50

    inner class SearchViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {

    }

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {

    }
}
