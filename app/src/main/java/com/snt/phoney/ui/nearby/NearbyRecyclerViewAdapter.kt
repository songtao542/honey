package com.snt.phoney.ui.nearby


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.snt.phoney.R

/**
 * [RecyclerView.Adapter] that can display a [DummyItem] and makes a call to the
 */
class NearbyRecyclerViewAdapter()
    : RecyclerView.Adapter<NearbyRecyclerViewAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_nearby, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

    }

    override fun getItemCount(): Int = 30

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {

    }
}
