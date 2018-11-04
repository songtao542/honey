package com.snt.phoney.ui.user


import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.snt.phoney.R

/**
 */
class VisitorRecyclerViewAdapter() : RecyclerView.Adapter<VisitorRecyclerViewAdapter.ViewHolder>() {



    init {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_visitor, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


    }

    override fun getItemCount(): Int = 20

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {

    }
}
