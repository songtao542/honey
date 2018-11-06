package com.snt.phoney.ui.dating.list


import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.snt.phoney.R

/**
 */
class DatingListRecyclerViewAdapter : RecyclerView.Adapter<DatingListRecyclerViewAdapter.ViewHolder>() {

    init {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_dating_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    }

    override fun getItemCount(): Int = 50

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
             fun setData(){

             }
    }
}
