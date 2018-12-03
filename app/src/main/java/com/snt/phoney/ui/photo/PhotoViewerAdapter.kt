package com.snt.phoney.ui.photo

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class PhotoViewerAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {

    var urls: List<String>? = null
        set(value) {
            value?.let {
                field = it
                notifyDataSetChanged()
            }
        }

    override fun getItem(position: Int): Fragment {
        return PhotoViewFragment.newInstance(urls!![position])
    }

    override fun getCount(): Int = urls?.size ?: 0

}