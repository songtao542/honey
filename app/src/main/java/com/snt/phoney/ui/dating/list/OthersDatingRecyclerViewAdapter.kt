package com.snt.phoney.ui.dating.list


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.snt.phoney.R

/**
 */
class OthersDatingRecyclerViewAdapter() : RecyclerView.Adapter<OthersDatingRecyclerViewAdapter.ViewHolder>() {


    init {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_othersdating, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

    }

    override fun getItemCount(): Int = 20

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {

    }
}
