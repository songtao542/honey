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
class MessageRecyclerViewAdapter() : RecyclerView.Adapter<MessageRecyclerViewAdapter.ViewHolder>() {

    init {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_message, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

    }

    override fun getItemCount(): Int = 50

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {

    }
}
