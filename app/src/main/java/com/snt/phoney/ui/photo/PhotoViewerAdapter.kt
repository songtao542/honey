package com.snt.phoney.ui.photo

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class PhotoViewerAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {

    override fun getItem(position: Int): Fragment {
        return PhotoViewFragment.newInstance()
    }

    override fun getCount(): Int {
        return 20
    }

}