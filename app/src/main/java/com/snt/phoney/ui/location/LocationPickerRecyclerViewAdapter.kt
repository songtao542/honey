package com.snt.phoney.ui.location

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.snt.phoney.R
import com.snt.phoney.domain.model.PoiAddress
import kotlinx.android.synthetic.main.fragment_location_sheet_item.view.*

class LocationPickerRecyclerViewAdapter() : RecyclerView.Adapter<LocationPickerRecyclerViewAdapter.ViewHolder>() {

    var data: List<PoiAddress>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount(): Int = data?.size ?: 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.fragment_location_sheet_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(data!![position])
    }

    inner class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        fun setData(address: PoiAddress) {
            view.title.text = address.title
            view.address.text = address.formatAddress
        }
    }
}