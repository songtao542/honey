package com.snt.phoney.ui.album

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.snt.phoney.R
import com.snt.phoney.domain.model.Photo
import kotlinx.android.synthetic.main.fragment_pay_setting_list_item.view.*

class PaySettingRecyclerViewAdapter(val fragment: Fragment) : RecyclerView.Adapter<PaySettingRecyclerViewAdapter.ViewHolder>() {

    var data: List<Photo>? = null
        set(value) {
            value?.let {
                field = it
                notifyDataSetChanged()
            }
        }

    //val selected = ArrayList<Photo>()
    var selectedPhoto: Photo? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.fragment_pay_setting_list_item, parent, false))
    }

    override fun getItemCount(): Int = data?.size ?: 0

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(data!![position])
    }

    inner class ViewHolder(private val mView: View) : RecyclerView.ViewHolder(mView) {
        private var photo: Photo? = null
        fun setData(photo: Photo) {
            this.photo = photo
            Glide.with(mView.image).load(photo.path).into(mView.image)
            mView.checkbox.isChecked = photo.selected
            mView.checkbox.isClickable = false
            mView.setOnClickListener {
                mView.checkbox.isChecked = true
                selectedPhoto?.selected = false
                photo.selected = true
                selectedPhoto = photo
                notifyDataSetChanged()
            }
        }
    }

}