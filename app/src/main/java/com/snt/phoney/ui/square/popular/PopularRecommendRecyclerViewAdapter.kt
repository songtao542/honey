package com.snt.phoney.ui.square.popular


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.snt.phoney.R

/**
 */
class PopularRecommendRecyclerViewAdapter() : RecyclerView.Adapter<PopularRecommendRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_popular_recommend, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

    }

    override fun getItemCount(): Int = 40

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {

    }
}
