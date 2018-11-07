package com.snt.phoney.ui.dating.list


import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.snt.phoney.R
import com.snt.phoney.extensions.dip
import kotlinx.android.synthetic.main.fragment_dating_item.view.*

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
        val context: Context = mView.context
        val point: Drawable = mView.context.getDrawable(R.drawable.point_primary)

        val datingAddress: TextView = mView.datingAddress
        val datingTime: TextView = mView.datingTime
        val datingJoiner: TextView = mView.datingJoiner

        init {
            val d = context.dip(8)
            point.setBounds(0, 0, d, d)
            datingAddress.setCompoundDrawables(point, null, null, null)
            datingTime.setCompoundDrawables(point, null, null, null)
            datingJoiner.setCompoundDrawables(point, null, null, null)
        }

        fun setData() {

        }
    }
}
